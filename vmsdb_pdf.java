//main class for running vmsdb as a PDF generator that compiles all annotated spectra from an experiment into a multi-page PDF

import interfascia.GUIEvent;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import processing.core.PApplet;
import processing.core.PFont;
import processing.pdf.*;

import javax.swing.*;

import com.lowagie.text.pdf.*;
import com.lowagie.text.Document;
import com.lowagie.text.Rectangle;
	
	
public class vmsdb_pdf extends vmsdb
{
	
    public vmsdb_pdf()
    {   
   	String outfileName;  	
    String fileName;
    String sequence;
    int thresholdValue;
    int temphighlight;
    Peak p1[];
    AAlegend legend;
    UI_pdf userinterface;
    PFont font;
    PFont arial2;
    PFont impact;
    PFont tickFont;
    PApplet _root;
    calculateTheory calculate;
    String fmcounter;
    mspalette palette;
    Selection selectUI;
    boolean select_release;
    boolean record;
    String color1;
    String color2;
    String color3;
    String color4;
    String color5;
    String color6;
    String color7;
    String color8;
    graphfunk gfunk;
    database currentdb;
    experiment exp;
    experiment exp2;
    experiment exp3;
    String fmcounter2;
    String fmcounter3;
    String user_id;
    String fmcount[];
    experiment pdfcollection[];
    experiment collection[];
    int numexp;
    int focusexp;
    int select_legend;
    int mouseBuffer;
    float legendslength;
    int midpt;
    double speed;
    String pname;
    boolean savepdf = false;      
    String jcountername; 	
    
    jcountername = "c:\\jcounter.txt";
    
        outfileName = "c:\\output.out";
        fileName = "c:\\output.dta";
        sequence = "";
        thresholdValue = 5;
        temphighlight = 0;
        _root = this;
        fmcounter = "706681";
        select_release = false;
        record = false;
        color1 = "black";
        color2 = "red";
        color3 = "orange";
        color4 = "gray";
        color5 = "green";
        color6 = "magenta";
        color7 = "blue";
        color8 = "pink";
        fmcounter2 = "706777";
        fmcounter3 = "706880";
        user_id = "testdb";
        focusexp = 0;
        select_legend = 0;
        mouseBuffer = 0;
        legendslength = 0.0F;
        speed = 10D;
        pname = "";
    }

