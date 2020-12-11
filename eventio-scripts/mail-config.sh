#!/bin/bash

curl -o setup.sh https://raw.githubusercontent.com/tomav/docker-mailserver/master/setup.sh

chmod a+x ./setup.sh

./setup.sh email add no-reply@eventio.com P7AdminSupport

rm ./setup.sh
