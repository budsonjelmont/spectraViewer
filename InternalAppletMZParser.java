//parser that performs the database query to get the DTA file
//corresponding to the requested counter # and then returns 
//the spectra parsed into daughter ion mass & intensity pairs 

import java.sql.ResultSet;
import java.sql.SQLException;

public class InternalAppletMZParser extends MzParser{
	database db;
	String DB_TYPE;
    public InternalAppletMZParser(String dbt) {
        DB_TYPE=dbt;
    }
    
    public String parse(String idx) { 
        ResultSet rs = null;
        String q;    	
        String s3="";
        if(DB_TYPE.equals("mysql")){	
        	db=new database();
 	        q = new StringBuilder("select dta from rawspec where fmcounter=").append(idx).append(";").toString();
	        rs = db.execSQLQuery(q);
	        try {
	            while(rs.next()) 
	                s3 = rs.getString("dta")+"";
	        } catch(SQLException e)  {
	            s3="";
	        }
	        db.close();
	        super.setHandler(s3,idx);
	        return super.parse(s3);
        }else if(DB_TYPE.equals("pgsql")){
        	db=new database();
        	q = new StringBuilder("select peaklist_xml from ppp.spectra_data where id=").append(idx).append(";").toString();
	        rs = db.execSQLQuery(q);
	
	        try {
	            while(rs.next()) 
	                s3 = rs.getString("peaklist_xml")+"";
	        } catch(SQLException e)  {
	            s3="";
	        }
	        db.close();
	        return s3;
        }else{
			s3=Util.ReadAll(idx);
	        super.setHandler(s3,idx);
	        return super.parse(s3);
		}
	}
}
