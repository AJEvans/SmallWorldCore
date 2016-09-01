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


import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;


/**
 * Import/Export class that reads and writes agents to files.<P>
 * Reads and writes XML files. For details see the appropriate methods.<P>
 * @author <A href="http://www.geog.leeds.ac.uk/people/a.evans/">Andy Evans</A>
 * @version 0.11
**/
public class AgentIO implements ActionListener {
   
    private MenuItem importMenuItem = null; // Reference so we know if we're importing.
    private MenuItem saveMenuItem = null;   // Reference so we know if we're exporting.
    private SmallWorld smallWorld = null;   // Reference so we can pass back agents.
    
    
    /**
     * Creates a new instance of AgentIO.
     **/
    public AgentIO(SmallWorld smallWorld, MenuItem importMenuItem, MenuItem saveMenuItem) {
        this.smallWorld = smallWorld;
        this.importMenuItem = importMenuItem;
        this.saveMenuItem = saveMenuItem;
    }
    

    
    
    /**
     * Checks whether the user wants to read or write and calls the appropriate methods.
    **/
    public void actionPerformed(ActionEvent ae) {
        if (((MenuItem)ae.getSource()).equals(importMenuItem)) {
            importAgents();
        } else {
            exportAgents();
        }
    }
    
    
    /**
     * Asks the user for a file and reads or writes it.
    **/
    private void importAgents() {

        // Get a file from the user. Note theres no checks here on type yet. XXXX.
        
        FileDialog openDialog = new FileDialog(new Frame(), "Open file", FileDialog.LOAD);
        openDialog.show();
        File file = new File(openDialog.getDirectory() + openDialog.getFile());
        
        if ((openDialog.getDirectory() == null) || (openDialog.getFile() == null)) return;
        
        // Make somewhere to store all our agents.
        
        Vector agents = new Vector();
        
        try {
            
            BufferedReader buff = new BufferedReader(new FileReader(file));
            String line = buff.readLine();
            
            // There seems to be a problem on some systems where
            // end of lines in text files are recognised by java as
            // a second, blank, line. We get rid of these by checking the
            // length of the line after calling trim().
            
            if (line != null) {
                line.trim();
                if (line.length() == 0) line = buff.readLine();
            }
            
            // For each line in the file, get an Agent using the parseLine
            // method (below) and add it to our store. Note that this doesn't
            // deal with connecting up neighbours.
            
            while (line != null){
                
                Agent agent = parseLine(line);
                if (agent != null) agents.addElement(agent);
                
                line = buff.readLine();
                
                if (line != null) {
                    line.trim();
                    if (line.length() == 0)	line = buff.readLine();
                }
            }
            
            buff.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Now we have all the Agents, we can run through and connect
        // up their neighbours. For the sake of keeping the Agent class
        // clean these are stored in each Agents name field before being processed
        // here. They are stored by parseLine in the form "name|||Agent&Agent&Agent".
        // When there are no neighbours set up, this just stores "name".
        
        // Run though each Agent.
        
        for (int i = 0; i < agents.size(); i++) {
            
            Agent agent = (Agent)agents.elementAt(i);
            
            // Get the name String, and find out where the real name ends.
            
            String name = agent.getName();
            int index = name.indexOf("|||");
            
            // If there are neighbouring Agent names stored, process them.
            // Correct the name variable if necessary.
            
            if (index > -1) {
                
                StringTokenizer st = new StringTokenizer((name.substring(index + 3)),"&");
                agent.setName(name.substring(0,index));
                String neighbourName = null;
                Agent potentialNeighbour = null;
                
                // Go through each neighbour's name and add the neighbour to the Agent's
                // Vector of neighbours.
                
                while (st.hasMoreTokens()) {
                    
                    neighbourName = st.nextToken();
                    
                    // For each name, we need to check all the Agents to see
                    // if the name matches. This could probably be more efficiently done.
                    // Some Agents will still have the ||| formated names.
                    
                    for (int j = 0; j < agents.size(); j++) {
                        
                        potentialNeighbour = (Agent)agents.elementAt(j);
                        
                        String potentialNeighbourName = potentialNeighbour.getName();
                        index = potentialNeighbourName.indexOf("|||");
                        
                        if (index > -1) potentialNeighbourName = potentialNeighbourName.substring(0,index);
                        
                        if ((potentialNeighbourName).equals(neighbourName)) {
                            
                            // We only add neighbours to our agent, and vice versa, if
                            // they aren't already listed as neighbours.
                            
                            if (potentialNeighbour.getNeighbours().contains((Object)agent) == false) {
                                potentialNeighbour.addNeighbour(agent);
                            }
                            if (agent.getNeighbours().contains((Object)potentialNeighbour) == false) {
                                agent.addNeighbour(potentialNeighbour);
                            }
                            break; // Just for Ian!
                        } // End of if name matches.
                    } // End of running through potential neighbours.
                } // End of running through the names of the Agent's neighbours.
            } // End of if the Agent has neighbours.
        } // End of processing for neighbours.
        
        // Pass the Agents to the store in the main program.
        // Note that it is there that missing geography etc. is added.
        
        smallWorld.setAgents(agents);
        
        // Else if saveMenuItem.
        
    } // End of importAgents.

    
    
    
    
    /**
     * Parses XML description from a String and turns it into an Agent.
     * The XML is broadly based on the 
     * <A href="http://www.fipa.org/specs/fipa00001/SC00001L.html#_Toc26668620">FIPA 
     * Abstract Architecture Specification</A> 
     * for Agents, though it currently misses the agent-locator property. 
     * The XML tags are &lt;AGENT-NAME&gt;&lt;/AGENT-NAME&gt; and 
     * &lt;AGENT-ATTRIBUTE&gt;&lt;/AGENT-ATTRIBUTE&gt;. The AGENT-NAME must be present. AGENT-ATTRIBUTE tags can 
     * contain any name-value pair, for example &lt;AGENT-ATTRIBUTE>x=200&lt;/AGENT-ATTRIBUTE&gt;.
     * x,y,z,graphX,graphY,graphZ,neighbours and value are recognised and parsed into the 
     * relevant Agent variables, all others are stored as KeyObject-Object pairs in 
     * the Agent's attributes Hashtable. Neighbours are given as 
     * &lt;AGENT-ATTRIBUTE&gt;neighbours=one&two&three&lt;/AGENT-ATTRIBUTE&gt; and 
     * transfered between this and any calling method in the Agents name variable in 
     * the format "name|||one&two&three"
     * We do the cleaning up of missing coordinates in SmallWorld 
     * rather than here, because we know what is 
     * required to display there - we don't make those assumptions in AgentIO.
    **/
    private Agent parseLine(String line) {
        
        Agent agent = new Agent();
        
        // Parse Agent's name variable. This is the 
        // only thing which must be present.
        
        int start = line.indexOf("<AGENT-NAME>");
        int end = line.indexOf("</AGENT-NAME>", start);
        
        if ((start > -1) && (end > -1)) {
            try {
                agent.setName(line.substring(start + 12,end));
            } catch (Exception e) {return null;}
            line = line.substring(0,start) + line.substring(end + 13);
        } else {
            return null;
        }
        
        // Parse Agent's x variable.
        
        start = line.indexOf("<AGENT-ATTRIBUTE>x=");
        end = line.indexOf("</AGENT-ATTRIBUTE>", start);
        
        if ((start > -1) && (end > -1)) {
            try {
                agent.setX(Integer.parseInt(line.substring(start + 19,end)));
            } catch (Exception e) {}
            line = line.substring(0,start) + line.substring(end + 18);
        }      
        
        // Parse Agent's y variable.
        
        start = line.indexOf("<AGENT-ATTRIBUTE>y=");
        end = line.indexOf("</AGENT-ATTRIBUTE>", start);
        
        if ((start > -1) && (end > -1)) {
            try {
                agent.setY(Integer.parseInt(line.substring(start + 19,end)));
            } catch (Exception e) {}
            line = line.substring(0,start) + line.substring(end + 18);
        }
        
        // Parse Agent's z variable.
        
        start = line.indexOf("<AGENT-ATTRIBUTE>z=");
        end = line.indexOf("</AGENT-ATTRIBUTE>", start);
        
        if ((start > -1) && (end > -1)) {
            try {
                agent.setZ(Integer.parseInt(line.substring(start + 19,end)));
            } catch (Exception e) {}
            line = line.substring(0,start) + line.substring(end + 18);
        }
        
        // Parse Agent's graphX variable.
        
        start = line.indexOf("<AGENT-ATTRIBUTE>graphX=");
        end = line.indexOf("</AGENT-ATTRIBUTE>", start);
        
        if ((start > -1) && (end > -1)) {
            try {
                agent.setGraphX(Integer.parseInt(line.substring(start + 24,end)));
            } catch (Exception e) {}
            line = line.substring(0,start) + line.substring(end + 18);
        }
        
        // Parse Agent's graphY variable.
        
        start = line.indexOf("<AGENT-ATTRIBUTE>graphY=");
        end = line.indexOf("</AGENT-ATTRIBUTE>", start);
        
        if ((start > -1) && (end > -1)) {
            try {
                agent.setGraphY(Integer.parseInt(line.substring(start + 24,end)));
            } catch (Exception e) {}
            line = line.substring(0,start) + line.substring(end + 18);
        }
        
        // Parse Agent's graphZ variable.
        
        start = line.indexOf("<AGENT-ATTRIBUTE>graphZ=");
        end = line.indexOf("</AGENT-ATTRIBUTE>", start);
        
        if ((start > -1) && (end > -1)) {
            try {
                agent.setGraphZ(Integer.parseInt(line.substring(start + 24,end)));
            } catch (Exception e) {}
            line = line.substring(0,start) + line.substring(end + 18);
        }
        
        // Parse Agent's value variable.
        
        start = line.indexOf("<AGENT-ATTRIBUTE>value=");
        end = line.indexOf("</AGENT-ATTRIBUTE>", start);
        
        if ((start > -1) && (end > -1)) {
            try {
                agent.setValue(Integer.parseInt(line.substring(start + 23,end)));
            } catch (Exception e) {}
            line = line.substring(0,start) + line.substring(end + 18);
        }
        
        // Parse Agent's neighbours.
        
        start = line.indexOf("<AGENT-ATTRIBUTE>neighbours=");
        end = line.indexOf("</AGENT-ATTRIBUTE>", start);
        
        if ((start > -1) && (end > -1)) {
            try {
                agent.setName(agent.getName() + "|||" + line.substring(start + 28,end));
            } catch (Exception e) {}
            line = line.substring(0,start) + line.substring(end + 18);
        }

        // Parse Agent's attributes variable which contains all remaining attributes.
        // This is slightly different from the above as we just keep reading and 
        // parsing the attributes as entries into a HashTable until they're all done.
        
        start = 0;
        end = 0;
        
        Hashtable attributes = new Hashtable();

        while ((start > -1) && (end > -1)) {

            start = line.indexOf("<AGENT-ATTRIBUTE>");
            end = line.indexOf("</AGENT-ATTRIBUTE>", start);
            int equals = line.indexOf("=",start);

            if (((start > -1) && (end > -1)) && (equals > -1)) {

                try {
                    attributes.put(
                        (Object)line.substring(start + 17, equals),
                        (Object)line.substring(equals + 1, end));

                } catch (Exception e) {}
                line = line.substring(0,start) + line.substring(end + 18);

            }
        }
        
        if (attributes.size() > 0) agent.setAttributes(attributes);

        return agent;
        
    } // End parseLine.

    
    

    
    /**
     * Writes agents out as XML files.
     * The files have the following fields: 
     * <UL>
     * <LI>lt;AGENT-NAME&gt;&lt;/AGENT-NAME&gt;<BR>
     * A text String representing the Agent. If this has not 
     * been assigned, it's the number of the Agent in the order 
     * they were created.</LI>
     * <LI>lt;AGENT-ATTRIBUTE&gt;&lt;/AGENT-ATTRIBUTE&gt;<BR>
     * Any attribute name-value pair. e.g.<BR>
     * lt;AGENT-ATTRIBUTE&gt;x=250&lt;/AGENT-ATTRIBUTE&gt;<BR>
     * The following case sensitive names are used to fill variables:
     * <UL>
     * <LI>x,y,z : geographical coordinates.</LI>
     * <LI>graphX, graphY, graphZ : graph space coordinates.</LI>
     * <LI>value : a double number</LI>
     * <LI>neighbours : other AGENT-NAMEs that we want linked to this Agent, in 
     * the form neighbours=NAME1&NAME2&NAME3 etc.</LI>
     * </UL>
     * </LI>
     * </UL>
     **/
    private void exportAgents() {
    
        // Get a filename from the user.
        
        FileDialog saveDialog = new FileDialog(new Frame(), "Save file", FileDialog.SAVE);
        saveDialog.show();
        File file = new File(saveDialog.getDirectory() + saveDialog.getFile());
        
        if ((saveDialog.getDirectory() == null) || (saveDialog.getFile() == null)) return;
        
        // Start writing process.
        
        try {
            
            BufferedWriter fw = new BufferedWriter(new FileWriter(file));
            Vector agents = smallWorld.getAgents();
            
            for (int i = 0; i < agents.size(); i++) {
            
                Agent agent = (Agent)agents.elementAt(i);
                
                // Write the default variables.
                
                String tempString = agent.getName();
                if (tempString != null) fw.write("<AGENT-NAME>" + tempString + "</AGENT-NAME>");
                double tempDouble = agent.getValue();
                fw.write("<AGENT-ATTRIBUTE>value=" + String.valueOf(tempDouble) + "</AGENT-ATTRIBUTE>");
                int tempInt = agent.getX();
                fw.write("<AGENT-ATTRIBUTE>x=" + String.valueOf(tempInt) + "</AGENT-ATTRIBUTE>");
                tempInt = agent.getY();
                fw.write("<AGENT-ATTRIBUTE>y=" + String.valueOf(tempInt) + "</AGENT-ATTRIBUTE>");
                tempInt = agent.getZ();
                fw.write("<AGENT-ATTRIBUTE>z=" + String.valueOf(tempInt) + "</AGENT-ATTRIBUTE>");
                tempInt = agent.getGraphX();
                fw.write("<AGENT-ATTRIBUTE>graphX=" + String.valueOf(tempInt) + "</AGENT-ATTRIBUTE>");
                tempInt = agent.getGraphY();
                fw.write("<AGENT-ATTRIBUTE>graphY=" + String.valueOf(tempInt) + "</AGENT-ATTRIBUTE>");
                tempInt = agent.getGraphZ();
                fw.write("<AGENT-ATTRIBUTE>graphZ=" + String.valueOf(tempInt) + "</AGENT-ATTRIBUTE>");
                
                // Write the neighbours, if they exist.
                
                Vector neighbours = agent.getNeighbours();
                
                for (int j = 0; j < neighbours.size(); j++) {
                    tempString = ((Agent)neighbours.elementAt(j)).getName();
                    if (j == 0) {
                        fw.write("<AGENT-ATTRIBUTE>neighbours=" + tempString);
                    } else {
                        fw.write("&" + tempString);
                    }
                }
                if (neighbours.size() > 0 ) fw.write("</AGENT-ATTRIBUTE>");
                
                // Write any other attributes.
                
                Hashtable attributes = agent.getAttributes();
                if (attributes != null) {
                    String attributeString = (attributes.toString()).trim();
                    System.out.println(attributeString);
                    attributeString = attributeString.substring(1,attributeString.length()-1);
                    StringTokenizer st = new StringTokenizer(attributeString,",");
                    while (st.hasMoreTokens()) {
                        fw.write("<AGENT-ATTRIBUTE>" + st.nextToken() + "<AGENT-ATTRIBUTE>");
                    }
                }
                fw.newLine();
            
            } // End looping through each Agent.
            
            fw.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    } // End of exportAgents.

// End of class.
}
