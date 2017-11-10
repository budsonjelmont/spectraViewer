//main class for creating, inserting, connecting to mysql/postgresql database where
//the DTA & OUT files are stored 

import java.sql.*;
import java.util.Vector;

public class database {
	Connection currentdb_;
	String accession;
	
    database()    {
        currentdb_ = connectDB();
    }
    
    public void close() {
    	// called to properly disconnect database from java app
        if(currentdb_ != null)
            try {
                currentdb_.close();
            }
            catch(SQLException se){}
    }
    
    public Connection connectDB()
    {
    	// creates connection w/ postgresql database
        try
        {
        	String dbstr;
        	if(Config.DB_TYPE.compareToIgnoreCase("mysql")==0)
        		// Edit tclifton 12-20-16
        		dbstr="com.mysql.jdbc.Driver";
        		//dbstr="org.gjt.mm.mysql.Driver";
        	else
        		dbstr="org.postgresql.Driver";
        		
            Class.forName(dbstr);
        }
        catch(ClassNotFoundException cnfe)
        {
            System.out.println("checking if driver is registered");
        }
        vmsdb.println("driver loaded");
        Connection c = null;
        try
        {
        	// can change postgresql server at this point (for getConnection)
        	if(Config.DB_TYPE.compareToIgnoreCase("mysql")==0){
    			c=DriverManager.getConnection("jdbc:mysql://localhost:3306/sequest", "superuser", "notTheRealPassword");
        	} else {
        		c = DriverManager.getConnection("jdbc:postgresql://proteome:543/sequest", "testms", "notTheRealPassword");	
        	}
        	c.getMetaData();
        }
        catch(SQLException se)        {
            System.out.println(se.getMessage());
        }
        return c;
    }
    public void setIdent(String id){
    	accession=id;
    }
    
	public void deleteAnnot(double xcor){
        try {
            Statement stmt2 = currentdb_.createStatement();
            
            String tmp6;
            if(Config.DB_TYPE.compareToIgnoreCase("mysql")==0)
            	tmp6=(new StringBuilder("delete from rawspecannot where fmcounter='")).append(accession).append("' and mz='").append(Double.toString(xcor)).append("';").toString();
            else
            	tmp6= (new StringBuilder("delete from ppp.spectra_annot where sid='")).append(accession).append("' and mz='").append(Double.toString(xcor)).append("';").toString();
            stmt2.executeUpdate(tmp6);
        }  catch(SQLException e) {
            vmsdb.print("sql exception error 2, in deleting into proteome db:\n");
        }
	}
	
	public Vector<String[]> getAnnot(){
        ResultSet rs = null;
        Vector<String[]> rst=new Vector();;
        try
        {
            Statement stmt = currentdb_.createStatement();
            if(Config.DB_TYPE.compareToIgnoreCase("mysql")==0)
            	rs = stmt.executeQuery((new StringBuilder("select * from rawspecannot where fmcounter=")).append(accession).append(";").toString());
            else
            	rs = stmt.executeQuery((new StringBuilder("select * from ppp.spectra_annot where sid=")).append(accession).append(";").toString());

            while(rs.next()){
            	String[] t={rs.getString("mz"),rs.getString("annotation")};
            	rst.add(t);
            }
        }
        catch(SQLException e)
        {
            vmsdb.print("sql exception error:\n");
            vmsdb.print(e);
        }
        return rst;
	}
	
	public void saveAnnot(double xcor, String userid, String annot){
		deleteAnnot(xcor);
		try
        {
            Statement stmt = currentdb_.createStatement();
            String tmp;
            if(Config.DB_TYPE.compareToIgnoreCase("mysql")==0)
            	tmp= (new StringBuilder("insert into rawspecannot (fmcounter, fmuser, mz, annotation) values ('")).append(accession).append("', '").append(userid).append("', '").append(Double.toString(xcor)).append("', '").append(annot).append("');").toString();
            else
            	tmp= (new StringBuilder("insert into ppp.spectra_annot (sid, userid, mz, annotation) values ('")).append(accession).append("', '").append(userid).append("', '").append(Double.toString(xcor)).append("', '").append(annot).append("', current_timestamp);").toString();
            stmt.executeUpdate(tmp);
        }catch(SQLException e)
        {
            vmsdb.print("sql exception error 1, in inserting into proteome db:\n");
            vmsdb.print(e);
        }
	}
	
	public String getAssignedSequence(){
        ResultSet rs = null;
        String seq="";
        try
        {
            Statement stmt = currentdb_.createStatement();
            String q ;

            if(Config.DB_TYPE.compareToIgnoreCase("mysql")==0){
            	q = new StringBuilder("select `peptide raw sequence` from `secondary peptide data` where fmcounter=").append(accession).append(";").toString();
            	rs = stmt.executeQuery(q);
            }
            else{
            	q = new StringBuilder("select assigned_seq from ppp.spectra_data where id=").append(accession).append(";").toString();
            	rs = stmt.executeQuery(q);   
            }
        	while(rs.next())
        		seq=Util.TrimSequence(rs.getString(1)+"");
        	vmsdb.print("assigned seq="+seq+"\n");       
        }
        catch(SQLException e)
        {
            vmsdb.print("sql exception error:\n");
            vmsdb.print(e);
        }
        return seq;
	}
	public void saveAssignedSequence(String as){
        ResultSet rs = null;
        try
        {
            Statement stmt = currentdb_.createStatement();
            String q ;

            if(Config.DB_TYPE.compareToIgnoreCase("mysql")==0){
            	q = new StringBuilder("update `secondary peptide data` set `peptide raw sequence`='").append(as).append("' where fmcounter=").append(accession).append(";").toString();
            	rs = stmt.executeQuery(q);
            }
            else{
            	q = new StringBuilder("update ppp.spectra_data set assigned_seq='").append(as).append("' where id=").append(accession).append(";").toString();
            	rs = stmt.executeQuery(q);   
            }     
        }
        catch(SQLException e)
        {
            vmsdb.print("sql exception error:\n");
            vmsdb.print(e);
        }
	}
	
	public ResultSet execSQLQuery(String sql){
        ResultSet rs = null;
        try {
            String q;
            Statement stmt = currentdb_.createStatement();
            rs = stmt.executeQuery(sql);
        } catch(SQLException e)        {
            System.out.print("sql exception error:\n");
            System.out.print(e);
        }
        return rs;
	}
    
}
