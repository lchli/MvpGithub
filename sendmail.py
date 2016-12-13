#!/usr/bin/env python3

import smtplib
import sys
import json
from email.mime.text import MIMEText
from email.header import Header
from email.mime.multipart import MIMEMultipart
import argparse
import urllib




parser = argparse.ArgumentParser()
parser.add_argument("-s", "--smtp", type=str, help="the smtp")
parser.add_argument("-f", "--emailSender", type=str, help="the emailSender")
parser.add_argument("-t", "--emailReceivers", type=str, help="the emailReceivers")
parser.add_argument("-u", "--subject", type=str, help="the subject")
#parser.add_argument("-m", "--content", type=str, help="the content")
parser.add_argument("-xp", "--emailPwd", type=str, help="the emailPwd")
parser.add_argument("-res", "--res", type=str, help="the response")

args = parser.parse_args()
print(args)

res = args.res
response = json.loads(res)
print(response)
data=response['data']
urllib.urlretrieve(data['appQRCodeURL'], "qr.jpg")



smtp = args.smtp
emailSender = args.emailSender
emailReceivers = args.emailReceivers
subject = args.subject
content = args.content
emailPwd = args.emailPwd
qrjpg = args.qrjpg


# 创建一个带附件的实例
msg = MIMEMultipart()

# 构造附件1
att1 = MIMEText(open(qrjpg, 'rb').read(), 'base64', 'gb2312')
att1["Content-Type"] = 'application/octet-stream'
att1["Content-Disposition"] = 'attachment; filename="qrcode"'  # 这里的filename可以任意写，写什么名字，邮件中显示什么名字

server =smtp
conn = smtplib.SMTP(server, 587)
conn.starttls()
user, password = (emailSender, emailPwd)
conn.login(user, password)

text = MIMEText(content, 'text', 'utf-8')  # 中文需参数‘utf-8'，单字节字符不需要

msg.attach(text)
msg.attach(att1)

# receives = ['996863054@qq.com', 'lchli888@sohu.com', 'lichenghang@wanda.cn']
msg['Subject'] = subject
rec=emailReceivers.split(",")

conn.sendmail(emailSender,rec,
              msg.as_string())
conn.quit()
