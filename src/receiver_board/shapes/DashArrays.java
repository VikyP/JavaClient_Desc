/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package receiver_board.shapes;

import java.awt.BasicStroke;

/**
 *
 * @author viky
 */
public class DashArrays
{
   final static float dash1[] = {10.0f };
   final static float dash2[] = {20.0f, 10.0f, 5.0f, 10.0f};
   final static float dash3[] = {20.0f, 10.0f, 5.0f, 10.0f, 5.0f, 10.0f};
   final static float dash4[] = { 3.0f, 10f};
   
   final static float dash[][]={null,dash1,dash2, dash3,dash4};
   
   public static BasicStroke getStrokeLine(float f, int index)
   {
     
     index= (index<0)?0:index;
     if(index==0)
         return new BasicStroke(f);
     else
       return
        new BasicStroke(f,
                        BasicStroke.CAP_ROUND,
                        BasicStroke.CAP_ROUND,
                        20.0f, dash[index], 0.0f);
   
   }
   
}
