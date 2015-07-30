package aa.view;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import aa.AAParameters;
import aa.AAUtil;

import java.awt.*;

public class SmallActivationPanel extends JPanel {		
	
	private String name;
	private int type;
	
	private JPanel unitGrid;
    private static int size = 16;
	private static int buttonSize = 10;    
    private int[][] activations;
        
    public SmallActivationPanel(String n, int t) {
        
    	name = n;
    	type = t;
    	this.initialize();        
    }

    public void initialize() {
    	    	
    	//no downscaling if it already fits
    	if(size > AAParameters.GRID_SIZE) {
    		size = AAParameters.GRID_SIZE;
    	}
    	
    	this.setBackground(Color.WHITE);
    	activations = new int[size][size];
    	//empty the array
    	
    	for(int i=0;i<activations.length;i++) {
    		for(int j=0;j<activations[0].length;j++) {
    			activations[i][j] = 0;
    		}
    	}
    	
        unitGrid = new JPanel(new GridLayout(size, size));
        for(int j=0;j<(size*size);j++) {                         
            JButton nButton = new JButton();
            nButton.setPreferredSize(new Dimension(buttonSize, buttonSize));                                     
            nButton.setOpaque(true);
            nButton.setBorderPainted(false);
            unitGrid.add(nButton); 
        }	        	
        
        this.setLayout(new BorderLayout());            	            	
        this.setBorder(new TitledBorder(name));		
        this.removeAll();
        
        unitGrid.setBackground(Color.WHITE);
        this.add(unitGrid, BorderLayout.CENTER);
        
    }
    
    public void reset() {
    	this.initialize();
    	this.updateView();
    	this.revalidate();    	    
    }
    
    public void updateView() {    	    	    	
    	   
    	//type 1: show catalysis (which is updated by Grid.java)
    	//type 2: show positions of crystals (minimum size of 2)
    	//type 3: show positions of autogens (crystal with particles contained within)    	
    	
		for(int i=0;i<activations.length;i++) {
    		for(int j=0;j<activations[i].length;j++) {
    			int index = (size * i) + j;
    			unitGrid.getComponent(index).setBackground(getColor(type, activations[i][j]));    			
    		}
		}    	
		//update values in activations
		for(int i=0;i<activations.length;i++) {
    		for(int j=0;j<activations[i].length;j++) {
    			if(activations[i][j] > 0) {
    				activations[i][j]--;
    			}
    		}
    	}    	
    }
         
    public void updateActivations(int x, int y, int value) {
    	
    	int i = (int)Math.round(   ((double)y * (double)(size-1)) /  (double)AAParameters.GRID_SIZE      ); //NOTE: x/y inversion is on purpose
    	int j = (int)Math.round(   ((double)x * (double)(size-1)) /  (double)AAParameters.GRID_SIZE      );    	
    	//System.out.println(i + " " + j + " " + x + " " + y + " " + size +" "+AAParameters.GRID_SIZE);
    	
    	activations[i][j] = value;
    	
    }    
    
    public static Color getColor(int panelType, int value) {    	    	    
    	switch(panelType) {
	    	case(1) : {
				return new Color(255,255-Math.round(value*10),255-Math.round(value*10));	    		
			}
	    	case(2) : {
	    		return new Color(255-Math.round(value*10),255,255);
			}
    		case(3) : {
    			return new Color(255-Math.round(value*10),255-Math.round(value*10),255-Math.round(value*10));    			    			    			
    		}
    		default : {
    			System.out.println("Unknown activation in panel: "+panelType);
    			return Color.PINK;
    		}    			
    	}    	    	
    }
}
