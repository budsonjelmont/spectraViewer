// class responsible for drawing the actual mass spec peaks

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class mspalette
{

    mspalette(vmsdb vmain1, int x1, int y1, int xsize1, int ysize1, experiment exp2)
    {
    	// constructor
        levels_ = 3;
        yoff_ = 20;
        xoff_ = 30;
        tick_ = 8;
        ytick_ = 4;
        current_selection_ = 0;
        temphighlight_ = 0;
        vmsdb_ = vmain1;
        x_ = x1;
        y_ = y1;
        xsize_ = xsize1;
        ysize_ = ysize1;
        exp_ = exp2;
        lmin_ = new double[levels_];
        lmax_ = new double[levels_];
        levely_ = new double[levels_];
        basey_ = new double[levels_ + 1];
        basey_[0] = 0.0D;
        ms_ = exp_.p1_;
    }

    public void setSpectra(Peak ms2[])
    {
    	// sets raw peak data and computes some basic characteristics of the spectra
        ms_ = ms2;
        findBaseY();
        findAMax();
        findMZMax();
        findLevels();
        findYMax();
        convertXY();
    }

    public void setYLevels(int temp)
    {
    	// computes variables required for the number of axes to represent the spectra
        levels_ = temp;
        lmin_ = new double[levels_];
        lmax_ = new double[levels_];
        levely_ = new double[levels_];
        basey_ = new double[levels_ + 1];
        basey_[0] = 0.0D;
        findBaseY();
        findAMax();
        findMZMax();
        findLevels();
        findYMax();
        convertXY();
    }

    public int getYLevels()
    {
    	// returns number of Axes from graph
        return levels_;
    }

    public void setXY(int x1, int y1)
    {
    	// sets width and height
        x_ = x1;
        y_ = y1;
        findBaseY();
        findAMax();
        findMZMax();
        findLevels();
        findYMax();
        convertXY();
    }

    public void setXsize(int x1)
    {
    	// sets width
        xsize_ = x1;
        findBaseY();
        findAMax();
        findMZMax();
        findLevels();
        findYMax();
        convertXY();
    }

    public void setYsize(int y1)
    {
    	// sets height
        ysize_ = y1;
        findBaseY();
        findAMax();
        findMZMax();
        findLevels();
        findYMax();
        convertXY();
    }

    public void draw()
    {
    	// called every frame
        vmsdb_.stroke(0);
        vmsdb_.fill(255);
        vmsdb_.rect(x_, y_, xsize_, ysize_);
        vmsdb_.strokeWeight(1.0F);
        vmsdb_.stroke(0);
        axes();
        spectra();
        if(!vmsdb_.userinterface_.on_ && !vmsdb_.select_release_ && exp_.changePhos() < 0)
            select();
    }

    public void findBaseY()
    {
    	// Calculates max peak for each level of graph
        double temp = (ysize_ - yoff_) / levels_;
        for(int i = 1; i <= levels_; i++)
            basey_[i] = y_ + i * (int)temp;

    }

    public void findAMax()
    {
    	// finds max peak
        double max = 0.0D;
        for(int i = 0; i < ms_.length; i++)
            if(max < ms_[i].ycor_)
                max = ms_[i].ycor_;

        if(max - 100D < 0.0D)
            abundmax_ = (float)(max - 100D);
        else
            abundmax_ = (float)max;
    }

    public void findMZMax()
    {
    	// finds max mz
        mzmin_ = ms_[0].xcor_;
        mzmax_ = ms_[ms_.length - 1].xcor_;
    }

    public void findLevels()
    {
    	// computes localmin and localmax of each level of peaks
        for(int i = 1; i <= levels_; i++)
        {
            int localmin = (int)(((mzmax_ - mzmin_) / (double)levels_) * (double)(i - 1) + mzmin_);
            int localmax = (int)(((mzmax_ - mzmin_) / (double)levels_) * (double)i + mzmin_);
            lmin_[i - 1] = localmin;
            lmax_[i - 1] = localmax;
        }

    }

    public void convertXY()
    {
    	// converts screen x and y positions for all peaks
        double temp = (ysize_ - yoff_) / levels_;
        for(int i = 1; i <= levels_; i++)
        {
            double localdiff = lmax_[i - 1] - lmin_[i - 1];
            for(int k = 0; k < ms_.length; k++)
            {
                double g1 = ms_[k].xcor_;
                double ymin = 0.0D;
                double ymax = levely_[i - 1];
                if(g1 <= lmax_[i - 1] && g1 >= lmin_[i - 1])
                {
                    double gdiff = g1 - lmin_[i - 1];
                    double glen = (gdiff / localdiff) * (double)(xsize_ - 2 * xoff_);
                    double g2 = ((ms_[k].ycor_ / abundmax_) * 100D) / ymax;
                    double sdiff = g2 - ymin;
                    double sydiff2 = ((double)y_ + (double)i * temp) - ((double)y_ + (double)(i - 1) * temp) - (double)yoff_;
                    double slen = sdiff * sydiff2;
                    int z1 = y_ + (int)((double)i * temp - slen);
                    ms_[k].xwin_ = x_ + xoff_ + (int)glen;
                    ms_[k].ywin_ = z1;
                }
            }

        }

    }

    public void findYMax()
    {
    	// helper function for finding local max of peaks per level
        for(int j = 1; j <= levels_; j++)
        {
            double max = 0.0D;
            for(int i = 0; i < ms_.length; i++)
            {
                double xtemp = ms_[i].xcor_;
                if(xtemp < lmax_[j - 1] && xtemp >= lmin_[j - 1])
                {
                    double ytemp = ms_[i].ycor_;
                    if(max < ytemp)
                        max = ytemp;
                }
            }

            levely_[j - 1] = (max / abundmax_) * 100D;
        }

    }

    public void spectra()
    {
    	// draws peaks to screen
        vmsdb_.textFont(vmsdb_.impact_, 16F);
        double temp = (ysize_ - yoff_) / levels_;
        for(int i = 1; i <= levels_; i++)
        {
            for(int k = ms_.length - 1; k >= 0; k--)
            {
                double g1 = ms_[k].xcor_;
         //       double ymax = levely_[i - 1]; 	//never read
                if(g1 <= lmax_[i - 1] && g1 >= lmin_[i - 1])
                {
                    vmsdb_.stroke(0);
                    int z2 = y_ + (int)((double)i * temp);
                    double thres1 = ((double)vmsdb_.thresholdValue_ / 100D) * abundmax_;
                    if(ms_[k].theorym_ != null && ms_[k].ycor_ > thres1)
                    {
                        int theory_select = -1;
                        for(int j = 0; j < ms_[k].theorym_.length; j++)
                            if(ms_[k].theorym_[j].selected_)
                                theory_select = j;

                        boolean sel = false;
                        if(theory_select == -1)
                        {
                            sel = true;
                            theory_select = 0;
                        }
                        String s = ms_[k].theorym_[theory_select].ionType_;
                        String annot = ms_[k].annot_;
                        String modtemp = ms_[k].theorym_[theory_select].minus_;
                        int charge = ms_[k].theorym_[theory_select].charge_;
                        Pattern lossH2O = Pattern.compile("H2O");
                        Pattern lossNH3 = Pattern.compile("NH3");
                        Pattern lossH3PO4 = Pattern.compile("H3PO4");
                        Pattern lossHPO3 = Pattern.compile("HPO3");
                        Matcher mH2O = lossH2O.matcher(modtemp);
                        Matcher mNH3 = lossNH3.matcher(modtemp);
                        Matcher mH3PO4 = lossH3PO4.matcher(modtemp);
                        Matcher mHPO3 = lossHPO3.matcher(modtemp);
                        
                        // draws spectra and highlights based on settings of the user interface
                        if(temphighlight_ == 0 && !sel)
                        {
                            if(vmsdb_.userinterface_.userselect() && annot != null)
                            {
                                vmsdb_.stroke(139F, 90F, 0.0F);
                                vmsdb_.fill(139F, 90F, 0.0F);
                                vmsdb_.text(annot, (int)ms_[k].xwin_, (int)ms_[k].ywin_);
                            }
                            if(vmsdb_.userinterface_.b1select() && annot == null)
                                if(s.charAt(0) == 'b' && modtemp == "" && charge == 1)
                                {
                                    vmsdb_.stroke(0.0F, 0.0F, 255F);
                                    vmsdb_.fill(0.0F, 0.0F, 255F);
                                    vmsdb_.text((new StringBuilder(String.valueOf(s))).append("+").append(charge).toString(), (int)ms_[k].xwin_, (int)ms_[k].ywin_);
                                } else
                                if(s.charAt(0) == 'b' && modtemp != "" && charge == 1)
                                {
                                    vmsdb_.stroke(80);
                                    vmsdb_.fill(80);
                                    if(vmsdb_.userinterface_.h2oselect() && mH2O.find(0))
                                        vmsdb_.text((new StringBuilder(String.valueOf(s))).append("+").append(vmsdb.str(charge)).append(modtemp).toString(), (int)ms_[k].xwin_, (int)ms_[k].ywin_);
                                    if(vmsdb_.userinterface_.hpo3select() && mHPO3.find(0))
                                        vmsdb_.text((new StringBuilder(String.valueOf(s))).append("+").append(vmsdb.str(charge)).append(modtemp).toString(), (int)ms_[k].xwin_, (int)ms_[k].ywin_);
                                    if(vmsdb_.userinterface_.h3po4select() && mH3PO4.find(0))
                                        vmsdb_.text((new StringBuilder(String.valueOf(s))).append("+").append(vmsdb.str(charge)).append(modtemp).toString(), (int)ms_[k].xwin_, (int)ms_[k].ywin_);
                                    if(vmsdb_.userinterface_.nh3select() && mNH3.find(0))
                                        vmsdb_.text((new StringBuilder(String.valueOf(s))).append("+").append(vmsdb.str(charge)).append(modtemp).toString(), (int)ms_[k].xwin_, (int)ms_[k].ywin_);
                                }
                            if(vmsdb_.userinterface_.b2select() && annot == null)
                                if(s.charAt(0) == 'b' && modtemp == "" && charge == 2)
                                {
                                    vmsdb_.stroke(0.0F, 0.0F, 255F);
                                    vmsdb_.fill(0.0F, 0.0F, 255F);
                                    vmsdb_.text((new StringBuilder(String.valueOf(s))).append("+").append(charge).toString(), (int)ms_[k].xwin_, (int)ms_[k].ywin_);
                                } else
                                if(s.charAt(0) == 'b' && modtemp != "" && charge == 2)
                                {
                                    vmsdb_.stroke(80);
                                    vmsdb_.fill(80);
                                    if(vmsdb_.userinterface_.h2oselect() && mH2O.find(0))
                                        vmsdb_.text((new StringBuilder(String.valueOf(s))).append("+").append(vmsdb.str(charge)).append(modtemp).toString(), (int)ms_[k].xwin_, (int)ms_[k].ywin_);
                                    if(vmsdb_.userinterface_.hpo3select() && mHPO3.find(0))
                                        vmsdb_.text((new StringBuilder(String.valueOf(s))).append("+").append(vmsdb.str(charge)).append(modtemp).toString(), (int)ms_[k].xwin_, (int)ms_[k].ywin_);
                                    if(vmsdb_.userinterface_.h3po4select() && mH3PO4.find(0))
                                        vmsdb_.text((new StringBuilder(String.valueOf(s))).append("+").append(vmsdb.str(charge)).append(modtemp).toString(), (int)ms_[k].xwin_, (int)ms_[k].ywin_);
                                    if(vmsdb_.userinterface_.nh3select() && mNH3.find(0))
                                        vmsdb_.text((new StringBuilder(String.valueOf(s))).append("+").append(vmsdb.str(charge)).append(modtemp).toString(), (int)ms_[k].xwin_, (int)ms_[k].ywin_);
                                }
                            if(vmsdb_.userinterface_.b3select() && annot == null)
                                if(s.charAt(0) == 'b' && modtemp == "" && charge == 3)
                                {
                                    vmsdb_.stroke(0.0F, 0.0F, 255F);
                                    vmsdb_.fill(0.0F, 0.0F, 255F);
                                    vmsdb_.text((new StringBuilder(String.valueOf(s))).append("+").append(charge).toString(), (int)ms_[k].xwin_, (int)ms_[k].ywin_);
                                } else
                                if(s.charAt(0) == 'b' && modtemp != "" && charge == 3)
                                {
                                    vmsdb_.stroke(80);
                                    vmsdb_.fill(80);
                                    if(vmsdb_.userinterface_.h2oselect() && mH2O.find(0))
                                        vmsdb_.text((new StringBuilder(String.valueOf(s))).append("+").append(vmsdb.str(charge)).append(modtemp).toString(), (int)ms_[k].xwin_, (int)ms_[k].ywin_);
                                    if(vmsdb_.userinterface_.hpo3select() && mHPO3.find(0))
                                        vmsdb_.text((new StringBuilder(String.valueOf(s))).append("+").append(vmsdb.str(charge)).append(modtemp).toString(), (int)ms_[k].xwin_, (int)ms_[k].ywin_);
                                    if(vmsdb_.userinterface_.h3po4select() && mH3PO4.find(0))
                                        vmsdb_.text((new StringBuilder(String.valueOf(s))).append("+").append(vmsdb.str(charge)).append(modtemp).toString(), (int)ms_[k].xwin_, (int)ms_[k].ywin_);
                                    if(vmsdb_.userinterface_.nh3select() && mNH3.find(0))
                                        vmsdb_.text((new StringBuilder(String.valueOf(s))).append("+").append(vmsdb.str(charge)).append(modtemp).toString(), (int)ms_[k].xwin_, (int)ms_[k].ywin_);
                                }
                            if(vmsdb_.userinterface_.y1select() && annot == null)
                                if(s.charAt(0) == 'y' && modtemp == "" && charge == 1)
                                {
                                    vmsdb_.stroke(255F, 0.0F, 0.0F);
                                    vmsdb_.fill(255F, 0.0F, 0.0F);
                                    vmsdb_.text((new StringBuilder(String.valueOf(s))).append("+").append(charge).toString(), (int)ms_[k].xwin_, (int)ms_[k].ywin_);
                                } else
                                if(s.charAt(0) == 'y' && modtemp != "" && charge == 1)
                                {
                                    vmsdb_.stroke(80);
                                    vmsdb_.fill(80);
                                    if(vmsdb_.userinterface_.h2oselect() && mH2O.find(0))
                                        vmsdb_.text((new StringBuilder(String.valueOf(s))).append("+").append(vmsdb.str(charge)).append(modtemp).toString(), (int)ms_[k].xwin_, (int)ms_[k].ywin_);
                                    if(vmsdb_.userinterface_.hpo3select() && mHPO3.find(0))
                                        vmsdb_.text((new StringBuilder(String.valueOf(s))).append("+").append(vmsdb.str(charge)).append(modtemp).toString(), (int)ms_[k].xwin_, (int)ms_[k].ywin_);
                                    if(vmsdb_.userinterface_.h3po4select() && mH3PO4.find(0))
                                        vmsdb_.text((new StringBuilder(String.valueOf(s))).append("+").append(vmsdb.str(charge)).append(modtemp).toString(), (int)ms_[k].xwin_, (int)ms_[k].ywin_);
                                    if(vmsdb_.userinterface_.nh3select() && mNH3.find(0))
                                        vmsdb_.text((new StringBuilder(String.valueOf(s))).append("+").append(vmsdb.str(charge)).append(modtemp).toString(), (int)ms_[k].xwin_, (int)ms_[k].ywin_);
                                }
                            if(vmsdb_.userinterface_.y2select() && annot == null)
                                if(s.charAt(0) == 'y' && modtemp == "" && charge == 2)
                                {
                                    vmsdb_.stroke(255F, 0.0F, 0.0F);
                                    vmsdb_.fill(255F, 0.0F, 0.0F);
                                    vmsdb_.text((new StringBuilder(String.valueOf(s))).append("+").append(charge).toString(), (int)ms_[k].xwin_, (int)ms_[k].ywin_);
                                } else
                                if(s.charAt(0) == 'y' && modtemp != "" && charge == 2)
                                {
                                    vmsdb_.stroke(80);
                                    vmsdb_.fill(80);
                                    if(vmsdb_.userinterface_.h2oselect() && mH2O.find(0))
                                        vmsdb_.text((new StringBuilder(String.valueOf(s))).append("+").append(vmsdb.str(charge)).append(modtemp).toString(), (int)ms_[k].xwin_, (int)ms_[k].ywin_);
                                    if(vmsdb_.userinterface_.hpo3select() && mHPO3.find(0))
                                        vmsdb_.text((new StringBuilder(String.valueOf(s))).append("+").append(vmsdb.str(charge)).append(modtemp).toString(), (int)ms_[k].xwin_, (int)ms_[k].ywin_);
                                    if(vmsdb_.userinterface_.h3po4select() && mH3PO4.find(0))
                                        vmsdb_.text((new StringBuilder(String.valueOf(s))).append("+").append(vmsdb.str(charge)).append(modtemp).toString(), (int)ms_[k].xwin_, (int)ms_[k].ywin_);
                                    if(vmsdb_.userinterface_.nh3select() && mNH3.find(0))
                                        vmsdb_.text((new StringBuilder(String.valueOf(s))).append("+").append(vmsdb.str(charge)).append(modtemp).toString(), (int)ms_[k].xwin_, (int)ms_[k].ywin_);
                                }
                            if(vmsdb_.userinterface_.y3select() && annot == null)
                                if(s.charAt(0) == 'y' && modtemp == "" && charge == 3)
                                {
                                    vmsdb_.stroke(255F, 0.0F, 0.0F);
                                    vmsdb_.fill(255F, 0.0F, 0.0F);
                                    vmsdb_.text((new StringBuilder(String.valueOf(s))).append("+").append(charge).toString(), (int)ms_[k].xwin_, (int)ms_[k].ywin_);
                                } else
                                if(s.charAt(0) == 'y' && modtemp != "" && charge == 3)
                                {
                                    vmsdb_.stroke(80);
                                    vmsdb_.fill(80);
                                    if(vmsdb_.userinterface_.h2oselect() && mH2O.find(0))
                                        vmsdb_.text((new StringBuilder(String.valueOf(s))).append("+").append(vmsdb.str(charge)).append(modtemp).toString(), (int)ms_[k].xwin_, (int)ms_[k].ywin_);
                                    if(vmsdb_.userinterface_.hpo3select() && mHPO3.find(0))
                                        vmsdb_.text((new StringBuilder(String.valueOf(s))).append("+").append(vmsdb.str(charge)).append(modtemp).toString(), (int)ms_[k].xwin_, (int)ms_[k].ywin_);
                                    if(vmsdb_.userinterface_.h3po4select() && mH3PO4.find(0))
                                        vmsdb_.text((new StringBuilder(String.valueOf(s))).append("+").append(vmsdb.str(charge)).append(modtemp).toString(), (int)ms_[k].xwin_, (int)ms_[k].ywin_);
                                    if(vmsdb_.userinterface_.nh3select() && mNH3.find(0))
                                        vmsdb_.text((new StringBuilder(String.valueOf(s))).append("+").append(vmsdb.str(charge)).append(modtemp).toString(), (int)ms_[k].xwin_, (int)ms_[k].ywin_);
                                }
                            if(vmsdb_.userinterface_.aselect() && annot == null)
                                if(s.charAt(0) == 'a' && modtemp == "")
                                {
                                    vmsdb_.stroke(80);
                                    vmsdb_.fill(80);
                                    vmsdb_.text((new StringBuilder(String.valueOf(s))).append("+").append(vmsdb.str(charge)).toString(), (int)ms_[k].xwin_, (int)ms_[k].ywin_);
                                } else
                                if(s.charAt(0) == 'a' && modtemp != "")
                                {
                                    vmsdb_.stroke(80);
                                    vmsdb_.fill(80);
                                    if(vmsdb_.userinterface_.h2oselect() && mH2O.find(0))
                                        vmsdb_.text((new StringBuilder(String.valueOf(s))).append("+").append(vmsdb.str(charge)).append(modtemp).toString(), (int)ms_[k].xwin_, (int)ms_[k].ywin_);
                                    if(vmsdb_.userinterface_.hpo3select() && mHPO3.find(0))
                                        vmsdb_.text((new StringBuilder(String.valueOf(s))).append("+").append(vmsdb.str(charge)).append(modtemp).toString(), (int)ms_[k].xwin_, (int)ms_[k].ywin_);
                                    if(vmsdb_.userinterface_.h3po4select() && mH3PO4.find(0))
                                        vmsdb_.text((new StringBuilder(String.valueOf(s))).append("+").append(vmsdb.str(charge)).append(modtemp).toString(), (int)ms_[k].xwin_, (int)ms_[k].ywin_);
                                    if(vmsdb_.userinterface_.nh3select() && mNH3.find(0))
                                        vmsdb_.text((new StringBuilder(String.valueOf(s))).append("+").append(vmsdb.str(charge)).append(modtemp).toString(), (int)ms_[k].xwin_, (int)ms_[k].ywin_);
                                }
                            if(vmsdb_.userinterface_.cselect() && annot == null)
                                if(s.charAt(0) == 'c' && modtemp == "")
                                {
                                    vmsdb_.stroke(80);
                                    vmsdb_.fill(80);
                                    vmsdb_.text((new StringBuilder(String.valueOf(s))).append("+").append(vmsdb.str(charge)).toString(), (int)ms_[k].xwin_, (int)ms_[k].ywin_);
                                } else
                                if(s.charAt(0) == 'c' && modtemp != "")
                                {
                                    vmsdb_.stroke(80);
                                    vmsdb_.fill(80);
                                    if(vmsdb_.userinterface_.h2oselect() && mH2O.find(0))
                                        vmsdb_.text((new StringBuilder(String.valueOf(s))).append("+").append(vmsdb.str(charge)).append(modtemp).toString(), (int)ms_[k].xwin_, (int)ms_[k].ywin_);
                                    if(vmsdb_.userinterface_.hpo3select() && mHPO3.find(0))
                                        vmsdb_.text((new StringBuilder(String.valueOf(s))).append("+").append(vmsdb.str(charge)).append(modtemp).toString(), (int)ms_[k].xwin_, (int)ms_[k].ywin_);
                                    if(vmsdb_.userinterface_.h3po4select() && mH3PO4.find(0))
                                        vmsdb_.text((new StringBuilder(String.valueOf(s))).append("+").append(vmsdb.str(charge)).append(modtemp).toString(), (int)ms_[k].xwin_, (int)ms_[k].ywin_);
                                    if(vmsdb_.userinterface_.nh3select() && mNH3.find(0))
                                        vmsdb_.text((new StringBuilder(String.valueOf(s))).append("+").append(vmsdb.str(charge)).append(modtemp).toString(), (int)ms_[k].xwin_, (int)ms_[k].ywin_);
                                }
                            if(vmsdb_.userinterface_.xselect() && annot == null)
                                if(s.charAt(0) == 'x' && modtemp == "")
                                {
                                    vmsdb_.stroke(80);
                                    vmsdb_.fill(80);
                                    vmsdb_.text((new StringBuilder(String.valueOf(s))).append("+").append(vmsdb.str(charge)).toString(), (int)ms_[k].xwin_, (int)ms_[k].ywin_);
                                } else
                                if(s.charAt(0) == 'x' && modtemp != "")
                                {
                                    vmsdb_.stroke(80);
                                    vmsdb_.fill(80);
                                    if(vmsdb_.userinterface_.h2oselect() && mH2O.find(0))
                                        vmsdb_.text((new StringBuilder(String.valueOf(s))).append("+").append(vmsdb.str(charge)).append(modtemp).toString(), (int)ms_[k].xwin_, (int)ms_[k].ywin_);
                                    if(vmsdb_.userinterface_.hpo3select() && mHPO3.find(0))
                                        vmsdb_.text((new StringBuilder(String.valueOf(s))).append("+").append(vmsdb.str(charge)).append(modtemp).toString(), (int)ms_[k].xwin_, (int)ms_[k].ywin_);
                                    if(vmsdb_.userinterface_.h3po4select() && mH3PO4.find(0))
                                        vmsdb_.text((new StringBuilder(String.valueOf(s))).append("+").append(vmsdb.str(charge)).append(modtemp).toString(), (int)ms_[k].xwin_, (int)ms_[k].ywin_);
                                    if(vmsdb_.userinterface_.nh3select() && mNH3.find(0))
                                        vmsdb_.text((new StringBuilder(String.valueOf(s))).append("+").append(vmsdb.str(charge)).append(modtemp).toString(), (int)ms_[k].xwin_, (int)ms_[k].ywin_);
                                }
                            if(vmsdb_.userinterface_.zselect() && annot == null)
                                if(s.charAt(0) == 'z' && modtemp == "")
                                {
                                    vmsdb_.stroke(80);
                                    vmsdb_.fill(80);
                                    vmsdb_.text((new StringBuilder(String.valueOf(s))).append("+").append(vmsdb.str(charge)).toString(), (int)ms_[k].xwin_, (int)ms_[k].ywin_);
                                } else
                                if(s.charAt(0) == 'z' && modtemp != "")
                                {
                                    vmsdb_.stroke(80);
                                    vmsdb_.fill(80);
                                    if(vmsdb_.userinterface_.h2oselect() && mH2O.find(0))
                                        vmsdb_.text((new StringBuilder(String.valueOf(s))).append("+").append(vmsdb.str(charge)).append(modtemp).toString(), (int)ms_[k].xwin_, (int)ms_[k].ywin_);
                                    if(vmsdb_.userinterface_.hpo3select() && mHPO3.find(0))
                                        vmsdb_.text((new StringBuilder(String.valueOf(s))).append("+").append(vmsdb.str(charge)).append(modtemp).toString(), (int)ms_[k].xwin_, (int)ms_[k].ywin_);
                                    if(vmsdb_.userinterface_.h3po4select() && mH3PO4.find(0))
                                        vmsdb_.text((new StringBuilder(String.valueOf(s))).append("+").append(vmsdb.str(charge)).append(modtemp).toString(), (int)ms_[k].xwin_, (int)ms_[k].ywin_);
                                    if(vmsdb_.userinterface_.nh3select() && mNH3.find(0))
                                        vmsdb_.text((new StringBuilder(String.valueOf(s))).append("+").append(vmsdb.str(charge)).append(modtemp).toString(), (int)ms_[k].xwin_, (int)ms_[k].ywin_);
                                }
                            if(vmsdb_.userinterface_.mselect() && annot == null)
                                if(s.charAt(0) == 'M' && modtemp == "")
                                {
                                    vmsdb_.stroke(102F, 0.0F, 255F);
                                    vmsdb_.fill(102F, 0.0F, 255F);
                                    vmsdb_.text((new StringBuilder(String.valueOf(s))).append("+").append(vmsdb.str(charge)).append(modtemp).toString(), (int)ms_[k].xwin_, (int)ms_[k].ywin_);
                                } else
                                if(s.charAt(0) == 'M' && modtemp != "")
                                {
                                    vmsdb_.stroke(80);
                                    vmsdb_.fill(80);
                                    if(vmsdb_.userinterface_.h2oselect() && mH2O.find(0))
                                        vmsdb_.text((new StringBuilder(String.valueOf(s))).append("+").append(vmsdb.str(charge)).append(modtemp).toString(), (int)ms_[k].xwin_, (int)ms_[k].ywin_);
                                    if(vmsdb_.userinterface_.hpo3select() && mHPO3.find(0))
                                        vmsdb_.text((new StringBuilder(String.valueOf(s))).append("+").append(vmsdb.str(charge)).append(modtemp).toString(), (int)ms_[k].xwin_, (int)ms_[k].ywin_);
                                    if(vmsdb_.userinterface_.h3po4select() && mH3PO4.find(0))
                                        vmsdb_.text((new StringBuilder(String.valueOf(s))).append("+").append(vmsdb.str(charge)).append(modtemp).toString(), (int)ms_[k].xwin_, (int)ms_[k].ywin_);
                                    if(vmsdb_.userinterface_.nh3select() && mNH3.find(0))
                                        vmsdb_.text((new StringBuilder(String.valueOf(s))).append("+").append(vmsdb.str(charge)).append(modtemp).toString(), (int)ms_[k].xwin_, (int)ms_[k].ywin_);
                                }
                        }
                    }
                    // if above the selected threshold
                    else if(ms_[k].theorym_ == null && ms_[k].ycor_ > thres1)
                    {
                        String annot = ms_[k].annot_;
                        if(vmsdb_.userinterface_.userselect() && annot != null)
                        {
                            vmsdb_.stroke(139F, 90F, 0.0F);
                            vmsdb_.fill(139F, 90F, 0.0F);
                            vmsdb_.text(annot, (int)ms_[k].xwin_, (int)ms_[k].ywin_);
                        }
                    }
                    vmsdb_.line((int)ms_[k].xwin_, (int)ms_[k].ywin_, (int)ms_[k].xwin_, z2);
                }
            }

        }
        // highlight amino acid if something is selected
        if(temphighlight_ != 0 && exp_.changePhos() < 0)
            highlightaa();
    }

    public void highlightaa()
    {
    	// if amino acid is highlighted
        vmsdb_.textFont(vmsdb_.arial2_, 13F);
        double temp = (ysize_ - yoff_) / levels_;
        for(int i = 1; i <= levels_; i++)
        {
          //  double localdiff = lmax_[i - 1] - lmin_[i - 1];	//never read
            for(int k = ms_.length - 1; k >= 0; k--)
            {
                double g1 = ms_[k].xcor_;
                if(g1 <= lmax_[i - 1] && g1 >= lmin_[i - 1])
                {
                    vmsdb_.stroke(0);
                    double thres2 = ((double)vmsdb_.thresholdValue_ / 100D) * abundmax_;
                    if(ms_[k].theorym_ != null && ms_[k].ycor_ > thres2)
                    {
                        int theory_select = 0;
                        for(int j = 0; j < ms_[k].theorym_.length; j++)
                            if(ms_[k].theorym_[j].selected_)
                                theory_select = j;

                        String s = ms_[k].theorym_[theory_select].ionType_;
                        int charge = ms_[k].theorym_[theory_select].charge_;
            //            double mztemp = ms_[k].theorym_[theory_select].values_;		//never read
                        String modtemp = ms_[k].theorym_[theory_select].minus_;
                        int aapos = ms_[k].theorym_[theory_select].aapos_;
                        int aareverse = (vmsdb_.calculate_.peptide_.length - aapos) + 1;
                        
                        // changes color of spectra and selection box based on the type of theoretical peak i.e. a,b,c,x,y,z, 
                        if(temphighlight_ == aapos)
                        {
                            if(s.charAt(0) == 'a' || s.charAt(0) == 'b' || s.charAt(0) == 'c' || modtemp.equals("b"))
                            {
                                if(s.charAt(0) == 'b' && modtemp == "")
                                {
                                    vmsdb_.stroke(0.0F, 0.0F, 255F);
                                    vmsdb_.fill(0.0F, 0.0F, 255F);
                                } else
                                if(s.charAt(0) == 'M')
                                {
                                    vmsdb_.stroke(51F, 0.0F, 204F);
                                    vmsdb_.fill(51F, 0.0F, 204F);
                                    modtemp = "";
                                    s = "M";
                                } else
                                {
                                    vmsdb_.stroke(0);
                                    vmsdb_.fill(0);
                                }
                                if(modtemp == "")
                                    vmsdb_.text((new StringBuilder(String.valueOf(s))).append("+").append(vmsdb.str(charge)).toString(), (int)ms_[k].xwin_, (int)ms_[k].ywin_);
                                else
                                    vmsdb_.text((new StringBuilder(String.valueOf(s))).append("+").append(vmsdb.str(charge)).append(modtemp).toString(), (int)ms_[k].xwin_, (int)ms_[k].ywin_);
                                int z2 = (y_ + i * (int)temp) - (int)ms_[k].ywin_;
                                vmsdb_.rect((float)ms_[k].xwin_, (float)ms_[k].ywin_, 3F, z2);
                            }
                        } else
                        if(temphighlight_ == aareverse && (s.charAt(0) == 'x' || s.charAt(0) == 'y' || s.charAt(0) == 'z' || modtemp.equals("y")))
                        {
                            if(s.charAt(0) == 'y' && modtemp == "")
                            {
                                vmsdb_.stroke(255F, 0.0F, 0.0F);
                                vmsdb_.fill(255F, 0.0F, 0.0F);
                            } else
                            if(s.charAt(0) == 'M')
                            {
                                vmsdb_.stroke(51F, 0.0F, 204F);
                                vmsdb_.fill(51F, 0.0F, 204F);
                                modtemp = "";
                            } else
                            {
                                vmsdb_.stroke(0);
                                vmsdb_.fill(0);
                            }
                            if(modtemp == "")
                                vmsdb_.text((new StringBuilder(String.valueOf(s))).append("+").append(vmsdb.str(charge)).toString(), (int)ms_[k].xwin_, (int)ms_[k].ywin_);
                            else
                                vmsdb_.text((new StringBuilder(String.valueOf(s))).append("+").append(vmsdb.str(charge)).append(modtemp).toString(), (int)ms_[k].xwin_, (int)ms_[k].ywin_);
                            int z2 = (y_ + i * (int)temp) - (int)ms_[k].ywin_;
                            vmsdb_.rect((float)ms_[k].xwin_, (float)ms_[k].ywin_, 3F, z2);
                        }
                    }
                }
            }

        }

    }

    public void axes()
    {
    	// draws axes to screen
        vmsdb_.stroke(0);
        double temp = (ysize_ - yoff_) / levels_;
        for(int i = 1; i <= levels_; i++)
        {
            vmsdb_.stroke(0);
            vmsdb_.line(x_ + xoff_, y_ + i * (int)temp, (x_ + xsize_) - xoff_, y_ + i * (int)temp);
            vmsdb_.line(x_ + xoff_, y_ + i * (int)temp, x_ + xoff_, y_ + (i - 1) * (int)temp + yoff_);
            Vector tick1 = graphfunk.computeTicks(lmin_[i - 1], lmax_[i - 1], tick_);
            vmsdb_.textFont(vmsdb_.tickFont_);
            double localdiff = lmax_[i - 1] - lmin_[i - 1];
            
            // determines number of tick marks for a specified range within a specified window size
            if(tick1 != null)
            {
            	// draws each tick mark to the screen
                for(int j = 0; j < tick1.size(); j++)
                {
                    int g1 = Integer.parseInt(tick1.get(j).toString());
                    double gdiff = (double)g1 - lmin_[i - 1];
                    double glen = (gdiff / localdiff) * (double)(xsize_ - 2 * xoff_);
                    vmsdb_.stroke(0);
                    vmsdb_.line(x_ + xoff_ + (int)glen, y_ + i * (int)temp + 5, x_ + xoff_ + (int)glen, y_ + i * (int)temp);
                    vmsdb_.fill(0);
                    vmsdb_.text(vmsdb.nfc(g1), (float)(x_ + xoff_ + (int)glen) - vmsdb_.textWidth(vmsdb.str(g1)) / 2.0F, y_ + 2 + i * (int)temp + yoff_ / 2);
                }

            }
            double ymin = 0.0D;
            double ymax = (float)levely_[i - 1];
            double ydiff = ymax - ymin;
            Vector tick2 = graphfunk.computeTicks(ymin, ymax, ytick_);
            vmsdb_.textFont(vmsdb_.tickFont_);
            if(tick2 != null)
            {
                for(int j = 0; j < tick2.size(); j++)
                {
                    double g1 = Double.parseDouble(tick2.get(j).toString());
                    double gdiff = g1 - ymin;
                    double ydiff2 = ((double)y_ + (double)i * temp) - ((double)y_ + (double)(i - 1) * temp) - (double)yoff_;
                    double glen = (gdiff / ydiff) * ydiff2;
                    vmsdb_.stroke(0);
                    int z1 = y_ + (int)((double)i * temp - glen);
                    vmsdb_.line((x_ + xoff_) - 2, z1, x_ + xoff_ + 2, z1);
                    vmsdb_.fill(0);
                    if(g1 < 5D)
                        vmsdb_.text(vmsdb.nfc((float)g1, 1), (float)(x_ + xoff_) - vmsdb_.textWidth(vmsdb.nfc((float)g1, 1)) - 2.0F, z1);
                    else
                        vmsdb_.text((new StringBuilder(String.valueOf(vmsdb.nfc((int)g1)))).append("%").toString(), (float)(x_ + xoff_) - vmsdb_.textWidth((new StringBuilder(String.valueOf(vmsdb.str((int)g1)))).append("%").toString()) - 2.0F, z1);
                }

            }
            
            // draws the threhold line for each level
            int threshold = vmsdb_.getThreshold();
            if((double)threshold < ymax)
            {
                double gdiff = (double)threshold - ymin;
                double ydiff2 = ((double)y_ + (double)i * temp) - ((double)y_ + (double)(i - 1) * temp) - (double)yoff_;
                double glen = (gdiff / ydiff) * ydiff2;
                int z1 = y_ + (int)((double)i * temp - glen);
                vmsdb_.stroke(0, 80F);
                vmsdb_.drawDashedLine(x_ + xoff_, z1, (x_ + xsize_) - xoff_, z1, 5D, 5D, "black");
            }
        }

    }

    boolean isSelected()
    {
    	// if spectra screen is selected
        return vmsdb_.mouseX > x_ && vmsdb_.mouseX < x_ + xsize_ && vmsdb_.mouseY > y_ && vmsdb_.mouseY < y_ + ysize_;
    }

    void select()
    {
    	// function for if a peak is selected
        if(isSelected())
        {
       //     double temp = (ysize_ - yoff_) / levels_;		//never read
            int templevel = 0;
            for(int i = 1; i <= levels_; i++)
            {
                double ymin = basey_[i - 1];
                double ymax = basey_[i];
                if((double)vmsdb_.mouseY <= ymax && (double)vmsdb_.mouseY >= ymin)
                    templevel = i;
            }

            float change[] = new float[ms_.length];
            for(int i = 0; i < ms_.length; i++)
            {
                double xcor = ms_[i].xcor_;
           //     double ycor = ms_[i].ycor_;		//never read
                int t2level = 0;
                for(int j = 1; j <= levels_; j++)
                    if(xcor <= lmax_[j - 1] && xcor >= lmin_[j - 1])
                        t2level = j;

                if(t2level == templevel)
                    change[i] = vmsdb.dist(vmsdb_.mouseX, vmsdb_.mouseY, (float)ms_[i].xwin_, (float)ms_[i].ywin_);
                else
                    change[i] = 1000F;
            }

            float change1[] = vmsdb_.sort(change);
            float temp1 = change1[0];
            String outtemp = "";
            int boxsize = 0;
            int boxwidth = 125;
            int theory_select = 0;
            current_selection_ = 0;
            String mslabel = "";
            vmsdb_.textFont(vmsdb_.font_, 11F);
            for(int i = 0; i < ms_.length; i++)
                if(temp1 == vmsdb.dist(vmsdb_.mouseX, vmsdb_.mouseY, (float)ms_[i].xwin_, (float)ms_[i].ywin_))
                {
                    boolean rightside = true;
                    if((double)vmsdb_.mouseX < ms_[i].xwin_)
                        rightside = false;
                    if(temp1 < 50F)
                    {
                        current_selection_ = i;
                        exp_.setSelection(current_selection_);
                        TheoreticalIon labelannot[] = ms_[i].theorym_;
                        mslabel = ms_[i].annot_;
                        if(labelannot != null && ms_[i].ycor_ > (double)vmsdb_.thresholdValue_ && mslabel == null)
                        {
                            theory_select = 0;
                            for(int j = 0; j < labelannot.length; j++)
                                if(ms_[i].theorym_[j].selected_)
                                    theory_select = j;
                            
                            // retrieves information about each spectra
                            String mod1 = ms_[i].theorym_[theory_select].minus_;
                            int charge1 = ms_[i].theorym_[theory_select].charge_;
                            String label1 = ms_[i].theorym_[theory_select].ionType_;
                            double diff = ms_[i].theorym_[theory_select].massError_;
                            
                            // sets color based on theoretical match
                            if(label1.charAt(0) == 'b' && mod1 == "")
                            {
                                vmsdb_.fill(0.0F, 0.0F, 255F, 80F);
                                vmsdb_.stroke(0.0F, 0.0F, 255F);
                            } else
                            if(label1.charAt(0) == 'y' && mod1 == "")
                            {
                                vmsdb_.stroke(255F, 0.0F, 0.0F);
                                vmsdb_.fill(255F, 0.0F, 0.0F, 80F);
                            } else
                            {
                                vmsdb_.colorChange("highlight", "highlight", 1.0F);
                            }
                            
                            // draws selection box with annotation information 
                            // creates text based on each peak
                            if(label1.charAt(0) == 'M')
                                label1 = "M";
                            if(mod1 == "")
                            {
                                outtemp = (new StringBuilder("Test 1 M/Z: ")).append(vmsdb.nfc((float)ms_[i].xcor_, 3)).append('\n').append("Abundance %: ").append(vmsdb.nfc((float)((ms_[i].ycor_ / abundmax_) * 100D), 2)).append('\n').toString();
                                outtemp = (new StringBuilder(String.valueOf(outtemp))).append("Label: ").append(label1).append('\n').append("Charge: ").append(charge1).append('\n').toString();
                                outtemp = (new StringBuilder(String.valueOf(outtemp))).append("Mass Error: ").append(vmsdb.nfc((float)diff, 3)).append('\n').toString();
                                outtemp = (new StringBuilder(String.valueOf(outtemp))).append("Matches: ").append(labelannot.length).append('\n').toString();
                                boxsize = 125;
                            } else
                            {
                                boxwidth = (int)vmsdb_.textWidth((new StringBuilder("Label: ")).append(label1).append(mod1).toString()) + 5;
                                if(boxwidth < 125)
                                   boxwidth = 125;
                                outtemp = (new StringBuilder("Test 2 M/Z: ")).append(vmsdb.nfc((float)ms_[i].xcor_, 3)).append('\n').append("Abundance %: ").append(vmsdb.nfc((float)((ms_[i].ycor_ / abundmax_) * 100D), 2)).append('\n').toString();
                                outtemp = (new StringBuilder(String.valueOf(outtemp))).append("Label: ").append(label1).append(mod1).append('\n').append("Charge: ").append(charge1).append('\n').toString();
                                outtemp = (new StringBuilder(String.valueOf(outtemp))).append("Mass Error: ").append(vmsdb.nfc((float)diff, 3)).append('\n').toString();
                                outtemp = (new StringBuilder(String.valueOf(outtemp))).append("Matches: ").append(labelannot.length).append('\n').toString();
                                boxsize = 125;
                            }
                        } else
                        	
                        // if ycor is greater than specified threshold
                        if(labelannot != null && ms_[i].ycor_ > (double)vmsdb_.thresholdValue_ && mslabel != null)
                        {
                            vmsdb_.colorChange("highlight", "highlight", 1.0F);
                            outtemp = (new StringBuilder("Test 3 M/Z: ")).append(vmsdb.nfc((float)ms_[i].xcor_, 3)).append('\n').append("Abundance %: ").append(vmsdb.nfc((float)((ms_[i].ycor_ / abundmax_) * 100D), 2)).append('\n').append("User Label: ").append(mslabel).append('\n').toString();
                            boxsize = 60;
                        } else
                        {
                            vmsdb_.colorChange("highlight", "highlight", 1.0F);
                            outtemp = (new StringBuilder("Test 4 M/Z: ")).append(vmsdb.nfc((float)ms_[i].xcor_, 3)).append('\n').append("Abundance %: ").append(vmsdb.nfc((float)((ms_[i].ycor_ / abundmax_) * 100D), 2)).append('\n').toString();
                            boxsize = 40;
                        }
                        
                        // draws box according to which side of the screen and within specified boundaries
                        vmsdb_.line((float)ms_[i].xwin_, (float)ms_[i].ywin_, vmsdb_.mouseX, vmsdb_.mouseY);
                        if(vmsdb_.mouseX + 100 <= x_ + xsize_)
                        {
                            if(rightside)
                            {
                                vmsdb_.rect(vmsdb_.mouseX, vmsdb_.mouseY - 30, boxwidth, boxsize);
                                vmsdb_.colorChange("black", "black", 1.0F);
                                vmsdb_.text(outtemp, vmsdb_.mouseX, vmsdb_.mouseY - 15);
                            } else
                            {
                                vmsdb_.rect(vmsdb_.mouseX - 125, vmsdb_.mouseY - 30, boxwidth, boxsize);
                                vmsdb_.colorChange("black", "black", 1.0F);
                                vmsdb_.text(outtemp, vmsdb_.mouseX - 125, vmsdb_.mouseY - 15);
                            }
                        } else
                        if(rightside)
                        {
                            vmsdb_.rect(vmsdb_.mouseX - 100, vmsdb_.mouseY - 30, boxwidth, boxsize);
                            vmsdb_.colorChange("black", "black", 1.0F);
                            vmsdb_.text(outtemp, vmsdb_.mouseX - 100, vmsdb_.mouseY - 15);
                        } else
                        {
                            vmsdb_.rect(vmsdb_.mouseX - 100, vmsdb_.mouseY - 30, boxwidth, boxsize);
                            vmsdb_.colorChange("black", "black", 1.0F);
                            vmsdb_.text(outtemp, vmsdb_.mouseX - 100, vmsdb_.mouseY - 15);
                        }
                    } else
                    {
                    	// if nothing is selected
                        exp_.setSelection(0);
                    }
                }

        }
    }

    // public variables
    public vmsdb vmsdb_;
    int x_;
    int y_;
    int xsize_;
    int ysize_;
    int levels_;
    int yoff_;
    int xoff_;
    int tick_;
    int ytick_;
    double abundmax_;
    double mzmax_;
    double mzmin_;
    double lmin_[];
    double lmax_[];
    double levely_[];
    int current_selection_;
    Peak ms_[];
    double basey_[];
    int temphighlight_;
    experiment exp_;
    int newx_;
    int newy_;
    String ctermString_;
    String proteinname_;
    String xcorr_;
}
