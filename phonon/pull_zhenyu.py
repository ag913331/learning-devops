from cmath import log
from pathlib import Path
from collections import defaultdict
import sys
import re
import subprocess
import csv
import datetime
import argparse
import traceback
import multiprocessing
import io
# from white_server_commands import exec_remote_cmd, pull_file, print_ts
from typing import List, Dict, Tuple, Optional
from tasks import load_daily_accounts, pull_zhenyu_bucket

LOG_TIME_FMT = "%Y-%m-%d %H:%M:%S"
def print_ts(text):
    start = datetime.datetime.now()
    print("[{tt}] {text}".format(tt=start.strftime(LOG_TIME_FMT), text=text))

# DAILY_ACCOUNT_DIR = Path("/media/nas/dailyAccountUpdate")
# EQUITY_TRADE_LOGS_DIR = Path("/media/nas/equityTradeLogs")
# raw_path = "/home/r3d/work/dailyAccountUpdate"
EQUITY_TRADE_LOGS_DIR = Path("/home/r3d/work/equityTradeLogs")

def make_result(results, daily_accounts):
    def result(res):
        print(res)
        for acc, (status, output) in res.items():
            results.update({acc: status})
            print(output)
        waiting = ",".join([acc for acc, _ in daily_accounts.items() if acc not in results])
        print_ts(f"Waiting[{waiting}]")
    return result


def pull_zhenyu(date: str):
    daily_accounts = load_daily_accounts.apply_async(([date]), retry=True, retry_policy={ 
        'max_retries': 3,
    }).get()
    server_buckets = defaultdict(list)
    ignored_accs = ['9998', '1000', '1001', '16203702']
    for acc in ignored_accs:
        daily_accounts.pop(acc, None)
    for acc, (server, folder, _) in daily_accounts.items():
        server_buckets[server[3:5]].append((acc, server, folder))

    # pool = multiprocessing.Pool(len(server_buckets))
    results = {}
    for _, bucket in server_buckets.items():
        # pool.apply_async(pull_zhenyu_bucket, (bucket, date), callback=make_result(results, daily_accounts))
        r = pull_zhenyu_bucket.apply_async((bucket, date), retry=True, retry_policy={ 
            'max_retries': 3,
        }).get()
        make_result(results, daily_accounts)(r)
   
    print(results)
    failed = False
    for acc, status in results.items():
        server = daily_accounts[acc][0]
        if status:
            print_ts(f"[{server} {acc}] [OK] Pulled successfully.")
        else:
            print_ts(f"[{server} {acc}] [ERR] Failed pull.")
            failed = True
    return not failed


if __name__ == "__main__":
    today_ds = datetime.datetime.now().strftime("%Y%m%d")
    parser = argparse.ArgumentParser(description="Pull maya files")
    parser.add_argument("--date", help="Date to pull for, otherwise use latest", default=today_ds)
    args = parser.parse_args()
    pull_zhenyu(args.date)
