#!/usr/bin/env bash
WORKDIR=$PWD
PROJECT=${1:-project}
cd $WORKDIR/project-mysql/ && sh start.sh ${PROJECT}
cd $WORKDIR/project-minio/ && sh start.sh ${PROJECT}
cd $WORKDIR/project-redis/ && sh start.sh ${PROJECT}
cd $WORKDIR/project-es/ && sh start.sh ${PROJECT}
cd $WORKDIR/project-nginx/ && sh start.sh ${PROJECT}
cd $WORKDIR/project-backup/ && sh start.sh ${PROJECT}
docker ps |grep -E ${PROJECT}
