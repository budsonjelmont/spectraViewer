// helper class for calculating tick sizes for the axes

import java.text.NumberFormat;
import java.util.Vector;

public class graphfunk
{

    graphfunk()
    {
    }

    public static Vector computeTicks(double ticMinVal, double ticMaxVal, int maxTicks)
    {
    	// computes number of ticks based on number of ticks required between two intervals
        double xStep = roundUp((ticMaxVal - ticMinVal) / (double)maxTicks);
        int numfracdigits = numFracDigits(xStep);
        double xStart = xStep * Math.ceil(ticMinVal / xStep);
        Vector labels = new Vector();
        for(double xpos = xStart; xpos <= ticMaxVal; xpos += xStep)
            labels.addElement(formatNum(xpos, numfracdigits));

        return labels;
    }

    public static double roundUp(double val)
    {
    	// rounds double values
        int exponent = (int)Math.floor(vmsdb.log((float)val) / vmsdb.log(10F));
        val *= Math.pow(10D, -exponent);
        if(val > 5D)
            val = 10D;
        else
        if(val > 2D)
            val = 5D;
        else
        if(val > 1.0D)
            val = 2D;
        val *= Math.pow(10D, exponent);
        return val;
    }

    public static int numFracDigits(double num)
    {
    	// helper function for returning a specific number of digits
        int numdigits;
        for(numdigits = 0; numdigits <= 15 && num != Math.floor(num); numdigits++)
            num *= 10D;

        return numdigits;
    }

    public static String formatNum(double num, int numfracdigits)
    {
    	// formats Double into a String based on the number of digits required
        if(numberFormat == null)
        {
            numberFormat = NumberFormat.getInstance();
            numberFormat.setGroupingUsed(false);
        }
        numberFormat.setMinimumFractionDigits(numfracdigits);
        numberFormat.setMaximumFractionDigits(numfracdigits);
        return numberFormat.format(num);
    }

    public static NumberFormat numberFormat = null;

}
