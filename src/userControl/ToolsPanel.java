/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userControl;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

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
    
    public ToolsPanel()
    {
        this.setLayout(new BorderLayout());  
        JPanel TB= new JPanel();
        this.historyOn= new JToggleButton(ImageIconURL.get("resources/openHistory24.png"));
        this.historyOn.setSelectedIcon(ImageIconURL.get("resources/closeHistory24.png"));
       // this.historyOn.setEnabled(false);
        setButtonPaintOff(this.historyOn);
        TB.add(this.historyOn);
        
        
        this.screenDesk=new JToggleButton(ImageIconURL.get("resources/desk24.png"));
        this.screenDesk.setSelectedIcon(ImageIconURL.get("resources/_Monitor24.png"));
      //  this.screenDesk.setEnabled(false);
        setButtonPaintOff(this.screenDesk);
        TB.add(this.screenDesk);
        
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
        
        
        JPanel TBWin= new JPanel();
        
        this.minsize= new JButton(ImageIconURL.get("resources/minsize20_1.png"));
        this.minsize.setToolTipText("Свернуть");
        this.minsize.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR));
        
        setButtonPaintOff( this.minsize);
        TBWin.add(this.minsize);
        
        this.maxsize= new JToggleButton(ImageIconURL.get("resources/maxSize20.png"));
        this.maxsize.setToolTipText("Развернуть");
        this.maxsize.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR));
        setButtonPaintOff( this.maxsize);
        TBWin.add(this.maxsize);
        
        this.exit= new JButton(ImageIconURL.get("resources/close20_1.png"));
        this.exit.setToolTipText("Закрыть приложение");
        this.exit.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR));
        setButtonPaintOff( this.exit);
        TBWin.add(this.exit);
        
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
