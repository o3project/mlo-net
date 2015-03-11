lagopus-docker
==============
lagopus-docker is a Docker container based general purpose network emulator which is built on OpenFlow 1.3 Lagopus vswitch and RYU OpenFlow controller.
Currently lagopus-docker can emulate MPLS and VLAN node and host.

All of the code is freely available under the Apache 2.0 license. lagopus-docker is written in bash.

Preparation
-----------
Set up Ubuntu 14.04 Desktop Edition Virtual Machine environment. We
tested this with VirtualBox on Windows 8.

Setup
-----
Open a terminal and execute the following commands:

```
% ./lagopus-docker.sh install
```
All necessary software will be installed.
when installation is finished, gracefully reboot the system.
```

% ./lagopus-docker.sh start [CONFIG]
```
Network emulator will be launched.
Note that if CONFIG is omitted, 'samples/topo.conf' will be used.
See 'samples' directory for a different configuration.
```

% ./lagopus-docker.sh stop
```
Network emulator will be stopped.
```

% ./lagopus-docker.sh clean
```
Files created by network emulator and Docker image named <none> will be deleted.
```

% ./lagopus-docker.sh status [start]
```
Network emulator status will be reported as below.
0: all containers have been started.
1: some containers are not running or launch in progress.
4: failed to start.
```

% ./lagopus-docker.sh status stop
```
Network emulator status will be reported as below.
0: all containers are already stopped.
1: some containers are still running.
```
