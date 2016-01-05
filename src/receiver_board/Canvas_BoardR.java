/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package receiver_board;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import receiver_board.events.IGraphChanged;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import masterPanel.ReportException;
import receiver_board.Canvas_BoardR.MyMouselistener;
import receiver_board.shapes.IShapeAction;
import receiver_board.events.ITextChanged;
import userControl.HistoryPanel;

/**
 *
 * @author viky
 */
public class Canvas_BoardR extends JEditorPane
{

    private BufferedImage BI;
    public HistoryPanel history;
    private ArrayList<PageContent> pages;

    //new Font("Consolas", Font.PLAIN, 16);
    private int rowsCount = 30;
    
    private int width_B = 710;
    private int height_B = 620;
    private int width_SC = 710;
    private int height_SC = 620;
    private byte fontSize = 16;
    private Font F = new Font(Font.MONOSPACED, Font.PLAIN, fontSize);
  //  private int fontHeight=0;
    
    private byte rowHeigth=0;
    private byte rowHeigthSC=0;
    private byte rowDescent=0;
    private byte rowDescentSC=0;
    
     // количество символов в строке
    private byte colsCount = 80;
    private byte colWidth=0;
    private byte colWidthSC=0;
    
    public Dimension dimScale;
    private float scale = 1;
    private Color colorLine;
    
    private final byte left=35;
    private final byte top=13;
    private final byte right=20;
    private final byte bottom=20;
    
    private byte line =0;
    /**
     * номер страницы полученной по UDP numberPageGet=0, если контент не
     * относится к текущей дате numberPageGet>0, если контент относится к
     * текущей дате
     */
    private byte numberPageGet = 0;

    /**
     * номер отображаемой страницы, указаный студентом
     */
    private byte currentPage = 0;

    private ArrayList<IShapeAction> getShapes()
    {
        return pages.get(currentPage).shapesContent;
    }
     
     public float getScale()
    {
        return this.scale;
    }

    class MyMouselistener extends MouseAdapter
    {

        @Override
        public void mouseClicked(MouseEvent me)
        {
            PageContent page = (PageContent) me.getSource();
            currentPage = page.number;
            Canvas_BoardR.this.setText("");
            Canvas_BoardR.this.setText(Canvas_BoardR.this.pages.get(currentPage).getText());
        }
    }

    /**
     * Получение текста доски
     */
    public ITextChanged ITCh = new ITextChanged()
    {

        @Override
        public void getNewText(byte numPage, byte numLine,byte fontHeigt, String s)
        {
            try{
                
                if(fontSize != fontHeigt)
                {
                    fontSize=fontHeigt;
                    Canvas_BoardR.this.F=new Font(Font.MONOSPACED, Font.PLAIN, fontSize);
                    Canvas_BoardR.this.setFont(F);
                    getMetricsScale(Canvas_BoardR.this.F.deriveFont(fontSize*scale));
                    getMetrics();
                    System.out.println("    **************************");
                }

            //номер доски отрицательный( транслируется доска не текущей даты)
            //положительный сохраняем в историю
            if (numPage > 0)
            {
                Canvas_BoardR.this.numberPageGet = numPage;
            }
            else
            {
                Canvas_BoardR.this.numberPageGet = 0;
            }
            System.out.println("    *1");
            if(!history.isVisible() && currentPage != numberPageGet)
            {
                setCurrentPage();
            }
             System.out.println("    *2");
            
            Canvas_BoardR.this.pagesUpdate(numberPageGet, s);
            System.out.println("    *3");

             //Если отображается текущая доска
            //назначем ей текст
            if (currentPage == numberPageGet )
            {
                if(! s.equals(Canvas_BoardR.this.getText()))
                {
                    System.out.println("    *4");
                    Canvas_BoardR.this.setText("");                
                    Canvas_BoardR.this.setText(s);
                    return;
                }
                
               if( line!=numLine)
                {
                    line=numLine;
                    Canvas_BoardR.this.repaint();

                }
            }
            
            
            }
            catch(Exception exc)
            {
                System.out.println(" getNewText   exc " +exc);
            }
        }

    };

