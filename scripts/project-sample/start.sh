#!/usr/bin/env bash
# 应用部署 / 用法: sh start.sh [tag]
WORKDIR=$PWD

# 加载 .env
SCRIPT_DIR=$(cd "$(dirname "$0")" && pwd)
set -a
source "$SCRIPT_DIR/../.env" 2>/dev/null || true
set +a

APP_NAME=${APP_NAME:-project}
IMAGE_TAG="${1:-$(date +%Y.%m.%d)}"

export IMAGE_TAG

echo "==> 停止旧服务..."
docker-compose -p ${APP_NAME}-service down --rmi local 2>/dev/null || true

echo "==> 构建镜像..."
./build-image.sh "${APP_NAME}" "${IMAGE_TAG}" || exit 1

echo "==> 启动服务..."
docker-compose -p ${APP_NAME}-service up -d || exit 1

echo "==> 重启 Nginx..."
docker restart ${APP_NAME}-nginx 2>/dev/null || true

echo "==> 运行状态:"
docker ps |grep -E "${APP_NAME}"
