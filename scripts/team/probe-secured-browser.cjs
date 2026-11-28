// Phase 1.2 environment probe using the team lockfile. The assessed use cases have their own scope.
const assert = require('node:assert/strict');
const { randomUUID } = require('node:crypto');
const { chromium, errors, request } = require('../../e2e/node_modules/playwright');

const ui = 'http://localhost:4200';
const backend = 'http://localhost:9966';
const api = '/petclinic/api';
const mode = process.argv[2];

function record(event, details) {
  console.log(JSON.stringify({ time: new Date().toISOString(), event, ...details }));
}

function credentials(username) {
  const password = process.env[username === 'admin' ? 'PETCLINIC_LOCAL_ADMIN_PASSWORD' : 'PETCLINIC_LOCAL_VET_PASSWORD'];
  assert.ok(password, `Set the local synthetic ${username} password in the session.`);
  return { username, password };
}

async function directOrigin(browser) {
  const context = await browser.newContext({ baseURL: ui });
  try {
    const page = await context.newPage();
    const network = await context.newCDPSession(page);
    const candidate = new Map();
    const observed = new Map();
    network.on('Network.requestWillBeSent', ({ requestId, request: sent }) => {
      if (sent.url.startsWith(backend + api)) {
        const entry = { method: sent.method, url: sent.url };
        candidate.set(requestId, entry);
        const earlierStatus = observed.get(requestId);
        if (earlierStatus) record('wire response', { ...entry, ...earlierStatus });
      }
    });
    network.on('Network.responseReceivedExtraInfo', ({ requestId, statusCode, headers }) => {
      const cors = Object.entries(headers).find(([key]) => key.toLowerCase() === 'access-control-allow-origin');
      const result = { status: statusCode, allowOrigin: cors?.[1] ?? null };
      observed.set(requestId, result);
      if (candidate.has(requestId)) record('wire response', { ...candidate.get(requestId), ...result });
    });
    network.on('Network.loadingFailed', ({ requestId, errorText, corsErrorStatus }) => {
      if (candidate.has(requestId)) record('wire request failed', { ...candidate.get(requestId), errorText, corsErrorStatus });
    });
    page.on('console', (message) => {
      if (message.type() === 'error' && /CORS|ERR_FAILED/.test(message.text())) {
        record('browser console', { message: message.text() });
      }
    });
    await network.send('Network.enable');
    const navigation = await page.goto('/petclinic/owners');
    record('Angular navigation', { url: page.url(), status: navigation.status() });
    const uiRequest = page.waitForEvent('requestfinished', (sent) => sent.url().startsWith(backend + api))
      .then(async (sent) => ({ method: sent.method(), url: sent.url(), http: (await sent.response())?.status() }));
    const uiFailure = page.waitForEvent('requestfailed', (sent) => sent.url().startsWith(backend + api))
      .then((sent) => ({ method: sent.method(), url: sent.url(), failure: sent.failure()?.errorText }));
    await page.getByRole('button', { name: 'Find Owner' }).click();
    record('Angular owner search', await Promise.race([uiRequest, uiFailure]));

    // A JSON request forces a browser preflight. Empty, anonymous input cannot create a Pet.
    const preflight = await page.evaluate(async (url) => {
      try {
        const response = await fetch(url, {
          method: 'POST', credentials: 'omit', headers: { 'Content-Type': 'application/json' }, body: '{}',
        });
        return { status: response.status };
      } catch (error) {
        return { failure: String(error) };
      }
    }, `${backend}${api}/owners/1/pets`);
    record('anonymous JSON preflight probe', preflight);
  } finally {
    await context.close();
  }
}

async function readJson(client, path) {
  const response = await client.get(path);
  record('independent API GET', { url: response.url(), status: response.status() });
  assert.equal(response.status(), 200, path);
  assert.match(response.headers()['content-type'], /json/i);
  return response.json();
}

async function afterBrowserGet(page, path, action) {
  const [response] = await Promise.all([
    page.waitForResponse((item) => item.url() === ui + path && item.request().method() === 'GET' && item.status() !== 401),
    action(),
  ]);
  await response.finished();
  record('browser GET', { url: response.url(), status: response.status() });
  assert.equal(response.status(), 200, path);
}

