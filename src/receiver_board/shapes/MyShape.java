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
import java.awt.RenderingHints;
import java.io.DataInputStream;
import java.io.DataOutputStream;



/**
 *
 * @author 06585
 */
public class MyShape
{    
    protected int Type;
    protected float thicknessLine;
    protected int typeLine;
    protected Color ColorLine;
    protected Rectangle SRect;
    protected Rectangle RectEditable=null;
    protected boolean isEditable=false;
    
    public MyShape(){}
    public MyShape(DataInputStream DIS, int type)
    {
        this.Type=type;
    }
    
    public MyShape(Point Begin, Point End, Color c, float s, int t)
    {
        this.SRect = new Rectangle(
                (Begin.x < End.x) ? Begin.x : End.x,
                (Begin.y < End.y) ? Begin.y : End.y,
                Math.abs(End.x - Begin.x),
                Math.abs(End.y - Begin.y));
        this.ColorLine=c;
        this.thicknessLine=s;
        this.typeLine=t;
    }
   
    
    protected Graphics2D setProperties(Graphics2D g2D)
    {        
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g2D.setColor(this.ColorLine);
        g2D.setStroke(DashArrays.getStrokeLine(this.thicknessLine, this.typeLine));
        return g2D;
    }
    
    public void BinaryWrite(DataOutputStream DOS)
    {}
    
    
    
    @Override
    public String toString()
    {
        switch(this.Type)
        {
            case ShapeType.Line:                
                return "Line";
            case ShapeType.PenLine:
                return "Карандаш";
            case ShapeType.Ellipse:
            case ShapeType.FillEllipse:
                return "Ellipse";
            case ShapeType.Rectangle:
            case ShapeType.FillRectangle:
                return "Rectangle";
        
        }
    return "Shape ?";
    
    }
    
    protected Color isInvert(Color F, Color S)
    {        
        byte range = 10;
        byte step=3;
        byte HalfByte=127;
        byte R= (Math.abs(F.getRed() - S.getRed()) > range)?(byte)S.getRed():(S.getRed()>HalfByte)?(byte)(S.getRed()-range*step):(byte)(S.getRed()+range*step);
        byte G= (Math.abs(F.getGreen() - S.getGreen()) > range)?(byte)S.getGreen():(S.getGreen()>HalfByte)?(byte)(S.getGreen()-range*step):(byte)(S.getGreen()+range*step);
        byte B= (Math.abs(F.getBlue() - S.getBlue()) > range)?(byte)S.getBlue():(S.getBlue()>HalfByte)?(byte)(S.getBlue()-range*step):(byte)(S.getBlue()+range*step);
        return new Color(R,G,B);
       
    }
}
