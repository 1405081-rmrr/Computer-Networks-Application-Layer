package mySMTP;


import java.net.InetAddress;
import java.util.Scanner;

public class Main {
    public static void main(String args[]) throws Exception{
        SimpleMessageTP smtp = new SimpleMessageTP(InetAddress.getByName("smtp.sendgrid.net"),25) ;
        smtp.initialize();
        System.out.println("STMP Require authorization? 1.Yes 2.No : ");
        Scanner sc = new Scanner(System.in) ;
        int choice = sc.nextInt() ;
        if(choice==1){
            int r = smtp.authorize();
            while (r!=0){
                System.out.println("Resetting.....");
                System.out.println();
                smtp.initialize();
                r = smtp.authorize() ;
            }
        }

        smtp.sendMail();
    }
}
