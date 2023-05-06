#!/bin/bash
MAIN=server.main.Server$1
SERVER_PORT=$2
REPOS_HOSTNAME=$3
REPOS_PORT=$4

BINPATH=BINPATH=/test/Assault/dir$1
GENCLASS=/lib/genclass.jar
CLASSPATH=$PWD$BINPATH:$PWD$GENCLASS

java -cp $CLASSPATH $MAIN $SERVER_PORT $REPOS_HOSTNAME $REPOS_PORT

echo "Server $1 running on port $SERVER_PORT"