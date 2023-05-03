package utils;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientCom {
    private Socket commSocket = null;
    private String serverHostName;
    private int serverPortNumb;
    private ObjectInputStream in = null;
    private ObjectOutputStream out = null;
    public ClientCom (String hostName, int portNumb) {
        serverHostName = hostName;
        serverPortNumb = portNumb;
    }

    public boolean open () {

    }

    public boolean close () {

    }

    public Object readObject () {

    }

    public void writeObject (Object obj) {

    }
}
