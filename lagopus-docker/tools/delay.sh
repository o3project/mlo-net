#!/bin/sh

tc qdisc add dev $1 root netem delay "$2""ms"
#tc qdisc change dev $1 root netem delay "$2""ms"
#tc qdisc show dev $1
#tc qdisc del dev $1 root
