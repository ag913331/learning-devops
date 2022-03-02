import time
from celery import Celery
from celery.schedules import crontab
import subprocess
import sys, io, traceback
from pathlib import Path
import csv
from typing import List, Dict, Tuple, Optional
import datetime
import logging

BROKER_URL = 'redis://localhost:6379/0'
BACKEND_URL = 'redis://localhost:6379/1'

logger = logging.getLogger(__name__)

app = Celery('tasks', backend=BACKEND_URL, broker=BROKER_URL)

# @app.on_after_configure.connect
# def setup_periodic_tasks(sender, **kwargs):
#     # Calls test('hello') every 10 seconds.
#     sender.add_periodic_task(60.0*5, download_progress_tracker.s('check progress again'), name='check progress every 5 min')

@app.task(name='Download file', bind=True, default_retry_delay=10)
def download_file(self, server, file):
    dest_path = file['dest_path']
    source_path = file['source_path']
    Path(dest_path).mkdir(parents=True, exist_ok=True) # check result

    try:
        out = subprocess.run(f"rsync -aivz --append {server}{source_path} {dest_path}", shell=True)
        if out.returncode != 0:
            raise Exception("Failed to download")
        
        sha1sum_remote = file['sha1sum']
        print(sha1sum_remote)
        local_file = source_path.split("/")[-1]
        sh1_out = subprocess.run(f"sha1sum {dest_path}/{local_file}", shell=True, stdout=subprocess.PIPE)
        if out.returncode != 0:
            print(f"[ERR] Failed getting sha1sum of {dest_path}/{local_file} [{out.returncode}]")
            return False, sys.stdout.getvalue()
        sha1sum_local = sh1_out.stdout.decode('utf-8').split(' ')[0]

        if sha1sum_local != sha1sum_remote:
            print(f"[ERR] Sanity check failed for {source_path} - sha1 does not match! Check the upload manually!")
            raise Exception("[ERR] Sanity check failed")
    except Exception as e:
        logger.exception(e)
        print('Try {0}/{1}'.format(self.request.retries, self.max_retries))
        self.retry()

    print("COPIED!!!")
    return True

@app.task(name='Download files from manifest', bind=True)
def download_manifest(self, manifest):
    server = manifest['server']
    files = manifest['files']
    for file in files:
        download_file.apply_async((server, file), retry=True, retry_policy={ 
            'max_retries': 3,
        }) # retries?
        print(f"{file['source_path']} download start")
        # if file download has failed -> mark whole manifest as failed
    # all good result
    return True

@app.task(name='Progress manager for download')
def download_progress_tracker(manifest):
    server = manifest['server']
    files = manifest['files']

    # every 5 minutes, or whatever
    local_size_total = 0
    remote_size_total = 0
    for file in files:
        remote_size_total += file['size']
        if Path(f"{file['dest_path']}").exists():
            print("INCREASE")
            local_size_total = Path(f"{file['dest_path']}").lstat().st_size
    print("progress")
    print(f"{local_size_total} / {remote_size_total}")

@app.task(name='Sample task', bind=True)
def pull_zhenyu(self, manifest):
    # if manifest fails build - complain and fail
    if not manifest:
        print("Manifest missing or corrupted")
        return False

    result = download_manifest.apply_async([manifest])

    progress = download_progress_tracker.delay(manifest)
    while not result.ready():
        print("Waiting")

    progress.revoke(terminate=True)


    return True
    #when download manifest finishes, stop progress tracker
    # after every failure - send some sort of notification
    # if (result == failed):
        # report failure
        # schedule pull again after 5 mins





















# @app.task(name="Find remote files")
# def find_remote_files(plan):
#     result = []
#     for f in plan:
#         ln = f.strip()
#         out = subprocess.run(f"find ~/work/zh_22_server_1/logs -name {ln}", shell=True, stdout=PIPE, stderr=PIPE, universal_newlines=True)
#         print("===out===")
#         print(out)
#         if out.stdout:
#             print("====out.stdout====")
#             print(out.stdout)
#             result.append(out.stdout.strip())

#     return result

# # ================ WORKING ===============================================
# @app.task(name='Attempt download', bind=True)
# def download(self, args):
#     local_dir = Path(f"media/logs/logs_zh_22_server_1_day_pcap")
#     result = {}
#     for remote_file in args:
#         try:
#             out = subprocess.run(f"rsync -aivz --info=progress2 --human-readable --append {remote_file} {local_dir}/", shell=True)
            