    /**
     * получение графики
     */
    public IGraphChanged IGrCh = new IGraphChanged()
    {
        @Override
        public void getNewGraph(byte numPage, Dimension D, ArrayList<IShapeAction> SA)
        {
            System.out.println("     numPage"+  numPage);
            System.out.println("     D"+  D.toString());
            //случай, когда доска преподавателя не прорисована
            if(D.width!=0 && D.height!=0)
            {
                if(D.width!=Canvas_BoardR.this.width_B)
                    Canvas_BoardR.this.width_B=D.width;
                if(D.height!=Canvas_BoardR.this.height_B)
                    Canvas_BoardR.this.height_B=D.height;
                if(Canvas_BoardR.this.getWidth()!=Canvas_BoardR.this.width_B
                        ||
                        Canvas_BoardR.this.getHeight()!=Canvas_BoardR.this.height_B)
                    Canvas_BoardR.this.setSize(width_B, height_B);
                    
            }
           
            if (numPage > 0)
            {
                Canvas_BoardR.this.numberPageGet = numPage;
            } else
            {
                Canvas_BoardR.this.numberPageGet = 0;
            }
            
            if(!history.isVisible() && currentPage != numberPageGet)
            {
                setCurrentPage();
            }

            //номер доски отрицательный( транслируется доска не текущей даты)
            //положительный сохраняем в историю             
            Canvas_BoardR.this.pagesUpdate(numberPageGet, SA);
            Canvas_BoardR.this.repaint();
        }

    };
    
    BufferedImage buffer;

    public Canvas_BoardR(Dimension D, Color b, Color f, float sc)
    {
        this.history = new HistoryPanel();
        this.setOpaque(false);
       // this.scale = sc;
        this.setEditable(false);
        this.setFocusable(false);
        buffer = new BufferedImage(this.width_B, this.height_B,BufferedImage.TYPE_INT_RGB);
        getMetrics();
        
        this.dimScale= D;// new Dimension((int)(this.width_B*sc), (int)(this.height_B*sc));
        this.setSize(new Dimension(this.width_B, this.height_B));
        this.setPreferredSize(new Dimension(this.width_B, this.height_B));
        this.setMinimumSize(new Dimension(200, 200));
        this.setMargin( new Insets(top, left, bottom, right));
        

        this.setBackground(b);
        this.setForeground(f);
        //scale();
        setColorLine();
        
        this.pages = new ArrayList<PageContent>();
        // нулевая страница соответсвует текущей присланной
        this.pages.add(new PageContent((byte)0, b, f, new MyMouselistener()));
        this.history.historyPanel.add(this.pages.get(this.pages.size() - 1));

        this.numberPageGet = 0;
        this.currentPage = this.numberPageGet;
        this.addComponentListener(new ComponentListener()
        {

            private void notSelected()
            {
                try
                {
                Canvas_BoardR.this.setSelectionStart(0);
                Canvas_BoardR.this.setSelectionEnd(0);
                Canvas_BoardR.this.setCaretPosition(0);
                
                }
                catch(Exception exc)
                {
                    System.out.println(" "+exc.getMessage());
                }
            }

            @Override
            public void componentResized(ComponentEvent e)
            {
                notSelected();
            }

            @Override
            public void componentMoved(ComponentEvent e)
            {
                notSelected();
            }

            @Override
            public void componentShown(ComponentEvent e)
            {
                notSelected(); 
            }

            @Override
            public void componentHidden(ComponentEvent e)
            {
                notSelected();
            }
        });

    }
    /**
     *Определяем  высоту доски в зависимости от шрифта
     * в разных системах один шрифт отрисовывается по разному 
     */
    private void getMetrics()
    { 
        Graphics2D g2d= (Graphics2D)buffer.createGraphics();
        g2d.setFont(this.F);
        FontMetrics metrics = g2d.getFontMetrics(this.F);
        this.rowHeigth= (byte)metrics.getHeight();
        this.colWidth = (byte)metrics.charWidth('X');
        this.rowDescent= (byte)metrics.getMaxDescent();
        this.width_B=this.colWidth*(this.colsCount+1)+this.left+this.right;
        this.height_B=this.rowHeigth*(this.rowsCount+1)+this.top+this.bottom ;
        g2d.dispose();        
        buffer = new BufferedImage(this.width_B, this.height_B,BufferedImage.TYPE_INT_RGB);
       
    }
    
