package masterPanel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.Color;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 *
 * @author Viky_Pa
 */
public class SettingsConfig
{
    public boolean isValid;
    public boolean isFirst;
    public Color Background;
    public Color Foreground;
    public float scale=0;
    
    public int width;
    
    public Rectangle Bounds= new Rectangle();
    
    
   /**
     * порт  маяка студента
     */
    public int  PORT_UDP; 
    /**
     * порт получения доски
     */
    public int  PORT_UDP_BOARD;
    /**
     * порт TCP соединения для отправки картинки
     */
    public int  PORT_TCP_IMG;
    /**
     * порт TCP соединения для получения команды
     */
    public int  PORT_TCP_COMMAND;
    /**
     * порт для получения экрана преподавателя по UDP
     */
    public int  PORT_TCP_ScStr;
    
    
    public InetAddress IP;
    public InetAddress IP_UDP;    

   
    Document doc;
    
    public  SettingsConfig()
    {
        isValid=isLoadStyle();
        isFirst=isFirst();       
    }
    
    private boolean isLoadStyle()
    { 
        try
        {
            
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            this.doc = builder.parse(new File("Settings.xml"));
            
           
            //<editor-fold defaultstate="collapsed" desc="Информация для соединений IP & ports ">
     
            Element ip = (Element)doc.getElementsByTagName("IP").item(0); 
            this.IP=InetAddress.getByName(ip.getTextContent().trim());
            // уточняем IP
            if(!this.IP.getHostAddress().equals(InetAddress.getLocalHost().getHostAddress()))
                this.IP=InetAddress.getLocalHost();
           
            Element ip_udp = (Element)doc.getElementsByTagName("IP_UDP").item(0); 
            this.IP_UDP=InetAddress.getByName(ip_udp.getTextContent().trim());
            
            Element p_udp = (Element)doc.getElementsByTagName("PORT_UDP").item(0); 
            this.PORT_UDP=Integer.parseInt(p_udp.getTextContent());            
            
            this.PORT_UDP_BOARD=this.PORT_UDP+1;            
            this.PORT_TCP_IMG=this.PORT_UDP+2;
            this.PORT_TCP_COMMAND=this.PORT_UDP+3;
            this.PORT_TCP_ScStr=this.PORT_UDP+4;
           //</editor-fold> 
            
            System.out.println("    this.IP"+this.IP);
            Element wPrView = (Element)doc.getElementsByTagName("WidthPreview").item(0);
            this.width=Integer.parseInt(wPrView.getTextContent()); 
            
            Element back = (Element)doc.getElementsByTagName("Background").item(0); 
            this.Background=new Color(Integer.parseInt(back.getTextContent(),16));
            Element fore = (Element)doc.getElementsByTagName("Foreground").item(0);
            this.Foreground=new Color(Integer.parseInt(fore.getTextContent(),16)); 
            
            //<editor-fold defaultstate="collapsed" desc=" Bounds board ">

            Element x = (Element)doc.getElementsByTagName("X").item(0); 
            this.Bounds.x=Integer.parseInt(x.getTextContent());
            
            Element y = (Element)doc.getElementsByTagName("Y").item(0); 
            this.Bounds.y=Integer.parseInt(y.getTextContent());
            
            Element w = (Element)doc.getElementsByTagName("Width").item(0); 
            this.Bounds.width=Integer.parseInt(w.getTextContent());
            
            Element h = (Element)doc.getElementsByTagName("Height").item(0); 
            this.Bounds.height=Integer.parseInt(h.getTextContent());
            
             Element sc = (Element)doc.getElementsByTagName("Scale").item(0);             
             this.scale=Float.parseFloat(sc.getTextContent());
            //</editor-fold>   
            return true;
        }
        catch (ParserConfigurationException ex)
        {
            Logger.getLogger(SettingsConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (SAXException ex)
        {
            Logger.getLogger(SettingsConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
            Logger.getLogger(SettingsConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
     public boolean isFirst()
    {
        boolean flag=false;
         try 
         { 
             ServerSocket s=null;
             try
             {
                 s= new ServerSocket(PORT_TCP_IMG);
                 flag=true;    
             } 
             catch (Exception se)
             {
                 ReportException.write("Sender_UDP.Send(..)!!!!!!!!!!!!" + se.getMessage());
             }
             if(flag)
                 s.close();
             
         } 
         catch (IOException ex)
        {
            Logger.getLogger(SettingsConfig.class.getName()).log(Level.SEVERE, null, ex);  
        }
        return flag;
    }
     
     /**
      * Сохранение цвета фона
      * @param b цвет фона
      */
    public void saveSettingsBack( Color b)
    {
        try
        {
            Element back = (Element)doc.getElementsByTagName("Background").item(0);             
            back.setTextContent(String.format( "%02X%02X%02X", b.getRed(), b.getGreen(), b.getBlue() ));
            
            Source domSource = new DOMSource(this.doc);
            Result fileResult = new StreamResult(new File("Settings.xml"));
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            transformer.transform(domSource, fileResult);
        }
        catch (TransformerConfigurationException ex)
        {
            Logger.getLogger(SettingsConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (TransformerException ex)
        {
            Logger.getLogger(SettingsConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
    
    /**
     * Сохранение цвета текста
     * @param f цвет текста
     */
    public void saveSettingsFore(Color f)
    {
        try
        {            
            Element fore = (Element)doc.getElementsByTagName("Foreground").item(0); 
            fore.setTextContent(String.format( "%02X%02X%02X", f.getRed(), f.getGreen(), f.getBlue() ));
            Source domSource = new DOMSource(this.doc);
            Result fileResult = new StreamResult(new File("Settings.xml"));
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            transformer.transform(domSource, fileResult);
        }
        catch (TransformerConfigurationException ex)
        {
            Logger.getLogger(SettingsConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (TransformerException ex)
        {
            Logger.getLogger(SettingsConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
    
    public void saveBounds(Rectangle R,float scale)
    {
        try
        {
            Element x = (Element)doc.getElementsByTagName("X").item(0);             
            x.setTextContent(String.valueOf( R.x));
            
            Element y = (Element)doc.getElementsByTagName("Y").item(0);             
            y.setTextContent(String.valueOf( R.y));
            
            Element w = (Element)doc.getElementsByTagName("Width").item(0);             
            w.setTextContent(String.valueOf( R.width));
            
            Element h = (Element)doc.getElementsByTagName("Height").item(0);             
            h.setTextContent(String.valueOf( R.height));
            
            Element sc = (Element)doc.getElementsByTagName("Scale").item(0);             
            sc.setTextContent(String.valueOf( scale));
            
            Source domSource = new DOMSource(this.doc);
            Result fileResult = new StreamResult(new File("Settings.xml"));
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            transformer.transform(domSource, fileResult);
        }
        catch (TransformerConfigurationException ex)
        {
            Logger.getLogger(SettingsConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (TransformerException ex)
        {
            Logger.getLogger(SettingsConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
}
