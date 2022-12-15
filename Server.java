// Autor: Antony Jacob Blanco Trujillo

package javaserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    int cantConnections;
    private ServerSocket clientSvSocket;
    private ServerIOLoader[] clientSocketsRunnables;
    private boolean[] enabledSockets;
    
    public Server(int cant){
        cantConnections = cant;
        //clientSvSockets = new ServerSocket[3];
        //clientSocketsThreads = new Thread[3];
        clientSocketsRunnables = new ServerIOLoader[cantConnections];
        
        try {
            clientSvSocket = new ServerSocket(0);
            System.out.print("Server opened at ");
            System.out.println(clientSvSocket.getInetAddress().getHostName()+":"+clientSvSocket.getLocalPort());
            
            for(int i = 0; i < clientSocketsRunnables.length; i++){
                clientSocketsRunnables[i] = new ServerIOLoader(i);
                (new Thread(clientSocketsRunnables[i])).start();
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
    class ServerIOLoader implements Runnable{
        private int sefverLoaderIndex;
        private ServerIORunnable serverIoProccess;
        public ServerIOLoader(int n){
            sefverLoaderIndex = n;
        }
        public void sendMessage(String msg){
            serverIoProccess.sendMessage(msg);
        }
        public void closeServerProccess(){
            serverIoProccess.closeConnection();
        }
        @Override
        public void run() {
            serverIoProccess = new ServerIORunnable(clientSvSocket);
            //clientSocketsThreads[sefverLoaderIndex] = new Thread(serverIoProccess);
            //clientSocketsThreads[sefverLoaderIndex].start();
            (new Thread(serverIoProccess)).start();
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
                openNewSocket();
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
        public void closeConnection(){
            try {
                actualSocket.close();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
        private void openNewSocket() throws IOException{
            System.out.println("Waiting for the client'a socket . . .");
            actualSocket = svSocketFromAccept.accept();
            System.out.println("Successfull client's socket connection");
            inputStream = new DataInputStream(actualSocket.getInputStream());
            outputStream = new DataOutputStream(actualSocket.getOutputStream());
            outputStream.writeUTF("CONNECTION ESTABLISHED (sent by server)");
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
                    openNewSocket();
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}
