/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userControl;

import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EtchedBorder;

/**
 *
 * @author 06585
 */
public class HistoryPanel  extends JPanel 
{
    private JScrollPane scrollPane;
    public JPanel historyPanel;
    public HistoryPanel()
    {
      
        this.setSize(150, 70);
        this.setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
        this.scrollPane= new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_NEVER,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        this.setVisible(false);
        historyPanel= new JPanel();
        historyPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.scrollPane.setViewportView(historyPanel);
       
        this.add(this.scrollPane);
        this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));    
    }
    
}
