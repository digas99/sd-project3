#!/bin/bash

xterm -T "AssaultPartyA" -hold -e "./AssaultPartyA.sh" &
xterm -T "AssaultPartyB" -hold -e "./AssaultPartyB.sh" &
xterm -T "CollectionSite" -hold -e "./CollectionSite.sh" &
xterm -T "ConcentrationSite" -hold -e "./ConcentrationSite.sh" &
xterm -T "Museum" -hold -e "./Museum.sh"