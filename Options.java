/**
 * --Copyright notice-- 
 *
 * Copyright (c) School of Geography, University of Leeds. 
 * http://www.geog.leeds.ac.uk/
 * This software is licensed under 'The Artistic License' which can be found at 
 * the Open Source Initiative website at... 
 * http://www.opensource.org/licenses/artistic-license.php
 *
 * The Standard Version source code, and associated documentation can be found at... 
 * [online] http://mass.leeds.ac.uk/
 * 
 *
 * --End of Copyright notice-- 
 *
 */


import java.awt.*;
import java.awt.event.*;


/**
 * A simple GUI for setting options. 
 * Allows the setting of colour through three scrollbars, and 
 * the number of points to be plotted etc.<P>
 * @version 0.12
 * @author <A href="http://www.geog.leeds.ac.uk/people/a.evans/">Andy Evans</A>
**/
class Options extends Frame implements ActionListener {


	private SmallWorld smallWorld = null;		// Main class that starts everything.
	private MenuItem menuItem = null;               // Menu item for setEnabled and display of this class.
	private GUICanvas canvas = null;              // Canvas to set color on.
	private TextField agentsField = null;           // Text for number of Agents.
        private TextField widthField = null;            // Text for width.
        private TextField heightField = null;           // Text for height.
        private TextField runsField = null;             // Text for number of iterations to run.        
	private TextField kField = null;		// Text for k.
	private TextField alphaField = null;            // Text for alpha.
	private TextField constantField = null;         // Text for constant.
	private Scrollbar redAgent = null;              // Red level for constructing a color for Agent.
	private Scrollbar blueAgent = null;             // Blue level for Agent.
	private Scrollbar greenAgent = null;            // Green level for Agent.
	private Scrollbar redLine = null;		// Red level for constructing a color for line.
	private Scrollbar blueLine = null;		// Blue level for line.
	private Scrollbar greenLine = null;		// Green level for line.	
	private Checkbox colour = null;                 // Whether to use a colours.
	private Checkbox values = null;                 // Whether to use value as the colours.
	private Checkbox random = null;                 // Whether to use random colours.    
        private CheckboxGroup cbg = null;               // Controls which colour scheme to use.
	private Button ok = null;                       // Save and continue.
	private Button cancel = null;                   // Don't save an continue.
	private GridBagLayout gridBag = null;           // Class level so we can add and remove bits.
        private GridBagConstraints c = null;            // Class level so we can add and remove bits.
        
        
        
