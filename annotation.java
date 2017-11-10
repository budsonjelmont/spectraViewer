import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;


public class annotation {
	annotation(String type){
		if(type.equalsIgnoreCase("file")){
			FileMode=true;
			props = new Properties();
		}else{
			FileMode=false;
				db=new database();
		}
	}
	public void setIdentifier(String id){
		ident=id;
		if(FileMode){
			f=new File(id);
			getValuesFromFile(f);
		}else
			db.setIdent(id);
	}
	public void saveAnnot(double xcor, String userid, String annot){
		if(FileMode){
			deleteAnnot(xcor);
			props.setProperty(String.valueOf(xcor), annot);
			saveValuesToFile(f);
		}else
			db.saveAnnot(xcor, userid, annot);
	}
	public void deleteAnnot(double xcor){
		if(FileMode){
			props.remove(xcor);
			saveValuesToFile(f);
		}else
			db.deleteAnnot(xcor);
	}
	public String[][] getAnnot(){
		
		Vector<String[]> rst=new Vector();
		if(FileMode){
			Enumeration em = props.keys();
			while(em.hasMoreElements()){
				String str = (String)em.nextElement();
				if(!str.equals(ASSIGN)){
					String[] t={str,props.getProperty(str)};
					rst.add(t);
				}
			}
		}else
			rst=db.getAnnot();
		String[][] rs=new String[rst.size()][2]; // 0: mz, 1:annotation
		for(int i=0;i<rst.size();i++){
			rs[i]=rst.get(i);
		}
		return rs;
	}

	public String getAssignedSequence(){
		String rst="";
		if(FileMode){
			if(props.getProperty(ASSIGN)!=null){
				rst=props.getProperty(ASSIGN);
			}
		}else
			rst=db.getAssignedSequence();
		return rst;
	}

	public void  saveAssignedSequence(String as){
		if(FileMode){
			props.setProperty(String.valueOf(ASSIGN), as);
			saveValuesToFile(f);
		}else
			db.saveAssignedSequence(as);
	}
	
	
	private void getValuesFromFile(File f){
		try{
			InputStream is=new FileInputStream(f);
			props.load(is);
		}catch(FileNotFoundException e){
			System.out.println("Annotation file doesn't exist: "+f.getName());
		}catch(IOException e){
			System.out.println("Unable to open file on disk.");
		}
	}
	private void saveValuesToFile(File f){
		if(!f.exists()){
			try{
				f.createNewFile();
			}catch(IOException e){
				System.out.println("Unable to create file on disk.");
			}
		}
		try{
			OutputStream os = new FileOutputStream(f);
			props.store(os, "Annotation Data");
		}catch(FileNotFoundException e){
			System.out.println("File doesn't exist.");
		}catch(IOException e){
			System.out.println("Unable to open file on disk.");
		}
	}
	
	Properties props;
	boolean FileMode;
	String ident;
	database db;
	File f;
	String ASSIGN="Assigned_Sequence";
}
