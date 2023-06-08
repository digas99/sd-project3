echo "Transfering data to the Museum node."
sshpass -f password ssh sd304@l040101-ws02.ua.pt 'mkdir -p test/Heist'
sshpass -f password ssh sd304@l040101-ws02.ua.pt 'rm -rf test/Heist/*'
sshpass -f password scp dirMuseum.zip sd304@l040101-ws02.ua.pt:test/Heist
echo "Decompressing data sent to the Museum node."
sshpass -f password ssh sd304@l040101-ws02.ua.pt 'cd test/Heist ; unzip -uq dirMuseum.zip'
echo "Executing program at the Museum node."
sshpass -f password ssh sd304@l040101-ws02.ua.pt 'cd test/Heist/dirMuseum ; ./museum_com_d.sh sd304'