CODEBASE="file:///home/"$1"/test/Heist/dirCollectionSite/"
java -Djava.rmi.server.codebase=$CODEBASE\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy\
     server.main.ServerCollectionSite 22331 localhost 22330
