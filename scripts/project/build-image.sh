#!/usr/bin/env bash
# 构建 Docker 镜像
cd "$(dirname "$0")"

IMAGE_NAME="${1:-project}"
IMAGE_TAG="${2:-$(date +%Y.%m.%d)}"
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
