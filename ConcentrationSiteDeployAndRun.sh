echo "Transfering data to the Concentration Site node."
sshpass -f password ssh sd304@l040101-ws03.ua.pt 'mkdir -p test/Heist'
sshpass -f password ssh sd304@l040101-ws03.ua.pt 'rm -rf test/Heist/*'
sshpass -f password scp dirConcentrationSite.zip sd304@l040101-ws03.ua.pt:test/Heist
echo "Decompressing data sent to the Concentration Site node."
sshpass -f password ssh sd304@l040101-ws03.ua.pt 'cd test/Heist ; unzip -uq dirConcentrationSite.zip'
echo "Executing program at the ConcentrationSite node."
sshpass -f password ssh sd304@l040101-ws03.ua.pt 'cd test/Heist/dirConcentrationSite ; ./concentrationSite_com_d.sh sd304'
