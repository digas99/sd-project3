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
./build_servers.sh AssaultParty
./build_servers.sh CollectionSite
./build_servers.sh ConcentrationSite
./build_servers.sh Museum

# build clients
./build_clients.sh MasterThief
./build_clients.sh OrdinaryThief

echo "Deploying and decompressing execution environments."
mkdir -p $PWD/test/Assault
rm -rf $PWD/test/Assault/*
cp temp/dirAssaultParty.zip $PWD/test/Assault
cp temp/dirCollectionSite.zip $PWD/test/Assault
cp temp/dirConcentrationSite.zip $PWD/test/Assault
cp temp/dirMuseum.zip $PWD/test/Assault
cp temp/dirMasterThief.zip $PWD/test/Assault
cp temp/dirOrdinaryThief.zip $PWD/test/Assault

cd $PWD/test/Assault
unzip -q dirAssaultParty.zip
rm -f dirAssaultParty.zip
unzip -q dirCollectionSite.zip
rm -f dirCollectionSite.zip
unzip -q dirConcentrationSite.zip
rm -f dirConcentrationSite.zip
unzip -q dirMuseum.zip
rm -f dirMuseum.zip
unzip -q dirMasterThief.zip
rm -f dirMasterThief.zip
unzip -q dirOrdinaryThief.zip
rm -f dirOrdinaryThief.zip