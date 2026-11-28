import { test, expect, Page, request } from '@playwright/test';

// Shared record: the pet created in UC-1 (W1) is the pet that gets visits in UC-2 (W2).
const stamp = Date.now().toString().slice(-6);
const PET = `PW-Pet-${stamp}`;
const PET_FIXED = `PW-Fix-${stamp}`;
const PET_EDGE = `PW-Edge-${stamp}-`.padEnd(30, 'x'); // exactly 30 chars (max allowed)
const VISIT1 = `Checkup PW-${stamp}`;
const VISIT2 = `Follow-up PW-${stamp}`;
const VISIT_FUTURE = `Vaccine PW-${stamp}`;

const pad = (n: number) => String(n).padStart(2, '0');
const fmtUi = (d: Date) => `${d.getFullYear()}/${pad(d.getMonth() + 1)}/${pad(d.getDate())}`;
const fmtIso = (d: Date) => `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())}`;
const daysFromNow = (n: number) => { const d = new Date(); d.setDate(d.getDate() + n); return d; };
const yearsAgo = (n: number) => { const d = new Date(); d.setFullYear(d.getFullYear() - n); return d; };

// --- API helpers for postcondition read-back ---
async function api(user: 'admin' | 'vet') {
  return request.newContext({ baseURL: 'http://localhost:4200', httpCredentials: { username: user, password: 'admin' } });
}
async function georgePets(): Promise<any[]> {
  const ctx = await api('admin');
  const res = await ctx.get('/petclinic/api/owners/1');
  expect(res.status()).toBe(200);
  const owner = await res.json();
  await ctx.dispose();
  return owner.pets;
}
const petByName = async (name: string) => (await georgePets()).find((p) => p.name === name);

// --- UI helpers ---
async function openGeorge(page: Page) {
  await page.goto('/petclinic/owners');
  await page.getByRole('button', { name: 'Find Owner' }).click();
  await page.getByRole('link', { name: 'George Franklin' }).click();
  await expect(page).toHaveURL(/\/owners\/1$/);
  await page.waitForLoadState('networkidle');
}
async function openAddPet(page: Page) {
  await openGeorge(page);
  await page.getByRole('button', { name: 'Add New Pet' }).click();
  await expect(page).toHaveURL(/pets\/add/, { timeout: 15000 });
  await expect(page.getByRole('heading', { name: 'Add Pet' })).toBeVisible();
}
async function fillPet(page: Page, name: string, birth: Date, type: string) {
  await page.locator('#name').fill(name);
  await page.locator('#birthDate').fill(fmtUi(birth));
  await page.locator('#birthDate').press('Tab');
  await page.locator('#typeId').selectOption({ label: type });
}
async function openAddVisit(page: Page, petName: string) {
  await openGeorge(page);
  await page.locator(
    `xpath=//td[normalize-space()="${petName}"]/ancestor::table/following::button[normalize-space()="Add Visit"][1]`
  ).click();
  await expect(page.getByRole('heading', { name: 'New Visit' })).toBeVisible();
  await expect(page.getByRole('cell', { name: petName })).toBeVisible(); // wait for pet data to load
}
async function fillVisit(page: Page, date: Date, desc: string) {
  await page.locator('#date').fill(fmtUi(date));
  await page.locator('#date').press('Tab');
  if (desc) await page.locator('#description').fill(desc);
}

