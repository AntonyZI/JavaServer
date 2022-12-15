
package javaserver;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        System.out.println("Select an option:");
        System.out.println(" 1.- Server");
        System.out.println(" 2.- Client");
        System.out.print("Opciòn: ");
        int optionInt = sc.nextInt();
        switch(optionInt){
            case 1:
                Server theServer = new Server();
                break;
            case 2:
                sc.nextLine();
                System.out.print("  Ip: ");
                String svIp = sc.nextLine();
                System.out.print("  Port: ");
                int svPort = sc.nextInt();
                sc.nextLine();
                
                Client socketToClient = new Client(svIp, svPort);
                socketToClient.enableSocket(0);
                System.out.println("Connected to server");
                while(sc.hasNextLine()){
                    String lineIn = sc.nextLine();
                    socketToClient.sendMessage(lineIn);
                }
                socketToClient.closeSocket(0);
                
                break;

            default:
                System.out.println("default: Invalid Option");
                break;
        }
    }
}