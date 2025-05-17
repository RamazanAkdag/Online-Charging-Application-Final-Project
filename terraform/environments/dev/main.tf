data "aws_vpc" "default" {
  default = true
}

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

module "app_sg" {
  source  = "../../modules/security_group"
  name    = "ramo-beko-sg"
  vpc_id  = data.aws_vpc.default.id
  ingress_rules = [
    { from_port = 25,    to_port = 25,    protocol = "tcp", cidr_blocks = ["0.0.0.0/0"] },
    { from_port = 80,    to_port = 80,    protocol = "tcp", cidr_blocks = ["0.0.0.0/0"] },
    { from_port = 443,   to_port = 443,   protocol = "tcp", cidr_blocks = ["0.0.0.0/0"] },
    { from_port = 22,    to_port = 22,    protocol = "tcp", cidr_blocks = ["0.0.0.0/0"] },
    { from_port = 465,   to_port = 465,   protocol = "tcp", cidr_blocks = ["0.0.0.0/0"] },
    { from_port = 6443,  to_port = 6443,  protocol = "tcp", cidr_blocks = ["0.0.0.0/0"] },
    { from_port = 3000,  to_port = 10000, protocol = "tcp", cidr_blocks = ["0.0.0.0/0"] },
    { from_port = 30000, to_port = 32767, protocol = "tcp", cidr_blocks = ["0.0.0.0/0"] },
  ]
}

module "sonarqube" {
  source             = "../../modules/ec2_instance"
  ami_id             = data.aws_ami.ubuntu_latest.id
  instance_type      = "t2.medium"
  key_name           = "ramo-beko-key"
  volume_size        = 20
  instance_name      = "sonarqube"
  security_group_ids = [module.app_sg.security_group_id]
}

module "nexus" {
  source             = "../../modules/ec2_instance"
  ami_id             = data.aws_ami.ubuntu_latest.id
  instance_type      = "t2.medium"
  key_name           = "ramo-beko-key"
  volume_size        = 20
  instance_name      = "nexus"
  security_group_ids = [module.app_sg.security_group_id]
}

module "k8s_master" {
  source             = "../../modules/ec2_instance"
  ami_id             = data.aws_ami.ubuntu_latest.id
  instance_type      = "t2.medium"
  key_name           = "ramo-beko-key"
  volume_size        = 25
  instance_name      = "kubernetes-master"
  security_group_ids = [module.app_sg.security_group_id]
}

module "k8s_slave_1" {
  source             = "../../modules/ec2_instance"
  ami_id             = data.aws_ami.ubuntu_latest.id
  instance_type      = "t2.medium"
  key_name           = "ramo-beko-key"
  volume_size        = 25
  instance_name      = "kubernetes-slave-1"
  security_group_ids = [module.app_sg.security_group_id]
}

module "k8s_slave_2" {
  source             = "../../modules/ec2_instance"
  ami_id             = data.aws_ami.ubuntu_latest.id
  instance_type      = "t2.medium"
  key_name           = "ramo-beko-key"
  volume_size        = 25
  instance_name      = "kubernetes-slave-2"
  security_group_ids = [module.app_sg.security_group_id]
}

module "backup_bucket" {
  source      = "../../modules/s3_bucket"
  bucket_name = "efs-backup-storage-bucket"
}

module "efs" {
  source            = "../../modules/efs"
  name              = "efs-backup"
  subnet_id         = module.network.public_subnet_id
  security_group_id = module.app_sg.security_group_id
}
