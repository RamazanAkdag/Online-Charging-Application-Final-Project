output "all_ips" {
  value = {
    sonarqube   = module.sonarqube.public_ip
    nexus       = module.nexus.public_ip
    k8s_master  = module.k8s_master.public_ip
    k8s_slave_1 = module.k8s_slave_1.public_ip
    k8s_slave_2 = module.k8s_slave_2.public_ip
    jenkins     = module.jenkins.public_ip     
  }
}
