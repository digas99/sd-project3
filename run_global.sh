#!/bin/bash

if [ ! -f config ]; then
    echo "Missing config file!"
    exit 1
elif [ ! -f password ]; then
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

ASSAULTPARTYAPORT=$(get_value ASSAULT_PARTY_A_PORT config)
ASSAULTPARTYBPORT=$(get_value ASSAULT_PARTY_B_PORT config)
COLLECTIONSITEPORT=$(get_value COLLECTION_SITE_PORT config)
CONCENTRATIONSITEPORT=$(get_value CONCENTRATION_SITE_PORT config)
MUSEUMPORT=$(get_value MUSEUM_PORT config)

run_servers() {
    xterm -T "AssaultPartyA" -hold -e "sshpass -f password ssh $ASSAULTPARTYAHOST 'fuser -k $ASSAULTPARTYAPORT/tcp -k $((ASSAULTPARTYAPORT+1))/tcp; ./run_server.sh AssaultParty $ASSAULTPARTYAPORT localhost $((ASSAULTPARTYAPORT+1)) /lib/genclass.jar'" &
    xterm -T "AssaultPartyB" -hold -e "sshpass -f password ssh $ASSAULTPARTYBHOST 'fuser -k $ASSAULTPARTYBPORT/tcp -k $((ASSAULTPARTYBPORT+1))/tcp; ./run_server.sh AssaultParty $ASSAULTPARTYBPORT localhost $((ASSAULTPARTYBPORT+1)) /lib/genclass.jar'" &
    xterm -T "CollectionSite" -hold -e "sshpass -f password ssh $COLLECTIONSITEHOST 'fuser -k $COLLECTIONSITEPORT/tcp -k $((COLLECTIONSITEPORT+1)); ./run_server.sh CollectionSite $COLLECTIONSITEPORT localhost $((COLLECTIONSITEPORT+1)) /lib/genclass.jar'" &
    xterm -T "ConcentrationSite" -hold -e "sshpass -f password ssh $CONCENTRATIONSITEHOST 'fuser -k $CONCENTRATIONSITEPORT/tcp -k $((CONCENTRATIONSITEPORT+1)); ./run_server.sh ConcentrationSite $CONCENTRATIONSITEPORT localhost $((CONCENTRATIONSITEPORT+1)) /lib/genclass.jar'" &
    xterm -T "Museum" -hold -e "sshpass -f password ssh $MUSEUMHOST 'fuser -k $MUSEUMPORT/tcp -k $((MUSEUMPORT+1)); ./run_server.sh Museum $MUSEUMPORT localhost $((MUSEUMPORT+1)) /lib/genclass.jar'"
}

run_clients() {
    xterm -T "MasterThief" -hold -e "sshpass -f password ssh $MASTERTHIEFHOST './run_client.sh MasterThief /lib/genclass.jar'" &
    xterm -T "OrdinaryThief" -hold -e "sshpass -f password ssh $ORDINARYTHIEFHOST './run_client.sh OrdinaryThief /lib/genclass.jar'"
}

echo -e "\nRunning servers on machines, check the terminals that pop up on your screen."

if [ $# -eq 0 ]; then
  run_servers &
  run_clients
else
  if [ $1 = "servers" ]; then
    run_servers
  elif [ $1 = "clients" ]; then
    run_clients
  fi
fi