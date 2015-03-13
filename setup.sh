#!/bin/bash

# common
sudo apt-get install openjdk-7-jdk -y
sudo apt-get install maven -y

# lagopus-docker installation
(cd lagopus-docker;./lagopus-docker.sh install)

# for odenos
sudo apt-get install redis-server -y
REDIS_CONF=etc/redis/redis.conf
sudo cp -f conf/$REDIS_CONF /$REDIS_CONF
sudo chmod 755 /$REDIS_CONF
sudo service redis-server restart

# for mlo
sudo apt-get install tomcat6 tomcat6-admin -y
sudo service tomcat6 restart
TOMCAT_USERS_XML=var/lib/tomcat6/conf/tomcat-users.xml
sudo cp -f conf/$TOMCAT_USERS_XML /$TOMCAT_USERS_XML
sudo chown root:tomcat6 /$TOMCAT_USERS_XML
sudo chmod 644 /$TOMCAT_USERS_XML
CRON_TOMCAT6=etc/logrotate.d/tomcat6
sudo cp -f conf/$CRON_TOMCAT6 /$CRON_TOMCAT6
sudo chown root:root /$CRON_TOMCAT6
sudo chmod 644 /$CRON_TOMCAT6


