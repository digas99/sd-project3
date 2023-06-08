echo "Transfering data to the Assault Party A node."
sshpass -f password ssh sd304@l040101-ws04.ua.pt 'mkdir -p test/Heist'
sshpass -f password ssh sd304b@l040101-ws04.ua.pt 'rm -rf test/Heist/*'
sshpass -f password scp dirAssaultPartyA.zip sd304@l040101-ws04.ua.pt:test/Heist
echo "Decompressing data sent to the Assault Party A node."
sshpass -f password ssh sd304@l040101-ws04.ua.pt 'cd test/Heist ; unzip -uq dirAssaultPartyA.zip'
echo "Executing program at the AssaultPartyA node."
sshpass -f password ssh sd304@l040101-ws04.ua.pt 'cd test/Heist/dirAssaultPartyA ; ./assaultPartyA_com_d.sh sd304'
