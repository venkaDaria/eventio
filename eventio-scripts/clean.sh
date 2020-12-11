#!/bin/bash

# Delete .DS_Store's
find . -name '.DS_Store' -type f -exec rm {} \;

# Stop all containers
./eventio.sh stop

# Delete all containers
docker rm $(docker ps -a -q)

docker volume rm $(docker volume ls -q)

# Delete all images
if [[ "$1" == "+images" ]]; then
  docker rmi $(docker images -q)
fi

# run like ./clean.sh or ./clean.sh +images
