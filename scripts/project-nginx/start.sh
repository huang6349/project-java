#!/usr/bin/env bash
# Nginx 反向代理
WORKDIR=$PWD

# 加载 .env
SCRIPT_DIR=$(cd "$(dirname "$0")" && pwd)
set -a
source "$SCRIPT_DIR/../.env" 2>/dev/null || true
set +a

APP_NAME=${APP_NAME:-project}

# 准备并启动
mkdir -p $WORKDIR/data
chmod -R 755 $WORKDIR/.
sed "s|\${APP_NAME}|${APP_NAME}|g" default.conf.sample > config/nginx/site-confs/default.conf
docker-compose -p ${APP_NAME}-nginx up -d --build
