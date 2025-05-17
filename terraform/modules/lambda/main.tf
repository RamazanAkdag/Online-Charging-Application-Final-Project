resource "aws_lambda_function" "this" {
  function_name = var.name
  role          = aws_iam_role.lambda_exec.arn
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

resource "aws_iam_role" "lambda_exec" {
  name = "${var.name}-exec"

  assume_role_policy = jsonencode({
    Version = "2012-10-17",
    Statement = [{
      Action = "sts:AssumeRole"
      Effect = "Allow"
      Principal = {
        Service = "lambda.amazonaws.com"
      }
    }]
  })
}

resource "aws_iam_role_policy_attachment" "lambda_policy" {
  role       = aws_iam_role.lambda_exec.name
  policy_arn = "arn:aws:iam::aws:policy/AWSLambda_FullAccess"
}

resource "aws_iam_role_policy_attachment" "lambda_policy_base" {
  role       = aws_iam_role.lambda_exec.name
  policy_arn = "arn:aws:iam::aws:policy/AWSLambda_FullAccess"
}

resource "aws_iam_role_policy_attachment" "lambda_policy_efs" {
  role       = aws_iam_role.lambda_exec.name
  policy_arn = "arn:aws:iam::aws:policy/AmazonElasticFileSystemClientFullAccess"
}

resource "aws_iam_role_policy_attachment" "lambda_policy_vpc" {
  role       = aws_iam_role.lambda_exec.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AWSLambdaVPCAccessExecutionRole"
}

