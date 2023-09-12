FROM azul/zulu-openjdk-centos:17

RUN mkdir -p /app/

RUN mkdir -p /app/logs/

ADD build/libs/*.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT java -jar /app/app.jar

# Build image
# $ docker build -t menuapi:0.0.1 .

# Run container
# $ docker run -it -p 8080:8080 menuapi:0.0.1

