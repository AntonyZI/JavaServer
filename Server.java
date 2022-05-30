// Autor: Antony Jacob Blanco Trujillo

package javaserver;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket[] clientSvSockets;
    private Socket[] clientSockets;
    
    public Server(){
        clientSvSockets = new ServerSocket[3];
        clientSockets = new Socket[3];
        try {
            clientSvSockets[0] = new ServerSocket(0);
            System.out.print("Server opened at ");
            System.out.println(clientSvSockets[0].getInetAddress().getHostName()+":"+clientSvSockets[0].getLocalPort());
            
            System.out.println("Waiting for the client'a socket . . .");
            clientSockets[0] = clientSvSockets[0].accept();
            System.out.println("Successfull client's socket connection");
            
            InputStream streamToClient = clientSockets[0].getInputStream();
            DataInputStream dataStreamToClient = new DataInputStream(streamToClient);
            
            System.out.println(dataStreamToClient.readUTF());
            
            clientSvSockets[0].close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
}
