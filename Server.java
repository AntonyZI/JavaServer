// Autor: Antony Jacob Blanco Trujillo

package javaserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket[] clientSvSockets;
    
    public Server(){
        clientSvSockets = new ServerSocket[3];
        try {
            clientSvSockets[1] = new ServerSocket(0);
            System.out.print("Server opened at ");
            System.out.println(clientSvSockets[1].getInetAddress()+":"+clientSvSockets[1].getLocalPort());
            
            clientSvSockets[1].close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
}
