data "aws_ami" "ubuntu_latest" {
  most_recent = true
  owners      = ["099720109477"]

  filter {
    name   = "name"
    values = ["ubuntu/images/hvm-ssd/ubuntu-*-amd64-server-*"]
  }

  filter {
    name   = "virtualization-type"
    values = ["hvm"]
  }
}

module "sonarqube" {
  source           = "../../modules/ec2_instance"
  ami_id           = data.aws_ami.ubuntu_latest.id
  instance_type    = "t2.medium"
  key_name         = "ramo-beko-key"
  volume_size      = 20
  instance_name    = "sonarqube"
}

module "nexus" {
  source           = "../../modules/ec2_instance"
  ami_id           = data.aws_ami.ubuntu_latest.id
  instance_type    = "t2.medium"
  key_name         = "ramo-beko-key"
  volume_size      = 20
  instance_name    = "nexus"
}

module "k8s_master" {
  source           = "../../modules/ec2_instance"
  ami_id           = data.aws_ami.ubuntu_latest.id
  instance_type    = "t2.medium"
  key_name         = "ramo-beko-key"
  volume_size      = 25
  instance_name    = "kubernetes-master"
}

module "k8s_slave_1" {
  source           = "../../modules/ec2_instance"
  ami_id           = data.aws_ami.ubuntu_latest.id
  instance_type    = "t2.medium"
  key_name         = "ramo-beko-key"
  volume_size      = 25
  instance_name    = "kubernetes-slave-1"
}

module "k8s_slave_2" {
  source           = "../../modules/ec2_instance"
  ami_id           = data.aws_ami.ubuntu_latest.id
  instance_type    = "t2.medium"
  key_name         = "ramo-beko-key"
  volume_size      = 25
  instance_name    = "kubernetes-slave-2"
}
