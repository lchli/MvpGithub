#!/usr/bin/env bash

function renameApk {
date_now=`date +%Y-%m-%d`
srcApk=$1
type=$2
uploadApk="./app/build/outputs/apk//Github-${type}-${date_now}.apk"
mv ${srcApk} ${uploadApk}
echo ${uploadApk}
}



function uploadToPgyer {

checkCmdIfExists jq

uploadApk=${1}

apiKey="55c2a4cbc4726d35f7740ff61a60061b"
uKey="230f24fc2963f346d21e9364dea95be2"

url="http://www.pgyer.com/apiv1/app/upload"

response=$(curl -F "file=@${uploadApk}" -F "uKey=${uKey}" -F "_api_key=${apiKey}" ${url})
echo ${response}

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
appQRCodeURL=`echo -e $appQRCodeURL`

echo "appQRCodeURL:$appQRCodeURL"

content="appName:${appName}\n
appVersion:${appVersion}\n
appVersionNo:${appVersionNo}\n
appQRCodeURL:${appQRCodeURL}\n
"

curl -o "qr.jpg" $appQRCodeURL

sendemail -s mail.sohu.com \
-f lchli888@sohu.com \
-t lchli888@sohu.com \
-u "apk upload successful." \
-m $content \
-o message-charset="utf-8" \
-xu lchli888@sohu.com \
-xp lchli878266 \
-o tls=no \
-a qr.jpg

##############send email end########################
else

echo "--------------------upload fail:$message-----------------------"
exit

fi


}


function checkCmdIfExists {

cmd=$1
res=`type ${cmd}`

if [ $? -ne 0 ]
then

echo "${cmd} not exists,please install ${cmd} first !!!!!!"
exit

fi

}



source ~/.bashrc

npm install

isUpload=${isUpload}
type=${type}

echo "upload:${isUpload}"
echo "type:${type}"

cd AnGithub
gradle clean assemble${type}

uploadApk=`renameApk "./app/build/outputs/apk/app-${type}.apk" ${type}`



if [ ${isUpload} == "true" ]
then
uploadToPgyer ${uploadApk}
fi

