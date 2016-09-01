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


import java.awt.image.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;


/**
 * Simple class to encapsulate the drawing of images, agents, and connections.
 * The class starts off as an empty canvas, you can then add images and agents 
 * to it. Lines are drawn between agents and their network neighbours.<P>
 * @author <A href="http://www.geog.leeds.ac.uk/people/a.evans/">Andy Evans</A>
 * @version 0.13
**/
public class GUICanvas extends Canvas implements MouseListener {

    private Image displayImage = null;      // Image to display.
    private Vector agents = null;	    // Agents to display.
    private boolean real = true;	    // If true, displays the real geography, if false, graph space.
    private Color agentColor = Color.red;   // Colour for Agents.
    private Color lineColor = Color.gray;   // Colour for lines.
    private int colourMode = 0;             // Colour scheme to use for each agent. 0 = scrollbars,
                                            // 1 = using values, 2 = random.
    
    
    /**
     * Simple constructor.
     * Just calls the standard Canvas constructor and adds 
     * itself as a mouse listener.
    **/
    public GUICanvas() {
		super();
                addMouseListener(this);
    }

    
    
    
    
    /**
     * Pass in an image to get it added to the canvas.
     * You'll have to call the canvas's repaint method 
     * to actually show it.
    **/
    public void setDisplayImage (Image dispImage) {
		displayImage = dispImage;
    }

    
    
    
    
    /**
     * Method to get the image stored in the canvas.
     * Returns null is there is no image.
    **/
    public Image getDisplayImage () {
		return displayImage;
    }

    
    

    
    /**
     * Pass a Vector of agents in to get it added to the canvas.
     * You'll have to call the canvas's repaint method 
     * to actually show them.
    **/
    public void setAgents (Vector v) {
		agents = v;
    }

    
    
    
    
    /**
     * Get the agents currently stored in the canvas.
    **/
    public Vector getAgents () {
		return agents;
    }

    
    
    
    
    /**
     * Sets the display mode.
     * If true, displays the real geography, if false, graph space.
    **/
    public void setReal (boolean b) {
	real = b;
    }
    
    
    
    
    
    /**
     * Set the colour for each agent.
     * Only used if set in the options, otherwise random colours used.
     * Default is red.
    **/
    public void setAgentColor(Color c) {
	agentColor = c;
    }
    
    
    
    
    
    /**
     * Set the colour for each line.
     * Only used if set in the options, otherwise random colours used.
     * Default is gray.
    **/
    public void setLineColor(Color c) {
	lineColor = c;
    } 
    
    
    
    
    
    /**
     * Colour scheme to use for each agent. 
     * 0 = scrollbars, 1 = using values, 2 = random.
    **/
    public void setColours(int mode) {
	colourMode = mode;
    }
    
    
    
    
    
    /**
     * Paints the image etc.
     * If the image has been set with setDisplayImage, it is painted.
     * If the agents have been set, they are painted plus their links to 
     * network neighbours.
    **/ 
    public void paint (Graphics g) {
		
		
        // If there's an image, paint it.
        
	if (displayImage != null) {
            g.drawImage(displayImage, 0, 0, this);
	} 
	
        
	
        // If there are agents, paint them.
        
	if (agents != null) {
	    
            // Calculate the value range of the Agents for colour stretching.
            
            double max = 0;
            double min = Double.MAX_VALUE;
            double range = 0;
                
            if (colourMode == 1) {
                if (colourMode == 1) {
                    for (int i = 0; i < agents.size(); i++) {
                        double value = ((Agent)agents.elementAt(i)).getValue();
                        if (value > max) max = value;
                        if (value < min) min = value;
                    }
                }
                range = max - min;
            }
            
	    // If real geography, use their geographical X and Y.
	    
	    if (real == true) {
		for (int i = 0; i < agents.size(); i++) {
		    
                    Agent temp = (Agent) agents.elementAt(i);
                    
		    // Draw the agent.
                    
		    if (colourMode == 2) {
			float hue = (float)Math.random(); 
			agentColor = Color.getHSBColor(hue, (float)1, (float)1);
			lineColor = Color.getHSBColor(hue, (float)0.7, (float)1);
		    } else if (colourMode == 1) {
                        int level = 235 - (int)(((temp.getValue()-min)/range)*235.0); // 235 to remove white.
                        agentColor = new Color(level, level, level);
			lineColor = new Color(level, level, level);
                    }

		    g.setColor(agentColor);
		    g.fillRect(temp.getX(), temp.getY(),3, 3);
		    
		    // Link to its neighbours positions.

                    Vector neighbours = temp.getNeighbours();

                    if (neighbours.isEmpty() == false) {
                        for (int j = 0; j < neighbours.size(); j++) {
                            g.setColor(lineColor);
                            Agent neighbour = (Agent)neighbours.elementAt(j);
                            g.drawLine(temp.getX(),temp.getY(),neighbour.getX(),neighbour.getY());
                        }
                    }
                }
		
            // If graph space to be displayed, shift the origin to the center of the 
	    // screen and use their display positions in graph space.
		
	    } else {
		
		int radius = (getWidth() - 10)/2;
		
		g.translate(radius + 5, radius + 5);
		
		for (int i = 0; i < agents.size(); i++) {
		    
                    Agent temp = (Agent) agents.elementAt(i);
                    
		    // Draw the agent.
                    
		    if (colourMode == 2) {
			float hue = (float)Math.random();
			agentColor = Color.getHSBColor(hue, (float)1, (float)1);
			lineColor = Color.getHSBColor(hue, (float)0.7, (float)1);
		    } else if (colourMode == 1) {
                        int level = 235 - (int)(((temp.getValue()-min)/range)*235.0); // 235 to remove white.
                        agentColor = new Color(level, level, level);
			lineColor = new Color(level, level, level);
                    }
		    
		    g.setColor(agentColor);
		    g.fillRect(temp.getGraphX(), temp.getGraphY(),3, 3); 
		    Vector neighbours = temp.getNeighbours();
		    
		    // Link to its neighbours positions.		    
		    
                    if (neighbours.isEmpty() == false) {
                        for (int j = 0; j < neighbours.size(); j++) {
                            g.setColor(lineColor);
                            Agent neighbour = (Agent)neighbours.elementAt(j);
                            g.drawLine(temp.getGraphX(),temp.getGraphY(),neighbour.getGraphX(),neighbour.getGraphY());
                        }
                    }
		}
		
		// Reset the zero point on the canvas.
		
		g.translate(-radius - 5, -radius - 5);
		   
	    } // End of whether to display the real or graph space.
	    
	} // End of if there are agents to display.
		
    } // End of paint.
    
    
    
    
    
