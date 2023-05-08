#!/bin/bash

if [ ! -d "temp" ]; then
  echo "Folder /temp is missing, please run build.sh first."
  exit 1
elif [ ! -f config ]; then
  echo "Missing config file!"
  exit 1
elif [ -f password ]; then
  echo "Missing password file!"
  exit 1
fi

get_value() {
    grep "$1" "$2" | cut -d "=" -f2
}

X=$(get_value TURMA config)
Y=$(get_value GRUPO config)
TAG=$X$Y

ASSAULTPARTYAHOST="sd$TAG@$(get_value ASSAULT_PARTY_A_MACHINE config)"
ASSAULTPARTYBHOST="sd$TAG@$(get_value ASSAULT_PARTY_B_MACHINE config)"
COLLECTIONSITEHOST="sd$TAG@$(get_value COLLECTION_SITE_MACHINE config)"
CONCENTRATIONSITEHOST="sd$TAG@$(get_value CONCENTRATION_SITE_MACHINE config)"
MUSEUMHOST="sd$TAG@$(get_value MUSEUM_MACHINE config)"
MASTERTHIEFHOST="sd$TAG@$(get_value MASTER_THIEF_MACHINE config)"
ORDINARYTHIEFHOST="sd$TAG@$(get_value ORDINARY_THIEF_MACHINE config)"

transfer_to_node() {
  sshpass -f password ssh $1 'mkdir -p test/Assault'
  sshpass -f password ssh $1 'rm -rf test/Assault/*'

  sshpass -f password ssh $1 'rm -rf lib'
  sshpass -f password scp temp/lib.zip $1:.

  sshpass -f password ssh $1 'rm -f run_server.sh'
  sshpass -f password ssh $1 'rm -f run_client.sh'
  sshpass -f password scp run_server.sh $1:.
  sshpass -f password scp run_client.sh $1:.

  sshpass -f password ssh $1 'rm -f config'
  sshpass -f password scp config $1:.

  sshpass -f password scp $2.zip $1:test/Assault
}

decompress_data() {
  sshpass -f password ssh $1 'unzip -uq lib.zip; rm -f lib.zip'
  sshpass -f password ssh $1 "cd test/Assault; unzip -uq $2.zip; rm -f $2.zip"
}

echo "Transfering data to computer nodes"
echo "  Assault Party A"
transfer_to_node $ASSAULTPARTYAHOST temp/dirAssaultParty
echo "  Assault Party B"
transfer_to_node $ASSAULTPARTYBHOST temp/dirAssaultParty
echo "  Collection Site"
transfer_to_node $COLLECTIONSITEHOST temp/dirCollectionSite
echo "  Concentration Site"
transfer_to_node $CONCENTRATIONSITEHOST temp/dirConcentrationSite
echo "  Museum"
transfer_to_node $MUSEUMHOST temp/dirMuseum
echo "  Master Thief"
transfer_to_node $MASTERTHIEFHOST temp/dirMasterThief
echo "  Ordinary Thief"
transfer_to_node $ORDINARYTHIEFHOST temp/dirOrdinaryThief

echo "Decompressing data sent to the computer nodes."
decompress_data $ASSAULTPARTYAHOST dirAssaultParty
decompress_data $ASSAULTPARTYBHOST dirAssaultParty
decompress_data $COLLECTIONSITEHOST dirCollectionSite
decompress_data $CONCENTRATIONSITEHOST dirConcentrationSite
decompress_data $MUSEUMHOST dirMuseum
decompress_data $MASTERTHIEFHOST dirMasterThief
decompress_data $ORDINARYTHIEFHOST dirOrdinaryThief