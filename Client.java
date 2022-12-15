// Autor: Antony Jacob Blanco Trujillo

package javaserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {
    
    // To create mutliple sockets
    private String ipAddress; // Ip address to server
    private int port; // port on ip address to connect
    
    private Socket clientSockets; // Sockets created to conect to server
    // First socket to connect for commands such as chat messages or
    // user states
    private OutputStream clientOutputStream;
    private DataOutputStream clientDataOutputStream;
    
    private InputStream clientInputStream;
    private DataInputStream clientDataInputStream;
    
    private Thread dataInputThread;
    
    public Client(String ipAddress, int port){
        this.ipAddress = ipAddress;
        this.port = port;
        //clientSockets = new Socket[1];
        //clientDataOutputStreams = new DataOutputStream[1];
        //clientDataInputStreams = new DataInputStream[1];
        
    }
    public void enableSocket(int n){
        try {
            clientSockets = new Socket(this.ipAddress, this.port);
            
            clientOutputStream = clientSockets.getOutputStream();
            clientDataOutputStream = new DataOutputStream(clientOutputStream);
            
            clientInputStream = clientSockets.getInputStream();
            clientDataInputStream = new DataInputStream(clientInputStream);
            
            dataInputThread = new Thread(new ClientDataInputRunnable(clientDataInputStream));
            dataInputThread.start();
            
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    public void sendMessage(String msg){
        try {
            clientDataOutputStream.writeUTF(msg);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    public void closeSocket(int n){
        try {
            //System.out.println("Socket at " + n + " closed successfully`");
            System.out.println("Socket closed successfully`");
            clientSockets.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    class ClientDataInputRunnable implements Runnable{
        private DataInputStream inputStream;
        
        public ClientDataInputRunnable(DataInputStream inputStream){
            this.inputStream = inputStream;
        }
        
        @Override
        public void run() {
            try {
                while(true){
                    String lineIn = inputStream.readUTF(); // Waits for stream input until user sends message
                    System.out.println("[Server]: " + lineIn);
                    if(lineIn.equals("^D")){ // EOF
                        break;
                    }
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
        
    }
}
