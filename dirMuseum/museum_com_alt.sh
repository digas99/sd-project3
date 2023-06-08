CODEBASE="file:///home/"$1"/test/Heist/dirMuseum/"
java -Djava.rmi.server.codebase=$CODEBASE\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy\
     server.main.ServerMuseum 22333 localhost 22330
