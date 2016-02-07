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
import masterPanel.ReportException;



/**
 *
 * @author viky
 */

// определяет параметры картинки
// определяет индексы блоков с отличиями
// фомирование массива байт необходимых блоков
class ScreenTiles
{
    public static final byte NULL=0;
    public static final byte PREVIEW=1;
    public static final byte FULL=2; 
    
    private byte TypeView=1;
    private ScreenProperties ImageToSend;
    
    //индексы блоков для отправки
    ArrayList<Integer> blocks= new ArrayList<>();
    private Robot robot;
    
    
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
            ReportException.write(this.toString()+"\t"+ex.getMessage() ); 
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
        //*** Огрубление цвета
        Dimension D =Toolkit.getDefaultToolkit().getScreenSize();
        BufferedImage BI=robot.createScreenCapture(new Rectangle(0,0,D.width,D.height));
        DataBuffer DB = robot.createScreenCapture(new Rectangle(0,0,D.width,D.height)).getRaster().getDataBuffer();
        
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
    
    /**
     * обработка события запроса картинки
     * @param typeView тип ззапрашиваемой картинки
     * @return  данные для отправки
     */
    public byte [] PrScrToBytes(byte typeView)
    {
       Runtime r=Runtime.getRuntime(); 
        ImageToSend.newPictureBuffer=MakePrintScreen();
        if(this.TypeView!=typeView)
        {
            this.TypeView= typeView;
            ImageToSend.NewSize(this.TypeView);
        }
        switch(typeView)
        {
            case FULL:
                r.gc () ;
                return this.gzip(this.byteCompressor());
                
            case PREVIEW:
               r.gc () ;
                return this.gzip(this.byteCompressorPreview());
        }
        r.gc () ;
        return getHead(NULL);
    }
    
    /**
     * сжатие пакета данных
     * @param body исходные данные
     * @return сжатые данные
     */
    byte [] gzip( byte [] body)
    {  
        // произошел сбой на этапе подготовки пакета
       if(body==null)
       {
           ReportException.write("ScreenTiles return NULL /tType View :"+ this.TypeView);
           return getHead(NULL);
       }
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
                return getHead(NULL);
                
            }
            catch (IOException ex)
            {
                return getHead(NULL);
            }
            
            byte [] bodyZip=BAOS.toByteArray();            
            byte [] head =this.getHead(bodyZip.length);
            BAOS.reset();
            if(bodyZip.length==0)
            {
                BAOS.write(getHead(NULL));
            }
            else
            {
                BAOS.write(head);
                BAOS.write(bodyZip, 0, bodyZip.length);
            }
            return  BAOS.toByteArray();
        } 
        catch (IOException ex)
        { 
            return getHead(NULL);
        } 
    }

    /**
     * определение блоков с отличиями для отправки
     */
    private void getChanges()
    {
       
        this.ImageToSend.CheckDimension(this.TypeView);
        DataBuffer DB_Base = this.ImageToSend.basePictureBuffer.getRaster().getDataBuffer();
        DataBuffer DB_New = this.ImageToSend.newPictureBuffer.getRaster().getDataBuffer();
        
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

    /**
     * Подготовка пакета для отправки предпросмотра
     * @return 
     */
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
                ReportException.write(this.toString()+"\t"+ex.getMessage() ); 
            }
        }
    }
     
    /**
     * Формироваине заголовка 
     * размер пакета для распаковки
     * тип просмотра
     * @param size 
     * @return 
     */
    public byte [] getHead(int size ) 
    {
        ByteArrayOutputStream BAOS= new ByteArrayOutputStream();
        DataOutputStream DOS= new DataOutputStream(BAOS);
        try
        {
            DOS.writeInt(size);
            DOS.writeByte(this.TypeView);
            return BAOS.toByteArray();
        } 
        catch (IOException ex)
        {
           ReportException.write("ScreenTiles.getHead() return NULL "+ex.getMessage());
           return null;
        }
        finally
        {
            try
            {
                DOS.close();
            } catch (IOException ex)
            {
               ReportException.write("ScreenTiles "+ex.getMessage());
            }
        } 
    }
     /**
      * подготовка пакета для отправки
      * @return 
      */
    public byte[] byteCompressor( )
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
        
        DataBuffer DB_New =this.ImageToSend.newPictureBuffer.getRaster().getDataBuffer();
       
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
            ReportException.write("ScreenTiles.byteCompressor() return NULL "+ex.getMessage());
            return null;
        }
       
        finally
        {
            try {
                DOS.close();
            } 
            catch (IOException ex)
            {
                ReportException.write(this.toString()+"\t"+ex.getMessage() ); 
            }
        }
        
    }
    
    
}
    

