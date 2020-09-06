FROM java:8-jre
MAINTAINER Manuel Santos <ney.br.santos@gmail.com>

ADD ./target/dpd-service.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/dpd-service.jar"]

EXPOSE 20000