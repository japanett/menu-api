FROM openjdk:17-jdk-alpine

WORKDIR /var/www

RUN apk update && apk add --no-cache findutils

EXPOSE 8080


# RUN mkdir -p /app/

# RUN mkdir -p /app/logs/

# ADD build/libs/*.jar /app/app.jar

# EXPOSE 8080

# ENTRYPOINT java -jar /app/app.jar

# Build image
# $ docker build -t menuapi:0.0.1 .

# Run container
# $ docker run -it -p 8080:8080 menuapi:0.0.1


