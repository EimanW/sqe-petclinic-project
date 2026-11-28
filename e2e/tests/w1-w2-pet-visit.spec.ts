import { test, expect, Page } from '@playwright/test';

// Shared record across W1 and W2: the same pet is created in W1 and gets a visit in W2.
const stamp = Date.now().toString().slice(-6);
const petName = `PW-Pet-${stamp}`;
const visitDesc = `Annual checkup PW-${stamp}`;

function fmt(d: Date): string {
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const dd = String(d.getDate()).padStart(2, '0');
  return `${d.getFullYear()}/${mm}/${dd}`;
}

async function openGeorge(page: Page) {
  await page.goto('/petclinic/owners');
  await page.getByRole('button', { name: 'Find Owner' }).click();
  await page.getByRole('link', { name: 'George Franklin' }).click();
  await expect(page).toHaveURL(/\/owners\/1$/);
  await page.waitForLoadState('networkidle');
}

test.describe.serial('W1 + W2 as OWNER_ADMIN', () => {
  test.use({ httpCredentials: { username: 'admin', password: 'admin' } });

  test('W1: owner admin adds a new pet to an owner', async ({ page }) => {
    await openGeorge(page);
    await page.getByRole('button', { name: 'Add New Pet' }).click();
    await expect(page).toHaveURL(/pets\/add/, { timeout: 15000 });
    await expect(page.getByRole('heading', { name: 'Add Pet' })).toBeVisible();
    await expect(page.locator('#owner_name')).toHaveValue('George Franklin');

    const birth = new Date();
    birth.setFullYear(birth.getFullYear() - 2);
    await page.locator('#name').fill(petName);
    await page.locator('#birthDate').fill(fmt(birth));
    await page.locator('#birthDate').press('Tab');
    await page.locator('#typeId').selectOption({ label: 'dog' });

    await page.getByRole('button', { name: 'Save Pet' }).click();
    await expect(page).toHaveURL(/\/owners\/1$/);
    await expect(page.getByText(petName, { exact: true })).toBeVisible();
  });

  test('W1 alt: future birth date is rejected and Save is disabled', async ({ page }) => {
    await openGeorge(page);
    await page.getByRole('button', { name: 'Add New Pet' }).click();
    await expect(page).toHaveURL(/pets\/add/, { timeout: 15000 });

    const future = new Date();
    future.setDate(future.getDate() + 10);
    await page.locator('#name').fill(`PW-Future-${stamp}`);
    await page.locator('#birthDate').fill(fmt(future));
    await page.locator('#birthDate').press('Tab');
    await page.locator('#typeId').selectOption({ label: 'dog' });

    await expect(page.getByRole('button', { name: 'Save Pet' })).toBeDisabled();
  });

  test('W2: owner admin books a visit for the pet created in W1', async ({ page }) => {
    await openGeorge(page);
    const addVisit = page.locator(
      `xpath=//td[normalize-space()="${petName}"]/ancestor::table/following::button[normalize-space()="Add Visit"][1]`
    );
    await addVisit.click();

    await expect(page.getByRole('heading', { name: 'New Visit' })).toBeVisible();
    await expect(page.getByRole('cell', { name: petName })).toBeVisible();
    await expect(page.getByRole('cell', { name: 'George Franklin' })).toBeVisible();

    await page.locator('#date').fill(fmt(new Date()));
    await page.locator('#date').press('Tab');
    await page.locator('#description').fill(visitDesc);
    await page.getByRole('button', { name: 'Add Visit' }).click();

    await expect(page).toHaveURL(/\/owners\/1$/);
    await expect(page.getByText(visitDesc)).toBeVisible();
  });

  test('W2 alt: visit without description cannot be submitted', async ({ page }) => {
    await openGeorge(page);
    const addVisit = page.locator(
      `xpath=//td[normalize-space()="${petName}"]/ancestor::table/following::button[normalize-space()="Add Visit"][1]`
    );
    await addVisit.click();
    await page.locator('#date').fill(fmt(new Date()));
    await page.locator('#date').press('Tab');
    await expect(page.getByRole('button', { name: 'Add Visit' })).toBeDisabled();
  });
});
