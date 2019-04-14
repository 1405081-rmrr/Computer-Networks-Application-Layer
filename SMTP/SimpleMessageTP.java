package mySMTP;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Scanner;

public class SimpleMessageTP {
    private Socket socket;
    private InetAddress address ;
    private int port ;
    private BufferedReader reader ;
    private PrintWriter writer ;
    private Scanner console ;

    SimpleMessageTP(InetAddress address,int port){
        this.address = address ;
        this.port = port ;
    }

    void initialize() throws Exception{
        socket = new Socket() ;
        socket.connect(new InetSocketAddress(address,port),20000);
        socket.setSoTimeout(20000);
        reader =  new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(socket.getOutputStream(),true);
        console = new Scanner(System.in) ;
        System.out.println(reader.readLine());
        writer.println("HELO "+InetAddress.getLocalHost());
        System.out.println(reader.readLine());
    }

    int authorize() throws Exception{
        String username,password ;
        writer.println("AUTH LOGIN");
        System.out.println(reader.readLine());
        System.out.println("Enter Username");
        username = console.nextLine() ;
        writer.println(new String(Base64.getEncoder().encode(username.getBytes())));
        System.out.println(reader.readLine());
        System.out.println("Enter Password");
        password = console.nextLine() ;
        writer.println(new String(Base64.getEncoder().encode(password.getBytes())));
        String msg = reader.readLine() ;
        System.out.println(msg);
        if(msg.equalsIgnoreCase("235 Authentication successful")){
            return 0 ;
        }
        return -1 ;
    }

    void sendMail() throws Exception{
        String from = null;
        boolean fr = false ;
        boolean to = false ;
        ArrayList<String> recepents =  new ArrayList<>() ;
        int current = 0 ;
        String commands ;
        while(true){
            timerThread timerThread = new timerThread() ;
            timerThread.start();
            commands = console.nextLine();
            timerThread.stop();
            if(!commands.isEmpty()){
                timerThread.stop();
            }
            if(commands.equalsIgnoreCase("Quit")){
                break;
            }

            else if(commands.startsWith("MAIL FROM:") | (commands.startsWith("mail from:"))){
                writer.println(commands);
                String tem = reader.readLine() ;
                if(tem.startsWith("5")){
                    System.out.println(tem);
                }
                else{
                    from = commands.substring(11,commands.length()-1) ;
                    fr = true ;
                    System.out.println(tem);
                    System.out.println("Sender Email: "+from);
                }


            }
            else if(commands.startsWith("RCPT TO:") | (commands.startsWith("rcpt to:"))){
                if(fr){
                    writer.println(commands);
                    String s = reader.readLine() ;
                    if(s.startsWith("5")){
                        System.out.println(s);
                    }
                    else{
                        System.out.println(s);
                        to = true ;
                        recepents.add(commands.substring(9,commands.length()-1)) ;
                        current++ ;
                        System.out.println("Number of  Recipient: "+current);
                    }
                }
                else{
                    System.out.println("Sender mail not set yet");
                }
            }

            else if(commands.startsWith("DATA")){
                if(fr & to){
                    writer.println(commands);
                    System.out.println(reader.readLine());
                    System.out.println("Enter Subject");
                    String subject = console.nextLine() ;
                    writer.println("Subject: "+subject);
                    System.out.println("From: "+from);
                    writer.println("From: "+from);
                    String re = "To: " ;
                    for(int i=0;i<recepents.size();i++){
                        if(i==recepents.size()-1){
                            re += recepents.get(i)  ;
                        }
                        else{
                            re += recepents.get(i) + "," ;
                        }
                    }
                    System.out.println(re);
                    writer.println(re);
                    System.out.println("Enter Message (End with . in the last line to finish): ");
                    String tem  ;
                    StringBuilder msg = new StringBuilder();
                    writer.println("\n");
                    while (!((tem=console.nextLine()).equalsIgnoreCase("."))){
                        msg.append(tem).append("\n");
                    }
                    msg.append(".");
                    writer.println(msg);
                    String tem2 = reader.readLine() ;
                    if(tem2.startsWith("2")){
                        System.out.println(tem2);
                        to = false ;
                        fr = false ;
                        from = "" ;
                        recepents.clear() ;
                        current=0 ;
                    }
                    else{
                        System.out.println(tem2);
                    }
                }
                if(fr & (!to)){
                    System.out.println("Recipient not set");
                }
            }

            else if(commands.equalsIgnoreCase("EHLO")) {
                //
            }
            else{
                if(!fr){
                    System.out.println("Sender not set");
                    System.out.println("Please first set the sender");
                }

                if(fr & !to){
                    System.out.println("Recipient not set");
                    System.out.println("Please set at least one recipient");
                }
                else{
                    writer.println(commands);
                    String error = reader.readLine() ;
                    if(error.startsWith("5")){
                        System.out.println(error);
                        System.out.println("Please try again");
                    }
                    if(error.startsWith("4")){
                        break ;
                    }
                }
            }
        }
        quit();
    }

    void quit() throws Exception{
        writer.println("Quit");
        System.out.println(reader.readLine());
        writer.close();
        reader.close();
        socket.close();
    }
}
