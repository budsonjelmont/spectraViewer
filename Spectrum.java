import plugininterface.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


public class Spectrum {


	public void setHandler(String plugintype, String handler, String sourcefilename){
		if(plugintype.equals("MZ")){
			pMZ.setHandler(handler, sourcefilename);
		}else if(plugintype.equals("SO")){
			pSO.setHandler(handler, sourcefilename);
		}
	}
	public void setAppletPlugin(){
		pMZ=new InternalAppletMZParser(Config.DB_TYPE);
		pSO=new InternalAppletSOParser(Config.DB_TYPE);
	}
			
	public void setPlugin(String mzPluginName, String soPluginName){
		try {
			pMZ=Util.loadParserPlugin(new URL[] {Util.toURL(Config.PLUGIN_DIR + mzPluginName)},"MzParser");
		} catch (InstantiationException e) {
            System.err.println("Couldn't instantiate plug-in: "+e.getLocalizedMessage());
        } catch (IllegalAccessException e) {
            System.err.println(e.getLocalizedMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Couldn't find specified class: "+e.getLocalizedMessage());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
		try {
			pSO=Util.loadParserPlugin(new URL[] {Util.toURL(Config.PLUGIN_DIR + soPluginName)},"SoParser");
		} catch (InstantiationException e) {
            System.err.println("Couldn't instantiate plug-in: "+e.getLocalizedMessage());
        } catch (IllegalAccessException e) {
            System.err.println(e.getLocalizedMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Couldn't find specified class: "+e.getLocalizedMessage());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
	}
	public void parse(String idx){
        try {
            yCor_ = new Vector();
            xCor_ = new Vector();
            //Document XMLDoc=DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(pMZ.parse(idx)))); // removed 072916. Need to set Document Builder Factory params before parsing
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
            dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            Document XMLDoc=dbf.newDocumentBuilder().parse(new InputSource(new StringReader(pMZ.parse(idx))));
            
            //troubleshooting code block below to print XML to console
            /*
            DOMImplementationLS domImplLS = (DOMImplementationLS) XMLDoc.getImplementation();
        	LSSerializer serializer = domImplLS.createLSSerializer();
        	String str = serializer.writeToString(XMLDoc);
        	System.out.println(str);
            */
             
            if(Integer.parseInt(((Element)XMLDoc.getElementsByTagName("Spectra").item(0)).getAttribute("found"))>0){
            	NodeList nodelist = ((Element)XMLDoc.getElementsByTagName("Spectra").item(0)).getElementsByTagName("Spectrum");
            	Element spectrum=(Element)nodelist.item(0);
            	NodeList mzlist=spectrum.getElementsByTagName("Peak");
            	for(int i=0; i<mzlist.getLength(); i++){
            		xCor_.add(((Element)((Element)mzlist.item(i)).getElementsByTagName("Mz").item(0)).getTextContent());
            		yCor_.add(((Element)((Element)mzlist.item(i)).getElementsByTagName("Intensity").item(0)).getTextContent());	
            	}
            }else{
            	//...
            }
        } catch (IOException ex) {            
            ex.printStackTrace();
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace();
        } catch (SAXException ex) {
            ex.printStackTrace();
        } 
        
        
        try {
            //Document XMLDoc=DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(pSO.parse(idx)))); // removed 072916--need to set Document Builder parameters first
        	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        	dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
            dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        	Document XMLDoc=dbf.newDocumentBuilder().parse(new InputSource(new StringReader(pSO.parse(idx))));
        	
        	//block below is for printing the XML to troubleshoot
        	/*
        	DOMImplementationLS domImplLS = (DOMImplementationLS) XMLDoc.getImplementation();
        	LSSerializer serializer = domImplLS.createLSSerializer();
        	String str = serializer.writeToString(XMLDoc);
        	System.out.println(str);
        	*/
        	
            if(Integer.parseInt(((Element)XMLDoc.getElementsByTagName("SearchResultList").item(0)).getAttribute("found"))>0){
            	NodeList nodelist = ((Element)XMLDoc.getElementsByTagName("SearchResultList").item(0)).getElementsByTagName("SearchResult");
            	Element result=(Element)nodelist.item(0);
            	Element desc= (Element)result.getElementsByTagName("Description").item(0);
            	filename=((Element)desc.getElementsByTagName("Filename").item(0)).getTextContent();
            	scan=Integer.parseInt(((Element)desc.getElementsByTagName("Scan").item(0)).getTextContent());
            	charge=Integer.parseInt(((Element)desc.getElementsByTagName("Charge").item(0)).getTextContent());
            	exp_mass=Double.parseDouble(((Element)desc.getElementsByTagName("ExperimentalMass").item(0)).getTextContent());
            	modstring=((Element)desc.getElementsByTagName("Modification").item(0)).getTextContent();
            	if(Integer.parseInt(((Element)result.getElementsByTagName("MatchList").item(0)).getAttribute("found"))>0){
            		NodeList matchlist=((Element)result.getElementsByTagName("MatchList").item(0)).getElementsByTagName("Match");
            		for(int i=0; i<matchlist.getLength(); i++){
            			Element match=(Element)matchlist.item(i);
            			if(match.getAttribute("rank").equals("1")){
            				rawSequence=((Element)match.getElementsByTagName("Sequence").item(0)).getTextContent();
            				//rawSequence=((Element)match.getElementsByTagName("RawSequence").item(0)).getTextContent(); //jmb removed 072916--tag name RawSequence isn't used
            				//trimmedSequence=((Element)match.getElementsByTagName("TrimmedSequence").item(0)).getTextContent(); //jmb removed 072916--tag name Trimmed Sequence isn't used
            				theo_mass=Double.parseDouble(((Element)match.getElementsByTagName("TheoreticalMass").item(0)).getTextContent());
                        	pri_score=Double.parseDouble(((Element)match.getElementsByTagName("PrimaryScore").item(0)).getTextContent());
                        	pri_score_name=((Element)match.getElementsByTagName("PrimaryScore").item(0)).getAttribute("name");
                        	sec_score=Double.parseDouble(((Element)match.getElementsByTagName("SecondaryScore").item(0)).getTextContent());
                        	sec_score_name=((Element)match.getElementsByTagName("SecondaryScore").item(0)).getAttribute("name");
                        	protein_name=((Element)match.getElementsByTagName("ProteinName").item(0)).getTextContent();
                        	protein_accession=((Element)match.getElementsByTagName("ProteinAccession").item(0)).getTextContent();
                        	protein_accession_type=((Element)match.getElementsByTagName("ProteinAccession").item(0)).getAttribute("type");
                        	//--optional--
                        	//...
            			}
            		}
            	}else{
            		//...
            	}
            }
        } catch (IOException ex) {            
            ex.printStackTrace();
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace();
        } catch (SAXException ex) {
            ex.printStackTrace();
        } 
	}
	

    
	int scan, charge;
	double exp_mass,theo_mass;
	String rawSequence, trimmedSequence, modstring, filename;
	String pri_score_name, sec_score_name;
	double pri_score,sec_score;
	String protein_name,protein_accession,protein_accession_type;

	ParserInterface pMZ,pSO;
	Vector optional_properties;
    Vector yCor_;
    Vector xCor_;
    String phosphorylation_symbol;
}
