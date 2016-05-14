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
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private boolean isDefine;
    public static boolean  isConnection;
    public Color Background;
    public Color Foreground;
    
    public int width;
    public float opacity;
    final static private Pattern ipPattern =  Pattern.compile("((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)");
    public Rectangle Bounds= new Rectangle();
    
    
   /**
     * порт  маяка студента
     */
    public static int   PORT_UDP; 
    /**
     * порт получения доски
     */
    public static int   PORT_UDP_BOARD;
    private final int DELTA_UDP_BOARD=1;
    /**
     * порт TCP соединения для отправки картинки
     */
    public static int  PORT_TCP_IMG;
    private final int DELTA_TCP_IMG=2;
    /**
     * порт TCP соединения для получения команды
     */
    public static int  PORT_TCP_COMMAND;
    private  final int DELTA_TCP_COMMAND=3;
    
    /**
     * порт для получения экрана преподавателя по UDP
     */
    public static int  PORT_TCP_ScStr;
    private final int DELTA_TCP_ScStr=4;
    public static int  PORT_TCP_ScStr_R;
    private final int DELTA_TCP_ScStr_R=5;
    
    public static InetAddress IP;
    public static InetAddress IP_UDP;

   
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
            
            Element ip_udp = (Element)doc.getElementsByTagName("IP_UDP").item(0); 
            this.IP_UDP=InetAddress.getByName(ip_udp.getTextContent().trim());
            
            Element isDef=(Element)doc.getElementsByTagName("Define").item(0);
            this.isDefine=Boolean.parseBoolean(isDef.getTextContent());
            
            Element isConnect=(Element)doc.getElementsByTagName("AutoConnection").item(0);
            this.isConnection=Boolean.parseBoolean(isConnect.getTextContent());
            // уточняем IP
            if(this.isDefine)
           {
               ArrayList<InetAddress> ipList = new ArrayList<InetAddress >();                  
                try
                {
                    Enumeration item = NetworkInterface.getNetworkInterfaces();
                    Matcher m=null;
                    while(item.hasMoreElements())
                    {
                        NetworkInterface n = (NetworkInterface) item.nextElement();
                        Enumeration ee = n.getInetAddresses();                            
                        while (ee.hasMoreElements())
                        {

                            InetAddress i = (InetAddress) ee.nextElement();
                             m = ipPattern.matcher(i.getHostAddress());
                             if(m.matches())
                            ipList.add(i);                                
                        }
                    }
                    boolean flag=true;
                    for(InetAddress ip_Item:ipList)
                    {       
                        boolean f=true;
                        System.out.println("    IP "+ip_Item);
                        for (int j = 0; j < 4; j++)
                        {
                            f=((IP_UDP.getAddress()[j]&0xFF)==(ip_Item.getAddress()[j]&0xFF)||(this.IP_UDP.getAddress()[j]&0xFF)==255 );
                            if(!f)break;
                        }
                        if(f)
                        {
                            SettingsConfig.IP= ip_Item;                            
                            ip.setTextContent(this.IP.getHostAddress());
                            saveDoc(); 
                            break;
                        }   
                    }

                } 
                catch (SocketException ex)
                {
                    Logger.getLogger(ConnectionDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
                    
            } 
            
            Element p_udp = (Element)doc.getElementsByTagName("PORT_UDP").item(0); 
            this.PORT_UDP=Integer.parseInt(p_udp.getTextContent()); 
            setAllPorts();
            
            
           //</editor-fold> 
            
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
            Element op = (Element)doc.getElementsByTagName("Opacity").item(0); 
            this.opacity=Float.parseFloat(op.getTextContent());
            //</editor-fold>   
           
            return true;
        }
        catch (ParserConfigurationException ex)
        {
            ReportException.write(this.toString()+"\t"+ex.getMessage() );  
        }
        catch (SAXException ex)
        {
            ReportException.write(this.toString()+"\t"+ex.getMessage() );    
        }
        catch (IOException ex)
        {
            ReportException.write(this.toString()+"\t"+ex.getMessage() );   
        }
        
        return false;
    }
    
     public boolean isFirst()
    { 
        DatagramSocket  DS=null;
        try
        {
           DS  = new DatagramSocket (PORT_UDP_BOARD);
           DS.close();
           DS=null;
        } 
        catch (IOException ex)
        { 
            ReportException.write(" "+ ex.getMessage());           
            return false;
        }
        
        return true;
    }
     
     /**
      * Сохранение цвета фона
      * @param b цвет фона
      */
    public void saveSettingsBack( Color b)
    {
        Element back = (Element)doc.getElementsByTagName("Background").item(0);             
        back.setTextContent(String.format( "%02X%02X%02X", b.getRed(), b.getGreen(), b.getBlue() ));

        saveDoc();     
    
    }
    
    /**
     * Сохранение цвета текста
     * @param f цвет текста
     */
    public void saveSettingsFore(Color f)
    {
                   
        Element fore = (Element)doc.getElementsByTagName("Foreground").item(0); 
        fore.setTextContent(String.format( "%02X%02X%02X", f.getRed(), f.getGreen(), f.getBlue() ));
        saveDoc();
    
    }
    
    public void saveBounds(Rectangle R,float op_)
    {
        Element x = (Element)doc.getElementsByTagName("X").item(0);             
        x.setTextContent(String.valueOf( R.x));

        Element y = (Element)doc.getElementsByTagName("Y").item(0);             
        y.setTextContent(String.valueOf( R.y));

        Element w = (Element)doc.getElementsByTagName("Width").item(0);             
        w.setTextContent(String.valueOf( R.width));

        Element h = (Element)doc.getElementsByTagName("Height").item(0);             
        h.setTextContent(String.valueOf( R.height));
        
        Element op = (Element)doc.getElementsByTagName("Opacity").item(0);             
        op.setTextContent(String.valueOf(op_));

        saveDoc();
        
    }
    
   private void  setAllPorts()
    {
        PORT_UDP_BOARD=PORT_UDP+DELTA_UDP_BOARD;            
        PORT_TCP_IMG=PORT_UDP+DELTA_TCP_IMG;
        PORT_TCP_COMMAND=PORT_UDP+DELTA_TCP_COMMAND;
        PORT_TCP_ScStr=PORT_UDP+DELTA_TCP_ScStr;
        PORT_TCP_ScStr_R=PORT_UDP+DELTA_TCP_ScStr_R;
    }
    
   public  boolean setCongig ()
    {    
        boolean isSave=false;    
        try
        {
            setAllPorts();
            
            //если настройки менялись, то изменения сохраняться
            Element isConnect=(Element)doc.getElementsByTagName("AutoConnection").item(0);
            if(isConnection!=Boolean.parseBoolean(isConnect.getTextContent()))
            {
              isConnect.setTextContent(""+isConnection);
              isSave=true;
            }
           
            Element ip = (Element)doc.getElementsByTagName("IP").item(0);
            if(InetAddress.getByName(ip.getTextContent().trim())!=IP)
            {
                ip.setTextContent(IP.getHostAddress());
                isSave=true;
            }
            Element ip_udp = (Element)doc.getElementsByTagName("IP_UDP").item(0);
            if(InetAddress.getByName(ip_udp.getTextContent().trim())!=IP_UDP)
            {
                ip_udp.setTextContent(IP_UDP.getHostAddress());
                isSave=true;
            }
            Element p_udp = (Element)doc.getElementsByTagName("PORT_UDP").item(0);
            if(PORT_UDP!=Integer.parseInt(p_udp.getTextContent()) )
            {
                
                p_udp.setTextContent("" + PORT_UDP);
                isSave = true;
            }
            if(isSave) saveDoc();
        } 
        catch (UnknownHostException ex)
        {
            Logger.getLogger(SettingsConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
        return isSave;
    }
    
    public void saveDoc()
    {
        try {
            Source domSource = new DOMSource(this.doc);
            Result fileResult = new StreamResult(new File("Settings.xml"));
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            transformer.transform(domSource, fileResult);
        } catch (TransformerConfigurationException ex) {
           ReportException.write(this.toString()+"\t"+ex.getMessage() );  
        } catch (TransformerException ex) {
            ReportException.write(this.toString()+"\t"+ex.getMessage() );  
        }
    
    }
}
