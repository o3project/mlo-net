mlo-net
==============
MLO-NET is a SDN packet transport software suite which supports multi-layer orchestration, control and network emulation.

Supported platform
--------
- CPU architecture: x64
- Memory: 1GB or more
- HDD space: 8GB or more.
- Operation system: Ubuntu 14.04 LTS

The easiest way to prepare your platform is to construct it on 
virtual machine environment.
Recomended environment is Oracle VM VirtualBox.

Getting started
--------

At the beginning, you need to add an user account as super-user 
on ubuntu, which has userid "developer" and the password "developer".
This user account is default settings of mlo-net.

```
$ sudo adduser developer
...

$ sudo gpasswd -a developer sudo
...

$
```
And then, re-login as this user.

Next, on the "developer" user, executes commands as follows:

```
$ sudo apt-get install git-core -y
...

$ cd /home/developer/
$ git clone https://github.com/o3project/mlo-net.git
...

$ cd mlo-net/
$ ./setup.sh
...

$ sudo reboot
...

$ cd /home/developer/mlo-net/
$ ./install.sh
...

$

```

Prerequisites
--------
The following software is required.
All are installed in executing *setup.sh* command.

- lagopus-docker, including docker.io and so on.
- jdk-7
- maven
- ODENOS
- tomcat6, tomcat6-admin


Building and deploying mlo-net
--------
Building and deploying mlo-net is executed in *install.sh* command.

Restarting mlo-net
--------
To restarting mlo-net, executes the following command.
```
$ ./system.restart
...

$ 
```




