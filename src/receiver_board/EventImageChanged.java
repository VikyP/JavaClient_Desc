/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package receiver_board;

import java.io.DataInputStream;
import java.util.ArrayList;

/**
 *
 * @author Viky
 */

interface IImageChanged
{
    void getNewImage(DataInputStream dis, byte typeImage);
}

public class EventImageChanged
{
    private  ArrayList listeners = new ArrayList();
    public EventImageChanged(){}
     
    public void ImageChangedAdd (IImageChanged l)
    {
        
        if (!listeners.contains(l))
            listeners.add (l);
        
    }

    // метод удаляющий из очереди подписчиков объект-слушатель
    public void ImageChangedRemove (IImageChanged l)
    {
        if (listeners.contains(l))
            listeners.remove (l);
    }
    public Object  getListener()
    {
        return listeners.iterator().next();
    
    }
    
}
