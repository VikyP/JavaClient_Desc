/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package receiver_board.shapes;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

/**
 *
 * @author Viky
 */
public class EndLineType 
{
    public static final byte NOT=0;
    public static final byte ARROW=1;
    public static final byte CIRCLE=2;
    public static final byte RECTANGLE=3;
    
    
    public static final int RECTANGLE_SIZE=5;
    
    public static final int CIRCLE_SIZE=5;
    
    public static final int ARROW_LENGTH=30;
    public static final int ARROW_WEIDTH=20;
    
    public static final double ARROW_ANGLE = 15*Math.PI/180;
    
    
    
    public static void drawArrowEnd(Graphics2D g, Point start, Point end )
    {
        int w=EndLineType.ARROW_LENGTH;
        int h=EndLineType.ARROW_WEIDTH;
      
        int W=end.x-start.x;
        int H=end.y-start.y;
        if(W!=0 )
        {
            double angle=Math.atan((double)H/W);
            if(end.x>start.x)
            {
                g.drawLine(end.x-(int)(w*Math.cos(angle-EndLineType.ARROW_ANGLE)),end.y-(int)(w*Math.sin(angle-EndLineType.ARROW_ANGLE)), end.x,end.y );
            g.drawLine(end.x-(int)(w*Math.cos(angle+EndLineType.ARROW_ANGLE)),end.y-(int)(w*Math.sin(angle+EndLineType.ARROW_ANGLE)), end.x,end.y );
            }
            else
            {
                g.drawLine(end.x+(int)(w*Math.cos(angle-EndLineType.ARROW_ANGLE)),end.y+(int)(w*Math.sin(angle-EndLineType.ARROW_ANGLE)), end.x,end.y );            
                g.drawLine(end.x+(int)(w*Math.cos(angle+EndLineType.ARROW_ANGLE)),end.y+(int)(w*Math.sin(angle+EndLineType.ARROW_ANGLE)), end.x,end.y );
            }

        }
        else
        {
            if(end.y>start.y)
            {
                g.drawLine(end.x,end.y, end.x+h/2,end.y-w/2 );
                g.drawLine(end.x,end.y, end.x-h/2,end.y-w/2 );
            }  
            else
            {
                g.drawLine(end.x,end.y, end.x+h/2,end.y+w/2 );
                g.drawLine(end.x,end.y, end.x-h/2,end.y+w/2 );            
            }
        }
    
    }
    
    
    public static void drawArrowStart(Graphics2D g, Point start, Point end)
    {
        int w=EndLineType.ARROW_LENGTH;
        int h=EndLineType.ARROW_WEIDTH;
      
        int W=end.x-start.x;
        int H=end.y-start.y;
        if(W!=0 )
        {
            double angle=Math.atan((double)H/W);
            if(end.x>start.x)
            {
                g.drawLine(start.x+(int)(w*Math.cos(angle-EndLineType.ARROW_ANGLE)),start.y+(int)(w*Math.sin(angle-EndLineType.ARROW_ANGLE)), start.x,start.y );
                g.drawLine(start.x+(int)(w*Math.cos(angle+EndLineType.ARROW_ANGLE)),start.y+(int)(w*Math.sin(angle+EndLineType.ARROW_ANGLE)), start.x,start.y );
            }
            else
            {
                g.drawLine(start.x-(int)(w*Math.cos(angle-EndLineType.ARROW_ANGLE)),start.y-(int)(w*Math.sin(angle-EndLineType.ARROW_ANGLE)), start.x,start.y );            
                g.drawLine(start.x-(int)(w*Math.cos(angle+EndLineType.ARROW_ANGLE)),start.y-(int)(w*Math.sin(angle+EndLineType.ARROW_ANGLE)), start.x,start.y );
            }

        }
        else
        {
            if(end.y>start.y)
            {
                g.drawLine(start.x,start.y, start.x+h/2,start.y-w/2 );
                g.drawLine(start.x,start.y, start.x-h/2,start.y-w/2 );
            }  
            else
            {
                g.drawLine(start.x,start.y, start.x+h/2,start.y+w/2 );
                g.drawLine(start.x,start.y, start.x-h/2,start.y+w/2 );            
            }
        }
    
    }
    
    
    
}
