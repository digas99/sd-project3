CODEBASE="file:///home/"$1"/test/Heist/dirAssaultPartyA/"
java -Djava.rmi.server.codebase=$CODEBASE\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy\
     server.main.ServerAssaultPartyA 22334 localhost 22330
