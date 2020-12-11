#!/bin/bash

# ./support.sh 0.4.1

git clone https://github.com/arturbosch/sonar-kotlin
cd sonar-kotlin
mvn package
cd ..

# Sonar-Kotlin support for Docker
mkdir -p ./sonarqube_extensions/plugins && \
  mv ./sonar-kotlin/target/sonar-kotlin-$version.jar \
  ./sonarqube_extensions/plugins/sonar-kotlin-plugin-$version.jar

rm -rf sonar-kotlin
