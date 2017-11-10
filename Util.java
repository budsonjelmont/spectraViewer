import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;

import javax.swing.JButton;

import plugininterface.ParserInterface;


public class Util {

	public static String getFileExt(String filename){
		return  (filename.lastIndexOf(".")==-1)?"All":filename.substring(filename.lastIndexOf(".")+1,filename.length());
	}
	public static void enableEnter(final JButton button){
		button.addKeyListener(new KeyListener(){
			public void keyPressed(KeyEvent e){
				if(e.getKeyCode()==(KeyEvent.VK_ENTER)){
					button.doClick();
				}
			}  
			public void keyReleased(KeyEvent e) {} 
			public  void keyTyped(KeyEvent e) {}
		});
	}

	public static ParserInterface loadParserPlugin(URL[] locations, String name) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        //URLClassLoader classloader = new URLClassLoader(locations, ClassLoader.getSystemClassLoader());
        URLClassLoader classloader = URLClassLoader.newInstance(locations);
        // Load the class that was specified
        Class<?> cls = classloader.loadClass(name);
      
        if (!ParserInterface.class.isAssignableFrom(cls)) {
            // Lets just throw some exception since this class doesn't implement the Plugin interface
            throw new RuntimeException("Invalid class: ");
        }

        // Create a new instance of the new class and return it
        // We can be sure this won't throw a class cast exception since we checked for that earlier
        return (ParserInterface)cls.newInstance();
    }
    
	public static  String ReadAll(String filepath){ 
        String temp="";
        StringBuffer contents = new StringBuffer();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filepath));
            String line;
            while ((line = reader.readLine()) != null) {
              // process the file line by line ...
                //System.out.println(line);
                //temp=temp.concat(line);
                contents.append(line);
                contents.append(System.getProperty("line.separator"));

                //System.out.println("freeMemory "+Runtime.getRuntime().freeMemory());
                //System.out.println("Bytes: "+contents.length());
            }
            reader.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        return contents.toString();
    }
	public static  String ReadAll(File file){ 
        String temp="";
        StringBuffer contents = new StringBuffer();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
              // process the file line by line ...
                //System.out.println(line);
                //temp=temp.concat(line);
                contents.append(line);
                contents.append(System.getProperty("line.separator"));

                //System.out.println("freeMemory "+Runtime.getRuntime().freeMemory());
                //System.out.println("Bytes: "+contents.length());
            }
            reader.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        return contents.toString();
    }
	
	public static void Wait(int s){
    	try {
			Thread.sleep(s*1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static URL toURL(String s) throws MalformedURLException{
		if(!Config.APPLET_MODE){
	    	try {
	    		File f=new File(s);
	    		return f.toURL();
	    	}catch(Exception e){
	        	System.out.println(e);
	    	}
		}
    	return new URL(s);
	}
	
	public static String TrimSequence(String s){
		try{
			if(s.indexOf(".")==1&&s.lastIndexOf(".")==s.length()-2){
				s=s.substring(2, s.length() - 2);
			}
		}catch(Exception e){
			
		}
		return s;
	}
	
	public static String locate(URL loc) {
        try {
            String pathString;
            pathString = URLDecoder.decode( loc.toString(), "UTF-8" );
            if( pathString.startsWith( "file:/" ) )            {
                pathString = pathString.substring( "file:/".length() );
            }
            if( pathString.charAt(1)!=':' ) {
                pathString = "/"+pathString;
            } 
			if(!pathString.endsWith("/"))
				pathString=pathString.substring(0,pathString.lastIndexOf("/")+1);
            String sep = System.getProperty( "file.separator" );

            if(sep.equals("\\")){
            	sep="\\\\";
            }
            return pathString.replaceAll("/", sep);
        } catch( UnsupportedEncodingException uee ) {
        // this should never happen for UTF-8
        }
        return "";
    }
}