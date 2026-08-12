#!/usr/bin/env bash
# Maven 打包并复制 JAR 到部署目录
WORKDIR=$(cd "$(dirname "$0")" && pwd)

echo "==> Maven 打包..."
cd "$WORKDIR/.."
mvn clean package -pl project-web -am -Dmaven.test.skip=true || exit 1

echo "==> 复制 JAR..."
JAR=$(ls -t project-web/target/project-web*.jar 2>/dev/null | grep -v original | head -n 1)
cp "$JAR" "$WORKDIR/project-sample/"
echo "==> $(basename "$JAR") -> project-sample/"
