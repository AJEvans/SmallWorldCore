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

/**
 * This class provides various helpful texts.<P>
 * It provides "Help" or "About" facilities, depending on how it's called.<P>
 * To Do: Ideally, we'd also use it to display fatal error messages.<P> 
 * @version 0.1
 * @author <A href="http://www.geog.leeds.ac.uk/people/a.evans/">Andy Evans</A>
**/
public class Help extends Frame implements ActionListener {
    
    private MenuItem aboutMenuItem = null;  // Used to determine how it's been called.
    private MenuItem helpMenuItem = null;   // Used to determine how it's been called.
    private TextArea textArea = null;       // Used to display text to the user.
    
    
    /** 
     * Creates a new instance of Help. 
     * Doesn't set it as visual - this is done in the actionPerformed method.
    **/
    public Help(MenuItem aboutMenuItem, MenuItem helpMenuItem) {
        this.aboutMenuItem = aboutMenuItem;
        this.helpMenuItem = helpMenuItem;
        
        setSize(500,500);
        setLocation(200,200);
        textArea = new TextArea("", 100 , 100 , TextArea.SCROLLBARS_VERTICAL_ONLY);
        textArea.setSize(300,600);
        add(textArea);
        
        addWindowListener(new WindowAdapter(){
	    public void windowClosing(WindowEvent e) {
		setVisible(false);
	    }
	});
    }
    
    
    
    
    
    /**
     * Displays the appropriate text to the user.<P>
     * This depends on the menuItem they've used to get here.
    **/
    public void actionPerformed(ActionEvent ae) {
        if (((MenuItem)ae.getSource()).equals(aboutMenuItem)) {
            textArea.setText(aboutText());
            setVisible(true);
        } else {
            textArea.setText(helpText());
            setVisible(true);
        }
    }
    
    
    
    
    
    /**
     * Returns text about the application.
    **/
    private String aboutText() {
        return 
        
        "Small World Agents [1.0] has been produced by the\nMulti Agents " + 
        "Systems and Simulation (MASS) Group at the University of Leeds, UK. \n\n" + 
        
        "It is a working framework for Agent based systems " + 
        "in which Agents can have a geographical location, and a location on a graph of " + 
        "connections between themselves and other Agents.\n\n" + 
        
        "The graphs can be developed into " + 
        "Small World Networks, using the algorithms published in Duncan J. Watt's 1999 paper " +
	"'Networks, Dynamics, and Small World Phenomenon', AJS, 105(2), 493-527. " + 
	"Similar algorithms can be found in his book 'Small Worlds'.\n\n" + 
        
        "The application should not be used for commercial or military purposes. For details " +
        "of the licensing of the source code, please contact the MASS group.\n\n" + 
        
        "http://www.geog.leeds.ac.uk/groups/mass/";
    }
    
    
    
    
    
