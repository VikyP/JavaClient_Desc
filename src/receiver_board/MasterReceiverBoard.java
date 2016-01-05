/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package receiver_board;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.Border;
import masterPanel.SettingsConfig;
import receiver_board.events.IGraphChanged;
import receiver_board.events.ITextChanged;
import userControl.ToolsPanel;

/**
 *
 * @author 06585
 */
public class MasterReceiverBoard extends JPanel {

    
    public ToolsPanel tools;
    public Canvas_BoardR BR;
    public TeacherPane TP;
    JPanel basePanel;
    JPanel canvasPanel;
    JColorChooser colorChooser ;
    JPanel panelHide;
    
    private boolean locationFlag=false;

    public ITextChanged getEvTextChanged() 
    {
        return BR.ITCh;
    }

    public IGraphChanged getEvGraphChanged() 
    {
        return BR.IGrCh;
    }
    
    public IImageChanged getEvNewImage()
    {
       return this.TP.UR;
    }

    public MasterReceiverBoard(final SettingsConfig SC) 
    {
        
        Border raisedbevel = BorderFactory.createRaisedBevelBorder();
        Border loweredbevel = BorderFactory.createLoweredBevelBorder();
        this.setBorder(BorderFactory.createCompoundBorder(raisedbevel, loweredbevel));
        this.setSize(SC.Bounds.getSize());        
        this.setLayout(new BorderLayout());
        
        //<editor-fold defaultstate="collapsed" desc="Формирование панели инструментов ">
        this.colorChooser= new JColorChooser();
       
        this.tools = new ToolsPanel();
     //   this.tools.setPreferredSize(new Dimension(SC.Bounds.width,40));
        this.tools.forecolor.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                // прячем панель, если она открыта
                hidePanel();
                colorChooser.setColor(basePanel.getForeground());
                JColorChooser.createDialog(MasterReceiverBoard.this.tools.forecolor,
                    "Новый цвет текста", true, colorChooser,
                    new ActionListener() 
                    {                           
                        public void actionPerformed(ActionEvent e)
                        {
                           hidePanel();
                           BR.setForeground(colorChooser.getSelectionModel().getSelectedColor());  
                           SC.saveSettingsFore(BR.getForeground());
                           
                        }

                    },  null) .setVisible(true); 
               MasterReceiverBoard.this.repaintFrame();
            }
        });        
        this.tools.backcolor.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                // прячем панель, если она открыта
                hidePanel();
                colorChooser.setColor(basePanel.getBackground()); 
                JColorChooser.createDialog(MasterReceiverBoard.this.tools.backcolor,
                    "Новый цвет фона", true, colorChooser,
                    new ActionListener() 
                    {                           
                        public void actionPerformed(ActionEvent e)
                        {  
                            BR.setBackground(colorChooser.getSelectionModel().getSelectedColor());
                            BR.setColorLine();
                            SC.saveSettingsBack(BR.getBackground());
                        }
                        
                    },  null).setVisible(true); 
                
                MasterReceiverBoard.this.repaintFrame();
              
            }
        });
        this.tools.historyOn.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) 
            {
                JToggleButton TB=(JToggleButton)e.getSource();
                if(TB.isSelected())
                {
                    MasterReceiverBoard.this.BR.history.setVisible(true); 
                }
                else
                {
                    MasterReceiverBoard.this.BR.history.setVisible(false);
                    MasterReceiverBoard.this.BR.setCurrentPage();
                   
                }
                
                
            }
        });
        
        this.tools.screenDesk.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) 
            {
                JToggleButton TB=(JToggleButton)e.getSource();                
                setPanel(TB.isSelected());                
            }
        });
        this.add(this.tools, BorderLayout.NORTH);
        //</editor-fold>   
        
        this.setOpaque(false);
        this.basePanel = new JPanel();
        this.basePanel.setOpaque(false);
        this.basePanel.setLayout(new BoxLayout(this.basePanel,BoxLayout.Y_AXIS));
        this.basePanel.setPreferredSize(new Dimension(SC.Bounds.width, SC.Bounds.height-this.tools.getHeight()));
        
        this.canvasPanel= new JPanel();
        this.canvasPanel.setOpaque(false);
        this.canvasPanel.setLayout( new FlowLayout(FlowLayout.CENTER));
        this.canvasPanel.setPreferredSize(new Dimension(SC.Bounds.width, SC.Bounds.height-this.tools.getHeight()));
        
        TP= new TeacherPane(); 
         
        this.BR = new Canvas_BoardR(this.basePanel.getPreferredSize(),SC.Background,SC.Foreground, SC.scale);
       
        
        this.canvasPanel.add(BR);
        this.basePanel.add(this.canvasPanel);
        
       TP.setVisible(false);
       this.basePanel.add(TP);        
       
        this.add(this.basePanel,BorderLayout.CENTER);        
        this.tools.add(this.BR.history, BorderLayout.SOUTH);
        
        
    }
    
    private void repaintFrame()
    {
        JFrame f=(JFrame) MasterReceiverBoard.this.getParent().getParent().getParent();
        if(locationFlag)
            f.setLocation(f.getX()+1,f.getY()); 
        else
            f.setLocation(f.getX()-1,f.getY());
                locationFlag=!locationFlag;
    
    }
    
    public void setPanel(boolean f)
    {   
        this.canvasPanel.setVisible(f);
        this.TP.setVisible(!f);
    }
    
    
    public void setPanelToHide(JPanel p)
    {
        this.panelHide=p;    
        
    }
    
    private void hidePanel()
    {
        
        if(this.panelHide.isVisible())
            this.panelHide.setVisible(false);
        this.tools.toolsOn.setSelected(false);
    
    }
    
    
    public void setEventOnTools( ActionListener AL)
    {
        this.tools.toolsOn.addActionListener(AL);    
    }
    
    
    
     public void setEventExit( ActionListener AL)
    {
        this.tools.exit.addActionListener(AL);    
    }
     
    public void setEventMinSize( ActionListener AL)
    {
        this.tools.minsize.addActionListener(AL);    
    }
    
    public void setEventMaxSize( ActionListener AL)
    {
        this.tools.maxsize.addActionListener(AL);    
    }
    
    public int getHeightToolBar()
    {
        return this.tools.getPreferredSize().height;
    }

}
