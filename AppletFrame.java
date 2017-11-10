import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import processing.core.PApplet;

class AppletFrame {
	Frame f;
	PApplet a;
	void display(){
		f = new Frame("vmsdb"); 
		f.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
            	dispose();
            }
          });
		f.setLayout(new BorderLayout());
		a=new vmsdb();
        f.add(a, BorderLayout.CENTER);
        a.init();
        f.pack();     
        f.setVisible(true);
		f.setSize(1024,780);  
	}
	void dispose(){
		a.destroy();
		f.dispose();
	}
}
