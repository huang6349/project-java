#!/usr/bin/env bash
# Redis 缓存
WORKDIR=$PWD

# 加载 .env
SCRIPT_DIR=$(cd "$(dirname "$0")" && pwd)
set -a
source "$SCRIPT_DIR/../.env" 2>/dev/null || true
set +a

APP_NAME=${APP_NAME:-project}

# 准备并启动
mkdir -p $WORKDIR/data
chmod -R 777 $WORKDIR/.
sed "s|\${REDIS_PASSWORD}|${REDIS_PASSWORD}|g" redis-stack.conf.sample > redis-stack.conf
docker-compose -p ${APP_NAME}-redis up -d --build
