local:
	docker compose up --build

test:
	cd backend && mvn -B test

frontend-build:
	cd frontend && npm install && npm run build

e2e:
	cd test-runner && npm install && npx playwright install chromium && npm test
