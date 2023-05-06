#!/bin/bash

SRCFILES=src

rm -rf temp/dir$1
mkdir -p temp/dir$1 temp/dir$1/server temp/dir$1/server/main temp/dir$1/client temp/dir$1/client/main temp/dir$1/client/entities \
         temp/dir$1/client/stubs temp/dir$1/utils

cp $SRCFILES/utils/Parameters.class temp/dir$1/server/main
cp $SRCFILES/client/main/Client$1.class temp/dir$1/client/main
cp $SRCFILES/client/entities/Thief.class $SRCFILES/client/entities/$1.class $SRCFILES/client/entities/$1States.class temp/dir$1/client/entities
cp $SRCFILES/client/stubs/AssaultPartyStub*.class $SRCFILES/client/stubs/CollectionSiteStub*.class $SRCFILES/client/stubs/ConcentrationSiteStub*.class $SRCFILES/client/stubs/MuseumStub*.class temp/dir$1/client/stubs
cp $SRCFILES/utils/Message.class $SRCFILES/utils/MessageType.class $SRCFILES/utils/MessageException.class $SRCFILES/utils/ClientCom.class temp/dir$1/utils

echo "Compressing execution environments for $1."
rm -f  temp/dir$1.zip
cd temp
zip -rq dir$1.zip dir$1
cd ..