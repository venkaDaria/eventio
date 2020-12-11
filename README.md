# Simple Usage

(need Docker to be installed)

- **./eventio.sh help** => help about all commands that exists

- **./eventio.sh run** => start app

- **./eventio.sh restart <service_name>** => restart service by name

- **./eventio.sh stop** => stop app

- **./eventio.sh clean [+images]** => stop app and clean all

- **./eventio.sh support** => add Sonar-Kotlin support (only if folder 'sonarqube_extensions' doesn't exist)

**IMPORTANT:** for using LiqPay must be added _eventio-server/src/main/resources/secret.properties_ file:

```properties
liqpay.key.private=XXXXXXX
liqpay.key.public=XXXXX
```

# Start

- database Neo4j (name db - eventio-db, Neo4j Desktop | Browser);
- server Java SpringBoot Gradle (IntelliJ Idea);

  - For debug java-app in Docker => add remote configuration localhost:50505

- using localtunnel

  - npm install -g localtunnel
  - lt --port 8080 --subdomain eventio;
  - ping <http://eventio.localtunnel.me/>

- client: npm install && npm start (Atom | VS Code);

## Localhost urls

- <http://localhost:8080/swagger-ui.html#/> - api;
- <http://localhost:3000> - ui;
- <http://localhost:7474/browser/> - Neo4j;
- <http://localhost:9000/dashboard?id=com.example.venka%3Aeventio-server> - Sonar;

## Windows

For using sh-scripts in Windows you need:

- installed Cygwin or Git bash;
- use _[unix|win]-support.sh_ scripts;

# Docker

- at first install and start Docker-client;
- need 4Gb memory instead of 2Gb;

## Docker Support

- docker network create eventio-net
- docker run --network=eventio-net --publish=7474:7474 --publish=7687:7687 --name=neo4j -e NEO4J_AUTH=neo4j/admin neo4j:latest
- (cd eventio-server) ./gradlew build docker
- docker run --network=eventio-net -p 8080:8080 -t --name=eventio-server com.example.venka/eventio-server
- docker build -t localtunnel ./eventio-config -f=eventio-config/Dockerfile-lt
- docker run --network=eventio-net localtunnel --name=lt
- docker build -t eventio-client eventio-client/.
- docker run --network=eventio-net -p 3000:3000 --name=eventio-client -v /eventio-client/node_modules -t eventio-client
- docker run --network=eventio-net -d --name sonarqube -p 9000:9000 -p 9092:9092 sonarqube

## DockerCompose Support

(installed with Docker)

- docker-compose up [-d --build]
- docker-compose restart [service_name]
- docker-compose build --no-cache [service_name]

## Mail Support

(Mail Server added to Docker)

- *./eventio.sh mail-config* - to add user etc.

# Fixes

- For Neo4j: when entity is transforming from dto, it is transforming without its relationships. After that relationships must be set manually with id from Neo4j database. Except Place entity.
Also all rooms (when SVG is changed) are considered new.

- Manipulating an SVG using HTMLObjectElement lead to a [Cross-Origin error]((https://stackoverflow.com/questions/46929730/why-does-dynamically-generating-an-svg-using-htmlobjectelement-lead-to-a-cross-o), so I used [inline svg](https://css-tricks.com/using-svg/) instead of [object tag](http://htmlbook.ru/html/object).

# Testing

- TestNG (back-end);

  - ./gradlew test

- Mocha & Chai (front-end);

  - npm test [brew install watchman for react-scripts test]

- Localtunnel - for localhost testing (LiqPay etc.);

- SonarQube and JaCoCo support;

  - ./gradlew sonarqube
  - ./gradlew test jacocoTestReport
  - support [Sonar-Kotlin](https://github.com/arturbosch/sonar-kotlin) ** ./support.sh

- ESLint support for React;

# Localization

- English, Ukrainian and Russian languages;
- Add new [lang]:

  - add [lang].json file with translation;
  - add this translation to languages map in *eventio-client/src/index.js*;
  - add *require('moment/locale/[lang]')* to moment localization (if localization exists);

# Future Releases

- ?разделить на два сайта - for person, for company - specification;
- vip-customization: theme, special way of payment;
- create "by mail" level for access to events;
- improve invitation mechanism and sharing mechanism (support email, Facebook, Telegram and other services to share public and private invites);
- improve complex search and date-time search;
- localization, auto-translate custom texts and auto-choose lang;
- support other calendars (Microsoft, Outlook etc.) and other auth-mechanism (Facebook, manual)
- improve svg - instructions, several stages etc.

# Plan

- вёрстка;
- api (+ настройка БД) + тесты;
- клиент + сервер соединить:

  - авторизация и аутентификация (security);
  - реализовать функционал...;
  - список исп. библиотек (gradle, package.json, GoogleAPI, sonarqube_extensions).

- документация.
