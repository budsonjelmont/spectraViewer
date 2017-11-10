import java.applet.Applet;
import java.net.URL;

import javax.swing.JOptionPane;

public class Config {
    static boolean APPLET_MODE=true;
    static boolean PDF_disk=false;
    final static String DB_TYPE="mysql";  //mysql|pgsql|disk
    
	static String PLUGIN_DIR;
    static String APP_BASE;
	public static void Init(URL codebase){

		/*
		if(APPLET_MODE){
			String cb=codebase.toString();
			if(!cb.endsWith("/"))
				cb=cb.substring(0,cb.lastIndexOf("/")+1);
			PLUGIN_DIR=cb+"plug-in/";
		}else{
			PLUGIN_DIR=System.getProperty("user.dir")+System.getProperty("file.separator")+"plug-in"+System.getProperty("file.separator");
		}*/
		//JOptionPane.showMessageDialog(null,PLUGIN_DIR);
		
		APP_BASE=Util.locate(codebase);
		PLUGIN_DIR=APP_BASE+"plug-in/";
		System.out.println(APP_BASE);
    }
}
