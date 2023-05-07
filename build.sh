#!/bin/bash

if [ "$1" != "local" ] && [ "$1" != "global" ]; then
  echo "Invalid argument. Use 'local' or 'global'."
  exit 1
fi

echo "Compiling source code."

SRCFILES=src
GENCLASS=/lib/genclass.jar
CLASSPATH=$PWD$SRCFILES:$PWD$GENCLASS

javac -cp $CLASSPATH src/*/*.java src/*/*/*.java

echo "Distributing intermediate code to the different execution environments."

if [ ! -d "temp" ]; then
  mkdir temp
fi

# build servers
./build_server.sh AssaultParty
./build_server.sh CollectionSite
./build_server.sh ConcentrationSite
./build_server.sh Museum

# build clients
./build_client.sh MasterThief
./build_client.sh OrdinaryThief

# build genclass dependency
mkdir -p temp/lib
rm -rf temp/lib/*
cp lib/genclass.jar temp/lib
cd temp
zip -rq lib.zip lib
cd ..

if [ "$1" == "local" ]; then
  ./deploy_local.sh
elif [ "$1" == "global" ]; then
  ./deploy_global.sh
else
  echo "Invalid argument. Use 'local' or 'global'."
fi