    /**
     * Returns help text for the application.
    **/
    private String helpText() {
        return 
        
        "Setup:\n\n" + 
        
        "Initially the field is set up to be 300 by 300. This dictates the size of the " + 
        "geographical coordinates that can be used. The graph space uses the same coordinate limits " +
        "To alter the field space choose Other > Options. Note that while the application changes " + 
        "size, old Agents still have coordinate systems that use the old field sizes - coordinates " + 
        "aren't recalculated until new Agents are generated.\n\n" +
        
        "You can either generate a set number of Agents with a random geographical distribution " + 
        "and an initial circular graph of connections (each Agent is connected to the next and the " +
        "last to the first) using the Layout > Ring World option, or you can import Agents written in " + 
        "XML (see 'Importing', below). Making a new Ring World will wipe any imported Agents and vice versa. " +
        "By default 10 Agents are created, though this can be altered in Other > Options. " +
        "As well as their coordinates in the two spaces, the Agents each have a name, a double value, " +
        "and a set of Attributes. If randomly generated these are set to 'Agent-X' (where X is " + 
        "the number of the Agent in the order they were created), zero, and nothing, respectively.\n\n" +
        
        "Viewing:\n\n" +
        "The Agents are displayed initially in geographical space. To view them in graph space, select " +
        "View > Graph Space. The color can be altered under Other > Options in three ways. The default " +
        "is to colour them on the basis of two sets of scrollbars, which control the red, green and blue " +
        "levels of the Agents and the lines of connection. However, they can also be coloured with a " + 
        "rather funky random option, or by the level of the Agent's 'value'. For details of altering the " +
        "value, see 'Running' below. The connections go both ways between connected Agents, however, they " +
        "will be coloured on the basis of only one of the Agents where appropriate.\n\n" +
        "Clicking on a given Agent with your mouse will bring up a dialog displaying the Agent's " + 
        "name, value, and attributes.\n\n" +

        "Connections:\n\n" + 
        "By default random Agents are given connections to the Agents generated just before and after " +
        "themselves, with the last linked to the first. Imported Agents can decide on their own connections. " + 
        "The connections can be subjected to the Small World algorithm of Duncan J. Watt's 1999 paper " + 
	"'Networks, Dynamics, and Small World Phenomenon', AJS, 105(2), 493-527. " +  
	"Similar algorithms can be found in his book 'Small Worlds'. " +
        "To run the algorithm, select Run > Small World it. The parameters for these may be altered from those " +
        "recommended by Watts under Other > Options.\n\n" +
        
        "Running:\n\n" + 
        "The Agents can be run as-is. The default is to run each Agent through 10 iterations. " + 
        "The number of iterations can be changed under Other > Options. The order in which " + 
        "Agents are picked is random within an iteration, though each is picked once each iteration. " + 
        "The number of iterations can be altered under Other > Options. The default behaviour is simply " +
        "to set the Agent's 'value' to the number of neighbours/connections it has in graph space. " + 
        "To generate more complex behaviour, programmers should subclass the Agent class and override " + 
        "their 'update' method, which is called each iteration. The Agent variables are 'protected' so " +
        "they can be used directly by subclasses. By updating each Agent's 'value' property, " + 
        "programmers can easily display changes in the Agent's Attributes'.\n\n" +
        
        "Importing/Exporting:\n\n" +
        "The system will read in XML descriptions of Agents (Layout > Import). The XML used is " + 
        "broadly based on the FIPA Abstract Architecture Specification for Agents " + 
        "(http://www.fipa.org/specs/fipa00001/SC00001L.html#_Toc26668620) " + 
        "though it currently misses the agent-locator property.\n\n" + 
        "The XML for each Agent should be on its own line. The XML tags are:\n" +
        "<AGENT-NAME></AGENT-NAME>\n" +
        "<AGENT-ATTRIBUTE></AGENT-ATTRIBUTE>\n" +
        "The AGENT-NAME must be present. AGENT-ATTRIBUTE tags can contain any name-value pair, " +
        "for example,\n\n " + 
        "<AGENT-ATTRIBUTE>x=200</AGENT-ATTRIBUTE>\n\n" + 
        "x, y, z, graphX, graphY, graphZ, " + 
        "neighbours and value are recognised (case sensitive) and parsed into the relevant Agent variables, " + 
        "all others are stored as KeyObject-Object pairs in the Agent's attributes Hashtable.\n\n" +
        
        "Neighbours/connections should be given in the form " + 
        "<AGENT-ATTRIBUTE>neighbours=one&two&three</AGENT-ATTRIBUTE>. When geographical or graph " + 
        "space coordinates are missing they are generated randomly, or on a circular graph respectively.\n\n" +
        
        "The Layout > Save Agents option saves the current Agents as a file in this XML format.\n\n" + 
        
        "The following is an example of Agents that can be imported into the system which can be " + 
        "cut and pasted into a text file. Not that not all the Agents have the same coordinate information or " + 
        "Attributes and that the system copes with this (see above)...\n\n" + 
        
        "<AGENT-NAME>Ay</AGENT-NAME><AGENT-ATTRIBUTE>x=200</AGENT-ATTRIBUTE><AGENT-ATTRIBUTE>y=220</AGENT-ATTRIBUTE><AGENT-ATTRIBUTE>graphX=60</AGENT-ATTRIBUTE><AGENT-ATTRIBUTE>graphY=10</AGENT-ATTRIBUTE><AGENT-ATTRIBUTE>weight=20</AGENT-ATTRIBUTE><AGENT-ATTRIBUTE>height=50</AGENT-ATTRIBUTE><AGENT-ATTRIBUTE>neighbours=Bee&Dee</AGENT-ATTRIBUTE><AGENT-ATTRIBUTE>hair=black</AGENT-ATTRIBUTE><AGENT-ATTRIBUTE>age=20</AGENT-ATTRIBUTE>\n"+
        "<AGENT-NAME>Bee</AGENT-NAME><AGENT-ATTRIBUTE>x=100</AGENT-ATTRIBUTE><AGENT-ATTRIBUTE>y=120</AGENT-ATTRIBUTE><AGENT-ATTRIBUTE>weight=120</AGENT-ATTRIBUTE><AGENT-ATTRIBUTE>height=150</AGENT-ATTRIBUTE><AGENT-ATTRIBUTE>graphX=10</AGENT-ATTRIBUTE><AGENT-ATTRIBUTE>graphY=60</AGENT-ATTRIBUTE><AGENT-ATTRIBUTE>neighbours=Ay&Dee</AGENT-ATTRIBUTE><AGENT-ATTRIBUTE>hair=blonde</AGENT-ATTRIBUTE><AGENT-ATTRIBUTE>age=30</AGENT-ATTRIBUTE>\n" + 
        "<AGENT-NAME>See</AGENT-NAME><AGENT-ATTRIBUTE>weight=150</AGENT-ATTRIBUTE><AGENT-ATTRIBUTE>height=180</AGENT-ATTRIBUTE><AGENT-ATTRIBUTE>graphX=15</AGENT-ATTRIBUTE><AGENT-ATTRIBUTE>graphY=85</AGENT-ATTRIBUTE>\n" +
        "<AGENT-NAME>Dee</AGENT-NAME><AGENT-ATTRIBUTE>x=150</AGENT-ATTRIBUTE><AGENT-ATTRIBUTE>y=150</AGENT-ATTRIBUTE><AGENT-ATTRIBUTE>weight=110</AGENT-ATTRIBUTE><AGENT-ATTRIBUTE>height=160</AGENT-ATTRIBUTE><AGENT-ATTRIBUTE>neighbours=Bee&Ay</AGENT-ATTRIBUTE><AGENT-ATTRIBUTE>hair=brown</AGENT-ATTRIBUTE><AGENT-ATTRIBUTE>age=25</AGENT-ATTRIBUTE>";

    } // End of helpText.
    
// End of class.  
}
