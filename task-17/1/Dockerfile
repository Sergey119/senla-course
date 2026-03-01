FROM tomcat:11.0-jre21-temurin

RUN rm -rf /usr/local/tomcat/webapps/ROOT/*
COPY target/*.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080