# AWS deployment notes

The Terraform here establishes the core cloud resources. For a polished personal deployment, you should complete the ECS/Fargate service, ALB, IAM roles, Secrets Manager secret attachment and S3 upload step in a dedicated AWS account/project.

The initial repository deliberately separates the application code from cloud provisioning so you can understand each layer.

## Before `terraform apply`

Set:

```bash
export TF_VAR_db_password='a-strong-random-password'
export TF_VAR_jwt_secret='a-long-random-secret-at-least-32-characters'
```

Use a dedicated AWS profile, check `aws sts get-caller-identity`, then run:

```bash
terraform init
terraform plan
terraform apply
```

Do not commit `.tfvars` files containing secrets.
