echo "Transfering data to the registry node."
sshpass -f password ssh sd304@l040101-ws09.ua.pt 'mkdir -p test/Heist'
sshpass -f password scp dirRegistry.zip sd304@l040101-ws09.ua.pt:test/Heist
echo "Decompressing data sent to the registry node."
sshpass -f password ssh sd304@l040101-ws09.ua.pt 'cd test/Heist ; unzip -uq dirRegistry.zip'
echo "Executing program at the registry node."
sshpass -f password ssh sd304@l040101-ws09.ua.pt 'cd test/Heist/dirRegistry ; ./registry_com_d.sh sd304'
