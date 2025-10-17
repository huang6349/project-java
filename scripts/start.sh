#!/usr/bin/env bash
WORKDIR=$PWD
PROJECT=${1:-project}
mkdir -p $WORKDIR/log
chmod -R 777 $WORKDIR/.
docker-compose -p ${PROJECT}-service up -d --build
docker ps |grep -E ${PROJECT}
