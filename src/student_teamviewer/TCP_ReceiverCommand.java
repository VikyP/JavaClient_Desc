/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student_teamviewer;

import java.awt.AWTException;
import java.awt.Cursor;
import java.awt.MouseInfo;
import java.awt.Robot;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import student_teamviewer.robotDevice.MessageAction;

/**
 *
 * @author Студент
 */
public class TCP_ReceiverCommand extends Thread
{

    private Socket clientCommand;
    public EventLostConnection ELC;
    private Robot R;

    public TCP_ReceiverCommand()
    {
        try
        {
            this.R= new Robot();           
            this.setDaemon(true);
            this.ELC=new EventLostConnection();
            
        }
        catch (AWTException ex)
        {
            Logger.getLogger(TCP_ReceiverCommand.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
   public void setClient(Socket client)
    {
        this.clientCommand= client;
    }
    @Override
    public void run()
    {
        System.out.println(this.clientCommand.getInetAddress().toString());

        byte[] buffer;
        ByteArrayOutputStream BAOS = new ByteArrayOutputStream();
        ByteArrayInputStream BAIS =null;
        DataOutputStream DOS = null;
        DataInputStream DIS = null;
       
        try
        {
            while (true)
            {
                buffer = new byte [16];
                BAOS.reset();
                do
                {
                    int cnt = this.clientCommand.getInputStream().read(buffer, 0, buffer.length);
                   // System.out.println(" cnt ="+ cnt);   
                    if (cnt == -1)
                    {
                        throw new IOException("Reciveed - 1 bytes");
                    }
                    BAOS.write(buffer, 0, cnt);
                }
                while (this.clientCommand.getInputStream().available()>0);
                byte[] command =BAOS.toByteArray();
                BAIS = new ByteArrayInputStream( command);
                DIS = new DataInputStream(BAIS);
                byte size=DIS.readByte();
                MessageAction MA= new MessageAction(DIS,size);
                MA.getRobot(R);
                
               // System.out.println("    "+Cursor.getSystemCustomCursor(null));
                
                byte [] report ={1};
               
                this.clientCommand.getOutputStream().write(report);       
                
            }
        }
        catch (IOException ioex)
        {
            try{
                System.out.println(  "Lost connection " + this.clientCommand.getInetAddress().toString()); 
                IReconnect IR = (IReconnect)ELC.getListener();
                IR.Reconnect();
            }
            catch(Exception ex)
            {ex.getMessage();}
        }
        finally
        {/*
            try
            {
               // DOS.close();
               // BAOS.close();
            } catch (IOException ex)
            {
                Logger.getLogger(TCP_ReceiverCommand.class.getName()).log(Level.SEVERE, null, ex);
            }
        */
        }
    }

}
