//main class for running vmsdb in applet mode (for interactive browsing of annotated spectra)

import interfascia.GUIEvent;
import interfascia.IFTextField;
import java.io.*;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import processing.core.PApplet;
import processing.core.PFont;
//import javax.swing.JFileChooser;
//import javax.swing.SwingUtilities;

public class vmsdb extends PApplet
{

    public vmsdb()
    {
    	//constructor for main class vmsdb
        outfileName_ = "c:\\output.out";
        fileName_ = "c:\\output.dta";
        sequence_ = "";
        thresholdValue_ = 5;
        temphighlight_ = 0;
        _root_ = this;
        //fmcounter = "706681";
        dbEntryIdx_ = "706680";
        select_release_ = false;
        record_ = false;
        color1_ = "black";
        color2_ = "red";
        color3_ = "orange";
        color4_ = "gray";
        color5_ = "green";
        color6_ = "magenta";
        color7_ = "blue";
        color8_ = "pink";
        fmcounter2_ = "706777";
        fmcounter3_ = "706880";
        user_id_ = "testdb";
        focusexp_ = 0;
        select_legend_ = 0;
        mouseBuffer_ = 0;
        legendslength_ = 0.0F;
        speed_ = 10D;
        pname_ = "";
    }

    public void setup()
    {
    	// setup is always first called when using the processing.core libraries (setups for visualization)
    	try{
    		Config.Init(this.getClass().getProtectionDomain().getCodeSource().getLocation());
    	}catch(Exception e){
    		Config.Init(this.getCodeBase());
    	}
    	//noLoop();
    	frameRate(15);
    	
        size(1024, 768, JAVA2D);
    	//size(1024, 768, OPENGL);
        //font = createFont("Geneva-11.vlw", 11F);
        //arial2 = createFont("ArialMT-16.vlw", 16F);
        //impact = createFont("Impact-16.vlw", 16F);
        //tickFont = createFont("Papyrus-10.vlw", 10F);
        font_ = loadFont("Geneva-11.vlw");
        arial2_ = loadFont("FrutigerLight-13.vlw");
        //impact = loadFont("Impact-16.vlw");
        //tickFont = createFont("Papyrus-10.vlw", 10F);
        gfunk_ = new graphfunk();
        // "1646212";
        // "706680";
        //dbEntryIdx_ = "706680";
        dbEntryIdx_ = "2565841";
       
        
        spec_=new Spectrum();
        annot_=new annotation(Config.APPLET_MODE?"DB":"FILE");
        
        if(Config.APPLET_MODE){
            try {
                // getParameter (retrieves data from the url)
                // gets fmcounter variable
                // gets user variable from url
    	        String s = getParameter("fmcounter");
    	        if(s != null)   {
    	           dbEntryIdx_ = s;
    	        }
    	        s = getParameter("user");
    	        if(s != null)
    	            user_id_ = s;
    	        // gets new protein name variable from url
    	        s = getParameter("name");
    	        if(s != null)
    	        	pname_ = s;
            }catch(Exception e){
                println(e.getMessage());
            }
            // how to solve database username and password problem?
            // handler should pass user/passwd for db connection
        	spec_.setAppletPlugin();
            spec_.setHandler("MZ", "", "");
            spec_.setHandler("SO", "", "");
            spec_.parse(dbEntryIdx_);   
            annot_.setIdentifier(dbEntryIdx_);
        }else{
        	spec_.setPlugin(InputDataSource.mzPlugin,InputDataSource.rsPlugin);
        	if(InputDataSource.mzStruct.equals("M")&&InputDataSource.current_mz_idx!=-1){
        		spec_.setHandler("MZ", Util.ReadAll(InputDataSource.mz_ident_list.get(InputDataSource.current_mz_idx).filename), InputDataSource.mz_ident_list.get(InputDataSource.current_mz_idx).filename);
        	}else if(InputDataSource.mzStruct.equals("S")){
        		spec_.setHandler("MZ", InputDataSource.mzData, InputDataSource.mzFile);
        	}
        	if(InputDataSource.rsStruct.equals("M")&&InputDataSource.current_rs_idx!=-1){
        		spec_.setHandler("SO", Util.ReadAll(InputDataSource.rs_ident_list.get(InputDataSource.current_rs_idx).filename), InputDataSource.rs_ident_list.get(InputDataSource.current_rs_idx).filename);
        	}else if(InputDataSource.mzStruct.equals("S")){
        		spec_.setHandler("SO", InputDataSource.rsData, InputDataSource.rsFile);
        	}
        	spec_.parse(InputDataSource.current_mz_idx==-1?InputDataSource.rs_ident_list.get(InputDataSource.current_rs_idx).matchid:InputDataSource.mz_ident_list.get(InputDataSource.current_mz_idx).matchid);
        	annot_.setIdentifier(InputDataSource.current_mz_idx==-1?
        			InputDataSource.rs_ident_list.get(InputDataSource.current_rs_idx).filename + "." + InputDataSource.rs_ident_list.get(InputDataSource.current_rs_idx).matchid + ".annot"
        			:
        			InputDataSource.mz_ident_list.get(InputDataSource.current_mz_idx).filename + "." + InputDataSource.mz_ident_list.get(InputDataSource.current_mz_idx).matchid + ".annot");
        }
        
        



        
        
        calculate_ = new calculateTheory(this, sequence_);
        userinterface_ = new UI(300, 300, 400, 220, this, sequence_);
        fmcount_ = new String[3];
        fmcount_[0] = dbEntryIdx_;
        fmcount_[1] = fmcounter2_;
        fmcount_[2] = fmcounter3_;
        collection_ = new experiment[1];
        collection_[0] = new experiment(this, calculate_, userinterface_,  spec_, annot_, 0);
        numexp_ = collection_.length;
        for(int i = 0; i < numexp_; i++)
            legendslength_ = legendslength_ + collection_[i].legend_.xsize_ + 10F;

     //   float temp1 = ((float)width - legendslength_) / 2.0F;		//never read
        legendslength_ = 0.0F;
        for(int i = 0; i < numexp_; i++)
            legendslength_ = legendslength_ + collection_[i].legend_.xsize_ + 10F;

        userinterface_.setSequence(collection_[0].sequence_);
        midpt_ = width / 2;
    }

