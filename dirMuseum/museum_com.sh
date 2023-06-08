CODEBASE="http://localhost/"$1"/classes/"
java -Djava.rmi.server.codebase=$CODEBASE\
     -Djava.rmi.server.useCodebaseOnly=true\
     -Djava.security.policy=java.policy\
     server.main.ServerMuseum 22333 localhost 22330
