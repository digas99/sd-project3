CODEBASE="file:///home/"$1"/test/Heist/dirRegistry/"
java -Djava.rmi.server.codebase=$CODEBASE\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy\
     server.main.ServerRegisterRemoteObject 22336 localhost 22330
