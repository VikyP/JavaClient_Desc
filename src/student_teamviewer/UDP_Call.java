/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student_teamviewer;

import java.awt.MouseInfo;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author viky
 */
public class UDP_Call extends Thread 
{
    private InetAddress IP;
    private InetAddress IP_UDP;
    private int port;
    private DatagramSocket DS;
    public boolean isConnected;
   
    public UDP_Call(InetAddress ip, int  port, InetAddress ip_udp)
    {
        
        try
        {
            
            this.IP=ip;
            this.port=port;
            this.IP_UDP=ip_udp;
            this.DS= new DatagramSocket();
            this.setDaemon(true);
            this.start();
            this.isConnected=false;
          //  System.out.println("    IP"+this.IP+"  IP_udp"+this.IP_UDP+"  port"+this.port);
               
        } 
        catch (SocketException ex)
        {
            
            System.out.println( ex.getMessage());
        } 
        
        
    }
    

   @Override
    public void run()
    {
        while(true)
        {
            try
            {
                
                if(!this.isConnected)
                this.Signal();
                Thread.sleep(1000);
                
            }
            catch (InterruptedException ex)
            {
                Logger.getLogger(UDP_Call.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
            
        
    }
    public synchronized void Signal()
    { 
        try
        {
            
            String msg=("NEW"+";"+this.IP.getHostAddress()+";"+this.port); 
            byte[] a=msg.getBytes("UTF8");  
            DatagramPacket DP= new DatagramPacket(
                    a, a.length, IP_UDP, this.port);
            
            System.out.println( msg);
        
            this.DS.send(DP);
        }
        catch(Exception se)
        {
            System.out.println( "SocketException #1 :" + se.getMessage());

        }
    }
    
}
