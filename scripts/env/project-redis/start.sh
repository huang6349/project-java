#!/usr/bin/env bash
WORKDIR=$PWD
mkdir -p $WORKDIR/data
chmod -R 777 $WORKDIR/.
docker-compose -p project-redis up -d --build
