/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student_teamviewer;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import masterPanel.ReportException;
import masterPanel.SettingsConfig;

/**
 *
 * @author viky
 */
public class UDP_Call extends Thread 
{
  
    private DatagramSocket DS;
    public boolean isConnected;
   
    public UDP_Call()
    {
        
        try
        {
            this.DS= new DatagramSocket();
            this.setDaemon(true);            
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
               ReportException.write(this.toString()+"\t"+ex.getMessage() ); 
            }
            
        }
            
        
    }
    public synchronized void Signal()
    { 
        try
        {
            
            String msg=("NEW"+";"+SettingsConfig.IP.getHostAddress()+";"+SettingsConfig.PORT_UDP); 
            byte[] a=msg.getBytes("UTF8");  
            DatagramPacket DP= new DatagramPacket(
                    a, a.length, SettingsConfig.IP_UDP, SettingsConfig.PORT_UDP);
            this.DS.send(DP);
        }
        catch(Exception se)
        {
            System.out.println( "SocketException #1 :" + se.getMessage());

        }
    }
    
}
