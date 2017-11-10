// main class associated w/ each specific spectra (creates legend, database calculations, etc

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import vmsdb.AAlegend;

public class experiment
{

    experiment(vmsdb v1,  calculateTheory calculate1,  UI ui2, Spectrum spec1, annotation annot1, int mid1)
    {
    	// constructor
    	annot_=annot1;
    	spec_=spec1;
        molecularWeight_ = 0;
        molecularWeightE_ = 0;
        id_ = "";
        chargeState_ = 0;
        sequence_ = "";
        current_selection_ = 0;
        ctermString_ = "";
        proteinname_ = "";
        calculate_ = calculate1;
        vmsdb_ = v1;
        userinterface_ = ui2;
        mid_ = mid1;
        palette_ = new mspalette(v1, 3, 3, 1000, 650, this);
        getMassDB2("");
        calculate_.xmass_ = calculate_.calcMass_x(calculate_.peptide_, calculate_.charge_);
        calculate_.ymass_ = calculate_.calcMass_y(calculate_.peptide_, calculate_.charge_);
        calculate_.zmass_ = calculate_.calcMass_z(calculate_.peptide_, calculate_.charge_);
        calculate_.amass_ = calculate_.calcMass_a(calculate_.peptide_, calculate_.charge_);
        calculate_.bmass_ = calculate_.calcMass_b(calculate_.peptide_, calculate_.charge_);
        calculate_.cmass_ = calculate_.calcMass_c(calculate_.peptide_, calculate_.charge_);
        buildPeaks();
        setAnnot();
        legend_ = new AAlegend(AA_, vmsdb_, userinterface_, 3, v1.height - 100, this);
        originalAA_ = AA_;
        legend_.setSpectra(p1_);
        palette_.setSpectra(p1_);
        updateSpectra(p1_);
    }

    public int changePhos()
    {
    	// returns current phosphorlyation position
        return legend_.change_phos_;
    }

    public void leftPhos()
    {
    	// calculates left possible AA phosphorlyation (needs to be S, Y, or T)
        for(int i = AA_.size() - 1; i >= 0; i--)
        {
            int temp = legend_.change_phos_ + i;
            temp %= AA_.size();
            String test = AA_.get(temp).toString();
            if(test.charAt(0) == 'S')
            {
                legend_.change_phos_ = temp;
                break;
            }
            if(test.charAt(0) == 'Y')
            {
                legend_.change_phos_ = temp;
                break;
            }
            if(test.charAt(0) != 'T')
                continue;
            legend_.change_phos_ = temp;
            break;
        }

        String temp = "";
        for(int i = 0; i < AA_.size(); i++)
            if(legend_.ogPhos() == legend_.change_phos_)
                temp = (new StringBuilder(String.valueOf(temp))).append(originalAA_.get(i)).toString();
            else
            if(i == legend_.ogPhos() && i == legend_.change_phos_)
                temp = (new StringBuilder(String.valueOf(temp))).append(AA_.get(i)).toString();
            else
            if(i == legend_.ogPhos())
            {
                String prev = AA_.get(i).toString();
                temp = (new StringBuilder(String.valueOf(temp))).append(prev.charAt(0)).toString();
            } else
            if(i == legend_.change_phos_)
            {
                temp = (new StringBuilder(String.valueOf(temp))).append(AA_.get(i)).toString();
                temp = (new StringBuilder(String.valueOf(temp))).append("*").toString();
            } else
            {
                temp = (new StringBuilder(String.valueOf(temp))).append(originalAA_.get(i)).toString();
            }

        AA_ = updatePhosphoSequence(temp);
        updatePhosphoRest();
    }

    public void rightPhos()
    {
//    	 calculates right possible AA phosphorlyation (needs to be S, Y, or T)
        for(int i = 1; i < AA_.size(); i++)
        {
            int temp = legend_.change_phos_ + i;
            temp %= AA_.size();
            String test = AA_.get(temp).toString();
            if(test.charAt(0) == 'S')
            {
                legend_.change_phos_ = temp;
                break;
            }
            if(test.charAt(0) == 'Y')
            {
                legend_.change_phos_ = temp;
                break;
            }
            if(test.charAt(0) != 'T')
                continue;
            legend_.change_phos_ = temp;
            break;
        }

        String temp = "";
        for(int i = 0; i < AA_.size(); i++)
            if(legend_.ogPhos() == legend_.change_phos_)
                temp = (new StringBuilder(String.valueOf(temp))).append(originalAA_.get(i)).toString();
            else
            if(i == legend_.ogPhos() && i == legend_.change_phos_)
                temp = (new StringBuilder(String.valueOf(temp))).append(AA_.get(i)).toString();
            else
            if(i == legend_.ogPhos())
            {
                String prev = AA_.get(i).toString();
                temp = (new StringBuilder(String.valueOf(temp))).append(prev.charAt(0)).toString();
            } else
            if(i == legend_.change_phos_)
            {
                temp = (new StringBuilder(String.valueOf(temp))).append(AA_.get(i)).toString();
                temp = (new StringBuilder(String.valueOf(temp))).append("*").toString();
            } else
            {
                temp = (new StringBuilder(String.valueOf(temp))).append(originalAA_.get(i)).toString();
            }

        AA_ = updatePhosphoSequence(temp);
        updatePhosphoRest();
    }

