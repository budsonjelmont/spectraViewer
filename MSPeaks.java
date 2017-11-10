// simple data structure to store information about each spectra
// raw peak data, x and y screen position, and corresponding calculated matched theoretical spectra
import java.util.Vector;

public class MSPeaks
{

    MSPeaks(double xcor1, double ycor1)
    {
        theory_ = new Vector();
        theorym_ = null;
        xcor_ = xcor1;
        ycor_ = ycor1;
    }

    public void printTheory()
    {
        if(theorym_ != null)
        {
            for(int i = 0; i < theorym_.length; i++)
            {
                vmsdb.println("theory");
                vmsdb.println(theorym_[i].massError_);
                vmsdb.println(theorym_[i].ionType_);
                vmsdb.println(theorym_[i].charge_);
                vmsdb.println(theorym_[i].minus_);
            }

        }
    }

    double xcor_; //mh value
    double ycor_; //peak magnitude
    double xwin_; // current screen x position
    double ywin_; // current screen y position
    String annot_; // annotation assoctiated with spectra
    String label2_; // label associated with spectra
    String color1_; // color associated with spectra
    int matched_;
    Vector theory_; 
    theoryMS theorym_[]; // Matched theoretical spectra with current experimental spectra
}
