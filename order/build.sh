#!/bin/sh
DIR=`dirname "$0"`
cd $DIR

PUSH_REPO=192.168.100.96:5000/yas

version=snc-1.0.0
imageName=yas-order

docker build -t yas/${imageName}:${version} . --no-cache

docker tag yas/${imageName}:${version} $PUSH_REPO/${imageName}:${version}
docker push $PUSH_REPO/${imageName}:${version}