    public Vector updatePhosphoSequence(String newseq)
    {
    	// updates the current sequence
        sequence_ = newseq;
        Vector AA2 = new Vector();
        for(int i = 0; i < sequence_.length(); i++)
            if(i < sequence_.length() - 1)
            {
                Pattern pA = Pattern.compile("[\\*]");
                Matcher m2 = pA.matcher(sequence_.substring(i + 1, i + 2));
                boolean found1 = m2.find();
                if(found1)
                {
                    AA2.add(sequence_.substring(i, i + 2));
                    i++;
                } else
                {
                    AA2.add(sequence_.substring(i, i + 1));
                }
            } else
            {
                AA2.add(sequence_.substring(i, i + 1));
            }

        return AA2;
    }

    public void updatePhosphoRest()
    {
    	// updates the current associated information for the specific spectra and sequence
        calculate_.parseSequ(sequence_);
        calculate_.xmass_ = calculate_.calcMass_x(calculate_.peptide_, calculate_.charge_);
        calculate_.ymass_ = calculate_.calcMass_y(calculate_.peptide_, calculate_.charge_);
        calculate_.zmass_ = calculate_.calcMass_z(calculate_.peptide_, calculate_.charge_);
        calculate_.amass_ = calculate_.calcMass_a(calculate_.peptide_, calculate_.charge_);
        calculate_.bmass_ = calculate_.calcMass_b(calculate_.peptide_, calculate_.charge_);
        calculate_.cmass_ = calculate_.calcMass_c(calculate_.peptide_, calculate_.charge_);
        rebuildPeaks();
        legend_.setSpectra(p1_);
        legend_.setSequence(AA_);
        palette_.setSpectra(p1_);
    }

    public void rebuildPeaks()
    {
    	// rebuilds the base information of matched theoretical peaks to actual peaks
        double thres1 = ((double)vmsdb_.thresholdValue_ / 100D) * palette_.abundmax_;
        for(int i = 0; i < p1_.length; i++)
            if(p1_[i].ycor_ > thres1)
            {
                p1_[i].theorym_ = null;
                p1_[i].theorym_ = calculate_.fragmentFind(p1_[i].xcor_);
            }

    }

    public void setYLevels(int temp1) {
    	// sets the number of Y axes
        palette_.setYLevels(temp1);
    }

    public int getYLevels() {
    	// returns the number of current Y axes
        return palette_.getYLevels();
    }

    public void setAnnot(){
    	// sets current annotation from database
    	String[][] rs=annot_.getAnnot();
    	for(int j=0; j<rs.length; j++){
            for(int i = 0; i < p1_.length; i++)
                if((double)vmsdb.abs((float)(p1_[i].xcor_ - Double.parseDouble(rs[j][0]))) < 0.0001D)
                    if(rs[j][1] != null)
                        p1_[i].annot_ = rs[j][1];
            
           //TODO: re-check peak assignment
    	}    	
    }

    public void updateSpectra(Peak temp1[])
    {
    	//updates the current associated spectra
        p1_ = temp1;
        palette_.setSpectra(p1_);
        legend_.setSpectra(p1_);
    }

    public void update(String newseq)
    {
    	// updates all corresponding information
        sequence_ = newseq;
        getMassDB2(sequence_);
        calculate_.parseSequ(sequence_);
        calculate_.xmass_ = calculate_.calcMass_x(calculate_.peptide_, calculate_.charge_);
        calculate_.ymass_ = calculate_.calcMass_y(calculate_.peptide_, calculate_.charge_);
        calculate_.zmass_ = calculate_.calcMass_z(calculate_.peptide_, calculate_.charge_);
        calculate_.amass_ = calculate_.calcMass_a(calculate_.peptide_, calculate_.charge_);
        calculate_.bmass_ = calculate_.calcMass_b(calculate_.peptide_, calculate_.charge_);
        calculate_.cmass_ = calculate_.calcMass_c(calculate_.peptide_, calculate_.charge_);
        buildPeaks();
        legend_.setSpectra(p1_);
        legend_.setSequence(AA_);
        palette_.setSpectra(p1_);
    }

    public void draw()
    {
    	// calls children to draw legend and palette
        legend_.draw();
        palette_.draw();
    }

    public void setPaletteXY(int x1, int y1)
    {
    	// sets palette width and height
        palette_.setXY(x1, y1);
    }

