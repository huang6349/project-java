#!/usr/bin/env bash
WORKDIR=$PWD
PROJECT=${1:-project}
mkdir -p $WORKDIR/snapshots
mkdir -p $WORKDIR/data
chmod -R 777 $WORKDIR/.
docker-compose -p ${PROJECT}-qdrant up -d --build
