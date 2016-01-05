/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student_teamviewer;


import java.util.logging.Level;
import java.util.logging.Logger;
import masterPanel.SettingsConfig;

/**
 *
 * @author viky
 */
public class ConnectionManager 
{

    private final ConnectCatch CC;
    private final ConnectCatchCommand CCC;
    private final UDP_Call Student_UDP_Message;
    
    private class ThreadConnectionControl  extends Thread
    {
        
        @Override
        public void run()
        {
            while(true)
            { 
                try
                {
                   ConnectionManager.this.Student_UDP_Message.isConnected=ConnectionManager.this.CC.isConnect;
                    Thread.sleep(1000);
                } 
                catch (InterruptedException ex)
                {
                    Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
           
        }

    }
   
   
    public ConnectionManager( SettingsConfig sc) 
    {  
        
        this.Student_UDP_Message = new UDP_Call(sc.IP, sc.PORT_UDP, sc.IP_UDP); 
        
        this.CC = new ConnectCatch(sc.IP, sc.PORT_TCP_IMG, sc.width  );
        this.CCC =new ConnectCatchCommand(sc.IP, sc.PORT_TCP_COMMAND );
        
        this.CC.start();
        this.CCC.start();
        ThreadConnectionControl TCC= new ThreadConnectionControl();
        TCC.start();
    }
   
    
    
}


