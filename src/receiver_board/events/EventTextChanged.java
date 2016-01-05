/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package receiver_board.events;

import java.util.ArrayList;

/**
 *
 * @author viky
 */
public class EventTextChanged
{
    private  ArrayList listeners = new ArrayList();
    public EventTextChanged(){}
     
    public void TextChangedAdd (ITextChanged l)
    {
        
        if (!listeners.contains(l))
            listeners.add (l);
        
    }

    // метод удаляющий из очереди подписчиков объект-слушатель
    public void TextChangedRemove (ITextChanged l)
    {
        if (listeners.contains(l))
            listeners.remove (l);
    }
    
    public Object  getListener()
    {
        return listeners.iterator().next();
    
    }
    
}
