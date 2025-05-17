output "all_ips" {
  value = {
    sonarqube = module.sonarqube.public_ip
    nexus     = module.nexus.public_ip
    master    = module.k8s_master.public_ip
    slave_1   = module.k8s_slave_1.public_ip
    slave_2   = module.k8s_slave_2.public_ip
  }
}
