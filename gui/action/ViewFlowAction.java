/*
 *  JFLAP - Formal Languages and Automata Package
 * 
 * 
 *  Susan H. Rodger
 *  Computer Science Department
 *  Duke University
 *  August 27, 2009

 *  Copyright (c) 2002-2009
 *  All rights reserved.

 *  JFLAP is open source software. Please see the LICENSE for terms.
 *
 */





package gui.action;

import gui.CustomUtilities;
import gui.environment.AutomatonEnvironment;
import gui.environment.Environment;
import gui.environment.Universe;
import gui.environment.tag.CriticalTag;
import gui.regular.ConvertPane;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.print.attribute.standard.Finishings;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


import automata.Automaton;
import automata.AutomatonSimulator;
import automata.Configuration;
import automata.SimulatorFactory;
import automata.State;
import automata.fsa.FSATransition;
import automata.fsa.FiniteStateAutomaton;

/**
 * The <CODE>SaveAction</CODE> is an action to save a serializable object
 * contained in an environment to a file.
 * 
 * @author Thomas Finley
 */

public class ViewFlowAction {
	 JButton next ;
	 JButton start;
	 JButton back;
	 boolean fromBack;
	public static String easyPath = "DFA_LIBRARY" + System.getProperty("file.separator") + "Easy";

	public static String hardPath = "DFA_LIBRARY" + System.getProperty("file.separator") + "Hard";
	public static String filePath = "DFA_LIBRARY" ;
	private int count;
	private boolean inTestAnswerElement;
	private int dfaElementCount;
	private JFrame parentDFAFrame;
	private JFrame showMeAnswerFrame;
	private Document doc;
	private JFrame viewPossibleStringsFrame;
	FiniteStateAutomaton fsa = null;
	String acceptStrings;
	String rejectStrings;
	JFrame tableFrame;
	private static List<String> elements = Arrays.asList("ViewStrings","testInput","showAnswer","drawDfa","testAnswer","showAnswer",
			"exit","acceptClicked","rejectClicked");
	
	/**
	 * Instantiates a new <CODE>SaveAction</CODE>. 
	 * 
	 * @param environment
	 *            the environment that holds the serializable
	 */
	

