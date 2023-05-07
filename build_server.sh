#!/bin/bash

SRCFILES=src

rm -rf temp/dir$1
mkdir -p temp/dir$1 temp/dir$1/server temp/dir$1/server/main temp/dir$1/server/entities temp/dir$1/server/sharedRegions \
         temp/dir$1/client temp/dir$1/client/entities temp/dir$1/client/stubs temp/dir$1/utils
         
cp $SRCFILES/utils/Parameters.class $SRCFILES/server/main/Server$1.class temp/dir$1/server/main
cp $SRCFILES/server/entities/$1ClientProxy.class $SRCFILES/server/entities/ClientProxy.class temp/dir$1/server/entities
cp $SRCFILES/server/sharedRegions/$1*.class temp/dir$1/server/sharedRegions
cp $SRCFILES/client/entities/MasterThiefStates.class $SRCFILES/client/entities/OrdinaryThiefStates.class \
   temp/dir$1/client/entities
cp $SRCFILES/utils/*.class temp/dir$1/utils

echo "Compressing execution environments for $1."
rm -f  temp/dir$1.zip
cd temp
zip -rq dir$1.zip dir$1
cd ..