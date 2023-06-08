echo "Transfering data to the Collection Site node."
sshpass -f password ssh sd304@l040101-ws09.ua.pt'mkdir -p test/Heist'
sshpass -f password ssh sd304@l040101-ws09.ua.pt'rm -rf test/Heist/*'
sshpass -f password scp dirMuseumDeployAndRun.zip sd304@l040101-ws09.ua.pt:test/Heist
echo "Decompressing data sent to the Collection Site node."
sshpass -f password ssh sd304@l040101-ws09.ua.pt'cd test/Heist ; unzip -uq dirCollectionSite.zip'
echo "Executing program at the CollectionSite node."
sshpass -f password ssh sd304@l040101-ws09.ua.pt'cd test/Heist/dirCollectionSite ; ./collectionSite_com_d.sh 304'
