#!/bin/bash

# common
sudo apt-get update -y
sudo apt-get install openssh-server -y
sudo apt-get install openjdk-7-jdk -y
sudo apt-get install maven -y

# lagopus-docker installation
(cd lagopus-docker;./lagopus-docker.sh install)

# for odenos
sudo apt-get install redis-server -y
REDIS_CONF=etc/redis/redis.conf
sudo install --mode=755 conf/$REDIS_CONF /$REDIS_CONF
sudo service redis-server restart

# for mlo
sudo apt-get install tomcat7 tomcat7-admin -y
TOMCAT_USERS_XML=var/lib/tomcat7/conf/tomcat-users.xml
sudo install --mode=644 --owner=root --group=tomcat7 conf/$TOMCAT_USERS_XML /$TOMCAT_USERS_XML
CRON_TOMCAT=etc/logrotate.d/tomcat7
sudo install --mode=644 --owner=root --group=root conf/$CRON_TOMCAT /$CRON_TOMCAT
DEFAULT_TOMCAT=etc/default/tomcat7
sudo install --mode=644 --owner=root --group=root conf/$DEFAULT_TOMCAT /$DEFAULT_TOMCAT
sudo service tomcat7 restart


