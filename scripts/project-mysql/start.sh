#!/usr/bin/env bash
# MySQL 数据库
WORKDIR=$PWD

# 加载 .env
SCRIPT_DIR=$(cd "$(dirname "$0")" && pwd)
set -a
source "$SCRIPT_DIR/../.env" 2>/dev/null || true
set +a

APP_NAME=${APP_NAME:-project}

# 准备并启动
mkdir -p $WORKDIR/data
mkdir -p $WORKDIR/log
chmod -R 755 $WORKDIR/.
chmod 644 mysqld.cnf
docker-compose -p ${APP_NAME}-mysql up -d --build
