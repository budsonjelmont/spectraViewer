// class for creating boxes full of possible theoretical matches
// creates all the boxes and buttons to create the match matrix

import interfascia.*;

public class Selection
{

    Selection(int ix, int iy, int ixsize, int iysize, vmsdb vmain2, experiment exp1, annotation annot1)
    {
    	// constructor
        modwidth_ = 75;
        sel_num_ = 0;
        isAnnot_ = false;
        isRight = true;
        x_ = ix;
        y_ = iy - 30;
        xsize_ = ixsize;
        ysize_ = iysize;
        vmain1_ = vmain2;
        exp_ = exp1;
        annot_ = annot1;
        
        // currently selected spectra
        select_current_ = exp_.current_selection_;

        if(x_ < (int)exp_.p1_[select_current_].xwin_)
        {
            isRight = false;
            x_ -= xsize_;
        }
        
        // currently selected label
        labelannot_ = exp_.p1_[select_current_].annot_;
        if(labelannot_ != null)
        {
            isAnnot_ = true;


        }
        if(exp_.p1_[select_current_].theorym_ != null)
            modsize_ = exp_.p1_[select_current_].theorym_.length;
        else
            modsize_ = 0;
        specx_ = (int)exp_.p1_[select_current_].xwin_;
        specy_ = (int)exp_.p1_[select_current_].ywin_;
        abundance_ = (float)((exp_.p1_[select_current_].ycor_ / exp_.palette_.abundmax_) * 100D);
        mz_ = (float)exp_.p1_[select_current_].xcor_;
        c_ = new GUIController(vmain1_);
        radio1_ = new IFRadioController("Selector");
        if(isAnnot_)
        {
            radioarray1_ = new IFRadioButton[modsize_ + 2];
            boxsize_ = (modsize_ + 2) * 22 + 66;
        } else
        {
            radioarray1_ = new IFRadioButton[modsize_ + 1];
            boxsize_ = (modsize_ + 1) * 22 + 66;
        }
        labels_ = new String[modsize_];
        charges_ = new int[modsize_];
        err_ = new float[modsize_];
        mod1_ = new String[modsize_];
        sel_theory_ = new boolean[modsize_];
        for(int i = 0; i <= modsize_; i++)
        {
            String tmp = vmsdb.str(i + 1);
            radioarray1_[i] = new IFRadioButton(tmp, x_ + 5, y_ + 22 * (i + 2) + 4 + 20, radio1_);
            c_.add(radioarray1_[i]);
            if(i < modsize_)
            {
                mod1_[i] = exp_.p1_[select_current_].theorym_[i].minus_;
                charges_[i] = exp_.p1_[select_current_].theorym_[i].charge_;
                err_[i] = (float)exp_.p1_[select_current_].theorym_[i].massError_;
                labels_[i] = exp_.p1_[select_current_].theorym_[i].ionType_;
                sel_theory_[i] = exp_.p1_[select_current_].theorym_[i].selected_;
            }
        }

        xsize_ -= modwidth_;
        modwidth_ = getMaxWidth();
        if(modwidth_ < 35)
            modwidth_ = 35;
        xsize_ += modwidth_;
        close_ = new IFButton("X", (x_ + xsize_) - 40, y_ + 2, 35);
        close_.setHeight(15);
        c_.add(close_);
        close_.addActionListener(vmain1_);
        save_ = new IFButton("Save", (x_ + xsize_) - 40, (y_ + boxsize_) - 20, 35);
        save_.setHeight(18);
        c_.add(save_);
        save_.addActionListener(vmain1_);
        input_ = new IFTextField("input", x_ + 42, (y_ + boxsize_) - 20, xsize_ - 90);
        input_.setHeight(18);
        c_.add(input_);
        if(isAnnot_)
        {
            radioarray1_[modsize_ + 1] = new IFRadioButton(vmsdb.str(modsize_ + 2), x_ + 5, y_ + 22 * (modsize_ + 3) + 4 + 20, radio1_);
            c_.add(radioarray1_[modsize_ + 1]);
            radio1_.selectButton(radioarray1_[modsize_]);
        } else
        {
            for(int j = 0; j < modsize_; j++)
                if(sel_theory_[j])
                {
                    vmsdb.println("selecting");
                    vmsdb.println(j);
                    radio1_.selectButton(radioarray1_[j]);
                }

        }
        c_.add(radio1_);
    }

