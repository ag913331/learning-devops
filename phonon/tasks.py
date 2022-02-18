from celery import Celery
import subprocess
import os
from pathlib import Path

BROKER_URL = 'redis://localhost:6379/0'
BACKEND_URL = 'redis://localhost:6379/1'

app = Celery('tasks', backend=BACKEND_URL, broker=BROKER_URL)

@app.task(name='Attempt download', bind=True)
def download(self, server):
    local_dir = Path(f"media/logs/logs_{server}_day_pcap")
    remote_dir = Path(f"logs")
    # file_size = os.path.getsize(f'/home/r3d/work/{server}/{remote_dir}/test_transfer.pcap')
    # print("File Size is :", file_size, "bytes")
    try:
        # return subprocess.run(f"sudo cp ~/work/{server}/{remote_dir}/*.pcap {local_dir}/", shell=True).returncode
        return subprocess.run(f"rsync -aivz --info=progress2 --human-readable --append ~/work/{server}/{remote_dir}/*.pcap {local_dir}/", shell=True).returncode
    except (subprocess.CalledProcessError, subprocess.SubprocessError, subprocess.TimeoutExpired) as exc:
        raise self.retry(exc=exc)