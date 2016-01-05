/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student_teamviewer;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.WritableRaster;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Студент
 */
public class Thread_GetPrintScreen extends Thread
{
    
   
    public BufferedImage newPictureBuffer;
    public int time=1000;
 
    private Robot robot;   
    
    public Thread_GetPrintScreen( )
    {
        try
        {
            
            this.robot= new Robot();
            this.newPictureBuffer=MakePrintScreen();
            this.setDaemon(true);
            this.start();
            
        }
        catch (AWTException ex)
        {
            System.out.println( " Ctor Thread_GetPrintScreen " + ex.getMessage());
        }
    }

    @Override
    public synchronized void run()
    {
        while(true)
        {
            try
            {
                this.newPictureBuffer=MakePrintScreen();
                Thread.sleep(time);
                
            } catch (InterruptedException ex)
            {
                Logger.getLogger(Thread_GetPrintScreen.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
    private BufferedImage MakePrintScreen() 
    {
        Dimension D =Toolkit.getDefaultToolkit().getScreenSize();
        BufferedImage BI=robot.createScreenCapture(new Rectangle(0,0,D.width,D.height));
       //*** Огрубление цвета
        WritableRaster WR = BI.getRaster();
        DataBuffer DB = WR.getDataBuffer();
        
        for (int i = 0; i < BI.getHeight(); i++)
        {
            for (int j = 0; j < BI.getWidth(); j++)
            {
              int value =DB.getElem(i*BI.getWidth()+j); 
              value= value &0x00F0F0F0;
              DB.setElem(i*BI.getWidth()+j, value);
            }
        }
        
        return BI;
    }

    
}
