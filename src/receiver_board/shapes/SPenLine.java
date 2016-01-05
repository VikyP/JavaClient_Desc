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
import java.awt.geom.GeneralPath;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Viky_Pa
 */
public class SPenLine  extends MyShape implements IShapeAction
{
    
    private Point Begin;
    private Point End;
    
    private Point Begin_Editable=null;
    private Point End_Editable=null;
    
    private static final int step=3;
    private ArrayList <Point> BezierPoints;
    public ArrayList Poins()
    { 
        return BezierPoints;      

    }
    
    public SPenLine( Point begin, Color c, float s, int t)            
    { 
        super(begin, begin, c, s, t);
        this.Type=ShapeType.PenLine; 
        BezierPoints = new ArrayList<Point>();
        BezierPoints.add(begin);
        this.Begin = begin;           
        this.End = begin; 
    }
    
    public SPenLine( ArrayList<Point> points, Color c, float s, int t)            
    { 
        super(points.get(0),points.get(points.size()-1), c, s, t); 
        this.Type=ShapeType.PenLine; 
        BezierPoints = points;       
        this.Begin = points.get(0);           
        this.End = points.get(points.size()-1); 
    }
    
    
    public SPenLine(DataInputStream DIS, int type)
    {
        super(DIS, type);
        try 
        {
            this.BezierPoints= new ArrayList<Point>();
            int size=DIS.readInt();
            for (int i = 0; i < size; i++)
            {
                int x=DIS.readInt();                
                int y=DIS.readInt();    
                
                this.BezierPoints.add(new Point(x,y));                
            }
            this.Begin = this.BezierPoints.get(0);           
            this.End = this.BezierPoints.get(this.BezierPoints.size()-1);
            
            this.thicknessLine=DIS.readFloat();
            this.typeLine= (int)DIS.readByte();
            this.ColorLine=new Color(DIS.readInt());            
            this.SRect = new Rectangle(
            (Begin.x < End.x) ? Begin.x : End.x,
            (Begin.y < End.y) ? Begin.y : End.y,
            Math.abs(End.x - Begin.x),
            Math.abs(End.y - Begin.y));
        } 
        catch (IOException ex)
        {
            Logger.getLogger(SLine.class.getName()).log(Level.SEVERE, null, ex);
        }
            
    }
    
    public void AddPoint(Point p)
    {
        Point Last = BezierPoints.get(BezierPoints.size()-1); 
        // добавление в массив не всех точек, а только те, что дальше от последней на 10
        if (Math.abs(Last.x - p.x) >10 || Math.abs(Last.y - p.y) >10)
        {
            BezierPoints.add(new Point(p.x, p.y));
        }
        this.End = BezierPoints.get(BezierPoints.size()-1); 
      
    }
    
     @Override
   public void BinaryWrite(DataOutputStream DOS)
   {
        try
        {
            DOS.writeByte((byte) this.Type);
            
            DOS.writeInt(this.BezierPoints.size());// arraySize
            
           
            for(Point p:this.BezierPoints)
            {
                DOS.writeInt(p.x);
                
                DOS.writeInt(p.y);
                
            }
            
            DOS.writeFloat(this.thicknessLine);
            DOS.writeByte(this.typeLine);
            DOS.writeInt(this.ColorLine.getRGB());
            
        }
        catch (IOException ex) 
        {
            Logger.getLogger(MyShape.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @Override
    public void draw(Graphics2D g2D)
    {
        if(BezierPoints.size()<3)
            return;
        setProperties(g2D);
        
        GeneralPath gp= new GeneralPath();
        gp.moveTo(this.Begin.x, this.Begin.y);    
        
        int z = (BezierPoints.size() - 1) / step;
        int count = z * step+1 ;       
        for (int i = 0; i+2< BezierPoints.size(); i=i+2)
        {           
            gp.curveTo(BezierPoints.get(i).x, BezierPoints.get(i).y,
            BezierPoints.get(i+1).x, BezierPoints.get(i+1).y,
            BezierPoints.get(i+2).x, BezierPoints.get(i+2).y);          
        }
        
        g2D.draw(gp); 
        
    }

   @Override
    public Rectangle move(int xStep, int yStep)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
  @Override
    public Point getBegin()
    {
       return this.Begin;
    }

    @Override
    public Point getEnd()
    {
        return this.End;
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
    public void setEditable(boolean flag)
    {
        this.isEditable=flag;
    }

   @Override
    public Rectangle resize_moveRightBottom(int deltaWidth, int deltaHeight)
    {
       return this.SRect;
    }

   @Override
    public Rectangle resize_moveLeftTop(int deltaWidth, int deltaHeight)
    {
       return this.SRect;
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
    
}