async function becomesVisible(locator) {
  try {
    await locator.waitFor({ state: 'visible', timeout: 1500 });
    return true;
  } catch (error) {
    if (!(error instanceof errors.TimeoutError)) throw error;
    return false;
  }
}

async function createFromUi(page, path, button) {
  const [response] = await Promise.all([
    page.waitForResponse((item) => item.url() === ui + path && item.request().method() === 'POST' && item.status() !== 401),
    page.getByRole('button', { name: button, exact: true }).click(),
  ]);
  const payload = response.request().postDataJSON();
  const body = await response.text();
  record('browser POST', {
    url: response.url(), status: response.status(), fields: Object.keys(payload).sort(),
    name: payload.name, type: payload.type, birthDate: payload.birthDate, date: payload.date,
    description: payload.description, ownerId: payload.owner?.id, petId: payload.pet?.id,
    location: response.headers().location, response: body,
  });
  assert.equal(response.status(), 201, path);
  return JSON.parse(body);
}

async function proxiedWorkflows(browser) {
  const ownerPath = `${api}/owners/1`;
  const admin = credentials('admin');
  const context = await browser.newContext({ baseURL: ui, httpCredentials: { ...admin, origin: ui } });
  const readback = await request.newContext({
    baseURL: backend,
    httpCredentials: { ...admin, origin: backend, send: 'always' },
    maxRedirects: 0,
  });
  try {
    const dateOfBirth = '2024-06-15';
    const visitDate = new Date().toISOString().slice(0, 10);
    const suffix = randomUUID().replaceAll('-', '').slice(0, 12);
    const name = `Runtime-${suffix}`;
    const description = `Runtime checkup ${suffix}`;
    const page = await context.newPage();
    await page.goto('/petclinic/owners');
    await page.getByRole('button', { name: 'Find Owner' }).click();
    await afterBrowserGet(page, ownerPath, () => page.getByRole('link', { name: 'George Franklin', exact: true }).click());
    record('owner detail after GET', { url: page.url(), name: await page.locator('.ownerFullName').textContent() });
    await afterBrowserGet(page, ownerPath, () => page.getByRole('button', { name: 'Add New Pet' }).click());
    await page.getByRole('heading', { name: 'Add Pet', exact: true }).waitFor();
    const ownerInput = page.getByLabel('Owner', { exact: true });
    record('W1 form before typing', { ownerInput: await ownerInput.inputValue() });
    await page.getByLabel('Name', { exact: true }).fill(name);
    await page.getByLabel('Birth Date', { exact: true }).fill(dateOfBirth.replaceAll('-', '/'));
    await page.getByLabel('Birth Date', { exact: true }).press('Tab');
    await page.getByLabel('Type', { exact: true }).selectOption({ label: 'dog' });
    record('W1 form after typing', { ownerInput: await ownerInput.inputValue() });
    const pet = await createFromUi(page, `${ownerPath}/pets`, 'Save Pet');
    assert.ok(Number.isInteger(pet.id) && pet.id > 0);
    const petPath = `${ownerPath}/pets/${pet.id}`;
    await page.waitForURL('**/owners/1');
    const petPanel = page.locator('app-pet-list').filter({ has: page.getByText(name, { exact: true }) });
    const storedPet = await readJson(readback, petPath);
    const owner = await readJson(readback, ownerPath);
    assert.equal(storedPet.id, pet.id);
    assert.equal(storedPet.ownerId, owner.id);
    assert.equal(owner.id, 1);
    assert.equal(storedPet.name, name);
    assert.equal(storedPet.birthDate, dateOfBirth);
    assert.equal(storedPet.type.name, 'dog');
    assert.equal(owner.pets.filter((item) => item.id === pet.id && item.name === name).length, 1);
    record('W1 readback', { storedPet, ownerId: owner.id, ownerPetIds: owner.pets.map((item) => item.id) });

    const petVisible = await becomesVisible(petPanel.getByText(name, { exact: true }));
    record('W1 owner detail result', { url: page.url(), pet: name, visible: petVisible });
    const visitPage = `/petclinic/pets/${pet.id}/visits/add`;
    const petDetailPath = `${api}/pets/${pet.id}`;
    const openVisitForm = (navigate) =>
      afterBrowserGet(page, ownerPath, () => afterBrowserGet(page, petDetailPath, navigate));
    if (petVisible) {
      await openVisitForm(() => petPanel.getByRole('button', { name: 'Add Visit', exact: true }).click());
    } else {
      // Confirm the rendered owner list, then address the Visit UI using the verified Pet ID.
      await page.goto('/petclinic/owners');
      await page.getByText(name, { exact: true }).waitFor({ timeout: 5000 });
      record('W1 owner list result', { url: page.url(), pet: name, visible: true });
      await openVisitForm(() => page.goto(visitPage));
    }
    record('W2 form navigation', { url: page.url(), petId: pet.id });
    await page.getByRole('heading', { name: 'New Visit', exact: true }).waitFor();
    await page.getByLabel('Date', { exact: true }).fill(visitDate.replaceAll('-', '/'));
    await page.getByLabel('Date', { exact: true }).press('Tab');
    await page.getByLabel('Description', { exact: true }).fill(description);
    await page.getByRole('cell', { name, exact: true }).waitFor({ timeout: 5000 });
    await page.getByRole('cell', { name: 'George Franklin', exact: true }).waitFor({ timeout: 5000 });
    record('W2 selected Pet displayed', { pet: name, owner: 'George Franklin' });
    const visit = await createFromUi(page, `${petPath}/visits`, 'Add Visit');
    assert.ok(Number.isInteger(visit.id) && visit.id > 0);
    await page.waitForURL('**/owners/1');
    const storedVisit = await readJson(readback, `${api}/visits/${visit.id}`);
    const petAfterVisit = await readJson(readback, petPath);
    assert.equal(storedVisit.id, visit.id);
    assert.equal(storedVisit.petId, pet.id);
    assert.equal(storedVisit.description, description);
    assert.equal(storedVisit.date, visitDate);
    assert.equal(petAfterVisit.visits.filter((item) => item.id === visit.id).length, 1);
    record('W2 readback', { storedVisit, ownerId: petAfterVisit.ownerId, petVisitIds: petAfterVisit.visits.map((item) => item.id) });
    const visitVisible = await becomesVisible(petPanel.getByText(description, { exact: true }));
    record('W2 owner detail result', { url: page.url(), pet: name, description, visible: visitVisible });
    if (!visitVisible) {
      await openVisitForm(() => page.goto(visitPage));
      await page.getByLabel('Description', { exact: true }).fill('Check previous visits');
      await page.getByRole('cell', { name: description, exact: true }).waitFor({ timeout: 5000 });
      record('W2 previous visits result', { url: page.url(), pet: name, description, visible: true });
    }
  } finally {
    await readback.dispose();
    await context.close();
  }

  const vet = await browser.newContext({ baseURL: ui, httpCredentials: { ...credentials('vet'), origin: ui } });
  try {
    const page = await vet.newPage();
    await page.goto('/petclinic/owners');
    const [denied] = await Promise.all([
      page.waitForResponse((item) => item.url().startsWith(`${ui}${api}/owners`) && item.status() !== 401),
      page.getByRole('button', { name: 'Find Owner' }).click(),
    ]);
    const ownerLinks = await page.getByRole('link', { name: 'George Franklin', exact: true }).count();
    record('vet browser owner search', { url: denied.url(), status: denied.status(), ownerLinks });
    assert.equal(denied.status(), 403);
    assert.equal(ownerLinks, 0);
  } finally {
    await vet.close();
  }
}

async function main() {
  assert.ok(['direct', 'proxy'].includes(mode), 'Supply direct or proxy.');
  const browser = await chromium.launch({ timeout: 15000 });
  try {
    record('runtime', { mode, ui, backend, node: process.version, chromium: browser.version() });
    if (mode === 'direct') await directOrigin(browser);
    else await proxiedWorkflows(browser);
  } finally {
    await browser.close();
  }
}

main().catch((error) => {
  console.error(error);
  process.exitCode = 1;
});
