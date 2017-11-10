//class for calculating theoretical x,y,z,a,b,c spectra (from kebing's mass2.php script)

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class calculateTheory {

	calculateTheory(vmsdb vmain1, String sequence1){
		// constructor
		masstype_ = "mono";
		charge_ = 0;
		tolerance_ = 0.5D;
		fmass_ = 0.0D;
		rdMass_ = new double[22];
		rdName_ = new String[22];
		sequence_ = sequence1;
		vmain_ = vmain1;
		setup();
		lossH2O_ = new String[4];
		lossH2O_[0] = "T";	//Threonine minus water
		lossH2O_[1] = "S";	//Serine minus water
		lossH2O_[2] = "D";	//Aspartate minus water
		lossH2O_[3] = "E";	//Glutamate minus water
		lossNH3_ = new String[4];
		lossNH3_[0] = "K";	//Lysine minus NH3
		lossNH3_[1] = "R";	//Argenine
		lossNH3_[2] = "Q";  //Glutamine
		lossNH3_[3] = "N";	//Asparagine
		lossHPO3_ = new String[3];
		lossHPO3_[0] = "T*";	//Threonine minus HPO3
		lossHPO3_[1] = "Y*";	//Tyrosine
		lossHPO3_[2] = "S*";	//Serine
		lossH3PO4_ = new String[2];
		lossH3PO4_[0] = "T*";	//Threonine minus H3PO4
		lossH3PO4_[1] = "S*";	//Serine
		psbLsH2O_ = new Vector();
		psbLsNH3_ = new Vector();
		psbLsHPO3_ = new Vector();
		psbLsH3PO4_ = new Vector();
		stamods_ = new Vector();
		dynmods_ = new Vector();
		peptide_ = new String[sequence_.length()];
		for(int i = 0; i < sequence_.length() - 1; i++)
			peptide_[i] = sequence_.substring(i, i + 1);

		amass_ = bmass_ = cmass_ = xmass_ = ymass_ = zmass_ = new double[charge_][peptide_.length];
	}

	public void setup()
	{
		// setting up theoretical masses for amino acids (also parsed in from sequest output) 

		if(masstype_ == "")
			masstype_ = "mono";
		if(masstype_ == "mono")	{
			//amino acids and weights
			// if set to mono masses
			rdName_[0] = "G";
			rdMass_[0] = 57.021464000000002D;
			rdName_[1] = "A";
			rdMass_[1] = 71.037114000000003D;
			rdName_[2] = "S";
			rdMass_[2] = 87.032028999999994D;
			rdName_[3] = "P";
			rdMass_[3] = 97.052763999999996D;
			rdName_[4] = "V";
			rdMass_[4] = 99.068414000000004D;
			rdName_[5] = "T";
			rdMass_[5] = 101.047679D;
			rdName_[6] = "C";
			rdMass_[6] = 103.009184D;
			rdName_[7] = "I";
			rdMass_[7] = 113.084064D;
			rdName_[8] = "L";
			rdMass_[8] = 113.084064D;
			rdName_[9] = "N";
			rdMass_[9] = 114.042928D;
			rdName_[10] = "D";
			rdMass_[10] = 115.026944D;
			rdName_[11] = "Q";
			rdMass_[11] = 128.05857800000001D;
			rdName_[12] = "K";
			rdMass_[12] = 128.09496300000001D;
			rdName_[13] = "E";
			rdMass_[13] = 129.04259400000001D;
			rdName_[14] = "M";
			rdMass_[14] = 131.04048399999999D;
			rdName_[15] = "H";
			rdMass_[15] = 137.05891199999999D;
			rdName_[16] = "F";
			rdMass_[16] = 147.06841399999999D;
			rdName_[17] = "R";
			rdMass_[17] = 156.101111D;
			rdName_[18] = "Y";
			rdMass_[18] = 163.06332900000001D;
			rdName_[19] = "W";
			rdMass_[19] = 186.07900000000001D;
			rdName_[20] = "CT";		//C terminus
			rdMass_[20] = 0.0D;
			rdName_[21] = "NT";		//N terminus
			rdMass_[21] = 0.0D;
		} else if(masstype_ == "ave") {
			//amino acids and weights
			// if masstype is average
			rdName_[0] = "G";
			rdMass_[0] = 57.052D;
			rdName_[1] = "A";
			rdMass_[1] = 71.078999999999994D;
			rdName_[2] = "S";
			rdMass_[2] = 87.078000000000003D;
			rdName_[3] = "P";
			rdMass_[3] = 97.117000000000004D;
			rdName_[4] = "V";
			rdMass_[4] = 99.132999999999996D;
			rdName_[5] = "T";
			rdMass_[5] = 101.105D;
			rdName_[6] = "C";
			rdMass_[6] = 103.14400000000001D;
			rdName_[7] = "I";
			rdMass_[7] = 113.16D;
			rdName_[8] = "L";
			rdMass_[8] = 113.16D;
			rdName_[9] = "N";
			rdMass_[9] = 114.104D;
			rdName_[10] = "D";
			rdMass_[10] = 115.089D;
			rdName_[11] = "Q";
			rdMass_[11] = 128.131D;
			rdName_[12] = "K";
			rdMass_[12] = 128.17400000000001D;
			rdName_[13] = "E";
			rdMass_[13] = 129.11600000000001D;
			rdName_[14] = "M";
			rdMass_[14] = 131.19800000000001D;
			rdName_[15] = "H";
			rdMass_[15] = 137.142D;
			rdName_[16] = "F";
			rdMass_[16] = 147.17699999999999D;
			rdName_[17] = "R";
			rdMass_[17] = 156.18799999999999D;
			rdName_[18] = "Y";
			rdMass_[18] = 163.16999999999999D;
			rdName_[19] = "W";
			rdMass_[19] = 186.21299999999999D;
			rdName_[20] = "CT";		//C terminus
			rdMass_[20] = 0.0D;
			rdName_[21] = "NT";		//N terminus
			rdMass_[21] = 0.0D;
		}
	}

	public boolean parseMods(String modstring)
	{
		// parsing mods from sequest output from database

		// cterm parsing
		Pattern p2 = Pattern.compile("(\\+Cterm-pep)=([0-9\\.]*)");
		Matcher m2 = p2.matcher(modstring);
		if(m2.find())
		{
			String cterm = m2.group();
			Pattern p3 = Pattern.compile("=[0-9[\\.]]*");
			Matcher m3 = p3.matcher(cterm);
			if(m3.find())
			{
				String cterminus = m3.group();
				rdMass_[20] = Double.parseDouble(cterminus.substring(1, cterminus.length()));
			}
		}

		// nterm parsing
		p2 = Pattern.compile("(\\+Nterm-pep)=([0-9\\.]*)");
		m2 = p2.matcher(modstring);
		if(m2.find())
		{
			String nterm = m2.group();
			Pattern p3 = Pattern.compile("=[0-9[\\.]]*");
			Matcher m3 = p3.matcher(nterm);
			if(m3.find())
			{
				String nterminus = m3.group();
				rdMass_[21] = Double.parseDouble(nterminus.substring(1, nterminus.length()));
			}
		}

		//static modifications
		Pattern p27 = Pattern.compile("[A-Z]\\=[0-9[\\.]]*");
		Matcher m28 = p27.matcher(modstring);
		Vector aminoAcidsMods = new Vector();
		String aminoAcidsModifications;
		for(; m28.find(); aminoAcidsMods.add(aminoAcidsModifications))
		{
			aminoAcidsModifications = m28.group();
		}

		for(int r = 0; r < aminoAcidsMods.size(); r++)
		{
			String aminoAcid = aminoAcidsMods.get(r).toString().substring(0, 1);
			double modifiedMass = Double.parseDouble(aminoAcidsMods.get(r).toString().substring(aminoAcidsMods.get(r).toString().indexOf("=") + 1, aminoAcidsMods.get(r).toString().length()));
			for(int i = 0; i < 22; i++)
			{
				if(!rdName_[i].equals(aminoAcid))
					continue;
				rdMass_[i] = modifiedMass;
				break;
			}

		}

		// dynamic modifications
		p2 = Pattern.compile("\\([A-Za-z][A-Za-z[[ ][\\+][\\-][0-9][\\.][\\[][\\]][\\*][\\#][\\%][\\^][\\@][\\$][\\~][\\&]]]*[0-9]\\)");
		m2 = p2.matcher(modstring);
		dynMods_ = new Vector();
		dynMass_ = new Vector();
		double dynmass;
		for(; m2.find(); dynMass_.add(Double.toString(dynmass)))
		{
			String dynmod = m2.group();
			dynmass = 0.0D;
			if(dynmod.indexOf("+") >= 0)
				dynmass = Double.parseDouble(dynmod.substring(dynmod.indexOf("+") + 1, dynmod.length() - 1));
			else
				if(dynmod.indexOf("-") >= 0)
					dynmass = -1D * Double.parseDouble(dynmod.substring(dynmod.indexOf("-") + 1, dynmod.length() - 1));
			dynmod = dynmod.substring(1, dynmod.indexOf(" "));
			String tag = dynmod.substring(dynmod.length() - 1, dynmod.length());
			dynMods_.add(tag);
		}

		return true;
	}

	public boolean isInArray(String array1[], String match){
		// checks to see if a particular AA is in the current AA sequence
		for(int i = 0; i < array1.length; i++)
			if(match.equals(array1[i]))
				return true;

		return false;
	}

	public void parseSequ(String seq)
	{
		// parses sequence and calculates number of possible losses
		psbLsH2O_ = new Vector();
		psbLsNH3_ = new Vector();
		psbLsHPO3_ = new Vector();
		psbLsH3PO4_ = new Vector();
		int c1 = 0;
		String peptideT[] = new String[seq.length()];
		for(int i = 0; i < seq.length(); i++)
		{
			peptideT[i] = seq.substring(i, i + 1);
			String temp = seq.substring(i, i + 1);
			int tc = temp.charAt(0);
			if(tc <= 90 && tc >= 65)
				c1++;
		}

		peptide_ = new String[c1];
		int j = 0;
		for(int i = 0; i < peptideT.length; i++)
			if(peptideT[i].charAt(0) <= 'Z' && peptideT[i].charAt(0) >= 'A' && (i == peptideT.length - 1 || peptideT[i + 1].charAt(0) <= 'Z' && peptideT[i + 1].charAt(0) >= 'A'))
			{
				peptide_[j] = peptideT[i];
				if(isInArray(lossH2O_, peptide_[j]))
					psbLsH2O_.add(Integer.valueOf(j));
				if(isInArray(lossNH3_, peptide_[j]))
					psbLsNH3_.add(Integer.valueOf(j));
				if(isInArray(lossHPO3_, peptide_[j]))
					psbLsHPO3_.add(Integer.valueOf(j));
				if(isInArray(lossH3PO4_, peptide_[j]))
					psbLsH3PO4_.add(Integer.valueOf(j));
				j++;
			} else
			{
				peptide_[j] = (new StringBuilder(String.valueOf(peptideT[i]))).append(peptideT[i + 1]).toString();
				i++;
				if(isInArray(lossH2O_, peptide_[j]))
					psbLsH2O_.add(Integer.valueOf(j));
				if(isInArray(lossNH3_, peptide_[j]))
					psbLsNH3_.add(Integer.valueOf(j));
				if(isInArray(lossHPO3_, peptide_[j]))
					psbLsHPO3_.add(Integer.valueOf(j));
				if(isInArray(lossH3PO4_, peptide_[j]))
					psbLsH3PO4_.add(Integer.valueOf(j));
				j++;
			}
	}

	public double[][] calcMass_x(String peptide1[], int charge)
	{
		// calculates theoretical X peaks for mass spec
		double xmass[][] = new double[charge][peptide1.length + 1];
		double xreturn[][] = new double[charge][peptide1.length];
		int j = 1;
		double dynass = 0.0D;
		double aamass = 0.0D;
		for(int c = charge; c > 0; c--)
		{
			xmass[c - 1][0] = 0.0D;
			j = 0;
			for(int i = peptide1.length - 1; i >= 0; i--)
			{
				dynass = 0.0D;
				aamass = 0.0D;
				j++;
				if(peptide1[i].length() == 2)
				{
					for(int k = 0; k < dynMods_.size(); k++)
					{
						if(!dynMods_.get(k).toString().equals(peptide1[i].substring(1, 2)))
							continue;
						dynass = Double.parseDouble(dynMass_.get(k).toString());
						break;
					}

					for(int m = 0; m < 22; m++)
					{
						if(!rdName_[m].equals(peptide1[i].substring(0, 1)))
							continue;
						aamass = rdMass_[m];
						break;
					}

					xmass[c - 1][j] = xmass[c - 1][j - 1] + aamass + dynass;
				} else
				{
					for(int m = 0; m < 22; m++)
					{
						if(!rdName_[m].equals(peptide1[i].toString()))
							continue;
						aamass = rdMass_[m];
						break;
					}

					xmass[c - 1][j] = xmass[c - 1][j - 1] + aamass;
				}
			}

		}

		for(int c = charge; c > 0; c--)
		{
			for(int i = 1; i < j + 1; i++)
			{
				if(i == j)
					xmass[c - 1][i] = (xmass[c - 1][i] + rdMass_[21]) - 27.994900000000001D;
				xmass[c - 1][i] = (xmass[c - 1][i] + 18.0105D + rdMass_[20] + 27.994900000000001D) / (double)c + 1.0072700000000001D;
			}

		}

		for(int c = 0; c < charge; c++)
			vmsdb.arraycopy(xmass[c], 1, xreturn[c], 0, peptide1.length);

		return xreturn;
	}

	public double[][] calcMass_y(String peptide1[], int charge)
	{
//		calculates theoretical Y peaks for mass spec
		double ymass[][] = new double[charge][peptide1.length + 1];
		double yreturn[][] = new double[charge][peptide1.length];
		int j = 1;
		double dynass = 0.0D;
		double aamass = 0.0D;
		for(int c = charge; c > 0; c--)
		{
			ymass[c - 1][0] = 0.0D;
			j = 0;
			for(int i = peptide1.length - 1; i >= 0; i--)
			{
				dynass = 0.0D;
				aamass = 0.0D;
				j++;
				if(peptide1[i].length() == 2)
				{
					for(int k = 0; k < dynMods_.size(); k++)
					{
						if(!dynMods_.get(k).toString().equals(peptide1[i].substring(1, 2)))
							continue;
						dynass = Double.parseDouble(dynMass_.get(k).toString());
						break;
					}

					for(int m = 0; m < 22; m++)
					{
						if(!rdName_[m].equals(peptide1[i].substring(0, 1)))
							continue;
						aamass = rdMass_[m];
						break;
					}

					ymass[c - 1][j] = ymass[c - 1][j - 1] + aamass + dynass;
				} else
				{
					for(int m = 0; m < 22; m++)
					{
						if(!rdName_[m].equals(peptide1[i].toString()))
							continue;
						aamass = rdMass_[m];
						break;
					}

					ymass[c - 1][j] = ymass[c - 1][j - 1] + aamass;
				}
			}

		}

		for(int c = charge; c > 0; c--)
		{
			for(int i = 1; i < j + 1; i++)
			{
				if(i == j)
					ymass[c - 1][i] = ymass[c - 1][i] + rdMass_[21];
				ymass[c - 1][i] = (ymass[c - 1][i] + 18.0105D + rdMass_[20]) / (double)c + 1.0072700000000001D;
			}

		}

		for(int c = 0; c < charge; c++)
			vmsdb.arraycopy(ymass[c], 1, yreturn[c], 0, peptide1.length);

		return yreturn;
	}

	public double[][] calcMass_z(String peptide1[], int charge)
	{
//		calculates theoretical Z peaks for mass spec
		double zmass[][] = new double[charge][peptide1.length + 1];
		double zreturn[][] = new double[charge][peptide1.length];
		int j = 1;
		double dynass = 0.0D;
		double aamass = 0.0D;
		for(int c = charge; c > 0; c--)
		{
			zmass[c - 1][0] = 0.0D;
			j = 0;
			for(int i = peptide1.length - 1; i >= 0; i--)
			{
				dynass = 0.0D;
				aamass = 0.0D;
				j++;
				if(peptide1[i].length() == 2)
				{
					for(int k = 0; k < dynMods_.size(); k++)
					{
						if(!dynMods_.get(k).toString().equals(peptide1[i].substring(1, 2)))
							continue;
						dynass = Double.parseDouble(dynMass_.get(k).toString());
						break;
					}

					for(int m = 0; m < 22; m++)
					{
						if(!rdName_[m].equals(peptide1[i].substring(0, 1)))
							continue;
						aamass = rdMass_[m];
						break;
					}

					zmass[c - 1][j] = zmass[c - 1][j - 1] + aamass + dynass;
				} else
				{
					for(int m = 0; m < 22; m++)
					{
						if(!rdName_[m].equals(peptide1[i].toString()))
							continue;
						aamass = rdMass_[m];
						break;
					}

					zmass[c - 1][j] = zmass[c - 1][j - 1] + aamass;
				}
			}

		}

		for(int c = charge; c > 0; c--)
		{
			for(int i = 1; i < j + 1; i++)
			{
				if(i == j)
					zmass[c - 1][i] = zmass[c - 1][i] + rdMass_[21] + 15.0108D;
				zmass[c - 1][i] = ((zmass[c - 1][i] + 18.0105D + rdMass_[20]) - 15.0108D) / (double)c + 1.0072700000000001D;
			}

		}

		for(int c = 0; c < charge; c++)
			vmsdb.arraycopy(zmass[c], 1, zreturn[c], 0, peptide1.length);

		return zreturn;
	}

	public double[][] calcMass_a(String peptide1[], int charge)
	{
//		calculates theoretical A peaks for mass spec
		double amass[][] = new double[charge][peptide1.length + 1];
		double areturn[][] = new double[charge][peptide1.length];
		int j = 1;
		double dynass = 0.0D;
		double aamass = 0.0D;
		for(int c = charge; c > 0; c--)
		{
			amass[c - 1][0] = 0.0D;
			j = 0;
			for(int i = 0; i <= peptide1.length - 1; i++)
			{
				dynass = 0.0D;
				aamass = 0.0D;
				j++;
				if(peptide1[i].length() == 2)
				{
					for(int k = 0; k < dynMods_.size(); k++)
					{
						if(!dynMods_.get(k).toString().equals(peptide1[i].substring(1, 2)))
							continue;
						dynass = Double.parseDouble(dynMass_.get(k).toString());
						break;
					}

					for(int m = 0; m < 22; m++)
					{
						if(!rdName_[m].equals(peptide1[i].substring(0, 1)))
							continue;
						aamass = rdMass_[m];
						break;
					}

					amass[c - 1][j] = amass[c - 1][j - 1] + aamass + dynass;
				} else
				{
					for(int m = 0; m < 22; m++)
					{
						if(!rdName_[m].equals(peptide1[i].toString()))
							continue;
						aamass = rdMass_[m];
						break;
					}

					amass[c - 1][j] = amass[c - 1][j - 1] + aamass;
				}
			}

		}

		for(int c = charge; c > 0; c--)
		{
			for(int i = 1; i < j + 1; i++)
			{
				if(i == j)
					amass[c - 1][i] = amass[c - 1][i] + rdMass_[21] + 18.0105D + 27.994900000000001D;
				amass[c - 1][i] = ((amass[c - 1][i] + rdMass_[20]) - 27.994900000000001D) / (double)c + 1.0072700000000001D;
			}

		}

		for(int c = 0; c < charge; c++)
			vmsdb.arraycopy(amass[c], 1, areturn[c], 0, peptide1.length);

		return areturn;
	}

	public double[][] calcMass_b(String peptide1[], int charge)
	{
//		calculates theoretical B peaks for mass spec
		double bmass[][] = new double[charge][peptide1.length + 1];
		double breturn[][] = new double[charge][peptide1.length];
		int j = 1;
		double dynass = 0.0D;
		double aamass = 0.0D;
		for(int c = charge; c > 0; c--)
		{
			bmass[c - 1][0] = 0.0D;
			j = 0;
			for(int i = 0; i <= peptide1.length - 1; i++)
			{
				dynass = 0.0D;
				aamass = 0.0D;
				j++;
				if(peptide1[i].length() == 2)
				{
					for(int k = 0; k < dynMods_.size(); k++)
					{
						if(!dynMods_.get(k).toString().equals(peptide1[i].substring(1, 2)))
							continue;
						dynass = Double.parseDouble(dynMass_.get(k).toString());
						break;
					}

					for(int m = 0; m < 22; m++)
					{
						if(!rdName_[m].equals(peptide1[i].substring(0, 1)))
							continue;
						aamass = rdMass_[m];
						break;
					}

					bmass[c - 1][j] = bmass[c - 1][j - 1] + aamass + dynass;
				} else
				{
					for(int m = 0; m < 22; m++)
					{
						if(!rdName_[m].equals(peptide1[i].toString()))
							continue;
						aamass = rdMass_[m];
						break;
					}

					bmass[c - 1][j] = bmass[c - 1][j - 1] + aamass;
				}
			}

		}

		for(int c = charge; c > 0; c--)
		{
			for(int i = 1; i < j + 1; i++)
			{
				if(i == j)
					bmass[c - 1][i] = bmass[c - 1][i] + rdMass_[21] + 18.0105D;
				bmass[c - 1][i] = (bmass[c - 1][i] + rdMass_[20]) / (double)c + 1.0072700000000001D;
			}

		}

		for(int c = 0; c < charge; c++)
			vmsdb.arraycopy(bmass[c], 1, breturn[c], 0, peptide1.length);

		return breturn;
	}

	public double[][] calcMass_c(String peptide1[], int charge)
	{
//		calculates theoretical C peaks for mass spec
		double cmass[][] = new double[charge][peptide1.length + 1];
		double creturn[][] = new double[charge][peptide1.length];
		int j = 1;
		double dynass = 0.0D;
		double aamass = 0.0D;
		for(int c = charge; c > 0; c--)
		{
			cmass[c - 1][0] = 0.0D;
			j = 0;
			for(int i = 0; i <= peptide1.length - 1; i++)
			{
				dynass = 0.0D;
				aamass = 0.0D;
				j++;
				if(peptide1[i].length() == 2)
				{
					for(int k = 0; k < dynMods_.size(); k++)
					{
						if(!dynMods_.get(k).toString().equals(peptide1[i].substring(1, 2)))
							continue;
						dynass = Double.parseDouble(dynMass_.get(k).toString());
						break;
					}

					for(int m = 0; m < 22; m++)
					{
						if(!rdName_[m].equals(peptide1[i].substring(0, 1)))
							continue;
						aamass = rdMass_[m];
						break;
					}

					cmass[c - 1][j] = cmass[c - 1][j - 1] + aamass + dynass;
				} else
				{
					for(int m = 0; m < 22; m++)
					{
						if(!rdName_[m].equals(peptide1[i].toString()))
							continue;
						aamass = rdMass_[m];
						break;
					}

					cmass[c - 1][j] = cmass[c - 1][j - 1] + aamass;
				}
			}

		}

		for(int c = charge; c > 0; c--)
		{
			for(int i = 1; i < j + 1; i++)
			{
				if(i == j)
					cmass[c - 1][i] = (cmass[c - 1][i] + rdMass_[21] + 18.0105D) - 15.0108D;
				cmass[c - 1][i] = (cmass[c - 1][i] + rdMass_[20] + 15.0108D) / (double)c + 1.0072700000000001D;
			}

		}

		for(int c = 0; c < charge; c++)
			vmsdb.arraycopy(cmass[c], 1, creturn[c], 0, peptide1.length);

		return creturn;
	}

	//adds new peak to input array from input parameters 
	//creates new TheoreticalIon instance
	//lablel1 is the ion type
	//the returned theoryMS[] is in the order that is seen in the UI list
	public TheoreticalIon[] addFrag(String label1, int charge1, double diff, String mod1, TheoreticalIon temp1[], int aapos1, 
			boolean select1)
	{
		// adds possible theoretical spectra to a single matrix to return to the visualization
		TheoreticalIon retTemp[];
		if(temp1 != null)
		{
			retTemp = new TheoreticalIon[temp1.length + 1];
			TheoreticalIon t1 = new TheoreticalIon(label1, charge1, diff, mod1, aapos1, select1);
			retTemp[temp1.length] = t1;
			vmsdb.arraycopy(temp1, 0, retTemp, 0, temp1.length);
		} else
		{
			retTemp = new TheoreticalIon[1];
			retTemp[0] = new TheoreticalIon(label1, charge1, diff, mod1, aapos1, select1);
		}
		return retTemp;
	}

	//returns a list of possible fragments for a given mass ordered from most likely to least
	//runs when program starts
	public TheoreticalIon[] fragmentFind(double fmass)
	{
		// main method for calculating possible theoretical spectra
		double massH2O = 18.0105D;
		double massNH3 = 17.026499999999999D;
		double massHPO3 = 79.966300000000004D;
		double massH3PO4 = 97.976799999999997D;
	//	int match = 0;	//never read
	//	theorystruct fragment = new theorystruct(charge_);	//never read
		TheoreticalIon frag[] = (TheoreticalIon[])null;
		int pnum = peptide_.length;
		Vector subPsbLsH2O = new Vector();
		Vector subPsbLsNH3 = new Vector();
		Vector subPsbLsHPO3 = new Vector();
		Vector subPsbLsH3PO4 = new Vector();

		// possible M, Y, or B ions
		for(int ch = 1; ch <= charge_; ch++)
		{
			for(int subtag = 0; subtag < pnum; subtag++)
			{
				double diff = bmass_[ch - 1][subtag] - fmass;
				if(diff >= -tolerance_ && diff <= tolerance_)
					if(frag != null)
					{
						if(subtag + 1 == peptide_.length)
							frag = addFrag("M", ch, diff, "", frag, subtag + 1, false);
						else
							frag = addFrag((new StringBuilder("b")).append(vmsdb.str(subtag + 1)).toString(), ch, diff, "", frag, subtag + 1, false);
					} else
						if(subtag + 1 == peptide_.length)
							frag = addFrag((new StringBuilder("M")).append(vmsdb.str(subtag + 1)).toString(), ch, diff, "", frag, subtag + 1, true);
						else
							frag = addFrag((new StringBuilder("b")).append(vmsdb.str(subtag + 1)).toString(), ch, diff, "", frag, subtag + 1, true);
				diff = ymass_[ch - 1][subtag] - fmass;
				if(diff >= -tolerance_ && diff <= tolerance_)
					if(frag != null)
					{
						if(subtag + 1 != peptide_.length)
							frag = addFrag((new StringBuilder("y")).append(vmsdb.str(subtag + 1)).toString(), ch, diff, "", frag, subtag + 1, false);
					} else
						if(subtag + 1 != peptide_.length)
							frag = addFrag((new StringBuilder("y")).append(vmsdb.str(subtag + 1)).toString(), ch, diff, "", frag, subtag + 1, true);
				diff = bmass_[ch - 1][subtag] - fmass;
				if(diff < -tolerance_ || diff > tolerance_)
				{
					subPsbLsH2O.clear();
					subPsbLsNH3.clear();
					subPsbLsHPO3.clear();
					subPsbLsH3PO4.clear();
					for(int t = 0; t < psbLsH2O_.size(); t++)
						if(Integer.parseInt(psbLsH2O_.get(t).toString()) <= subtag)
							subPsbLsH2O.add(psbLsH2O_.get(t));

					for(int t = 0; t < psbLsNH3_.size(); t++)
						if(Integer.parseInt(psbLsNH3_.get(t).toString()) <= subtag)
							subPsbLsNH3.add(psbLsNH3_.get(t));

					for(int t = 0; t < psbLsHPO3_.size(); t++)
						if(Integer.parseInt(psbLsHPO3_.get(t).toString()) <= subtag)
							subPsbLsHPO3.add(psbLsHPO3_.get(t));

					for(int t = 0; t < psbLsH3PO4_.size(); t++)
						if(Integer.parseInt(psbLsH3PO4_.get(t).toString()) <= subtag)
							subPsbLsH3PO4.add(psbLsH3PO4_.get(t));

					for(int nHO = 0; nHO <= subPsbLsH2O.size(); nHO++)
					{
						for(int nNH = 0; nNH <= subPsbLsNH3.size() + 1; nNH++)
						{
							for(int nHP4 = 0; nHP4 <= subPsbLsH3PO4.size(); nHP4++)
							{
								for(int nHP3 = 0; nHP3 <= subPsbLsHPO3.size() - nHP4; nHP3++)
								{
									double psbMass = bmass_[ch - 1][subtag] - (massH2O * (double)nHO + massNH3 * (double)nNH + massHPO3 * (double)nHP3 + massH3PO4 * (double)nHP4) / (double)ch;
									diff = psbMass - fmass;
									if((double)vmsdb.abs((float)diff) < tolerance_)
									{
										String tlabel = (new StringBuilder("b")).append(vmsdb.str(subtag + 1)).toString();
										String mod = "";
										if(subtag + 1 == peptide_.length)
											tlabel = "M";
										if(nHO > 0)
											mod = (new StringBuilder(String.valueOf(mod))).append("-").append(vmsdb.str(nHO)).append("/H2O").toString();
										if(nNH > 0)
											mod = (new StringBuilder(String.valueOf(mod))).append("-").append(vmsdb.str(nNH)).append("/NH3").toString();
										if(nHP3 > 0)
											mod = (new StringBuilder(String.valueOf(mod))).append("-").append(vmsdb.str(nHP3)).append("/HPO3").toString();
										if(nHP4 > 0)
											mod = (new StringBuilder(String.valueOf(mod))).append("-").append(vmsdb.str(nHP4)).append("/H3PO4").toString();
										if(frag != null)
											frag = addFrag(tlabel, ch, diff, mod, frag, subtag + 1, false);
										else
											frag = addFrag(tlabel, ch, diff, mod, frag, subtag + 1, true);
									}
								}

							}

						}

					}

				}
				diff = ymass_[ch - 1][subtag] - fmass;
				if(diff < -tolerance_ || diff > tolerance_)
				{
					subPsbLsH2O.clear();
					subPsbLsNH3.clear();
					subPsbLsHPO3.clear();
					subPsbLsH3PO4.clear();
					for(int t = 0; t < psbLsH2O_.size(); t++)
						if(pnum - Integer.parseInt(psbLsH2O_.get(t).toString()) - 1 <= subtag)
							subPsbLsH2O.add(psbLsH2O_.get(t));

					for(int t = 0; t < psbLsNH3_.size(); t++)
						if(pnum - Integer.parseInt(psbLsNH3_.get(t).toString()) - 1 <= subtag)
							subPsbLsNH3.add(psbLsNH3_.get(t));

					for(int t = 0; t < psbLsHPO3_.size(); t++)
						if(pnum - Integer.parseInt(psbLsHPO3_.get(t).toString()) - 1 <= subtag)
							subPsbLsHPO3.add(psbLsHPO3_.get(t));

					for(int t = 0; t < psbLsH3PO4_.size(); t++)
						if(pnum - Integer.parseInt(psbLsH3PO4_.get(t).toString()) - 1 <= subtag)
							subPsbLsH3PO4.add(psbLsH3PO4_.get(t));

					for(int nHO = 0; nHO <= subPsbLsH2O.size() + 1; nHO++)
					{
						for(int nNH = 0; nNH <= subPsbLsNH3.size(); nNH++)
						{
							for(int nHP4 = 0; nHP4 <= subPsbLsH3PO4.size(); nHP4++)
							{
								for(int nHP3 = 0; nHP3 <= subPsbLsHPO3.size() - nHP4; nHP3++)
								{
									double psbMass = ymass_[ch - 1][subtag] - (massH2O * (double)nHO + massNH3 * (double)nNH + massHPO3 * (double)nHP3 + massH3PO4 * (double)nHP4) / (double)ch;
									diff = psbMass - fmass;
									if((double)vmsdb.abs((float)diff) < tolerance_)
									{
										String tlabel = (new StringBuilder("y")).append(vmsdb.str(subtag + 1)).toString();
										String mod = "";
										if(nHO > 0)
											mod = (new StringBuilder(String.valueOf(mod))).append("-").append(vmsdb.str(nHO)).append("/H2O").toString();
										if(nNH > 0)
											mod = (new StringBuilder(String.valueOf(mod))).append("-").append(vmsdb.str(nNH)).append("/NH3").toString();
										if(nHP3 > 0)
											mod = (new StringBuilder(String.valueOf(mod))).append("-").append(vmsdb.str(nHP3)).append("/HPO3").toString();
										if(nHP4 > 0)
											mod = (new StringBuilder(String.valueOf(mod))).append("-").append(vmsdb.str(nHP4)).append("/H3PO4").toString();
										if(frag != null)
										{
											if(subtag + 1 != peptide_.length)
												frag = addFrag(tlabel, ch, diff, mod, frag, subtag + 1, false);
										} else
											if(subtag + 1 != peptide_.length)
												frag = addFrag(tlabel, ch, diff, mod, frag, subtag + 1, true);
									}
								}

							}

						}

					}

				}
								
				
				diff = amass_[ch - 1][subtag] - fmass;

				// possible A ions
				if(diff >= -tolerance_ && diff <= tolerance_)
					if(frag != null)
					{
						if(subtag + 1 != peptide_.length)
							frag = addFrag((new StringBuilder("a")).append(vmsdb.str(subtag + 1)).toString(), ch, diff, "", frag, subtag + 1, false);
					} else
						if(subtag + 1 != peptide_.length)
							frag = addFrag((new StringBuilder("a")).append(vmsdb.str(subtag + 1)).toString(), ch, diff, "", frag, subtag + 1, true);
				diff = zmass_[ch - 1][subtag] - fmass;

				// possible Z ions
				if(diff >= -tolerance_ && diff <= tolerance_)
					if(frag != null)
					{
						if(subtag + 1 != peptide_.length)
							frag = addFrag((new StringBuilder("z")).append(vmsdb.str(subtag + 1)).toString(), ch, diff, "", frag, subtag + 1, false);
					} else
						if(subtag + 1 != peptide_.length)
							frag = addFrag((new StringBuilder("z")).append(vmsdb.str(subtag + 1)).toString(), ch, diff, "", frag, subtag + 1, true);
				diff = cmass_[ch - 1][subtag] - fmass;

				// possible c ions
				if(diff >= -tolerance_ && diff <= tolerance_)
					if(frag != null)
					{
						if(subtag + 1 != peptide_.length)
							frag = addFrag((new StringBuilder("c")).append(vmsdb.str(subtag + 1)).toString(), ch, diff, "", frag, subtag + 1, false);
					} else
						if(subtag + 1 != peptide_.length)
							frag = addFrag((new StringBuilder("c")).append(vmsdb.str(subtag + 1)).toString(), ch, diff, "", frag, subtag + 1, true);
				diff = xmass_[ch - 1][subtag] - fmass;

				//possible x ions
				if(diff >= -tolerance_ && diff <= tolerance_)
					if(frag != null)
					{
						if(subtag + 1 != peptide_.length)
							frag = addFrag((new StringBuilder("x")).append(vmsdb.str(subtag + 1)).toString(), ch, diff, "", frag, subtag + 1, false);
					} else
						if(subtag + 1 != peptide_.length)
							frag = addFrag((new StringBuilder("x")).append(vmsdb.str(subtag + 1)).toString(), ch, diff, "", frag, subtag + 1, true);
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				

				diff = amass_[ch - 1][subtag] - fmass;
				//possible a ions
				if(diff < -tolerance_ || diff > tolerance_)
				{
					subPsbLsH2O.clear();
					subPsbLsNH3.clear();
					subPsbLsHPO3.clear();
					subPsbLsH3PO4.clear();
					for(int t = 0; t < psbLsH2O_.size(); t++)
						if(Integer.parseInt(psbLsH2O_.get(t).toString()) <= subtag)
							subPsbLsH2O.add(psbLsH2O_.get(t));

					for(int t = 0; t < psbLsNH3_.size(); t++)
						if(Integer.parseInt(psbLsNH3_.get(t).toString()) <= subtag)
							subPsbLsNH3.add(psbLsNH3_.get(t));

					for(int t = 0; t < psbLsHPO3_.size(); t++)
						if(Integer.parseInt(psbLsHPO3_.get(t).toString()) <= subtag)
							subPsbLsHPO3.add(psbLsHPO3_.get(t));

					for(int t = 0; t < psbLsH3PO4_.size(); t++)
						if(Integer.parseInt(psbLsH3PO4_.get(t).toString()) <= subtag)
							subPsbLsH3PO4.add(psbLsH3PO4_.get(t));

					for(int nHO = 0; nHO <= subPsbLsH2O.size(); nHO++)
					{
						for(int nNH = 0; nNH <= subPsbLsNH3.size() + 1; nNH++)
						{
							for(int nHP4 = 0; nHP4 <= subPsbLsH3PO4.size(); nHP4++)
							{
								for(int nHP3 = 0; nHP3 <= subPsbLsHPO3.size() - nHP4; nHP3++)
								{
									double psbMass = amass_[ch - 1][subtag] - (massH2O * (double)nHO + massNH3 * (double)nNH + massHPO3 * (double)nHP3 + massH3PO4 * (double)nHP4) / (double)ch;
									diff = psbMass - fmass;
									if((double)vmsdb.abs((float)diff) < tolerance_)
									{
										String tlabel = (new StringBuilder("a")).append(vmsdb.str(subtag + 1)).toString();
										String mod = "";
										if(nHO > 0)
											mod = (new StringBuilder(String.valueOf(mod))).append("-").append(vmsdb.str(nHO)).append("/H2O").toString();
										if(nNH > 0)
											mod = (new StringBuilder(String.valueOf(mod))).append("-").append(vmsdb.str(nNH)).append("/NH3").toString();
										if(nHP3 > 0)
											mod = (new StringBuilder(String.valueOf(mod))).append("-").append(vmsdb.str(nHP3)).append("/HPO3").toString();
										if(nHP4 > 0)
											mod = (new StringBuilder(String.valueOf(mod))).append("-").append(vmsdb.str(nHP4)).append("/H3PO4").toString();
										if(frag != null)
										{
											if(subtag + 1 != peptide_.length)
												frag = addFrag(tlabel, ch, diff, mod, frag, subtag + 1, false);
										} else
											if(subtag + 1 != peptide_.length)
												frag = addFrag(tlabel, ch, diff, mod, frag, subtag + 1, true);
									}
								}

							}

						}

					}

				}
				diff = zmass_[ch - 1][subtag] - fmass;
				if(diff < -tolerance_ || diff > tolerance_)
				{
					subPsbLsH2O.clear();
					subPsbLsNH3.clear();
					subPsbLsHPO3.clear();
					subPsbLsH3PO4.clear();
					for(int t = 0; t < psbLsH2O_.size(); t++)
						if(pnum - Integer.parseInt(psbLsH2O_.get(t).toString()) - 1 <= subtag)
							subPsbLsH2O.add(psbLsH2O_.get(t));

					for(int t = 0; t < psbLsNH3_.size(); t++)
						if(pnum - Integer.parseInt(psbLsNH3_.get(t).toString()) - 1 <= subtag)
							subPsbLsNH3.add(psbLsNH3_.get(t));

					for(int t = 0; t < psbLsHPO3_.size(); t++)
						if(pnum - Integer.parseInt(psbLsHPO3_.get(t).toString()) - 1 <= subtag)
							subPsbLsHPO3.add(psbLsHPO3_.get(t));

					for(int t = 0; t < psbLsH3PO4_.size(); t++)
						if(pnum - Integer.parseInt(psbLsH3PO4_.get(t).toString()) - 1 <= subtag)
							subPsbLsH3PO4.add(psbLsH3PO4_.get(t));

					for(int nHO = 0; nHO <= subPsbLsH2O.size() + 1; nHO++)
					{
						for(int nNH = 0; nNH <= subPsbLsNH3.size(); nNH++)
						{
							for(int nHP4 = 0; nHP4 <= subPsbLsH3PO4.size(); nHP4++)
							{
								for(int nHP3 = 0; nHP3 <= subPsbLsHPO3.size() - nHP4; nHP3++)
								{
									double psbMass = zmass_[ch - 1][subtag] - (massH2O * (double)nHO + massNH3 * (double)nNH + massHPO3 * (double)nHP3 + massH3PO4 * (double)nHP4) / (double)ch;
									diff = psbMass - fmass;
									if((double)vmsdb.abs((float)diff) < tolerance_)
									{
										String tlabel = (new StringBuilder("z")).append(vmsdb.str(subtag + 1)).toString();
										String mod = "";
										if(nHO > 0)
											mod = (new StringBuilder(String.valueOf(mod))).append("-").append(vmsdb.str(nHO)).append("/H2O").toString();
										if(nNH > 0)
											mod = (new StringBuilder(String.valueOf(mod))).append("-").append(vmsdb.str(nNH)).append("/NH3").toString();
										if(nHP3 > 0)
											mod = (new StringBuilder(String.valueOf(mod))).append("-").append(vmsdb.str(nHP3)).append("/HPO3").toString();
										if(nHP4 > 0)
											mod = (new StringBuilder(String.valueOf(mod))).append("-").append(vmsdb.str(nHP4)).append("/H3PO4").toString();
										if(frag != null)
										{
											if(subtag + 1 != peptide_.length)
												frag = addFrag(tlabel, ch, diff, mod, frag, subtag + 1, false);
										} else
											if(subtag + 1 != peptide_.length)
												frag = addFrag(tlabel, ch, diff, mod, frag, subtag + 1, true);
									}
								}

							}

						}

					}

				}
				
				
				//possible c loss ions
				diff = cmass_[ch - 1][subtag] - fmass;
				if(diff < -tolerance_ || diff > tolerance_)
				{
					subPsbLsH2O.clear();
					subPsbLsNH3.clear();
					subPsbLsHPO3.clear();
					subPsbLsH3PO4.clear();
					for(int t = 0; t < psbLsH2O_.size(); t++)
						if(Integer.parseInt(psbLsH2O_.get(t).toString()) <= subtag)
							subPsbLsH2O.add(psbLsH2O_.get(t));

					for(int t = 0; t < psbLsNH3_.size(); t++)
						if(Integer.parseInt(psbLsNH3_.get(t).toString()) <= subtag)
							subPsbLsNH3.add(psbLsNH3_.get(t));

					for(int t = 0; t < psbLsHPO3_.size(); t++)
						if(Integer.parseInt(psbLsHPO3_.get(t).toString()) <= subtag)
							subPsbLsHPO3.add(psbLsHPO3_.get(t));

					for(int t = 0; t < psbLsH3PO4_.size(); t++)
						if(Integer.parseInt(psbLsH3PO4_.get(t).toString()) <= subtag)
							subPsbLsH3PO4.add(psbLsH3PO4_.get(t));

					for(int nHO = 0; nHO <= subPsbLsH2O.size(); nHO++)
					{
						for(int nNH = 0; nNH <= subPsbLsNH3.size() + 1; nNH++)
						{
							for(int nHP4 = 0; nHP4 <= subPsbLsH3PO4.size(); nHP4++)
							{
								for(int nHP3 = 0; nHP3 <= subPsbLsHPO3.size() - nHP4; nHP3++)
								{
									double psbMass = cmass_[ch - 1][subtag] - (massH2O * (double)nHO + massNH3 * (double)nNH + massHPO3 * (double)nHP3 + massH3PO4 * (double)nHP4) / (double)ch;
									diff = psbMass - fmass;
									if((double)vmsdb.abs((float)diff) < tolerance_)
									{
										String tlabel = (new StringBuilder("c")).append(vmsdb.str(subtag + 1)).toString();
										String mod = "";
										if(nHO > 0)
											mod = (new StringBuilder(String.valueOf(mod))).append("-").append(vmsdb.str(nHO)).append("/H2O").toString();
										if(nNH > 0)
											mod = (new StringBuilder(String.valueOf(mod))).append("-").append(vmsdb.str(nNH)).append("/NH3").toString();
										if(nHP3 > 0)
											mod = (new StringBuilder(String.valueOf(mod))).append("-").append(vmsdb.str(nHP3)).append("/HPO3").toString();
										if(nHP4 > 0)
											mod = (new StringBuilder(String.valueOf(mod))).append("-").append(vmsdb.str(nHP4)).append("/H3PO4").toString();
										if(frag != null)
										{
											if(subtag + 1 != peptide_.length)
												frag = addFrag(tlabel, ch, diff, mod, frag, subtag + 1, false);
										} else
											if(subtag + 1 != peptide_.length)
												frag = addFrag(tlabel, ch, diff, mod, frag, subtag + 1, true);
									}
								}

							}

						}

					}

				}
//				possible X loss ions
				diff = xmass_[ch - 1][subtag] - fmass;
				//System.out.println("xmass: ch="+ch+"; subtag="+subtag+";mass="+xmass_[ch - 1][subtag]);
				if(diff < -tolerance_ || diff > tolerance_)
				{

					subPsbLsH2O.clear();
					subPsbLsNH3.clear();
					subPsbLsHPO3.clear();
					subPsbLsH3PO4.clear();
					for(int t = 0; t < psbLsH2O_.size(); t++){
						int pos = pnum - Integer.parseInt(psbLsH2O_.get(t).toString()) - 1;
						if(pos <= subtag){
							subPsbLsH2O.add(psbLsH2O_.get(t));
						}
					}
					

					for(int t = 0; t < psbLsNH3_.size(); t++){	
						int pos = pnum - Integer.parseInt(psbLsNH3_.get(t).toString()) - 1;
						if(pos <= subtag){
							subPsbLsNH3.add(psbLsNH3_.get(t));
						}
					}
						
					for(int t = 0; t < psbLsHPO3_.size(); t++)
						if(pnum - Integer.parseInt(psbLsHPO3_.get(t).toString()) - 1 <= subtag)
							subPsbLsHPO3.add(psbLsHPO3_.get(t));

					for(int t = 0; t < psbLsH3PO4_.size(); t++)
						if(pnum - Integer.parseInt(psbLsH3PO4_.get(t).toString()) - 1 <= subtag)
							subPsbLsH3PO4.add(psbLsH3PO4_.get(t));
					for(int nHO = 0; nHO <= subPsbLsH2O.size() + 1; nHO++)
					{
						for(int nNH = 0; nNH <= subPsbLsNH3.size(); nNH++)
						{
							for(int nHP4 = 0; nHP4 <= subPsbLsH3PO4.size(); nHP4++)
							{
								for(int nHP3 = 0; nHP3 <= subPsbLsHPO3.size() - nHP4; nHP3++)
								{
									double psbMass = xmass_[ch - 1][subtag] - (massH2O * (double)nHO + massNH3 * (double)nNH + massHPO3 * (double)nHP3 + massH3PO4 * (double)nHP4) / (double)ch;
									diff = psbMass - fmass;
									if((double)vmsdb.abs((float)diff) < tolerance_)
									{
										String tlabel = (new StringBuilder("x")).append(vmsdb.str(subtag + 1)).toString();
										String mod = "";
										if(nHO > 0)
											mod = (new StringBuilder(String.valueOf(mod))).append("-").append(vmsdb.str(nHO)).append("/H2O").toString();
										if(nNH > 0)
											mod = (new StringBuilder(String.valueOf(mod))).append("-").append(vmsdb.str(nNH)).append("/NH3").toString();
										if(nHP3 > 0)
											mod = (new StringBuilder(String.valueOf(mod))).append("-").append(vmsdb.str(nHP3)).append("/HPO3").toString();
										if(nHP4 > 0)
											mod = (new StringBuilder(String.valueOf(mod))).append("-").append(vmsdb.str(nHP4)).append("/H3PO4").toString();
										if(frag != null)
										{
											if(subtag + 1 != peptide_.length)
												frag = addFrag(tlabel, ch, diff, mod, frag, subtag + 1, false);
										} else
											if(subtag + 1 != peptide_.length)
												frag = addFrag(tlabel, ch, diff, mod, frag, subtag + 1, true);
									}
								}

							}

						}
						
					}	
		

				}
				
			}

		}
		if(frag!=null){
			frag[0].selected_ = false;
			InsertionSort.sort(frag);
			frag[0].selected_ = true;
		}
		return frag;
	}

	//public variables
	String masstype_;
	double rdMass_[];
	String rdName_[];
	double tolerance_;
	double fmass_;
	int charge_;
	String lossH2O_[];
	String lossNH3_[];
	String lossHPO3_[];
	String lossH3PO4_[];
	Vector psbLsH2O_;
	Vector psbLsNH3_;
	Vector psbLsHPO3_;
	Vector psbLsH3PO4_;
	Vector stamods_;
	Vector dynmods_;
	String peptide_[];
	String sequence_;
	Vector dynMods_;
	Vector dynMass_;
	vmsdb vmain_;
	double amass_[][];
	double bmass_[][];
	double cmass_[][];
	double xmass_[][];
	double ymass_[][];
	double zmass_[][];
}
