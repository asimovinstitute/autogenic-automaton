package aa.view;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import aa.AAParameters;
import aa.AAUtil;
import aa.model.Grid2D;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ResetPanel extends JPanel implements ActionListener {	        	

	private AAView view;    	   
         
    private JTextField tSize;
    private JTextField tTypeA;
    private JTextField tTypeB;
    private JTextField tTypeC;
    private JTextField tTypeD;
    private JTextField tTypeE;
    private JTextField tTypeF;   
    private JTextField tTypeG;   
    
    private JButton resetButton;
    
    public ResetPanel(AAView v) {
        view = v;
        Grid2D g = view.getModel().getGrid();
                
        this.setBackground(Color.WHITE);    	
        this.setLayout(new BorderLayout());
        this.setBorder(new TitledBorder("Reset"));
        
        JPanel westPanel = new JPanel(new GridLayout(8,1));
        westPanel.setBackground(Color.WHITE);  
        westPanel.setPreferredSize(new Dimension(40, 260));
                
        JPanel eastPanel = new JPanel(new GridLayout(8,1));
        eastPanel.setBackground(Color.WHITE);  
        eastPanel.setPreferredSize(new Dimension(35, 260));
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);  
        centerPanel.setPreferredSize(new Dimension(90, 260));
        
        JPanel southPanel = new JPanel(new GridLayout(1,1));
        southPanel.setBackground(Color.WHITE);  
        southPanel.setPreferredSize(new Dimension(90, 30));
        
        westPanel.add(new JLabel("Size"));
        westPanel.add(new JLabel("A"));
        westPanel.add(new JLabel("B"));
        westPanel.add(new JLabel("C"));
        westPanel.add(new JLabel("D"));
        westPanel.add(new JLabel("E"));
        westPanel.add(new JLabel("F"));
        westPanel.add(new JLabel("G"));
        
        tSize = new JTextField(String.valueOf(AAParameters.GRID_SIZE));
        eastPanel.add(tSize);  
        
        tTypeA = new JTextField(String.valueOf(g.getGridFunctions().getTotalBasicParticlesOfType(AAUtil.type.A)));        
        eastPanel.add(tTypeA); 
        
        tTypeB = new JTextField(String.valueOf(g.getGridFunctions().getTotalBasicParticlesOfType(AAUtil.type.B)));
        eastPanel.add(tTypeB); 
        
        tTypeC = new JTextField(String.valueOf(g.getGridFunctions().getTotalBasicParticlesOfType(AAUtil.type.C)));
        eastPanel.add(tTypeC); 
        
        tTypeD = new JTextField(String.valueOf(g.getGridFunctions().getTotalBasicParticlesOfType(AAUtil.type.D)));
        eastPanel.add(tTypeD); 
        
        tTypeE = new JTextField(String.valueOf(g.getGridFunctions().getTotalBasicParticlesOfType(AAUtil.type.E)));
        eastPanel.add(tTypeE);         

        tTypeF = new JTextField(String.valueOf(g.getGridFunctions().getTotalBasicParticlesOfType(AAUtil.type.F)));
        eastPanel.add(tTypeF);                                      
     
        tTypeG = new JTextField(String.valueOf(g.getGridFunctions().getTotalBasicParticlesOfType(AAUtil.type.G)));
        eastPanel.add(tTypeG);                                      
     
        resetButton = new JButton("Reset");
        resetButton.addActionListener(this);
        southPanel.add(resetButton);
        
        centerPanel.add(westPanel, BorderLayout.WEST);
        centerPanel.add(eastPanel, BorderLayout.EAST);
        
        this.add(centerPanel, BorderLayout.CENTER);
        this.add(southPanel, BorderLayout.SOUTH);           
    }
    
    public void updateView() {
    	 tSize.setText(Integer.toString(AAParameters.GRID_SIZE));
    	 tTypeA.setText(Integer.toString(AAParameters.INIT_PARTICLES[0]));
    	 tTypeB.setText(Integer.toString(AAParameters.INIT_PARTICLES[1]));
    	 tTypeC.setText(Integer.toString(AAParameters.INIT_PARTICLES[2]));
    	 tTypeD.setText(Integer.toString(AAParameters.INIT_PARTICLES[3]));
    	 tTypeE.setText(Integer.toString(AAParameters.INIT_PARTICLES[4]));
    	 tTypeF.setText(Integer.toString(AAParameters.INIT_PARTICLES[5]));
    	 tTypeG.setText(Integer.toString(AAParameters.INIT_PARTICLES[6]));
    }
       
    public int getGridSize() {
    	return Integer.parseInt(tSize.getText());
    }
    
    public int[] getParticles() {
    	int[] p = new int[AAParameters.INIT_PARTICLES.length];
    	for(int i=0;i<p.length;i++) {
    		p[i] = 0;
    	}    	
    	p[0] = Integer.parseInt(tTypeA.getText());
    	p[1] = Integer.parseInt(tTypeB.getText());
    	p[2] = Integer.parseInt(tTypeC.getText());
    	p[3] = Integer.parseInt(tTypeD.getText());
    	p[4] = Integer.parseInt(tTypeE.getText());
    	p[5] = Integer.parseInt(tTypeF.getText());
    	p[6] = Integer.parseInt(tTypeG.getText());
    	
    	return p;
    }
    
    public void actionPerformed(ActionEvent event) {       
        Object source = event.getSource();

        if (source.equals(resetButton)) {
        	AAParameters.GRID_SIZE = this.getGridSize();
        	AAParameters.INIT_PARTICLES = this.getParticles();
        	view.getControl().resetSimulation();
        }
    }    
    
    public JButton getResetButton() {
    	return resetButton;
    }
}
