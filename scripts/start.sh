#!/usr/bin/env bash
WORKDIR=$PWD
mkdir -p $WORKDIR/log
chmod -R 777 $WORKDIR/.
docker-compose -p project-service up -d --build
docker ps |grep -E "project"
