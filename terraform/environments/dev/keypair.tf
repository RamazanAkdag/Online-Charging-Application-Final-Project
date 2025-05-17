resource "tls_private_key" "ocs_key" {
  algorithm = "RSA"
  rsa_bits  = 4096
}

resource "aws_key_pair" "ocs_key" {
  key_name   = "ocs-key"
  public_key = tls_private_key.ocs_key.public_key_openssh
}

resource "local_file" "private_key" {
  content              = tls_private_key.ocs_key.private_key_pem
  filename             = "${path.module}/ocs-key.pem"
  file_permission      = "0400"
  directory_permission = "0700"
}