    private void getMetricsScale(Font f)
    { 
        Graphics2D g2d= (Graphics2D)buffer.createGraphics();
        g2d.setFont(f);
        FontMetrics metrics = g2d.getFontMetrics(f);
        this.rowHeigthSC = (byte)metrics.getHeight();
        this.colWidthSC  = (byte)metrics.charWidth('X');
        this.rowDescentSC=(byte)metrics.getMaxDescent();
        this.width_SC=this.colWidthSC*(this.colsCount+1)+(int)((this.left+this.right)*this.scale);
        this.height_SC=this.rowHeigth*(this.rowsCount+1)+(int)((this.top+this.bottom)*this.scale) ;
        g2d.dispose(); 
    }
   
    
    /**
     * Цвет выделения текущей строчки
     */
    public void setColorLine()
    {
        Color b=this.getBackground();
        int delta=40;
        int lim=190;
        colorLine= new Color
            (
                    (b.getRed()>lim)?b.getRed()-delta:b.getRed()+delta,
                    (b.getGreen()>lim)?b.getGreen()-delta:b.getGreen()+delta,
                    (b.getBlue()>lim)?b.getBlue()-delta:b.getBlue()+delta

            );

    }

    @Override
    public void paintComponent(Graphics g)
    {
       // super.paintComponent(g);
       try
       {
            Graphics2D g2D = (Graphics2D)g.create();
            rebuildBuffer();
            g.drawImage(buffer, 0, 0, this);
            drawLinesNumber(g2D);    
            //изменяем порядок прорисовки сначала фигуры потом текст
            getUI().paint(g2D, this);
            g2D.dispose();
          
       }
       catch(Exception exc)
       {
           System.out.println(" Exception !!!" +exc.getMessage());
           ReportException.write(exc.getMessage());
       }
        
    }
    private void rebuildBuffer()
    {  
        Graphics2D g2D = buffer.createGraphics();
        g2D.setColor(getBackground());        
        g2D.fillRect(0, 0, this.getWidth(), this.getHeight());
        
        g2D.scale(this.scale, this.scale);
         drawContur(g2D);

        for (IShapeAction R : this.getShapes())
        {
            R.draw(g2D);
        }      
       
        g2D.dispose();    
    }
   
