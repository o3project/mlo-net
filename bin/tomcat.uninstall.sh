sudo service tomcat-7 stop
sudo rm -fr /opt/apache-tomcat-7.0.62/
sudo rm /opt/tomcat-7
sudo rm /etc/logrotate.d/tomcat-7
sudo update-rc.d -f tomcat-7 remove
sudo rm /etc/init.d/tomcat-7

