/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package receiver_board;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.WritableRaster;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import masterPanel.ReportException;


/**
 *
 * @author viky
 */
public class TeacherPane extends JComponent
{
    public BufferedImage BI;    
    public BufferedImage BI_Row;
  
    public Dimension DImg;
    private Dimension DImgReal;
    private int scale_point=1;  
    public Dimension imgD=new Dimension(250,250);
    int []R ;
    
    
  
    public IImageChanged UR= new IImageChanged()
    {

        @Override
        public void getNewImage(DataInputStream dis, byte typeImage)
        { 
           
            if(dis==null)
            {
            TeacherPane.this.BI= new BufferedImage(
                   TeacherPane.this.getWidth(),TeacherPane.this.getHeight(), BufferedImage.TYPE_INT_RGB);
            }
            else                
            switch(typeImage)  
            {
               
                case TypeImageSend.Fast:
                TeacherPane.this.UnPackFast(dis);
                break;
            
                case TypeImageSend.Row:
                TeacherPane.this.UnPackRow(dis);
                break;
                    
                case TypeImageSend.Difference:
                TeacherPane.this.UnPackImage(dis);
                break;
            }
             
        }

    };
    
   
            
            
    public static Dimension PaneSize=new Dimension(250,250);
      
    public TeacherPane()
    { 
      
       this.DImg= new Dimension(PaneSize.width-10,PaneSize.height-20);
       this.DImgReal= new Dimension(PaneSize.width,PaneSize.height);
       this.BI= new BufferedImage(this.DImg.width,this.DImg.height, BufferedImage.TYPE_INT_ARGB); 
       this.BI_Row= new BufferedImage(this.DImg.width,this.DImg.height, BufferedImage.TYPE_INT_ARGB);
       this.setPreferredSize(this.PaneSize);
       this.R= new int [1000];
      
    }
    
   
    
    @Override
    public void paintComponent(Graphics g)
    { 
        g.setColor(new Color (155,155,200));
        
        g.fillRoundRect(0, 0, this.getWidth(), this.getHeight(),5,5);   
        
        if(this.BI== null)return;
       
        Graphics2D g2D = (Graphics2D) g.create();        
        g2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2D.drawImage(this.BI,0,0,this.getWidth(),this.getHeight(),this);
        g2D.dispose();
    
    }
    
    
    public  void UnPackFast( DataInputStream DIS ) 
    {         
        try
        {          
            int w=DIS.readInt();  
            int h=DIS.readInt();
            if(this.DImgReal.width!=w ||this.DImgReal.height!=h)
            {
                this.DImgReal= new Dimension(w,h);
                this.R= new int [w*h];
                UpdateSize();
            }           
           int WF= DIS.readInt();
           int HF=DIS.readInt();
            synchronized(this.BI)
            {               
                if((this.BI.getWidth()!=WF)||(this.BI.getHeight()!=HF))
                {
                   this.BI= new BufferedImage(WF, HF, BufferedImage.TYPE_INT_RGB);  
                } 
            int value=0;int value1=0;int value2=0;
            WritableRaster WR_Small = this.BI.getRaster();      
            DataBuffer DB_small = WR_Small.getDataBuffer();
            for (int i =0; i <HF; i++)
            { 
                for (int j = 0; j < WF; j=j+2)
                {
                    value=DIS.readInt();
                    value1=value&0x00F0F0F0;
                    value2=(value&0x000F0F0F)<<4;
                    DB_small.setElem(i*WF+j, value1) ;
                    DB_small.setElem(i*WF+j+1,value2) ;
                }
            }
            this.repaint();
            }
        }
        catch (IOException ex)
        {
            ReportException.write("StudentPane.UnPackPrewiew()"+ex.getMessage());
        }
    }
   
