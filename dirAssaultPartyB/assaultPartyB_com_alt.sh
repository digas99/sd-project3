CODEBASE="file:///home/"$1"/test/Heist/dirAssaultPartyB/"
java -Djava.rmi.server.codebase=$CODEBASE\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy\
     server.main.ServerAssaultPartyB 22335 localhost 22330
