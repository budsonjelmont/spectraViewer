//parser that performs the database query to get the OUT file
//corresponding to the requested counter # and then returns 
//the parsed MASCOT sequence assignment file

import java.sql.ResultSet;
import java.sql.SQLException;

public class InternalAppletSOParser  extends SoParser{
	database db;
	String DB_TYPE;
	
    public InternalAppletSOParser(String dbt) {
        DB_TYPE=dbt;
    }
       
    public String parse(String idx) { 
        ResultSet rs = null;
        String q;
        String s3="";
        if(DB_TYPE.equals("mysql")){
        	db=new database();
	        q = new StringBuilder("select `out` from rawspec where fmcounter=").append(idx).append(";").toString();
	        rs = db.execSQLQuery(q);
	
	        try {
	            while(rs.next()) 
	                s3 = rs.getString("out")+"";
	        } catch(SQLException e)  {
	            s3="";
	        }
	        db.close();
	        super.setHandler(s3,idx);
	        return super.parse(s3);
        }else if(DB_TYPE.equals("pgsql")){
        	db=new database();
        	q = new StringBuilder("select search_output_xml from ppp.spectra_data where id=").append(idx).append(";").toString();
	        rs = db.execSQLQuery(q);

	        try {
	            while(rs.next()) 
	                s3 = rs.getString("search_output_xml")+"";
	        } catch(SQLException e)  {
	            s3="";
	        }
	        db.close();
	        return s3;
        }else{
			s3=Util.ReadAll(idx.substring(0, idx.lastIndexOf("."))+".out");
	        super.setHandler(s3,idx);
	        return super.parse(s3);
        }
    }
}