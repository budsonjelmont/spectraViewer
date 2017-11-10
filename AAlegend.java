// Class for creating the legend for vmsdb
import java.util.Vector;

public class AAlegend
{

    AAlegend(Vector aa1, vmsdb vmain1, UI ui2, int x1, int y1, experiment exp2)
    {
    	
        xchanging_ = false;
        ychanging_ = false;
        wasselected_ = false;
        loose_ = 5;
        ctermString_ = "";
        proteinname_ = "";
        change_phos_ = -1;
        og_phos_ = -1;
        peptide_size_ = 0;
        x_ = x1;
        y_ = y1;
        newx_ = x1;
        newy_ = y1;
        diff_ = 15F;
        yup_ = 10F;
        ydown_ = 0.0F;
        vmain_ = vmain1;
        ui1_ = ui2;
        exp_ = exp2;
        ctermString_ = exp_.ctermString_;
        proteinname_ = exp_.proteinname_;
        priScore_ = exp_.priScore_;
        priScoreName_ = exp_.priScoreName_;
        s1_ = new String[aa1.size()];
        for(int i = 0; i < s1_.length; i++)
            s1_[i] = (String)aa1.get(i);

        peptide_size_ = s1_.length;
        xsize_ = (float)s1_.length * diff_;
        ysize_ = vmsdb.abs(8F * yup_);
        newxsize_ = xsize_;
        newysize_ = ysize_;
    }
    
    public void setProteinName(String name) {
    	//sets protein name
    	proteinname_ = name; 
    }

    public int ogPhos() {
    	// returns original phosphorlyation position from sequence file
        return og_phos_;
    }

    public void setSequence(Vector s2)
    {
    	// sets the legends current sequence
        s1_ = new String[s2.size()];
        for(int i = 0; i < s1_.length; i++)
            s1_[i] = (String)s2.get(i);

    }

    public boolean isSelected()
    {
    	// return true if mouseX and mouseY are located within the legend boundaries
        return (float)vmain_.mouseX > x_ && (float)vmain_.mouseX < x_ + xsize_ && (float)vmain_.mouseY > y_ && (float)vmain_.mouseY < y_ + ysize_;
    }

    public void changeX(int x1)
    {
    	// changes x position
        newx_ = x1;
    }

    public void setXY(int x1, int y1)
    {
    	// changes x and y position
        newx_ = x1;
        newy_ = y1;
    }

    public void setXsize(int x1)
    {
    	// sets current width of legend
        xsize_ = x1;
    }

    public void setYsize(int y1)
    {
    	// sets legends height
        ysize_ = y1;
    }

    public void update()
    {
    	// updates current sizes to aa legend
        if(newx_ != x_)
            xchanging_ = true;
        else
            xchanging_ = false;
        if(newy_ != y_)
            ychanging_ = true;
        else
            ychanging_ = false;
        if(xchanging_ || ychanging_)
        {
            if(vmsdb.abs(newx_ - x_) > 1.0F)
                x_ += (newx_ - x_) / (float)loose_;
            else
                x_ = newx_;
            if(vmsdb.abs(newy_ - y_) > 1.0F)
                y_ += (newy_ - y_) / (float)loose_;
            else
                y_ = newy_;
        }
    }

    public void setSpectra(Peak ms2[])
    {
    	// sets the current spectra (peaks)
        ms_ = ms2;
    }

