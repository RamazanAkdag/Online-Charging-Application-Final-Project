#!/usr/bin/env python3

import json
import sys

hosts = {}
with open("ec2_ids", "r") as f:
    for line in f:
        if "=" in line:
            k, v = line.strip().split("=")
            hosts[k.strip()] = v.strip()

inventory = {
    "_meta": {
        "hostvars": {}
    },
    "nexus": {
        "hosts": [hosts["nexus"]]
    },
    "sonarqube": {
        "hosts": [hosts["sonarqube"]]
    },
    "k8s_master": {
        "hosts": [hosts["k8s_master"]]
    },
    "k8s_slaves": {
        "hosts": [hosts["k8s_slave_1"], hosts["k8s_slave_2"]]
    },
    "jenkins": {
        "hosts": [hosts["jenkins"]]
    }
}

if "--list" in sys.argv:
    print(json.dumps(inventory, indent=2))
elif "--host" in sys.argv:
    print(json.dumps({}))
else:
    print("Usage: --list or --host <hostname>", file=sys.stderr)
    sys.exit(1)
