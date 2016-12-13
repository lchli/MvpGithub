#!/usr/bin/env python3

import smtplib
import sys
import json
from email.mime.text import MIMEText
from email.header import Header
from email.mime.multipart import MIMEMultipart
import argparse
import urllib
import urllib2
import time




parser = argparse.ArgumentParser()

parser.add_argument("-isUpload", "--isUpload", type=str, help="the smtp")
parser.add_argument("-type", "--type", type=str, help="the emailSender")
parser.add_argument("-apiKey", "--apiKey", type=str, help="the emailReceivers")
parser.add_argument("-uKey", "--uKey", type=str, help="the subject")
parser.add_argument("-emailReceivers", "--emailReceivers", type=str, help="the content")
parser.add_argument("-emailSender", "--emailSender", type=str, help="the emailPwd")
parser.add_argument("-emailPwd", "--emailPwd", type=str, help="the response")
parser.add_argument("-smtp", "--smtp", type=str, help="the response")

args = parser.parse_args()
print(args)

isUpload=args.isUpload
type=args.type
apiKey=args.apiKey
uKey=args.uKey
emailSender=args.emailSender
emailPwd=args.emailPwd
emailReceivers=args.emailReceivers
smtp=args.smtp

srcApk="./app/build/outputs/apk/app-{0}.apk".format(type)
date=time.strftime('%Y-%m-%d',time.localtime(time.time()))
uploadApk="./app/build/outputs/apk//Github-{0}-{1}.apk".format(type,date)

subprocess.getstatusoutput('source ~/.bashrc')
subprocess.getstatusoutput('npm install')
subprocess.getstatusoutput('react-native bundle --platform android --dev false --entry-file index.android.js \
  --bundle-output AnGithub/app/src/main/assets/index.android.bundle \
  --assets-dest AnGithub/app/src/main/res/')

subprocess.getstatusoutput('cd AnGithub')
subprocess.getstatusoutput('gradle clean assemble{0}'.format(type))

os.rename(srcApk,uploadApk)

if isUpload != 'true':
    exit()

url="http://www.pgyer.com/apiv1/app/upload"
params = {'file': open(uploadApk, "rb"),'uKey': uKey,'_api_key': apiKey}
datagen, headers = poster.encode.multipart_encode(params)
request = urllib2.Request(url, datagen, headers)
responseJson = urllib2.urlopen(request)

response = json.loads(responseJson)
print(response)

if response['code'] !=0 :
    print("upload fail:"+response['message'])
    exit()

data=response['data']
qrjpg="qr.jpg"
urllib.urlretrieve(data['appQRCodeURL'], qrjpg)



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

text = MIMEText("this is content", 'text', 'utf-8')  # 中文需参数‘utf-8'，单字节字符不需要

msg.attach(text)
msg.attach(att1)

# receives = ['996863054@qq.com', 'lchli888@sohu.com', 'lichenghang@wanda.cn']
msg['Subject'] = "upload success."
rec=emailReceivers.split(",")

conn.sendmail(emailSender,rec,
              msg.as_string())
conn.quit()
