mlo-net
==============
MLO-NET is a SDN packet transport software suite which supports multi-layer orchestration, control and network emulation.

Supported platform
--------
- CPU architecture: x64
- Memory: 1GB or more
- HDD space: 8GB or more.
- Operating system: Ubuntu 14.04 LTS (Trusty Tahr)

The easiest way to prepare your environment is to construct it on 
virtual machine.
Especially, for convenience, it is strongly recommended that mlo-net 
envirionment is constructed as guest OS on Oracle VM VirtualBox 
installed on Windows 7 or 8.1.

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
$ sudo apt-get update -y
...

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
- openssh-server
- jdk7
- maven
- [ODENOS 1.0.0](https://github.com/o3project/odenos/releases/tag/1.0.0)
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




