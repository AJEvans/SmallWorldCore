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
 * [online] https://github.com/AJEvans/SmallWorldCore/
 * 
 *
 * --End of Copyright notice-- 
 *
 */



import java.awt.event.*;
import java.awt.*;
import java.util.*;

/**
 * A class to do a very basic "geographical" small world simulation.
 * The code generates 10 agents in a 300 by 300 field. The algorithms 
 * for small world connecting are broadly taken from Duncan J. Watt's 1999 paper 
 * "Networks, Dynamics, and Small World Phenomenon", AJS, 105(2), 493-527. 
 * Similar algorithms can be found in his book "Small Worlds".<P>
 * To do: Graphing.<P> 
 * @author <A href="http://www.geog.leeds.ac.uk/people/a.evans/">Andy Evans</A>
 * @version 0.15
 **/
public class SmallWorld extends Frame implements ActionListener  {
    
    private GUICanvas canvas = null;		    // Display.
    private Vector agents = null;		    // Store of agents.
    private int numberOfAgents = 10;		    // Alter this for more/less agents.
    private double constant = 0.0000000001;	    // Watt's constant.
    private double alpha = 15.0;		    // Watt's alpha.
    private double k = 4.0;			    // Watt's k.
    private int width = 300;			    // Width of application - also used to determine graph radius.
    private int height = 400;			    // Height of application.
    private int radius = (width/2) - 10;	    // Radius for graph display.
    private double angle = 360.0 / (double)numberOfAgents;  // Number of degrees for each agent on graph layout.
    private int runs = 10;                          // Number of iterations to run.
    private MenuItem smallWorldMenuItem = null;	    // Menu option to run smallworld conversion.
    private MenuItem ringWorldMenuItem = null;	    // Menu option to generate a fresh ring world.  
    private MenuItem viewRealMenuItem = null;	    // Menu option to display the real geography.
    private MenuItem viewGraphMenuItem = null;	    // Menu option to display the graph space.
    private MenuItem runMenuItem = null;	    // Menu option to run the agents.
    private MenuItem importMenuItem = null;	    // Menu option to import the agents.
    private MenuItem saveMenuItem = null;	    // Menu option to save the agents.
    private Options options = null;                 // Listener for options menu.
    private Label helpLabel = null;                 // Used for pointing out functionality to users. 
    
    /** 
     * Sets the whole thing going.
    **/
    public SmallWorld() {
	
	// Set up display.

        super("Small World Agents [1.0]");
        
	MenuBar menuBar = new MenuBar();
	setMenuBar(menuBar);

        Menu layoutMenu = new Menu("Layout"); 
	menuBar.add(layoutMenu);
        
        ringWorldMenuItem = new MenuItem("Ring World");
	layoutMenu.add(ringWorldMenuItem);
	ringWorldMenuItem.addActionListener(this);
        
        importMenuItem = new MenuItem("Import Agents");
	layoutMenu.add(importMenuItem);
        
        saveMenuItem = new MenuItem("Save Agents");
	layoutMenu.add(saveMenuItem);
	saveMenuItem.setEnabled(false);
        
        AgentIO agentIO = new AgentIO(this, importMenuItem, saveMenuItem);
        
	importMenuItem.addActionListener(agentIO);
        saveMenuItem.addActionListener(agentIO);
	
	Menu runMenu = new Menu("Run"); 
	menuBar.add(runMenu);

	smallWorldMenuItem = new MenuItem("Small World it");
	runMenu.add(smallWorldMenuItem);
	smallWorldMenuItem.addActionListener(this);
	smallWorldMenuItem.setEnabled(false);

        runMenuItem = new MenuItem("Run Agents");
	runMenu.add(runMenuItem);
	runMenuItem.addActionListener(this);
        runMenuItem.setEnabled(false);
        
	Menu viewMenu = new Menu("View"); 
	menuBar.add(viewMenu);
	
	viewRealMenuItem = new MenuItem("View real geography");
	viewMenu.add(viewRealMenuItem);
	viewRealMenuItem.addActionListener(this);
	viewRealMenuItem.setEnabled(false);
	
	viewGraphMenuItem = new MenuItem("View graph geography");
	viewMenu.add(viewGraphMenuItem);
	viewGraphMenuItem.addActionListener(this);
	        
        Menu otherMenu = new Menu("Other"); 
	menuBar.add(otherMenu);
        
        MenuItem aboutMenuItem = new MenuItem("About Small World Agents");
        otherMenu.add(aboutMenuItem);
      	MenuItem helpMenuItem = new MenuItem("Help");
	otherMenu.add(helpMenuItem);
        Help help = new Help(aboutMenuItem, helpMenuItem);
        aboutMenuItem.addActionListener(help);
        helpMenuItem.addActionListener(help);
        
        
	MenuItem optionsMenuItem = new MenuItem("Options");
	otherMenu.add(optionsMenuItem);

        
        GridBagLayout gridBag = new GridBagLayout();
	setLayout(gridBag);
	GridBagConstraints c = new GridBagConstraints();
        
        c.anchor = GridBagConstraints.SOUTHWEST;
	c.insets = new Insets(5, 5, 5, 5); 
	c.ipadx = 2;
	c.ipady = 2;
	c.fill = GridBagConstraints.HORIZONTAL;
		
	c.gridx = 0;
	c.gridy = 0;
	c.gridwidth = 1;
	c.gridheight = 1;
        
        canvas = new GUICanvas();
	canvas.setSize(width + 2, height + 2);
	gridBag.setConstraints(canvas, c);
        add(canvas);
        
        options = new Options (this, canvas, optionsMenuItem);
	optionsMenuItem.addActionListener(options); 
        
        c.gridy++;
        helpLabel = new Label(" Pick a Layout to start (Options are under 'Other')");
        helpLabel.setSize(150,50);
        gridBag.setConstraints(helpLabel, c);
        add(helpLabel);
	
	setLocation(200,200);
	setSize(width + 12,height + 102);
	addWindowListener(new WindowAdapter(){
	    public void windowClosing(WindowEvent e) {
		System.exit(0);
	    }
	});
	
	setVisible(true);
	
	
    } // End of constructor.
    
    
    
   
    
