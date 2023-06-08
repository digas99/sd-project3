xterm  -T "RMI registry" -hold -e "./RMIRegistryDeployAndRun.sh" &
sleep 4
xterm  -T "Registry" -hold -e "./RegistryDeployAndRun.sh" &
sleep 4
xterm  -T "Collection Site" -hold -e "./CollectionSiteDeployAndRun.sh" &
xterm  -T "Concentration Site" -hold -e "./ConcentrationSiteDeployAndRun.sh" &
xterm  -T "Museum" -hold -e "./MuseumDeployAndRun.sh" &
xterm  -T "Assault Party A" -hold -e "./AssaultPartyADeployAndRun.sh" &
xterm  -T "Assault Party B" -hold -e "./AssaultPartyBDeployAndRun.sh" &
sleep 1
xterm  -T "Master" -hold -e "./MasterDeployAndRun.sh" &
xterm  -T "Ordinaries" -hold -e "./OrdinariesDeployAndRun.sh" &
