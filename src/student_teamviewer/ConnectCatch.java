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

/**
 *
 * @author viky
 */
public class ConnectCatch extends Thread {

    private InetAddress IP;
    private int port_IMG;
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

    public ConnectCatch(InetAddress ip, int p_I, int w)
    {
      //  System.out.println("Start catch");
        this.IP = ip;
        this.port_IMG = p_I;
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
                server_IMG = new ServerSocket(this.port_IMG, -1, this.IP);
            }
            catch(NullPointerException ex)
            {
                server_IMG.close();
                server_IMG = new ServerSocket(this.port_IMG, -1, this.IP);
                System.out.println("Error 2" + ex.getMessage());
                ReportException.write("ConnectCatch.run()  Error 2" + ex.getMessage());
            }
            catch (BindException ex) {
                /////////////// Second !!!!!!!!!!!
                System.out.println("Exception " + ex + this.IP + ":" + this.port_IMG);
                ReportException.write("ConnectCatch.run()  Error 1" + ex.getMessage() + this.IP + ":" + this.port_IMG);
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
            ReportException.write("ConnectCatch.run()  Error 3" + ex.getMessage() + this.IP + ":" + this.port_IMG);
        } catch (IOException ex) {
            System.out.println("Error 3" + ex.getMessage());
            ReportException.write("ConnectCatch.run()  Error 4" + ex.getMessage() + this.IP + ":" + this.port_IMG);
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