#             file = remote_file.split('/')[-1]
#             if out.returncode == 0:
#                 result.update({file: file + ' from ' + '[zh_22_server_1]:[' + remote_file + ']' + ' was pulled successfully'})
        
#         except (subprocess.CalledProcessError, subprocess.SubprocessError, subprocess.TimeoutExpired) as exc:
#             raise self.retry(exc=exc)

#     return result
# ================ WORKING ===============================================

# @app.task(name='Attempt download', bind=True)
# def download(self, args):
#     local_dir = Path(f"media/logs/logs_server_1_day_pcap")
#     result = {}
#     for i, remote_file in enumerate(args):
#         out = subprocess.run(f"rsync -aivz --append {remote_file} {local_dir}/", shell=True)
        
#         file = remote_file.split('/')[-1]
#         if out.returncode == 0:
#             self.update_state(state='PROGRESS', meta={'current': i, 'total': len(args)})
#             result.update({file: file + ' from ' + '[server_1]:[' + remote_file + ']' + ' was pulled successfully'})

#     return result

# @app.task(name="Just print results")
# def next_t(args):
#     print("Just print stuff")
#     print(args)


#=================pull_zhenyu.py=====================================
# LOG_TIME_FMT = "%Y-%m-%d %H:%M:%S"
# def print_ts(text):
#     start = datetime.datetime.now()
#     print("[{tt}] {text}".format(tt=start.strftime(LOG_TIME_FMT), text=text))

# raw_path = "/home/r3d/work/dailyAccountUpdate"
# EQUITY_TRADE_LOGS_DIR = Path("/home/r3d/work/equityTradeLogs")

# @app.task(name="tasks.load_daily_accounts", bind=True, default_retry_delay=10)
# def load_daily_accounts(self, date: str) -> Dict[str, Tuple[str, str, str]]:
#     accounts = {}
#     cur_date = datetime.datetime.strptime(date, "%Y%m%d")
#     cur_monthday = cur_date.strftime("%m%d")
#     DAILY_ACCOUNT_DIR = Path(raw_path)
#     acc_file = None
#     acc_file = DAILY_ACCOUNT_DIR.joinpath(f"{cur_monthday}/account_server_folder.csv")
#     if acc_file is None:
#         return {}
#     try:
#         with acc_file.open('r') as csvfile:
#             reader = csv.reader(csvfile)
#             next(reader)
#             accounts = dict([(account, (server, folder, note)) for (account, server, folder, note) in reader if account and not account.startswith('#')])
#     except Exception as e:
#         logger.exception(e)
#         print('Try {0}/{1}'.format(self.request.retries, self.max_retries))
#         self.retry()

#     return accounts

# @app.task(name="tasks.pull_zhenyu_bucket", bind=True, default_retry_delay=10)
# def pull_zhenyu_bucket(self, bucket, date):
#     log_types = ["flags", "orderLog"]
#     results = {}
#     for (acc, server, folder) in bucket:
#         sys.stdout = io.StringIO()
#         sys.stderr = sys.stdout
#         status = True
#         try:
#             dest_dir = EQUITY_TRADE_LOGS_DIR.joinpath(date).joinpath("raw").joinpath(acc)
#             dest_dir.mkdir(parents=True, exist_ok=True)
#             # remote_dir = f"~/.trash/logs_{date}_{server}_day_{folder.split('-')[-1]}.tar.gz"
#             # remote_dir = f"~/work/server_3/.trash/logs_{date}_{server}_day_{folder.split('-')[-1]}.tar.gz"
#             remote_dir = f"~/work/server_3/.trash/logs"
#             for lt in log_types:
#                 tries = 5
#                 while tries:
#                     print(f"rsync -a {remote_dir}/{lt}* {dest_dir}/")
#                     # out = subprocess.run(f"rsync -a {server}:{remote_dir}/{lt}* {dest_dir}/", stdout=subprocess.PIPE, stderr=subprocess.PIPE, shell=True)
#                     out = subprocess.run(f"rsync -a {remote_dir}/{lt}* {dest_dir}/", shell=True)
#                     if out.returncode == 0:
#                         break
#                     # print(out.stdout.decode('utf-8'))
#                     # print(out.stderr.decode('utf-8'))
#                     tries -= 1
#                 if not tries:
#                     print_ts(f"[ERR] Failed pulling {lt}")
#                     status = False
#                     break
#             results[acc] = (status, sys.stdout.getvalue())

#         except:
#             # logger.exception(e)
#             print('Try {0}/{1}'.format(self.request.retries, self.max_retries))
#             self.retry()
#             traceback.print_exc()
#             results[acc] = (False, sys.stdout.getvalue())
#     return results