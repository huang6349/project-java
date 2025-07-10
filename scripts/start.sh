#!/usr/bin/env bash
WORKDIR=$PWD
mkdir -p $WORKDIR/log
chmod -R 777 $WORKDIR/.
docker-compose -p miniapp-l10n up -d --build
docker ps |grep miniapp-l10n
