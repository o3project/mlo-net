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
./bin/tomcat.setup.sh


