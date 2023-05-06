#!/bin/bash
TAG=AssaultParty

MAIN=server.main.Server$TAG
SERVER_PORT=22001
REPOS_HOSTNAME=coiso
REPOS_PORT=22002

echo "Server $TAG will run on port $SERVER_PORT"
./Run.sh $MAIN $SERVER_PORT $REPOS_HOSTNAME $REPOS_PORT