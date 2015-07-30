package aa.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileSystemView;

import aa.AAControl;
import aa.AAParameters;
import aa.AAUtil;

public class DialogPanel extends JPanel implements ActionListener {	    
	private static final long serialVersionUID = -7182922020574597008L;

	private AAView view;
	private AAControl control;
	private JButton save, load, print, about;
	private final JFileChooser fc;	
	
	public DialogPanel(AAView v, AAControl c) {            	
        			        
		view = v;	
		control = c;
		
		save = new JButton("Save");
		save.addActionListener(this);
		
		load = new JButton("Load");
		load.addActionListener(this);
		
		print = new JButton("Print");
		print.addActionListener(this);

		about = new JButton("About");
		about.addActionListener(this);
		
		fc = new JFileChooser(); 
	    fc.setCurrentDirectory(new File(AAControl.DEFAULT_SETTINGS_FILEPATH));	    
	    		
		JPanel buttonPanel = new JPanel(new GridLayout(1,4));
		buttonPanel.setBackground(Color.WHITE);
		buttonPanel.setPreferredSize(new Dimension(230, 30));
		
		buttonPanel.add(save);
		buttonPanel.add(load);
		buttonPanel.add(print);
		//buttonPanel.add(about);
		
		this.setBorder(new TitledBorder(""));
		this.setBackground(Color.WHITE);        
        this.setLayout(new BorderLayout());
        this.add(buttonPanel, BorderLayout.CENTER);
                
    }
	
	
	private void openSaveDialog() {
			
		fc.setDialogTitle("Specify the file name and path");
		
		int returnVal = fc.showSaveDialog(view.getFrame());
		if(returnVal == JFileChooser.APPROVE_OPTION) {			
			AAUtil.saveParameters(control, fc.getSelectedFile());
		}										
	}
		
	private void openLoadDialog() {
		
		fc.setDialogTitle("Select a parameter file to load");
		
		int returnVal = fc.showOpenDialog(view.getFrame());
		if(returnVal == JFileChooser.APPROVE_OPTION) {						
			AAUtil.loadParameters(control, fc.getSelectedFile(), true);			
		}			
	}
		
	private void openAboutDialog() {

		JDialog dialog = createDialog(400,200);
		dialog.setModal(true);
		dialog.setTitle("About");

		JTextArea aboutTextArea = new JTextArea();
		aboutTextArea.append("\n\n\n");
		aboutTextArea.append("     Autogenic Automaton\n");
		aboutTextArea.append("     v. 0.95 build 14.02.13\n\n");
		aboutTextArea.append("     Author: Stefan Leijnen\n");
		aboutTextArea.append("     Contact: stefan@leijnen.org\n");
		aboutTextArea.append("     Information: http://www.leijnen.org/\n");
		aboutTextArea.setLineWrap(true);	
		aboutTextArea.setEditable(false);
				
		dialog.getContentPane().add(aboutTextArea, BorderLayout.CENTER);		
		dialog.pack();
		dialog.setVisible(true);				
	}
	
	private JDialog createDialog(int sizeX, int sizeY) {
		
		try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(Exception e) {        	
        }
        
		JDialog dialog = new JDialog();

		dialog.setPreferredSize(new Dimension(sizeX,sizeY));
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setResizable(false);
		dialog.setLayout(new BorderLayout());	        
		
		int x = ((view.getFrame().getSize().width - sizeX)/2) + view.getFrame().getLocationOnScreen().x;
		int y = ((view.getFrame().getSize().height - sizeY)/2) + view.getFrame().getLocationOnScreen().y;
		
		dialog.setLocation(x, y);
				
		try {
        	UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        }
        catch(Exception e) {
        }
        
		return dialog;
		
	}
		
	public void actionPerformed(ActionEvent event) {       
        Object source = event.getSource();

        if (source.equals(about)) {            
        	openAboutDialog();        	
        }
        else if(source.equals(save)) {
        	openSaveDialog();
        }  
        else if(source.equals(load)) {
        	openLoadDialog();
        }  
        else if(source.equals(print)) {
        	//control.getView().getInformationPanel().printEntropies();
        	System.out.println(control.getModel().getGrid().getGridFunctions().getTotalBasicParticlesOfType(AAUtil.type.G));
        }  
    }     	
}
