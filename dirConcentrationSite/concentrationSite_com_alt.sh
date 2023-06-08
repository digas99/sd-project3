CODEBASE="file:///home/"$1"/test/Heist/dirConcentrationSite/"
java -Djava.rmi.server.codebase=$CODEBASE\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy\
     server.main.ServerConcentrationSite 22332 localhost 22330