	/**
	 * If a save was attempted, call the methods that handle the saving of the
	 * serializable object to a file.
	 * 
	 * @param event
	 *            the action event
	 */
	public void actionPerformed(ActionEvent event) {
		try{
			refreshConfig();
		JFrame jf = new JFrame();
		JPanel jp = new JPanel();
		count = 0;
		 JFileChooser chooser = new JFileChooser();
         FileNameExtensionFilter xmlfilter = new FileNameExtensionFilter(
        		 
             "xml files (*.xml)", "xml");
         chooser.setFileFilter(xmlfilter);
         chooser.setDialogTitle("Open schedule file");
int val = chooser.showOpenDialog(null);
if(val == JFileChooser.APPROVE_OPTION){
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		doc = docBuilder.parse (new File(chooser.getSelectedFile().getPath()));
		final List<Node> elementList = new ArrayList<Node>();
		final Node details = doc.getElementsByTagName("details").item(0);
		// normalize text representation
		doc.getDocumentElement ().normalize ();
		NodeList children = details.getChildNodes();
		int counter = 1;
		Object data[] = new Object[2];
		List<Object[]> dataList  = new ArrayList<Object[]>();
		int time = 0;
		for(int i = 0;i <children.getLength() ;i++){
			Node n = children.item(i);
			if(n.getNodeType() == Node.ELEMENT_NODE){
				if(n.getNodeName().equals("drawDfa")){
					data = new Object[2];
					data[0] = "Draw DFA Window";
					data[1] =((Element)n).getElementsByTagName("time").item(0).getTextContent();
					time+=Integer.parseInt((String)data[1]);
					dataList.add(data);
					elementList.add(n);
					NodeList DFAchildren = n.getChildNodes();
					for(int j = 0;j <DFAchildren.getLength() ;j++){
						Node dfaChild = DFAchildren.item(j);
						if(dfaChild.getNodeType() == Node.ELEMENT_NODE &&
								elements.contains(dfaChild.getNodeName())){
								if(dfaChild.getNodeName().equals("testAnswer")){
									elementList.add(dfaChild);
									data = new Object[2];
									data[0] = "DFA was Tested";
									data[1] = "";
									dataList.add(data);

									NodeList testAnswerchildren = dfaChild.getChildNodes();
									for(int k = 0; k<testAnswerchildren.getLength() ;k++){
										Node testAnswerChild = testAnswerchildren.item(k);
										if(testAnswerChild.getNodeType() == Node.ELEMENT_NODE &&
												elements.contains(testAnswerChild.getNodeName())){
											data = new Object[2];
											if(testAnswerChild.getNodeName().equals("acceptClicked"))
												data[0] = "Strings to be accepted ";
											if(testAnswerChild.getNodeName().equals("rejectClicked"))
												data[0] = "Strings to be rejected ";
											data[1] = "";
											dataList.add(data);

											elementList.add(testAnswerChild);
										}
									}
								}
								else{
									elementList.add(dfaChild);
									data = new Object[2];
									data[0] = dfaChild.getNodeName();
									data[1] = "";
									dataList.add(data);

								}
						}
					}
				}
				else if(elements.contains(n.getNodeName())){
					data = new Object[2];
						dataList.add(data);
						
					if(n.getNodeName().equals("ViewStrings")){
						data[0] = "View Possible Strings";
						data[1] = "";
						if(!n.getParentNode().getNodeName().equals("drawDfa")){
							data[1] =((Element)n).getElementsByTagName("time").item(0).getTextContent();
							time+=Integer.parseInt((String)data[1]);
						}
						
					}
					if(n.getNodeName().equals("testInput")){
						data[0] = "Test Input String";
						data[1] = "";
						if(!n.getParentNode().getNodeName().equals("drawDfa")){
							data[1] =((Element)n).getElementsByTagName("time").item(0).getTextContent();
							time+=Integer.parseInt((String)data[1]);
						}

					}
					if(n.getNodeName().equals("showAnswer")){
						data[0] = "Show Me The Answer";
						data[1] = "";
						if(!n.getParentNode().getNodeName().equals("drawDfa")){
								data[1] =((Element)n).getElementsByTagName("time").item(0).getTextContent();
								time+=Integer.parseInt((String)data[1]);
						}
					}
					if(n.getNodeName().equals("exit")){
						String scenario = n.getTextContent();
						data[0] = "Exit : " + scenario;
						data[1] = "";
					}
						elementList.add(n);
				}
			}

		}
		Object rowData[][] = new Object[dataList.size()][];
		int rowcount=0;
		for(Object[] listData : dataList){
			rowData[rowcount++] = listData;
		}
		String question=null;
		Node qType = details.getChildNodes().item(0);
		if(qType.getNodeName().equals("qType"))
			question = qType.getTextContent();
		if(question.contains("RegExp"))
			question = "Regular Expression : " + question.replaceAll("RegExp", "").trim();
		else{
			question = question.replaceAll("E","").trim();
			int number = (Integer.parseInt(question));
			try{
				File f = new File("DFA_LIBRARY" + System.getProperty("file.separator") + number + ".txt");
				StringBuilder stringBuilder = new StringBuilder();
				FileReader reader = new FileReader(f);
				BufferedReader br = new BufferedReader(reader);
				for(String line = br.readLine(); line != null; line = br.readLine()) {
				    stringBuilder.append(line + "\n");
				}
			br.close();
			 question = stringBuilder.toString();
			}
			catch(Exception e){
			}
	

			
		}
		int totalTime = Integer.parseInt(((Element)details).getElementsByTagName("totalTime").item(0).getTextContent());
		int qReadTime = Integer.parseInt(((Element)details).getElementsByTagName("qReadTime").item(0).getTextContent());
		createTable(rowData,question,time,totalTime,qReadTime);

		System.out.println ("Root element of the doc is " + doc.getDocumentElement().getNodeName());
		 
		start.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				start.setEnabled(false);
				if(elementList.size() > 1)
				next.setEnabled(true);
				useElement(elementList,count);
			
		 // set selected filter
		        
	}
		});
		next.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stubd
				//closeOtherFrames();
			/*	if(inDFAElement && dfaElementCount > 0)
					displayDFAElements(count);*/
				back.setEnabled(true);
				fromBack = false;
				useElement(elementList,count);
				
				
		 // set selected filter
		        
			}
		});
	
		back.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stubd
				//closeOtherFrames();
			/*	if(inDFAElement && dfaElementCount > 0)
					displayDFAElements(count);*/
				if(count-2 < 0){
					back.setEnabled(false);
					return;
				}
				count-=2;
				fromBack = true;
				if(!next.isEnabled())
					next.setEnabled(true);
				useElement(elementList,count);
				
				
		 // set selected filter
		        
			}
		});

		/*jf.add(jp);
		jp.add(start);
		jf.setSize(200,200);
		jp.add(next);
		jf.setVisible(true);*/
}
		}
		catch(Exception e){
			System.out.println(e);
		}
	}
	
