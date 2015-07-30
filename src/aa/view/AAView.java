
package aa.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;

import aa.AAControl;
import aa.model.AAModel;

public class AAView {

    private AAModel model;
    private AAControl control;

    private JFrame frame;
    
    private ActivationPanel mainActivationPanel;  
    private SmallActivationPanel subActivationPanel1;
    private SmallActivationPanel subActivationPanel2;
    private SmallActivationPanel subActivationPanel3;
        
    private ControlPanel controlPanel;        
    private ParameterPanel parameterPanel;
    
    private ResetPanel resetPanel;
    private RulePanel rulePanel;
    
    private InformationPanel informationPanel; 
    private DialogPanel dialogPanel;
    private static String imagePath = "img/autogen.jpg"; 

    public AAView(AAModel m, AAControl c) {
       model = m;       
       control = c;
    }

    
    public void initialize() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        }
        catch(Exception e) {
        }

        
        // PANEL 1 //
                        
        JPanel panel1 = new JPanel(new BorderLayout());        
        panel1.setPreferredSize(new Dimension(500, 700));
        panel1.setBackground(Color.WHITE);
        
        mainActivationPanel = new ActivationPanel(this);
        mainActivationPanel.setPreferredSize(new Dimension(500, 500));
        panel1.add(mainActivationPanel, BorderLayout.NORTH);
        
        JPanel panel1South = new JPanel(new GridLayout(1,3));
        panel1South.setPreferredSize(new Dimension(500,200));        
        
        subActivationPanel1 = new SmallActivationPanel("Auto-Catalysis", 1);
        subActivationPanel1.setPreferredSize(new Dimension(160, 200));                
        panel1South.add(subActivationPanel1);
        
        subActivationPanel2 = new SmallActivationPanel("Capsules", 2);
        subActivationPanel2.setPreferredSize(new Dimension(160, 200));
        panel1South.add(subActivationPanel2);
        
        subActivationPanel3 = new SmallActivationPanel("Autogens", 3);
        subActivationPanel3.setPreferredSize(new Dimension(160, 200));
        panel1South.add(subActivationPanel3);

        panel1.add(panel1South, BorderLayout.CENTER);        
        
        // PANEL 2 //
        
        JPanel panel2 = new JPanel(new BorderLayout());
        panel2.setPreferredSize(new Dimension(270,700));
        panel2.setBackground(Color.WHITE);
        
        controlPanel = new ControlPanel(this);
        controlPanel.setPreferredSize(new Dimension(270,210));
        panel2.add(controlPanel, BorderLayout.NORTH);
                
        parameterPanel = new ParameterPanel(this);
        parameterPanel.setPreferredSize(new Dimension(270, 490));
        panel2.add(parameterPanel, BorderLayout.CENTER);
        
        
        
        // INFORMATION PANEL //                
        
        informationPanel = new InformationPanel(this);
        informationPanel.setPreferredSize(new Dimension(520,410));               
                               
        
        // PANEL 3 //
                        
        JPanel panel3 = new JPanel(new BorderLayout());
        panel3.setPreferredSize(new Dimension(520, 290));
        panel3.setBackground(Color.WHITE);        
        
        rulePanel = new RulePanel();
        rulePanel.setPreferredSize(new Dimension(180,290));
        
        resetPanel = new ResetPanel(this);
        resetPanel.setPreferredSize(new Dimension(90,290));        
        
        
        //PANEL 4 //
        
        JPanel panel4 = new JPanel(new BorderLayout());
        panel4.setPreferredSize(new Dimension(250, 290));
        panel4.setBackground(Color.WHITE);
                
        JPanel imgPanel = new JPanel(new BorderLayout());
        imgPanel.setBorder(new TitledBorder("Autogenesis"));
        imgPanel.setBackground(Color.WHITE);        
        JLabel image;
        try {
        	image = new JLabel(new ImageIcon(readImage()));	
        }       
        catch(Exception ex) {
        	image = new JLabel(ex.getMessage());
        }        
        imgPanel.setPreferredSize(new Dimension(250,250));  
        imgPanel.add(image, BorderLayout.CENTER);
        panel4.add(imgPanel, BorderLayout.CENTER);
        
        dialogPanel = new DialogPanel(this, control);
        dialogPanel.setPreferredSize(new Dimension(250, 40));
        panel4.add(dialogPanel, BorderLayout.SOUTH);
        

        panel3.add(rulePanel, BorderLayout.WEST);
        panel3.add(resetPanel, BorderLayout.CENTER);
        panel3.add(panel4, BorderLayout.EAST);
        
                
                        
        // FRAME //
        
        frame = new JFrame ("Autogenic Automaton");
		frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);       
        frame.setBackground(Color.WHITE);
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().add(panel1, BorderLayout.WEST);        
    	frame.setPreferredSize(new Dimension(1290,700));    
        //frame.setLocation(100, 100);
    	         
        JPanel eastPanel = new JPanel(new BorderLayout()); 
        eastPanel.setBackground(Color.WHITE);
        eastPanel.setPreferredSize(new Dimension(520,700));
        eastPanel.add(informationPanel, BorderLayout.CENTER);
        eastPanel.add(panel3, BorderLayout.SOUTH);
        
        frame.getContentPane().add(panel1, BorderLayout.WEST);
        frame.getContentPane().add(panel2, BorderLayout.CENTER);                	            
        frame.getContentPane().add(eastPanel, BorderLayout.EAST);
        
        frame.pack();
        frame.setVisible(true);  
        
        this.update();
    }

    public void update() {        
        mainActivationPanel.updateView();
        subActivationPanel1.updateView();
        subActivationPanel2.updateView();
        subActivationPanel3.updateView();                                
        informationPanel.updateView();                          
    }
    
    public void reset() { //from parameters to view
    	mainActivationPanel.reset();
        subActivationPanel1.reset();
        subActivationPanel2.reset();
        subActivationPanel3.reset();        
        informationPanel.reset(); 
        controlPanel.updateView();
    	parameterPanel.updateView();
    	resetPanel.updateView();
    	rulePanel.updateView();  
    }
    
    public void valuesToParameters() {
    	//....
    }
    
    private BufferedImage readImage() throws IOException {

    	BufferedImage img;
    	
        try {
     	   img = ImageIO.read(new File(imagePath));
        }
        catch(Exception e) {
        	img = new BufferedImage(0,0,0);
            throw new IOException("image not available");        	
        }        
        return img;
    }
       
    public SmallActivationPanel getSmallActivationsPanel(int type) {
    	switch(type) {
    		case(1) : {
    			return subActivationPanel1;
    		}
    		case(2) : {
    			return subActivationPanel2;
    		}
    		case(3) : {
    			return subActivationPanel3;
    		}
    		default : {
    			System.out.println("Error in AView: no such panel");
    			return null;
    		}
    	}
    }
    
    public ResetPanel getResetPanel () {
    	return resetPanel;
    }
    
    public RulePanel getRulePanel () {
    	return rulePanel;
    }    
    
    public ControlPanel getControlPanel () {
    	return controlPanel;
    }
    
    public ParameterPanel getParameterPanel() {
    	return parameterPanel;
    }
        
    public JFrame getFrame() {
    	return frame;
    }    
    
    public ActivationPanel getActivationPanel() {
    	return mainActivationPanel;
    }
    
    public InformationPanel getInformationPanel() {
    	return informationPanel;
    }
    
    public AAModel getModel() {
    	return model;
    }
    
    public AAControl getControl() {
    	return control;
    }
}
