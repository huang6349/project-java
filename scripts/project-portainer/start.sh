#!/usr/bin/env bash
# Portainer 容器管理
WORKDIR=$PWD

# 准备并启动
mkdir -p $WORKDIR/data
chmod -R 777 $WORKDIR/.
docker-compose -p portainer-ce up -d --build