/*	public void saveAsProblem(String type, String problemDescription){
		Universe.frameForEnvironment(environment).saveAsProblem(type,problemDescription,false);
	}*/

	/** The environment this action will handle saving for. */
	private Environment environment;
	
	public void useElement(List<Node> details, int localCount){
		closeFrames();
		//Node children = details.getChildNodes().item(localCount);
		Node children = details.get(localCount);
		details.get(localCount);
		boolean found = false;
		if(children.getNodeType() == Node.ELEMENT_NODE){
			Element child = (Element)children;
			if(child.getNodeName().equals("ViewStrings")){
				JOptionPane.showMessageDialog(null, "View Possible Strings window was viewed");
				if(!child.getParentNode().getNodeName().equals("drawDfa") &&
						parentDFAFrame != null){
					parentDFAFrame.dispose();
				}
				found = true;
				
				viewPossibleStringsFrame.setSize(400, 300);
	    		viewPossibleStringsFrame.setTitle("Some Strings Belonging to the Automaton");
	    		viewPossibleStringsFrame.setLocationRelativeTo(null);
	    		String viewStrings = child.getTextContent().toString().replaceAll(",","\n");
	    		JTextArea ta1 = new JTextArea(20, 60);  
	    		ta1.setText(viewStrings);
	    		ta1.setEditable(false);  
	    		ta1.select(0,0);
	    		JScrollPane js = new JScrollPane(ta1);
	    		viewPossibleStringsFrame.add(js);
	    		viewPossibleStringsFrame.setVisible(true);
			}
			else if(child.getNodeName().equals("testInput")){
				JOptionPane.showMessageDialog(null, "Input String was tested");
				if(!child.getParentNode().getNodeName().equals("drawDfa") &&
						parentDFAFrame != null){
					parentDFAFrame.dispose();
				}
				found = true;
				String originalStr = child.getElementsByTagName("string").item(0).getTextContent().toString();
				String str = "";
				if(originalStr != "" && originalStr != null){
					Element automatonElement = (Element)((Element)doc.getElementsByTagName("details").item(0)).getElementsByTagName("automaton").item(0);
					String states = ((Element)automatonElement).getElementsByTagName("States").item(0).getTextContent().toString();
					String trans = ((Element)automatonElement).getElementsByTagName("Transitions").item(0).getTextContent().toString();
					FiniteStateAutomaton fsa = prepareFSA(states,trans);
					AutomatonEnvironment env= new AutomatonEnvironment(fsa);
					if(originalStr.contains("Rejected"))
						str = originalStr.replaceAll("Rejected", "").trim() + " was Rejected. Try Another String";
					else
						str = originalStr.replaceAll("Accepted", "").trim() + " was Accepted . Try Another String";
							
					String input = JOptionPane.showInputDialog(null,str,"Test Input String",JOptionPane.NO_OPTION);
	            	Configuration[] configs = null;
	            	AutomatonSimulator as = SimulatorFactory
							.getSimulator(fsa);
	            	configs = as
							.getInitialConfigurations(input);
	            	List associated = new ArrayList();
	            	try{
	            		MultipleSimulateAction ms = new MultipleSimulateAction(fsa,env);
	            	int result = ms.handleInput(fsa,  as,
							configs, input, associated);
	            	if(result == 0){
	            		JOptionPane.showMessageDialog(null, "Your Input will be accepted by the automaton");
	            	}
	            	else{
	            		JOptionPane.showMessageDialog(null, "Your Input will be Rejected by the automaton");
	            	}
	           
	            	}
	            	catch(Exception ex){
	            		System.out.println(ex.getStackTrace());
	            	}
	
	        	}
			}
			else if(child.getNodeName().equals("drawDfa")){
				found = true;
				//inDFAElement = true;
				if(!child.getParentNode().getNodeName().equals("drawDfa") &&
						parentDFAFrame!=null){
					parentDFAFrame.dispose();
				}
				FiniteStateAutomaton fsa = null;
				NodeList testAnswerList = child.getElementsByTagName("testAnswer");
				if(testAnswerList.getLength() > 0){
					/*useElement(details, ++count);
					return;*/
					JOptionPane.showMessageDialog(null, "DFA Window was Opened and an Automaton was drawn");

					Node firstTestAnswer = testAnswerList.item(0); 
					String states = ((Element)firstTestAnswer).getElementsByTagName("States").item(0).getTextContent().toString();
					String trans = ((Element)firstTestAnswer).getElementsByTagName("Transitions").item(0).getTextContent().toString();
					 fsa = prepareFSA(states,trans);
					parentDFAFrame =  NewAction.createCustomWindow(fsa);
				
			}
			
			
				
			else{
				if(((Element)child).getElementsByTagName("States").getLength() == 0){
					JOptionPane.showMessageDialog(null, "DFA window was opened but DFA was not drawn");
					fsa = new FiniteStateAutomaton();
			}
					else{
				String states = ((Element)child).getElementsByTagName("States").item(0).getTextContent();
				Node transitions = ((Element)child).getElementsByTagName("Transitions").item(0);
				String trans = null;
						if(transitions != null)
							trans= transitions.getTextContent().toString();
				 fsa = prepareFSA(states,trans);
					}
				 parentDFAFrame = NewAction.createCustomWindow(fsa);
			}

		 }
			else if(child.getNodeName().equals("testAnswer")){
				
				found = true;
				
				
				/*if(!inTestAnswerElement){
					if(fromBack){
						String result = ((Element)child).getElementsByTagName("result").item(0).getTextContent().toString();
					
					JOptionPane.showMessageDialog(null, "DFA drawn was tested and the answer was " + result);

					}
					else
						JOptionPane.showMessageDialog(null, "DFA was drawn");

					if(parentDFAFrame != null)
						parentDFAFrame.dispose();
				inTestAnswerElement = true;
				//inDFAElement = true;
					dfaElementCount = testAnswerList.getLength();
					Node testAnswer = child.getElementsByTagName("testAnswer").item(0);
					String states = ((Element)child).getElementsByTagName("States").item(0).getTextContent().toString();
					String trans = ((Element)child).getElementsByTagName("Transitions").item(0).getTextContent().toString();
					 fsa = prepareFSA(states,trans);
					parentDFAFrame =  NewAction.createCustomWindow(fsa);
					if(fromBack){
						 prepareStringsAndDisplay(fsa);
					}else					
					return;
				}
				else{
					String result = ((Element)child).getElementsByTagName("result").item(0).getTextContent().toString();

					JOptionPane.showMessageDialog(null, "DFA drawn was tested and the answer was "+ result);
					 NodeList acceptClickedList = child.getElementsByTagName("acceptClicked");

					 NodeList rejectClickedList = child.getElementsByTagName("rejectClicked");
					 prepareStringsAndDisplay(fsa);
											 inTestAnswerElement = false;
				}*/
					 
					// JOptionPane.showMessageDialog(null,result + " Answer was drawn");
				
				
				String result = ((Element)child).getElementsByTagName("result").item(0).getTextContent().toString();

				JOptionPane.showMessageDialog(null,"DFA was tested : " + result);
				if(parentDFAFrame != null)
					parentDFAFrame.dispose();
			inTestAnswerElement = true;
			//inDFAElement = true;
				String states = ((Element)child).getElementsByTagName("States").item(0).getTextContent().toString();
				String trans = ((Element)child).getElementsByTagName("Transitions").item(0).getTextContent().toString();
				 fsa = prepareFSA(states,trans);
				parentDFAFrame =  NewAction.createCustomWindow(fsa);
				if(result.contains("Incorrect"))
				 prepareStringsAndDisplay(fsa);
				 
			}
			else if(child.getNodeName().equals("acceptClicked")){
				String states = ((Element)child.getParentNode()).getElementsByTagName("States").item(0).getTextContent().toString();
				String trans = ((Element)child.getParentNode()).getElementsByTagName("Transitions").item(0).getTextContent().toString();
				 fsa = prepareFSA(states,trans);
				acceptStrings =  prepareStrings(fsa).get(0);
				

				JOptionPane.showMessageDialog(null, "List of Strings to be accepted was viewed");
				found= true;
				JTextArea jt = new JTextArea(acceptStrings);
				jt.setSize(20, 60);
				JOptionPane.showMessageDialog(parentDFAFrame,new JScrollPane(jt),"Strings to be Accepted",JOptionPane.NO_OPTION);
					
					 
					// JOptionPane.showMessageDialog(null,result + " Answer was drawn");
					 
			}
			else if(child.getNodeName().equals("rejectClicked")){
				JOptionPane.showMessageDialog(null, "List of Strings to be Rejected was viewed");
				String states = ((Element)child.getParentNode()).getElementsByTagName("States").item(0).getTextContent().toString();
				String trans = ((Element)child.getParentNode()).getElementsByTagName("Transitions").item(0).getTextContent().toString();
				 fsa = prepareFSA(states,trans);
				 rejectStrings =  prepareStrings(fsa).get(1);
				found= true;
				JTextArea jt = new JTextArea(rejectStrings);
				jt.setSize(20, 60);
				JOptionPane.showMessageDialog(parentDFAFrame,new JScrollPane(jt),"Strings to be Rejected",JOptionPane.NO_OPTION);
				inTestAnswerElement = false;
				
			}
			else if(child.getNodeName().equals("showAnswer")){
				JOptionPane.showMessageDialog(null, "Show me the answer was viewed");

				found= true;
				Element automatonElement = (Element)((Element)doc.getElementsByTagName("details").item(0)).getElementsByTagName("automaton").item(0);
				String states = ((Element)automatonElement).getElementsByTagName("States").item(0).getTextContent().toString();
				String trans = ((Element)automatonElement).getElementsByTagName("Transitions").item(0).getTextContent().toString();
				FiniteStateAutomaton openedAutomaton = prepareFSA(states,trans);
				if(!child.getParentNode().getNodeName().equals("drawDfa") &&
						parentDFAFrame != null){
					parentDFAFrame.dispose();
				}
				showMeAnswerFrame =  NewAction.createCustomWindow((FiniteStateAutomaton)openedAutomaton);
				
			}
			
			else if(child.getNodeName().equals("exit")){

				String value = child.getTextContent();
				if(showMeAnswerFrame != null)
				showMeAnswerFrame.dispose();
				if(parentDFAFrame != null)
					parentDFAFrame.dispose();
				
				JOptionPane.showMessageDialog(null, "The test Flow ends with Exit Scenario : " + value);
			}
			
			
		count++;
	//	if(count >= details.getChildNodes().getLength()){
		if(count >= details.size()){
			/*if(tableFrame != null)
				tableFrame.dispose();*/
			next.setEnabled(false);
			
		} else if(!found)
			useElement(details, count);
		
	}
	}
	
	public FiniteStateAutomaton prepareFSA(String states, String trans){
		String[] stdet = states.split(",");
		FiniteStateAutomaton fsa = new FiniteStateAutomaton();
		int count = 0;
		State[] automatonStates = new State[stdet.length];
		for(String stateDet : stdet){
		String[] state = stateDet.split(" ");				
		automatonStates[count++] = fsa.createState(new Point(Integer.parseInt(state[1]),Integer.parseInt(state[2])));
			if(state.length == 5){
				fsa.setInitialState(automatonStates[count-1]);
				fsa.addFinalState(automatonStates[count-1]);
			}
			else if(state.length == 4){
				if(state[3].equals("Initial")){
					fsa.setInitialState(automatonStates[count-1]);
				}
				else
					fsa.addFinalState(automatonStates[count-1]);
			}
		}
		if(trans != null){
		String[] transDet = trans.split(",");
		for(String t : transDet){
			if(!(t.trim().equals(""))){
			String[] transition = t.split(" ");
			FSATransition ft = new FSATransition(automatonStates[Integer.parseInt(transition[0])], automatonStates[Integer.parseInt(transition[2])], transition[1]);
			fsa.addTransition(ft);
			}
		}
		}
		
		return fsa;
	}

	/*private void displayDFAElements(Node details, int localCount){
		Element dfaElement = (Element)details.getChildNodes().item(count);
		Element childElement = (Element)dfaElement.getChildNodes().item(--dfaElementCount);
		
		
		
		
		
	}*/
	
	private void prepareStringsAndDisplay(FiniteStateAutomaton aut){
		
		List<String> list  = prepareStrings(aut);
		JPanel jp = new JPanel();
		JButton jb1 = new JButton("Accept These Strings");
		JButton jb2 = new JButton("Reject These Strings");
		jp.setLayout(new GridBagLayout());
		String acceptTheseStrings = list.get(0);
		String rejectTheseStrings = list.get(1);
		if(acceptTheseStrings == "\u00F8" || acceptTheseStrings == "" || acceptTheseStrings.length() == 0){
			jb1.setEnabled(false);
		}
		
		
		if(rejectTheseStrings == "\u00F8" || rejectTheseStrings == "" || rejectTheseStrings.length() == 0){
			jb2.setEnabled(false);
		}
		
	
		
		GridBagConstraints gc = new GridBagConstraints();
		/*gc.gridx=0;
		gc.gridy=0;
       	gc.insets = new Insets(0,15,0,0); 
       	*/
       	JPanel main = new JPanel();
       	main.setLayout(new GridLayout(2,1));
       	main.add(new JLabel("This is not the Right Answer.Please Try Again!"));
		//jp.add(new JLabel("This is not the Right Answer.Please Try Again!"),gc);
		gc = new GridBagConstraints();
		gc.gridx=0;
		gc.gridy=0;
       	gc.insets = new Insets(5,0,0,0); 

		jp.add(jb1,gc);
		gc = new GridBagConstraints();
		gc.gridx=1;
		gc.gridy=0;

       	gc.insets = new Insets(5,5,0,0); 
		jp.add(jb2,gc);
		 acceptStrings = acceptTheseStrings;
		 rejectStrings = rejectTheseStrings;
		jb1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
			
				JTextArea jt = new JTextArea(acceptStrings);
				jt.setSize(20, 60);
				JOptionPane.showMessageDialog(null,new JScrollPane(jt));
				
			}
		});
		
		jb2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
							
			
				JTextArea jt = new JTextArea(rejectStrings);
				jt.setSize(20, 60);
				JOptionPane.showMessageDialog(null,new JScrollPane(jt));
				
			}
		});
		main.add(jp);
		JOptionPane.showMessageDialog(
				parentDFAFrame,main,"ERROR",JOptionPane.ERROR_MESSAGE);
		
      }
	
	private String prepareRE(Automaton automaton) {
		/*
		 * try{ ConvertFSAToREActionNew.computedRE = ""; AutomatonEnvironment at
		 * = new AutomatonEnvironment(automaton);
		 * ConvertFSAToREActionNew.secondAction = true; ConvertPane pane = new
		 * ConvertPane(at); ConvertFSAToREActionNew.secondAction = false; }
		 */
		String computedRE = "";
		ConvertFSAToREActionNew.computedRE = "";
		try {
			new ConvertFSAToREActionNew(new AutomatonEnvironment(automaton))
					.checkAndApplyLamdaTransition(automaton);
			computedRE = "";
			ConvertFSAToREActionNew.secondAction = true;
			ConvertPane pane = new ConvertPane(new AutomatonEnvironment(
					automaton));
			ConvertFSAToREActionNew.secondAction = false;
			/*environment.add(pane, "Convert FA to RE", new CriticalTag() {
			});
			environment.remove(pane);
*/
		} catch (Exception e) {
			ConvertFSAToREActionNew.secondAction = false;
			if (ConvertFSAToREActionNew.computedRE == "") {
			return null;
			}
			computedRE = ConvertFSAToREActionNew.computedRE;
		}
		computedRE = ConvertFSAToREActionNew.computedRE;
		return computedRE;
	}
	
	private void closeFrames(){
		if(showMeAnswerFrame != null)
			showMeAnswerFrame.dispose();
		if(viewPossibleStringsFrame !=null)
			viewPossibleStringsFrame.dispose();
	}
	
	private void refreshConfig(){
		fsa = null;
		parentDFAFrame=showMeAnswerFrame=null;
		inTestAnswerElement = false;
		viewPossibleStringsFrame = new JFrame();
		tableFrame = null;
		next = new JButton("Next");
		start = new JButton("Start");
		back = new JButton("Back");
				
	}
	private void createTable(Object[][] rowData, String question, int time, int totalTime , int qReadTime){
		Object columnNames[] = { "Event", "Time Taken(Seconds)"};
		JTable table = new JTable(rowData,columnNames);
		JScrollPane scrollPane = new JScrollPane(table);
		JLabel ques = new JLabel("Question : " + question);
		JLabel total = new JLabel("Total Time : " + totalTime +" Seconds");
		JLabel qReadtime = new JLabel("Question Read Time : " + qReadTime +" Seconds");
		//JLabel nTime = new JLabel("Navigation Time : " + (totalTime - time)+" Seconds");
		
	//	table.setSize(200, 200);
	JPanel pan=new JPanel(new BorderLayout());
	JPanel timePanel = new JPanel(new BorderLayout());
    Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
	timePanel.setBorder(BorderFactory.createTitledBorder(lowerEtched,"Details"));
	//timePanel.add(ques);
	timePanel.add(total,BorderLayout.NORTH);
	timePanel.add(qReadtime,BorderLayout.CENTER);
	//timePanel.add(nTime,BorderLayout.SOUTH);
	pan.add(new JLabel("\n\n"));
	pan.add(new JLabel("\n\n"));
	pan.add(new JLabel("\n\n"));

	pan.add(timePanel,BorderLayout.NORTH);
	JLabel spacer;
	pan.add(new JLabel("\n\n\n\n"));
		pan.add(scrollPane,BorderLayout.SOUTH);
		JPanel pan1 = new JPanel();
		pan1.add(start);
		pan1.add(next);
		pan1.add(back);
		next.setEnabled(false);
		back.setEnabled(false);
		start.setEnabled(true);
		//table.setPreferredScrollableViewportSize(new Dimension(30*rowData.length,(35*rowData.length)/2));
		tableFrame=new JFrame();
		tableFrame.setVisible(true);
		tableFrame.add(ques, BorderLayout.NORTH); 
		tableFrame.add(pan,BorderLayout.CENTER); 
		tableFrame.add(pan1,BorderLayout.SOUTH); 

		tableFrame.pack();
		
		
       
	}
	
	private List<String> prepareStrings(FiniteStateAutomaton aut){
		List<String> list = new ArrayList<String>();
		Element automatonElement = (Element)((Element)doc.getElementsByTagName("details").item(0)).getElementsByTagName("automaton").item(0);
		String states = ((Element)automatonElement).getElementsByTagName("States").item(0).getTextContent().toString();
		String trans = ((Element)automatonElement).getElementsByTagName("Transitions").item(0).getTextContent().toString();
		FiniteStateAutomaton openedAutomaton = prepareFSA(states,trans);
		FiniteStateAutomaton userAutomaton = aut;
		final Map<String,String> testAnswerFlow = new HashMap<String,String>(); 
		testAnswerFlow.put("Result", "Incorrect");

		 String acceptTheseStrings = "";
		 String rejectTheseStrings = "";
		String originalRE = prepareRE((FiniteStateAutomaton)openedAutomaton.clone());
		String userRE = prepareRE((Automaton)userAutomaton.clone());

		
		List<String> originalPossibleStrings = CustomUtilities.getXegerList(originalRE);
		List<String> userPossibleStrings = CustomUtilities.getXegerList(userRE);
		
		for(String str : originalPossibleStrings){
			AutomatonEnvironment env= new AutomatonEnvironment(aut);
			if(CustomUtilities.checkInputString(env,str) != 0){
				acceptTheseStrings += (str + "\n");
			}
		}
		
		AutomatonEnvironment answerEnvironment = new AutomatonEnvironment(openedAutomaton);
		for(String str : userPossibleStrings){

			if(CustomUtilities.checkInputString(answerEnvironment, str) != 0){
				if(!str.equals("\u00F8"))
				rejectTheseStrings += (str + "\n");
			}
		}
		list.add(acceptTheseStrings);
		list.add(rejectTheseStrings);
		return list;
	}
}