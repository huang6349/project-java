#!/usr/bin/env bash
# Elasticsearch 搜索引擎
WORKDIR=$PWD

# 加载 .env
SCRIPT_DIR=$(cd "$(dirname "$0")" && pwd)
set -a
source "$SCRIPT_DIR/../.env" 2>/dev/null || true
set +a

APP_NAME=${APP_NAME:-project}

# 准备并启动
mkdir -p $WORKDIR/data
chmod -R 777 $WORKDIR/data
docker-compose -p ${APP_NAME}-es up -d --build
