/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package masterPanel;

//import com.sun.awt.AWTUtilities;
import com.sun.awt.AWTUtilities;
//import com.sun.glass.ui.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import receiver_board.MasterReceiverBoard;
import receiver_board.ReceiverBoard_UDP;
import receiver_board.ReceiverScreeen_UDP;
import student_teamviewer.ConnectionManager;
import userControl.ClientToolsPanel;

/**
 *
 * @author viky
 */
public class MasterFrame extends JFrame {

    private Point begin;
    private Point oldLocation;

    class myMouseMotionListener implements MouseMotionListener {

        @Override
        public void mouseDragged(MouseEvent e) {
            //перемещение окна поэкрану мышкой
            if (MasterFrame.this.MRB.BR.getCursor().getType() == java.awt.Cursor.MOVE_CURSOR) {
                int stepX = e.getX() - MasterFrame.this.begin.x;
                int stepY = e.getY() - MasterFrame.this.begin.y;
                Point newLocation = new Point(MasterFrame.this.getX() + stepX, MasterFrame.this.getY() + stepY);
                MasterFrame.this.setLocation(newLocation);
                return;
            }

            if (MasterFrame.this.MRB.BR.getCursor().getType() == java.awt.Cursor.E_RESIZE_CURSOR) {
                int stepX = e.getX() - MasterFrame.this.begin.x;                
                Dimension newSize = new Dimension(MasterFrame.this.getPreferredSize().width + stepX, MasterFrame.this.getPreferredSize().height+stepX);
                MasterFrame.this.setSize(newSize);
                System.out.println("3    W= "+ MasterFrame.this.getWidth()+"  H ="+MasterFrame.this.getHeight());
            }

            if (MasterFrame.this.MRB.BR.getCursor().getType() == java.awt.Cursor.N_RESIZE_CURSOR) {
                
                int stepY = e.getY() - MasterFrame.this.begin.y;               
                Dimension newSize = new Dimension(MasterFrame.this.getWidth(), MasterFrame.this.getPreferredSize().height + stepY);
                MasterFrame.this.setSize(newSize);
                System.out.println("1    W= "+ MasterFrame.this.getWidth()+"  H ="+MasterFrame.this.getHeight());
                
            }
            if (MasterFrame.this.MRB.BR.getCursor().getType() == java.awt.Cursor.SE_RESIZE_CURSOR)
            {
                int stepX = e.getX() - MasterFrame.this.begin.x;
                int stepY = e.getY() - MasterFrame.this.begin.y;
                Dimension newSize = new Dimension(MasterFrame.this.getPreferredSize().width + stepX, MasterFrame.this.getPreferredSize().height + stepY);
                MasterFrame.this.setSize(newSize);
                 System.out.println("2    W= "+ MasterFrame.this.getWidth()+"  H ="+MasterFrame.this.getHeight());
            }
            setNewSizeCanvas();

        }

        @Override
        public void mouseMoved(MouseEvent e)
        { 
            
            int delta = 15;
            if ( (MasterFrame.this.getX() + MasterFrame.this.getWidth()-e.getXOnScreen()) < delta) {
                if ((MasterFrame.this.getY() + MasterFrame.this.getHeight()-e.getYOnScreen()) < delta) {
                    MasterFrame.this.MRB.BR.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.SE_RESIZE_CURSOR));
                    return;
                } else {
                    MasterFrame.this.MRB.BR.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.E_RESIZE_CURSOR));
                    return;
                }
            }
            if (( (MasterFrame.this.getY() + MasterFrame.this.getHeight())-e.getYOnScreen()) < delta) {
                MasterFrame.this.MRB.BR.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.N_RESIZE_CURSOR));
                return;
            } else {
                MasterFrame.this.MRB.BR.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR));
            }

        }
    };

    class MyMouselistener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            System.out.print("mouseClicked");
        }

        @Override
        public void mousePressed(MouseEvent e) 
        {
            if (MasterFrame.this.MRB.BR.getCursor().getType() == java.awt.Cursor.DEFAULT_CURSOR) 
            {
               MasterFrame.this.MRB.BR.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.MOVE_CURSOR));
            }
            MasterFrame.this.begin = e.getPoint();
        }

        @Override
        public void mouseReleased(MouseEvent e)
        {

            if (MasterFrame.this.MRB.BR.getCursor().getType() != java.awt.Cursor.MOVE_CURSOR) 
            {
                MasterFrame.this.setPreferredSize(MasterFrame.this.getSize());                
                setNewSizeCanvas();
            }

            MasterFrame.this.MRB.BR.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR));
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    };
    
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
    private MasterReceiverBoard MRB;
    private ReceiverBoard_UDP receiver;
    private ReceiverScreeen_UDP screen;
    public MasterFrame()
    {
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
        
        this.setMinimumSize(new Dimension(300,300));
        this.setBounds(SC.Bounds);
        this.setPreferredSize(SC.Bounds.getSize());
        this.addMouseListener(new MyMouselistener());
        this.addMouseMotionListener(new myMouseMotionListener());
        this.receiver = new ReceiverBoard_UDP(SC.PORT_UDP_BOARD);
        this.MRB = new MasterReceiverBoard(SC);
        this.setBackground(SC.Background);
        ConnectionManager SenderPrScr = new ConnectionManager(SC);
        
        receiver.ETCh.TextChangedAdd(MRB.getEvTextChanged());
        receiver.EGrCh.GraphChangedAdd(MRB.getEvGraphChanged());

        this.setContentPane(MRB);
        MRB.BR.addMouseListener(new MyMouselistener());
        MRB.BR.addMouseMotionListener(new myMouseMotionListener());
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
        this.tools.setBounds(40, MRB.getHeightToolBar(), 240, 120);
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
            public void actionPerformed(ActionEvent e)
               {
                    MasterFrame.this.setState(JFrame.ICONIFIED);                                        
               }
          });
        
        //разворачивание окна
        MRB.setEventMaxSize(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e)
               {
                   JToggleButton btn= (JToggleButton)e.getSource();
                   if(btn.isSelected())
                     MasterFrame.this.setExtendedState(JFrame.MAXIMIZED_BOTH); 
                   else
                     MasterFrame.this.setExtendedState(JFrame.NORMAL); 
                   
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
        receiver.start();
    }
    
    private void setNewSizeCanvas()
    {     
       this.MRB.BR.scale();
    }

}
