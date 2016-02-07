/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package masterPanel;

import java.io.File;
import java.io.FileNotFoundException;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
//import java.time.LocalDateTime;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author viky
 */
public class ReportException
{
   
    public static void write(String exc)
    { 
        String fileName="Report.txt";
        FileWriter FW;
       try
       {
         File file  = new File(fileName);
        if(!file.exists()) 
         {

             file.createNewFile();
         }
        
        FW = new FileWriter (file,true);
        FW.write(exc+"\t\tTime:\t"+ Calendar.getInstance().getTime().toString()+"\r\n");
        FW.close();   
       }
       
       catch (FileNotFoundException ex)
       {
            System.out.println("    " + ex);
       } 
       
       catch (IOException ex)
        {
           System.out.println("    " + ex);
        }
    
   }
}
