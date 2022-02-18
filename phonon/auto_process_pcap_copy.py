import argparse
from hashlib import sha1, sha256
from pathlib import Path
import multiprocessing
import sys
import io
import datetime
import subprocess
import traceback
import os
from tasks import download

LOG_TIME_FMT = "%Y-%m-%d %H:%M:%S"


def print_ts(text):
    start = datetime.datetime.now()
    print("[{tt}] {text}".format(tt=start.strftime(LOG_TIME_FMT), text=text))


manifest = [
    'server_1',
    'server_2',
    'server_3'
]


def load_state(pcap_dir: Path):
    statefile = pcap_dir.joinpath("statefile")
    if not statefile.exists():
        return "NOT_STARTED"
    return statefile.read_text()


def make_result(results, server, servers):
    def result(res):
        results.update({server: res})
        waiting = ",".join([srv for srv in servers if srv not in results])
        print(res[1])
        print_ts(f"[{server}] Done - waiting[{waiting}]")
    return result


def auto_process_pcap_server(server: str, date: str):
    sys.stdout = io.StringIO()
    sys.stderr = sys.stdout
    try:
        # pull file
        local_dir = Path(f"media/logs/logs_{server}_day_pcap")
        # remote_dir = Path(f"logs")
        local_dir.mkdir(parents=True, exist_ok=True)

        out = download.apply_async([server], retry=True, retry_policy={ 'max_retries': 10 })
        out.get()
        if not out:
            return False, sys.stdout.getvalue()

        print(sha1(f"{local_dir}/*.pcap".encode()))

        pcap_files = [f for f in local_dir.glob(f"test_transfer.pcap")]
        if not pcap_files:
            print("[ERR] No pcap file found")
            return False, sys.stdout.getvalue()
        if len(pcap_files) > 1:
            print(f"[ERR] Too many pcap files found - {pcap_files}")
            return False, sys.stdout.getvalue()
        pcap_file = pcap_files[0]
        proto = pcap_file.name.split('_')[-2]
        # if not subprocess.run(f"/media/nas/Decrypted/pcap/pcapfeed_latest --file {pcap_file.name} --protocol {proto}", shell=True, cwd=local_dir):
        #     return False, sys.stdout.getvalue()
        return True, sys.stdout.getvalue()
    except:
        traceback.print_exc()
        return False, sys.stdout.getvalue()


def auto_process_pcap(date: str):
    pool = multiprocessing.Pool(len(manifest))
    results = {}
    for server in manifest:
        pool.apply_async(auto_process_pcap_server, (server, date), callback=make_result(results, server, manifest)) # TODO: convert make_result to @app.task
    pool.close()
    pool.join()
    failed = False
    for server, (res, stdout) in results.items():
        if res:
            print_ts(f"[{server}] [OK] Deployed.")
        else:
            print_ts(f"[{server}] [ERR] Failed deployment.")
            failed = True
    return not failed


if __name__ == '__main__':
    today_ds = datetime.datetime.now().strftime("%Y%m%d")
    parser = argparse.ArgumentParser(description="Pull and process pcap logs")
    parser.add_argument("--date", help="Date of pcaps to process", default=today_ds)

    args = parser.parse_args()
    if not auto_process_pcap(args.date):
        exit(1)
