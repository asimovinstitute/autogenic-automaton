package aa.view;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

import aa.AAUtil;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;


public class ChoicePanel extends JPanel implements ActionListener, ItemListener {	    
	private static final long serialVersionUID = -3403796950412486611L;
		
	private InformationPanel informationPanel;
	
	private JCheckBox cA;
	private JCheckBox cB;	
	private JCheckBox cD;
	private JCheckBox cE;
	private JCheckBox cC;
	private JCheckBox cF;
	private JCheckBox cG;
	
	private JCheckBox cEmptyCapsules;
	private JCheckBox cEmptyCapSize;		
	private JCheckBox cAutogens;
	private JCheckBox cAutogenSize;
	
	private JCheckBox cH1;
	private JCheckBox cH2;
	private JCheckBox cMI;
	private JCheckBox cJE;
	private JCheckBox cKLD1;
	private JCheckBox cKLD2;
	private JCheckBox cCE1;
	private JCheckBox cCE2;
	
	private JRadioButton hundredButton, thousandButton, tenThousandButton;
	private ButtonGroup scaleButtonGroup;    		
	
    public ChoicePanel(InformationPanel i) {
    	
    	informationPanel = i;
        this.setBackground(Color.WHITE);    
        
        // GRAPH //
        
        JPanel westPanel = new JPanel(new GridLayout(1,4));
        westPanel.setBorder(new TitledBorder("Show"));
        westPanel.setBackground(Color.WHITE);
        westPanel.setPreferredSize(new Dimension(400, 80));
        
        JPanel p1 = new JPanel(new GridLayout(7,1));
        p1.setPreferredSize(new Dimension(80, 80));
        p1.setBackground(Color.WHITE);
        p1.setBorder(new TitledBorder(""));
        
        cA = new JCheckBox();
        cA.setSelected(false);
        p1.add(choicePanelFactory("  A", 0, cA));  
        cA.addItemListener(this);
        cB = new JCheckBox();
        cB.setSelected(false);
        p1.add(choicePanelFactory("  B", 1, cB));
        cB.addItemListener(this);
        cC = new JCheckBox();
        cC.setSelected(false);
        p1.add(choicePanelFactory("  C", 2, cC));
        cC.addItemListener(this);    
        cD = new JCheckBox();
        cD.setSelected(false);
        p1.add(choicePanelFactory("  D", 3, cD));
        cD.addItemListener(this);
        cE = new JCheckBox();
        cE.setSelected(false);
        p1.add(choicePanelFactory("  E", 4, cE));
        cE.addItemListener(this);                   
        cF = new JCheckBox();
        cF.setSelected(false);
        p1.add(choicePanelFactory("  F", 5, cF));
        cF.addItemListener(this);     
        cG = new JCheckBox();
        cG.setSelected(true);
        p1.add(choicePanelFactory("  G", 6, cG));
        cG.addItemListener(this);     

        JPanel p2 = new JPanel(new GridLayout(4,1));
        p2.setPreferredSize(new Dimension(80, 80));
        p2.setBackground(Color.WHITE);
        p2.setBorder(new TitledBorder(""));
                
        cEmptyCapsules = new JCheckBox();    
        cEmptyCapsules.setSelected(true);
        p2.add(choicePanelFactory("#EC", 7, cEmptyCapsules));
        cEmptyCapsules.addItemListener(this);           
        cEmptyCapSize = new JCheckBox();    
        cEmptyCapSize.setSelected(false);
        p2.add(choicePanelFactory("AvgEC", 8, cEmptyCapSize));
        cEmptyCapSize.addItemListener(this);        
        cAutogens = new JCheckBox();    
        cAutogens.setSelected(true);
        p2.add(choicePanelFactory("#AG", 9, cAutogens));
        cAutogens.addItemListener(this);                
        cAutogenSize = new JCheckBox();    
        cAutogenSize.setSelected(false);
        p2.add(choicePanelFactory("AvgAG", 10, cAutogenSize));
        cAutogenSize.addItemListener(this);        
        
        JPanel p3 = new JPanel(new GridLayout(4,1));
        p3.setPreferredSize(new Dimension(120, 80));
        p3.setBackground(Color.WHITE);
        p3.setBorder(new TitledBorder(""));
               
        cH1 = new JCheckBox();    
        cH1.setSelected(false);
        p3.add(choicePanelFactory("H1", 11, cH1));
        cH1.addItemListener(this);   
        cH2 = new JCheckBox();    
        cH2.setSelected(false);
        p3.add(choicePanelFactory("H2", 12, cH2));
        cH2.addItemListener(this);   
        cMI = new JCheckBox();    
        cMI.setSelected(false);
        p3.add(choicePanelFactory("MI", 13, cMI));
        cMI.addItemListener(this);           
        cJE = new JCheckBox();    
        cJE.setSelected(false);
        p3.add(choicePanelFactory("JE", 14, cJE));
        cJE.addItemListener(this);   
        
        JPanel p4 = new JPanel(new GridLayout(4,1));
        p4.setPreferredSize(new Dimension(120, 80));
        p4.setBackground(Color.WHITE);
        p4.setBorder(new TitledBorder(""));        
       
        cKLD1 = new JCheckBox();
        cKLD1.setSelected(false);
        p4.add(choicePanelFactory("KLD1", 15, cKLD1));
        cKLD1.addItemListener(this);        
        cKLD2 = new JCheckBox();
        cKLD2.setSelected(false);
        p4.add(choicePanelFactory("KLD2", 16, cKLD2));
        cKLD2.addItemListener(this);        
        cCE1 = new JCheckBox();
        cCE1.setSelected(false);
        p4.add(choicePanelFactory("CE1", 17, cCE1));
        cCE1.addItemListener(this);        
        cCE2 = new JCheckBox();
        cCE2.setSelected(false);
        p4.add(choicePanelFactory("CE2", 18, cCE2));
        cCE2.addItemListener(this);
                
    
        westPanel.add(p1);
        westPanel.add(p2);
        westPanel.add(p3);
        westPanel.add(p4);
                
        
        // SCALE //
        
        JPanel eastPanel = new JPanel(new GridLayout(3, 1));
        eastPanel.setBorder(new TitledBorder("Scale"));
        eastPanel.setBackground(Color.WHITE);
        eastPanel.setPreferredSize(new Dimension(100, 80));
        
        scaleButtonGroup = new ButtonGroup();

        hundredButton = new JRadioButton("100");
        hundredButton.addActionListener(this);
        hundredButton.setBackground(Color.WHITE);
        eastPanel.add(hundredButton);
        scaleButtonGroup.add(hundredButton);
        hundredButton.setSelected(true);

        thousandButton = new JRadioButton("1.000");
        thousandButton.addActionListener(this);
        thousandButton.setBackground(Color.WHITE);
        eastPanel.add(thousandButton);
        scaleButtonGroup.add(thousandButton);
        
        tenThousandButton = new JRadioButton("10.000");
        tenThousandButton.addActionListener(this);
        tenThousandButton.setBackground(Color.WHITE);
        eastPanel.add(tenThousandButton);
        scaleButtonGroup.add(tenThousandButton);
        
        this.setLayout(new BorderLayout());
        this.add(westPanel, BorderLayout.CENTER);
        this.add(eastPanel, BorderLayout.EAST);
                
    }    
    
