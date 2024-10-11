#!/bin/sh

DIR=`dirname "$0"`
cd $DIR

sh ./backoffice/build.sh
sh ./storefront/build.sh
