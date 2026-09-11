variable "aws_region" {
  type    = string
  default = "ca-central-1"
}

variable "project_name" {
  type    = string
  default = "qa-cloud-platform"
}

variable "image_tag" {
  type    = string
  default = "latest"
}

variable "db_username" {
  type      = string
  default   = "qa_user"
  sensitive = true
}

variable "db_password" {
  type      = string
  sensitive = true
}

variable "jwt_secret" {
  type      = string
  sensitive = true
}

variable "worker_token" {
  type      = string
  sensitive = true
}