    public void keyPressed()
    {
    	// called when a specific key is pressed
        if(!select_release_)
            userinterface_.keyPressed();
        if(keyCode == 37)
        {
        	// button for changing phosphorlyation position
            if(collection_[0].changePhos() >= 0)
            {
                collection_[0].leftPhos();
                println("left");
            }
        } else
        if(keyCode == 39 && collection_[0].changePhos() >= 0)
        {
            collection_[0].rightPhos();
            println("right");
        }
    }

    public void mousePressed()
    {
    	// Called when a mouse click is detected
    	
        println("mspress");
        if(mouseY > height - 100)
        {
            for(int i = 0; i < collection_.length; i++)
                if(collection_[i].legend_.isSelected())
                    select_legend_ = i;

        }
        
        // creates selection box based on theoretical spectra on a specific peak
        if(mouseX > collection_[0].palette_.x_ && mouseX < collection_[0].palette_.x_ + collection_[0].palette_.xsize_ && mouseY > collection_[0].palette_.y_ && mouseY < collection_[0].palette_.y_ + collection_[0].palette_.ysize_ && !select_release_ && !userinterface_.on_ && collection_[0].current_selection_ != 0)
        {
            if(mouseX + 225 > width)
                selectUI_ = new Selection(mouseX - 225, mouseY, 225, 110, this, collection_[0], annot_);
            else
                selectUI_ = new Selection(mouseX, mouseY, 225, 110, this, collection_[0], annot_);
            select_release_ = true;
            println("selection made");
        }
        
    }

    public void draw()
    {
    	// draw is called once per frame (this is where the visualization is mainly called)
        background(255);
        for(int i = 0; i < collection_.length; i++)
            collection_[i].draw();

        if(selectUI_ != null && select_release_)
            selectUI_.draw();
        else
            userinterface_.draw();
        
    }

    public void actionPerformed(GUIEvent e)
    {
    	//function for detecting button actions 
        if(e.getSource() == userinterface_.go1_)
        {
        	// button for changing sequence 
            println("go was clicked");
            update(userinterface_.seq1_.getValue(), false);
            
        } else
        if(e.getSource() == userinterface_.minusY_)
        {
        	// button for increasing yaxis
            println("minusY was clicked");
            updateLevels(Integer.parseInt(userinterface_.ylevels_.getValue()), -1);
            
        } else
        if(e.getSource() == userinterface_.plusY_)
        {
        	// button for decreasing yaxis
            println("plusY was clicked");
            updateLevels(Integer.parseInt(userinterface_.ylevels_.getValue()), 1);
        } else
        if(e.getSource() == userinterface_.goylevels_) {
            updateLevels(Integer.parseInt(userinterface_.ylevels_.getValue()), 0);
        }
        else
        if(e.getSource() == userinterface_.minus_)
        {
        	// button for decreasing threshold
            println("minus was clicked");
            if(thresholdValue_ - 1 > 0)
                thresholdValue_--;
            else
                thresholdValue_ = 0;
            userinterface_.threshold1_.setValue(str(thresholdValue_));
        } else
        if(e.getSource() == userinterface_.plus_)
        {
        	// button for increasing threshold
            println("plus was clicked");
            thresholdValue_++;
            if(thresholdValue_ > 100)
                thresholdValue_ = 100;
            userinterface_.threshold1_.setValue(str(thresholdValue_));
        } else
        if(e.getSource() == userinterface_.gothres_)
        {
        	// button for setting threshold
            String temp = userinterface_.threshold1_.getValue();
            thresholdValue_ = parseInt(temp);
        } else
        if(e.getSource() == selectUI_.close_)
        {
        	// button for closing threshold
            println("ui closing");
            selectUI_.destroy();
        } else
        if(e.getSource() == selectUI_.save_)
        {
        	// button for user interface saving
            println("ui saving");
            selectUI_.saveAnnot();
        }
    }

