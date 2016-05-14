/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */ 

package masterPanel;

import javax.swing.JOptionPane;
import javax.swing.JDialog;
import javax.swing.JTextField;
import java.beans.*; //property change stuff
import java.awt.*;
import java.awt.event.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/* 1.4 example used by DialogDemo.java. */
class ConnectionDialog extends JDialog
                   implements ActionListener,
                              PropertyChangeListener {
    private JTextField textIP;
    private JTextField textIP_Br;
    private JTextField port;
    private JLabel message;
    private JLabel error;
    public JCheckBox isAutoConnection;

    private JOptionPane optionPane;

    private String btnString1;
    private final String EXIT = "Выход";
    private final String CONNECT = "Подключиться";
    private final String OK = "OK";
    private Dimension controlD;
    private boolean isValid=true;
 final static private Pattern ipPattern =  Pattern.compile("((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)");
  //  final static private Pattern ipPattern =  Pattern.compile("(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)");


   

    /** Creates the reusable dialog. */
    public ConnectionDialog(Frame aFrame, boolean flag) {
        super(aFrame, true);
        /*
        Matcher m=ipPattern.matcher("192.168.0.103");
        if(m.find())            
        System.out.println("    M " +m.group());*/
        btnString1 =flag?OK: CONNECT;
        setTitle("Настройки подключения");
        controlD=new Dimension(100, 20);
        this.setSize(300, 340);
        this.setLocation(aFrame.getX()+(aFrame.getWidth()-this.getWidth())/2, aFrame.getY()+50);
        
        this.message= new JLabel( flag? "Подключение выполнено":"");
        error = new JLabel();
        error.setForeground(Color.red);
        
        textIP = new JTextField(SettingsConfig.IP.getHostAddress());
        textIP.setSize(controlD);
        textIP.addFocusListener(new FocusListener(){

            @Override
            public void focusGained(FocusEvent e)
            {
               // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void focusLost(FocusEvent e)
            {
                JTextField tf=(JTextField)e.getSource();
                ArrayList<InetAddress> ipList = new ArrayList<InetAddress >();
                try
                {
                    InetAddress  ip =InetAddress.getByName(tf.getText()); 
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
                        /*
                        проверка адресов машины
                        */
                        for(InetAddress i:ipList)
                        {       
                             if(ip.equals(i))
                             { 
                                SettingsConfig.IP= ip;
                                return;
                             }
                        }
                        
                    } catch (SocketException ex)
                    {
                        Logger.getLogger(ConnectionDialog.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                } 
                catch (UnknownHostException ex)
                {
                    error.setText(" IP указан неверно: "+tf.getText());
                }
                isValid=false;
                tf.setText(""+SettingsConfig.IP.getHostAddress());
            }
        });
        JPanel p=new JPanel();
        p.setLayout(new GridLayout(10,0));
        JLabel l1= new JLabel("IP");
        l1.setSize(controlD);
        p.add(l1);
        p.add(textIP);
        
        textIP_Br = new JTextField(SettingsConfig.IP_UDP.getHostAddress());
        JLabel l2 = new JLabel("IP Broadcast");
        l2.setSize(controlD);
        textIP_Br.setSize(controlD);
        textIP_Br.addFocusListener(new FocusListener(){

            @Override
            public void focusGained(FocusEvent e)
            {
               // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void focusLost(FocusEvent e)
            {
                JTextField tf=(JTextField)e.getSource();
                try
                {
                    InetAddress  ip_broadcast =InetAddress.getByName(tf.getText());                    
                    InetAddress[] ip_=InetAddress.getAllByName(InetAddress.getLocalHost().getHostAddress());
                    for(InetAddress i:ip_)
                    {
                        boolean f=true;
                        for (int j = 0; j < 4; j++)
                        {
                            f=((ip_broadcast.getAddress()[j]&0xFF)==(i.getAddress()[j]&0xFF) ||(ip_broadcast.getAddress()[j]&0xFF)==255 );
                        }
                        if(f)
                        {
                            SettingsConfig.IP_UDP= ip_broadcast;
                            error.setText("");
                            return;
                        }
                    } 
                   error.setText(" Уточните настройки сети");
                } 
                catch (UnknownHostException ex)
                {
                    error.setText(" IP указан неверно: "+tf.getText());
                }
                isValid=false;
                tf.setText(""+SettingsConfig.IP.getHostAddress());
            }
        });
        p.add(l2);
        p.add(textIP_Br);
        JLabel l3 = new JLabel("Порт");
        l3.setSize(controlD);
        textIP_Br.setSize(controlD);
        port = new JTextField(""+SettingsConfig.PORT_UDP);
        port.addFocusListener(new FocusListener(){

            @Override
            public void focusGained(FocusEvent e)
            {
               
            }

            @Override
            public void focusLost(FocusEvent e)
            {
                isValid=true;
                JTextField tf=(JTextField)e.getSource();                
                try
                {
                    int  port =Integer.parseInt(tf.getText());
                    if( port>4000)
                    {
                        error.setText("");
                        SettingsConfig.PORT_UDP=port;
                    }
                    else
                    {
                       error.setText("Порт указан неверно: "+tf.getText());
                       isValid=false;
                    }
                }
                catch(NumberFormatException exc)
                {                    
                    error.setText("Порт указан неверно: "+tf.getText());
                    isValid=false;
                    tf.setText(""+SettingsConfig.PORT_UDP);
                }
                
            }
        });
        p.add(l3);
        p.add(port);
        
        this.isAutoConnection = new JCheckBox("Подключение при запуске"); 
        this.isAutoConnection.setSelected(SettingsConfig.isConnection);
        this.isAutoConnection.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e)
            {
                JCheckBox ch= (JCheckBox)e.getSource();
                SettingsConfig.isConnection=ch.isSelected();
                textIP.setEditable(!ch.isSelected());
                textIP_Br.setEditable(!ch.isSelected());
                port.setEditable(!ch.isSelected());
            }
        });
        p.add(isAutoConnection);
        p.add(message);
       
        p.add(error);
       


        Object[] options = {btnString1, EXIT};

        //Create the JOptionPane.
        optionPane = new JOptionPane(p,
                                    JOptionPane.INFORMATION_MESSAGE,
                                    JOptionPane.YES_NO_OPTION,
                                    null,
                                    options,
                                    options[0]);

        //Make this dialog display it.
        setContentPane(optionPane);

        //Handle window closing correctly.
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent we) {
                /*
                 * Instead of directly closing the window,
                 * we're going to change the JOptionPane's
                 * value property.
                 */
                    optionPane.setValue(new Integer(
                                        JOptionPane.CLOSED_OPTION));
            }
        });

        //Ensure the text field always gets the first focus.
        addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent ce) {
                textIP.requestFocusInWindow();
            }
        });

        //Register an event handler that puts the text into the option pane.
        textIP.addActionListener(this);

        //Register an event handler that reacts to option pane state changes.
        optionPane.addPropertyChangeListener(this);
        
    }

    

    /** This method reacts to state changes in the option pane. */
    @Override
    public void propertyChange(PropertyChangeEvent e) {
        String prop = e.getPropertyName();
        if (isVisible()
         && (e.getSource() == optionPane)
         && (JOptionPane.VALUE_PROPERTY.equals(prop) ||
             JOptionPane.INPUT_VALUE_PROPERTY.equals(prop))) 
        {
            Object value = optionPane.getValue();
            if (value == JOptionPane.UNINITIALIZED_VALUE || !isValid)
            {
               optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);
               isValid=true;
               return;
            }

            
            setVisible(false);
        }
    }

    /** This method clears the dialog and hides it. */
    public int getValue()
    {
        switch(this.optionPane.getValue().toString())
        {
            case CONNECT: return 0;
            case OK : return 1;
            case EXIT :return -1;
        
        }
        return -1;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
       
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
