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

import gui.environment.AutomatonEnvironment;
import gui.environment.EnvironmentFactory;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/*import sun.java2d.pipe.SolidTextRenderer;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;*/

import automata.Automaton;
import automata.State;
import automata.fsa.FSATransition;
import automata.fsa.FiniteStateAutomaton;

/**
 * This action handles the conversion of an FSA to a regular expression.
 * 
 * @author Thomas Finley
 */

public class SearchForProblemsAction {
	/** The automaton environment. */
	public static String computedRE = "";
    static String speaktext;
    public static boolean secondAction = false;
    public static List<String> problemDescriptions;

	/**
	 * Instantiates a new <CODE>ConvertFSAToREAction</CODE>.
	 * 
	 * @param environment
	 *            the environment
	 */
	
	public SearchForProblemsAction(){
		
	}
	/**
	 * This method begins the process of converting an automaton to a regular
	 * expression.
	 * 
	 * @param event
	 *            the action event
	 */

public void searchProblems() {

	choosePredicates();
		
}

private void choosePredicates(){
	final List<String> predicateList = new ArrayList<String>();
	final JFrame options = new JFrame();
	options.setSize(700, 600);
	
	
	final JCheckBoxMenuItem atleast = new JCheckBoxMenuItem("atLeast");
	final JCheckBoxMenuItem reverse = new JCheckBoxMenuItem("reverse");
	final JCheckBoxMenuItem length = new JCheckBoxMenuItem("length");
	final JCheckBoxMenuItem size = new JCheckBoxMenuItem("size");

	final JCheckBoxMenuItem IndexesOf = new JCheckBoxMenuItem("indexesOf");
	final JCheckBoxMenuItem FromEnd = new JCheckBoxMenuItem("fromEnd");
	final JCheckBoxMenuItem contains = new JCheckBoxMenuItem("contains");
	final JCheckBoxMenuItem atPosition =  new JCheckBoxMenuItem("atPos");

	final JCheckBoxMenuItem endsWith = new JCheckBoxMenuItem("endsWith");
	final JCheckBoxMenuItem atMost = new JCheckBoxMenuItem("atMost");
	final JCheckBoxMenuItem substring = new JCheckBoxMenuItem("subString");
	final JCheckBoxMenuItem preceededBy = new JCheckBoxMenuItem("preceededBy");

	final JCheckBoxMenuItem FollowedBy = new JCheckBoxMenuItem("followedBy");
	final JCheckBoxMenuItem match = new JCheckBoxMenuItem("match");
	final JCheckBoxMenuItem exacty = new JCheckBoxMenuItem("exactly");
	final JCheckBoxMenuItem exists = new JCheckBoxMenuItem("exists");

	final JCheckBoxMenuItem BeginsWith = new JCheckBoxMenuItem("beginsWith");
	final JCheckBoxMenuItem isOdd = new JCheckBoxMenuItem("isOdd");
	final JCheckBoxMenuItem isEven = new JCheckBoxMenuItem("isEven");
	final JCheckBoxMenuItem bin = new JCheckBoxMenuItem("bin");
	JPanel predicatePanel = new JPanel();
	predicatePanel.setLayout(new GridLayout(10, 2));
	predicatePanel.add(atleast);
	predicatePanel.add(reverse);
	predicatePanel.add(length);
	predicatePanel.add(size);
	predicatePanel.add(IndexesOf);
	predicatePanel.add(FromEnd);
	predicatePanel.add(contains);
	predicatePanel.add(atPosition);
	predicatePanel.add(endsWith);
	predicatePanel.add(atMost);
	predicatePanel.add(substring);
	predicatePanel.add(preceededBy);
	predicatePanel.add(FollowedBy);
	predicatePanel.add(match);
	predicatePanel.add(exacty);
	predicatePanel.add(exists);
	predicatePanel.add(BeginsWith);
	predicatePanel.add(isOdd);
	predicatePanel.add(isEven);
	predicatePanel.add(bin);
	
	JPanel mainPanel = new JPanel();

	mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

	JPanel otherPatamtersPanel = new JPanel();
	otherPatamtersPanel.setLayout(new GridLayout(3,1));


	JPanel noOfStatesMainPanel = new JPanel();
	noOfStatesMainPanel.setLayout(new GridLayout(2,1));
	JPanel noOfStatesPanel = new JPanel();
	//noOfStatesPanel.setLayout(new GridLayout(2,1));
	final JTextField noOfStatesMaxvalue = new JTextField(15);
	final JTextField noOfStatesMinvalue = new JTextField(15);
	JLabel minimumLabel = new JLabel("Minimum Value : ");
	JLabel maximumLabel = new JLabel("Maximum Value : ");
	noOfStatesPanel.add(minimumLabel);
	noOfStatesPanel.add(noOfStatesMinvalue);
	noOfStatesPanel.add(maximumLabel);
	noOfStatesPanel.add(noOfStatesMaxvalue);
	JLabel noOfStatesLabel = new JLabel("Number Of States");
	noOfStatesMainPanel.add(noOfStatesLabel);
	noOfStatesMainPanel.add(noOfStatesPanel);
	noOfStatesMainPanel.setBackground(Color.white);
	otherPatamtersPanel.add(noOfStatesMainPanel);
	
	JPanel noOfFinalStatesMainPanel = new JPanel();
	noOfFinalStatesMainPanel.setLayout(new GridLayout(2,1));
	JPanel noOfFinalStatesPanel = new JPanel();
	//noOfStatesPanel.setLayout(new GridLayout(2,1));
	final JTextField noOfFinalStatesMaxValue = new JTextField(15);
	final JTextField noOfFinalStatesMinValue = new JTextField(15);
	JLabel FinalminimumLabel = new JLabel("Minimum Value : ");
	JLabel FinalmaximumLabel = new JLabel("Maximum Value : ");
	noOfFinalStatesPanel.add(FinalminimumLabel);
	noOfFinalStatesPanel.add(noOfFinalStatesMinValue);
	noOfFinalStatesPanel.add(FinalmaximumLabel);
	noOfFinalStatesPanel.add(noOfFinalStatesMaxValue);
	JLabel noOfFinalStatesLabel = new JLabel("Number Of Final States");
	noOfFinalStatesMainPanel.add(noOfFinalStatesLabel);
	noOfFinalStatesMainPanel.add(noOfFinalStatesPanel);
	noOfFinalStatesMainPanel.setBackground(Color.white);
	otherPatamtersPanel.add(noOfFinalStatesMainPanel);
	
	JPanel noOfTrapStatesMainPanel = new JPanel();
	noOfTrapStatesMainPanel.setLayout(new GridLayout(2,1));
	JPanel noOfTrapStatesPanel = new JPanel();
	//noOfStatesPanel.setLayout(new GridLayout(2,1));
	final JTextField noOfTrapStatesMaxValue = new JTextField(15);
	final JTextField noOfTrapStatesMinValue = new JTextField(15);
	JLabel TrapminimumLabel = new JLabel("Minimum Value : ");
	JLabel TrapmaximumLabel = new JLabel("Maximum Value : ");
	noOfTrapStatesPanel.add(TrapminimumLabel);
	noOfTrapStatesPanel.add(noOfTrapStatesMinValue);
	noOfTrapStatesPanel.add(TrapmaximumLabel);
	noOfTrapStatesPanel.add(noOfTrapStatesMaxValue);
	JLabel noOfTrapStatesLabel = new JLabel("Number Of Trap States");
	noOfTrapStatesMainPanel.add(noOfTrapStatesLabel);
	noOfTrapStatesMainPanel.add(noOfTrapStatesPanel);
	noOfTrapStatesMainPanel.setBackground(Color.white);
	otherPatamtersPanel.add(noOfTrapStatesMainPanel);
	otherPatamtersPanel.setBorder(BorderFactory.
		      createTitledBorder(null, "Other Parameters", javax.swing.border.
		    	      TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.
		    	      TitledBorder.DEFAULT_POSITION, null, java.awt.Color.blue));
	predicatePanel.setBackground(Color.white);
	
	otherPatamtersPanel.setBackground(Color.white);
	mainPanel.add(new JSeparator(JSeparator.HORIZONTAL));
	mainPanel.add(predicatePanel);
	predicatePanel.setBorder(BorderFactory.createTitledBorder(null, "Choice Of Predicates", javax.swing.border.
	  	      TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.
	  	      TitledBorder.DEFAULT_POSITION, null, java.awt.Color.blue)); 
	//mainPanel.add(new JSeparator(JSeparator.HORIZONTAL));
	mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
	mainPanel.add(otherPatamtersPanel);
	mainPanel.setBackground(Color.WHITE);
	mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

	JButton submit = new JButton("Submit");
	submit.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			options.dispose();
			JTextArea text = new JTextArea(predicateList.toString());
			List<Integer> minValuesList = new ArrayList<Integer>();
			List<Integer> maxValuesList = new ArrayList<Integer>();
			
			int noOfStatesMinVal = 0;
			if(noOfStatesMinvalue.getText().trim().matches("\\d+"))
				noOfStatesMinVal = Integer.parseInt(noOfStatesMinvalue.getText().trim());
			minValuesList.add(noOfStatesMinVal);
			
			int noOfFinalStatesMinVal = 0;
			if(noOfFinalStatesMinValue.getText().trim().matches("\\d+"))
				noOfFinalStatesMinVal = Integer.parseInt(noOfFinalStatesMinValue.getText().trim());
			minValuesList.add(noOfFinalStatesMinVal);
			
			int noOfTrapStatesMinVal = 0;
			if(noOfTrapStatesMinValue.getText().trim().matches("\\d+"))
				noOfTrapStatesMinVal = Integer.parseInt(noOfTrapStatesMinValue.getText().trim());
			minValuesList.add(noOfTrapStatesMinVal);
				
			int noOfStatesMaxVal = 0;
			if(noOfStatesMaxvalue.getText().trim().matches("\\d+"))
				noOfStatesMaxVal = Integer.parseInt(noOfStatesMaxvalue.getText().trim());
			maxValuesList.add(noOfStatesMaxVal);
				
			int noOfFinalStatesMaxVal = 0;
			if(noOfFinalStatesMaxValue.getText().trim().matches("\\d+"))
				noOfFinalStatesMaxVal = Integer.parseInt(noOfFinalStatesMaxValue.getText().trim());
			maxValuesList.add(noOfFinalStatesMaxVal);
				
			int noOfTrapStatesMaxVal = 0;
			if(noOfTrapStatesMaxValue.getText().trim().matches("\\d+"))
				noOfTrapStatesMaxVal = Integer.parseInt(noOfTrapStatesMaxValue.getText().trim());
			maxValuesList.add(noOfTrapStatesMaxVal);
				
			text.setText(text.getText() + "\n" + minValuesList.toString() + "\n" + maxValuesList.toString()) ;
			
			String problems_list = "";
			// TODO 
			// Send the predicateList to the Website and the Website will return the list of problem descriptions
			// which will be sent to displayProblemDescriptions.
			try{
				URL website = new URL("http://dfa-varunshenoy.rhcloud.com/jar/Submit.py");
				//Open a connection to the website
				URLConnection connection = website.openConnection();
				connection.setDoOutput(true);
				OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
				out.write("predicateList=" + predicateList + "&min=" +minValuesList.toString() + "&max=" +maxValuesList.toString());

				out.close();

				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String decodedString;
				
				while ((decodedString = in.readLine()) != null) {
					problems_list = problems_list + decodedString;
					
					
				}
				in.close();
				
			}
			catch(MalformedURLException e)
			{
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/*List<String>*/ problemDescriptions = new ArrayList<String>();
			String[] problems = problems_list.split("#");
			problemDescriptions = Arrays.asList(problems);
 			displayProblemDescriptions(problemDescriptions);
		}
	});
	//mainPanel.add(new JSeparator(JSeparator.HORIZONTAL));
	//mainPanel.add(new JSeparator(JSeparator.HORIZONTAL));
	mainPanel.add(submit);

	
	atleast.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e)
        {
        	if(atleast.isSelected() && !predicateList.contains(atleast.getName())){
        		predicateList.add(atleast.getText());
        	}
        }
    });
	
	reverse.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e)
        {
        	if(reverse.isSelected() && !predicateList.contains(reverse.getName())){
        		predicateList.add(reverse.getText());
        	}
        }
    });
	
	length.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e)
        {
        	if(length.isSelected() && !predicateList.contains(length.getName())){
        		predicateList.add(length.getText());
        	}
        }
    });
	
	
	
	IndexesOf.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e)
        {
        	if(IndexesOf.isSelected() && !predicateList.contains(IndexesOf.getName())){
        		predicateList.add(IndexesOf.getText());
        	}
        }
    });
	
	FromEnd.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e)
        {
        	if(FromEnd.isSelected() && !predicateList.contains(FromEnd.getName())){
        		predicateList.add(FromEnd.getText());
        	}
        }
    });
	
	contains.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e)
        {
        	if(contains.isSelected() && !predicateList.contains(contains.getName())){
        		predicateList.add(contains.getText());
        	}
        }
    });
	
	atPosition.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e)
        {
        	if(atPosition.isSelected() && !predicateList.contains(atPosition.getName())){
        		predicateList.add(atPosition.getText());
        	}
        }
    });
	
	size.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e)
        {
        	if(size.isSelected() && !predicateList.contains(size.getName())){
        		predicateList.add(size.getText());
        	}
        }
    });
	
	endsWith.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e)
        {
        	if(endsWith.isSelected() && !predicateList.contains(endsWith.getName())){
        		predicateList.add(endsWith.getText());
        	}
        }
    });
	
	atMost.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e)
        {
        	if(atMost.isSelected() && !predicateList.contains(atMost.getName())){
        		predicateList.add(atMost.getText());
        	}
        }
    });
	
	substring.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e)
        {
        	if(substring.isSelected() && !predicateList.contains(substring.getName())){
        		predicateList.add(substring.getText());
        	}
        }
    });
	
	preceededBy.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e)
        {
        	if(preceededBy.isSelected() && !predicateList.contains(preceededBy.getName())){
        		predicateList.add(preceededBy.getText());
        	}
        }
    });
	
	FollowedBy.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e)
        {
        	if(FollowedBy.isSelected() && !predicateList.contains(FollowedBy.getName())){
        		predicateList.add(FollowedBy.getText());
        	}
        }
    });
	match.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e)
        {
        	if(match.isSelected() && !predicateList.contains(match.getName())){
        		predicateList.add(match.getText());
        	}
        }
    });
	
	exacty.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e)
        {
        	if(exacty.isSelected() && !predicateList.contains(exacty.getName())){
        		predicateList.add(exacty.getText());
        	}
        }
    });
	
	exists.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e)
        {
        	if(exists.isSelected() && !predicateList.contains(exists.getName())){
        		predicateList.add(exists.getText());
        	}
        }
    });
	
	BeginsWith.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e)
        {
        	if(BeginsWith.isSelected() && !predicateList.contains(BeginsWith.getName())){
        		predicateList.add(BeginsWith.getText());
        	}
        }
    });
	isOdd.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e)
        {
        	if(isOdd.isSelected() && !predicateList.contains(isOdd.getName())){
        		predicateList.add(isOdd.getText());
        	}
        }
    });
	
	isEven.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e)
        {
        	if(isEven.isSelected() && !predicateList.contains(isEven.getName())){
        		predicateList.add(isEven.getText());
        	}
        }
    });
	
	bin.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e)
        {
        	if(bin.isSelected() && !predicateList.contains(bin.getName())){
        		predicateList.add(bin.getText());
        	}
        }
    });
	
	
	options.add(mainPanel);
	options.setTitle("PREDICATES");
	options.setVisible(true);
	
	
}

