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


import java.util.*;

/**
 * A geographical agent class with network connection.
 * Agents can have an x/y/z coordinate, a double value and 
 * a String name, as well as a series of networked neighbours.
 * The update method alters the Agent's properties.
 * Subclasses should override this method. They can use the protected neighbours 
 * variable and others if they are needed. Default test behaviour for this 
 * super class's update method is to set the value variable to 
 * the number of neighbours. Properties can be set 
 * using the protected properties hashtable variable.<P>
 * @author <A href="http://www.geog.leeds.ac.uk/people/a.evans/">Andy Evans</A>
 * @version 0.13
 */
public class Agent {
    
    // All variables can be accessed by subclasses.
    
    protected Vector neighbours = null;         // Network neighbours. 
    protected int x = -1;			// Geographical x.
    protected int y = -1;			// Geographical y.
    protected int z = -1;			// Geographical z.
    protected String name = null;		// What it is. Easy to get string property or name.
    protected double value = 0;			// What its value is. Easy to get double property.
    protected Hashtable attributes = null;      // Holds more complex Agent attributes.
    protected int graphX = -1;			// Display X position for graph space.
    protected int graphY = -1;			// Display y position for graph space.
    protected int graphZ = -1;			// Display z position for graph space.    
    
    
    /** 
     * Creates a new instance of Agent. 
    **/
    public Agent() {
         neighbours = new Vector();
    }
    
    
    
    
    
    /**
     * Adds a network neighbour.
     * Note that these don't have to be geographical links.
    **/
    public void addNeighbour(Agent a) {
	neighbours.add(a);
    }
    
    
    
    
    
    /**
     * Removes a network neighbour.
     * Up to developers to ask for neigbours that exist.
    **/
    public void removeNeighbour(Agent a) {
	neighbours.remove(a);
    }
    
    
    
    
    
    /**
     * Gets all the network neighbour.
     * Returns null is there are none.
    **/
    public Vector getNeighbours() {
        return neighbours;
    }
    
    
    
    
    
    /**
     * Runs one iteration of the Agent.
     * Alters the Agent's attributes.
     * Subclasses should override this method using getNeighbours 
     * if they are needed. Default test behaviour is to set the  
     * the value variable to the number of neighbours. 
    **/   
    public void update() {
        value = neighbours.size();
    }
    
    
    
    
    
    /**
     * Sets geographical x.
     * The default value is -1.
    **/
    public void setX(int x) {
	this.x = x;
    }
    
    
    
    
    
    /**
     * Gets geographical x.
     * Returns -1 if not set.
    **/
    public int getX() {
	return x;
    }
    
    
    
    
    
    /**
     * Sets geographical y.
     * The default value is -1.
    **/
    public void setY(int y) {
	this.y = y;
    }
    
    
    
    
    
    /**
     * Gets geographical y.
     * Returns -1 if not set.
    **/   
    public int getY() {
	return y;
    }
    
    
    
    
    
    /**
     * Sets geographical z.
     * The default value is -1.
    **/
    public void setZ(int z) {
	this.z = z;
    }
    
    
    
    
    
    /**
     * Gets geographical z.
     * Returns -1 if not set.
    **/   
    public int getZ() {
	return z;
    }
    
    
    
    
    /**
     * Sets X position in graph display space.
     * The default value is -1.
    **/
    public void setGraphX(int x) {
	this.graphX = x;
    }
    
    
    
    
    
    /**
     * Gets X position in graph display space.
     * The default value is -1.
    **/
    public int getGraphX() {
	return graphX;
    }
    
    
    
    
    
    /**
     * Sets Y position in graph display space.
     * The default value is -1.
    **/
    public void setGraphY(int y) {
	this.graphY = y;
    }
        
    
    
    
        
    /**
     * Gets Y position in graph display space.
     * The default value is -1.
    **/
    public int getGraphY() {
	return graphY;
    }  
    
    
    
    
    
    /**
     * Sets Z position in graph display space.
     * The default value is -1.
    **/
    public void setGraphZ(int z) {
	this.graphZ = z;
    }
    
    
    
    
    
        
    /**
     * Gets Z position in graph display space.
     * The default value is -1.
    **/
    public int getGraphZ() {
	return graphZ;
    } 
    
    
    
    
    
    /**
     * Sets the value.
     * The default value is 0.
    **/
    public void setValue(double x) {
	value = x;
    }
    
    
    
    
    
    /**
     * Gets the value.
     * Returns 0 if not set.
    **/   
    public double getValue() {
	return value;
    }
    
    
    
    
    
    /**
     * Sets the name.
     * The default value is null.
    **/
    public void setName(String a) {
	this.name = a;
    }
    
    
    
    
    
    /**
     * Gets the name.
     * Returns null if not set.
    **/   
    public String getName() {
	return name;
    }
    
    
    
    
    
    /**
     * Sets the attributes of the Agent.
     * Used for storing more complex properties than name or value.
    **/      
    public void setAttributes(Hashtable attributes) {
        this.attributes = attributes;
    }
        
    
    
    
    
    /**
     * Gets the attributes of the Agent.
     * Used to display properties on the interface.
    **/      
    public Hashtable getAttributes() {
        return attributes;
    }
    
    
// End of class.
}
