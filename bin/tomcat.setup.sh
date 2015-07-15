#!/bin/sh

DIR=$(cd $(dirname $0); pwd)
cd $DIR

TOMCAT_VERSION=7.0.62
TOMCAT_NAME=apache-tomcat-$TOMCAT_VERSION

sudo service tomcat-7 stop > /dev/null

curl -O -# http://archive.apache.org/dist/tomcat/tomcat-7/v$TOMCAT_VERSION/bin/$TOMCAT_NAME.tar.gz
sudo tar xzf $TOMCAT_NAME.tar.gz
sudo mv $TOMCAT_NAME /opt/
sudo ln -s /opt/$TOMCAT_NAME/ /opt/tomcat-7

INIT_D=etc/init.d/tomcat-7
sudo install --mode=755 --owner=root --group=root ../conf/$INIT_D /$INIT_D
sudo update-rc.d tomcat-7 defaults

LOGROTATE_D=etc/logrotate.d/tomcat-7
sudo install --mode=644 --owner=root --group=root ../conf/$LOGROTATE_D /$LOGROTATE_D

TOMCAT_USERS_XML=opt/tomcat-7/conf/tomcat-users.xml
sudo install --mode=644 --owner=root --group=root ../conf/$TOMCAT_USERS_XML /$TOMCAT_USERS_XML

sudo service tomcat-7 start

