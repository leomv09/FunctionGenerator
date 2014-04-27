package logic;

public class Utilities 
{
    public static double[] splitAndCast(String s, String delimit) throws NumberFormatException
    {
        String[] temp = s.split(delimit);
        double[] result = new double[temp.length];
        
        for (int i=0; i<temp.length; i++)
        {
            result[i] = Double.parseDouble(temp[i]);
        }
        
        return result;
    }
}