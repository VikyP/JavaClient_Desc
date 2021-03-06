/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package receiver_board.shapes;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.DataInputStream;
import java.io.IOException;
import masterPanel.ReportException;

/**
 *
 * @author 06585
 */
public class SContour extends MyShape
{
    protected Color Filling;    
    
    public SContour(){}
    
    public SContour(DataInputStream DIS, byte type)
    {
        super( DIS, type);
        try
        {
           
            Point Begin= new Point();
            Begin.x=DIS.readInt();
            Begin.y=DIS.readInt();
            
            int w=DIS.readInt();
            int h=DIS.readInt();
            this.SRect = new Rectangle(Begin.x,Begin.y,w,h);
            
            this.thicknessLine=DIS.readFloat();
            this.typeLine=(int) DIS.readByte();
            this.ColorLine=new Color(DIS.readInt());  
        } 
        catch (IOException ex)
        {
            ReportException.write(this.toString()+"\t"+ex.getMessage() ); 
        }
    }
    
    public SContour(Point Begin, Point End, Color c, float s, byte t) 
    {
        super(Begin, End, c,s, t);
        this.Filling=null;        
    }
    
    public SContour(Point Begin, Point End, Color c, Color f, float s, byte t) 
    {
        super(Begin, End, c, s, t);
        this.Filling=f;        
    }
    
   
    
    protected void SetRE_ResizeMoveRightBottom(int deltaWidth, int deltaHeight)
    {
        int Delta_x = this.SRect.width + deltaWidth;
        int Delta_y = this.SRect.height + deltaHeight;
        int x = 0, y = 0, w = 0, h = 0;
        if (Delta_x < 0)
        {
            x = this.SRect.x + Delta_x;
            w = Math.abs(Delta_x);
        }
        else
        {
            x = this.SRect.x;
            w = this.SRect.width + deltaWidth;
        }
        if (Delta_y < 0)
        {
            y = this.SRect.y + Delta_y;
            h = Math.abs(Delta_y);
        }
        else
        {
            y = this.SRect.y;
            h = this.SRect.height + deltaHeight;
        }
        this.RectEditable = new Rectangle(x, y, w, h);
        
    }
    protected void SetRE_ResizeMoveLeftTop(int deltaWidth, int deltaHeight)
    {
        int Delta_x = this.SRect.width - deltaWidth;
        int Delta_y = this.SRect.height - deltaHeight;
        int x = 0, y = 0, w = 0, h = 0;
        if (Delta_x < 0)
        {
            x = this.SRect.x+this.SRect.width;
            w = Math.abs(Delta_x);
        }
        else
        {
            x = this.SRect.x + deltaWidth;
            w = Delta_x;
        }
        if (Delta_y < 0)
        {
            y = this.SRect.y+this.SRect.height;
            h = Math.abs(Delta_y);
        }
        else
        {
            y = this.SRect.y + deltaHeight;
            h = Delta_y;
        }
        this.RectEditable = new Rectangle(x, y, w, h);
    }
    protected void SetRE_ResizeMoveRightTop(int deltaWidth, int deltaHeight)
    {
        int Delta_x = this.SRect.width + deltaWidth;
        int Delta_y = this.SRect.height - deltaHeight;
        int x = 0, y = 0, w = 0, h = 0;
        if (Delta_x < 0)
        {
            x = this.SRect.x + Delta_x;
            w = Math.abs(Delta_x);
        }
        else
        {
            x = this.SRect.x;
            w = Delta_x;
        }
        if (Delta_y < 0)
        {
            y = this.SRect.y+this.SRect.height;
            h = Math.abs(Delta_y);
        }
        else
        {
            y = this.SRect.y + deltaHeight;
            h = Delta_y;
        }
        this.RectEditable = new Rectangle(x, y, w, h);

    }
    protected void SetRE_ResizeMoveLeftBottom(int deltaWidth, int deltaHeight)
    {
        int Delta_x = this.SRect.width- deltaWidth;
        int Delta_y = this.SRect.height + deltaHeight;
        int x = 0, y = 0, w = 0, h = 0;
        if (Delta_x < 0)
        {
            x = this.SRect.x+this.SRect.width;
            w = Math.abs(Delta_x);
        }
        else
        {
            x = this.SRect.x + deltaWidth;
            w = Delta_x;
        }
        if (Delta_y < 0)
        {
            y = this.SRect.y + Delta_y;
            h = Math.abs(Delta_y);
        }
        else
        {
            y = this.SRect.y;
            h = this.SRect.height + deltaHeight;
        }
        this.RectEditable = new Rectangle(x, y, w, h);
    }
    
}
