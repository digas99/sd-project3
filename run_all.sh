#!/bin/bash

# run servers
xterm -T "AssaultPartyA" -hold -e "./run_server.sh AssaultParty 22001 localhost 22002" &
xterm -T "AssaultPartyB" -hold -e "./run_server.sh AssaultParty 22003 localhost 22004" &
xterm -T "CollectionSite" -hold -e "./run_server.sh CollectionSite 22005 localhost 22006" &
xterm -T "ConcentrationSite" -hold -e "./run_server.sh ConcentrationSite 22007 localhost 22008" &
xterm -T "Museum" -hold -e "./run_server.sh Museum 22009 localhost 22010"

# run clients
xterm -T "MasterThief" -hold -e "./run_client.sh MasterThief" &
xterm -T "OrdinaryThief" -hold -e "./run_client.sh OrdinaryThief" &