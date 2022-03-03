import yaml
import os, sys
import hashlib
import subprocess
from pathlib import Path

from tasks import pull_zhenyu, find_remote_files

LOCAL = '/home/r3d/work/'
servers = [
    'zh_22_server_1',
    'zh_23_server_2'
]

plan = [
    'flags.csv',
    'orderLog.csv',
    'fnLog.csv',
    '*.pcap'
]

def build_zhenyu_manifest(server, files):
    dict_file = { "server": server }
    file_objects = []
    for f in files:
        out = subprocess.run(f"sha1sum {f}", shell=True, stdout=subprocess.PIPE)
        if out.returncode != 0:
            print("manifest build failed")
            return False

        f_path = Path(f"{f}")
        obj = {}
        obj["source_path"] = f
        obj["dest_path"] = f"/home/r3d/celery-project/media/logs/{server}"
        obj["size"] = f_path.stat().st_size
        obj["sha1sum"] = out.stdout.decode('utf-8').split(' ')[0]

        file_objects.append(obj)

    dict_file.update({"files": file_objects})

    return dict_file


# def build_manifest(file):
#     return yaml.safe_load(file)

if __name__ == "__main__":
    # with open('manifest.yaml', 'r') as file:
    #     manifest = build_manifest(file)
    
    for ser in servers:
        result = find_remote_files.apply_async((ser, plan)).get()

        manifest = build_zhenyu_manifest(ser, result)

        res = pull_zhenyu.apply_async([manifest]).get()
        print(res)