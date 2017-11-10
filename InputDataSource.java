import java.util.Vector;

public class InputDataSource {
	static String mzStruct,mzPlugin,mzFile,mzExtFilter;
	static String rsStruct,rsPlugin,rsFile,rsExtFilter;
	static int current_mz_idx=-1,current_rs_idx=-1;
	static String mzData,rsData;
	static Vector<FileInfoSet> mz_ident_list=new Vector<FileInfoSet>(),rs_ident_list=new Vector<FileInfoSet>();
	static void ResetCurrentIdx(){
		current_mz_idx=-1;
		current_rs_idx=-1;
	}
	
}