    public void testImage(BufferedImage BI, String name)
    {
        try
        {
            File F = new File(name + ".png");
            ImageIO.write(BI, "PNG", F);

        } catch (IOException ex)
        {
           
            ReportException.write("StudentPane.UnPackPrewiew()"+ex.getMessage());
        }

    }
    public  void UnPackRow( DataInputStream DIS ) 
    {         
        try
        {
          
            int W=DIS.readInt();   
            int H=DIS.readInt();
            if(this.DImgReal.width!=W ||this.DImgReal.height!=H)
            {
                this.DImgReal= new Dimension(W,H);
                UpdateSize();
            }     
            
            int startRow= DIS.readInt();
            int stopRow=DIS.readInt();            
            int h=stopRow-startRow;
            
            
            int value=0;
            int value1=0;
            int value2=0;
            if((this.BI_Row.getWidth()!=W)||(this.BI_Row.getHeight()!=h))
            {
                this.BI_Row= new BufferedImage(W,h, BufferedImage.TYPE_INT_RGB);
            }             
            WritableRaster WR_Row = this.BI_Row.getRaster();      
            DataBuffer DB_Row = WR_Row.getDataBuffer();
            for (int i =0; i <h; i++)
            { 
                for (int j = 0; j < W; j=j+2)
                {
                    value=DIS.readInt();
                    value1=value&0x00F0F0F0;
                    value2=(value&0x000F0F0F)<<4;
                    DB_Row.setElem(i*W+j,value1) ;
                    DB_Row.setElem(i*W+j+1,value2) ;                   
                }                
            }
            
          
          synchronized(this.BI)
            {  
                BufferedImage BI_TMP= BI;                
                this.BI= new BufferedImage(W,H, BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics2D = this.BI.createGraphics();
                graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR); //Has worked best in my case
                graphics2D.drawImage(BI_TMP, 0, 0, BI.getWidth(), BI.getHeight(), null);
                graphics2D.drawImage(BI_Row, 0,startRow, BI.getWidth(),h, null);
                graphics2D.dispose();                
                this.repaint();
            }
        }
        catch (IOException ex)
        {
            System.out.println(ex.getMessage());
            ReportException.write("StudentPane.UnPackPrewiew()"+ex.getMessage());
        }
        
   
    }
    /**
     * 
     * @param DIS 
     */
    public  void UnPackImage( DataInputStream DIS ) 
    {
          
        try{
        int blockPixelWidth = DIS.readByte()&0xFF;//System.out.println("blockPixelWidth "+blockPixelWidth);   
        int blockPixelHeight = DIS.readByte()&0xFF;// System.out.println("blockPixelHeight "+blockPixelHeight);
        int widthCountOfBlocks = DIS.readInt();// System.out.println("widthCountOfBlocks "+widthCountOfBlocks);
        int heightCountOfBlocks = DIS.readInt();//System.out.println("heightCountOfBlocks "+heightCountOfBlocks);

        int blocks_size = DIS.readInt(); // System.out.println("blocks_size "+blocks_size);

        int pixelInBlock = blockPixelHeight * blockPixelWidth;
        int pixelInLine = pixelInBlock * widthCountOfBlocks;
        int widthResolution= blockPixelWidth* widthCountOfBlocks;
        int heightResolution = blockPixelHeight * heightCountOfBlocks;
        this.DImg.setSize(new Dimension(widthResolution,heightResolution));
        
        synchronized(this.BI)
        {         
         WritableRaster WR_Base = this.BI.getRaster();
         DataBuffer DB_Base = WR_Base.getDataBuffer(); 

         int value=0;
         int value1=0;
         int value2=0;
            for (int b = 0; b < blocks_size; b++)
            {
                int indexBlock =DIS.readInt();       
                int blockFullLines = indexBlock / widthCountOfBlocks;
                int blockInNotFullLine = indexBlock % widthCountOfBlocks;
                int startByte = blockFullLines * pixelInLine + blockInNotFullLine * blockPixelWidth;
                int endByte = startByte + blockPixelHeight * widthResolution - 1;

                for (int i = startByte; i < endByte; i += widthResolution )
                {
                    for (int j = 0; j < blockPixelWidth-1; j=j+2)
                    {                                       
                        try
                        {   

                            value=DIS.readInt();                              
                            value1=value&0x00F0F0F0;
                            value2=(value&0x000F0F0F)<<4;

                            DB_Base.setElem(i+j, value1) ;
                            DB_Base.setElem(i+j+1,value2) ;
                        }
                        catch  (EOFException ex)
                        {
                            System.out.println(ex.getMessage()+" DIS.read()" );
                            ReportException.write("StudentPane.UnPackImage() EOFException\t"+ex.getMessage());
                        }                                                
                    }
                }

            }
        }
        this.repaint();
    }
    catch(Exception ex)
    {
        ex.getMessage();
        ReportException.write("StudentPane.UnPackImage()  Exception\t"+ex.getMessage());
    }

       
    }
    
    
    public void UpdateSize()
    {       
       
        if(this.getParent().getParent()==null
                ||
                this.getParent().getParent().getWidth()==0
               
                ||
                this.getParent().getParent().getHeight()==0)
            return;
        Dimension dimParent=this.getParent().getParent().getSize();
      
        int w= dimParent.width;
        int h =dimParent.height;
        int scaleW =1000;
      //  if(this.DImgReal.width>dimParent.width)
            scaleW=(w*1000)/this.DImgReal.width;
        int scaleH=1000;
        
       // if(this.DImgReal.height>dimParent.height)
          scaleH=(h*1000)/this.DImgReal.height;
         
        if(scaleW<scaleH)
        {  
            this.scale_point=w*1000/this.DImgReal.width;
            this.imgD=new Dimension(w,this.scale_point*this.DImgReal.height/1000);
        }
        else
        {
           this.scale_point=h*1000/this.DImgReal.height;
           this.imgD=new Dimension(this.DImgReal.width*this.scale_point/1000,h);
           
        }
        this.getParent().getParent().setSize(imgD);
        this.getParent().setSize(imgD);
        this.setSize(imgD);
        this.setPreferredSize(imgD);
       
    }
    
}
