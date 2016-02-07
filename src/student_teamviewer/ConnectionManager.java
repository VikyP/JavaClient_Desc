/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student_teamviewer;


import java.util.logging.Level;
import java.util.logging.Logger;
import masterPanel.ReportException;
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
    private final ThreadConnectionControl TCC;
    
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
                    ReportException.write(this.toString()+"\t"+ex.getMessage() ); 
                }
            }
           
        }

    }
   
   
    public ConnectionManager(int width) 
    {  
        
        this.Student_UDP_Message = new UDP_Call(); 
        this.CC = new ConnectCatch( width );
        this.CCC =new ConnectCatchCommand(); 
        TCC= new ThreadConnectionControl();
        
    }
    
    public void startConnection()
    { 
        this.Student_UDP_Message.start();
        this.CC.start();
        this.CCC.start();
        this.TCC.start();
    
    }
   
    
    
}


