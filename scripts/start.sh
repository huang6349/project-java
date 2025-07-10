#!/usr/bin/env bash
WORKDIR=$PWD
mkdir -p $WORKDIR/log
chmod -R 777 $WORKDIR/.
docker-compose -p project up -d --build
docker ps |grep project
