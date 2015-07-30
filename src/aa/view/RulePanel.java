package aa.view;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import aa.AAParameters;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RulePanel extends JPanel implements ActionListener {	    
            
    private JTextField tP1;
    private JTextField tP2;
    private JTextField tP3;     
    private JTextField tP4;
    private JTextField tP5;
    private JTextField tP6;   
    
    private JButton updateButton;
        
    public RulePanel() {          	    	
    	
        this.setBackground(Color.WHITE);    	
        this.setLayout(new BorderLayout());
        this.setBorder(new TitledBorder("Probabilities"));
        
        JPanel westPanel = new JPanel(new GridLayout(6,1));
        westPanel.setBackground(Color.WHITE);  
        westPanel.setPreferredSize(new Dimension(100, 260));
                
        JPanel eastPanel = new JPanel(new GridLayout(6,1));
        eastPanel.setBackground(Color.WHITE);  
        eastPanel.setPreferredSize(new Dimension(70, 260));
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);  
        centerPanel.setPreferredSize(new Dimension(180, 260));
        
        JPanel southPanel = new JPanel(new GridLayout(1,1));
        southPanel.setBackground(Color.WHITE);  
        southPanel.setPreferredSize(new Dimension(180, 30));
        
        westPanel.add(new JLabel("1: G + Gn => Gn+1"));
        westPanel.add(new JLabel("2: Gn+1 => G + Gn"));      
        westPanel.add(new JLabel("3: A + B => C"));
        westPanel.add(new JLabel("4: C => A + B"));
        westPanel.add(new JLabel("5: D + E => F + G"));
        westPanel.add(new JLabel("6: F => D + E")); 
        westPanel.setPreferredSize(new Dimension(110, 40));

        
        tP1 = new JTextField(String.valueOf(AAParameters.PROBABILITIES[0]));
        eastPanel.add(tP1);                
        tP2 = new JTextField(String.valueOf(AAParameters.PROBABILITIES[1]));
        eastPanel.add(tP2);
        tP3 = new JTextField(String.valueOf(AAParameters.PROBABILITIES[2]));
        eastPanel.add(tP3);
        tP4 = new JTextField(String.valueOf(AAParameters.PROBABILITIES[3]));
        eastPanel.add(tP4);
        tP5 = new JTextField(String.valueOf(AAParameters.PROBABILITIES[4]));
        eastPanel.add(tP5);        
        tP6 = new JTextField(String.valueOf(AAParameters.PROBABILITIES[5]));
        eastPanel.add(tP6);
        eastPanel.setPreferredSize(new Dimension(40, 40));

        updateButton = new JButton("Update");
        updateButton.addActionListener(this);
        southPanel.add(updateButton);
        
        centerPanel.add(westPanel, BorderLayout.CENTER);
        centerPanel.add(eastPanel, BorderLayout.EAST);
        
        this.add(centerPanel, BorderLayout.CENTER);
        this.add(southPanel, BorderLayout.SOUTH);                                                                             
    }    
    
    public void updateView() {
    	 tP1.setText(String.valueOf(AAParameters.PROBABILITIES[0]));
         tP2.setText(String.valueOf(AAParameters.PROBABILITIES[1]));
         tP3.setText(String.valueOf(AAParameters.PROBABILITIES[2]));
         tP4.setText(String.valueOf(AAParameters.PROBABILITIES[3]));
         tP5.setText(String.valueOf(AAParameters.PROBABILITIES[4]));
         tP6.setText(String.valueOf(AAParameters.PROBABILITIES[5]));         
    }
    
    public double[] getProbabilities() {
    	double[] p = new double[AAParameters.PROBABILITIES.length];
    	for(int i=0;i<p.length;i++) {
    		p[i] = 0;
    	}
    	p[0] = Double.parseDouble(tP1.getText());
    	p[1] = Double.parseDouble(tP2.getText());
    	p[2] = Double.parseDouble(tP3.getText());    	 
    	p[3] = Double.parseDouble(tP4.getText());
    	p[4] = Double.parseDouble(tP5.getText());
    	p[5] = Double.parseDouble(tP6.getText()); 
    	
    	return p;
    }
    
    public void actionPerformed(ActionEvent event) {       
        Object source = event.getSource();

        if (source.equals(updateButton)) {            
            AAParameters.PROBABILITIES = getProbabilities();            
        }
    }
}
