variable "ami_id" {
  type = string
}

variable "instance_type" {
  type = string
}

variable "key_name" {
  type = string
}

variable "security_group_ids" {
  type    = list(string)
  default = []
}

variable "volume_size" {
  type = number
}

variable "instance_name" {
  type = string
}
