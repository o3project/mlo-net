#!/bin/bash

export JAVA_HOME=/usr/lib/jvm/java-7-openjdk-amd64/
sudo service tomcat6 stop
(cd odenos-1.0.0/;./odenos stop)
(cd lagopus-docker/;./lagopus-docker.sh stop)

