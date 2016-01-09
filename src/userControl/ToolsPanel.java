/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userControl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;

/**
 *
 * @author 06585
 */
public class ToolsPanel extends JPanel {

    public JToggleButton historyOn;
    public JToggleButton screenDesk;
    public JToggleButton toolsOn;
    public JButton exit;
    public JButton minsize;
    public JToggleButton maxsize;
    public JButton forecolor;
    public JButton backcolor;
    public JLabel IP;
    public JLabel screenStatus;
    
    public final static int toolsHeigth=30;
    
    
    public ToolsPanel()
    {
        this.setLayout(new BorderLayout());  
        JPanel TB= new JPanel();
        this.historyOn= new JToggleButton(ImageIconURL.get("resources/openHistory24.png"));
        this.historyOn.setSelectedIcon(ImageIconURL.get("resources/closeHistory24.png"));
       // this.historyOn.setEnabled(false);
        setButtonPaintOff(this.historyOn);
        TB.add(this.historyOn);
       
       // TB.setBackground(Color.red);
      //  this.screenDesk=new JToggleButton(ImageIconURL.get("resources/desk24.png"));
      //  this.screenDesk.setSelectedIcon(ImageIconURL.get("resources/_Monitor24.png"));
      //  this.screenDesk.setEnabled(false);
     //   setButtonPaintOff(this.screenDesk);
       // TB.add(this.screenDesk);
        
        this.toolsOn= new JToggleButton(ImageIconURL.get("resources/setting24.png"));
        this.toolsOn.setSelectedIcon(ImageIconURL.get("resources/setting24_press.png"));
        this.toolsOn.setToolTipText("Настройки окна");
        setButtonPaintOff(this.toolsOn);
        TB.add(this.toolsOn);
      
        this.backcolor= new JButton(ImageIconURL.get("resources/themes_24.png"));
        this.backcolor.setPressedIcon(ImageIconURL.get("resources/themes_24_press.png"));
        this.backcolor.setToolTipText("Цвет фона доски");
        setButtonPaintOff(this.backcolor);
        TB.add(this.backcolor);
        this.forecolor= new JButton(ImageIconURL.get("resources/Fontcolor24.png"));
        this.forecolor.setPressedIcon(ImageIconURL.get("resources/Fontcolor24_press.png"));
        this.forecolor.setToolTipText("Цвет текста доски");
        setButtonPaintOff(this.forecolor);
        TB.add(this.forecolor);
        
        JPanel panelStatus=new JPanel();
        panelStatus.setLayout(new FlowLayout(FlowLayout.CENTER));
        this.IP= new JLabel();
       
        this.IP.setIcon(ImageIconURL.get("resources/IP_student_.png"));
        this.IP.setFont(new Font(Font.SERIF, Font.PLAIN, 15));
        this.IP.setHorizontalAlignment(SwingConstants.CENTER);
       
        this.screenStatus=new JLabel();
        this.screenStatus.setIcon(ImageIconURL.get("resources/signalOff.png"));
        JPanel TBWin= new JPanel();
        
        panelStatus.add(this.screenStatus);
        panelStatus.add(this.IP);
        this.minsize= new JButton(ImageIconURL.get("resources/minsize16.png"));
        this.minsize.setToolTipText("Свернуть");
        this.minsize.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR));
        
        setButtonPaintOff( this.minsize);
        TBWin.add(this.minsize);
        
        this.maxsize= new JToggleButton(ImageIconURL.get("resources/maxSize20.png"));
        this.maxsize.setSelectedIcon(ImageIconURL.get("resources/normSize20.png"));
        this.maxsize.setToolTipText("Развернуть");
        this.maxsize.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR));
        setButtonPaintOff( this.maxsize);
        TBWin.add(this.maxsize);
        
        this.exit= new JButton(ImageIconURL.get("resources/close16.png"));
        this.exit.setToolTipText("Закрыть приложение");
        this.exit.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR));
        setButtonPaintOff( this.exit);
        TBWin.add(this.exit);
        
        this.add(panelStatus,BorderLayout.CENTER);
        this.add(TB, BorderLayout.WEST);
        this.add(TBWin, BorderLayout.EAST);
    }
    
    /**
     * Отключение прорисовки рамки, фона 
     * @param B  кнопка
     */
    private void setButtonPaintOff(JButton B)
    {
        B.setFocusPainted(false);
        B.setBorderPainted(false);
        B.setContentAreaFilled(false); 
        B.setPreferredSize(new Dimension(30,30));
    }
    
    /**
     * Отключение прорисовки рамки, фона 
     * @param B  кнопка
     */
    private void setButtonPaintOff(JToggleButton B)
    {
        B.setFocusPainted(false);
        B.setBorderPainted(false);
        B.setContentAreaFilled(false);  
        B.setPreferredSize(new Dimension(30,30));
    }

}