    void savepdf(String s2)
    {
    	// function for saving pdf
        String tmp = (new StringBuilder(String.valueOf(s2))).append(".pdf").toString();
        println("saving");
        println(tmp);
        beginRecord("processing.pdf.PGraphicsPDF", tmp);
        palette_.draw();
        legend_.draw();
        endRecord();
    }

    void update(String seq1, boolean thres)
    {
    	// updates current spectra
        if(!thres)
            collection_[0].update(seq1);
    }

    void updateLevels(int temp1, int newlevel)
    {
    	// function for changing number of levels
        int tempY = collection_[0].getYLevels();
        if(newlevel == 1)
            tempY++;
        else
        if(newlevel == -1)
            tempY--;
        else
            tempY = temp1;
        collection_[0].setYLevels(tempY);
        userinterface_.ylevels_.setValue(str(tempY));
    }

    public int getThreshold()
    {
    	// returns current threshold
        return thresholdValue_;
    }

    public void setThreshold(int newthres)
    {
    	// sets current threshold value
        thresholdValue_ = newthres;
    }

    public void drawDashedLine(int x1, int y1, int x2, int y2, double dashlength, double spacelength, String colorcode)
    {
    	// draws a dashed line 
        if(x1 == x2 && y1 == y2)
        {
            line(x1, y1, x2, y2);
            return;
        }
        double linelength = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
  //      double yincrement = (double)(y2 - y1) / (linelength / (dashlength + spacelength));	//never read
        double xincdashspace = (double)(x2 - x1) / (linelength / (dashlength + spacelength));
        double yincdashspace = (double)(y2 - y1) / (linelength / (dashlength + spacelength));
        double xincdash = (double)(x2 - x1) / (linelength / dashlength);
        double yincdash = (double)(y2 - y1) / (linelength / dashlength);
        int counter = 0;
        for(double i = 0.0D; i < linelength - dashlength; i += dashlength + spacelength)
        {
            line((int)((double)x1 + xincdashspace * (double)counter), (int)((double)y1 + yincdashspace * (double)counter), (int)((double)x1 + xincdashspace * (double)counter + xincdash), (int)((double)y1 + yincdashspace * (double)counter + yincdash));
            counter++;
        }

        if((dashlength + spacelength) * (double)counter <= linelength)
            line((int)((double)x1 + xincdashspace * (double)counter), (int)((double)y1 + yincdashspace * (double)counter), x2, y2);
    }

    
    
    public void colorChange(String colorcode, String colorcode2, float strokethick)
    {
    	// function for changing color schemes
        strokeWeight(strokethick);
        if(colorcode == "blue")
            stroke(0.0F, 0.0F, 255F);
        else
        if(colorcode == "red")
            stroke(255F, 0.0F, 0.0F);
        else
        if(colorcode == "yellow")
            stroke(255F, 255F, 0.0F);
        else
        if(colorcode == "green")
            stroke(0.0F, 192F, 0.0F);
        else
        if(colorcode == "orange")
            stroke(255F, 165F, 0.0F);
        else
        if(colorcode == "pink")
            stroke(238F, 169F, 184F);
        else
        if(colorcode == "magenta")
            stroke(255F, 0.0F, 255F);
        else
        if(colorcode == "grey")
            stroke(150);
        else
        if(colorcode == "highlight")
            stroke(204F, 102F, 0.0F);
        else
            stroke(0);
        if(colorcode2 == "blue")
            fill(0.0F, 0.0F, 255F);
        else
        if(colorcode2 == "red")
            fill(255F, 0.0F, 0.0F);
        else
        if(colorcode2 == "yellow")
            fill(255F, 255F, 0.0F);
        else
        if(colorcode2 == "green")
            fill(0.0F, 192F, 0.0F);
        else
        if(colorcode2 == "orange")
            fill(255F, 165F, 0.0F);
        else
        if(colorcode2 == "pink")
            fill(238F, 169F, 184F);
        else
        if(colorcode2 == "magenta")
            fill(255F, 0.0F, 255F);
        else
        if(colorcode2 == "grey")
            fill(150);
        else
        if(colorcode == "highlight")
            fill(204F, 102F, 0.0F, 90F);
        else
        if(colorcode == "white")
            fill(255);
        else
            fill(0);
    }

