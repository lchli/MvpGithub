#!/usr/bin/env python3

import smtplib
import sys
import json
from email.mime.text import MIMEText
from email.header import Header
from email.mime.multipart import MIMEMultipart
from email.mime.image import MIMEImage
import argparse
import urllib
import time
import subprocess
import os
import requests

parser = argparse.ArgumentParser()

parser.add_argument("-apiKey", "--apiKey", type=str, help="the emailReceivers")
parser.add_argument("-uKey", "--uKey", type=str, help="the subject")
parser.add_argument("-emailReceivers", "--emailReceivers", type=str, help="the content")
parser.add_argument("-emailSender", "--emailSender", type=str, help="the emailPwd")
parser.add_argument("-emailPwd", "--emailPwd", type=str, help="the response")
parser.add_argument("-smtp", "--smtp", type=str, help="the response")
parser.add_argument("-uploadApk", "--uploadApk", type=str, help="the response")
parser.add_argument("-isSendEmail", "--isSendEmail", type=str, help="the response")

args = parser.parse_args()
print(args)

apiKey = args.apiKey
uKey = args.uKey
emailSender = args.emailSender
emailPwd = args.emailPwd
emailReceivers = args.emailReceivers
smtp = args.smtp
isSendEmail = args.isSendEmail
uploadApk = args.uploadApk

sys.stdout.write('hhhhhhhhhhhhhhhhhhhhhhhhhhhhhh'+'\n')

subprocess.call("echo ----------------------------uploading-----------------------------------------", shell=True)

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
subprocess.call("echo ----------------------------apk上传成功-----------------------------------------", shell=True)

if isSendEmail != 'true':
    print("All Finished*******no need to send email.***********************************************")
    exit()

data = response['data']
qrjpg = "qr.jpg"

r = requests.get(data['appQRCodeURL'], stream=True)
with open(qrjpg, 'wb') as fd:
    for buf in r.iter_content(chunk_size=1024):
        fd.write(buf)
r.close()

subprocess.call("echo ----------------------------sendEmail-----------------------------------------", shell=True)

server = smtp
user, password = (emailSender, emailPwd)

sender = emailSender
receives = emailReceivers

# 创建一个带附件的实例
msg = MIMEMultipart()

textMsg = MIMEText('应用名称：<br>{0}<br>应用版本：<br>{1}<br>应用版本号：<br>{2}<br>'
                   '<p><img src="cid:image1"></p>'.format(data['appName'],data['appVersion'], data['appVersionNo']),
                   'html',
                   'utf-8')

# 指定图片为当前目录
img = open(qrjpg, 'rb')
imgMsg = MIMEImage(img.read())
img.close()
# 定义图片 ID，在 HTML 文本中引用
imgMsg.add_header('Content-ID', '<image1>')

msg.attach(textMsg)
msg.attach(imgMsg)

msg['Subject'] = "【{0}】上传成功！".format(data['appName'])
msg['From'] = sender
msg['To'] = receives

conn = smtplib.SMTP()
try:
    conn.connect(server)
    # conn.starttls()
    conn.login(user, password)
    conn.sendmail(sender, receives.split(', '), msg.as_string())
except Exception as e:
    print("邮件发送失败******************************************************")
    print("Unexpected error:", sys.exc_info()[0], sys.exc_info()[0])

conn.quit()

print("All Finished******************************************************")
exit()
