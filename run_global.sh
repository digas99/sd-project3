#!/bin/bash

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

xterm -T "AssaultPartyA" -hold -e "sshpass -f password ssh $ASSAULTPARTYAHOST './run_server.sh AssaultParty 22330 localhost 22331 /lib/genclass.jar'" &
xterm -T "AssaultPartyB" -hold -e "sshpass -f password ssh $ASSAULTPARTYBHOST './run_server.sh AssaultParty 22332 localhost 22333 /lib/genclass.jar'" &
xterm -T "CollectionSite" -hold -e "sshpass -f password ssh $COLLECTIONSITEHOST './run_server.sh CollectionSite 22334 localhost 22335 /lib/genclass.jar'" &
xterm -T "ConcentrationSite" -hold -e "sshpass -f password ssh $CONCENTRATIONSITEHOST './run_server.sh ConcentrationSite 22336 localhost 22337 /lib/genclass.jar'" &
xterm -T "Museum" -hold -e "sshpass -f password ssh $MUSEUMHOST './run_server.sh Museum 22338 localhost 22339 /lib/genclass.jar'" &
xterm -T "MasterThief" -hold -e "sshpass -f password ssh $MASTERTHIEFHOST './run_client.sh MasterThief /lib/genclass.jar'" &
xterm -T "OrdinaryThief" -hold -e "sshpass -f password ssh $ORDINARYTHIEFHOST './run_client.sh OrdinaryThief /lib/genclass.jar'"