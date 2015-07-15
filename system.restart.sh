#!/bin/bash

export JAVA_HOME=/usr/lib/jvm/java-7-openjdk-amd64/
sudo service tomcat-7 stop
(cd odenos-1.0.0/;./odenos stop)
(cd odenos-1.0.0/;./odenos start)
sleep 10
sudo service tomcat-7 start

