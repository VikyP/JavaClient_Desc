/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student_teamviewer;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import masterPanel.ReportException;
import masterPanel.SettingsConfig;

/**
 *
 * @author viky
 */
public class ConnectCatch extends Thread {

   
    public Socket client_IMG;

    private IReconnect IR = new IReconnect() {

        @Override
        public void Reconnect() {
            ConnectCatch.this.clearTRW();
        }
    };
    public boolean isConnect;
    public Thread_SenderImage TRW;
    private int W;

    public ConnectCatch( int w)
    {
      
        this.setDaemon(true);
        this.isConnect = false;
        this.W=w;
    }

    @Override
    public void run() {
      //  System.out.println("Run");

        ServerSocket server_IMG = null;
        try {
            try
            {
                server_IMG = new ServerSocket(SettingsConfig.PORT_TCP_IMG, -1, SettingsConfig.IP);
            }
            catch(NullPointerException ex)
            {
                server_IMG.close();
                server_IMG = new ServerSocket(SettingsConfig.PORT_TCP_IMG, -1,SettingsConfig.IP);
                System.out.println("Error 2" + ex.getMessage());
                ReportException.write("ConnectCatch.run()  Error 2" + ex.getMessage());
            }
            catch (BindException ex) {
                /////////////// Second !!!!!!!!!!!
                System.out.println("Exception " + ex + SettingsConfig.IP + ":" + SettingsConfig.PORT_TCP_IMG);
                ReportException.write("ConnectCatch.run()  Error 1" + ex.getMessage() + SettingsConfig.IP + ":" + SettingsConfig.PORT_TCP_IMG);
                return;
            }

            while (true)
            {
                System.out.println(" Waiting for connection ...");

////////////////////////////////////////////////////////////////////////////////////
                Socket clientTryConnect = server_IMG.accept();
// если клиент еще не инициализирован 
                if (this.client_IMG == null) {

                    this.client_IMG = clientTryConnect;
                    this.isConnect = true;
                    this.TRW = new Thread_SenderImage(this.W);
                    this.TRW.setClient(this.client_IMG);
                    this.TRW.ELC.addEventLostConnection(IR);
                    this.TRW.start();
                    System.out.println("  Connected from :"
                            + this.client_IMG.getInetAddress().toString() + ":"
                            + this.client_IMG.getPort());

                } 
                else ///поток обмена данными уже существует , но пришел неожидаемый запрос, который нужно  закрыть
                {
                    try {
                        clientTryConnect.close();
                        System.out.println("Поток обмена данными уже существует");
                    } catch (Exception exc) {
                        ReportException.write("Поток обмена данными уже существует" + exc.getMessage());
                    }
                }
            }

        } catch (UnknownHostException ex) {
            System.out.println("Error 3" + ex.getMessage());
            ReportException.write("ConnectCatch.run()  Error 3" + ex.getMessage()  + SettingsConfig.IP + ":" + SettingsConfig.PORT_TCP_IMG);
        } catch (IOException ex) {
            System.out.println("Error 3" + ex.getMessage());
            ReportException.write("ConnectCatch.run()  Error 4" + ex.getMessage()  + SettingsConfig.IP + ":" + SettingsConfig.PORT_TCP_IMG);
        }

    }

    public void clearTRW()
    {
        System.out.println("Lost connection ");
        this.TRW = null;
        this.client_IMG = null;
        this.isConnect = false;
        System.out.println("Create new connection ");
    }

}
