/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student_teamviewer;

import java.util.ArrayList;

/**
 *
 * @author viky
 */

interface IReconnect
{
    public void Reconnect();    
}

public class EventLostConnection
{
    private  ArrayList listeners = new ArrayList();
    public EventLostConnection(){}
     
    public void addEventLostConnection (IReconnect l)
    {        
        if (!listeners.contains(l))
            listeners.add (l);        
    }

    public void removeEventLostConnection (IReconnect l)
    {
        if (listeners.contains(l))
            listeners.remove (l);
    }
    public Object  getListener()
    {
        return listeners.iterator().next();
    
    }
}

