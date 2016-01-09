/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student_teamviewer;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.WritableRaster;

import java.io.ByteArrayInputStream;

import java.io.ByteArrayOutputStream;

import java.io.DataOutputStream;

import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.ArrayList;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPOutputStream;



/**
 *
 * @author viky
 */

// определяет параметры картинки
// определяет индексы блоков с отличиями
// фомирование массива байт необходимых блоков
public class ScreenTiles
{

    private final String FULL="FULL";
    private final String PREVIEW ="PREVIEW";
    private String TypeView="";
    private ScreenProperties ImageToSend;
   
   // private Thread_GetPrintScreen T_GPS;

    //индексы блоков для отправки
    ArrayList<Integer> blocks= new ArrayList<>();
    private Robot robot;
    
    private int getTypeView()
    {
        switch(this.TypeView)
        {
            case FULL:return 1;
            case PREVIEW: return 0;
        
        }
    return -1;
    
    }
    
    public ScreenTiles(int w)
    {  
        ImageToSend= new ScreenProperties();        
        this.TypeView= this.PREVIEW;
        
        try
        {
            this.robot= new Robot();
        } 
        catch (AWTException ex)
        {
            this.robot=null;
            Logger.getLogger(ScreenTiles.class.getName()).log(Level.SEVERE, null, ex);
        }
         ImageToSend.newPictureBuffer=MakePrintScreen();
         ImageToSend.setDimentionSmall(w);
    }
    /**
     * Создание и подготовка(огрубление цвета) снимка экрана
     * @return снимок экрана
     */
    private BufferedImage MakePrintScreen() 
    {
        Dimension D =Toolkit.getDefaultToolkit().getScreenSize();
        BufferedImage BI=robot.createScreenCapture(new Rectangle(0,0,D.width,D.height));
       //*** Огрубление цвета
        WritableRaster WR = BI.getRaster();
        DataBuffer DB = WR.getDataBuffer();
        
        for (int i = 0; i < BI.getHeight(); i++)
        {
            for (int j = 0; j < BI.getWidth(); j++)
            {
              int value =DB.getElem(i*BI.getWidth()+j); 
              value= value &0x00F0F0F0;
              DB.setElem(i*BI.getWidth()+j, value);
            }
        }
        
        return BI;
    }
    
    public byte [] PrScrToBytes(String msg) throws IOException
    {
      
       
        ImageToSend.newPictureBuffer=MakePrintScreen();
        
        if(!this.TypeView.equals(msg))
        {
           this.TypeView= msg;
           ImageToSend.NewSize(this.TypeView);
           
        }
        switch(msg)
        {
            case FULL: 
            return this.gzip(this.byteCompressor());
        
            case PREVIEW:     
               
            return this.gzip(this.byteCompressorPreview());       
        }
        return null;
    }
    
    byte [] gzip( byte [] body)
    {  
       
        try
        {
            ByteArrayInputStream BAIS = new ByteArrayInputStream(body);
            ByteArrayOutputStream BAOS = new ByteArrayOutputStream();
            
            try 
            {
                byte[] buffer = new byte[32768];
                
                GZIPOutputStream gzos = new GZIPOutputStream (BAOS);                
                int length;
                while ((length = BAIS.read(buffer)) > 0)
                {
                    gzos.write(buffer, 0, length);
                }
                gzos.finish();
                gzos.close();                
            }
            catch (FileNotFoundException ex)
            {
                return null;
                
            } catch (IOException ex)
            {
                return null;
            }
            
            byte [] bodyZip=BAOS.toByteArray();            
            byte [] head =this.getHead(bodyZip.length +5);//int(4 byte)+1byte
          
            BAOS.reset();
            BAOS.write(head);
            BAOS.write(bodyZip, 0, bodyZip.length);
            
            return  BAOS.toByteArray();
            
        } 
        catch (IOException ex)
        { 
            return null;
        } 
    }

    
    private void getChanges()
    {
        this.ImageToSend.CheckDimension(this.TypeView);
        DataBuffer DB_Base = this.ImageToSend.DataBase();
        DataBuffer DB_New = this.ImageToSend.DataNew();
        
        // очищается контейнер для отправки блоков
        this.blocks.clear();
        int value_Base=0;
        int value_New=0;
        for (int block = 0; block < this.ImageToSend.getTotalCountOfBlocks(); block++)
        {   
            //опредеяет переход на новую строку блока
            int blockFullLines = block / this.ImageToSend.widthCountOfBlocks;

            int blockInNotFullLine = block % this.ImageToSend.widthCountOfBlocks;

            int startByte = blockFullLines * this.ImageToSend.getPixelsInLine() + blockInNotFullLine * this.ImageToSend.blockPixelWidth;
            int EndByte =startByte + this.ImageToSend.blockPixelHeight * this.ImageToSend.Width - 1;

            for (int i = startByte; i < EndByte; i += this.ImageToSend.Width )
            {
                for (int j = 0; j < this.ImageToSend.blockPixelWidth ; j++)
                {
                 
                    value_Base =DB_Base.getElem(i+j);
                    value_New =DB_New.getElem(i+j);
                    if ((value_Base ^ value_New) != 0)
                    {
                        this.blocks.add(block);
                        i=EndByte;
                        j=this.ImageToSend.blockPixelWidth;
                       
                    }
                    
                }
                
            }
                    
         } 
        
        this.ImageToSend.NextImage(); 

    }

