#!/bin/bash

# Rebuild images
cd eventio-server
  ./gradlew build
cd ..

# Start Docker Compose
docker-compose up -d --build

sleep 2

# Start Sonar Analysis
if [[ "$1" == "+sonar" ]]; then
  cd eventio-server
    ./gradlew sonarqube
  cd ..
fi

sleep 2

# Show running containers
docker ps

echo Containers count: $(( $(docker ps | wc -l) - 1 ))
