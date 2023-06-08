echo "Compiling source code."
javac */*.java */*/*.java
echo "Distributing intermediate code to the different execution environments."
echo "  Register Remote Objects"
rm -rf dirRegistry/server dirRegistry/interfaces
mkdir -p dirRegistry/server dirRegistry/server/main dirRegistry/server/objects dirRegistry/interfaces
cp server/main/ServerRegisterRemoteObject.class dirRegistry/server/main
cp server/objects/RegisterRemoteObject.class dirRegistry/server/objects
cp interfaces/Register.class dirRegistry/interfaces

#Shared Regions
echo "  Collection Site"
rm -rf dirCollectionSite/server dirCollectionSite/client dirCollectionSite/interfaces dirCollectionSite/utils
mkdir -p dirCollectionSite/server dirCollectionSite/server/main dirCollectionSite/server/objects dirCollectionSite/interfaces \
         dirCollectionSite/client dirCollectionSite/client/entities dirCollectionSite/utils
cp utils/Parameters.class server/main/ServerCollectionSite.class dirCollectionSite/server/main
cp server/objects/CollectionSite.class dirCollectionSite/server/objects
cp interfaces/*.class dirCollectionSite/interfaces
cp client/entities/MasterThiefStates.class client/entities/OrdinaryThiefStates.class dirCollectionSite/client/entities
cp utils/*.class dirCollectionSite/utils

echo "  Concentration Site"
rm -rf dirConcentrationSite/server dirConcentrationSite/client dirConcentrationSite/interfaces dirConcentrationSite/utils
mkdir -p dirConcentrationSite/server dirConcentrationSite/server/main dirConcentrationSite/server/objects dirConcentrationSite/interfaces \
         dirConcentrationSite/client dirConcentrationSite/client/entities dirConcentrationSite/utils
cp utils/Parameters.class server/main/ServerConcentrationSite.class dirConcentrationSite/server/main
cp server/objects/ConcentrationSite.class dirConcentrationSite/server/objects
cp interfaces/*.class dirConcentrationSite/interfaces
cp client/entities/MasterThiefStates.class client/entities/OrdinaryThiefStates.class dirConcentrationSite/client/entities
cp utils/*.class dirConcentrationSite/utils


echo "  Museum"
rm -rf dirMuseum/server dirMuseum/client dirMuseum/interfaces dirMuseum/utils
mkdir -p dirMuseum/server dirMuseum/server/main dirMuseum/server/objects dirMuseum/interfaces \
         dirMuseum/client dirMuseum/client/entities dirMuseum/utils
cp utils/Parameters.class server/main/ServerMuseum.class dirMuseum/server/main
cp server/objects/Museum.class dirMuseum/server/objects
cp interfaces/*.class dirMuseum/interfaces
cp client/entities/MasterThiefStates.class client/entities/OrdinaryThiefStates.class dirMuseum/client/entities
cp utils/*.class dirMuseum/utils


echo "  Assault Party A"
rm -rf dirAssaultPartyA/server dirAssaultPartyA/client dirAssaultPartyA/interfaces dirAssaultPartyA/utils
mkdir -p dirAssaultPartyA/server dirAssaultPartyA/server/main dirAssaultPartyA/server/objects dirAssaultPartyA/interfaces \
         dirAssaultPartyA/client dirAssaultPartyA/client/entities dirAssaultPartyA/utils
cp utils/Parameters.class server/main/ServerAssaultParty.class dirAssaultParty/server/main
cp server/objects/AssaultParty.class dirAssaultParty/server/objects
cp interfaces/*.class dirAssaultParty/interfaces
cp client/entities/MasterThiefStates.class client/entities/OrdinaryThiefStates.class dirAssaultParty/client/entities
cp utils/*.class dirAssaultParty/utils

echo "  Assault Party B"
rm -rf dirAssaultPartyB/server dirAssaultPartyB/client dirAssaultPartyB/interfaces dirAssaultPartyB/utils
mkdir -p dirAssaultPartyB/server dirAssaultPartyB/server/main dirAssaultPartyB/server/objects dirAssaultPartyB/interfaces \
         dirAssaultPartyB/client dirAssaultPartyB/client/entities dirAssaultPartyB/utils
cp utils/Parameters.class server/main/ServerAssaultParty.class dirAssaultParty/server/main
cp server/objects/AssaultParty.class dirAssaultParty/server/objects
cp interfaces/*.class dirAssaultParty/interfaces
cp client/entities/MasterThiefStates.class client/entities/OrdinaryThiefStates.class dirAssaultParty/client/entities
cp utils/*.class dirAssaultParty/utils


#Entities
echo "  Master Thief"
rm -rf dirMasterThief/server dirMasterThief/client dirMasterThief/interfaces
mkdir -p dirMasterThief/server dirMasterThief/server/main dirMasterThief/client dirMasterThief/client/main dirMasterThief/client/entities \
         dirMasterThief/interfaces
cp utils/Parameters.class dirMasterThief/server/main
cp client/main/ClientMasterThief.class dirMasterThief/client/main
cp client/entities/MasterThief.class client/entities/MasterThiefStates.class dirMasterThief/client/entities
cp interfaces/AssaultPartyInterface.class CollectionSiteInterface.class ConcentrationSiteInterface.class MuseumInterface.class interfaces/ReturnBoolean.class dirMasterThief/interfaces

echo "  Ordinary Thief"
rm -rf dirOrdinaryThief/server dirOrdinaryThief/client dirOrdinaryThief/interfaces
mkdir -p dirOrdinaryThief/server dirOrdinaryThief/server/main dirOrdinaryThief/client dirOrdinaryThief/client/main dirOrdinaryThief/client/entities \
         dirOrdinaryThief/interfaces
cp utils/Parameters.class dirOrdinaryThief/server/main
cp client/main/ClientOrdinaryThief.class dirOrdinaryThief/client/main
cp client/entities/OrdinaryThief.class client/entities/OrdinaryThiefStates.class dirOrdinaryThief/client/entities
cp interfaces/AssaultPartyInterface.class CollectionSiteInterface.class ConcentrationSiteInterface.class MuseumInterface.class interfaces/ReturnBoolean.class dirOrdinaryThief/interfaces


echo "Compressing execution environments."
echo "  Register Remote Objects"
rm -f  dirRegistry.zip
zip -rq dirRegistry.zip dirRegistry set_rmiregistry_alt.sh

echo "  Collection Site"
rm -f  dirCollectionSite.zip
zip -rq dirCollectionSite.zip dirCollectionSite
echo "  Concentration Site"
rm -f  dirConcentrationSite.zip
zip -rq dirConcentrationSite.zip dirConcentrationSite
echo "  Museum"
rm -f  dirMuseum.zip
zip -rq dirMuseum.zip dirMuseum
echo "  Assault Party A"
rm -f  dirAssaultPartyA.zip
zip -rq dirAssaultPartyA.zip dirAssaultPartyA
echo "  Assault Party B"
rm -f  dirAssaultPartyB.zip
zip -rq dirAssaultPartyB.zip dirAssaultPartyB

echo "  Master Thief"
rm -f  dirMasterThief.zip
zip -rq dirMasterThief.zip diMasterThief
echo "  Ordinary Thief"
rm -f  dirOrdinaryThief.zip
zip -rq dirOrdinaryThief.zip dirOrdinaryThief


echo "Deploying and decompressing execution environments."
mkdir -p /home/$1/test/Heist
rm -rf /home/$1/test/Heist/*
cp dirRegistry.zip /home/$1/test/Heist
cp dirCollectionSite.zip /home/$1/test/Heist
cp dirCollectionSite.zip /home/$1/test/Heist
cp dirMuseum.zip /home/$1/test/Heist
cp dirAssaultPartyA.zip /home/$1/test/Heist
cp dirAssaultPartyB.zip /home/$1/test/Heist

cp dirMasterThief.zip /home/$1/test/Heist
cp dirOrdinaryThief.zip /home/$1/test/Heist

cd /home/$1/test/Heist
unzip -q dirRegistry.zip
mv set_rmiregistry_alt.sh /home/$1
unzip -q dirCollectionSite.zip
unzip -q dirConcentrationSite.zip
unzip -q dirMuseum.zip
unzip -q dirAssaultPartyA.zip
unzip -q dirAssaultPartyB.zip

unzip -q dirMasterThief.zip
unzip -q dirOrdinaryThief.zip
