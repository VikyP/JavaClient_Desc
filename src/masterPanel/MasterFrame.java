/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package masterPanel;

//import com.sun.awt.AWTUtilities;
import com.sun.awt.AWTUtilities;
import java.awt.Component;
import java.awt.Cursor;
//import com.sun.glass.ui.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import receiver_board.MasterReceiverBoard;
import receiver_board.ReceiverBoard_UDP;
import receiver_board.ReceiverScreeen_UDP;
import student_teamviewer.ConnectionManager;
import userControl.ClientToolsPanel;
import userControl.ImageIconURL;
import userControl.ToolsPanel;

/**
 *
 * @author viky
 */
public class MasterFrame extends JFrame {

    private Point begin;

    class MouseMotionListenerFrame implements MouseMotionListener {

        @Override
        public void mouseDragged(MouseEvent e)
        {
            Cursor mouseCursor =((Component)e.getSource()).getCursor();
            //перемещение окна поэкрану мышкой
            if (mouseCursor.getType() == java.awt.Cursor.MOVE_CURSOR)  
                
            {
                int stepX = e.getX() - MasterFrame.this.begin.x;
                int stepY = e.getY() - MasterFrame.this.begin.y;
                Point newLocation = new Point(MasterFrame.this.getX() + stepX, MasterFrame.this.getY() + stepY);
                MasterFrame.this.setLocation(newLocation);
                return;
            }
            Dimension newSize= new Dimension();
           

            if (mouseCursor.getType() == java.awt.Cursor.E_RESIZE_CURSOR)
            {
                int stepX = e.getX() - MasterFrame.this.begin.x;                
                 newSize = 
                        new Dimension(MasterFrame.this.getPreferredSize().width + stepX, 
                        MasterFrame.this.getPreferredSize().height + MasterFrame.this.MRB.BR.getStepY(stepX));
            }

            if (mouseCursor.getType() == java.awt.Cursor.N_RESIZE_CURSOR)
            {
                int stepY = e.getY() - MasterFrame.this.begin.y;               
                 newSize = new Dimension(MasterFrame.this.getPreferredSize().width + MasterFrame.this.MRB.BR.getStepX(stepY), 
                     MasterFrame.this.getPreferredSize().height + stepY);
              
                
            }
            if (mouseCursor.getType() == java.awt.Cursor.SE_RESIZE_CURSOR)
            {
                int stepX = e.getX() - MasterFrame.this.begin.x;
                int stepY = MasterFrame.this.MRB.BR.getStepY(stepX);
                newSize = new Dimension(MasterFrame.this.getPreferredSize().width + stepX, MasterFrame.this.getPreferredSize().height + stepY);
            }
            
           MasterFrame.this.setSize(newSize);
           setNewSizeCanvas();

        }

