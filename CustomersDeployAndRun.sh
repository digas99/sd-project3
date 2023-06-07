echo "Transfering data to the Ordinary node."
sshpass -f password ssh 304@l040101-ws04.ua.pt 'mkdir -p test/SleepingBarbers'
sshpass -f password ssh 304@l040101-ws04.ua.pt 'rm -rf test/SleepingBarbers/*'
sshpass -f password scp dirOrdinary.zip 304@l040101-ws04.ua.pt:test/SleepingBarbers
echo "Decompressing data sent to the Ordinary node."
sshpass -f password ssh 304@l040101-ws04.ua.pt 'cd test/SleepingBarbers ; unzip -uq dirOrdinary.zip'
echo "Executing program at the Ordinary node."
sshpass -f password ssh 304@l040101-ws04.ua.pt 'cd test/SleepingBarbers/dirOrdinary ; ./Ordinary_com_d.sh'
