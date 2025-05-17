resource "aws_lambda_function" "this" {
  function_name = var.name
  role          = aws_iam_role.lambda_exec.arn
  handler       = "index.handler"
  runtime       = "python3.9"

  filename         = data.archive_file.lambda_zip.output_path
  source_code_hash = data.archive_file.lambda_zip.output_base64sha256


  file_system_config {
    arn              = var.efs_access_point_arn
    local_mount_path = "/mnt/efs"
  }

  environment {
    variables = {
      TARGET_BUCKET = var.target_s3_bucket
    }
  }

  timeout = 60
}

resource "aws_iam_role" "lambda_exec" {
  name = "${var.name}-exec"
  assume_role_policy = jsonencode({
    Version = "2012-10-17",
    Statement = [{
      Action    = "sts:AssumeRole",
      Effect    = "Allow",
      Principal = {
        Service = "lambda.amazonaws.com"
      }
    }]
  })
}

resource "aws_iam_role_policy_attachment" "lambda_policy" {
  role       = aws_iam_role.lambda_exec.name
  policy_arn = "arn:aws:iam::aws:policy/AWSLambdaFullAccess"
}

data "archive_file" "lambda_zip" {
  type        = "zip"
  source_file = "${path.module}/index.py"
  output_path = "${path.module}/efs_backup_lambda.zip"
}

