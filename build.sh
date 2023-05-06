echo "Compiling source code."

SRCFILES=src
GENCLASS=/lib/genclass.jar
CLASSPATH=$PWD$SRCFILES:$PWD$GENCLASS

javac -cp $CLASSPATH src/*/*.java src/*/*/*.java

echo "Distributing intermediate code to the different execution environments."

if [ ! -d "temp" ]; then
  mkdir temp
fi

echo "  AssaultParty"
rm -rf temp/dirAssaultParty
mkdir -p temp/dirAssaultParty temp/dirAssaultParty/server temp/dirAssaultParty/server/main temp/dirAssaultParty/server/entities temp/dirAssaultParty/server/sharedRegions \
         temp/dirAssaultParty/client temp/dirAssaultParty/client/entities temp/dirAssaultParty/client/stubs temp/dirAssaultParty/utils
cp src/utils/Parameters.class src/server/main/ServerAssaultParty.class temp/dirAssaultParty/server/main
cp src/server/entities/AssaultPartyClientProxy.class temp/dirAssaultParty/server/entities
cp src/server/sharedRegions/AssaultPartyInterface.class src/server/sharedRegions/AssaultParty.class temp/dirAssaultParty/server/sharedRegions
cp src/client/entities/MasterThiefStates.class src/client/entities/OrdinaryThiefStates.class \
   temp/dirAssaultParty/client/entities
cp src/utils/*.class temp/dirAssaultParty/utils

echo "Compressing execution environments."
echo "  AssaultParty"
rm -f  temp/dirAssaultParty.zip
cd temp
zip -rq dirAssaultParty.zip dirAssaultParty
cd ..

echo "Deploying and decompressing execution environments."
mkdir -p $PWD/test/Assault
rm -rf $PWD/test/Assault/*
cp temp/dirAssaultParty.zip $PWD/test/Assault
cd $PWD/test/Assault
unzip -q dirAssaultParty.zip