package receiver_board;

import java.awt.Dimension;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import receiver_board.events.EventTextChanged;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import masterPanel.ReportException;

import receiver_board.events.EventGraphChanged;
import receiver_board.events.IGraphChanged;
import receiver_board.events.ITextChanged;
import receiver_board.shapes.IShapeAction;
import receiver_board.shapes.SEllipse;
import receiver_board.shapes.SLine;
import receiver_board.shapes.SPenLine;
import receiver_board.shapes.SRectangle;
import receiver_board.shapes.STable;
import receiver_board.shapes.ShapeType;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author viky
 */
public class ReceiverBoard_UDP extends Thread
{
    
    public EventTextChanged ETCh;
    public EventGraphChanged EGrCh;
    private final int port_UDP_Board;
    private final byte  TEXT=1;
    private final byte  SHAPES=2;
    public Dimension canvasSize;
    
    public ReceiverBoard_UDP( int port)
    {
        this.port_UDP_Board=port;
        this.setDaemon(true);
        ETCh= new EventTextChanged();
        EGrCh= new EventGraphChanged();
        canvasSize= new Dimension(0,0);
        
    }
    
    
    @Override 
    public synchronized void run()
    {  try
        {
            DatagramSocket  DS  = new DatagramSocket (this.port_UDP_Board);           
            byte[] byte_info= new byte [32768] ;
            DatagramPacket info= new DatagramPacket (byte_info, 0, byte_info.length);
            boolean isReceive=false;
       
            while(true)
            {
                try
                {
                     DS.receive(info);
                     isReceive=true;
                }
                catch(Exception se)
                {
                    ReportException.write("Receiver_UDP.run() receive()"+se.getMessage());
                  //  System.out.println( " ReceiverBoard_UDP receive() : udp" + se.getMessage());
                    isReceive=false;
                }
               if(isReceive)
               {
                
                ByteArrayInputStream BAIS= new ByteArrayInputStream(info.getData()); 
                
                DataInputStream DIS= new DataInputStream(BAIS);
                byte isRecord=DIS.readByte();
                byte size=DIS.readByte();
                char[] name_gr= new char[size];
                for(int i=0;i<size;i++)
                {
                    name_gr[i]=DIS.readChar();
                }        
                //   System.out.println(" Group " + new String(name_gr));
                byte numberPage=DIS.readByte(); 
              //  System.out.println("  Page ="+numberPage);
                byte type=DIS.readByte(); 
              //  System.out.println("  type ="+type);
                switch (type) {
                    case TEXT:
                        try
                        {
                        int arrSize=DIS.readInt();                           
                        byte line = DIS.readByte();                        
//параметр для согласования размеров холста, шрифта, расположения и масштаба графики
                        byte fontHeigt= DIS.readByte();
                        // размер пакета для записи                       
                        String msg = DIS.readUTF(); 
                        ITextChanged ITCh = (ITextChanged) this.ETCh.getListener();                        
                        ITCh.getNewText(numberPage, line, fontHeigt, msg);                         
                        }
                        catch(Exception exc)
                        {
                            System.out.println("  Exc 1");
                        }
                        break;
                    case SHAPES:
                        // размер пакета для записи
                        int arrSize=DIS.readInt();
                        IGraphChanged IGrCh = (IGraphChanged) this.EGrCh.getListener();
                        
//параметр для согласования размеров холста, шрифта, расположения и масштаба графики                       
                        canvasSize.width= DIS.readInt();
                        canvasSize.height= DIS.readInt();                        
                       IGrCh.getNewGraph(numberPage,canvasSize, readGraph(DIS));
                        break;

                }        
                   BAIS.close();
               }
               
            }
        }
        catch(Exception se)
        {
            ReportException.write("Receiver_UDP.run()"+se.getMessage());
            System.out.println( " ReceiverBoard_UDP SocketException #1 : udp" + se.getMessage());
                    
        }
    }
    
    /**
     * метод чтения графических элементов из байт-массива
     * @param DIS поток для чтения
     * @return  массив графических элементов
     */
    
    private ArrayList<IShapeAction> readGraph(DataInputStream DIS)
    {
       ArrayList<IShapeAction>  shapes = new ArrayList<IShapeAction> ();
       byte type;
       
       int length=0;
        try
        {
            length = DIS.readInt();
            
        } catch (IOException ex)
        {
            Logger.getLogger(ReceiverBoard_UDP.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        for (int i=0; i<length;i++)
            {
            try
            {
                type=DIS.readByte();                
                switch (type)
                {
                    case ShapeType.Line:
                        shapes.add(new SLine(DIS,type));
                        break;
                     case ShapeType.PenLine:
                        shapes.add(new SPenLine(DIS,type));
                        break;   
                    case ShapeType.Ellipse:
                    case ShapeType.FillEllipse:
                        shapes.add(new SEllipse(DIS,type));
                        break;
                    case ShapeType.Rectangle:
                    case ShapeType.FillRectangle:                        
                        shapes.add( new SRectangle(DIS,type));
                        break; 
                        
                    case ShapeType.Table:                      
                        shapes.add( new STable(DIS,type));
                        break;
                }
            } catch (IOException ex)
            {
                System.out.println("    Exc ");
                Logger.getLogger(ReceiverBoard_UDP.class.getName()).log(Level.SEVERE, null, ex);
            }
            }
       
        return shapes;
    
    }
    
    
}