        /**
         * Simple constructor. 
         * Needs various bits, mainly to set mask, color and setEnabled on menuitem.
        **/
	Options(SmallWorld sw, GUICanvas cv, MenuItem mi) {
	
		smallWorld = sw;
		menuItem = mi;
		canvas = cv;
		
                // Set up layout.
                
		setSize(300,650);
		gridBag = new GridBagLayout();
		setLayout(gridBag);
		c = new GridBagConstraints();
 
		c.anchor = GridBagConstraints.SOUTHWEST;
		c.insets = new Insets(5, 5, 5, 5); 
		c.ipadx = 2;
		c.ipady = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		agentsField = new TextField("10",10); 
		gridBag.setConstraints(agentsField, c);
		add(agentsField);

 
		c.gridx = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		Label label = new Label("Number of Ring World Agents");
		gridBag.setConstraints(label, c);
		add(label);
                
                
                c.gridx = 0;
		c.gridy++;
		c.gridwidth = 1;
		c.gridheight = 1;
		widthField = new TextField("300",10); 
		gridBag.setConstraints(widthField, c);
		add(widthField);

                
		c.gridx = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		label = new Label("Width of field"); 
		gridBag.setConstraints(label, c);
		add(label);

                
                c.gridx = 0;
		c.gridy++;
		c.gridwidth = 1;
		c.gridheight = 1;
		heightField = new TextField("400",10); 
		gridBag.setConstraints(heightField, c);
		add(heightField);

 		c.gridx = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		label = new Label("Height of field"); 
		gridBag.setConstraints(label, c);
		add(label);           
 
                
                c.gridx = 0;
		c.gridy++;
		c.gridwidth = 1;
		c.gridheight = 1;
		runsField = new TextField("10",10); 
		gridBag.setConstraints(runsField, c);
		add(runsField);

               
		c.gridx = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		label = new Label("Number of iterations"); 
		gridBag.setConstraints(label, c);
		add(label);
                
                
		c.gridx = 0;
		c.gridy++;
		c.gridwidth = 1;
		c.gridheight = 1;
		kField = new TextField("4.0",10); 
		gridBag.setConstraints(kField, c);
		add(kField);

 
		c.gridx = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		label = new Label("Value of k"); 
		gridBag.setConstraints(label, c);
		add(label);


		
		c.gridx = 0;
		c.gridy++;
		c.gridwidth = 1;
		c.gridheight = 1;
		alphaField = new TextField("15.0",10); 
		gridBag.setConstraints(alphaField, c);
		add(alphaField);

 
		c.gridx = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		label = new Label("Value of alpha"); 
		gridBag.setConstraints(label, c);
		add(label);
		
		
		c.gridx = 0;
		c.gridy++;
		c.gridwidth = 1;
		c.gridheight = 1;
		constantField = new TextField("0.0000000001",10); 
		gridBag.setConstraints(constantField, c);
		add(constantField);

 
		c.gridx = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		label = new Label("Value of constant"); 
		gridBag.setConstraints(label, c);
		add(label);
		
		c.gridx = 0;
		c.gridy++;
		c.gridwidth = 1;
		c.gridheight = 1;
		redAgent = new Scrollbar(Scrollbar.HORIZONTAL, 255, 1, 0, 255);
		gridBag.setConstraints(redAgent, c);
		add(redAgent);

		c.gridx = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		label = new Label("Red level for Agents");
		gridBag.setConstraints(label, c);
		add(label);
		

		c.gridx = 0;
		c.gridy++;
		c.gridwidth = 1;
		c.gridheight = 1;
		greenAgent = new Scrollbar(Scrollbar.HORIZONTAL, 0, 1, 0, 255);
		gridBag.setConstraints(greenAgent, c);
		add(greenAgent);

		c.gridx = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		label = new Label("Green level for Agents");
		gridBag.setConstraints(label, c);
		add(label);
		
		c.gridx = 0;
		c.gridy++;
		c.gridwidth = 1;
		c.gridheight = 1;
		blueAgent = new Scrollbar(Scrollbar.HORIZONTAL, 0, 1, 0, 255);
		gridBag.setConstraints(blueAgent, c);
		add(blueAgent);

		c.gridx = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		label = new Label("Blue level for Agents");
		gridBag.setConstraints(label, c);
		add(label);
		
		c.gridx = 0;
		c.gridy++;
		c.gridwidth = 1;
		c.gridheight = 1;
		redLine = new Scrollbar(Scrollbar.HORIZONTAL, 125, 1, 0, 255);
		gridBag.setConstraints(redLine, c);
		add(redLine);

		c.gridx = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		label = new Label("Red level for lines");
		gridBag.setConstraints(label, c);
		add(label);
		

		c.gridx = 0;
		c.gridy++;
		c.gridwidth = 1;
		c.gridheight = 1;
		greenLine = new Scrollbar(Scrollbar.HORIZONTAL, 125, 1, 0, 255);
		gridBag.setConstraints(greenLine, c);
		add(greenLine);

		c.gridx = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		label = new Label("Green level for lines");
		gridBag.setConstraints(label, c);
		add(label);
		
		c.gridx = 0;
		c.gridy++;
		c.gridwidth = 1;
		c.gridheight = 1;
		blueLine = new Scrollbar(Scrollbar.HORIZONTAL, 125, 1, 0, 255);
		gridBag.setConstraints(blueLine, c);
		add(blueLine);

		c.gridx = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		label = new Label("Blue level for lines");
		gridBag.setConstraints(label, c);
		add(label);
	
                cbg = new CheckboxGroup();
                
		c.gridx = 0;
		c.gridy++;
		c.gridwidth = 2;
		c.gridheight = 1;
		colour = new Checkbox("Use scrollbars to colour", cbg, true);
		gridBag.setConstraints(colour, c);
		add(colour);
		
                c.gridx = 0;
		c.gridy++;
		c.gridwidth = 2;
		c.gridheight = 1;
		random = new Checkbox("Use random colours", cbg, false);
		gridBag.setConstraints(random, c);
		add(random);
                
                c.gridx = 0;
		c.gridy++;
		c.gridwidth = 2;
		c.gridheight = 1;
		values = new Checkbox("Use agent values to colour", cbg, false);
		gridBag.setConstraints(values, c);
		add(values);
                
		c.gridx = 0;
		c.gridy++;
		c.gridwidth = 1;
		c.gridheight = 1;
		ok = new Button("Ok");
		gridBag.setConstraints(ok, c);
		ok.addActionListener(this);
		add(ok);

		c.gridx = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		cancel = new Button("Cancel");
		gridBag.setConstraints(cancel, c);
		cancel.addActionListener(this);
		add(cancel);
		
		addWindowListener(new WindowAdapter(){
		    public void windowClosing(WindowEvent e) {
			setVisible(false); 
		    }
		});
	
	} // End of constructor.




        
        /**
         * Called by the main class, but also by this one.
         * Sets various parameters in the main class and canvas class.
        **/
	public void actionPerformed (ActionEvent ae) {

            // If called from the main class, display the gui.
            
	    if (ae.getSource() == menuItem) {
		
		setLocation(250,150);
		setVisible(true);  
		return;
	    } 
	    
	    
	    // If called from the Ok button, set the parameters elsewhere 
            // and hide the options.
                
	    if ((Button)ae.getSource() == ok) {
		try {
		    int agents = Integer.parseInt(agentsField.getText());
		    smallWorld.setNumberOfAgents(agents);
		} catch (NumberFormatException nfe) {
		    smallWorld.setNumberOfAgents(10);
		    agentsField.setText("10");
		}
                try {
		    int width = Integer.parseInt(widthField.getText());
		    smallWorld.setWidth(width);
		} catch (NumberFormatException nfe) {
		    smallWorld.setWidth(300);
		    widthField.setText("300");
		}
                try {
		    int height = Integer.parseInt(heightField.getText());
		    smallWorld.setHeight(height);
		} catch (NumberFormatException nfe) {
		    smallWorld.setHeight(400);
		    heightField.setText("400");
		}
                try {
		    int runs = Integer.parseInt(runsField.getText());
		    smallWorld.setRuns(runs);
		} catch (NumberFormatException nfe) {
		    smallWorld.setRuns(10);
		    runsField.setText("10");
		}
	        try {
		    double k = Double.parseDouble(kField.getText());
		    smallWorld.setK(k);
		} catch (NumberFormatException nfe) {
		    smallWorld.setK(4.0);
		    kField.setText("4.0");
		}
		try {
		    double alpha = Double.parseDouble(alphaField.getText());
		    smallWorld.setAlpha(alpha);
		} catch (NumberFormatException nfe) {
		    smallWorld.setAlpha(15.0);
		    alphaField.setText("15.0");
		}
		try {
		    double constant = Double.parseDouble(constantField.getText());
		    smallWorld.setAlpha(constant);
		} catch (NumberFormatException nfe) {
		    smallWorld.setConstant(0.0000000001);
		    constantField.setText("0.0000000001");
		}
		
		
		canvas.setAgentColor(new Color(redAgent.getValue(), greenAgent.getValue(), blueAgent.getValue()));
		canvas.setLineColor(new Color(redLine.getValue(), greenLine.getValue(), blueLine.getValue()));
		
		if (colour.getState()) {
                    canvas.setColours(0);
                } else if (values.getState()) {
                    canvas.setColours(1);
                } else {
                    canvas.setColours(2);
                }
                
		setVisible(false);
		canvas.repaint();
		
	    } 
	    
            // If called by the cancel button, just hide the options.
                
	    if ((Button)ae.getSource() == cancel) {
	        setVisible(false); 
	    }
                
	} // End of ActionPeformed.
	

        public void setAgentsField(int agents) {
            agentsField.setText(String.valueOf(agents));
        }

// End of class.
}
