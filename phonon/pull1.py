import yaml
import os, sys
import hashlib
import subprocess

from tasks import pull_zhenyu

def build_manifest(file):
    return yaml.safe_load(file)

if __name__ == "__main__":
    with open('manifest.yaml', 'r') as file:
        manifest = build_manifest(file)
    
    result = pull_zhenyu.apply_async([manifest]).get()
    print(result)