        @Override
        public void mouseMoved(MouseEvent e)
        { 
            Component mouseComponent=((Component)e.getSource());
            int delta = 15;
            if ( (MasterFrame.this.getX() + MasterFrame.this.getWidth()-e.getXOnScreen()) < delta)
            {
                if ((MasterFrame.this.getY() + MasterFrame.this.getHeight()-e.getYOnScreen()) < delta) 
                {
                    mouseComponent.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.SE_RESIZE_CURSOR));
                    return;
                }
                else 
                {
                    mouseComponent.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.E_RESIZE_CURSOR));
                    return;
                }
            }
            if (( (MasterFrame.this.getY() + MasterFrame.this.getHeight())-e.getYOnScreen()) < delta)
            {
                mouseComponent.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.N_RESIZE_CURSOR));
            }
            else
            {
                mouseComponent.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR));
            }

        }
    };
    private MouseMotionListenerFrame mouseMotionFrame;
    class MouselistenerFrame extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            System.out.print("mouseClicked");
        }

        @Override
        public void mousePressed(MouseEvent e) 
        {
            Component mouseComponent=((Component)e.getSource());
            if (mouseComponent.getCursor().getType() == java.awt.Cursor.DEFAULT_CURSOR) 
            {
               mouseComponent.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.MOVE_CURSOR));
            }
            
            MasterFrame.this.begin = e.getPoint();
        }

        @Override
        public void mouseReleased(MouseEvent e)
        {
            Cursor mouseCursor =((Component)e.getSource()).getCursor();

            if (mouseCursor.getType() != java.awt.Cursor.MOVE_CURSOR) 
            {
                MasterFrame.this.setPreferredSize(MasterFrame.this.getSize());
            }

            ((Component)e.getSource()).setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR));
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    };
    private MouselistenerFrame mouseFrame;
    /**
     * Обработка изменения положения слайдера прозрачности
     */
    public ChangeListener CL_transparency = new ChangeListener() {

        @Override
        public void stateChanged(ChangeEvent e) {
            
            JSlider source = (JSlider) e.getSource();
            if(source.getValue()>90)
                return;
            float transp = (source.getMaximum()-(float) source.getValue())/ source.getMaximum();
           AWTUtilities.setWindowOpacity(MasterFrame.this, transp);
        }
    };
    
    /**
     * Обработка изменения положения слайдера прозрачности
     */
    public ChangeListener CL_sizer = new ChangeListener() {

        @Override
        public void stateChanged(ChangeEvent e) {
            
            JSlider source = (JSlider) e.getSource();
            if(source.getValue()>90)
                return;
            float transp = (source.getMaximum()-(float) source.getValue())/ source.getMaximum();
           
        }
    };
    
    /**
     * Обработка CheckBox поверхвсех окон
     */
    public ItemListener CL_topAll = new ItemListener() {

        @Override
        public void itemStateChanged(ItemEvent e) {
            JCheckBox source = (JCheckBox) e.getSource();
            MasterFrame.this.setAlwaysOnTop(source.isSelected());
        }
    };

    private ClientToolsPanel tools;
    private final int toolPanelHeight=140;
    private final int toolPanelWidth=230;
    private final int toolPanelX=40;
    private MasterReceiverBoard MRB;
    private ReceiverBoard_UDP receiver;
    private ReceiverScreeen_UDP screen;
    private Timer checkReceiverScreeen_UDP;
    private TimerTask iconScreeen_UDP = new TimerTask()
    {
       

        @Override
        public void run()
        {
            boolean status=(Calendar.getInstance().getTimeInMillis()-MasterFrame.this.screen.timeReceive.getTimeInMillis()>1100);
            
            if(MRB.isDesc==status)
                return;
            else
            { 
                MRB.isDesc=status;
                if(MRB.isDesc)
                { 
                    MasterFrame.this.MRB.tools.screenStatus.setIcon(ImageIconURL.get("resources/signalOff.png"));
                   
                }
                else
                {
                    MasterFrame.this.MRB.tools.screenStatus.setIcon(ImageIconURL.get("resources/signalOn.png"));                   
                }
                
                
                MasterFrame.this.MRB.setPanel(MRB.isDesc);
                MasterFrame.this.setFrameSize();
            }
        }
    };
    public MasterFrame()
    {
        this.checkReceiverScreeen_UDP = new Timer();
        this.mouseFrame= new MouselistenerFrame();
        this.mouseMotionFrame= new MouseMotionListenerFrame();
        final SettingsConfig SC= new SettingsConfig();
        if(!SC.isValid)
        {
            JOptionPane.showMessageDialog(this, "Ошибка при загрузке файла конфигурации (Settings.xml)");
            System.exit(0);        
        }
        
       
        if(!SC.isFirst)
        {
            JOptionPane.showMessageDialog(this, "Допускается запуск только одной копии");
            System.exit(0);        
        }
        this.setIconImage(ImageIconURL.get("resources/desk24.png").getImage());
        
        this.setBounds(SC.Bounds);
        this.setPreferredSize(SC.Bounds.getSize());
        this.addMouseListener(this.mouseFrame);
        this.addMouseMotionListener(this.mouseMotionFrame);
        this.receiver = new ReceiverBoard_UDP(SC.PORT_UDP_BOARD);
        this.MRB = new MasterReceiverBoard(SC);
        
        
        this.setBackground(SC.Background);
        ConnectionManager SenderPrScr = new ConnectionManager(SC);
        
        receiver.ETCh.TextChangedAdd(MRB.getEvTextChanged());
        receiver.EGrCh.GraphChangedAdd(MRB.getEvGraphChanged());
       
        this.setContentPane(MRB);
        MRB.BR.addMouseListener(this.mouseFrame);  
        MRB.BR.addMouseMotionListener(this.mouseMotionFrame);
        MRB.tools.historyOn.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) 
            {
                Dimension d=MasterFrame.this.getSize();
                JToggleButton TB=(JToggleButton)e.getSource();
                if(TB.isSelected())
                {
                    MRB.BR.history.setVisible(true);                    
                    MasterFrame.this.setSize(d.width, d.height+MRB.BR.history.getHeight());   
                }
                else
                {
                    MRB.BR.history.setVisible(false);
                    MasterFrame.this.setSize(d.width, d.height-MRB.BR.history.getHeight());
                }
                MasterFrame.this.setPreferredSize(MasterFrame.this.getSize());
            }
        });
        
        this.setTitle(" Receiver ");
        
        this.tools = new ClientToolsPanel();
        this.tools.setVisible(false);
        this.tools.setBounds(toolPanelX, MRB.getHeightToolBar(), toolPanelWidth, toolPanelHeight);
        this.tools.trancparency.addChangeListener(CL_transparency);
        this.tools.isAlwaysOnTop.addItemListener(CL_topAll);
    
        JLayeredPane lp = getLayeredPane();
        lp.add(this.tools, JLayeredPane.POPUP_LAYER);
        
        MRB.setPanelToHide(this.tools);
        
        this.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent we)
            {
                SC.saveBounds(MasterFrame.this.getBounds(),MRB.BR.getScale());
                System.exit(0);
            }
            
            
            
        });
        this.addComponentListener(new ComponentListener()
        {

            @Override
            public void componentResized(ComponentEvent e)
            {
               setNewSizeCanvas();              
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void componentShown(ComponentEvent e)
            {
               MasterFrame.this.setMinimumSize(MasterFrame.this.MRB.BR.getMinSize(ToolsPanel.toolsHeigth));
               setNewSizeCanvas();
               setFrameSize();
            }

            @Override
            public void componentHidden(ComponentEvent e) {
              //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        
        });

        MRB.setEventOnTools(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MasterFrame.this.tools.setVisible(!MasterFrame.this.tools.isVisible());
            }
        }
        );

        //сворачивание окна
        MRB.setEventMinSize(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e)
               {
                    MasterFrame.this.setState(JFrame.ICONIFIED);                                        
               }
          });
        
        //разворачивание окна
        MRB.setEventMaxSize(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e)
               {
                   JToggleButton btn= (JToggleButton)e.getSource();
                   if(btn.isSelected())
                   {
                       MasterFrame.this.setExtendedState(JFrame.MAXIMIZED_BOTH);
                       btn.setToolTipText("Свернуть в окно");                        
                   } 
                   else
                   {                     
                       MasterFrame.this.setExtendedState(JFrame.NORMAL);
                       btn.setToolTipText("Развернуть");                    
                   }
                   
                   setNewSizeCanvas();
               }
          
            
          });
        
        
        //закрытие приложения
        MRB.setEventExit(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                SC.saveBounds(MasterFrame.this.getBounds(),MRB.BR.getScale());
                System.exit(0);
            }
            

        }
        );
        setFrameSize();
        // прозрачность
        try
        {
            
            this.setUndecorated(true);
            this.setAlwaysOnTop(true);
            this.setVisible(true);
            AWTUtilities.setWindowOpacity(this, 0.8f);
        } 
        catch (Exception ex) 
        {
            System.out.println("  exception " + ex.getMessage());
        }
        this.screen= new ReceiverScreeen_UDP(SC.PORT_TCP_ScStr);
        this.screen.EImCh.ImageChangedAdd(this.MRB.getEvNewImage());
        this.screen.start();
        this.checkReceiverScreeen_UDP.schedule(iconScreeen_UDP, 1000, 500);
        
        receiver.start();
    }
    
    
    private void setNewSizeCanvas()
    { 
        if(this.MRB.isDesc)
            this.MRB.BR.scale();
        else
            this.MRB.TP.UpdateSize();
    }
    
    private void setFrameSize()
    {
        Dimension d= new Dimension(800, 600);
        if(MRB.isDesc)
                { 
                    d= new Dimension(MasterFrame.this.MRB.BR.dimScale.width,MasterFrame.this.MRB.BR.dimScale.height+MRB.tools.getHeight());                   
                }
                else
                {
                    d= new Dimension(MasterFrame.this.MRB.TP.imgD.width, MasterFrame.this.MRB.TP.imgD.height+MRB.tools.getHeight());
                }
        if(!d.equals(this.getPreferredSize()))
        {
            this.setSize(d);
            this.setPreferredSize(d);
        }
    }

}
