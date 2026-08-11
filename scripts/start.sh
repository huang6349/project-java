#!/usr/bin/env bash
# 开发环境一键启动
WORKDIR=$(cd "$(dirname "$0")" && pwd)

# 加载 .env（不存在则从 .sample 生成）
if [ ! -f "$WORKDIR/.env" ]; then
    if [ ! -f "$WORKDIR/env.sample" ]; then
        echo "错误: 未找到 env.sample，请在 scripts/ 目录下执行 sh start.sh"
        exit 1
    fi
    cp "$WORKDIR/env.sample" "$WORKDIR/.env"
    echo "==> 已生成 .env，请根据需要修改配置"
fi
set -a
source "$WORKDIR/.env"
set +a

APP_NAME=${APP_NAME:-project}

# 创建共享网络
docker network create ${APP_NAME} 2>/dev/null || true

# 启动服务
cd $WORKDIR/project-qdrant/ && sh start.sh
cd $WORKDIR/project-mysql/ && sh start.sh
cd $WORKDIR/project-minio/ && sh start.sh
cd $WORKDIR/project-redis/ && sh start.sh
cd $WORKDIR/project-es/ && sh start.sh
cd $WORKDIR/project-nginx/ && sh start.sh

docker ps |grep -E ${APP_NAME}
