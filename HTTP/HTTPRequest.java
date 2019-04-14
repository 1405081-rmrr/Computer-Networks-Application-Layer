/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


/**
 *
 * @author Nafis
 */
public class HTTPRequest {

    /**
     * @param args the command line arguments
     */
    
    //static final int PORT = 6789;
    
    public static void main(String[] args) throws IOException {
        Server server;
        try{
            server=new Server(8080);
            server.startServer();
        }catch(Exception e)
        {
            System.out.println(e);
        }
    
    }
}