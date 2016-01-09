package receiver_board.shapes;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.BasicStroke;
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

/**
 *
 * @author 06585
 */
public class SLine extends MyShape implements IShapeAction
{
    private Point Begin;
    private Point End;
    
    private Point Begin_Editable=null;
    private Point End_Editable=null;
    
    private byte startLine=0;
    private byte endLine=0;
    
    

    public SLine(Point Begin, Point End, Color c, float s, byte t, byte start, byte end)
    {
        super(Begin, End, c, s,t);
        this.Type=ShapeType.Line;        
        this.Begin=Begin;
        this.End=End;
        this.startLine=start;
        this.endLine=end;
    }
    
    public SLine(DataInputStream DIS, byte type)
    {
        super(DIS, type);
        try 
        {
            this.Begin= new Point();
            Begin.x=DIS.readInt();
            Begin.y=DIS.readInt();
            this.End= new Point();
            End.x=DIS.readInt();
            End.y=DIS.readInt();
            this.thicknessLine=DIS.readFloat();
            this.typeLine=DIS.readByte();
            this.ColorLine=new Color(DIS.readInt());            
            this.SRect = new Rectangle(
            (Begin.x < End.x) ? Begin.x : End.x,
            (Begin.y < End.y) ? Begin.y : End.y,
            Math.abs(End.x - Begin.x),
            Math.abs(End.y - Begin.y));
            try{
            this.startLine=DIS.readByte();
            this.endLine=DIS.readByte();
            }
            catch(Exception exc)
            {
                this.startLine=EndLineType.NOT;
                this.endLine=EndLineType.NOT;
            }
        } 
        catch (IOException ex)
        {
             this.Type=-1;
            Logger.getLogger(SLine.class.getName()).log(Level.SEVERE, null, ex);
        }
            
    }
    
    
    
    @Override
    public Point getBegin()
    {
        return Begin;
    }
    
    @Override
    public Point getEnd()
    {
        return End;
    }
    @Override
    public Rectangle getRectangle()
    {
        return this.SRect;
    }
    
    @Override
    public void draw(Graphics2D g)
    { 
        if(this.isEditable && this.Begin_Editable!=null && this.End_Editable!=null)
            this.setProperties(g).drawLine(this.Begin_Editable.x, this.Begin_Editable.y,this.End_Editable.x, this.End_Editable.y); 
       else
            this.setProperties(g).drawLine(this.Begin.x, this.Begin.y,this.End.x, this.End.y);
        
        switch(this.startLine)
        {
            case EndLineType.ARROW:
                EndLineType.drawArrowStart(g,this.Begin,this.End);
                break;
            case EndLineType.CIRCLE:
                g.fillOval(this.Begin.x-EndLineType.CIRCLE_SIZE, this.Begin.y-EndLineType.CIRCLE_SIZE,EndLineType.CIRCLE_SIZE*2, EndLineType.CIRCLE_SIZE*2);
            break;
                case EndLineType.RECTANGLE:
                g.fillRect(this.Begin.x-EndLineType.RECTANGLE_SIZE, this.Begin.y-EndLineType.RECTANGLE_SIZE,EndLineType.RECTANGLE_SIZE*2, EndLineType.RECTANGLE_SIZE*2);
            break;
        
        }
        switch(this.endLine)
        {
            case EndLineType.ARROW:
                EndLineType.drawArrowEnd(g,this.Begin,this.End);
                break;
            case EndLineType.CIRCLE:
                g.fillOval(this.End.x-EndLineType.CIRCLE_SIZE, this.End.y-EndLineType.CIRCLE_SIZE,EndLineType.CIRCLE_SIZE*2, EndLineType.CIRCLE_SIZE*2);
            break;
                case EndLineType.RECTANGLE:
                g.fillRect(this.End.x-EndLineType.RECTANGLE_SIZE, this.End.y-EndLineType.RECTANGLE_SIZE,EndLineType.RECTANGLE_SIZE*2, EndLineType.RECTANGLE_SIZE*2);
            break;
        
        }
        
       
    }
    
    

    @Override
    public Rectangle move(int xStep, int yStep)
    {
        this.Begin_Editable = new Point(this.Begin.x + xStep, this.Begin.y + yStep);
        this.End_Editable = new Point(this.End.x + xStep, this.End.y + yStep);
        this.RectEditable = new Rectangle(
            (Begin.x < End.x) ? Begin.x + xStep : End.x + xStep,
            (Begin.y < End.y) ? Begin.y + yStep : End.y + yStep,
            this.SRect.width, this.SRect.height);        
        return this.RectEditable;
    }
    
    

    

    @Override
    public Rectangle resize_moveRightBottom(int deltaWidth, int deltaHeight)
    {
        this.Begin_Editable = new Point(this.Begin.x + deltaWidth, this.Begin.y + deltaHeight);
        this.End_Editable = new Point(this.End.x, this.End.y);
           
        this.RectEditable = new Rectangle(
               (this.Begin_Editable.x < this.End_Editable.x) ? this.Begin_Editable.x : this.End_Editable.x,
                (this.Begin_Editable.y < this.End_Editable.y) ? this.Begin_Editable.y : this.End_Editable.y,
                Math.abs(this.End_Editable.x - this.Begin_Editable.x),
                Math.abs(this.End_Editable.y - this.Begin_Editable.y));            
        return this.RectEditable;
    }

    @Override
    public Rectangle resize_moveLeftTop(int deltaWidth, int deltaHeight)
    {
        this.Begin_Editable = new Point(this.Begin.x , this.Begin.y );
        this.End_Editable = new Point(this.End.x+ deltaWidth, this.End.y+ deltaHeight);           
        this.RectEditable = new Rectangle(
               (this.Begin_Editable.x < this.End_Editable.x) ? this.Begin_Editable.x : this.End_Editable.x,
                (this.Begin_Editable.y < this.End_Editable.y) ? this.Begin_Editable.y : this.End_Editable.y,
                Math.abs(this.End_Editable.x - this.Begin_Editable.x),
                Math.abs(this.End_Editable.y - this.Begin_Editable.y));            
        return this.RectEditable;
    }

    @Override
    public Rectangle resize_moveRightTop(int deltaWidth, int deltaHeight)
    {
        return this.SRect;
    }

    @Override
    public Rectangle resize_moveLeftBottom(int deltaWidth, int deltaHeight)
    {
        return this.SRect;
    }

    @Override
    public int getType()
    {
       return this.Type;
    }

    @Override
    public void setEditable(boolean flag)
    {
        if(!flag && this.Begin_Editable!=null && this.End_Editable!=null)
        { 
            this.Begin=this.Begin_Editable;
            this.End=this.End_Editable;
            this.SRect=this.RectEditable;
        }        
       this.isEditable=flag;
    }

   

}
