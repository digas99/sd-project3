echo "Transfering data to the Ordinary Thief node."
sshpass -f password ssh sd304@l040101-ws04.ua.pt 'mkdir -p test/Heist'
sshpass -f password ssh sd304@l040101-ws04.ua.pt 'rm -rf test/Heist/*'
sshpass -f password scp dirOrdinaryThief.zip sd304@l040101-ws04.ua.pt:test/Heist
echo "Decompressing data sent to the Ordinary Thief node."
sshpass -f password ssh sd304@l040101-ws04.ua.pt 'cd test/Heist ; unzip -uq dirOrdinaryThief.zip'
echo "Executing program at the Ordinary Thief node."
sshpass -f password ssh sd304@l040101-ws04.ua.pt 'cd test/Heist/dirOrdinaryThief ; ./ordinaryThief_com_d.sh'
