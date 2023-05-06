echo "Compiling source code."
javac */*.java */*/*.java

echo "Distributing intermediate code to the different execution environments."

echo "  AssaultParty"
rm -rf dirAssaultParty
mkdir -p dirAssaultParty dirAssaultParty/server dirAssaultParty/server/main dirAssaultParty/server/entities dirAssaultParty/server/sharedRegions \
         dirAssaultParty/client dirAssaultParty/client/entities dirAssaultParty/client/stubs dirAssaultParty/utils
cp server/main/Parameters.class server/main/ServerAssaultParty.class dirAssaultParty/server/main
cp server/entities/AssaultPartyClientProxy.class dirAssaultParty/server/entities
cp server/sharedRegions/AssaultPartyInterface.class server/sharedRegions/AssaultParty.class dirAssaultParty/server/sharedRegions
cp client/entities/MasterThiefStates.class client/entities/OrdinaryThiefStates.class \
   dirAssaultParty/client/entities
cp utils/*.class dirAssaultParty/utils

echo "Compressing execution environments."
echo "  AssaultParty"
rm -f  dirAssaultParty.zip
zip -rq dirAssaultParty.zip dirBarberShop

echo "Deploying and decompressing execution environments."
mkdir -p $PWD/test/Assault
rm -rf $PWD/test/Assault/*
cp dirAssaultParty.zip $PWD/test/Assault
cd $PWD/test/Assault
unzip -q dirAssaultParty.zip