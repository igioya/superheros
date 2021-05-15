FROM ubuntu:18.04

MAINTAINER Ignacio Gioya

#Install JAVA 11
RUN apt-get update && \
	apt-get install -y openjdk-11-jdk && \
	apt-get install -y ant && \
	apt-get clean && \
	rm -rf /var/lib/apt/lists/* && \
	rm -rf /var/cache/oracle-jdk11-installer;

ENV JAVA_VER 11
ENV JAVA_HOME /usr/lib/jvm/java-11-openjdk-amd64/

ENV JAVA_HOME=$JAVA_HOME
ENV PATH="$PATH:$JAVA_HOME/bin"

# Install Maven
RUN apt-get update && \
	apt-get -y install maven

# Copy superheros-app to user directory
COPY . /usr/local/superheros/

# Maven install and assemble sample app
RUN cd /usr/local/superheros && mvn clean install -Dmaven.test.skip=true


