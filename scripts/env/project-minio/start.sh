#!/usr/bin/env bash
WORKDIR=$PWD
mkdir -p $WORKDIR/data
chmod -R 777 $WORKDIR/data
docker-compose -p project-minio up -d --build
