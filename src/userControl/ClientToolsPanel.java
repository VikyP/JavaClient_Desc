/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userControl;

import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.EtchedBorder;

/**
 *
 * @author Viky
 */
public class ClientToolsPanel extends JPanel
{
       
    public JSlider trancparency ;
    public JCheckBox isAlwaysOnTop;
    
    public ClientToolsPanel()    
    {
        this.setLayout(new FlowLayout(FlowLayout.LEFT));        
        this.add(new JLabel("Прозрачность"));
        this.trancparency= new JSlider(); 
        this.trancparency.setValue(2);  
        this.trancparency.setMaximum(10);
        this.trancparency.setMinimum(0);
        this.trancparency.setMajorTickSpacing(1);
        this.trancparency.setPaintLabels(true);
        this.trancparency.setPaintTicks(true);
        this.add(this.trancparency);
        
        
        this.isAlwaysOnTop = new JCheckBox("Поверх всех окон"); 
        this.isAlwaysOnTop.setSelected(true);
        this.add(this.isAlwaysOnTop);
        this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        
        
    /*    JLabel l=new JLabel(" Размер");
        l.setPreferredSize(new Dimension(100,20));
        this.add(l);
        this.sizer= new JSlider(); 
        this.sizer.setValue(5);
        this.sizer.setMaximum(10);
        this.sizer.setMinimum(1);
        this.sizer.setMajorTickSpacing(1);
        this.sizer.setPaintLabels(true);
        this.sizer.setPaintTicks(true);
        this.add(this.sizer);*/
    
    }
    
}
