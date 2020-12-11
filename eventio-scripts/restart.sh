#!/bin/bash

service="$@"

# Rebuild images
cd eventio-server
  ./gradlew build
cd ..

docker-compose build --no-cache $service
docker-compose up -d --no-deps $service
