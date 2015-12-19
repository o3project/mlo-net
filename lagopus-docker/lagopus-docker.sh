#!/bin/bash

cwd=$(pwd)
workdir=${cwd}/workspace

check_user() {
    if [ `whoami` = "root" ]; then
        echo "Super user cannot execute! Please execute as non super user"
        exit 2
    fi
}

extract_tag() {
    local from=`grep "^FROM" Dockerfile | awk '{print $2}'`
    echo $from | grep ":"  >/dev/null
    if [ $? -eq 0 ]; then
        local name=`echo $from | sed -e "s/^\([^:]*\):\(.*\)$/\1/"`
        local tag=`echo $from | sed -e "s/^\([^:]*\):\(.*\)$/\2/"`
        TAG="${name}"
        [ -n "${tag}" ] && TAG="${TAG}_${tag}"
    fi
}

docker_build_lagopus_vswitch() {
    extract_tag
    sudo docker build --no-cache -t lagopus-vswitch:${TAG} .
}

mod_sudoers() {
    local user=`whoami`
    echo "sudo echo \"${user} ALL=(ALL:ALL) NOPASSWD: ALL\" >/etc/sudoers.d/${user}" >mod_sudoers.sh
    echo "sudo chmod 0440 /etc/sudoers.d/${user}" >>mod_sudoers.sh
    chmod 755 mod_sudoers.sh
    sudo ./mod_sudoers.sh
    rm -f ./mod_sudoers.sh
}

start() {
    local conf=$1
    if [ -z "$conf" ]; then
        ${cwd}/conf-docker ${cwd}/samples/topo.conf
    else
        ${cwd}/conf-docker ${conf}
    fi
    [ $? -eq 0 ] && ${workdir}/run quickstart
}

stop() {
    if [ -f "${workdir}/run" ]; then
        ${workdir}/run kill_all >/dev/null 2>&1
        docker rm -f $(docker ps -qa) >/dev/null 2>&1
    else
        docker rm -f $(docker ps -qa) >/dev/null 2>&1
    fi
}

clean() {
    [ -f "${workdir}/run" ] && ${workdir}/run clean >/dev/null 2>&1
}

do_status() {
    local cmd=$1
    [ "${cmd}" ] || cmd="start"
    local count=0
    [ -n "$(docker ps -qa)" ] && count=`docker ps -qa | wc -l`
    case "${cmd}" in
        start)
            if [ -f "${workdir}/run" -a -f "${workdir}/count" ]; then
                [ $(cat "${workdir}/count") -eq ${count} ] || return 1
                return 0
            else
                return 4
            fi
            ;;
        stop)
            [ ${count} -eq 0 ] || return 1
            return 0
            ;;
        *)
            exit 2
	    ;;
    esac
}

case "$1" in
    start)
        do_status stop
        [ $? -eq 0 ] || stop
        for ((;;)); do
            do_status stop
            [ $? -eq 0 ] && break
            sleep 1
        done
        echo "Starting..."
	start $2
	;;
    stop)
        echo "Stopping..."
	stop
	;;
    clean)
        echo "Cleaning..."
	clean
	;;
    status)
	do_status $2
	exit $?
	;;
    install)
	check_user
	sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys 36A1D7869245C8950F966E92D8576A8BA88D21E9
	sudo sh -c "echo deb https://get.docker.io/ubuntu docker main > /etc/apt/sources.list.d/docker.list"
	sudo apt-get update
	sudo apt-get install -y --force-yes lxc-docker-1.9.1
	sudo ln -sf /usr/bin/docker.io /usr/local/bin/docker
	sudo gpasswd -a `whoami` docker
	sudo apt-get install -y --force-yes iputils-arping
	sudo apt-get install -y --force-yes bridge-utils
	sudo apt-get install -y --force-yes curl
	sudo service docker restart
	sudo docker pull osrg/ryu
	sudo docker run --name ryu-p1 -it osrg/ryu sed -i -e "s/^LLDP_MAC_NEAREST_BRIDGE/###LLDP_MAC_NEAREST_BRIDGE/" /root/ryu-master/ryu/lib/packet/lldp.py
	sudo docker commit ryu-p1 ryu:lldp
	sudo docker run --name ryu-p2 -it ryu:lldp sed -i -e "/^###LLDP_MAC_NEAREST_BRIDGE/i LLDP_MAC_NEAREST_BRIDGE = 'ff:ff:ff:ff:ff:ff'" /root/ryu-master/ryu/lib/packet/lldp.py
	sudo docker commit ryu-p2 ryu:lldp
	sudo docker run --name ryu-p3 -it ryu:lldp bash -c "cd /root/ryu-master; python ./setup.py install"
	sudo docker commit ryu-p3 ryu:lldp
	sudo docker run --name ryu-p4 -it ryu:lldp bash -c "pip install netaddr six pbr --upgrade"
	sudo docker commit ryu-p4 ryu:lldp
	sudo docker rm -f ryu-p1 ryu-p2 ryu-p3 ryu-p4
	docker_build_lagopus_vswitch
	mod_sudoers
	;;
    *)
	echo "Usage: lagopus-docker.sh {stop|install|clean}"
	echo "                         {start [CONFIG]}"
	echo "                         {status [{start|stop}]}"
	exit 2
	;;
esac
