import { test, expect } from '@playwright/test';

test('demo user can sign in and view the dashboard', async ({ page }) => {
  await page.goto('/');

  await expect(
    page.getByText('QACloud', { exact: true })
  ).toBeVisible();

  await page.getByRole('button', { name: 'Sign in' }).click();

  await expect(
    page.getByText('QA Dashboard', { exact: true })
  ).toBeVisible();

  await expect(
    page.getByRole('heading', { name: 'Projects', exact: true })
  ).toBeVisible();
});