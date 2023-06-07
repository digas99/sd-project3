echo "Transfering data to the Master node."
sshpass -f password ssh 304@l040101-ws08.ua.pt 'mkdir -p test/Master'
sshpass -f password ssh 304@l040101-ws08.ua.pt 'rm -rf test/Master/*'
sshpass -f password scp dirMaster.zip 304@l040101-ws08.ua.pt:test/Master
echo "Decompressing data sent to the Master node."
sshpass -f password ssh 304@l040101-ws08.ua.pt 'cd test/Master ; unzip -uq dirMaster.zip'
echo "Executing program at the Master node."
sshpass -f password ssh 304@l040101-ws08.ua.pt 'cd test/Master/dirMaster ; ./Master_com_d.sh'
