
package aa.view;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

import javax.swing.border.TitledBorder;

import aa.AAParameters;

public class ControlPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = -1240845872494747437L;
	
	private JButton runButton, stopButton, stepButton;
    private JRadioButton slowButton, mediumButton, fastButton, maxButton;
    private ButtonGroup speedButtonGroup;
   
    private AAView view;
    
    public ControlPanel(AAView v) {        
        view = v;
        this.setBackground(Color.WHITE);
      
        //Control panel
        JPanel runPanel = new JPanel(new GridLayout(1,3));
        runPanel.setBorder(new TitledBorder("Control"));
        runPanel.setBackground(Color.WHITE);                       
        
        runButton = new JButton("Run");
        runButton.addActionListener(this);
        runPanel.add(runButton);

        stopButton = new JButton("Stop");
        stopButton.addActionListener(this);
        runPanel.add(stopButton);    
        
        stepButton = new JButton("Step");
        stepButton.addActionListener(this);
        runPanel.add(stepButton);

        //Speed Panel
        JPanel speedSelectionPanel = new JPanel(new GridLayout(1,4));
        speedSelectionPanel.setBorder(new TitledBorder("Speed"));
        speedSelectionPanel.setBackground(Color.WHITE);
        speedButtonGroup = new ButtonGroup();

        slowButton = new JRadioButton("Min");
        slowButton.addActionListener(this);
        slowButton.setBackground(Color.WHITE);
        speedSelectionPanel.add(slowButton);
        speedButtonGroup.add(slowButton);

        mediumButton = new JRadioButton("Slow");
        mediumButton.addActionListener(this);
        mediumButton.setBackground(Color.WHITE);
        speedSelectionPanel.add(mediumButton);
        speedButtonGroup.add(mediumButton);

        fastButton = new JRadioButton("Fast");
        fastButton.addActionListener(this);
        fastButton.setBackground(Color.WHITE);
        speedSelectionPanel.add(fastButton);
        speedButtonGroup.add(fastButton);       
        
        maxButton = new JRadioButton("Max");
        maxButton.addActionListener(this);
        maxButton.setBackground(Color.WHITE);
        speedSelectionPanel.add(maxButton);
        speedButtonGroup.add(maxButton);
        
        switch(AAParameters.SIM_SPEED) {
        case(1) : {slowButton.setSelected(true); break;}
        case(2) : {mediumButton.setSelected(true); break;}
        case(3) : {fastButton.setSelected(true); break;}
        case(4) : {maxButton.setSelected(true); break;}
        }
        
        this.setLayout(new GridLayout(2,1));        
        this.add(runPanel);
        this.add(speedSelectionPanel);        
        this.setBackground(Color.WHITE);
    }

    public void updateView() {
    	switch(AAParameters.SIM_SPEED) {
        case(1) : {slowButton.setSelected(true); break;}
        case(2) : {mediumButton.setSelected(true); break;}
        case(3) : {fastButton.setSelected(true); break;}
        case(4) : {maxButton.setSelected(true); break;}
        }    	
    }
    
    public void actionPerformed(ActionEvent event) {       
        Object source = event.getSource();

        if (source.equals(runButton)) { 
        	view.getResetPanel().getResetButton().setEnabled(false);
            view.getControlPanel().getStepButton().setEnabled(false);
            view.getControl().runSimulation();
        }
        else if (source.equals(stopButton)) {            
        	view.getResetPanel().getResetButton().setEnabled(true);
            view.getControlPanel().getStepButton().setEnabled(true);             
            view.getControl().stopSimulation();
        }        
        else if (source.equals(stepButton)) {
        	view.getControl().stepSimulation();
        }
        else if (source.equals(slowButton)) {            
        	AAParameters.SIM_SPEED = 1;
        }
        else if (source.equals(mediumButton)) {            
        	AAParameters.SIM_SPEED = 2;
        }
        else if (source.equals(fastButton)) {            
        	AAParameters.SIM_SPEED = 3;
        }     
        else if (source.equals(maxButton)) {            
        	AAParameters.SIM_SPEED = 4;
        }             
    }      
    
    public JButton getStepButton() {
    	return stepButton;
    }
    
}
