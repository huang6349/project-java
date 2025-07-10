#!/usr/bin/env bash
WORKDIR=$PWD
cd $WORKDIR/project-minio/ && sh start.sh
cd $WORKDIR/project-mysql/ && sh start.sh
cd $WORKDIR/project-redis/ && sh start.sh
cd $WORKDIR/project-es/ && sh start.sh
cd $WORKDIR/project-nginx/ && sh start.sh
docker ps |grep project
