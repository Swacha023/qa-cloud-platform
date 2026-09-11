# Verafin interview talking points

## Object-oriented software development

The backend is a layered Spring Boot application. Controllers own HTTP concerns, services own business logic, repositories own persistence, and JPA entities model domain relationships.

## PostgreSQL

The data model is relational: projects own suites, suites own cases, runs own results, and projects own defects. The dashboard can calculate pass rates using PostgreSQL aggregate queries.

## Automated testing

- JUnit 5 for service-level behavior
- Mockito for isolated unit tests
- Testcontainers for PostgreSQL-backed integration tests
- Playwright for browser-level user journeys

## AWS/cloud

The target architecture places PostgreSQL on RDS, application containers on ECS/Fargate, static frontend assets behind CloudFront/S3, and logs in CloudWatch. Test-run work can be moved to SQS + an independent worker.

## Distributed computing

The local profile keeps test execution in-process for developer convenience. The cloud profile turns a test run into a queue message so workers can consume tasks asynchronously. This means the API does not have to hold an HTTP request open while hundreds of browser tests execute.

## CI/CD

Every push and pull request runs automated tests. The deployment workflow uses GitHub OIDC so AWS access is based on short-lived credentials rather than permanent AWS keys stored as GitHub secrets.
