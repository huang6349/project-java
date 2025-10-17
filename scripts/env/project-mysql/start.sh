#!/usr/bin/env bash
WORKDIR=$PWD
PROJECT=${1:-project}
mkdir -p $WORKDIR/data
mkdir -p $WORKDIR/log
chmod -R 777 $WORKDIR/.
chmod 644 mysqld.cnf
docker-compose -p ${PROJECT}-mysql up -d --build
