echo "Transfering data to the Collection Site node."
sshpass -f password ssh sd304@l040101-ws07.ua.pt 'mkdir -p test/Heist'
sshpass -f password ssh sd304@l040101-ws07.ua.pt 'rm -rf test/Heist/*'
sshpass -f password scp dirCollectionSite.zip sd304@l040101-ws07.ua.pt:test/Heist
echo "Decompressing data sent to the Collection Site node."
sshpass -f password ssh sd304@l040101-ws07.ua.pt 'cd test/Heist ; unzip -uq dirCollectionSite.zip'
echo "Executing program at the CollectionSite node."
sshpass -f password ssh sd304@l040101-ws07.ua.pt 'cd test/Heist/dirCollectionSite ; ./collectionSite_com_d.sh sd304'
