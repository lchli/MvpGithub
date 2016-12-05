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
uploadApk=$1
uKey=""
apiKey=""
url="http://www.pgyer.com/apiv1/app/upload"

response=$(curl -F "file=@${uploadApk}" -F "uKey=${uKey}" -F "_api_key=${apiKey}" ${url})
#echo ${response}
code=`echo ${response} | jq '.code'`
message=`echo ${response} | jq '.message'`

if [ $code -eq 0 ]
then
echo "-------------------------upload success----------------------"
else
echo "upload fail:$message"
fi


}

function checkJq {

res=`type jq`

if [ $? -ne 0 ]
then

echo "jq not exists,please install jq first !!!!!!"
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


checkJq

if [ ${isUpload} == "true" ]
then
uploadToPgyer ${uploadApk}
fi
