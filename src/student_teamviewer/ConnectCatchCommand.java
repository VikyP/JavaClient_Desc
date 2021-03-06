/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student_teamviewer;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import masterPanel.ReportException;
import masterPanel.SettingsConfig;

/**
 *
 * @author viky
 */
public class ConnectCatchCommand extends Thread 
{
   
    public boolean isConnect; 
    public Socket client_RC;
    public TCP_ReceiverCommand receiverCommand;
    
    private IReconnect IR =new IReconnect()
    {

        @Override
        public void Reconnect()
        {
           ConnectCatchCommand.this.clearRC();
        }
    };

    public ConnectCatchCommand() 
    {       
        this.setDaemon(true);
        this.isConnect = false;        
    }

   @Override
    public void run()
    {
       
        ServerSocket  server_RC = null ;
        try {
                try 
                {

                    server_RC = new ServerSocket(SettingsConfig.PORT_TCP_COMMAND,-1, SettingsConfig.IP);
                }
                catch (NullPointerException ex)
                {
                    server_RC.close();                
                    server_RC = new ServerSocket(SettingsConfig.PORT_TCP_COMMAND,-1,SettingsConfig.IP);
                    System.out.println("Error 2" + ex.getMessage());
                    ReportException.write("ConnectCatch.run()  Error 2" + ex.getMessage());
                } 

                catch (BindException ex) 
                {
                    /////////////// Second !!!!!!!!!!!
                    System.out.println("Exception " + ex +SettingsConfig.IP+ ":" +SettingsConfig.PORT_TCP_COMMAND );
                    ReportException.write("ConnectCatch.run()  Error 1" + ex.getMessage() +SettingsConfig.IP+ ":" +SettingsConfig.PORT_TCP_COMMAND);
                    return;
                }

            while (true) 
            {
                System.out.println(" Waiting for connection ...");

////////////////////////////////////////////////////////////////////////////////////
                Socket clientTryConnect = server_RC.accept();
// если клиент еще не инициализирован 
                if (this.client_RC == null)
                {

                    this.client_RC = clientTryConnect;
                    this.isConnect = true;
                    this.receiverCommand = new TCP_ReceiverCommand();
                    this.receiverCommand.setClient(this.client_RC);
                    this.receiverCommand.ELC.addEventLostConnection(IR);
                    this.receiverCommand.start();
                }
                else ///поток обмена данными уже существует , но пришел неожидаемый запрос, который нужно  закрыть
                {
                    try 
                    {
                        clientTryConnect.close();
                        System.out.println("Поток обмена данными уже существует");   
                    } 
                    catch (Exception exc)
                    {
                        ReportException.write("Поток обмена данными уже существует" + exc.getMessage());
                    }
                }
            }

        } catch (UnknownHostException ex) {
            System.out.println("Error 3" + ex.getMessage());
            ReportException.write("ConnectCatch.run()  Error 3" + ex.getMessage() + SettingsConfig.IP+ ":" +SettingsConfig.PORT_TCP_COMMAND);
        } 
        catch (IOException ex) {
            System.out.println("Error 3" + ex.getMessage());
            ReportException.write("ConnectCatch.run()  Error 4" + ex.getMessage()+SettingsConfig.IP+ ":" +SettingsConfig.PORT_TCP_COMMAND);
        }

    }

    public void clearRC() 
    {
        this.receiverCommand=null;
        this.client_RC=null;
        this.isConnect = false;
    }

    
}