    public byte[] byteCompressorPreview( ) 
    {
        ByteArrayOutputStream BAOS= new ByteArrayOutputStream();
        DataOutputStream DOS= new DataOutputStream(BAOS);
      
        DataBuffer DB_small = this.ImageToSend.DataSmall();
        try
        {
            //////////int size= ( 4+ 1+   4+  4 +  image.w* image.h *2);
            int size= Integer.BYTES+Byte.BYTES+Integer.BYTES+Integer.BYTES+DB_small.getSize()*2;
            
            int w=this.ImageToSend.dSmall.width;
            int h=this.ImageToSend.dSmall.height;
            DOS.writeInt(w);//4
          //  System.out.println("    w ="+ w);
            DOS.writeInt(h );//4
          //  System.out.println("    h"+ h);
            int value1=0;
            int value2=0;
            int pixel=0;
            for (int i = 0; i <h; i++)
            {
                for (int j = 0; j < w; j=j+2)
                {
                    
                  value1 =DB_small.getElem(i*w+j)&0x00F0F0F0;
                  value2 =DB_small.getElem(i*w+j+1)&0x00F0F0F0;
                  pixel=(value1|((value2 &0x00FFFFFF)>>4));
                  DOS.writeInt(pixel);
                   
                }
             
            }
           
            DOS.close();
            return BAOS.toByteArray();
        }
        catch( IOException ex)
        { 
            return null;
        }
       
        finally
        {
            try
            {
                DOS.close();
            }
            catch (IOException ex)
            {
                Logger.getLogger(ScreenTiles.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
     
    public byte [] getHead(int size ) throws IOException
    {
        ByteArrayOutputStream BAOS= new ByteArrayOutputStream();
        DataOutputStream DOS= new DataOutputStream(BAOS);
        try
        {
            DOS.writeInt(size);
            DOS.writeByte((byte)this.getTypeView());
            return BAOS.toByteArray();
        } 
        catch (IOException ex)
        {
           return null;
        }
        finally
        {
            DOS.close();
        }
    }
     
    public byte[] byteCompressor( ) throws IOException
    {
        ByteArrayOutputStream BAOS= new ByteArrayOutputStream();
        DataOutputStream DOS= new DataOutputStream(BAOS);
        
        try
        {
           
        getChanges();
        
        DOS.writeByte(this.ImageToSend.blockPixelWidth); //System.out.println("blockPixelWidth "+blockPixelWidth);
           
        //высота блока в пикселях
        DOS.writeByte(this.ImageToSend.blockPixelHeight);//System.out.println("blockPixelHeight "+blockPixelHeight);
        
        //количество блоков по ширине
        DOS.writeInt(this.ImageToSend.widthCountOfBlocks);//System.out.println("widthCountOfBlocks "+widthCountOfBlocks);
        
        //количество блоков по высоте
        DOS.writeInt(this.ImageToSend.heightCountOfBlocks);//System.out.println("heightCountOfBlocks "+heightCountOfBlocks);
        
        DOS.writeInt(blocks.size());  //System.out.println("blocks.size() "+blocks.size()); 
        
        DataBuffer DB_New =this.ImageToSend.DataNew();
       
        for (int b = 0; b < blocks.size(); b++)
        {
            DOS.writeInt(blocks.get(b));
          
            int blockFullLines = blocks.get(b) / this.ImageToSend.widthCountOfBlocks;
            int blockInNotFullLine = blocks.get(b) % this.ImageToSend.widthCountOfBlocks;
            int startByte = blockFullLines * this.ImageToSend.getPixelsInLine() + blockInNotFullLine * this.ImageToSend.blockPixelWidth;
            int endByte = startByte + this.ImageToSend.blockPixelHeight * this.ImageToSend.Width - 1;
            for (int i = startByte; i < endByte; i += this.ImageToSend.Width )
            {
                for (int j = 0; j < this.ImageToSend.blockPixelWidth-1; j=j+2)
                {
                    int value1 =DB_New.getElem(i+j)&0x00F0F0F0;
                    int value2 =DB_New.getElem(i+j+1)&0x00F0F0F0;
                    int pixel=(value1|((value2 &0x00FFFFFF)>>4)); 
                    DOS.writeInt(pixel);
                      
                }
           }
            
        }
        return BAOS.toByteArray();
        }
        catch( IOException ex)
        { 
            return null;
        }
       
        finally
        {
            DOS.close();
        }
        
    }
    
    
    
    
    
    
}
    

