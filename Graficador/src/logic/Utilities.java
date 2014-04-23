/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

/**
 *
 * @author Leo
 */
public class Utilities {
    
    public static int getMaxFromArray(int[] array)
    {
        int last = -1;
         for(int i = 0; i < array.length; i++)
         {
             if(array[i] > last)
             {
                 last = array[i];
             }
         }
         return last;
    }
}
