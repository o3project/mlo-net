#!/bin/sh

. ./topo.conf
for controller in $CONTROLLERS; do
  docker inspect --format="{{ .NetworkSettings.IPAddress }}" $controller 
done
