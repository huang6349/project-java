#!/usr/bin/env bash
# Maven 打包，输出到 dist 目录
WORKDIR=$(cd "$(dirname "$0")" && pwd)
DIST_DIR="$WORKDIR/../dist"

echo "==> Maven 打包..."
cd "$WORKDIR/.."
mvn clean package -pl project-web -am -Dmaven.test.skip=true || exit 1

echo "==> 准备 dist 目录..."
rm -rf "$DIST_DIR"
mkdir -p "$DIST_DIR"

echo "==> 复制 JAR..."
JAR=$(ls -t project-web/target/project-web*.jar 2>/dev/null | grep -v original | head -n 1)
cp "$JAR" "$DIST_DIR/"

echo "==> 复制部署文件..."
cp "$WORKDIR/project-sample/"* "$DIST_DIR/"

echo "==> 完成: $DIST_DIR"
ls -la "$DIST_DIR"