private void displayProblemDescriptions(List<String> problemDescriptions){

	JFrame jf = new JFrame();
	jf.setSize(400, 400);
	JPanel listPanel = new JPanel();
	listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
	listPanel.setBorder(BorderFactory.createTitledBorder(" select a Problem."));
    for(int i = 1; i < problemDescriptions.size(); i++){
    	   JPanel pane = new JPanel(new BorderLayout());
    	 final JEditorPane ep = new JEditorPane("text",problemDescriptions.get(i));

    	   // ep.setBackground(this.getBackground());
	        ep.setDisabledTextColor(Color.BLACK);
	        ep.setEnabled(false);
	        pane.add(ep, BorderLayout.CENTER);

	        JButton button = new JButton("Select");
	        button.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO 
					// The problem Description string "ep.getText()" will be sent to the website and the corresponding DFA will be returned in an arraylist
					// The arrayList here is used to create the automaton
					
					createDFAFromProblemDescription(ep.getText());
				}
			});
	        pane.add(button, BorderLayout.EAST);
	        pane.setBorder(BorderFactory.createEtchedBorder());
        listPanel.add(pane);
    }
      
    JScrollPane js = new JScrollPane(listPanel);
   
      jf.add(js, BorderLayout.CENTER);
      jf.setVisible(true);	
     
}

