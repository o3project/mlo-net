#!/bin/bash

M2_EXTRA_OPTS=-Dmaven.test.skip=true
JAVA_HOME=/usr/lib/jvm/java-7-openjdk-amd64

# Builds odenos
git clone -b 1.0.0 https://github.com/o3project/odenos.git odenos-1.0.0
mvn clean package $M2_EXTRA_OPTS -f odenos-1.0.0/pom.xml

# Deploys MLO
mvn clean install -Pproduct $M2_EXTRA_OPTS -f mlo-srv/pom.xml
mvn clean tomcat7:redeploy $M2_EXTRA_OPTS -f mlo-psdtnc/pom.xml
mvn clean tomcat7:redeploy -Pproduct $M2_EXTRA_OPTS -f mlo-srv/pom.xml

sh system.restart.sh

