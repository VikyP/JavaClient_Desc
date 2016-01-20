/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student_teamviewer;

import java.io.ByteArrayOutputStream;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Студент
 */
class Thread_SenderImage extends Thread
{
    private Socket client;
    private ScreenTiles ST;
    public EventLostConnection ELC;
    public boolean isClose;
    
    public Thread_SenderImage(int w) 
    {
        this.ELC=new EventLostConnection();
        this.ST = new  ScreenTiles(w);
        this.setDaemon(true);
        this.isClose= false;
    }
    
    public void setClient(Socket client)
    {
        this.client= client;
    }
            
    
    
    @Override
    public void run()
    {
      // System.out.println(this.client.getInetAddress().toString());
       // буфер для получения запроса от клиента на отправку картинки
       byte[] b= new byte [2048];
       ByteArrayOutputStream BAOS = new ByteArrayOutputStream();
       try
       {
           while(true)
           {
              
               BAOS.reset();
               do
               {
                   int cnt =client.getInputStream().read(b, 0, b.length);
                   if(cnt==-1)
                   {
                        throw new IOException ("Reciveed - 1 bytes");
                   }
                   BAOS.write(b, 0 , cnt);               
               }
               while( client.getInputStream().available()>0);
               
               // получаем команду на отправку( preview or full)
               // команда соответсвует 1 byte 
               byte typeView = BAOS.toByteArray()[0];
              
               //отправляем команду на обработку и получаем массив(запакованный) для отправки
               byte[] AA=  ST.PrScrToBytes(typeView);
               
                if(AA!=null)
                { 
                    client.getOutputStream().write(AA);                    
                }
                else
                {
                    client.getOutputStream().write(new byte[Integer.BYTES+Byte.BYTES]);
                }
               
            }
       
       }
       
       catch(IOException ioe)
       {
          System.out.println(  "Lost connection 1" + client.getInetAddress().toString()); 
          try
          {
           IReconnect IR = (IReconnect)ELC.getListener();
           IR.Reconnect();  
          }
          catch(Exception ex)
          {
              System.out.println(  "Lost connection 2" + client.getInetAddress().toString());
              
              System.out.println (ex.getMessage());
          }
          
       } 
       
       finally
       {
           try
           {
               this.isClose= true;
               this.client.close();
               this.client=null;
           } 
           
           catch (IOException ex)
           {
               Logger.getLogger(Thread_SenderImage.class.getName()).log(Level.SEVERE, null, ex);
           }
        }
    }
    
    
}
