#!/bin/bash
TAG=ConcentrationSite

MAIN=server.main.Server$TAG
SERVER_PORT=22007
REPOS_HOSTNAME=coiso
REPOS_PORT=22008

echo "Server $TAG will run on port $SERVER_PORT"
./Run.sh $MAIN $SERVER_PORT $REPOS_HOSTNAME $REPOS_PORT