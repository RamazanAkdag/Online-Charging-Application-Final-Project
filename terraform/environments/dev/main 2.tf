# 📌 Default VPC ve ilk subnet kullanımı
data "aws_vpc" "default" {
  default = true
}

data "aws_subnets" "default" {
  filter {
    name   = "vpc-id"
    values = [data.aws_vpc.default.id]
  }
}

data "aws_subnet" "first" {
  id = data.aws_subnets.default.ids[0]
}

# 📌 Ubuntu en güncel AMI
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

# 📌 Security Group modülü
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

module "keypair" {
  source    = "../../modules/keypair"
  key_name  = "ocs-key"
}

# 📌 EC2 Modülleri
module "sonarqube" {
  source             = "../../modules/ec2_instance"
  ami_id             = data.aws_ami.ubuntu_latest.id
  instance_type      = "t2.medium"
  key_name = module.keypair.key_name
  volume_size        = 20
  instance_name      = "sonarqube"
  subnet_id          = data.aws_subnet.first.id
  vpc_id             = data.aws_vpc.default.id
  security_group_ids = [module.app_sg.security_group_id]

  depends_on = [module.app_sg]
}

module "nexus" {
  source             = "../../modules/ec2_instance"
  ami_id             = data.aws_ami.ubuntu_latest.id
  instance_type      = "t2.medium"
  key_name = module.keypair.key_name
  volume_size        = 20
  instance_name      = "nexus"
  subnet_id          = data.aws_subnet.first.id
  vpc_id             = data.aws_vpc.default.id
  security_group_ids = [module.app_sg.security_group_id]
  
  depends_on = [module.app_sg]
}

module "k8s_master" {
  source             = "../../modules/ec2_instance"
  ami_id             = data.aws_ami.ubuntu_latest.id
  instance_type      = "t2.medium"
  key_name = module.keypair.key_name
  volume_size        = 25
  instance_name      = "kubernetes-master"
  subnet_id          = data.aws_subnet.first.id
  vpc_id             = data.aws_vpc.default.id
  security_group_ids = [module.app_sg.security_group_id]

  depends_on = [module.app_sg]
}

module "k8s_slave_1" {
  source             = "../../modules/ec2_instance"
  ami_id             = data.aws_ami.ubuntu_latest.id
  instance_type      = "t2.medium"
  key_name = module.keypair.key_name
  volume_size        = 25
  instance_name      = "kubernetes-slave-1"
  subnet_id          = data.aws_subnet.first.id
  vpc_id             = data.aws_vpc.default.id
  security_group_ids = [module.app_sg.security_group_id]

  depends_on = [module.app_sg]
}

module "k8s_slave_2" {
  source             = "../../modules/ec2_instance"
  ami_id             = data.aws_ami.ubuntu_latest.id
  key_name = module.keypair.key_name  
  instance_type   = "t2.medium"
  volume_size        = 25
  instance_name      = "kubernetes-slave-2"
  subnet_id          = data.aws_subnet.first.id
  vpc_id             = data.aws_vpc.default.id
  security_group_ids = [module.app_sg.security_group_id]

  depends_on = [module.app_sg]
}

# 📌 S3 Bucket Modülü
module "backup_bucket" {
  source      = "../../modules/s3_bucket"
  bucket_name = "efs-backup-storage-bucket"
}

# 📌 EFS Modülü (subnet_id düzeltildi)
module "efs" {
  source            = "../../modules/efs"
  name              = "efs-backup"
  subnet_id         = data.aws_subnet.first.id
  security_group_id = module.app_sg.security_group_id

  depends_on = [module.app_sg]
}

module "lambda_exec_role" {
  source = "../../modules/iam_role"

  role_name = "efs-lambda-role"

  assume_role_policy_json = jsonencode({
    Version = "2012-10-17",
    Statement = [{
      Effect = "Allow",
      Principal = {
        Service = "lambda.amazonaws.com"
      },
      Action = "sts:AssumeRole"
    }]
  })

  policy_arns = [
    "arn:aws:iam::aws:policy/AWSLambda_FullAccess",
    "arn:aws:iam::aws:policy/AmazonElasticFileSystemClientFullAccess",
    "arn:aws:iam::aws:policy/service-role/AWSLambdaVPCAccessExecutionRole"
  ]
}

module "efs_backup_lambda" {
  source               = "../../modules/lambda"
  name                 = "efs-to-s3-backup"
  lambda_zip_path      = "${path.root}/../../modules/lambda/efs_backup_lambda.zip"
  lambda_role_arn      = module.lambda_exec_role.role_arn
  efs_access_point_arn = module.efs.access_point_arn
  target_s3_bucket     = module.backup_bucket.bucket_name
  subnet_ids           = [data.aws_subnet.first.id]
  security_group_ids   = [module.app_sg.security_group_id]

  depends_on = [module.efs]
}