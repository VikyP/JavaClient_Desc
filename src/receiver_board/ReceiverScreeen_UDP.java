/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package receiver_board;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Calendar;
import java.util.zip.GZIPInputStream;
import masterPanel.ReportException;


/**
 *
 * @author Viky
 */
public class ReceiverScreeen_UDP extends Thread
{
    private int port_UDP;
    public EventImageChanged EImCh;
    public Calendar timeReceive;
    public ReceiverScreeen_UDP (int port)
    {
        this.EImCh= new EventImageChanged();
        this.port_UDP=port;
        this.setDaemon(true);
        this.timeReceive = Calendar.getInstance();
        this.timeReceive.add(Calendar.SECOND, -5);
    }
    
    @Override 
    public void run()
    {
        try
        {
            DatagramSocket  DS  = new DatagramSocket (this.port_UDP);           
            byte[] byte_info= new byte [65536] ;
            DatagramPacket info= new DatagramPacket (byte_info, 0, byte_info.length);
            
            
            while(true)
            {
                try{
                
                DS.receive(info);
                ReportException.write("Receiver_UDP.run() receive");
                ByteArrayInputStream BAIS= new ByteArrayInputStream(info.getData());                
                DataInputStream DIS= new DataInputStream(BAIS);
                
                
                /*************************************/
                
                this.timeReceive = Calendar.getInstance();
                byte isRecord=DIS.readByte();
                
               // System.out.println(" is Record " + isRecord+this.timeReceive.getTime().toString());
                int length=(int)DIS.readByte();
                ReportException.write("Receiver_UDP.run() receive L"+length);
                char[] name_gr= new char[length];
                for(int i=0;i<length;i++)
                {
                    name_gr[i]=DIS.readChar();
                }        
              //  System.out.println(" Group " + new String(name_gr));                
                 /*************************************/
                
                
                
                int   lengthByteArr = DIS.readInt();
               // System.out.println(" lengthByteArr " + lengthByteArr); 
                byte typeImage=DIS.readByte();
             //    System.out.println(" typeImage " + typeImage);
               ///////////// System.out.println("    ------------------------------"+lengthByteArr);                
                ByteArrayOutputStream BAOS = new ByteArrayOutputStream();
                byte[] dataBuffer = new byte[8192];
                int size = 0;
                do
                {
                    int cnt = DIS.read(dataBuffer, 0, dataBuffer.length);
                    if (cnt == -1)
                    {
                      break;
                    }
                    BAOS.write(dataBuffer, 0, cnt);
                    size = size + cnt;
                } 
                while (lengthByteArr - size > 0);
                    
                byte[] body= unzip(BAOS.toByteArray());
               
                BAIS.reset();
                BAIS= new ByteArrayInputStream(body); 
                DIS= new DataInputStream(BAIS);
                IImageChanged IImCh= (IImageChanged)this.EImCh.getListener();
                IImCh.getNewImage(DIS, typeImage);
                BAIS.close();
                }
                catch(Exception se)
                {
                    IImageChanged IImCh= (IImageChanged)this.EImCh.getListener();
                    IImCh.getNewImage(null, (byte)10);
                    ReportException.write("Receiver_UDP.run()"+se.getMessage());
                    System.out.println( " ReceiverScreeen_UDP  SocketException #1 : udp" + se.getMessage());

                }
                
            }
        }
        catch(Exception se)
        {
            ReportException.write("Receiver_UDP.run()"+se.getMessage());
            System.out.println( " ReceiverScreeen_UDP  SocketException #1 : udp" + se.getMessage());
        }
    }
    
    private byte [] unzip(byte [] tmp)
    {
            
        byte[] buffer = new byte[8192]; 
        ByteArrayOutputStream Baos = new ByteArrayOutputStream();
        ByteArrayInputStream BAIS = new ByteArrayInputStream(tmp);
       int length = 0;
        try
        { 
            GZIPInputStream gzipis = new GZIPInputStream(BAIS);
            
            while ((length = gzipis.read(buffer)) > 0)
            {
                Baos.write(buffer, 0, length);
            }
            BAIS.close();
            gzipis.close();          
            
        } 
        catch (FileNotFoundException ex)
        {
            System.out.println("  1 "  +ex.getMessage());
            ReportException.write("TCP_Client_RecieverPrScr.unzip()  FileNotFoundException\t"+ex.getMessage());
        } 
        catch (IOException ex)
        {
            System.out.println( " 2 "+length + "  "+ex.getMessage());
             ReportException.write("TCP_Client_RecieverPrScr.unzip()  IOException\t"+ex.getMessage());
        }
        finally {
     
    }
         
    return  Baos.toByteArray();
    
    }
    
}
