#!/usr/bin/env python3

import smtplib
import sys
import json
from email.mime.text import MIMEText
from email.header import Header
from email.mime.multipart import MIMEMultipart
import argparse
import urllib
import time
import subprocess
import os
import requests


def checkCmdExe(status, output):
    if status != 0:
        print(output)
        exit()


parser = argparse.ArgumentParser()

parser.add_argument("-isUpload", "--isUpload", type=str, help="the smtp")
parser.add_argument("-type", "--type", type=str, help="the emailSender")
parser.add_argument("-apiKey", "--apiKey", type=str, help="the emailReceivers")
parser.add_argument("-uKey", "--uKey", type=str, help="the subject")
parser.add_argument("-emailReceivers", "--emailReceivers", type=str, help="the content")
parser.add_argument("-emailSender", "--emailSender", type=str, help="the emailPwd")
parser.add_argument("-emailPwd", "--emailPwd", type=str, help="the response")
parser.add_argument("-smtp", "--smtp", type=str, help="the response")
parser.add_argument("-worksp", "--worksp", type=str, help="the response")

args = parser.parse_args()
print(args)

isUpload = args.isUpload
type = args.type
apiKey = args.apiKey
uKey = args.uKey
emailSender = args.emailSender
emailPwd = args.emailPwd
emailReceivers = args.emailReceivers
smtp = args.smtp
worksp = args.worksp

srcApk = "{0}/AnGithub/app/build/outputs/apk/app-{1}.apk".format(worksp,type)
date = time.strftime('%Y-%m-%d', time.localtime(time.time()))
uploadApk = "{0}/AnGithub/app/build/outputs/apk//Github-{1}-{2}.apk".format(worksp,type, date)

status=subprocess.call("cd {0} && npm install".format(worksp),shell=True)
checkCmdExe(status,"fail.")

status = subprocess.call('cd {0} && react-native bundle --platform android --dev false --entry-file index.android.js \
  --bundle-output AnGithub/app/src/main/assets/index.android.bundle \
  --assets-dest AnGithub/app/src/main/res/'.format(worksp),shell=True)
checkCmdExe(status,"fail.")

status = subprocess.call('source ~/.bashrc && cd {0}/AnGithub && gradle clean assemble{1}'.format(worksp,type),shell=True)
checkCmdExe(status,"fail.")

os.rename(srcApk, uploadApk)

if isUpload != 'true':
    exit()

subprocess.call("echo ----------------------------uploading-----------------------------------------",shell=True)

url = "http://www.pgyer.com/apiv1/app/upload"

data = {
    'uKey': uKey,
    '_api_key': apiKey,
}
files = {'file': open(uploadApk, 'rb')}

response = requests.post(url, data=data, files=files).json()
print(response)

if response['code'] != 0:
    print("upload fail:" + response['message'])
    exit()

data = response['data']
qrjpg = "qr.jpg"

r = requests.get(data['appQRCodeURL'], stream=True)
with open(qrjpg, 'wb') as fd:
    for buf in r.iter_content(chunk_size=128):
        fd.write(buf)
r.close()

subprocess.call("echo ----------------------------sendEmail-----------------------------------------")
# 创建一个带附件的实例
msg = MIMEMultipart()

# 构造附件1
att1 = MIMEText(open(qrjpg, 'rb').read(), 'base64', 'gb2312')
att1["Content-Type"] = 'application/octet-stream'
att1["Content-Disposition"] = 'attachment; filename="qrcode"'  # 这里的filename可以任意写，写什么名字，邮件中显示什么名字

server = smtp
conn = smtplib.SMTP()
conn.connect(server)
#conn.starttls()
user, password = (emailSender, emailPwd)
conn.login(user, password)

text = MIMEText("this is content", 'text', 'utf-8')  # 中文需参数‘utf-8'，单字节字符不需要

msg.attach(text)
msg.attach(att1)

# receives = ['996863054@qq.com', 'lchli888@sohu.com', 'lichenghang@wanda.cn']
msg['Subject'] = "upload success."
rec = emailReceivers.split(",")
print(rec)

conn.sendmail(emailSender, rec,
              msg.as_string())
conn.quit()
