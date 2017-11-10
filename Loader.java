import java.awt.BorderLayout;

import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import plugininterface.ParserInterface;
import processing.core.PApplet;

public class Loader {
	AppletFrame af;
	public Loader() {
    	Config.Init(this.getClass().getProtectionDomain().getCodeSource().getLocation());
    	load_v1();

    }
    public void load_v1(){
    	new Scene1(this);
    }
    public void load_v2(){
    	new Scene2(this);
    }
    public void load_v3(){
    	new Scene3(this);
    }

    public void loadSpectrum(String dataset,String idx){
    	InputDataSource.ResetCurrentIdx();
    	if(dataset.equals("MZ")){
    		InputDataSource.current_mz_idx =Integer.parseInt(idx);
    		for(int i=0;i<InputDataSource.rs_ident_list.size();i++){
    			if(InputDataSource.rs_ident_list.get(i).matchid.equals(InputDataSource.mz_ident_list.get(InputDataSource.current_mz_idx).matchid)){
    				InputDataSource.current_rs_idx =i;
    				break;
    			}
    		}
    	}else{
    		InputDataSource.current_rs_idx =Integer.parseInt(idx);
    		for(int i=0;i<InputDataSource.mz_ident_list.size();i++){
    			if(InputDataSource.mz_ident_list.get(i).matchid.equals(InputDataSource.rs_ident_list.get(InputDataSource.current_rs_idx).matchid)){
    				InputDataSource.current_mz_idx =i;
    				break;
    			}
    		}
    	}
        af = new AppletFrame();
        af.display();
    }
    public void destroySpectrum(){
        af.dispose();
    }
    
    public static void main(String args[])    {
    	Loader s=new Loader();
    }
    
    public void RetrieveSpectralList(Scene3 s3){
		ParserInterface p;
    	if(InputDataSource.mzStruct.equals("M")){
    		try {
    			p=Util.loadParserPlugin(new URL[] {Util.toURL(Config.PLUGIN_DIR + InputDataSource.mzPlugin)},"MzParser");
    		
	    		File dir = new File(InputDataSource.mzFile);
	    		FilenameFilter fileFilter = new FilenameFilter() {
	    	        public boolean accept(File dir, String name) {
	    	            return (InputDataSource.mzExtFilter.equals("All") || name.endsWith(InputDataSource.mzExtFilter));
	    	        }
	    	    };
	    	    File[] files = dir.listFiles(fileFilter);
	            for (int i=0; i<files.length; i++) {
	                File file = files[i];
	                FileInfoSet f=new FileInfoSet();
	                f.filename=file.toString();
	                p.setHandler(Util.ReadAll(file),f.filename);
	                f.matchid = p.parseHeader()[0];
	                f.idx=i;
	                s3.lbProgress.setText(i+"/"+files.length);
	                InputDataSource.mz_ident_list.add(f);
	            }
    		} catch (Exception e) {
                e.printStackTrace();
            }
    	}else if(InputDataSource.mzStruct.equals("S")){
    		try {
    			p=Util.loadParserPlugin(new URL[] {Util.toURL(Config.PLUGIN_DIR + InputDataSource.mzPlugin)},"MzParser");
    		
	    		File file = new File(InputDataSource.mzFile);
	    		InputDataSource.mzData=Util.ReadAll(file);
	    		p.setHandler(InputDataSource.mzData,InputDataSource.mzFile);
	    		String[] h = p.parseHeader();
	            for (int i=0; i<h.length; i++) {
	                FileInfoSet f=new FileInfoSet();
	                f.filename=InputDataSource.mzFile;
	                f.matchid = h[i];
	                f.idx=i;
	                s3.lbProgress.setText(i+"/"+h.length);
	                InputDataSource.mz_ident_list.add(f);
	            }
    		} catch (Exception e) {
                e.printStackTrace();
            }
    	}
    	
    	

    	if(InputDataSource.rsStruct.equals("M")){
    		try {
    			p=Util.loadParserPlugin(new URL[] {Util.toURL(Config.PLUGIN_DIR + InputDataSource.rsPlugin)},"SoParser");
    		
	    		File dir = new File(InputDataSource.rsFile);
	    		FilenameFilter fileFilter = new FilenameFilter() {
	    	        public boolean accept(File dir, String name) {
	    	            return (InputDataSource.rsExtFilter.equals("All") || name.endsWith(InputDataSource.rsExtFilter));
	    	        }
	    	    };
	    	    File[] files = dir.listFiles(fileFilter);
	            for (int i=0; i<files.length; i++) {
	                File file = files[i];
	                FileInfoSet f=new FileInfoSet();
	                f.filename=file.toString();
	                p.setHandler(Util.ReadAll(file),f.filename);
	                f.matchid = p.parseHeader()[0];
	                f.idx=i;
	                s3.lbProgress.setText(i+"/"+files.length);
	                InputDataSource.rs_ident_list.add(f);
	            }
    		} catch (Exception e) {
                e.printStackTrace();
            }
    	}else if(InputDataSource.rsStruct.equals("S")){
    		try {
    			p=Util.loadParserPlugin(new URL[] {Util.toURL(Config.PLUGIN_DIR + InputDataSource.rsPlugin)},"RsParser");
    		
	    		File file = new File(InputDataSource.rsFile);
	    		InputDataSource.rsData=Util.ReadAll(file);
	    		p.setHandler(InputDataSource.rsData,InputDataSource.rsFile);
	    		String[] h = p.parseHeader();
	            for (int i=0; i<h.length; i++) {
	                FileInfoSet f=new FileInfoSet();
	                f.filename=InputDataSource.rsFile;
	                f.matchid = h[i];
	                f.idx=i;
	                s3.lbProgress.setText(i+"/"+h.length);
	                InputDataSource.rs_ident_list.add(f);
	            }
    		} catch (Exception e) {
                e.printStackTrace();
            }
    	}
    	System.out.println(InputDataSource.mz_ident_list.size());
    	System.out.println(InputDataSource.rs_ident_list.size());
    }

    
    
}