    /**
     * Прорисовка номера строки
     *
     * @param g Graphics доски
     */
    private void drawLinesNumber(Graphics2D g2D)
    {
        g2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);       
        AlphaComposite A1 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
        g2D.setComposite(A1);        
        g2D.setFont(F.deriveFont(fontSize*scale));
        g2D.setColor(this.getForeground());
        for (int i = 0; i < this.rowsCount; i++)
        {
             g2D.drawString((i + 1) + "", 5, (i+1) * this.rowHeigthSC +this.top*this.scale-this.rowDescentSC);
        }
        AlphaComposite A2 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f);
        g2D.setComposite(A2);
        
    }
    
    private void drawContur(Graphics2D g2D)
    {   
        AlphaComposite A1 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
        g2D.setComposite(A1);
        g2D.setColor(Color.GREEN);
        g2D.setColor(this.colorLine);
        g2D.fillRect(0,(this.line+1)*this.rowHeigth-this.rowDescent,this.getWidth(),this.rowHeigth);
        g2D.drawRect(0, 0, this.getWidth()-5, this.getHeight()-5);
        AlphaComposite A2 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f);
        g2D.setComposite(A2);
    }

    /**
     * При закрытии истории отображается страница(доска), которая транслируентся
     * в данный момент
     */
    public void setCurrentPage()
    {
        this.currentPage = this.numberPageGet;
    }


    /**
     * Вычисление коэфициента масштабирования в зависимости от соотношения
     * высоты и ширины холста
     */
    public void scale()
    {
        if (this.getWidth() == 0 || this.getHeight() == 0 || !this.isDisplayable())
        {
            return;
        }
        
        Dimension D;
        Dimension dimParent = this.getParent().getSize();
        int scaleW = (dimParent.width * 1000) / this.width_B;
        int scaleH = (dimParent.height * 1000) / this.height_B;
        if (scaleW < scaleH)
        {
            this.scale = (float) scaleW / 1000;
            D = new Dimension(dimParent.width, (int) (this.height_B * this.scale));
            System.out.println("W " +D.width);

        } else
        {
            this.scale = (float) scaleH / 1000;
            D = new Dimension((int) ( this.width_B * this.scale), dimParent.height);
            System.out.println("H "+D.height);
        }
        System.out.println(this.scale);
        
        setScale( D);
       
    }
    
    private void setScale(Dimension D)
    {  
        getMetricsScale(Canvas_BoardR.this.F.deriveFont(fontSize*scale));
        this.setSize(D);
        this.setFont(F.deriveFont(fontSize*scale)); 
    }
   
    /**
     * Назначение текстового контента для полученной доски
     *
     * @param number номер доски
     * @param msg текст
     */
    private void pagesUpdate(int number, String msg)
    {

        while (this.pages.size() < number + 1)
        {
            this.pages.add(new PageContent((byte)this.pages.size(), this.getBackground(), this.getForeground(), new MyMouselistener()));
            this.history.historyPanel.add(this.pages.get(this.pages.size() - 1));
        }

        if (!this.pages.get(number).getText().equals(msg))
        {
            this.pages.get(number).setTextContent(msg);
        }
    }

    private void pagesUpdate(int number, ArrayList<IShapeAction> SA)
    {
       
        
        while (this.pages.size() < number + 1)
        {
            this.pages.add(new PageContent((byte)this.pages.size(), this.getBackground(), this.getForeground(), new MyMouselistener()));
            this.history.historyPanel.add(this.pages.get(this.pages.size() - 1));
        }

        this.pages.get(number).setGraph(SA);

    }

}
//<editor-fold defaultstate="collapsed" desc=" PageContent ">
   
class PageContent extends JEditorPane
{

    public ArrayList<IShapeAction> shapesContent = new ArrayList<IShapeAction>();
    float scale = 0.1f;
    Dimension D = new Dimension(75, 60);
    private Font F = new Font(Font.MONOSPACED, Font.PLAIN,2);
    public byte number;

    public PageContent(byte number, Color b, Color f, MyMouselistener MML)
    {
        this.number = number;
        this.setEditable(false);
        this.setSize(D);
        this.setPreferredSize(D);
        this.setBackground(b);
        this.setForeground(f);
        this.setFont(F);
        this.addMouseListener(MML);
    }

    public void setTextContent(String msg)
    {
        synchronized (this.getText())
        {
            this.setText("");
            this.setText(msg);
        }
    }

    public void setGraph(ArrayList<IShapeAction> shapes)
    {
        synchronized (shapesContent)
        {
            if (shapes.size() != shapesContent.size())
            {
                shapesContent.clear();
                shapesContent.addAll(shapes);
                this.repaint();
            }

        }
    }

    @Override
    public void paintComponent(Graphics g)
    {
      super.paintComponent(g);

        // Рисование фона       
        Graphics2D g2D = (Graphics2D)g.create();
      
        g2D.scale(this.scale, this.scale);
        synchronized (this.shapesContent)
        {
            for (IShapeAction R : this.shapesContent)
            {
                R.draw(g2D);
            }
        }
        drawLinesNumber(g2D);
        g2D.dispose();
        
    }

    private void drawLinesNumber(Graphics2D g2D)
    {
       
        g2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        g2D.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 500));
        AlphaComposite A1 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f);
        g2D.setComposite(A1);
        g2D.setColor(this.getForeground());
        g2D.drawString((this.number) + "", 20, 500);

        AlphaComposite A2 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f);
        g2D.setComposite(A2);
        g2D.setFont(this.F);
    }

}
//</editor-fold> 