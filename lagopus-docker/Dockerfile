FROM ubuntu:14.04

RUN apt-get update -y

### Lagopus vswitch installation w/o Intel DPDK
RUN apt-get install -y build-essential libexpat-dev libgmp-dev libncurses-dev \
	libssl-dev libpcap-dev byacc flex libreadline-dev \
	python-dev python-pastedeploy python-paste python-twisted git
WORKDIR /src
RUN git clone -b release-0.1.2 https://github.com/lagopus/lagopus.git
WORKDIR /src/lagopus
RUN ./configure
RUN make OPT_CFLAGS=-O2
RUN make install

RUN apt-get install -y tcpdump curl iperf

WORKDIR /src/workspace
