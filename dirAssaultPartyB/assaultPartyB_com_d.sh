CODEBASE="http://l040101-ws06.ua.pt/"$1"/classes/"
java -Djava.rmi.server.codebase=$CODEBASE\
     -Djava.rmi.server.useCodebaseOnly=true\
     -Djava.security.policy=java.policy\
     server.main.ServerAssaultPartyB 22335 l040101-ws06.ua.pt 22330