    /**
     * Listens to menus and does stuff.
    **/
    public void actionPerformed(ActionEvent ae) {
	
	// Make a new ring world of random agents 
	// with a link between each and the last make.
	
	if (ae.getSource() == ringWorldMenuItem) {
	    
	    makeAgents();

	    // Paint the agents and allow the user to small world and run it.
	
	    canvas.setAgents(agents);
	    canvas.repaint();
	    smallWorldMenuItem.setEnabled(true);
	    runMenuItem.setEnabled(true);
            saveMenuItem.setEnabled(true);
            helpLabel.setText(" Click on Agents for info");
            doLayout();
            
	// Else the user wants to small world it.
	    
	} 
	
	// Run the small world generation on the current graph. 
	// XXXX We need to decide on whether to remove the initial ring structure and how.
	
	if (ae.getSource() == smallWorldMenuItem) {
	    
	    // Number of connections so we can determine small-worldness.
	    	    
	    int connections = 0;
	    
	    while (connections < (k*numberOfAgents)/2) {
		
		// Get a random list of agents.
		
		Vector randomAgents = agents;
		randomAgents = shuffle(randomAgents);
			
		// Run through random list of agents and link to one neighbour from each.
		
		for (int r = 0; r < randomAgents.size(); r++) {
		    
		    Agent agent = (Agent)randomAgents.elementAt(r);
		    
		    // Set up some variables to store results, and get a list of its neighbours. Note that our array
		    // is numberOfAgents long just because I'm too lazy to work out the alternative for agentsNeighbours.size().
		    
		    double [] propensities = new double[numberOfAgents];
		    propensities[r] = -1;
		    double sumOfPropensities = 0;
		    
		    Vector agentsNeighbours = agent.getNeighbours();
		    
		    // Run through all the other agents, and find out how many neighbours they have in common
		    // with our current agent.
		    
		    for (int i = 0; i < numberOfAgents; i++) {
			
			// If we'return not dealing with the agent we picked, calculate the 
			// number of mutual neighbours it has with all other agents.
			
			if (i != r) {
			    			    
			    double neighbours = 0;
			    
			    Vector othersNeighbours = ((Agent)agents.elementAt(i)).getNeighbours();
			    
			    for (int y = 0; y < othersNeighbours.size(); y++) {
				
				if (agentsNeighbours.contains(othersNeighbours.elementAt(y))) {
				    neighbours++;
				}
				
			    }
			    
			    // Use the number of mutual neighbours to calculate propensity 
			    // of our agent to join with each of the others (with some special conditions 
			    // to prevent negative numbers coming up).
			    
			    if (neighbours == 0) {
				propensities[i] = constant;
			    } else if (neighbours >= k) {
				propensities[i] = 1;
			    } else {
				propensities[i] = ((Math.pow( ((((double)neighbours) - 1.0)/k), alpha))*(1.0-constant)) + constant;
			    }
			    sumOfPropensities = sumOfPropensities + propensities[i];
			    
			} // End of checking we're not testing our agent.
			
		    } // End of running through all other agents.
		    
		    // Normalize the propensities.
		    
		    for (int j = 0; j < propensities.length; j++) {
			if (propensities[j] > -1) propensities[j] = propensities[j] / sumOfPropensities;
			else propensities[j] = 0;
		    }
		    
		    // Distribute the propensities for Monte Carlo style picking.
		    
		    for (int j = 1; j < (propensities.length - 1); j++) {
			propensities[j] = propensities[j] + propensities[j-1];
		    }
		    
		    // Given these propensities pick one neighbour and link to it. 
		    
		    double randomDouble = Math.random();
    
		    int neighbour = propensities.length - 1;
		    
		    for (int j = 0; j < propensities.length; j++) {
			if (randomDouble < propensities[j]) {
			    neighbour = j;
			    break;
			}
		    }
		    
		    // Provided the agents aren't already neighbours, link them.
		    
		    Agent neighbourAgent = (Agent)agents.elementAt(neighbour);
		    
		    if (agentsNeighbours.contains(agents.elementAt(neighbour)) == false) {
			agent.addNeighbour(neighbourAgent);
		    }
		    if (neighbourAgent.getNeighbours().contains(randomAgents.elementAt(r)) == false) {
			neighbourAgent.addNeighbour(agent);
		    }
		    
		    connections++;
		    
		} // End of looping through each agent.
		
	    } // End of testing for small-worldness.
	    
	    canvas.repaint();
	    
	} // End of small-worlding.
	
	
	// Display the real geography.
	
	if (ae.getSource() == viewRealMenuItem) {
	    viewGraphMenuItem.setEnabled(true);
	    viewRealMenuItem.setEnabled(false);
	    canvas.setReal(true);
	    canvas.repaint();
	}
	
	// Display the graph space.
	
	if (ae.getSource() == viewGraphMenuItem) {
	    viewGraphMenuItem.setEnabled(false);
	    viewRealMenuItem.setEnabled(true);
	    canvas.setReal(false);
	    canvas.repaint();
	}
	
        // Run through all the agents calling their update methods.
        
        if (ae.getSource() == runMenuItem) {
            
            for (int i = 0; i < runs; i++) {
                
                Vector randomAgents = agents;
                randomAgents = shuffle(randomAgents); 
                
                for (int r = 0; r < randomAgents.size(); r++) {
		    Agent agent = (Agent)randomAgents.elementAt(r);
                    agent.update();
                }
                
            }
            canvas.repaint();
        }
        
        
	
    } // End of ActionPerformed.
    
    
    
    
    
