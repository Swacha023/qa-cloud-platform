# QA Cloud Platform

A cloud-ready QA and test management platform built to demonstrate software engineering, PostgreSQL, automated testing, AWS, Docker, CI/CD and distributed test execution.

## What it does

- Project, test-suite and test-case management
- Test-run tracking with pass/fail/skip results
- Defect tracking with severity, status and assignment
- QA dashboard with live aggregate statistics
- JWT-based authentication for the demo application
- Playwright end-to-end test suite
- JUnit/Mockito unit tests and Testcontainers PostgreSQL integration tests
- Docker Compose local development
- AWS deployment target: S3 + CloudFront, ECS/Fargate, RDS PostgreSQL, SQS, Secrets Manager, CloudWatch and ECR
- GitHub Actions CI/CD with AWS OIDC
- Terraform infrastructure definitions

## Architecture

```text
Browser
  |
  +--> CloudFront --> S3 (React static app)
  |
  +--> HTTPS --> Application Load Balancer --> ECS/Fargate (Spring Boot API)
                                            |
                                            +--> RDS PostgreSQL
                                            +--> S3 (attachments, optional)
                                            +--> SQS --> Test Worker (Playwright)
                                            +--> Secrets Manager
                                            +--> CloudWatch

GitHub --> GitHub Actions --> tests --> ECR --> ECS
                    |
                    +--> AWS via OIDC (short-lived credentials)
```

## Technology stack

- Frontend: React + TypeScript + Vite
- Backend: Java 21 + Spring Boot 3.5.16 + Spring Security + JPA/Hibernate
- Database: PostgreSQL 17
- Test automation: JUnit 5 + Mockito + Testcontainers + Playwright
- Infrastructure: Docker, AWS, Terraform
- CI/CD: GitHub Actions

## Prerequisites

Install these on your computer:

1. Git
2. JDK 21
3. Maven 3.9+
4. Node.js 22+
5. Docker Desktop
6. AWS CLI v2 (only for AWS deployment)
7. Terraform 1.8+ (only for AWS deployment)

## 1. Run locally

```bash
cd qa-test-platform
cp .env.example .env
cp backend/.env.example backend/.env
cp frontend/.env.example frontend/.env

docker compose up --build
```

Open:

- Frontend: http://localhost:5173
- API: http://localhost:8080
- API health: http://localhost:8080/actuator/health

Demo credentials:

- Email: `demo@qacloud.local`
- Password: `ChangeMe123!`

These credentials are for local demonstration only. Change them before publishing a deployed environment.

## 2. Run backend tests

Use Maven 3.9+ directly from your terminal.

```bash
cd backend
mvn test
```

The integration test uses Testcontainers and therefore needs Docker running.

## 3. Run frontend tests

```bash
cd frontend
npm install
npm run build
```

## 4. Run Playwright end-to-end tests

Start the application first, then:

```bash
cd test-runner
npm install
npx playwright install
npm test
```

The test runner defaults to `http://localhost:5173`.

## 5. Development workflow

Recommended order:

1. Build and verify the local application.
2. Push the repository to GitHub.
3. Enable GitHub Actions.
4. Apply Terraform infrastructure in a dedicated AWS account/project.
5. Configure GitHub OIDC trust for the repository/branch.
6. Set GitHub environment variables required by the deploy workflow.
7. Deploy the frontend to S3/CloudFront and the API to ECS/Fargate.
8. Turn on the SQS worker after the basic deployment is healthy.

## AWS cost safety

AWS resources can incur charges. Start with the smallest suitable RDS/ECS sizing, configure billing alerts, and destroy test infrastructure when you are finished.

## Portfolio presentation

For a Verafin software-development application, emphasize:

- Java/Spring Boot object-oriented architecture
- PostgreSQL schema and real relational queries
- JUnit/Mockito/Testcontainers/Playwright automated testing
- Dockerized services
- AWS RDS/ECS/S3/CloudWatch/SQS
- GitHub Actions CI/CD
- AWS OIDC instead of long-lived AWS access keys
- Terraform infrastructure-as-code

## Important implementation note

The local profile runs the test runner directly so the whole product works on one machine. The AWS profile is designed to move test execution behind SQS and an independent worker service. That gives you a clean progression from a simple local architecture to a distributed cloud architecture without making local development unnecessarily difficult.
