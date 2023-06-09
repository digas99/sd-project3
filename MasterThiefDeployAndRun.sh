echo "Transfering data to the Master Thief node."
sshpass -f password ssh sd304@l040101-ws08.ua.pt 'mkdir -p test/Heist'
sshpass -f password ssh sd304@l040101-ws08.ua.pt 'rm -rf test/Heist/*'
sshpass -f password scp dirMasterThief.zip sd304@l040101-ws08.ua.pt:test/Heist
echo "Decompressing data sent to the Master Thief node."
sshpass -f password ssh sd304@l040101-ws08.ua.pt 'cd test/Heist ; unzip -uq dirMasterThief.zip'
echo "Executing program at the Master Thief node."
sshpass -f password ssh sd304@l040101-ws08.ua.pt 'cd test/Heist/dirMasterThief ; ./masterThief_com_d.sh'