    public void setPaletteXsize(int x1)
    {
    	// sets x palette size
        palette_.setXsize(x1);
    }

    public void setPaletteYsize(int y1)
    {
    	// sets palettes height
        palette_.setYsize(y1);
    }

    public void setLegendXY(int x1, int y1)
    {
    	// sets legend width and height
        legend_.setXY(x1, y1);
    }

    public void setLegendXsize(int x1)
    {
    	// sets legend's width
        legend_.setXsize(x1);
    }

    public void setLegendYsize(int y1)
    {
    	// sets legends height
        legend_.setYsize(y1);
    }

    public void setSelection(int t1)
    {
    	// sets selected peak
        current_selection_ = t1;
    }

    public int getSelection()
    {
    	// returns the currently selected peak
        return current_selection_;
    }

    public void buildPeaks()
    {
    	// initial function for calculating theoretical fragments 
        p1_ = new Peak[xCor_.size()];
        double thres1 = ((double)vmsdb_.thresholdValue_ / 100D) * palette_.abundmax_;
        for(int i = 0; i < xCor_.size(); i++)
        {
            Float s = Float.valueOf(Float.parseFloat(xCor_.get(i).toString()));
            Float s2 = Float.valueOf(Float.parseFloat(yCor_.get(i).toString()));
            p1_[i] = new Peak(s.floatValue(), s2.floatValue());
            if((double)s2.floatValue() > thres1)
                p1_[i].theorym_ = calculate_.fragmentFind(s.floatValue());
        }
    }

    void getMassDB2(String change1)
    {
    	// basic function for reading in the data from database
    	// raw peak values and sequest parsing
    	
    	vmsdb_.println("setup spectral visualization");
    	
        yCor_ = new Vector();
        xCor_ = new Vector();
        AA_ = new Vector();
        
        
        //get assigned sequence from annotation.java
        
        sequence_=annot_.getAssignedSequence();
        if(sequence_=="")
        	sequence_=spec_.trimmedSequence;
        id_=spec_.protein_accession;
        id_type_=spec_.protein_accession_type;

        proteinname_=vmsdb_.pname_!=""?vmsdb_.pname_:spec_.protein_name;

        priScore_=spec_.pri_score;
        priScoreName_=spec_.pri_score_name;
        molecularWeight_=spec_.exp_mass;
        molecularWeightE_=spec_.theo_mass;
        chargeState_=spec_.charge;
        ctermString_=spec_.modstring;
        
        
        
        
        
        
        if(change1 != "")
        {
            sequence_ = change1;
        }
        for(int i = 0; i < sequence_.length(); i++)
            if(i < sequence_.length() - 1)
            {
                Pattern pA = Pattern.compile("[[\\*][\\#][\\%][\\^][\\@][\\~][\\$][\\&][\\[][\\]]]");
                Matcher m2 = pA.matcher(sequence_.substring(i + 1, i + 2));
                boolean found1 = m2.find();
                if(found1)
                {
                    AA_.add(sequence_.substring(i, i + 2));
                    i++;
                } else
                {
                    AA_.add(sequence_.substring(i, i + 1));
                }
            } else
            {
                AA_.add(sequence_.substring(i, i + 1));
            }

        
        
        xCor_=spec_.xCor_;
        yCor_=spec_.yCor_;        
        
        
        calculate_.parseMods(ctermString_);
        calculate_.charge_ = chargeState_;
        calculate_.parseSequ(sequence_);
        //vmsdb.println("get mass db 2");
    }

    public String parseInput(String s)
    {
    	// helper function for getmass to parse in data
        Pattern p1 = Pattern.compile("[a-z[A-Z][0-9][\\.][ ][\\+][\\=][\\^][\\#][\\@][\\$][\\!][\\_][\\~][\\;][\\'][\\,][\\*][\\%][\\(][\\)][\\[][\\]][\\:][\\-]]*");
        Matcher m1 = p1.matcher(s.toString());
    //    boolean found1 = m1.find();	//never read
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
    public String decodeURL(String s)
    {
    	return s.replaceAll("%23", "#").replaceAll("%2A", "*").replaceAll("%2B", "+").replaceAll("%5E", "^").replaceAll("%7E", "~").replaceAll("%24", "\\$").replaceAll("%3D", "=");

    }
    // public variables

    Vector yCor_;
    Vector xCor_;
    Vector AA_;
    Vector originalAA_;
    AAlegend legend_;
    mspalette palette_;
    calculateTheory calculate_;


    vmsdb vmsdb_;


    double molecularWeight_;
    double molecularWeightE_;
    String id_,id_type_;
    int chargeState_;
    Peak p1_[];	//array of mass peaks
    String sequence_;
    UI userinterface_;
    int current_selection_;
    int mid_;	//not called in class
    String ctermString_;
    String proteinname_, priScoreName_;
    double priScore_;
    
    Spectrum spec_;
    annotation annot_;
    
}