class Scene1 extends Frame {
	Loader s;
	String data_struct="";
    String chkboxTXT[]={"Multiple peak list files v.s Multiple search output files",
    			"Single peak list file v.s Single search output file",
    			"Multiple peak list files v.s Single search output file",
    			"Single peak list file v.s Multiple search output files"};
    Scene1(Loader s) {
        super("SpecNote: Spectral Visualization and Annotation Tool");
        this.s=s;   	
        
        setLayout(new BorderLayout());


        add(new Label("Select data structure:",Label.CENTER),BorderLayout.NORTH);
        add(new Label("    ",Label.CENTER),BorderLayout.WEST);
        add(new Label("    ",Label.CENTER),BorderLayout.EAST);
       
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1));
        CheckboxGroup cg1=new CheckboxGroup();
        Checkbox MM=new Checkbox(chkboxTXT[0], cg1, false);
        panel.add(MM);
        Checkbox SS=new Checkbox(chkboxTXT[1], cg1, false);
        panel.add(SS);
        Checkbox MS=new Checkbox(chkboxTXT[2], cg1, false);
        panel.add(MS);
        Checkbox SM=new Checkbox(chkboxTXT[3], cg1, false);
        panel.add(SM);
		ActionListener ar = new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				//JRadioButton radio = (JRadioButton) ae.getSource();
				JOptionPane.showMessageDialog(null,"This is the " + ae.getActionCommand() + 
				" radio button.");
			}
		};
		ItemListener ir = new ItemListener() {
			public void itemStateChanged(ItemEvent ev) {
				//JRadioButton radio = (JRadioButton) ae.getSource();
		        //boolean selected = (ev.getStateChange() == ItemEvent.SELECTED);
		        //System.out.println(ev.getItem().toString());
		        if(ev.getItem().toString().equals(chkboxTXT[0]))
		        	data_struct="MM";
		        else if(ev.getItem().toString().equals(chkboxTXT[1]))
		        	data_struct="SS";
		        else if(ev.getItem().toString().equals(chkboxTXT[2]))
		        	data_struct="MS";
		        else if(ev.getItem().toString().equals(chkboxTXT[3]))
		        	data_struct="SM";
			}
		};
		MM.addItemListener(ir);
		SS.addItemListener(ir);
		MS.addItemListener(ir);
		SM.addItemListener(ir);
        add(panel,BorderLayout.CENTER);
        
        JButton btnNext = new JButton("Next");
		//btnNext.setFont(new Font("Arial",Font.PLAIN, 12));
        btnNext.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Next();
			}
		});
        
        add(btnNext,BorderLayout.SOUTH);

        
        addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
            	dispose();
            	System.exit(0);
            }
          });
     
        pack();     
        setLocation(200,150);
        setSize(500,250);
        setVisible(true);

    }    
    
    void Next(){
    	if(data_struct.equals(""))
    		JOptionPane.showMessageDialog(null,"Select at least one option!");
    	else{
    		InputDataSource.mzStruct=String.valueOf(data_struct.charAt(0));
    		InputDataSource.rsStruct=String.valueOf(data_struct.charAt(1));
    		s.load_v2();
    		dispose();
    	}
    }
}

