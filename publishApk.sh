#!/usr/bin/env bash

function renameApk {

date_now=`date +%Y-%m-%d`

uploadApk="./app/build/outputs/apk//Github-${type}-${date_now}.apk"
mv ${srcApk} ${uploadApk}
echo ${uploadApk}

}



function uploadToPgyer {

checkCmdIfExists jq


local url="http://www.pgyer.com/apiv1/app/upload"

response=$(curl -F "file=@${uploadApk}" -F "uKey=${uKey}" -F "_api_key=${apiKey}" ${url})
echo ${response}

chmod +x ../sendmail.py
../sendmail.py -res ${response}
exit

code=`echo ${response} | jq '.code'`
message=`echo ${response} | jq '.message'`

if [ ${code} -eq 0 ]
then
echo "-------------------------upload success----------------------"
##############send email########################

checkCmdIfExists sendemail

data=`echo $response | jq '.data'`

appName=`echo ${data} | jq '.appName'`
appVersion=`echo ${data} | jq '.appVersion'`
appVersionNo=`echo ${data} | jq '.appVersionNo'`
appQRCodeURL=`echo ${data} | jq '.appQRCodeURL'`
len=${#appQRCodeURL}
appQRCodeURL=${appQRCodeURL:1:$[len-2]}

content="appName:${appName}\n
appVersion:${appVersion}\n
appVersionNo:${appVersionNo}\n
appQRCodeURL:${appQRCodeURL}\n
"
qrjpg="qr.jpg"

curl -o $qrjpg $appQRCodeURL

chmod +x ../sendmail.py
../sendmail.py -s $smtp \
-f $emailSender \
-t $emailReceivers \
-u "apk upload successful." \
-m $content \
-o message-charset="utf-8" \
-xu $emailSender \
-xp $emailPwd \
-o tls=auto \
-a $qrjpg

##############send email end########################
else

echo "--------------------upload fail:$message-----------------------"
exit

fi


}


function checkCmdIfExists {

local cmd=$1
res=`type ${cmd}`

if [ $? -ne 0 ]
then

echo "${cmd} not exists,please install ${cmd} first !!!!!!"
exit

fi

}

function installReactBundle {

react-native bundle --platform android --dev false --entry-file index.android.js \
  --bundle-output AnGithub/app/src/main/assets/index.android.bundle \
  --assets-dest AnGithub/app/src/main/res/

}


source ~/.bashrc

npm install

isUpload=${isUpload}
type=${type}
apiKey=${apiKey}
uKey=${uKey}
emailSender=${emailSender}
emailPwd=${emailPwd}
emailReceivers=${emailReceivers}
smtp=${smtp}

srcApk="./app/build/outputs/apk/app-${type}.apk"

echo "upload:${isUpload}"
echo "type:${type}"


chmod +x sendmail.py
sendmail.py -isUpload ${isUpload} -type ${type} -apiKey ${apiKey} -uKey ${uKey} \
-emailSender ${emailSender} -emailPwd ${emailPwd} -emailReceivers ${emailReceivers} -smtp ${smtp}
exit






installReactBundle

cd AnGithub
gradle clean assemble${type}

renameApk



if [ ${isUpload} == "true" ]
then
uploadToPgyer
fi

