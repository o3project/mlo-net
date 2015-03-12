LANG=C /sbin/ifconfig $1 | grep 'inet addr' | grep -v 127.0.0.1 | awk '{print $2;}' | cut -d: -f2
