echo "Transfering data to the Master Thief node."
sshpass -f password ssh sd304@l040101-ws08.ua.pt 'mkdir -p test/MasterThief'
sshpass -f password ssh sd304@l040101-ws08.ua.pt 'rm -rf test/MasterThief/*'
sshpass -f password scp dirMasterThief.zip sd304@l040101-ws08.ua.pt:test/MasterThief
echo "Decompressing data sent to the Master Thief node."
sshpass -f password ssh sd304@l040101-ws08.ua.pt 'cd test/MasterThief ; unzip -uq dirMasterThief.zip'
echo "Executing program at the Master Thief node."
sshpass -f password ssh sd304@l040101-ws08.ua.pt 'cd test/MasterThief/dirMasterThief ; ./masterThief_com_d.sh'
