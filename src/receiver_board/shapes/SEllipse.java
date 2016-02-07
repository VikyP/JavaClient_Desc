/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package receiver_board.shapes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.DataInputStream;
import java.io.IOException;
import masterPanel.ReportException;

/**
 *
 * @author 06585
 */
public class SEllipse extends SContour implements IShapeAction 
{
    public SEllipse(Point Begin, Point End, Color c, float s, byte t)
    {
        super(Begin, End, c, s,  t);
        this.Type=ShapeType.Ellipse;
    }
    
    public SEllipse(Point Begin, Point End, Color c, Color f, float s, byte t) 
    {
        super(Begin, End, c, s, t);
        this.Filling=f;
        this.Type=ShapeType.FillEllipse;
    }
    
    public SEllipse (DataInputStream DIS, byte type)
    {
        super( DIS, type);
        
        try
        {
            if(this.Type==ShapeType.FillEllipse)
                this.Filling= new Color(DIS.readInt());
            else
                this.Filling=null;
        } catch (IOException ex)
        {
            ReportException.write(this.toString()+"\t"+ex.getMessage() ); 
        }
    }

    //@Override
    public void draw(Graphics2D g2D ) 
    {
        Rectangle R=null;
        if(this.isEditable)
            R=this.RectEditable;
        else
            R=this.SRect;
        if(this.Filling!=null)
        {
            g2D.setColor(this.Filling);
            this.setProperties(g2D).fillOval(R.x, R.y, R.width, R.height);
        }
        else
        this.setProperties(g2D).drawOval(R.x, R.y, R.width, R.height);
       
    }
   
  //  @Override
    public Point getBegin()
    {
        return new Point(SRect.x,SRect.y);
    }
    
   // @Override
    public Point getEnd()
    {
        return new Point(SRect.x+SRect.width,SRect.y+SRect.height);
    }
  //   @Override
    public Rectangle getRectangle()
    {
        return this.SRect;
    }
  //  @Override
    public int getType()
    {
       return this.Type;
    }
  //  @Override
    public Rectangle resize_moveRightBottom(int deltaWidth, int deltaHeight)
    {
        this.SetRE_ResizeMoveRightBottom(deltaWidth, deltaHeight);
        return this.RectEditable;
    }

  //  @Override
    public Rectangle resize_moveLeftTop(int deltaWidth, int deltaHeight)
    {
       this.SetRE_ResizeMoveLeftTop(deltaWidth, deltaHeight);
       return this.RectEditable;
    }

  //  @Override
    public Rectangle resize_moveRightTop(int deltaWidth, int deltaHeight)
    {
       this.SetRE_ResizeMoveRightTop(deltaWidth, deltaHeight);
       return this.RectEditable;
    }

  //  @Override
    public Rectangle resize_moveLeftBottom(int deltaWidth, int deltaHeight)
    {
        this.SetRE_ResizeMoveLeftBottom(deltaWidth, deltaHeight);
        return this.RectEditable;
    }

    
// @Override
    public Rectangle move(int xStep, int yStep) 
    {
       this.RectEditable = new Rectangle(this.SRect.x + xStep, this.SRect.y + yStep, this.SRect.width,this.SRect.height);
       return this.RectEditable;
    }

    

   
 //  @Override
    public void setEditable(boolean flag)
    {
      if(!flag && this.RectEditable!=null)
        {
            this.SRect=this.RectEditable;
        }        
       this.isEditable=flag;
    }

   
}
