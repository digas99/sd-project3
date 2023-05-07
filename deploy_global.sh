#!/bin/bash

if [ ! -d "temp" ]; then
  echo "Folder /temp is missing, please run build.sh first."
  exit 1
fi

X=3
Y=04
TAG=$X$Y

ASSAULTPARTYAHOST=sd$TAG@l040101-ws01.ua.pt
ASSAULTPARTYBHOST=sd$TAG@l040101-ws02.ua.pt
COLLECTIONSITEHOST=sd$TAG@l040101-ws03.ua.pt
CONCENTRATIONSITEHOST=sd$TAG@l040101-ws04.ua.pt
MUSEUMHOST=sd$TAG@l040101-ws05.ua.pt
MASTERTHIEFHOST=sd$TAG@l040101-ws06.ua.pt
ORDINARYTHIEFHOST=sd$TAG@l040101-ws07.ua.pt

PORT=$((22000 + 100 * $X + 10 * ($Y - 1)))

transfer_to_node() {
  sshpass -f password ssh $1 'mkdir -p test/Assault'
  sshpass -f password ssh $1 'rm -rf test/Assault/*'
  sshpass -f password ssh $1 'rm -rf lib'
  sshpass -f password scp temp/lib.zip $1:.
  sshpass -f password ssh $1 'rm -f run_server.sh'
  sshpass -f password ssh $1 'rm -f run_client.sh'
  sshpass -f password scp run_server.sh $1:.
  sshpass -f password scp run_client.sh $1:.
  sshpass -f password scp $2.zip $1:test/Assault
}

decompress_data() {
  sshpass -f password ssh $1 'unzip -uq lib.zip; rm -f lib.zip'
  sshpass -f password ssh $1 "cd test/Assault; unzip -uq $2.zip; rm -f $2.zip"
}

echo "Transfering data to computer nodes"
transfer_to_node $ASSAULTPARTYAHOST temp/dirAssaultParty
transfer_to_node $ASSAULTPARTYBHOST temp/dirAssaultParty
transfer_to_node $COLLECTIONSITEHOST temp/dirCollectionSite
transfer_to_node $CONCENTRATIONSITEHOST temp/dirConcentrationSite
transfer_to_node $MUSEUMHOST temp/dirMuseum
transfer_to_node $MASTERTHIEFHOST temp/dirMasterThief
transfer_to_node $ORDINARYTHIEFHOST temp/dirOrdinaryThief

echo "Decompressing data sent to the computer nodes."
decompress_data $ASSAULTPARTYAHOST dirAssaultParty
decompress_data $ASSAULTPARTYBHOST dirAssaultParty
decompress_data $COLLECTIONSITEHOST dirCollectionSite
decompress_data $CONCENTRATIONSITEHOST dirConcentrationSite
decompress_data $MUSEUMHOST dirMuseum
decompress_data $MASTERTHIEFHOST dirMasterThief
decompress_data $ORDINARYTHIEFHOST dirOrdinaryThief