    /** 
     * Gets a list of an Agent's properties when the user clicks on it.
     * The list is given in the order Name, Value, Attributes. 
    */
    public void mouseClicked(MouseEvent e) {
        
        int x = e.getX();
        int y = e.getY();
        
        // Find the Agent they've clicked on.
        
        for (int i = 0; i < agents.size(); i++) {
            
            Agent agent = (Agent)agents.elementAt(i);
            int agentX = 0;
            int agentY = 0;
            if (real == true) {
                agentX = agent.getX();
                agentY = agent.getY();
            } else {
                int radius = (getWidth() - 10)/2;
                agentX = agent.getGraphX() + radius + 5;
                agentY = agent.getGraphY() + radius + 5;
            }
            
            // Send its properties to the reporting script. 
            // This is kept separate incase we want a different interface 
            // or reporting format.
            
            if (((x > agentX - 3) && (x < agentX + 3)) && ((y > agentY - 3) && (y < agentY + 3))) {
                report(agent.getName(), agent.getValue(), agent.getAttributes());
            }
        }
    }
    
    
    
    
    
    /** 
     * Displays a popup list of an Agent's properties when the user clicks on it.
     * The list is given in the order Attribute, Value, Properties. 
     * Where an Attribute exists, it is used as the name of the window.
     * Removed from mouseClicked to allow different implementations, for example, 
     * tooltips.
    */
    private void report(String name, double value, Hashtable attributes) {
        
        Frame frame = new Frame(name);
        Label label = null;

        // Resize the frame so it can contain all the text.
        // XXXX Ideally we need to find the longest property and resize horizontally as well.
        
        if (attributes != null) {
            frame.setSize(200, (attributes.size()*50 + 100));
        } else {
            frame.setSize(200,100);
        }
        
        GridBagLayout gridBag = new GridBagLayout();
        frame.setLayout(gridBag);
        GridBagConstraints c = new GridBagConstraints();
     
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(5, 5, 5, 5);
        c.ipadx = 2;
        c.ipady = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        
        label = new Label("value = " + Double.toString(value));
        gridBag.setConstraints(label, c);
        frame.add(label);
        
        // Add the properties.
        
        if (attributes != null) {
            String attributeString = (attributes.toString()).trim();
            attributeString = attributeString.substring(1,attributeString.length()-1);
            StringTokenizer st = new StringTokenizer(attributeString,", ");
            while (st.hasMoreTokens()) {
                c.gridy++;
                label = new Label(st.nextToken());
                gridBag.setConstraints(label, c);
                frame.add(label);
            }
        }
        
        frame.setLocation(350,350);
        frame.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e) {
                ((Frame)e.getComponent()).setVisible(false);
            }
        });
        frame.setVisible(true);
        
    } // End of report.

    
    
    
    
    /** 
     * Invoked when the mouse enters a component.
     * Empty method.
    */
    public void mouseEntered(MouseEvent e) {
    }
    
    
    
    
    
    /** 
     * Invoked when the mouse exits a component.
     * Empty method.
    */
    public void mouseExited(MouseEvent e) {
    }

    
    
    
    
    /** 
     * Invoked when a mouse button has been pressed on a component.
     * Empty method.
    */
    public void mousePressed(MouseEvent e) {
    }
    
    
    
    
    
    /** 
     * Invoked when a mouse button has been released on a component.
     * Empty method.
    */
    public void mouseReleased(MouseEvent e) {
    }


// End of class.
}
