/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package receiver_board.shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.DataInputStream;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import masterPanel.ReportException;

/**
 *
 * @author 06585
 */
public class STable extends SContour implements IShapeAction  
{
    private byte rows=0;
    private byte columns=0;

    public STable(DataInputStream DIS, byte type)
    {
        super(DIS, type);
        this.Filling=null;
        try {
            this.rows=DIS.readByte();
            this.columns=DIS.readByte();
        } catch (IOException ex) {
           ReportException.write(this.toString()+"\t"+ex.getMessage() ); 
        }
       
    
    }
    public STable(Point Begin, Point End, Color c, float s, byte t, byte r, byte col)
    {
        super(Begin, End, c, s, t);
        this.Type=ShapeType.Table;
        this.rows=r;
        this.columns=col;
    }
    
     
    @Override
    public Point getBegin()
    {
        return new Point(SRect.x,SRect.y);
    }
    
    @Override
    public Point getEnd()
    {
        return new Point(SRect.x+SRect.width,SRect.y+SRect.height);
    }
     @Override
    public Rectangle getRectangle()
    {
        return this.SRect;
    }
    @Override
    public int getType()
    {
       return this.Type;
    }

     @Override
    public void draw(Graphics2D g2D) 
    {        
        Rectangle R=null;
        if(this.isEditable && this.RectEditable!=null)
            R=this.RectEditable;
        else
            R=this.SRect;
        
       this.setProperties(g2D).drawRect(R.x, R.y, R.width, R.height);
        for(int i=1;i<this.rows;i++)
            g2D.drawLine(R.x, R.y+R.height/rows*i,R.x+ R.width,  R.y+R.height/rows*i);
        
        for(int i=1;i<this.columns; i++)
            g2D.drawLine(R.x+R.width/columns*i, R.y, R.x+R.width/columns*i, R.y+R.height);
        
    }
    
     @Override
    public Rectangle resize_moveRightBottom(int deltaWidth, int deltaHeight)
    {
        this.SetRE_ResizeMoveRightBottom(deltaWidth, deltaHeight);
        return this.RectEditable;
    }

    @Override
    public Rectangle resize_moveLeftTop(int deltaWidth, int deltaHeight)
    {
       this.SetRE_ResizeMoveLeftTop(deltaWidth, deltaHeight);
       return this.RectEditable;
    }

    @Override
    public Rectangle resize_moveRightTop(int deltaWidth, int deltaHeight)
    {
       this.SetRE_ResizeMoveRightTop(deltaWidth, deltaHeight);
       return this.RectEditable;
    }

    @Override
    public Rectangle resize_moveLeftBottom(int deltaWidth, int deltaHeight)
    {
        this.SetRE_ResizeMoveLeftBottom(deltaWidth, deltaHeight);
        return this.RectEditable;
    }

    

    @Override
    public Rectangle move(int xStep, int yStep)
    {
        this.RectEditable = new Rectangle(this.SRect.x + xStep, this.SRect.y + yStep, this.SRect.width,this.SRect.height);
        return this.RectEditable;
    }

    
    @Override
    public void setEditable(boolean flag)
    {
        if( !flag && this.RectEditable!=null)
        {
            this.SRect=this.RectEditable;
        }        
       this.isEditable=flag;
      
    }
     
}