    public void colorChange(String colorcode, String colorcode2, float strokethick, float alpha1)
    {
    	// function for changing color schemes
        strokeWeight(strokethick);
        if(colorcode == "blue")
            stroke(0.0F, 0.0F, 255F);
        else
        if(colorcode == "red")
            stroke(255F, 0.0F, 0.0F);
        else
        if(colorcode == "yellow")
            stroke(255F, 255F, 0.0F);
        else
        if(colorcode == "green")
            stroke(0.0F, 192F, 0.0F);
        else
        if(colorcode == "orange")
            stroke(255F, 165F, 0.0F);
        else
        if(colorcode == "pink")
            stroke(238F, 169F, 184F);
        else
        if(colorcode == "magenta")
            stroke(255F, 0.0F, 255F);
        else
        if(colorcode == "grey")
            stroke(150);
        else
        if(colorcode == "highlight")
            stroke(204F, 102F, 0.0F);
        else
            stroke(0);
        if(colorcode2 == "blue")
            fill(0.0F, 0.0F, 255F, alpha1);
        else
        if(colorcode2 == "red")
            fill(255F, 0.0F, 0.0F, alpha1);
        else
        if(colorcode2 == "yellow")
            fill(255F, 255F, 0.0F, alpha1);
        else
        if(colorcode2 == "green")
            fill(0.0F, 192F, 0.0F, alpha1);
        else
        if(colorcode2 == "orange")
            fill(255F, 165F, 0.0F, alpha1);
        else
        if(colorcode2 == "pink")
            fill(238F, 169F, 184F, alpha1);
        else
        if(colorcode2 == "magenta")
            fill(255F, 0.0F, 255F, alpha1);
        else
        if(colorcode2 == "grey")
            fill(150, alpha1);
        else
        if(colorcode == "highlight")
            fill(204F, 102F, 0.0F, alpha1);
        else
        if(colorcode == "white")
            fill(255, alpha1);
        else
            fill(0, alpha1);
    }

    String parseInput(String s)
    {
    	// function for parsing input
        Pattern p1 = Pattern.compile("[a-z[A-Z][0-9][\\.][ ][\\+][\\=][\\^][\\#][\\@][\\$][\\!][\\_][\\~][\\;][\\'][\\,][\\*][\\%][\\(][\\)][\\[][\\]][\\:][\\-]]*");
        Matcher m1 = p1.matcher(s.toString());
      //  boolean found1 = m1.find();	//never read
        Vector a = new Vector();
        a.add(m1.group());
        String b = "";
        for(; m1.find(); a.add(b))
            b = m1.group();

        int j = 0;
        s = "";
        for(j = 0; j < a.size(); j++)
            s = (new StringBuilder(String.valueOf(s))).append(a.get(j).toString()).toString();

        return s;
    }

    public static void main(String args[])
    {
    	print(args);
        PApplet.main(new String[] {
            "vmsdb"
        });
    }
   
    
    // public variables
  //  private static final long serialVersionUID_ = 1L;		//never read
    String outfileName_;
    String fileName_;
    String sequence_;
    int thresholdValue_;
    int temphighlight_;
    Peak p1_[];
    public AAlegend legend_;
    public UI userinterface_;
    PFont font_= createFont("Geneva-11.vlw", 11F);
    PFont arial2_= createFont("ArialMT-16.vlw", 16F);;
    PFont impact_= createFont("Impact-16.vlw", 16F);;
    PFont tickFont_= createFont("Papyrus-10.vlw", 10F);;
    PApplet _root_;
    public calculateTheory calculate_;
    String dbEntryIdx_;
    public mspalette palette_;
    public Selection selectUI_;
    boolean select_release_;
    boolean record_;
    String color1_;
    String color2_;
    String color3_;
    String color4_;
    String color5_;
    String color6_;
    String color7_;
    String color8_;
    graphfunk gfunk_;
    experiment exp_;
    experiment exp2_;
    experiment exp3_;
    String fmcounter2_;
    String fmcounter3_;
    String user_id_;
    String fmcount_[];
    experiment collection_[];
    int numexp_;
    int focusexp_;
    int select_legend_;
    int mouseBuffer_;
    float legendslength_;
    int midpt_;
    double speed_;
    String pname_;
    boolean savepdf_ = false; 
    annotation annot_;
    Spectrum spec_;
}
