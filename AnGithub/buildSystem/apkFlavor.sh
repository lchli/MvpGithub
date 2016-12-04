#!/usr/bin/env bash

function checkFileExists {
if [ ! -f $1 ]; then
echo "$1 not exists !!!"
exit
fi
}

function deleteDir {
rm -rf $1
}


echo "input source apk path:"
read sourceApk
echo "input dest save dir:"
read saveDir
echo "input channel file path:"
read channelsFile

checkFileExists $sourceApk
checkFileExists $channelsFile
if [ ! -d $saveDir ]; then
mkdir -p $saveDir
fi

tmpdir="./tmp"
if [ -d $tmpdir ]; then
deleteDir $tmpdir#delete old files.
fi

mkdir -p $tmpdir
unzip ${sourceApk} -d ${tmpdir}

if [ $? -ne 0 ]; then
echo "unzip fail !!!!!!!!!!!!!!!"
deleteDir $tmpdir#delete old files.
exit
fi

for channelName in `cat ${channelsFile}`
do

cd $tmpdir
channel="./META-INF/channel_${channelName}"
touch ${channel}

zip -r ${channelName}.apk .
rm -f ${channel}

cd ..
mv ${tmpdir}/${channelName}.apk ${saveDir}

done

deleteDir $tmpdir#delete old files.

echo "---------------------------success------------------------------"