    public void saveAnnot()
    {
        vmsdb.println("save annot");
        exp_.p1_[select_current_].annot_ = input_.getValue();
        vmsdb.println(exp_.p1_[select_current_].theorym_.length);
        annot_.saveAnnot( exp_.p1_[select_current_].xcor_, vmain1_.user_id_, input_.getValue());
        destroy();
    }

    public void changeTheory(int kp)
    {
        for(int j = 0; j < modsize_; j++)
            if(j == kp)
            {
                exp_.p1_[select_current_].theorym_[j].selected_ = true;
                sel_num_ = j;
            } else
            {
                exp_.p1_[select_current_].theorym_[j].selected_ = false;
            }

        exp_.updateSpectra(exp_.p1_);
    }

    public boolean highlight(int row)
    {
        return vmain1_.mouseX < x_ + xsize_ && vmain1_.mouseX > x_ && vmain1_.mouseY > y_ + 22 * (row - 1) && vmain1_.mouseY < y_ + 22 * row;
    }

    public void destroy()
    {
        if(modsize_ > 0)
        {
           /** for(int i = 0; i <= modsize_; i++)	//source of hanging (sql error 3 + ArrayIndexOutOfBoundException
                c_.remove(radioarray1_[i]);
*/
        	
        	try{
        		for(int j=0;j<radioarray1_.length;j++){	//new code
        			c_.remove(radioarray1_[j]);
        		}
        	}catch(ArrayIndexOutOfBoundsException e){
        		System.out.println("Would hang here!\nDatabase not updated!");
        	}
        }
        if(isAnnot_)
        c_.remove(radioarray1_[modsize_ + 1]);
        c_.remove(radio1_);
        c_.remove(input_);
        c_.remove(save_);
        c_.remove(close_);
        c_.setVisible(false);
        vmain1_.select_release_ = false;
        vmain1_.selectUI_ = null;
    }

    public void removeAnnot()
    {
        if(exp_.p1_[select_current_].annot_ != null)
            exp_.p1_[select_current_].annot_ = null;
        exp_.updateSpectra(exp_.p1_);
    }

    public int getMaxWidth()
    {
        float newxsize1 = 0.0F;
        if(exp_.p1_[select_current_].theorym_ != null)
        {
            for(int i = 0; i < exp_.p1_[select_current_].theorym_.length; i++)
            {
                String temp = "";
                temp = (new StringBuilder(String.valueOf(exp_.p1_[select_current_].theorym_[i].ionType_))).append(exp_.p1_[select_current_].theorym_[i].minus_).toString();
                float sw = vmain1_.textWidth(temp);
                if(sw > newxsize1)
                    newxsize1 = sw;
            }

        }
        return (int)newxsize1;
    }