private static void createAndDisplayAutomaton(List<String> dfaReceived, String problemDescription){
	
	   List<String> dfa = dfaReceived;
	    
	    FiniteStateAutomaton fsa = new FiniteStateAutomaton();
	    int i;
		String regex = " +";
		String[] transitions;
		String[] states = dfa.get(1).trim().split(regex);
		String[] finalStates = dfa.get(3).trim().split(regex);
		String initial = dfa.get(2).trim();
		
		int x = 0;
		int y = 0;
		State[] Automatonstates = new State[states.length];
		int count = 0;
		for(String s : states){
			Point p = new java.awt.Point(x, y);
			x+=50;
			y+=50;
			Automatonstates[count++] = fsa.createState(p);
			if(s.equals(initial))
				fsa.setInitialState(Automatonstates[Integer.parseInt(initial)]);
			
			for(int f=0; f<finalStates.length ; f++){
				if(s.equals(finalStates[f])){
					fsa.addFinalState((Automatonstates[Integer.parseInt(s)]));
				}
			}
			
		}
		
		for (i=4; i<dfa.size();i++) {
			transitions = dfa.get(i).trim().split(regex);
			
			FSATransition fst = new FSATransition(Automatonstates[Integer.parseInt(transitions[0])], Automatonstates[Integer.parseInt(transitions[2])], transitions[1]);
			fsa.addTransition(fst);
		}
		OpenAction.openedAutomaton = fsa;
		AutomatonEnvironment env = (AutomatonEnvironment) EnvironmentFactory.getEnvironment((Automaton)fsa);
		/*if (problemDescription instanceof String)
		{
			JOptionPane.showMessageDialog(null, problemDescription);
			
		}*/
		//AutomatonEnvironment env = (AutomatonEnvironment) EnvironmentFactory.getEnvironment((Automaton)fsa);
		ConvertFSAToREActionNew csn = new ConvertFSAToREActionNew(
				env);
		csn.fromSearchForProblemsFlow = true;
		csn.displayData(ConvertFSAToREActionNew.fromFlow.SEARCHFORPROBLEMS,"", problemDescription);
		
	
}

public static void createDFAFromProblemDescription(String problemDescription){
	String solution = "";
	URL website;
	String problem = "";
	problem = problemDescription.replace("'", "\\'");
	try {
	website = new URL("http://dfa-varunshenoy.rhcloud.com/jar/Solutions.py");
	//Open a connection to the website
	URLConnection connection = website.openConnection();
	connection.setDoOutput(true);
	OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
	

	problem = URLEncoder.encode(problem, "UTF-8");
	out.write("problem=" +problem);
//	JOptionPane.showMessageDialog(null, "written");
	
	out.close();
	BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	String decodedString;
	while ((decodedString = in.readLine()) != null) {
		solution = solution + decodedString + "\n";
		
		
	}
	in.close();

//	JOptionPane.showMessageDialog(null, solution);
	
	}
	
	catch (MalformedURLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	//JTextArea text = new JTextArea(problemDescription);
	String[] solution_list = solution.split("\n");
	List<String> dfaReceived = Arrays.asList(solution_list);
	createAndDisplayAutomaton(dfaReceived,problemDescription);
}
}