#!/bin/bash

run_servers() {
  xterm -T "AssaultPartyA" -hold -e "./run_server.sh AssaultParty 22001 localhost 22002 /lib/genclass.jar" &
  xterm -T "AssaultPartyB" -hold -e "./run_server.sh AssaultParty 22003 localhost 22004 /lib/genclass.jar" &
  xterm -T "CollectionSite" -hold -e "./run_server.sh CollectionSite 22005 localhost 22006 /lib/genclass.jar" &
  xterm -T "ConcentrationSite" -hold -e "./run_server.sh ConcentrationSite 22007 localhost 22008 /lib/genclass.jar" &
  xterm -T "Museum" -hold -e "./run_server.sh Museum 22009 localhost 22010 /lib/genclass.jar"
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