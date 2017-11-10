//represents a single daughter ion in the MSMS spectra

public class TheoreticalIon {
	double massError_; 	//mass error
	String ionType_;  //represents ion type (ie: m,b,y,x,c,a,z)
	int charge_; 	//equals 1 or 2
	String minus_; //fragment (functional group)that is missing or added to polypeptide
	int aapos_; 
	boolean selected_; 
	String annot_; 	//annotation initialized as null
	
	TheoreticalIon (String name1, int charge1, double diff1, String mod1, int aapos1, boolean select1) {
		massError_ = diff1;
		ionType_ = name1; 
		//charge = charge1;
		charge_ = charge1;
		minus_ = mod1;
		aapos_ = aapos1;
		selected_ = select1; 
	}
	
	//returns the ion type for this instantiation of theoryMS
	public String getIonType(){
		return ionType_+minus_;
	}
	
	//returns the relative probability rank of the current ion type and fragment loss
	//where 1 is the most likely and 5 is least
	//5 is also returned when there is no match
	public double getIonRank(){
		//mass error is added to probability rank
		Double error;
		if(massError_>=0){
			error = massError_;
		}
		else{	//negative mass error
			error = massError_*-1;
		}
		//M ion
		if(ionType_.equals("M")&&minus_.equals("")){
			return 1+error;
		}
		//b, y ion
		if((ionType_.charAt(0)=='b'||ionType_.charAt(0)=='y')&&minus_.equals("")){
			return 2+error;
		}
		//M minus something
		if(ionType_.equals("M")){
			return 3+error;
		}
		//b, y ion minus something
		if(ionType_.charAt(0)=='b'||ionType_.charAt(0)=='y'){
			return 4+error;
		}
		//a,c,x,z
		else{
			return 5+error;
		}		
	}
}