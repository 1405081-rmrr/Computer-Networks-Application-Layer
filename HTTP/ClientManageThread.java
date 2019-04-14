/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Date;

class ClientManageThread implements Runnable {
    private Socket socket;
    private InputStream is;
    private OutputStream os;

    private int id = 0;
    
    
    
    public ClientManageThread(Socket s, int id) {
        this.socket = s;

        try {
            this.is = this.socket.getInputStream();
            this.os = this.socket.getOutputStream();
        } catch (Exception e) {
            System.err.println("Sorry. Cannot manage client [" + id + "] properly.");
        }

        this.id = id;
    }
    
    
    private void HandleGetRequest(BufferedReader br,DataOutputStream ds,String str)
    {   
        //System.out.println(str);
        
        String fileName;
        
        String [] parts=new String[2];
        fileName=str.substring(5);
        if(fileName.equals(" HTTP/1.1"))
            fileName="index.html";
        else
        {
            parts=fileName.split(" HTTP/1.1");
            fileName=parts[0];
        }
       
       // System.out.println(fileName);
        
        boolean check=new File(fileName).exists();
        if(check==true)
        {
            File file=new File(fileName);
            String []fileNameSplit=fileName.split("\\.");
            String fileExtension=fileNameSplit[1];
            try{
                byte [] array =Files.readAllBytes(file.toPath());
                String status="HTTP/1.1 200 OK\r\n";
                String serverDetails="Server: Java HTTPServer\r\n";
                //String accept="Accept-Ranges: bytes";
                String fileSize="Content-Length: "+array.length+"\r\n";
                //String contentType="Content-Type: text/html"+"\r\n "; //need to be changed
                String contentType="Content-Type: ";
                if(fileExtension.equals("html"))
                    contentType+=("text/html"+"\r\n");
                else if(fileExtension.equals("gif"))
                    contentType+=("image/gif"+"\r\n");
                else if(fileExtension.equals("jpeg")||fileExtension.equals("jpg"))
                    contentType+=("image/jpeg"+"\r\n");
                else if(fileExtension.equals("png"))
                    contentType+=("image/png"+"\r\n");
                else if(fileExtension.equals("pdf"))
                    contentType+=("application/pdf"+"\r\n");
                
                
                
                 //System.out.println(status+"\n"+fileSize+"\n"+contentType+"\n");
                
                ds.writeBytes(status);
                ds.writeBytes(serverDetails);
                ds.writeBytes(contentType);
                ds.writeBytes(fileSize);
                ds.writeBytes("Connection: close\r\n");
                ds.writeBytes("\r\n");
                ds.write(array);
                ds.flush();
                //System.out.println("Hohohoh");
                
                //str2=br.readLine();
                //System.out.println(str2);
                
                ds.close();
                
                
            }catch(Exception e)
            {
                System.out.println(e);
            }
        }
        else
        {   
            File file=new File("Error.html");
            try{
                String status="HTTP/1.1 404 NotFound\r\n";
                String serverDetails="Server: Java HTTPServer\r\n";
                byte [] array =Files.readAllBytes(file.toPath());
                String fileSize="Content-Length: "+array.length+"\r\n";
                String contentType="Content-Type: text/html"+"\r\n";
                ds.writeBytes(status);
                ds.writeBytes(serverDetails);
                ds.writeBytes(contentType);
                ds.writeBytes(fileSize);
                ds.writeBytes("Connection: close\r\n");
                ds.writeBytes("\r\n");
                ds.write(array);
                ds.flush();
            }catch(Exception e)
            {
                System.out.println(e);
            }
        }
        
        
    }
    
    private void updateFile(String input)
    {
        StringBuilder updatedBuilder= new StringBuilder();
         File file=new File("index.html");
         try{
             BufferedReader br= new BufferedReader(new FileReader("index.html"));
             String str;
             while((str=br.readLine())!=null)
                 updatedBuilder.append(str);
             
             String content=updatedBuilder.toString();
             
             String [] parts=content.split("\t\t");
             
             br.close();
             //System.out.println(updatedBuilder.toString());
             for(int i=0;i<parts.length;i++)
             {
                 //System.out.println(str2);
                 if(parts[i].contains("<h2> Post->"))
                     parts[i]="<h2> Post-> "+input+"</h2>";
             }
             
             content="";
             for(int i=0;i<parts.length;i++)
                 content+=(parts[i]+"\t\t");
             content=content.trim();
             Writer writer=new FileWriter(file);
             BufferedWriter bw=new BufferedWriter(writer);
             bw.write(content);
             bw.close();
         }
        catch(Exception e)
        {
            System.out.println(e);
            
        }
    }
    
    private void HandlePost(BufferedReader br, DataOutputStream ds)
    {
        String str;
        int arr_size=0;
        char [] input;
        String input_string;
        try{
            while((str=br.readLine()).length()!=0)
            {
                if(str.startsWith("Content-Length: "))
                    arr_size=Integer.parseInt(str.substring("Content-Length: ".length()));
                
            }
            input=new char[arr_size];
            br.read(input);
            input_string=new String(input);
            input_string=input_string.substring("user=".length());
            updateFile(input_string);
            File file=new File("index.html");
            ds.writeBytes("HTTP/1.1 200 OK\r\n");
            ds.writeBytes("Content-Type: text/html\r\n");
            ds.writeBytes("\r\n");
            //ds.writeBytes(new Date().toString());  
            ds.write(Files.readAllBytes(file.toPath()));
            ds.flush();
            ds.close();
            

            //System.out.println(input_string);
        }catch(Exception e)
        {
            
        }
    }
    
    @Override
    public void run() {
        BufferedReader br = new BufferedReader(new InputStreamReader(this.is));
   
        DataOutputStream ds= new DataOutputStream(this.os);
        
        String str;
        
        try{
            str=br.readLine();
            if(str.startsWith("GET"))
                HandleGetRequest(br,ds,str);
            else if(str.startsWith("POST"))
                HandlePost(br,ds);
        }catch(Exception e)
        {
            System.out.println(e);
                    
        }
        
        try {
                this.is.close();
                this.os.close();
                this.socket.close();
            } catch (Exception e) {

            }

        }
    }
