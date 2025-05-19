#!/usr/bin/env python3

import json

hosts = {}
with open("ec2_ids", "r") as f:
    for line in f:
        if "=" in line:
            k, v = line.strip().split("=")
            hosts[k.strip()] = v.strip()

inventory = {
    "all": {
        "children": {
            "nexus": {"hosts": [hosts["nexus"]]},
            "sonarqube": {"hosts": [hosts["sonarqube"]]},
            "k8s_master": {"hosts": [hosts["k8s_master"]]},
            "k8s_slaves": {
                "hosts": [hosts["k8s_slave_1"], hosts["k8s_slave_2"]]
            },
        }
    }
}

print(json.dumps(inventory, indent=2))

