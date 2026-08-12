#!/usr/bin/env bash
# 应用部署 / 用法: sh start.sh [tag]
WORKDIR=$PWD
SCRIPT_DIR=$(cd "$(dirname "$0")" && pwd)

# 如果在 dist/ 下，移到上级目录
if [ "$(basename "$SCRIPT_DIR")" = "dist" ]; then
    echo "==> 从 dist/ 移动到上级目录..."
    cd "$SCRIPT_DIR/.."
    mv dist/* . 2>/dev/null || true
    rmdir dist 2>/dev/null || true
    SCRIPT_DIR=$(pwd)
fi

# 加载 .env
PARENT_DIR=$(cd "$SCRIPT_DIR/.." && pwd)
set -a
if [ -f "$SCRIPT_DIR/.env" ]; then
    source "$SCRIPT_DIR/.env"
elif [ -f "$PARENT_DIR/.env" ]; then
    source "$PARENT_DIR/.env"
fi
set +a

APP_NAME=${APP_NAME:-project}
IMAGE_TAG="${1:-$(date +%Y.%m.%d)}"

export IMAGE_TAG

echo "==> 停止旧服务..."
docker-compose -p ${APP_NAME}-service down --rmi local 2>/dev/null || true

echo "==> 构建镜像..."
sh build-image.sh "${IMAGE_TAG}" || exit 1

echo "==> 启动服务..."
docker-compose -p ${APP_NAME}-service up -d || exit 1

echo "==> 重启 Nginx..."
docker restart ${APP_NAME}-nginx 2>/dev/null || true

echo "==> 运行状态:"
docker ps |grep -E "${APP_NAME}"
