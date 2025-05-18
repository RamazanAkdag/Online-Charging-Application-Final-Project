resource "aws_iam_role" "this" {
  name = var.role_name

  assume_role_policy = var.assume_role_policy_json
}

resource "aws_iam_role_policy_attachment" "attachments" {
  for_each = toset(var.policy_arns)

  role       = aws_iam_role.this.name
  policy_arn = each.value
}
