/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student_teamviewer.robotDevice;

//import com.sun.glass.events.KeyEvent;

import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import masterPanel.ReportException;

/**
 *
 * @author Студент
 */


public class MessageAction
{
    
    private byte action=-1;
 //   private byte size=-1;
    
    private int KM_code=-1;
    
    private int X=-1 ;
    private int Y=-1;
   
    public   MessageAction ()
    {
      
    }
    public   MessageAction (byte act, int kc)
    {
     this.action=act;
     this.KM_code=kc;
   //  this.size=(byte)Byte.SIZE/Byte.SIZE+(byte)Byte.SIZE/Byte.SIZE+(byte)Integer.SIZE/Byte.SIZE;
    //  this.size=(byte)Byte.BYTES+(byte)Byte.BYTES+(byte)Integer.BYTES;
    // size + actiontype + key_code
    }
    
    public   MessageAction (byte at, int x, int y )
    {
     this.action=at;
     this.X=x;
     this.Y=y;
    // this.size=(byte)Byte.SIZE/Byte.SIZE +    (byte)Byte.SIZE/Byte.SIZE+    (byte)Integer.SIZE/ Byte.SIZE+   (byte)Integer.SIZE/Byte.SIZE;
    // this.size=(byte)Byte.BYTES +    (byte)Byte.BYTES +    (byte)Integer.BYTES +   (byte)Integer.BYTES;
    // size + actiontype  x+y;
    }
    
    
    public MessageAction(DataInputStream DIS)
    {
        try
        {
          //  this.size =s;
            this.action=DIS.readByte();
            if(this.action!=ActionType.Mouse_Move)
            {
                
              this.KM_code= DIS.readInt();
              
            }
            if(this.action==ActionType.Mouse_Move)
            {
                this.X=DIS.readInt();
                this.Y= DIS.readInt();            
            }            
        } 
        catch (IOException ex)
        {
            ReportException.write(this.toString()+"\t"+ex.getMessage() ); 
        } 
    }
    
    public void getRobot( Robot R)
    {
        
        switch(this.action)
        {
            case ActionType.Key_Press:
                R.keyPress(this.KM_code);
                break;
            case ActionType.Key_Release:
                R.keyRelease(KM_code);
                break;
                
            case ActionType.Mouse_Press:
               if(this.KM_code==1)               
                    R.mousePress(InputEvent.BUTTON1_MASK);
               if(this.KM_code==2)               
                    R.mousePress(InputEvent.BUTTON2_MASK);
                if(this.KM_code==3)               
                    R.mousePress(InputEvent.BUTTON3_MASK);
                break;
            case ActionType.Mouse_Release:
                if(this.KM_code==1)  
                    R.mouseRelease(InputEvent.BUTTON1_MASK);
                if(this.KM_code==2) 
                    R.mouseRelease(InputEvent.BUTTON2_MASK);
                if(this.KM_code==3) 
                    R.mouseRelease(InputEvent.BUTTON3_MASK);
                break;
            case ActionType.Mouse_Move:
                R.mouseMove(X, Y);
                break;
                
        }
     
    }
}