    public void setup()
    {


    	try{
    		Config.Init(this.getClass().getProtectionDomain().getCodeSource().getLocation());
    	}catch(Exception e){
    		Config.Init(this.getCodeBase());
    	}
    	
        font_ = createFont("Geneva-11.vlw", 11F);
        arial2_ = createFont("ArialMT-14.vlw", 14F);
        impact_ = createFont("sansserif-18.vlw", 18F);
        tickFont_ = createFont("PMicrosoftSansSerif-10.vlw", 10F);
        
        pdfoutname_="C:\\pdf\\s.pdf";
        /*
        
    	try {
    		//println("file");
    		
    		final JFileChooser fc1 = new JFileChooser(); 
    		int returnVal = fc1.showSaveDialog(this);

    		if (returnVal == JFileChooser.APPROVE_OPTION) { 
    			File file = fc1.getSelectedFile(); 
    			String s2 = fc1.getCurrentDirectory() + "\\" +file.getName() + ".pdf"; 
    			pdfoutname_ = s2; 
    		} 
    		else { 
    			println("Save command cancelled by user."); 
    		} 
    		//println("pdfoutname");
    		//println(pdfoutname);
    		
    	} catch (Exception e1) { 
			e1.printStackTrace(); 
    	} 
        */
    	//registerDispose(this);

        //size(800, 600, JAVA2D);
    	size(400, 220, JAVA2D);

        gfunk_ = new graphfunk();
        
        spec_=new Spectrum();
        annot_=new annotation(Config.APPLET_MODE?"DB":"FILE");
        spec_.setAppletPlugin();
        spec_.setHandler("MZ", "", "");
        spec_.setHandler("SO", "", "");
        
        
        calculate_ = new calculateTheory(this, sequence_);
        
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
            	/*
            	
        		//size(1024, 768, PDF, pdfoutname);
    			//savepdfseries(g);
        		parseCounter("c:\\jcounter.txt");    
        	    //currentdb = new database();
        	    //gfunk = new graphfunk();
        	        
        	    //calculate = new calculateTheory(vmsdb_pdf.this, sequence);
        		experiment pdfcollection[] = new experiment[fmLookup_.size()]; 
    	        
        		//noLoop();
    			
    	    
        		//PGraphicsPDF pdf = (PGraphicsPDF)g;
        		processing.core.PGraphics pdf = createGraphics(1024, 768, PDF, pdfoutname_);
        		PGraphicsPDF pdf1 = (PGraphicsPDF)pdf;
        		
        		println("counter names");
        		println(fmLookup_); 
        		
        		//beginRecord(PDF, pdfoutname); 
        		for (int i = 0; i < fmLookup_.size(); i++) {

        			spec_.parse(fmLookup_.get(i).toString());   
        	        
        	        annot_.setIdentifier(fmLookup_.get(i).toString());

        			pdfcollection[i] = new experiment(vmsdb_pdf.this, calculate_, userinterface_,spec_, annot_, 0);
        			userinterface_.setSequence(pdfcollection[i].sequence_);
        			pdfcollection[i].legend_.setProteinName(nameLookup_.get(i).toString());
        			pdfcollection[i].draw(); 
        			pdf1.nextPage();  // tell it to go to the next page
        		}
        		//endRecord(); 
        		
    		

                */
            	exit();
            }
            
        });
    
        
        

        
        
        fmcount_ = new String[3];
        midpt_ = width / 2;
        
        userinterface_ = new UI(0, 0, 400, 220, this, sequence_);
        userinterface_.visible();
		
		/*
		
        
        fmcount = new String[3];
        midpt = width / 2;
			
		size(1024, 768, PDF, pdfoutname);
			//savepdfseries(g);
		parseCounter("c:\\jcounter.txt");    
		experiment pdfcollection[] = new experiment[fmLookup.size()]; 
	        
		noLoop();
		
	    
		PGraphicsPDF pdf = (PGraphicsPDF)g;
		
		for (int i = 0; i < fmLookup.size(); i++) {
			pdfcollection[i] = new experiment(this, fmLookup.get(i).toString(), calculate, currentdb, userinterface, 0);
			userinterface.setSequence(pdfcollection[i].sequence);
			pdfcollection[i].legend.setProteinName(nameLookup.get(i).toString());
			pdfcollection[i].draw(); 
			pdf.nextPage();  // tell it to go to the next page
		}
		exit();
		
		*/
        setThreshold(0);
        savepdf(pdfoutname_);
        exit();

    }
    
    
    public void parseCounter(String filein) {
    	fmLookup_ = new Vector();
    	nameLookup_ = new Vector(); 
    	
    	try {
    		File theFile = new File(filein);
            FileInputStream fstream = new FileInputStream(theFile);
            DataInputStream in = new DataInputStream(fstream);
            for(int h = 0; in.available() != 0; h++){
                /*if(h == 0)
                {*/
                    String temp = in.readLine();
                    /*
                    for(int i=0;i<temp.length();i++){
                    	System.out.println((int)(temp.charAt(i)));
                    	if(i==9) 
                    		break;
                    }*/
                    //println(temp);
                    if(temp.indexOf((char)11)>=0){
                    	String[] words1=temp.split(String.valueOf((char)11));
                    	for(int i=0;i<words1.length;i++){
                    		String[] words = words1[i].split("@");
    	                    if(words[0].indexOf("\t")>=0){
    	                    	words=words[0].split("\t");
    	                    }
                            fmLookup_.add(new String(words[0]));
                            if (words.length > 1) {
                            	nameLookup_.add(new String(words[1]));
                            }else
                            	nameLookup_.add("");
                    	}
                    }else{
	                    String[] words = temp.split("@");
	                    if(words[0].indexOf("\t")>=0){
	                    	words=words[0].split("\t");
	                    }
	                    fmLookup_.add(new String(words[0]));
	                    if (words.length > 1) {
	                    	nameLookup_.add(new String(words[1]));
	                    }else
	                    	nameLookup_.add("");
                    }
/*
                } else
                {
                    String temp = in.readLine();
                    String[] words = temp.split("\t");
                    fmLookup_.add(new String(words[0]));
                    if (words.length > 1) {
                    	nameLookup_.add(new String(words[1]));
                    }else
                    	nameLookup_.add("");
                }*/
            }
            in.close();
    	}
        catch(Exception e)
        {
            System.err.println("File input error");
        }
    }

    public void keyPressed()
    {
        if(!select_release_)
            userinterface_.keyPressed();

        
        if (keyCode==RIGHT) {
        	

        }
    }

    public void mousePressed()
    {
        //println("mspress");
            //println("selection made");
        
    }

    public void draw()
    {
        background(255);
        //for(int i = 0; i < collection.length; i++)
        //    collection[i].draw();

        //if(selectUI != null && select_release)
        //    selectUI.draw();
        //else
        

        userinterface_.draw();
        
    }

    public void mouseReleased()
    {
    }

    public void actionPerformed(GUIEvent e)
    {
        if(e.getSource() == userinterface_.go1_)
        {
            println("go was clicked");
            update(userinterface_.seq1_.getValue(), false);
        } else
        if(e.getSource() == userinterface_.minusY_)
        {
            println("minusY was clicked");
            updateLevels(Integer.parseInt(userinterface_.ylevels_.getValue()), -1);
        } else
        if(e.getSource() == userinterface_.plusY_)
        {
            println("plusY was clicked");
            updateLevels(Integer.parseInt(userinterface_.ylevels_.getValue()), 1);
        } else
        if(e.getSource() == userinterface_.goylevels_)
            updateLevels(Integer.parseInt(userinterface_.ylevels_.getValue()), 0);
        else
        if(e.getSource() == userinterface_.minus_)
        {
            println("minus was clicked");
            if(thresholdValue_ - 1 > 0)
                thresholdValue_--;
            else
                thresholdValue_ = 0;
            userinterface_.threshold1_.setValue(str(thresholdValue_));
        } else
        if(e.getSource() == userinterface_.plus_)
        {
            println("plus was clicked");
            thresholdValue_++;
            if(thresholdValue_ > 100)
                thresholdValue_ = 100;
            userinterface_.threshold1_.setValue(str(thresholdValue_));
        } else
        if(e.getSource() == userinterface_.gothres_)
        {
            String temp = userinterface_.threshold1_.getValue();
            thresholdValue_ = parseInt(temp);
        } 
        else if(e.getSource() == userinterface_.save1_) {
            println("ui saving");
            savepdf(pdfoutname_); 
        }

        
        else if(e.getSource() == selectUI_.close_)
        {
            println("ui closing");
            selectUI_.destroy();
        } else
        if(e.getSource() == selectUI_.save_)
        {

            //selectUI.saveAnnot();
        }

    }
    
	public void parseFolder(String myfolder) {
    	fmLookup_ = new Vector();
    	nameLookup_ = new Vector(); 
		try {
			File dir = new File(myfolder);
			FilenameFilter fileFilter = new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.endsWith("dta");
				}
			};
			File[] files = dir.listFiles(fileFilter);
			for (int i=0; i<files.length; i++) {
				File file = files[i];
				File file2=new File(file.toString().substring(0,file.toString().lastIndexOf(".")+1)+"out");
				if(file2.exists()){
					fmLookup_.add(file.toString());
					nameLookup_.add(file.toString().substring(file.toString().lastIndexOf("\\")+1,file.toString().lastIndexOf(".")));
				}
			}
		} catch (Exception e) {
			println("Folder doesn't exist!");
		}
	}
	
    private String chooseFolder(String desc, String default_dir){
    	System.out.print("\n"+desc);
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    	String instr = null;
        try {
        	instr = br.readLine();
        } catch (IOException ioe) {
        	instr="";
        	System.out.println("IO error trying to read your input!");
        }
        if(instr.length()==0)
        	return default_dir;
        else
        	return instr;
    }
    
    public void savepdf(String s2)
    {
        //String tmp = (new StringBuilder(String.valueOf(s2))).append(".pdf").toString();
        //println("saving");
        //println(tmp);
        //beginRecord("processing.pdf.PGraphicsPDF", tmp);
        //palette.draw();
        //legend.draw();
        //collection[0].draw(); 
        
        //endRecord();
		//size(1024, 768, PDF, pdfoutname);
		//savepdfseries(g);
    	String outputfolder=System.getProperty("user.home")+System.getProperty("file.separator")+"Desktop"+System.getProperty("file.separator")+"output"+System.getProperty("file.separator");
    	String inputfolder=System.getProperty("user.home")+System.getProperty("file.separator")+"Desktop"+System.getProperty("file.separator")+"MSdata"+System.getProperty("file.separator");
    	
    	
    	if(Config.PDF_disk){
    		if(infold.equals(""))
    			parseFolder(chooseFolder("Data Input Folder, default: "+inputfolder+"\n[press enter to accept default or type a new directory]: ",inputfolder)); 
    		else
    			parseFolder(infold);
    		println(fmLookup_.size()+" spectra to process..."); 
    		if(outfold.equals(""))
    			outputfolder=chooseFolder("PDF Output Folder, default: "+outputfolder+"\n[press enter to accept default or type a new directory]: ",outputfolder);
    		else
    			outputfolder=outfold;
    		if(!outputfolder.endsWith(System.getProperty("file.separator"))){
    			outputfolder+=System.getProperty("file.separator");
    		}
    		
    	}else{
    		if(this.getOsName()==1)
    			parseCounter("c:\\counters.txt");  
    		if(this.getOsName()==3)  
    			parseCounter(System.getProperty("user.home")+System.getProperty("file.separator")+"Desktop"+System.getProperty("file.separator")+"counters.txt"); 
    			
    	}
	    //currentdb = new database();
	    //gfunk = new graphfunk();
	        
	    //calculate = new calculateTheory(vmsdb_pdf.this, sequence);
		experiment pdfcollection[] = new experiment[fmLookup_.size()]; 
        
		//noLoop();
		
    
		//	PGraphicsPDF pdf = (PGraphicsPDF)g;
		
		processing.core.PGraphics pdf = createGraphics(1024, 768, PDF, s2);

		PGraphicsPDF pdf1 = (PGraphicsPDF)pdf;

		
		//PGraphicsPDF pdf4 = (PGraphicsPDF) g;
		//pdf1.beginDraw();
		size(1024, 768);
		List<String> pdfs=new ArrayList<String>();;
		for (int i = 0; i < fmLookup_.size(); i++) {
			String t;
			println((i+1) + "   " + nameLookup_.get(i).toString());

			if(Config.PDF_disk){				
				t=outputfolder+nameLookup_.get(i).toString()+".pdf";
				pdfs.add(t);
			}else{
				t=filterABC(nameLookup_.get(i).toString(), 30);
				t=outputfolder+(t.length()==0?"":(t+"_"))+fmLookup_.get(i).toString()+".pdf";
				pdfs.add(t);
			}
			beginRecord(PDF, t);

			spec_.parse(fmLookup_.get(i).toString());  
			if(Config.PDF_disk){
				annot_.setIdentifier(nameLookup_.get(i).toString()+".annot");
			}else{
				annot_.setIdentifier(fmLookup_.get(i).toString());
			}
			pdfcollection[i] = new experiment(vmsdb_pdf.this, calculate_, userinterface_,spec_, annot_, 0);

			userinterface_.setSequence(pdfcollection[i].sequence_);
			if(nameLookup_.get(i).toString()!="" && !Config.PDF_disk)
				pdfcollection[i].legend_.setProteinName(nameLookup_.get(i).toString());
			pdfcollection[i].draw();
			endRecord();
			pdfcollection[i]=null;
			//pdf1.nextPage();  // tell it to go to the next page
		}
		if(fmLookup_.size()>0){
			String s=Config.PDF_disk?nameLookup_.get(0).toString():"myExp_Spectra";
			String output=outputfolder+filterABC(s.indexOf(".")>0?s.substring(0,s.indexOf(".")):s,10)+"_combined.pdf";
			System.out.println("\nConcat PDFs to "+output);
			concatPDFs(pdfs, output, true);
	        Iterator<String> iteratorPDFs = pdfs.iterator();
	        while (iteratorPDFs.hasNext()) {
	        	File f=new File(iteratorPDFs.next());
	        	f.delete();
	       }
		}
		System.out.println("................................");
		System.out.println("................................");
		System.out.println("............Job Done!...........");
		System.out.println("................................");
		System.out.println("................................");
		//pdf1.dispose(); 
		//pdf1.endDraw();
		System.exit(0);
    }
    
    public static void concatPDFs(List<String> pdfs, String output, boolean paginate) {
    	Rectangle pageSize = new Rectangle(1024, 768);
        Document document = new Document(pageSize);
        OutputStream outputStream=null;
        Collections.sort(pdfs, String.CASE_INSENSITIVE_ORDER);
        try {
          Iterator<String> iteratorPDFs = pdfs.iterator();

          outputStream = new FileOutputStream(output);
          PdfWriter writer = PdfWriter.getInstance(document, outputStream);

          document.open();
          BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
          PdfContentByte cb = writer.getDirectContent(); // Holds the PDF data

          PdfImportedPage page;
          int currentPageNumber = 0;
          int pageOfCurrentReaderPDF = 0;

          // Loop through the PDF files and add to the output.
          while (iteratorPDFs.hasNext()) {
        	  
	          String pdf = iteratorPDFs.next();
	          PdfReader pdfReader = new PdfReader(pdf);

            // Create a new page in the target for each source page.
            while (pageOfCurrentReaderPDF < pdfReader.getNumberOfPages()) {
              document.newPage();
              pageOfCurrentReaderPDF++;
              currentPageNumber++;
              page = writer.getImportedPage(pdfReader, pageOfCurrentReaderPDF);
              cb.addTemplate(page, 0, 0);

              // Code for pagination.
              if (paginate) {
                cb.beginText();
                cb.setFontAndSize(bf, 13);
                cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "" + currentPageNumber + " - " + pdf.substring(1+pdf.lastIndexOf((System.getProperty( "file.separator" )))), 520, 5, 0);
                cb.endText();
              }
            }
            pageOfCurrentReaderPDF = 0;
          }
          outputStream.flush();
          document.close();
          outputStream.close();
        } catch (Exception e) {
          e.printStackTrace();
        } finally {
          if (document.isOpen())
            document.close();
          try {
            if (outputStream != null)
              outputStream.close();
          } catch (IOException ioe) {
            ioe.printStackTrace();
          }
        }
      }
    
    
    public String filterABC(String s, int n){
    	String w="";
    	for(int i=0;i<s.length();i++){
    		int k=(int)(s.charAt(i));
    		if((k>=48&&k<=57)||(k>=65&&k<=90)||(k>=97&&k<=122))
    			w=w + String.valueOf(s.charAt(i));
    		if(i>n)
    			break;
    	}
    	return w;
    }
    
    public void update(String seq1, boolean thres)
    {
        if(!thres)
            collection_[0].update(seq1);
    }

    public void updateLevels(int temp1, int newlevel)
    {
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
        return thresholdValue_;
    }

    public void setThreshold(int newthres)
    {
        thresholdValue_ = newthres;
    }

    public void drawDashedLine(int x1, int y1, int x2, int y2, double dashlength, double spacelength, String colorcode)
    {
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

    public static void main(String args[])
    {
        if(args.length==2){
        	infold=args[0];
        	println(infold);
        	outfold=args[1];
        	println(outfold);
        }
        PApplet.main(new String[] {
            "vmsdb_pdf"
        });
    }
    
    public static int getOsName() {
        int os = -1;
        if (System.getProperty("os.name").toLowerCase().indexOf("windows") > -1) {
          os = 1;
        } else if (System.getProperty("os.name").toLowerCase().indexOf("linux") > -1) {
          os = 2;
        } else if (System.getProperty("os.name").toLowerCase().indexOf("mac") > -1) {
          os = 3;
        }

        return os;
    }
    
	static String infold="", outfold="";
    private static final long serialVersionUID_ = 1L;
    Vector nameLookup_; 
    Vector fmLookup_; 
    JFileChooser fc_;
    String jcountername_;
    String pdfoutname_;
    PFont font_;
    PFont arial2_;
    PFont impact_;
    PFont tickFont_;
    annotation annot_;
    Spectrum spec_;
}
