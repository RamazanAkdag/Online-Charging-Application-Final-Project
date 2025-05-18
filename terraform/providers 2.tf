provider "aws" {
  region  = var.aws_region
  profile = "default"  # veya ~/.aws/credentials'daki ad neyse o
}
