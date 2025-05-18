variable "role_name" {
  description = "Name of the IAM Role"
  type        = string
}

variable "assume_role_policy_json" {
  description = "IAM Trust Relationship JSON"
  type        = string
}

variable "policy_arns" {
  description = "List of policy ARNs to attach"
  type        = list(string)
}
