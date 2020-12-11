FROM node:latest

VOLUME /tmp

RUN mkdir eventio-client
WORKDIR eventio-client

COPY ./ ./

ENV PATH /eventio-client/node_modules/.bin:$PATH
RUN npm install --silent
RUN npm install react-scripts@1.1.1 -g --silent

CMD ["npm", "start"]

MAINTAINER Daria Pydorenko <daria.pydorenko@nure.ua>
