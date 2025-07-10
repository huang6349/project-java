#!/usr/bin/env bash
WORKDIR=$PWD
mkdir -p $WORKDIR/data
mkdir -p $WORKDIR/log
chmod -R 777 $WORKDIR/.
chmod 644 mysqld.cnf
docker-compose -p project-mysql up -d --build
