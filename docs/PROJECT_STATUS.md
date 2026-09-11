# Project status

## Included now

- Complete source repository layout
- React/TypeScript frontend
- Spring Boot REST API
- PostgreSQL domain model
- JWT login
- Seeded demo data
- Project/suite/case/run/defect workflows
- Local automated test-run simulation
- JUnit + Mockito test
- Testcontainers integration-test skeleton
- Playwright end-to-end tests
- Dockerfiles + Docker Compose
- GitHub Actions test and deployment workflows
- Terraform networking, RDS, ECS/Fargate, ALB, ECR, S3/CloudFront, Secrets Manager, CloudWatch and SQS resources
- Windows PowerShell setup scripts
- Interview/demo documentation

## Requires your computer/account

- Installing Docker, Maven, Node.js and Terraform
- Running the local containers
- Connecting the repository to GitHub
- Creating/configuring AWS IAM OIDC trust
- Supplying your AWS environment variables and secrets
- Running Terraform in your AWS account
- Setting the final production frontend API URL

## Intentional boundary

The local build is fully self-contained and uses an in-process deterministic runner so you can develop without AWS. The cloud architecture includes an SQS queue for the distributed worker path; wiring a worker to your AWS account is the final deployment-specific step rather than pretending it can be performed without your account access.
