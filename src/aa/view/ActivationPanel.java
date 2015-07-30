package aa.view;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import aa.AAControl;
import aa.AAParameters;
import aa.AAUtil;
import aa.model.BasicParticle2D;
import aa.model.Capsule2D;
import aa.model.Particle2D;

import java.awt.*;
import java.util.ArrayList;

public class ActivationPanel extends JPanel {	   
	
	private JPanel particleGrid;
    private AAView view;  
    
    private int _dimensionX = 500; //TODO update for rescaling window
    private int _dimensionY = 500; //TODO update for rescaling window
        
    public ActivationPanel(AAView v) {
        view = v;
        this.initialize();
    }

    public void initialize() {
    	    	    	
    	this.setBackground(Color.WHITE);
    	this.removeAll();
    	this.setLayout(new BorderLayout());            	
        
    	int size = AAParameters.GRID_SIZE;    	    	
    	
		this.setBorder(new TitledBorder("Grid"));
			    		
        int buttonWidth = (int)Math.floor((double)_dimensionX/(double)size);
        int buttonHeight = (int)Math.floor((double)_dimensionY/(double)size);
       // Insets i = getInsets();
       // this.setSize(new Dimension(_dimensionX+i.left+i.right,_dimensionY+i.top+i.bottom));	
        
        particleGrid = new JPanel(new GridLayout(size, size));
        particleGrid.setBackground(Color.WHITE);
        this.add(particleGrid, BorderLayout.CENTER);

        //add the unit buttons
        for(int j=0;j<(size*size);j++) {                         
            JButton nButton = new JButton();
            nButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));                    
            nButton.setBackground(Color.WHITE);     
            nButton.setOpaque(true);
            nButton.setBorderPainted(false);
            particleGrid.add(nButton); 
        }	      	
    }
    
    public void reset() {
    	this.initialize();
    	this.updateView();
    	this.revalidate();
    }
    
    public void updateView() {    	    	    	
    	for(Component c : particleGrid.getComponents()) {
    		c.setBackground(Color.WHITE);
		}    	    
    	int[][] reactions = view.getModel().getGrid().getReactionsArray(); 
    	for(int i=0; i<reactions.length;i++) {
    		for(int j=0;j<reactions[0].length;j++) {
    			int index = AAParameters.GRID_SIZE*i + j; //order doesn't really matter for now, only temporary
    				particleGrid.getComponent(index).setBackground(AAUtil.getBWColor(reactions[i][j]));
    		}
    	}
    	
    	
    	
    	
    	
    /*	ArrayList<Particle2D> ps = view.getModel().getGrid().getParticles();
    	for(Particle2D p : ps) {
    		int index = AAParameters.GRID_SIZE * p.getY() + p.getX();
    		if(p instanceof BasicParticle2D)
    			particleGrid.getComponent(index).setBackground(AAUtil.getColor(((BasicParticle2D) p).getType().ordinal()));
    		else if(p instanceof Capsule2D)
    			particleGrid.getComponent(index).setBackground(AAUtil.getColor(6)); 
    	}  */  	         	   
    }      
    
    
}