    public void drawAnnotation()
    {
    	// draws legend
    	
        vmain_.strokeWeight(1.0F);
        vmain_.fill(255);
        
        vmain_.textFont(vmain_.font_, 11F);
        float annotsize = 110F + vmain_.textWidth(proteinname_);
        if(annotsize < 200F)
            annotsize = 200F;
        boolean annotOver = false;
        if((float)vmain_.mouseX > x_ + newxsize_ + 10F && (float)vmain_.mouseX < x_ + newxsize_ + 10F + annotsize && (float)vmain_.mouseY < y_ + ysize_ && (float)vmain_.mouseY > y_)
        {
            annotOver = true;
            vmain_.stroke(255F, 0.0F, 0.0F);
        } else
        {
            vmain_.stroke(0);
        }
        // if legend is pressed, it will open a web browser to the selected protein
        if(annotOver && vmain_.mousePressed)
        {
            String url = "http://www.ncbi.nlm.nih.gov/entrez/viewer.fcgi?db=protein&val=";
            url = (new StringBuilder(String.valueOf(url))).append(exp_.id_).toString();
            vmain_.link(url, "_new");
            vmsdb.println("clicked");
            vmsdb.println(url);
        }
        vmain_.rect(x_ + newxsize_ + 10F, y_, annotsize, ysize_);
        vmain_.fill(0);
        vmain_.stroke(0);
        vmain_.textAlign(39);
        vmain_.text("Protein Name:", x_ + newxsize_ + 105F, y_ + 15F);
        vmain_.text(exp_.id_type_+" Accession:", x_ + newxsize_ + 105F, y_ + 30F);
        vmain_.text("Precursor Mass:", x_ + newxsize_ + 105F, y_ + 45F);
        vmain_.text("Delta Mass:", x_ + newxsize_ + 105F, y_ + 60F);
        vmain_.text(priScoreName_ +":", x_ + newxsize_ + 105F, y_ + 75F);
        vmain_.textAlign(37);
        vmain_.text(proteinname_, x_ + newxsize_ + 110F, y_ + 15F);
        vmain_.text(exp_.id_, x_ + newxsize_ + 110F, y_ + 30F);
        vmain_.text(String.valueOf(priScore_), x_ + newxsize_ + 110F, y_ + 75F);
        double expTH = exp_.molecularWeight_;
        double expEX = exp_.molecularWeightE_;
        double charge = exp_.chargeState_;
        double constant = 1.007272F;

        double deltamass = Math.abs(((expEX - constant) / charge + constant) - ((expTH - constant) / charge + constant)) / (expEX / charge);
        deltamass *= 1000000F;
        double premass = Math.abs(expTH - constant) / charge + constant;
        vmain_.text((new StringBuilder(String.valueOf(vmsdb.nfc(Float.parseFloat(Double.toString(premass)), 4)))).append("+").append(exp_.chargeState_).toString(), x_ + newxsize_ + 110F, y_ + 45F);
        vmain_.text((new StringBuilder(String.valueOf(vmsdb.nfc(Float.parseFloat(Double.toString(deltamass)), 3)))).append(" ppm").toString(), x_ + newxsize_ + 110F, y_ + 60F);
    }

