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



if [ ${isUpload} == "true" ]
then
../sendmail.py -apiKey "${apiKey}" -uKey "${uKey}" -emailSender "${emailSender}" \
-emailPwd "${emailPwd}" -emailReceivers "${emailReceivers}" -smtp "${smtp}" \
-uploadApk "${uploadApk}" -isSendEmail "${isSendEmail}"
fi

