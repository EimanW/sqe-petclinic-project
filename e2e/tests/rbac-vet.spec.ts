import { test, expect } from '@playwright/test';

test.describe('Role check: VET_ADMIN cannot use owner workflows', () => {
  test.use({ httpCredentials: { username: 'vet', password: 'admin' } });

  test('vet gets 403 (not 500) when the UI loads owners', async ({ page }) => {
    const ownersCall = page.waitForResponse(
      (r) => r.url().includes('/petclinic/api/owners') && r.request().method() === 'GET' && r.status() !== 401
    );
    await page.goto('/petclinic/owners');
    await page.getByRole('button', { name: 'Find Owner' }).click();
    const res = await ownersCall;
    expect(res.status()).toBe(403);
    await expect(page.getByRole('link', { name: 'George Franklin' })).toHaveCount(0);
  });
});
