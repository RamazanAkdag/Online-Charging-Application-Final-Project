variable "name" {
  description = "Lambda function name"
  type        = string
}

variable "lambda_zip_path" {
  description = "Path to the zipped Lambda code"
  type        = string
}

variable "lambda_role_arn" {
  description = "IAM Role ARN for Lambda"
  type        = string
}

variable "target_s3_bucket" {
  description = "Target S3 bucket for backups"
  type        = string
}

variable "efs_access_point_arn" {
  description = "EFS Access Point ARN"
  type        = string
}

variable "subnet_ids" {
  description = "List of subnet IDs for Lambda VPC config"
  type        = list(string)
}

variable "security_group_ids" {
  description = "List of security group IDs for Lambda VPC config"
  type        = list(string)
}