    /**
     * Sets up the agents.
     * Called by making a new ringworld, or by changing the 
     * number of agents in the options.
    **/
    private void makeAgents() {
	
	// Run through and make new agents.
	
	agents = new Vector();
	Agent lastAgent = null;
	
	for (int i = 0; i < numberOfAgents; i++) {
	    
	    Agent temp = new Agent();
	    
            temp.setName("Agent-" + i);
            
	    // Random geographical position.
	    
	    int x = (int)(Math.random() * width);
	    int y = (int)(Math.random() * height);
	    
	    temp.setX(x);
	    temp.setY(y);
	    
	    // Calculate graph space display position (works in radians).
	    
	    double positionAngle = ((double)i * angle * Math.PI)/180.0;
	    
	    int graphX = (int)(radius * Math.sin(positionAngle));
	    int graphY = (int)(radius * Math.cos(positionAngle));
	    
	    temp.setGraphX(graphX);
	    temp.setGraphY(graphY);
	    
	    // Connect each agent with the last one, vice versa,
	    // and the first and last.
	    
	    if (lastAgent != null) {
		temp.addNeighbour(lastAgent);
		lastAgent.addNeighbour(temp);
	    }
	    
	    if (i == (numberOfAgents - 1)) {
		lastAgent = (Agent) agents.elementAt(0);
		temp.addNeighbour(lastAgent);
		lastAgent.addNeighbour(temp);
	    }
	    
	    agents.add(temp);
	    lastAgent = temp;
	}
	
    } // End of makeAgents.
    
    
    
    
    
