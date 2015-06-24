sudo install -o root -g root -m 0777 -d /var/log/mlo/
sudo install -o root -g root -m 0755 ./notify_node_alarm_to_mlo.py /etc/zabbix/alert.d/

