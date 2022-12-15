// Autor: Antony Jacob Blanco Trujillo

package javaserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket clientSvSocket;
    private ServerIORunnable[] clientSocketsRunnables;
    private Thread[] clientSocketsThreads;
    private boolean[] enabledSockets;
    
    public Server(){
        //clientSvSockets = new ServerSocket[3];
        clientSocketsThreads = new Thread[3];
        clientSocketsRunnables = new ServerIORunnable[3];
        
        try {
            clientSvSocket = new ServerSocket(0);
            System.out.print("Server opened at ");
            System.out.println(clientSvSocket.getInetAddress().getHostName()+":"+clientSvSocket.getLocalPort());
            
            for(int i = 0; i < clientSocketsThreads.length; i++){
                (new Thread(new ConstructorServerIOLoader(i))).start();
            }
            
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    public void sendMessage(String msg){
        for(int i = 0; i < clientSocketsRunnables.length; i++){
            if(clientSocketsRunnables[i] != null)
                clientSocketsRunnables[i].sendMessage(msg);
        }
    }
    public void closeServer(){
        try {
            clientSvSocket.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    private Server getServer(){
        return this;
    }
    class ConstructorServerIOLoader implements Runnable{
        private int index;
        public ConstructorServerIOLoader(int n){
            index = n;
        }
        @Override
        public void run() {
            clientSocketsRunnables[index] = new ServerIORunnable(clientSvSocket);
            clientSocketsThreads[index] = new Thread(clientSocketsRunnables[index]);
            clientSocketsThreads[index].start();
        }
    }
    class ServerIORunnable implements Runnable{
        private ServerSocket svSocketFromAccept;
        private Socket actualSocket;
        private DataInputStream inputStream;
        private DataOutputStream outputStream;
        
        private boolean sendingMessage;
        private String msg;
        public ServerIORunnable(ServerSocket svSocket){
            svSocketFromAccept = svSocket;
            try{
                System.out.println("Waiting for the client'a socket . . .");
                actualSocket = svSocketFromAccept.accept();
                System.out.println("Successfull client's socket connection");
                inputStream = new DataInputStream(actualSocket.getInputStream());
                outputStream = new DataOutputStream(actualSocket.getOutputStream());
                outputStream.writeUTF("CONNECTION ESTABLISHED (sent by server)");
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
        public void sendMessage(String msg){
            try {
                outputStream.writeUTF(msg);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
        
        @Override
        public void run() {
            try {
                while(true){
                    while(true){
                        String lineIn = inputStream.readUTF(); // Waits for stream input until user sends message
                        System.out.println("[User]: " + lineIn);
                        getServer().sendMessage("[User]: " + lineIn);
                        if(lineIn.equals("^D")){ // EOF
                            break;
                        }
                    }
                    System.out.println("Waiting for the client'a socket . . .");
                    actualSocket = svSocketFromAccept.accept();
                    System.out.println("Successfull client's socket connection");
                    
                    inputStream = new DataInputStream(actualSocket.getInputStream());
                    outputStream = new DataOutputStream(actualSocket.getOutputStream());
                    outputStream.writeUTF("CONNECTION ESTABLISHED (sent by server)");
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}
