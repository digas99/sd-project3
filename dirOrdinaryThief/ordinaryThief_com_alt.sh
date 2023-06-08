CODEBASE="file:///home/"$1"/test/Heist/dirOrdinaryThief/"
java -Djava.rmi.server.codebase=$CODEBASE\
     -Djava.rmi.server.useCodebaseOnly=false\
     client.main.ClientOrdinaryThief localhost 22000 
