/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student_teamviewer;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.WritableRaster;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import masterPanel.Receiver_Board;

/**
 *
 * @author viky
 */
public class ScreenProperties
{
    //ширина и высота блока в пикселях
    public  int blockPixelWidth, blockPixelHeight;
    //количество блоков по ширине и высоте
    public  int widthCountOfBlocks, heightCountOfBlocks;
    
    // разрешение экрана
    //ширина
    public int Width;
    //высота
    public int Height;
    
    // снимок экрана для сранения
    private BufferedImage basePictureBuffer;  
    // получение обновленного снимка экрана
    public BufferedImage newPictureBuffer;
    
    
    public Dimension dSmall;
    
    private BufferedImage small;
    
    public DataBuffer DataNew()
    {
        WritableRaster WR_New = this.newPictureBuffer.getRaster();
        return WR_New.getDataBuffer();
    }
    public DataBuffer DataBase()
    {
        WritableRaster WR_Base = this.basePictureBuffer.getRaster();
        return WR_Base.getDataBuffer();
    }
    
    public DataBuffer DataSmall()
    {
        getSmallImage();
        WritableRaster WR_Small = this.small.getRaster();
        return WR_Small.getDataBuffer();
    }
    
    // общее количество блоков
     public   int getTotalCountOfBlocks()
     {
         return widthCountOfBlocks*heightCountOfBlocks;
     }
     //количество pixels  в блоке
     public   int getPixelsInBlock()
     {
         return blockPixelHeight * blockPixelWidth;
     }
     //количество pixel в строке блокa
     public int getPixelsInLine ()
     {
         return blockPixelHeight * blockPixelWidth * widthCountOfBlocks;
     }
            
    
    
    //определение количества блоков по оси х  и по оси у 
    //в зависимости от размеров экрана
    private void getBlocksCount()
    {
        if(basePictureBuffer== null)
        {
            System.out.println("basePictureBuffer== null");   
            return;
        }
        this.Width = this.basePictureBuffer.getWidth(); //System.out.println("this.widthResolution = "+this.widthResolution); 
        this.Height=  this.basePictureBuffer.getHeight();// System.out.println(" this.heightResolution = "+ this.heightResolution); 
      
        int cnt = 48;
        do
        {
            if ((double)((double)this.Width / (double)cnt) % 1 == 0)
            {
                widthCountOfBlocks = cnt;
                break;
            }
            cnt++;
        } while (true);
        cnt = 48;
        do
        {
            if ((double)((double)this.Height / (double)cnt) % 1 == 0)
            {
                heightCountOfBlocks = cnt;
                break;
            }
            cnt++;
        } while (true);

        blockPixelWidth = this.Width / widthCountOfBlocks;
       // System.out.println("blockPixelWidth "+ blockPixelWidth); 
      //  System.out.println(" widthCountOfBlocks "+  widthCountOfBlocks);   
        blockPixelHeight = this.Height / heightCountOfBlocks;
      // System.out.println("blockPixelHeight"+ blockPixelHeight);   
               
            
    }
    
    // размер изображения в зависимости от
    // типа передачи данных ( Full  || Preview)
    public void NewSize( String msg )
    {
        Dimension D =new Dimension();
        
        switch(msg)
        {
            case "FULL":// экран 1:1
                D=getDimensionPrScr();                
                break;
                // предпросмотр
            case "PREVIEW":
                D=this.dSmall;
                break;  
            default:  break;
        }
       
        this.basePictureBuffer=new BufferedImage(D.width, D.height, BufferedImage.TYPE_INT_ARGB);
        WritableRaster WR_Base = this.basePictureBuffer.getRaster();
        DataBuffer DB_Base = WR_Base.getDataBuffer();
        for (int i=0; i<this.basePictureBuffer.getHeight();i++)
        {
            for (int j = 0; j < this.basePictureBuffer.getWidth(); j++)
            {               
               DB_Base.setElem(i*this.basePictureBuffer.getWidth()+j, 0x00FFFFFF);
            }            
        }
        getBlocksCount();       // System.out.println(" Exit new size");
        
    }
    
    // новый кадр 
    public void NextImage()
    {
        this.basePictureBuffer=this.newPictureBuffer; 
    }
    
    //размер экрана
    private Dimension getDimensionPrScr()
    {
         return new Dimension(this.newPictureBuffer.getWidth(),this.newPictureBuffer.getHeight());
    }
    
    //определение размеров панели предпросмотра
    //заданы в файле конфигурации
    public void setDimentionSmall(int w)
    {
        
        this.dSmall = new Dimension();
        this.dSmall.width=w;

        this.dSmall.height=this.newPictureBuffer.getHeight()*w/this.newPictureBuffer.getWidth();            
        this.small= new BufferedImage(this.dSmall.width,this.dSmall.height,BufferedImage.TYPE_INT_ARGB);  

    }
    
    //определение размеров панели предпросмотра
    //заданы в файле конфигурации
    public void setDimentionSmall()
    {
        InputStream IS = null;
        try
        {
            Properties property = new Properties();
            IS = new FileInputStream("config.properties");
            if (IS == null)
            {
                System.out.println("Sorry, unable to find ");
                return;
            }
            property.load(IS);
            int w=Integer.parseInt(property.getProperty("W"));
           // int h =Integer.parseInt(property.getProperty("H"));
           
            this.dSmall = new Dimension();
            this.dSmall.width=w;
            this.dSmall.height=this.newPictureBuffer.getHeight()*w/this.newPictureBuffer.getWidth();            
            this.small= new BufferedImage(this.dSmall.width,this.dSmall.height,BufferedImage.TYPE_INT_ARGB);            
        }
        catch (IOException ex)
        {
            Logger.getLogger(Receiver_Board.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try
            {
                IS.close();
            } catch (IOException ex)
            {
                System.out.println(ex.getMessage());
            }
        }

    }
    
    //масштабирование экрана для предпросмота
    private void getSmallImage() 
    {
      
       // Dimension D= new Dimension(this.newPictureBuffer.getWidth()*this.dSmall.height/this.newPictureBuffer.getHeight(), this.dSmall.height);
        
        Graphics myG = this.small.createGraphics();
        Graphics2D graphics2D = this.small.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR); //Has worked best in my case
        graphics2D.drawImage(this.newPictureBuffer, 0, 0, this.small.getWidth(),this.small.getHeight(), null);
      
        graphics2D.dispose(); 
        myG.dispose();
    }
    
    //проверка на необходимость нового разбиения экрана на блоки
    //при изменении расширения
    public void CheckDimension(String msg)
    {
        //изменилось разрешение экрана
        if(this.basePictureBuffer.getHeight()!=this.newPictureBuffer.getHeight() ||
           this.basePictureBuffer.getWidth()!=this.newPictureBuffer.getWidth())
        {
          //  System.out.println("Error  getChanges()");
            NewSize(msg);
            getBlocksCount();
        }
    }
}
