/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package receiver_board.shapes;

/**
 *
 * @author 06585
 */
public class ShapeType
{
    public static final int Line=1;
    public static final int LineHorizontal=2;
    public static final int LineVertical=3;
    public static final int PenLine=4;
    public static final int Ellipse=5;
    public static final int FillEllipse=6;
    public static final int Rectangle=10;  
    public static final int FillRectangle=11;  
    
    public static class LineCl implements IShapeModel
    {
       
        @Override
        public int getType()
        {
            return ShapeType.Line;
        }

        @Override
        public String getName()
        {
            return "Line";
        }

        @Override
        public String getToolTipText()
        {
            return "Линия";
        }
    }
    
    public static class LineHorizontalCl implements IShapeModel
    {
     
        @Override
        public int getType()
        {
            return ShapeType.LineHorizontal;
        }
        
        @Override
        public String getName()
        {
            return "LineHorizontal";
        }

        @Override
        public String getToolTipText()
        {
            return "Горизонтальная линия ";
        }
    }
    
    public static class LineVerticalCl implements IShapeModel
    {
       
        @Override
        public int getType()
        {
            return ShapeType.LineVertical;
        }

        @Override
        public String getName()
        {
            return "LineVertical";
        }

        @Override
        public String getToolTipText()
        {
            return "Вертикальная линия";
        }
    }
    
    public static class PenLineCl implements IShapeModel
    {
       
        @Override
        public int getType()
        {
            return ShapeType.PenLine;
        }

        @Override
        public String getName()
        {
            return "PenLine";
        }

        @Override
        public String getToolTipText()
        {
            return "Карандаш";
        }
    }
    
    public static class EllipseCl implements IShapeModel
    {
       
        @Override
        public int getType()
        {
            return ShapeType.Ellipse;
        }

        @Override
        public String getName()
        {
            return "Ellipse";
        }

        @Override
        public String getToolTipText()
        {
            return "Эллипс";
        }
        
    }
    
    public static class FillEllipseCl implements IShapeModel
    {
       
        @Override
        public int getType()
        {
            return ShapeType.FillEllipse;
        }

        @Override
        public String getName()
        {
            return "FillEllipse";
        }

        @Override
        public String getToolTipText()
        {
           return "Эллипс с заливкой";
        }
        
    }
    public static class RectangleCl implements IShapeModel
    {
       
        @Override
        public int getType()
        {
            return ShapeType.Rectangle;
        }

        @Override
        public String getName()
        {
            return "Rectangle";
        }

        @Override
        public String getToolTipText()
        {
            return "Прямоугольник";
        }
    }
    
    public static class FillRectangleCl implements IShapeModel
    {
        
        @Override
        public int getType()
        {
            return ShapeType.FillRectangle;
        }

        @Override
        public String getName()
        {
            return "FillRectangle";
        }

        @Override
        public String getToolTipText()
        {
            return "Прямоугольник с заливкой";
        }
        
    }
    
    public static final IShapeModel[] Shapes={ new LineCl(), new LineHorizontalCl(), new LineVerticalCl(),new PenLineCl(), 
                                               new EllipseCl(),new FillEllipseCl(),new RectangleCl(),new FillRectangleCl()
    
    };
    
    
    public static String toStr(int Type)
    {
        switch( Type)
        {
            case ShapeType.Line:
                return "Линия";
            case ShapeType.PenLine:
                return "Карандаш";
                
            case ShapeType.Ellipse:
            case ShapeType.FillEllipse:
                return "Эллипс";
            case ShapeType.Rectangle:
            case ShapeType.FillRectangle:
                return "Прямоугольник";
        
        }
    return "Shape ?";
    
    }
    
}