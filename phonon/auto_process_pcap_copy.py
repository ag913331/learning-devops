import argparse
from pathlib import Path
import multiprocessing
import sys
import io
import datetime
import subprocess
import traceback
from white_server_commands import exec_remote_cmd, pull_file, print_ts
from tasks import auto_process_pcap_server
from celery import group


manifest = [
    'zs_88_04',
    'zs_72_01',
    'zt_94_02',
    'zt_52_03',
    'zs_66_01',
    'zs_96_02',
    'zt_88_06'
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

# 28.6%|##     | 2/7
def auto_process_pcap(date: str):
    results = group((auto_process_pcap_server.s(server, date) for server in manifest), link=make_result(server, manifest))()
    results.join()
    results.as_tuple()
    results.completed_count()

    # results.items() ???
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