    void draw()
    {
    	// called once per frame 
        update();
        vmain_.strokeWeight(1.0F);
        int theory_select = 0;
        
        // if legend is selected boundary line is set to red
        if(isSelected())
            vmain_.stroke(255F, 0.0F, 0.0F);
        else
            vmain_.stroke(0);
        
        // draws text
        drawAnnotation();
        vmain_.fill(255);
        vmain_.rect(x_, y_, newxsize_, newysize_);
        
        // draws rect above and below the sequence to represent peaks
        for(int i = 0; i < ms_.length; i++)
            if(ms_[i].theorym_ != null && ms_[i].ycor_ > ((double)vmain_.thresholdValue_ / 100D) * exp_.palette_.abundmax_)
            {
                theory_select = 0;
                for(int j = 0; j < ms_[i].theorym_.length; j++)
                    if(ms_[i].theorym_[j].selected_)
                        theory_select = j;
                
                // retrieves information about the selected spectra
                String mod1 = ms_[i].theorym_[theory_select].minus_;
                int charge = ms_[i].theorym_[theory_select].charge_;
                String label1 = ms_[i].theorym_[theory_select].ionType_;
                int aapos = ms_[i].theorym_[theory_select].aapos_ - 1;
                
                int revaapos = peptide_size_ - aapos - 1;
                if(label1.charAt(0) == 'b' && mod1 == "")
                {
                	// sets b labels to blue
                    vmain_.stroke(0.0F, 0.0F, 255F);
                    vmain_.fill(0.0F, 0.0F, 255F);
                    vmain_.rect(x_ + (float)aapos * diff_ + 5F, (y_ + ysize_ / 2.0F) - 6F - (float)(7 * charge), (x_ + (float)aapos * diff_ + 7F) - (x_ + (float)aapos * diff_), 3F);
                } else
                if(label1.charAt(0) == 'y' && mod1 == "")
                {
                	// sets y labels to red
                    vmain_.stroke(255F, 0.0F, 0.0F);
                    vmain_.fill(255F, 0.0F, 0.0F);
                    vmain_.rect(x_ + (float)revaapos * diff_ + 5F, y_ + ysize_ / 2.0F + 6F + (float)(7 * charge), (x_ + (float)revaapos * diff_ + 7F) - (x_ + (float)revaapos * diff_), 3F);
                } else
                if(label1.charAt(0) == 'a' || label1.charAt(0) == 'c')
                {
                	// sets a and c labels to blue
                    vmain_.stroke(0.0F, 0.0F, 255F);
                    vmain_.fill(255);
                    vmain_.rect(x_ + (float)aapos * diff_ + 5F, (y_ + ysize_ / 2.0F) - 6F - (float)(7 * charge), (x_ + (float)aapos * diff_ + 7F) - (x_ + (float)aapos * diff_), 3F);
                } else
                if(label1.charAt(0) == 'x' || label1.charAt(0) == 'z')
                {
                	// sets x and z labels to red
                    vmain_.stroke(255F, 0.0F, 0.0F);
                    vmain_.fill(255);
                    vmain_.rect(x_ + (float)revaapos * diff_ + 5F, y_ + ysize_ / 2.0F + 6F + (float)(7 * charge), (x_ + (float)revaapos * diff_ + 7F) - (x_ + (float)revaapos * diff_), 3F);
                } else
                if(label1.charAt(0) == 'M' && mod1 == "")
                {
                	// sets M labels to brownish orangish
                    vmain_.stroke(51F, 0.0F, 153F);
                    vmain_.fill(51F, 0.0F, 153F);
                    vmain_.rect(x_ + (float)aapos * diff_ + 5F, (y_ + ysize_ / 2.0F) - 6F - (float)(7 * charge), (x_ + (float)aapos * diff_ + 7F) - (x_ + (float)aapos * diff_), 3F);
                    vmain_.rect(x_ + (float)revaapos * diff_ + 5F, y_ + ysize_ / 2.0F + 6F + (float)(7 * charge), (x_ + (float)revaapos * diff_ + 7F) - (x_ + (float)revaapos * diff_), 3F);
                }
            }

        for(int i = 0; i < s1_.length; i++)
        {
            if((x_ + 5F + (float)i * diff_) - diff_ / 2.0F <= (float)vmain_.mouseX && (float)vmain_.mouseX <= (x_ + 5F + (float)(i + 1) * diff_) - diff_ / 2.0F && (y_ + ysize_ / 2.0F) - 6F <= (float)vmain_.mouseY && (float)vmain_.mouseY <= y_ + ysize_ / 2.0F + 6F)
            {
                if(s1_[i].length() > 1)
                {
                    if(vmain_.mousePressed)
                    {
                    	// phosphorlyation position being detected
                        vmain_.stroke(0.0F, 255F, 0.0F);
                        vmain_.strokeWeight(1.0F);
                        vmain_.noFill();
                        float tsize = vmain_.textWidth(s1_[i]);
                        vmain_.rect(x_ + 2.0F + (float)i * diff_, (y_ + ysize_ / 2.0F) - 5F, tsize + 4F, 14F);
                        change_phos_ = i;
                        og_phos_ = i;
                    }
                } else
                if(vmain_.mousePressed)
                {
                    change_phos_ = -1;
                    og_phos_ = -1;
                }
                vmain_.colorChange("red", "red", 1.0F);
                vmain_.textFont(vmain_.arial2_, 13F);
                exp_.palette_.temphighlight_ = i + 1;
            } else
            {
                vmain_.colorChange("black", "black", 3F);
                vmain_.textFont(vmain_.arial2_, 13F);
            }
            vmain_.text(s1_[i], x_ + 5F + (float)i * diff_, y_ + ysize_ / 2.0F + 6F);
            if(change_phos_ == i)
            {
                vmain_.stroke(0.0F, 255F, 0.0F);
                vmain_.strokeWeight(1.0F);
                vmain_.noFill();
                float tsize = vmain_.textWidth(s1_[i]);
                vmain_.rect(x_ + 2.0F + (float)i * diff_, (y_ + ysize_ / 2.0F) - 5F, tsize + 4F, 14F);
            }
        }

        if((float)vmain_.mouseX < x_ || (float)vmain_.mouseX > x_ + diff_ * (float)s1_.length)
            exp_.palette_.temphighlight_ = 0;
        vmain_.strokeWeight(1.0F);
    }

    // public variables fo aalegend
    String s1_[];
    float x_;
    float y_;
    float diff_;
    float yup_;	//y up
    float ydown_;
    float xsize_;
    float newxsize_;
    float ysize_;
    float newysize_;
    float newx_;
    float newy_;
    vmsdb vmain_;
    UI ui1_;
    Peak ms_[];
    experiment exp_;
    boolean xchanging_;
    boolean ychanging_;
    boolean wasselected_;
    int loose_;
    String ctermString_;
    String proteinname_, priScoreName_;
    double priScore_;
    
    float datamass_;
    int change_phos_;
    int og_phos_;
    int peptide_size_;
}