class Scene2 extends Frame {
	Loader s;
	FileChooserSet mzFileChooser,soFileChooser;
	JComboBox mzPluginsCombo,soPluginsCombo;
	Scene2(Loader s) {
        super("SVAT: Spectral Visualization and Annotation Tool");
        this.s=s;  
        
        File dir = new File(Config.PLUGIN_DIR);
        
        String[] children = dir.list();
        Vector<String> mzPlugins=new Vector<String>(), soPlugins=new Vector<String>();
        if (children == null) {
        	JOptionPane.showMessageDialog(null,"No plug-in is available in folder: " + Config.PLUGIN_DIR );
        } else {
            for (int i=0; i<children.length; i++) {
                // Get filename of file or directory
                String filename = children[i];
                if(filename.lastIndexOf('.')>0){
                	if(filename.lastIndexOf(".MZ.")>0){
                		mzPlugins.add(filename.substring(0,filename.lastIndexOf(".MZ.")));
                	}
                	if(filename.lastIndexOf(".SO.")>0){
                		soPlugins.add(filename.substring(0,filename.lastIndexOf(".SO.")));
                	}
                }
                //JOptionPane.showMessageDialog(null,filename);
            }
        }
        /*
        for(int i=0; i<mzPlugins.size(); i++) {
        	System.out.println(mzPlugins.get(i));
        }*/
        
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
              
		mzPluginsCombo = new JComboBox();
		for(int i=0; i<mzPlugins.size(); i++) {
			mzPluginsCombo.addItem(mzPlugins.get(i));
        }		
		soPluginsCombo = new JComboBox();
		for(int i=0; i<soPlugins.size(); i++) {
			soPluginsCombo.addItem(soPlugins.get(i));
        }		
		JButton btnNext = new JButton("Next");


		mzFileChooser=new FileChooserSet();
		soFileChooser=new FileChooserSet();
		if(InputDataSource.mzStruct.equals("S"))
			mzFileChooser.chkFolder.setSelected(false);
		if(InputDataSource.rsStruct.equals("S"))
			soFileChooser.chkFolder.setSelected(false);
		
		
		c.weightx = 1;
		
		c.ipady = 0;
		c.gridx = 0;
		c.gridy = 0;
		add(new Label(" "), c);
		c.gridx = 1;
		add(new Label(" "), c);
		c.gridx = 2;
		add(new Label(" "), c);
		c.gridx = 3;
		add(new Label(" "), c);
		c.gridx = 4;
		add(new Label(" "), c);
		c.gridx = 5;
		add(new Label(" "), c);
		c.gridx = 6;
		add(new Label(" "), c);
		
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 5;
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 2;
		add(new Label("Peak List File:",Label.LEFT), c);
		
		c.ipady = 0;
		c.gridx = 2;
		c.gridy = 2;
		add(new Label("Select file format: ",Label.LEFT), c);		
		
		c.gridx = 4;
		add(mzPluginsCombo, c);
		
		c.gridy = 3;
		c.gridx = 2;
		c.gridwidth = 1;
		add(mzFileChooser.btnFolder, c);
		c.gridx = 3;
		c.gridwidth = 3;
		add(mzFileChooser.txtFolder, c);
		c.gridx = 6;
		c.gridwidth = 1;
		add(mzFileChooser.chkFolder, c);
		c.gridy = 4;
		c.gridx = 2;
		c.gridwidth = 1;
		add(mzFileChooser.lbFolder, c);
		c.gridx = 3;
		c.gridwidth = 2;
		add(mzFileChooser.cboFolder, c);
		
		c.ipady = 10;
		c.gridx = 0;
		c.gridy = 5;
		c.gridwidth = 7;
		add(new Label(" "), c);

		c.gridwidth = 2;
		c.ipady = 5;
		c.gridx = 1;
		c.gridy = 6;
		add(new Label("Search Result File:",Label.LEFT), c);
		
		c.ipady = 0;
		c.gridx = 2;
		c.gridy = 7;
		add(new Label("Select file format: ",Label.LEFT), c);		
		
		c.gridx = 4;
		add(soPluginsCombo, c);
		
		c.gridy = 8;
		c.gridx = 2;
		c.gridwidth = 1;
		add(soFileChooser.btnFolder, c);
		c.gridx = 3;
		c.gridwidth = 3;
		add(soFileChooser.txtFolder, c);
		c.gridx = 6;
		c.gridwidth = 1;
		add(soFileChooser.chkFolder, c);
		c.gridy = 9;
		c.gridx = 2;
		c.gridwidth = 1;
		add(soFileChooser.lbFolder, c);
		c.gridx = 3;
		c.gridwidth = 2;
		add(soFileChooser.cboFolder, c);
		
		c.ipady = 10;
		c.gridx = 0;
		c.gridy = 10;
		c.gridwidth = 7;
		add(new Label(" "), c);
		
		c.ipady = 0;
		c.gridx = 0;
		c.gridy = 11;
		c.gridwidth = 7;
		add(btnNext, c);
		
        btnNext.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Next();
			}
		});
        addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
            	dispose();
            	System.exit(0);
            }
          });
        
        pack();     
        setLocation(200,150);
        setSize(500,350);
        setVisible(true);
        
	}
    void Next(){
    	String mzPath=mzFileChooser.txtFolder.getText();
    	String soPath=soFileChooser.txtFolder.getText();
    	if(mzPath.equals(""))
    		JOptionPane.showMessageDialog(null,"Please designate data source");
    	else{
    		try{
	    		InputDataSource.rsFile=soPath;
	    		InputDataSource.rsPlugin=soPluginsCombo.getSelectedItem().toString()+".SO.jar";
	    		InputDataSource.rsExtFilter=soFileChooser.cboFolder.getSelectedItem().toString();
    		}catch(Exception e){
    		}
    		try{
	    		InputDataSource.mzFile=mzPath;
	    		InputDataSource.mzPlugin=mzPluginsCombo.getSelectedItem().toString()+".MZ.jar";
	    		InputDataSource.mzExtFilter=mzFileChooser.cboFolder.getSelectedItem().toString();
	    		dispose();
	    		s.load_v3();
    		}catch(Exception e){
    			e.printStackTrace();
    			JOptionPane.showMessageDialog(null,"Invalid selection");
    		}
    	}
    }
	
}

