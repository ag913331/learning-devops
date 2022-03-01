import yaml
import os, sys
import hashlib
import subprocess

from tasks import download_manifest, download_progress_tracker

def build_manifest(file):
    return yaml.safe_load(file)

if __name__ == "__main__":
    with open('manifest.yaml', 'r') as file:
        manifest = build_manifest(file)
    result = download_manifest.apply_async([manifest])

    # track = download_progress_tracker.delay()
    # while not result.ready():
    #     print("Waiting")

    # track.revoke(terminate=True)