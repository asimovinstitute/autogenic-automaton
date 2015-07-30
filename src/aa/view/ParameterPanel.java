package aa.view;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import aa.AAParameters;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ParameterPanel extends JPanel implements ActionListener {	        		

	private AAView view;    	   
             
    private JTextField tParticleSpeed;
    private JTextField tMinCapPart;
    private JTextField tMaxCapPart;
    private JTextField tMaxConWeight;
    private JCheckBox cEncapsulation;
    private JCheckBox cGRemoval;
   
    private JButton updateButton;
    
    
    public ParameterPanel(AAView v) {
        view = v;
                
        this.setBackground(Color.WHITE);    	
        this.setLayout(new BorderLayout());
        this.setBorder(new TitledBorder("Parameters"));
        
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setBackground(Color.WHITE);
        northPanel.setPreferredSize(new Dimension(255, 180));
                
        JPanel northWestPanel = new JPanel(new GridLayout(6,1));
        northWestPanel.setBackground(Color.WHITE);  
        northWestPanel.setPreferredSize(new Dimension(135, 180));
                
        JPanel northEastPanel = new JPanel(new GridLayout(6,1));
        northEastPanel.setBackground(Color.WHITE);  
        northEastPanel.setPreferredSize(new Dimension(120, 180));
                 
        northWestPanel.add(new JLabel("Particle Speed")); 
        northWestPanel.add(new JLabel("Min Cap Particles Enc"));
        northWestPanel.add(new JLabel("Max Cap Particles"));
        northWestPanel.add(new JLabel("Max Con Weight"));
        northWestPanel.add(new JLabel("Encapsulation"));
        northWestPanel.add(new JLabel("G Removal"));
        
        tParticleSpeed = new JTextField(String.valueOf(AAParameters.PARTICLE_SPEED));
        northEastPanel.add(tParticleSpeed);
        
        tMinCapPart = new JTextField(String.valueOf(AAParameters.MIN_CAPSULE_PARTICLES_FOR_ENCAPSULATION));
        northEastPanel.add(tMinCapPart);
               
        tMaxCapPart = new JTextField(String.valueOf(AAParameters.MAX_CAPSULE_PARTICLES));
        northEastPanel.add(tMaxCapPart);
        
        tMaxConWeight = new JTextField(String.valueOf(AAParameters.MAX_CONTAINED_PARTICLES_WEIGHT));
        northEastPanel.add(tMaxConWeight);
        
        cEncapsulation = new JCheckBox("", AAParameters.ENCAPSULATION);
        cEncapsulation.setBackground(Color.WHITE);
        northEastPanel.add(cEncapsulation);
        
        cGRemoval = new JCheckBox("", AAParameters.GREMOVAL);
        cGRemoval.setBackground(Color.WHITE);
        northEastPanel.add(cGRemoval);
        
        updateButton = new JButton("Update");
        updateButton.addActionListener(this);       
        
        northPanel.add(northWestPanel, BorderLayout.WEST);
        northPanel.add(northEastPanel, BorderLayout.EAST);
        
        this.add(northPanel, BorderLayout.CENTER);
        this.add(updateButton, BorderLayout.SOUTH);
    }
    
    public void updateView() {
    	tParticleSpeed.setText(String.valueOf(AAParameters.PARTICLE_SPEED));
        tMinCapPart.setText(String.valueOf(AAParameters.MIN_CAPSULE_PARTICLES_FOR_ENCAPSULATION));        
        tMaxCapPart.setText(String.valueOf(AAParameters.MAX_CAPSULE_PARTICLES));        
        tMaxConWeight.setText(String.valueOf(AAParameters.MAX_CONTAINED_PARTICLES_WEIGHT));        
        cEncapsulation.setSelected(AAParameters.ENCAPSULATION);       
        cGRemoval.setSelected(AAParameters.GREMOVAL);
    }    
       
    
    public double getParticleSpeed() {
    	return Double.parseDouble(tParticleSpeed.getText());
    }
    
    public int getMinCapsuleParticles() {
    	return Integer.parseInt(tMinCapPart.getText());
    }
    
    public int getMaxCapsuleParticles() {
    	return Integer.parseInt(tMaxCapPart.getText());
    }
    
    public int getMaxContainedParticlesWeight() {
    	return Integer.parseInt(tMaxConWeight.getText());
    }
    
    public boolean getEncapsulation() {
    	return cEncapsulation.isSelected();    	
    }
    
    public boolean getGRemoval() {
    	return cGRemoval.isSelected();
    }
    
    public void actionPerformed(ActionEvent event) {       
        Object source = event.getSource();

        if (source.equals(updateButton)) {            
            AAParameters.PARTICLE_SPEED = this.getParticleSpeed();
            AAParameters.MIN_CAPSULE_PARTICLES_FOR_ENCAPSULATION = this.getMinCapsuleParticles();
            AAParameters.MAX_CAPSULE_PARTICLES = this.getMaxCapsuleParticles();
            AAParameters.MAX_CONTAINED_PARTICLES_WEIGHT = this.getMaxContainedParticlesWeight();
            AAParameters.ENCAPSULATION = this.getEncapsulation();  
            AAParameters.GREMOVAL = this.getGRemoval();
        }
    }    
    
    public JButton getResetButton() {
    	return updateButton;
    }
}
