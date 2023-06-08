echo "Transfering data to the Assault Party B node."
sshpass -f password ssh sd304@l040101-ws06.ua.pt 'mkdir -p test/Heist'
sshpass -f password ssh sd304@l040101-ws06.ua.pt 'rm -rf test/Heist/*'
sshpass -f password scp dirAssaultPartyB.zip sd304@l040101-ws06.ua.pt:test/Heist
echo "Decompressing data sent to the Assault Party B node."
sshpass -f password ssh sd304@l040101-ws06.ua.pt 'cd test/Heist ; unzip -uq dirAssaultPartyB.zip'
echo "Executing program at the Assault Party B node."
sshpass -f password ssh sd304@l040101-ws06.ua.pt 'cd test/Heist/dirAssaultPartyB ; ./assaultPartyB_com_d.sh sd304'
