# Playwright E2E tests (W1 add pet, W2 book visit, role check)

## Run
1. Backend: `./mvnw spring-boot:run` (port 9966, security on)
2. UI: clone spring-petclinic-angular, copy `ui-setup/proxy.conf.json` into it,
   change REST_API_URL in `src/environments/environment*.ts` to `/petclinic/api/`,
   then `npm install && npx ng serve --proxy-config proxy.conf.json`
3. Tests: `cd e2e && npm install && npx playwright install chromium && npx playwright test`

## Why the proxy
The Angular UI has no login. Proxying the API through :4200 makes it same-origin,
so the browser (and Playwright httpCredentials) can answer the HTTP Basic challenge.

## Test users (test data only)
- admin / admin (OWNER_ADMIN, VET_ADMIN, ADMIN)
- vet / admin (VET_ADMIN only)