    private JPanel choicePanelFactory(String label, int color, JCheckBox box) {
		JPanel pA = new JPanel(new BorderLayout());
		pA.setBackground(Color.WHITE);
		JPanel jPw = new JPanel();
		jPw.setBackground(AAUtil.getColor(color));
		jPw.setPreferredSize(new Dimension(20, 80));		
		pA.add(jPw, BorderLayout.WEST);
		JLabel jLc = new JLabel(label);
		jLc.setPreferredSize(new Dimension(30, 80));		
        pA.add(jLc, BorderLayout.CENTER);        
        box.setPreferredSize(new Dimension(30, 80));
        box.setBackground(Color.WHITE);
        pA.add(box, BorderLayout.EAST);
        return pA;
	}
    
    
    public boolean getChecked(int index) {
    	switch(index) {
	    	case(0) : {return cA.isSelected();}
	    	case(1) : {return cB.isSelected();}
	    	case(2) : {return cC.isSelected();}
	    	case(3) : {return cD.isSelected();}
	    	case(4) : {return cE.isSelected();}
	    	case(5) : {return cF.isSelected();}
	    	case(6) : {return cG.isSelected();}
	    	case(7) : {return cEmptyCapsules.isSelected();}
	    	case(8) : {return cEmptyCapSize.isSelected();}	    	
	    	case(9) : {return cAutogens.isSelected();}
	    	case(10) : {return cAutogenSize.isSelected();}	    	
	    	case(11) : {return cH1.isSelected();}
	    	case(12) : {return cH2.isSelected();}
	    	case(13) : {return cMI.isSelected();}
	    	case(14) : {return cJE.isSelected();}
	    	case(15) : {return cKLD1.isSelected();}
	    	case(16) : {return cKLD2.isSelected();}
	    	case(17) : {return cCE1.isSelected();}
	    	case(18) : {return cCE2.isSelected();}
	    	default : {System.out.println("Error in ChoicePanel: Checkbox does not exist");return false;}
    	}    	
    }
    
    public void itemStateChanged(ItemEvent e) {
        informationPanel.updateView();
    }
    
    public void actionPerformed(ActionEvent event) {       
        Object source = event.getSource();
        changeScale(source);    
        informationPanel.updateView();
    }
    
    public void changeScale(Object source) {
    	if (source.equals(hundredButton)) {            
        	informationPanel.getGraphPanel().setScale(1);
        }
        else if (source.equals(thousandButton)) {            
        	informationPanel.getGraphPanel().setScale(2);
        }
        else if (source.equals(tenThousandButton)) {            
        	informationPanel.getGraphPanel().setScale(3);
        }        
    }
    
    public void changeScale() {
    	if(hundredButton.isSelected()) 
    		changeScale(hundredButton);
    	else if(thousandButton.isSelected()) 
    		changeScale(thousandButton);
    	else if(tenThousandButton.isSelected()) 
    		changeScale(tenThousandButton);    	    	
    }    
}
    
