/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

/**
 *
 * @author Nafis
 */
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket ;
    public static int i = 0 ;

    Server(int port) throws Exception{
        serverSocket = new ServerSocket(port) ;
    }

    void startServer() throws Exception{

        while (true){
            Socket socket = serverSocket.accept();
            System.out.println();
            System.out.println("Connected Browser ID : "+i);
            ClientManageThread wt = new ClientManageThread(socket, i);
            Thread t = new Thread(wt);
            t.start();
            i++ ;
        }
    }
}