#!/bin/sh

DIR=`dirname "$0"`
cd $DIR

sh ./backoffice-bff/build.sh
sh ./cart/build.sh
sh ./customer/build.sh
sh ./inventory/build.sh
sh ./location/build.sh
sh ./media/build.sh
sh ./order/build.sh
sh ./payment-paypal/build.sh
sh ./payment/build.sh
sh ./rating/build.sh
sh ./promotion/build.sh
sh ./product/build.sh
sh ./tax/build.sh
sh ./search/build.sh
sh ./storefront-bff/build.sh
