resource "aws_lambda_function" "this" {
  function_name = var.name
  role          = var.lambda_role_arn
  handler       = "index.handler"
  runtime       = "python3.9"
  filename      = var.lambda_zip_path
  timeout       = 60

  environment {
    variables = {
      TARGET_BUCKET = var.target_s3_bucket
    }
  }

  vpc_config {
    subnet_ids         = var.subnet_ids
    security_group_ids = var.security_group_ids
  }

  file_system_config {
    arn              = var.efs_access_point_arn
    local_mount_path = "/mnt/efs"
  }
}
