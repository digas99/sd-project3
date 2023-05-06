# run servers
xterm -T "AssaultPartyA" -hold -e "./run.sh AssaultParty 22001 localhost 22002" &
xterm -T "AssaultPartyB" -hold -e "./run.sh AssaultParty 22003 localhost 22004" &
xterm -T "CollectionSite" -hold -e "./run.sh CollectionSite 22005 localhost 22006" &
xterm -T "ConcentrationSite" -hold -e "./run.sh ConcentrationSite 22007 localhost 22008" &
xterm -T "Museum" -hold -e "./run.sh Museum 22009 localhost 22010"

# run clients
xterm -T "MasterThief" -hold -e "./run.sh MasterThief" &
xterm -T "OrdinaryThief1" -hold -e "./run.sh OrdinaryThief" &