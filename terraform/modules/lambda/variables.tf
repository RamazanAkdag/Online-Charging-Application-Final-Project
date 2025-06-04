variable "name" {
  description = "Lambda function name"
  type        = string
}

variable "lambda_zip_path" {
  description = "Path to the zipped Lambda function code"
  type        = string
}

variable "efs_access_point_arn" {
  description = "ARN of the EFS access point"
  type        = string
}

variable "target_s3_bucket" {
  description = "Target S3 bucket name"
  type        = string
}

variable "subnet_ids" {
  description = "List of subnet IDs for Lambda to run in a VPC"
  type        = list(string)
}

variable "security_group_ids" {
  description = "List of security group IDs for Lambda VPC access"
  type        = list(string)
}
