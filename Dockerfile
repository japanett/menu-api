FROM openjdk:17-jdk-alpine

WORKDIR /var/www

RUN apk update && apk add --no-cache findutils

EXPOSE 8080