    public void draw()
    {
        vmain1_.strokeWeight(1.0F);
        vmain1_.fill(255F, 255F, 255F);
        vmain1_.rect(x_, y_, xsize_, boxsize_);
        vmain1_.fill(200);
        vmain1_.rect(x_, y_, xsize_, 22F);
        vmain1_.stroke(0);
        vmain1_.fill(0);
        vmain1_.textFont(vmain1_.tickFont_);
        vmain1_.text("Choose Theoretical spectra:", x_ + 2, y_ + 16);
        for(int i = 0; i < modsize_; i++)
            if(highlight(i + 4))
            {
                vmain1_.fill(0.0F, 0.0F, 255F, 80F);
                vmain1_.rect(x_, y_ + ((i + 4) - 1) * 22, xsize_, 22F);
                vmain1_.stroke(0);
                vmain1_.fill(0);
                if(vmain1_.mousePressed)
                {
                    if(isAnnot_)
                    {
                        removeAnnot();
                        isAnnot_ = false;
                        changeTheory(i);
                        if(i == 0)
                            annot_.deleteAnnot( exp_.p1_[select_current_].xcor_);
                        else
                        	annot_.saveAnnot( exp_.p1_[select_current_].xcor_, vmain1_.user_id_, 
                        			exp_.p1_[select_current_].theorym_[i].ionType_ + exp_.p1_[select_current_].theorym_[i].minus_);
                    } else
                    {
                        changeTheory(i);
                        if(i == 0)
                        	annot_.deleteAnnot( exp_.p1_[select_current_].xcor_);
                        else
                        	annot_.saveAnnot( exp_.p1_[select_current_].xcor_, vmain1_.user_id_, 
                        			exp_.p1_[select_current_].theorym_[i].ionType_ + exp_.p1_[select_current_].theorym_[i].minus_);
                    }
                    destroy();
                }
            }

        for(int i = 0; i < modsize_ + 3; i++)
            vmain1_.line(x_, y_ + 22 * (i + 1), x_ + xsize_, y_ + 22 * (i + 1));

        vmain1_.textFont(vmain1_.tickFont_);
        if(isAnnot_)
        {
            if(highlight(modsize_ + 4))
            {
                vmain1_.fill(0.0F, 0.0F, 255F, 80F);
                vmain1_.rect(x_, y_ + ((modsize_ + 4) - 1) * 22, xsize_, 22F);
                vmain1_.stroke(0);
                vmain1_.fill(0);
                if(vmain1_.mousePressed)
                {
                    radio1_.selectButton(radioarray1_[modsize_]);
                    changeTheory(100);
                    destroy();
                }
            }
            vmain1_.line(x_, y_ + 22 * (modsize_ + 4), x_ + xsize_, y_ + 22 * (modsize_ + 4));
            vmain1_.line(x_ + 40, (y_ + boxsize_) - 22, x_ + 40, y_ + boxsize_);
            vmain1_.text(labelannot_, x_ + 46, y_ + 80 + 22 * modsize_);
            changeTheory(modsize_ - 1);
        }
        vmain1_.text((new StringBuilder("Abundance: ")).append(vmsdb.nfc(abundance_, 2)).append("%").toString(), x_ + 5, y_ + 35);
        vmain1_.text((new StringBuilder("M/Z: ")).append(vmsdb.nfc(mz_, 2)).toString(), x_ + 125, y_ + 35);
        vmain1_.text("Num:", x_ + 5, y_ + 57);
        vmain1_.line(x_ + 40, y_ + 44, x_ + 40, y_ + 88 + 22 * modsize_);
        vmain1_.text("Label", x_ + 45, y_ + 57);
        vmain1_.line(x_ + 40 + modwidth_, y_ + 44, x_ + 40 + modwidth_, y_ + 66 + 22 * modsize_);
        vmain1_.text("Charge", x_ + 43 + modwidth_, y_ + 57);
        vmain1_.line(x_ + 80 + modwidth_, y_ + 44, x_ + 80 + modwidth_, y_ + 66 + 22 * modsize_);
        vmain1_.text("MassError", x_ + 90 + modwidth_, y_ + 57);
        if(isRight)
            vmain1_.line(x_, y_, specx_, specy_);
        else
            vmain1_.line(specx_, specy_, x_ + xsize_, y_);
        for(int j = 0; j < modsize_; j++)
        {
            String tmp = mod1_[j];
            if(tmp != "")
                tmp = (new StringBuilder(String.valueOf(labels_[j]))).append(tmp).toString();
            else
                tmp = labels_[j];
            vmain1_.text(tmp, x_ + 46, y_ + j * 22 + 79);
            vmain1_.text((new StringBuilder("+")).append(charges_[j]).toString(), x_ + 50 + modwidth_, y_ + j * 22 + 79);
            vmain1_.text(vmsdb.nfc(err_[j], 4), x_ + 90 + modwidth_, y_ + j * 22 + 79);
        }

    }

    int x_;
    int y_;
    int xsize_;
    int ysize_;
    int select_current_;
    int modsize_;
    int boxsize_;
    int specx_;
    int specy_;
    float abundance_;
    float mz_;
    vmsdb vmain1_;
    int modwidth_;
    IFRadioController radio1_;
    IFRadioButton radioarray1_[];
    GUIController c_;
    IFButton close_;
    IFButton save_;
    IFTextField input_;
    String labels_[];
    String mod1_[];
    int charges_[];
    float err_[];
    boolean sel_theory_[];
    int sel_num_;
    String labelannot_;
    boolean isAnnot_;
    experiment exp_;
    annotation annot_;
    boolean isRight;
}
