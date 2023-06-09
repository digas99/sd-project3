# sshpass -f password scp killPorts.sh sd304@l040101-ws01.ua.pt:/home/sd304/
# sshpass -f password ssh sd304@l040101-ws01.ua.pt 'chmod +x killPorts.sh'
# sshpass -f password ssh sd304@l040101-ws01.ua.pt './killPorts.sh'

# sshpass -f password scp killPorts.sh sd304@l040101-ws02.ua.pt:/home/sd304/
# sshpass -f password ssh sd304@l040101-ws02.ua.pt 'chmod +x killPorts.sh'
# sshpass -f password ssh sd304@l040101-ws02.ua.pt './killPorts.sh'

# sshpass -f password scp killPorts.sh sd304@l040101-ws03.ua.pt:/home/sd304/
# sshpass -f password ssh sd304@l040101-ws03.ua.pt 'chmod +x killPorts.sh'
# sshpass -f password ssh sd304@l040101-ws03.ua.pt './killPorts.sh'

# sshpass -f password scp killPorts.sh sd304@l040101-ws04.ua.pt:/home/sd304/
# sshpass -f password ssh sd304@l040101-ws04.ua.pt 'chmod +x killPorts.sh'
# sshpass -f password ssh sd304@l040101-ws04.ua.pt './killPorts.sh'

# sshpass -f password scp killPorts.sh sd304@l040101-ws05.ua.pt:/home/sd304/
# sshpass -f password ssh sd304@l040101-ws05.ua.pt 'chmod +x killPorts.sh'
# sshpass -f password ssh sd304@l040101-ws05.ua.pt './killPorts.sh'

# sshpass -f password scp killPorts.sh sd304@l040101-ws06.ua.pt:/home/sd304/
# sshpass -f password ssh sd304@l040101-ws06.ua.pt 'chmod +x killPorts.sh'
# sshpass -f password ssh sd304@l040101-ws06.ua.pt './killPorts.sh'

# sshpass -f password scp killPorts.sh sd304@l040101-ws07.ua.pt:/home/sd304/
# sshpass -f password ssh sd304@l040101-ws07.ua.pt 'chmod +x killPorts.sh'
# sshpass -f password ssh sd304@l040101-ws07.ua.pt './killPorts.sh'

# sshpass -f password scp killPorts.sh sd304@l040101-ws08.ua.pt:/home/sd304/
# sshpass -f password ssh sd304@l040101-ws08.ua.pt 'chmod +x killPorts.sh'
# sshpass -f password ssh sd304@l040101-ws08.ua.pt './killPorts.sh'

xterm  -T "Collection Site" -hold -e "./CollectionSiteDeployAndRun.sh" &
xterm  -T "Concentration Site" -hold -e "./ConcentrationSiteDeployAndRun.sh" &
xterm  -T "Museum" -hold -e "./MuseumDeployAndRun.sh" &
xterm  -T "Assault Party A" -hold -e "./AssaultPartyADeployAndRun.sh" &
xterm  -T "Assault Party B" -hold -e "./AssaultPartyBDeployAndRun.sh" &
sleep 1
xterm  -T "Master" -hold -e "./MasterThiefDeployAndRun.sh" &
xterm  -T "Ordinaries" -hold -e "./OrdinaryThiefDeployAndRun.sh" &