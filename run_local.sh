#!/bin/bash

ASSAULTPARTYAPORT=$(get_value ASSAULT_PARTY_A_PORT config)
ASSAULTPARTYBPORT=$(get_value ASSAULT_PARTY_B_PORT config)
COLLECTIONSITEPORT=$(get_value COLLECTION_SITE_PORT config)
CONCENTRATIONSITEPORT=$(get_value CONCENTRATION_SITE_PORT config)
MUSEUMPORT=$(get_value MUSEUM_PORT config)

run_servers() {
  xterm -T "AssaultPartyA" -hold -e "./run_server.sh AssaultParty $ASSAULTPARTYAPORT localhost $((ASSAULTPARTYAPORT+1)) /lib/genclass.jar" &
  xterm -T "AssaultPartyB" -hold -e "./run_server.sh AssaultParty $ASSAULTPARTYBPORT localhost $((ASSAULTPARTYBPORT+1)) /lib/genclass.jar" &
  xterm -T "CollectionSite" -hold -e "./run_server.sh CollectionSite $COLLECTIONSITEPORT localhost $((COLLECTIONSITEPORT+1)) /lib/genclass.jar" &
  xterm -T "ConcentrationSite" -hold -e "./run_server.sh ConcentrationSite $CONCENTRATIONSITEPORT localhost $((CONCENTRATIONSITEPORT+1)) /lib/genclass.jar" &
  xterm -T "Museum" -hold -e "./run_server.sh Museum $MUSEUMPORT localhost $((MUSEUMPORT+1)) /lib/genclass.jar"
}

run_clients() {
 xterm -T "MasterThief" -hold -e "./run_client.sh MasterThief /lib/genclass.jar" &
 xterm -T "OrdinaryThief" -hold -e "./run_client.sh OrdinaryThief /lib/genclass.jar"
}

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