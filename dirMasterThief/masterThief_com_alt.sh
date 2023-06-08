CODEBASE="file:///home/"$1"/test/Heist/dirMasterThief/"
java -Djava.rmi.server.codebase=$CODEBASE\
     -Djava.rmi.server.useCodebaseOnly=false\
     clientSide.main.ClientSleepingBarbersBarber localhost 22000
