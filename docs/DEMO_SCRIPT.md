# 5-minute portfolio demo

1. Open the login page and sign in with the seeded demo account.
2. Show the QA dashboard: projects, test cases, failed runs, open defects.
3. Open the sample project and explain the hierarchy: project -> suite -> case -> run -> result.
4. Add a test suite and a test case.
5. Click **Run tests** and show the run moving from queued to completed.
6. Open the defect tracker and create a defect.
7. In GitHub, show the test workflow and explain JUnit, Mockito, Testcontainers and Playwright.
8. Show the Docker Compose file and explain the local architecture.
9. Show the Terraform resources and explain RDS, ECS/Fargate, S3/CloudFront, CloudWatch, Secrets Manager and SQS.
10. Show the GitHub deployment workflow and specifically point out AWS OIDC.

## Strong interview explanation

"I built a QA and test-management platform using Java/Spring Boot and PostgreSQL. Locally, the API and database run in Docker Compose, with a React/TypeScript frontend. I wrote unit tests with JUnit and Mockito, PostgreSQL integration tests with Testcontainers, and browser-level tests with Playwright. The cloud target uses ECS/Fargate, RDS PostgreSQL, S3/CloudFront and CloudWatch. For CI/CD I use GitHub Actions with OIDC to obtain short-lived AWS credentials. I designed SQS as the asynchronous path for scaling test execution into independent workers."
