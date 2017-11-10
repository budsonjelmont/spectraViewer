import interfascia.*;

public class UI
{

    UI(int xA, int yA, int xwidth1, int ywidth1, vmsdb vmain1, String sequence1)
    {
        x_ = xA;
        y_ = yA;
        xwidth_ = xwidth1;
        ywidth_ = ywidth1;
        on_ = false;
        vmain_ = vmain1;
        curr_seq_ = sequence1;       
        
        c_ = new GUIController(vmain_);
        b1_ = new IFCheckBox("B1", x_ + 10, y_ + 65);
        b1_.makeTrue();
        b2_ = new IFCheckBox("B2", x_ + 10, y_ + 85);
        b2_.makeTrue();
        b3_ = new IFCheckBox("B3", x_ + 10, y_ + 105);
        y1_ = new IFCheckBox("Y1", x_ + 70, y_ + 65);
        y1_.makeTrue();
        y2_ = new IFCheckBox("Y2", x_ + 70, y_ + 85);
        y2_.makeTrue();
        y3_ = new IFCheckBox("Y3", x_ + 70, y_ + 105);
        mcheck_ = new IFCheckBox("M", x_ + 10, y_ + 45);
        mcheck_.makeTrue();
        usercheck_ = new IFCheckBox("User", x_ + 70, y_ + 45);
        usercheck_.makeTrue();
        mod1_ = new IFCheckBox("H20", x_ + 10, y_ + 125);
        mod2_ = new IFCheckBox("HPO3", x_ + 10, y_ + 145);
        mod3_ = new IFCheckBox("H3PO4", x_ + 70, y_ + 145);
        mod4_ = new IFCheckBox("NH3", x_ + 70, y_ + 125);
        x1_ = new IFCheckBox("X", x_ + 70, y_ + 165);
        z1_ = new IFCheckBox("Z", x_ + 70, y_ + 185);
        a1_ = new IFCheckBox("A", x_ + 10, y_ + 165);
        c1_ = new IFCheckBox("C", x_ + 10, y_ + 185);
        seqlength_ = (int)vmain_.textWidth(curr_seq_) + 25;
        seq1_ = new IFTextField("sequence:", x_ + 140, y_ + 95, seqlength_);
        go1_ = new IFButton("go", x_ + 150 + seqlength_, y_ + 95, 25);
        threshold1_ = new IFTextField("threshold:", x_ + 158, y_ + 45, (int)(vmain_.textWidth("55") + 25F));
        minus_ = new IFButton("-", x_ + 140, y_ + 45, 15);
        plus_ = new IFButton("+", x_ + 200, y_ + 45, 15);
        gothres_ = new IFButton("go", x_ + 220, y_ + 45, 25);
        ylevels_ = new IFTextField("# of Y-Axis:", x_ + 158, y_ + 145, (int)(vmain_.textWidth("55") + 25F));
        minusY_ = new IFButton("-", x_ + 140, y_ + 145, 15);
        plusY_ = new IFButton("+", x_ + 200, y_ + 145, 15);
        goylevels_ = new IFButton("go", x_ + 220, y_ + 145, 25);
        save1_ = new IFButton("Save", x_ + 300, y_ + 45, 50);
        ylevels_.addActionListener(vmain_);
        minusY_.addActionListener(vmain_);
        plusY_.addActionListener(vmain_);
        goylevels_.addActionListener(vmain_);
        ylevels_.setValue("3");
        c_.add(ylevels_);
        c_.add(minusY_);
        c_.add(plusY_);
        c_.add(goylevels_);
        c_.add(x1_);
        c_.add(z1_);
        c_.add(a1_);
        c_.add(c1_);
        c_.add(mcheck_);
        c_.add(usercheck_);
        c_.add(b1_);
        c_.add(b2_);
        c_.add(b3_);
        c_.add(y1_);
        c_.add(y2_);
        c_.add(y3_);
        c_.add(mod1_);
        c_.add(mod2_);
        c_.add(mod3_);
        c_.add(mod4_);
        c_.add(seq1_);
        c_.add(go1_);
        c_.add(threshold1_);
        c_.add(plus_);
        c_.add(minus_);
        c_.add(save1_);
        c_.add(gothres_);
        seq1_.setValue(vmain_.sequence_);
        threshold1_.setValue((new StringBuilder(String.valueOf(vmsdb.str(vmain_.thresholdValue_)))).append(".0").toString());
        c_.setVisible(false);
        go1_.addActionListener(vmain_);
        plus_.addActionListener(vmain_);
        minus_.addActionListener(vmain_);
        save1_.addActionListener(vmain_);
        gothres_.addActionListener(vmain_);
    }
    
    