test.describe.serial('UC-1 Add pet (W1) and UC-2 Book visit (W2)', () => {
  test.use({ httpCredentials: { username: 'admin', password: 'admin' } });

  // ================= UC-1 =================
  test('UC-1 main: owner admin adds a pet to an owner', async ({ page }) => {
    await openAddPet(page);
    await expect(page.locator('#owner_name')).toHaveValue('George Franklin');
    await fillPet(page, PET, yearsAgo(2), 'dog');
    await page.getByRole('button', { name: 'Save Pet' }).click();
    await expect(page).toHaveURL(/\/owners\/1$/);
    await expect(page.getByText(PET, { exact: true })).toBeVisible();
    const pet = await petByName(PET);                       // postcondition
    expect(pet).toBeTruthy();
    expect(pet.type.name).toBe('dog');
    expect(pet.visits).toHaveLength(0);
  });

  test('UC-1 A1: rejected birth date is corrected, then the pet is saved', async ({ page }) => {
    await openAddPet(page);
    await fillPet(page, PET_FIXED, daysFromNow(10), 'cat');
    await expect(page.getByText('Birth date cannot be in the future')).toBeVisible();
    await expect(page.getByRole('button', { name: 'Save Pet' })).toBeDisabled();
    await page.locator('#birthDate').fill(fmtUi(yearsAgo(1)));
    await page.locator('#birthDate').press('Tab');
    await expect(page.getByText('Birth date cannot be in the future')).toBeHidden();
    await page.getByRole('button', { name: 'Save Pet' }).click();
    await expect(page).toHaveURL(/\/owners\/1$/);
    await expect(page.getByText(PET_FIXED, { exact: true })).toBeVisible();
    expect((await petByName(PET_FIXED))?.type.name).toBe('cat');   // postcondition
  });

  test('UC-1 A2: boundary values accepted (born today, 30-char name, cat)', async ({ page }) => {
    expect(PET_EDGE.length).toBe(30);
    await openAddPet(page);
    await fillPet(page, PET_EDGE, new Date(), 'cat');
    await page.getByRole('button', { name: 'Save Pet' }).click();
    await expect(page).toHaveURL(/\/owners\/1$/);
    await expect(page.getByText(PET_EDGE, { exact: true })).toBeVisible();
    const pet = await petByName(PET_EDGE);                  // postcondition
    expect(pet.birthDate).toBe(fmtIso(new Date()));
  });

  test('UC-1 E1: future birth date is rejected and no pet is created', async ({ page }) => {
    const name = `PW-Bad-${stamp}`;
    await openAddPet(page);
    await fillPet(page, name, daysFromNow(1), 'dog');
    await expect(page.getByText('Birth date cannot be in the future')).toBeVisible();
    await expect(page.getByRole('button', { name: 'Save Pet' })).toBeDisabled();
    await page.getByRole('button', { name: 'Back' }).click();
    await expect(page).toHaveURL(/\/owners\/1$/);
    await expect(page.getByText(name, { exact: true })).toHaveCount(0);
    expect(await petByName(name)).toBeUndefined();         // postcondition
  });

  test('UC-1 E2: birth date older than 50 years is rejected and no pet is created', async ({ page }) => {
    const name = `PW-Old-${stamp}`;
    await openAddPet(page);
    await fillPet(page, name, yearsAgo(51), 'dog');
    await expect(page.getByText('Birth date cannot be older than 50 years')).toBeVisible();
    await expect(page.getByRole('button', { name: 'Save Pet' })).toBeDisabled();
    expect(await petByName(name)).toBeUndefined();         // postcondition
  });

  test('UC-1 E3: VET_ADMIN cannot add a pet (403, no leak, nothing stored)', async () => {
    const name = `PW-Vet-${stamp}`;
    const vet = await api('vet');
    const res = await vet.post('/petclinic/api/owners/1/pets', {
      data: { name, birthDate: fmtIso(yearsAgo(1)), type: { id: 2, name: 'dog' } },
    });
    expect(res.status()).toBe(403);
    expect(await res.text()).not.toContain('AuthorizationDeniedException');
    await vet.dispose();
    expect(await petByName(name)).toBeUndefined();         // postcondition
  });

  // ================= UC-2 =================
  test('UC-2 main: owner admin books a visit for the pet created in UC-1', async ({ page }) => {
    await openAddVisit(page, PET);
    await expect(page.getByRole('cell', { name: PET })).toBeVisible();
    await expect(page.getByRole('cell', { name: 'George Franklin' })).toBeVisible();
    await fillVisit(page, new Date(), VISIT1);
    await page.getByRole('button', { name: 'Add Visit' }).click();
    await expect(page).toHaveURL(/\/owners\/1$/);
    await expect(page.getByText(VISIT1)).toBeVisible();
    const pet = await petByName(PET);                       // postcondition
    expect(pet.visits).toHaveLength(1);
    expect(pet.visits[0].description).toBe(VISIT1);
  });

  test('UC-2 A1: a second visit is booked for the same pet', async ({ page }) => {
    await openAddVisit(page, PET);
    await expect(page.getByText(VISIT1)).toBeVisible();     // previous visit listed on the form
    await fillVisit(page, new Date(), VISIT2);
    await page.getByRole('button', { name: 'Add Visit' }).click();
    await expect(page).toHaveURL(/\/owners\/1$/);
    await expect(page.getByText(VISIT2)).toBeVisible();
    const descs = (await petByName(PET)).visits.map((v: any) => v.description);
    expect(descs).toEqual(expect.arrayContaining([VISIT1, VISIT2]));   // postcondition
    expect(descs).toHaveLength(2);
  });

  test('UC-2 A2: a visit is booked for a future date', async ({ page }) => {
    const when = daysFromNow(14);
    await openAddVisit(page, PET);
    await fillVisit(page, when, VISIT_FUTURE);
    await page.getByRole('button', { name: 'Add Visit' }).click();
    await expect(page).toHaveURL(/\/owners\/1$/);
    await expect(page.getByText(VISIT_FUTURE)).toBeVisible();
    const v = (await petByName(PET)).visits.find((x: any) => x.description === VISIT_FUTURE);
    expect(v.date).toBe(fmtIso(when));                      // postcondition
  });

  test('UC-2 E1: visit without description cannot be submitted, nothing stored', async ({ page }) => {
    const before = (await petByName(PET)).visits.length;
    await openAddVisit(page, PET);
    await fillVisit(page, new Date(), '');
    await expect(page.getByRole('button', { name: 'Add Visit' })).toBeDisabled();
    await page.getByRole('button', { name: 'Back' }).click();
    await expect(page).toHaveURL(/\/owners\/1$/);
    expect((await petByName(PET)).visits).toHaveLength(before);   // postcondition
  });

  test('UC-2 E2: VET_ADMIN cannot book a visit (403, nothing stored)', async () => {
    const pet = await petByName(PET);
    const before = pet.visits.length;
    const vet = await api('vet');
    const res = await vet.post(`/petclinic/api/owners/1/pets/${pet.id}/visits`, {
      data: { date: fmtIso(new Date()), description: `PW-Vet-visit-${stamp}` },
    });
    expect(res.status()).toBe(403);
    await vet.dispose();
    expect((await petByName(PET)).visits).toHaveLength(before);   // postcondition
  });

  test('UC-2 E3: visit through another owner URL is rejected [known defect #9]', async () => {
    test.fail(true, 'Defect #9: addVisitToOwner ignores ownerId, so this currently returns 201');
    const pet = await petByName(PET);
    const admin = await api('admin');
    const res = await admin.post(`/petclinic/api/owners/2/pets/${pet.id}/visits`, {
      data: { date: fmtIso(new Date()), description: `PW-WrongOwner-${stamp}` },
    });
    await admin.dispose();
    expect(res.status()).toBe(404);
  });
});
