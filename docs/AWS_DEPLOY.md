# AWS deployment guide

This is the part you must perform on your computer because it requires access to your AWS account and GitHub repository. Never paste AWS passwords, secret keys, database passwords, or tokens into chat or commit them to Git.

## 1. Install tools

On Windows install:

- AWS CLI v2
- Terraform 1.8+
- Git
- Docker Desktop
- JDK 21
- Maven 3.9+
- Node.js 22+

Run:

```powershell
aws --version
terraform version
java -version
mvn -version
node -v
npm -v
docker --version
```

## 2. Configure AWS CLI

Use an AWS profile that you control:

```powershell
aws configure --profile qa-cloud
$env:AWS_PROFILE = "qa-cloud"
aws sts get-caller-identity
```

Do not create long-lived AWS access keys for GitHub Actions. The deployment workflow is written for GitHub OIDC.

## 3. Create the GitHub repository

Create an empty GitHub repository, then from the project root:

```powershell
git init
git add .
git commit -m "Initial QA Cloud Platform"\ngit branch -M main\ngit remote add origin <YOUR_GITHUB_REPOSITORY_URL>
git push -u origin main
```

## 4. Create the deployment role for GitHub Actions

Create an IAM OIDC provider for:

```text
https://token.actions.githubusercontent.com
```

Use audience:

```text
sts.amazonaws.com
```

Create an IAM role whose trust policy is restricted to your repository and branch/environment. The GitHub documentation has the current OIDC trust pattern and explains why this avoids long-lived GitHub secrets.

Then copy the role ARN. In GitHub:

Settings -> Environments -> New environment -> production

Add repository/environment variable:

```text
AWS_DEPLOY_ROLE_ARN=<role ARN>
AWS_REGION=ca-central-1
PROJECT_NAME=qa-cloud-platform
```

Add secret values:

```text
DB_PASSWORD=<strong random database password>
JWT_SECRET=<long random string, 32+ characters>
```

## 5. First AWS deployment

The infrastructure uses:

- ECS/Fargate for the Spring Boot API
- RDS PostgreSQL
- S3 + CloudFront for the React frontend
- ECR for the API image
- CloudWatch logs
- Secrets Manager
- SQS queue for the distributed test-run path

From `infrastructure/terraform` you can inspect the plan before allowing resources to be created:

```powershell
terraform init
terraform validate
terraform plan -var='project_name=qa-cloud-platform' -var='aws_region=ca-central-1' -var='db_password=REPLACE_ME' -var='jwt_secret=REPLACE_ME'
```

For normal CI/CD, let the GitHub deployment workflow pass the secret variables rather than putting them in a `.tfvars` file.

## 6. Frontend API routing after deployment

The CloudFront distribution proxies `/api/*` to the ALB, so the production frontend uses `VITE_API_URL=/api`. The deployment workflow already sets this and uploads the static build. Set these GitHub variables after the first infrastructure creation:

```text
FRONTEND_BUCKET=<terraform frontend_bucket output>
CLOUDFRONT_DISTRIBUTION_ID=<CloudFront distribution ID>
```

## 7. Verify

Check:

```text
GET <api_url>/actuator/health
```

Then open the CloudFront domain and sign in with the seeded demo account only if this is still a disposable demo environment.

## 8. Cost cleanup

When you're not demonstrating the app, destroy the disposable environment:

```powershell
cd infrastructure/terraform
terraform destroy
```

Check the AWS billing console after cleanup.
