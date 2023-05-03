package utils;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ServerCom {
   private ServerSocket listeningSocket = null;
   private Socket commSocket = null;
   private int serverPortNumb;
   private ObjectInputStream in = null;
   private ObjectOutputStream out = null;

   public ServerCom (int portNumb) {
       serverPortNumb = portNumb;
   }

   public ServerCom (int portNumb, ServerSocket lSocket) {
       serverPortNumb = portNumb;
       listeningSocket = lSocket;
   }

   public void start () {

   }

   public void end () {

   }

   public ServerCom accept () throws SocketTimeoutException {

   }

   public void close () {

   }

   public Object readObject () {

   }

   public void writeObject (Object obj) {

   }
}
