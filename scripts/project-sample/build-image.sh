#!/usr/bin/env bash
# 构建 Docker 镜像
SCRIPT_DIR=$(cd "$(dirname "$0")" && pwd)
PARENT_DIR=$(cd "$SCRIPT_DIR/.." && pwd)

# 加载 .env
set -a
if [ -f "$SCRIPT_DIR/.env" ]; then
    source "$SCRIPT_DIR/.env"
elif [ -f "$PARENT_DIR/.env" ]; then
    source "$PARENT_DIR/.env"
fi
set +a

IMAGE_NAME=${IMAGE_NAME:-${APP_NAME:-project}}
IMAGE_TAG="${1:-$(date +%Y.%m.%d)}"
FULL_IMAGE="${IMAGE_NAME}:${IMAGE_TAG}"

echo "==> 构建镜像: ${FULL_IMAGE}"

# 删除同名旧镜像
if docker images -q "${IMAGE_NAME}" | grep -q .; then
    echo "==> 删除旧镜像..."
    docker rmi -f $(docker images -q "${IMAGE_NAME}") 2>/dev/null || true
fi

echo "==> 编译新镜像..."
docker build -t "${FULL_IMAGE}" .

echo "==> 构建完成: ${FULL_IMAGE}"
docker images "${IMAGE_NAME}"

# 清理 JAR
if [ "${KEEP_JAR:-true}" != "true" ]; then
    rm -f "$SCRIPT_DIR"/project-web*.jar
    echo "==> 已删除 JAR"
fi