    /** 
     * Routine for shuffling a Vector.
     * This is just so we don't have to use any Java 1.2 
     * Collections etc.
    **/
    private Vector shuffle(Vector v) {
	
	// Working in arrays is just a good deal easier 
	// than working out the Vector positions.
	
	Object [] vectorArray = new Object[v.size()];

	for (int i = 0; i < numberOfAgents; i++)
	    vectorArray[i] = v.elementAt(i);
	
        for(int i = 0; i < numberOfAgents; i++){

	    int j = (int)(Math.random()*numberOfAgents);

            if (i != j) {
                Object temp = vectorArray[i];
		vectorArray[i] = vectorArray[j];
		vectorArray[j] = temp;

            }
        }

	v.removeAllElements();
	for (int i = 0; i < numberOfAgents; i++)
	    v.add(vectorArray[i]);
	
	return v;
    }
    
    
    
    
    
    /**
     * Sets the level of Watt's K.
     * Default is 4.0.
    **/
    public void setK (double k) {
	this.k = k;
    }
    
    
    
    
    
    /**
     * Sets the level of Watt's alpha.
     * Default is 15.0.
    **/
    public void setAlpha (double alpha) {
	this.alpha = alpha;
    }
    
    
    
    
    
    /**
     * Sets the level of Watt's constant.
     * Default is 0.0000000001.
    **/
    public void setConstant (double constant) {
	this.constant = constant;
    }
    
    
    
    
    
    /**
     * Sets the number of iterations to run.
     * Default is 10.
    **/
    public void setRuns (int iterations) {
	runs = iterations;
    }
    
    
    
    
    
    /**
     * Sets the height of the geographical area.<P>
     * This is used to set the size of the application.
     * Default is 400.
    **/
    public void setHeight (int height) {
        this.height = height;
        setSize(width + 12, height + 102);
        canvas.setSize(width,height);
        doLayout();
    }
    
    
    
    
    
    /**
     * Sets the width of the geographical area.<P>
     * This is used to set the size of the application.
     * Default is 300.
    **/
    public void setWidth (int width) {
        this.width = width;
        radius = (width/2) - 10;
        setSize(width + 12, height + 102);
        canvas.setSize(width,height);
        doLayout();
    }   

    
    
    
    
    /**
     * Sets the agents.
     * We do the cleaning up of missing coordinates in this code 
     * rather than, for example AgentIO, because we know what is 
     * required to display here - we don't make those assumptions in AgentIO.
     **/
    public void setAgents(Vector agents) {
        
        this.agents = agents;
        
        // Recalculate the graph space angle incase we need it,
        // and adjust the number of Agents where appropriate.
        
        if (numberOfAgents != agents.size()) {
            numberOfAgents = agents.size();
            angle = 360.0 / (double)numberOfAgents;
            options.setAgentsField(numberOfAgents);
        }
        
        // If necessary, give the default (random) geographical and (circlular) graph space values.
        
        for (int i = 0; i < agents.size(); i++) {
            Agent agent = (Agent)agents.elementAt(i);
            if (agent.getX() == -1) agent.setX((int)(Math.random() * width));
            if (agent.getY() == -1) agent.setY((int)(Math.random() * height));
            double positionAngle = ((double)i * angle * Math.PI)/180.0;
	    if (agent.getGraphX() == -1) agent.setGraphX((int)(radius * Math.sin(positionAngle)));
            if (agent.getGraphY() == -1) agent.setGraphY((int)(radius * Math.cos(positionAngle)));
        }

        // Set up GUI.
        
        canvas.setAgents(agents);
        smallWorldMenuItem.setEnabled(true);
        saveMenuItem.setEnabled(true);
        runMenuItem.setEnabled(true);
        helpLabel.setText(" Click on Agents for info");
        doLayout();
        canvas.repaint();
        
    } // End of setAgents.
    
    
    
    
    
    /**
     * Gets the Agents.
     * Used mainly in saving Agents.
    **/   
    public Vector getAgents() {
        return agents;
    }
    
    
    
    
    
    /**
     * Sets the number of agents.
     * Default is 10.
    **/
    public void setNumberOfAgents (int numberOfAgents) {
	
	if (this.numberOfAgents != numberOfAgents) {
	    
	    this.numberOfAgents = numberOfAgents;
	    angle = 360.0 / (double)numberOfAgents;
	    
	    makeAgents();
	    canvas.setAgents(agents);
	    
	    // If we're already showing the first agents, repaint.
	    
	    if (smallWorldMenuItem.isEnabled() == true) {
		canvas.repaint();
	    }
	    
	}
	
    }
    
    
    
    
    
    /**
     * Start up method.
     * Just calls the constructor.
     */
    public static void main(String[] args) {
	new SmallWorld();
    }
 
// End of class.
}
