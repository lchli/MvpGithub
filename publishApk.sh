#!/usr/bin/env bash

function renameApk {

local date_now=`date +%Y-%m-%d`

uploadApk="./app/build/outputs/apk//Github-${type}-${date_now}.apk"
mv ${srcApk} ${uploadApk}
echo ${uploadApk}

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



source ~/.bashrc

npm install

srcApk="./app/build/outputs/apk/app-${type}.apk"

echo "upload:${isUpload}"
echo "type:${type}"

react-native bundle --platform android --dev false --entry-file index.android.js \
  --bundle-output AnGithub/app/src/main/assets/index.android.bundle \
  --assets-dest AnGithub/app/src/main/res/

cd AnGithub
gradle clean assemble${type}

renameApk

json="{'data': {'appBuildVersion': '10', 'appUpdated': '2016-12-18 00:55:41', 'appCreated': '2016-12-18 00:55:41', 'appUpdateDescription': '', 'appVersionNo': '1', 'appKey': '1845144ce93f1bcdd7b16b42622270f1', 'appIsLastest': '1', 'appIcon': '61ac707ef0644f339b9c4b41ed198f95', 'appDescription': '', 'appVersion': '1.0', 'appQRCodeURL': 'http://static.pgyer.com/app/qrcodeHistory/a898ef1680505d9dbcaabe4840b3bb9484dd7adef2889fbd8d7b09db0f1a4f1a', 'appType': '2', 'appName': 'AnGithub', 'appIdentifier': 'com.lchli.angithub', 'appFileSize': '9027216', 'appScreenshots': '', 'userKey': '230f24fc2963f346d21e9364dea95be2', 'appShortcutUrl': 'ilvM'}, 'message': '', 'code': 0}"


if [ ${isUpload} == "true" ]
then
../uploadApk.py -apiKey "${apiKey}" -uKey "${uKey}" -emailSender "${emailSender}" \
-emailPwd "${emailPwd}" -emailReceivers "${emailReceivers}" -smtp "${smtp}" \
-uploadApk "${uploadApk}" -isSendEmail "${isSendEmail}" -json "${json}"
fi

