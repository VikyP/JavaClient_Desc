/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userControl;

/**
 *
 * @author viky
 */
public class ImageIconURL
{
    public static javax.swing.ImageIcon get(String resImgName)
    { 
        java.net.URL imgUrl =ImageIconURL.class.getResource(resImgName);
        if(imgUrl== null)
           return null;
        return new javax.swing.ImageIcon(imgUrl);
  
    }
    
}