class Scene3 extends Frame {
	Loader s;
	Label lbProgress;
	entry_list el;
	Scene3(Loader ss){
		super("SVAT: Spectral Visualization and Annotation Tool");
        this.s=ss;
        setLayout(new BorderLayout());
        JPanel panel_1 = new JPanel();
        panel_1.setLayout(new GridLayout(0, 1));
        panel_1.add(new Label("Reading data...",Label.CENTER),BorderLayout.NORTH);
        lbProgress=new Label("",Label.CENTER);
        panel_1.add(lbProgress,BorderLayout.CENTER);
        
        
        
        
		
        addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
            	dispose();
            	System.exit(0);
            }
          });
        
        add(panel_1,BorderLayout.CENTER);
        pack();     
        setLocation(200,150);
        setSize(500,250);
        setVisible(true);
        
        s.RetrieveSpectralList(this);
        this.remove(panel_1);
        el=new entry_list();
        el.addData("MZ");
        el.table.getSelectionModel().addListSelectionListener(
                new ListSelectionListener() {
                    public void valueChanged(ListSelectionEvent event) {
                    	if(!event.getValueIsAdjusting()){
	                        int selCols = el.table.getSelectedColumn();
	                        int selRows = el.table.getSelectedRow();
	                        if (selRows < 0) {
	                            //Selection got filtered away.
	                        } else {
	                        	//if(selCols==1)
	                        		s.loadSpectrum(el.dataset,String.valueOf((el.table.getValueAt(selRows, 0))));
	                        }
                    	}
                    }
                }
        );
        add(el,BorderLayout.CENTER);
        pack();
	}
}

