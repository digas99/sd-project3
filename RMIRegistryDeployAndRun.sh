echo "Transfering data to the RMIregistry node."
sshpass -f password ssh sd304@l040101-ws09.ua.pt 'mkdir -p test/Heist'
sshpass -f password ssh sd304@l040101-ws09.ua.pt 'rm -rf test/Heist/*'
sshpass -f password ssh sd304@l040101-ws09.ua.pt 'mkdir -p Public/classes/interfaces'
sshpass -f password ssh sd304@l040101-ws09.ua.pt 'rm -rf Public/classes/interfaces/*'
sshpass -f password scp dirRMIRegistry.zip sd304@l040101-ws09.ua.pt:test/Heist
echo "Decompressing data sent to the RMIregistry node."
sshpass -f password ssh sd304@l040101-ws09.ua.pt 'cd test/Heist ; unzip -uq dirRMIRegistry.zip'
sshpass -f password ssh sd304@l040101-ws09.ua.pt 'cd test/Heist/dirRMIRegistry ; cp interfaces/*.class /home/sd304/Public/classes/interfaces ; cp set_rmiregistry_d.sh /home/sd304'
echo "Executing program at the RMIregistry node."
sshpass -f password ssh sd304@l040101-ws09.ua.pt './set_rmiregistry_d.sh sd304 22339'