    public void setSequence(String seqt)
    {
        seqlength_ = (int)vmain_.textWidth(seqt) + 25;
        seq1_.setWidth(seqlength_);
        seq1_.setValue(seqt);
        go1_.setPosition(x_ + 150 + seqlength_, y_ + 95);
    }

    public void keyPressed()
    {
        if(vmain_.key == ' ')
            if(on_)
            {
                on_ = false;
                c_.setVisible(false);
            } else
            {
                on_ = true;
                c_.setVisible(true);
            }
    }
    public void visible() {
    	
    	if (on_) {
    		on_ = false;
    		c_.setVisible(false);
    	}
    	else {
    		on_ = true; 
    		c_.setVisible(true);
    	}
    }

    public void draw()
    {
        if(on_)
        {
            vmain_.colorChange("white", "white ", 1.0F);
            vmain_.rect(x_, y_, xwidth_, ywidth_);
        /*    if(vmain_.mouseX > x_ && vmain_.mouseX < x_ + xwidth_ && vmain_.mouseY > y_)
            {
            //    int _tmp = vmain_.mouseY;	//NEVER READ
            }*/
            vmain_.rect(x_, y_, xwidth_, 20F);
            vmain_.colorChange("black", "black", 1.0F);
            vmain_.line(x_, y_ + 20, x_ + xwidth_, y_ + 20);
            vmain_.textFont(vmain_.font_, 12F);
            vmain_.text("Mass Spec Program Options", x_ + 5, y_ + 15);
            vmain_.line(x_ + 10, y_ + 40, x_ + 120, y_ + 40);
            vmain_.textFont(vmain_.font_, 12F);
            vmain_.text("Choose Labels:", x_ + 10, y_ + 37);
            vmain_.line(x_ + 140, y_ + 90, x_ + 140 + seqlength_, y_ + 90);
            vmain_.text("Current Sequence:", x_ + 140, y_ + 87);
            vmain_.line(x_ + 140, y_ + 140, x_ + 140 + seqlength_, y_ + 140);
            vmain_.text("# of Y-Axes:", x_ + 140, y_ + 137);
            vmain_.line(x_ + 140, y_ + 40, x_ + 140 + seqlength_, y_ + 40);
            vmain_.text("Threshold:", x_ + 140, y_ + 37);
            vmain_.line(x_ + 300, y_ + 40, x_ + 300 + 75, y_ + 40);
            vmain_.text("Save to PDF:", x_ + 300, y_ + 37);
        }
    }

    public boolean b1select()
    {
        return b1_.isSelected();
    }

    public boolean b2select()
    {
        return b2_.isSelected();
    }

    public boolean b3select()
    {
        return b3_.isSelected();
    }

    public boolean y1select()
    {
        return y1_.isSelected();
    }

    public boolean y2select()
    {
        return y2_.isSelected();
    }

    public boolean y3select()
    {
        return y3_.isSelected();
    }

    public boolean h2oselect()
    {
        return mod1_.isSelected();
    }

    public boolean hpo3select()
    {
        return mod2_.isSelected();
    }

    public boolean h3po4select()
    {
        return mod3_.isSelected();
    }

    public boolean nh3select()
    {
        return mod4_.isSelected();
    }

    public boolean userselect()
    {
        return usercheck_.isSelected();
    }

    public boolean xselect()
    {
        return x1_.isSelected();
    }

    public boolean zselect()
    {
        return z1_.isSelected();
    }

    public boolean aselect()
    {
        return a1_.isSelected();
    }

    public boolean cselect()
    {
        return c1_.isSelected();
    }

    public boolean mselect()
    {
        return mcheck_.isSelected();
    }

    int x_;
    int y_;
    int xwidth_;
    int ywidth_;
    boolean on_;
    GUIController c_;
    IFCheckBox b1_;
    IFCheckBox b2_;
    IFCheckBox b3_;
    IFCheckBox y1_;
    IFCheckBox y2_;
    IFCheckBox y3_;
    IFCheckBox mod1_;
    IFCheckBox mod2_;
    IFCheckBox mod3_;
    IFCheckBox mod4_;
    IFCheckBox mcheck_;
    IFCheckBox usercheck_;
    IFCheckBox a1_;
    IFCheckBox c1_;
    IFCheckBox x1_;
    IFCheckBox z1_;
    IFButton go1_;
    IFButton plus_;
    IFButton minus_;
    IFButton save1_;
    IFButton gothres_;
    IFButton goylevels_;
    IFButton plusY_;
    IFButton minusY_;
    IFTextField seq1_;
    IFTextField threshold1_;
    IFTextField ylevels_;
    String curr_seq_;
    int seqlength_;
    vmsdb vmain_;
}