class entry_list extends JPanel {
	JTable table;
	DefaultTableModel model;
	String dataset="";
	public entry_list(){
	    model = new DefaultTableModel();
	    table = new JTable(model);
	    table.setAutoCreateRowSorter(true);
	    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		if (model.getColumnCount()==0) {
			model.addColumn("id");
			model.addColumn("Scan #");
			model.addColumn(" ## ");
			model.addColumn(" ## ");
		}
		table.setLayout( new BorderLayout() );
		table.setPreferredScrollableViewportSize(new Dimension(300,600)); 
		JScrollPane scrollPane =new JScrollPane(table);
		//scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); 
		//table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); 
		TableColumn col = table.getColumnModel().getColumn(1);
		col.setPreferredWidth(150);
		col = table.getColumnModel().getColumn(0);
		col.setPreferredWidth(30);
		col = table.getColumnModel().getColumn(2);
		col.setPreferredWidth(50);
		add(table.getTableHeader(),BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);
		
	}
	public void addData(String ds){
		dataset=ds;
		if(dataset.equals("MZ")){
			for (int i=0;i<InputDataSource.mz_ident_list.size();i++ ) {
				FileInfoSet f=InputDataSource.mz_ident_list.get(i);
				model.insertRow(model.getRowCount(), new Object[]{f.idx,f.matchid});
			}
		}else if(dataset.equals("RS")){
			for (int i=0;i<InputDataSource.rs_ident_list.size();i++ ) {
				FileInfoSet f=InputDataSource.rs_ident_list.get(i);
				model.insertRow(model.getRowCount(), new Object[]{f.idx,f.matchid});
			}
		}
	}
}

class BrowseBox extends JFileChooser {

	JTextField textField_;	//textfield to be filled
	JComboBox cboList_;	//textfield to be filled
	javax.swing.filechooser.FileFilter filter_;	//filters out all files not of specified type
	
	//Component c is cast to a JTextField and filled in once a file is selected
	// String type represents the file type (ie .txt .jar)
	BrowseBox(Component c, Component cbo, String type){
		super();
		textField_ = (JTextField) c;
		cboList_ = (JComboBox) cbo;
		if(type.equals("DIRECTORY")){
			setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		}
		else{
			setFileSelectionMode(JFileChooser.FILES_ONLY);
		}
		//setFileFilter(filter_);
		showOpenDialog(c);
	}
	
	//called when the 'open' button on file browser is pressed 
	// if the selected file is of the correct type, then its absolute path is set as the text in textField_
	public void approveSelection(){
		File file = getSelectedFile();
		textField_.setText(file.toString());
		cboList_.addItem("All");
		if(file.isDirectory()==true){
			String[] children = file.list();
			if (children == null) {
				JOptionPane.showMessageDialog(null,"The folder you selected is empty");
		    } else {
		    	String extlist=",All,";
		        for (int i=0; i<children.length; i++) {
		            // Get filename of file or directory
		            String ext = Util.getFileExt(children[i]);
		            if(extlist.indexOf(","+ext+",")==-1){
		            	cboList_.addItem(ext);
		            	extlist+=ext+",";
		            }
		        }
		    }
		}else
			cboList_.addItem(Util.getFileExt(file.toString()));

		super.approveSelection();
	}
	
}

class FileChooserSet {
	JCheckBox chkFolder ;
	JTextField txtFolder ;
	JButton btnFolder ;
	JComboBox cboFolder ;
	Label lbFolder ;
	
	FileChooserSet() {
		chkFolder = new JCheckBox("folder only?",true);
		txtFolder = new JTextField("",23);
		btnFolder = new JButton("Browse");
		cboFolder = new JComboBox();
		lbFolder = new Label("Extension Filter: ");
		
		Util.enableEnter(btnFolder);	
		btnFolder.addActionListener(new ActionListener(){
			public void actionPerformed(final ActionEvent e){
				cboFolder.removeAllItems();
				if(chkFolder.isSelected()==true)
					new BrowseBox(txtFolder,cboFolder,"DIRECTORY");	
				else
					new BrowseBox(txtFolder,cboFolder,"");
			} 
		});	
	}
}