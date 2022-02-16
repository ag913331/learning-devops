from celery import Celery
import sys
import io
import subprocess
import traceback
from pathlib import Path

BROKER_URL = 'redis://localhost:6379/0' # TODO change

app = Celery('tasks', broker=BROKER_URL)

@app.task(name='try to download')
def download(server, remote_dir, local_dir):
    # update status - completed_count
    return subprocess.run(f"rsync -aivz --progress --append {server}:{remote_dir}/*.pcap {local_dir}/", shell=True)

@app.task(name='Pull file', bind=True)
def auto_process_pcap_server(server: str, date: str):
    sys.stdout = io.StringIO()
    sys.stderr = sys.stdout
    try:
        # pull file
        local_dir = Path(f"/media/stride/trading/record_data/equities_CN/md_data/{date}/logs_{date}_{server}_day_pcap")
        remote_dir = Path(f"~/.trash/logs_{date}_{server}_day_pcap.tar.gz")
        local_dir.mkdir(parents=True, exist_ok=True)
        out = download.apply_async((server, remote_dir, local_dir), ignore_result=True, retry=True, retry_policy={ 'max_retries': 30 }) # callback auto_process_pcap_server???
        out.get()
        if not out:
            return False, sys.stdout.getvalue()
        pcap_files = [f for f in local_dir.glob(f"*{date}*.pcap")]
        if not pcap_files:
            print("[ERR] No pcap file found")
            return False, sys.stdout.getvalue()
        if len(pcap_files) > 1:
            print(f"[ERR] Too many pcap files found - {pcap_files}")
            return False, sys.stdout.getvalue()
        pcap_file = pcap_files[0]
        proto = pcap_file.name.split('_')[-2]
        if not subprocess.run(f"/media/nas/Decrypted/pcap/pcapfeed_latest --file {pcap_file.name} --protocol {proto}", shell=True, cwd=local_dir):
            return False, sys.stdout.getvalue()
        return True, sys.stdout.getvalue()
    except:
        traceback.print_exc()
        return False, sys.stdout.getvalue()