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
import gui.environment.EnvironmentFrame;
import gui.environment.Universe;
import gui.environment.tag.CriticalTag;
import gui.regular.ConvertPane;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import automata.Automaton;
import automata.AutomatonChecker;
import automata.State;
import automata.Transition;
import automata.UselessStatesDetector;
import automata.fsa.FSATransition;
import automata.fsa.FiniteStateAutomaton;
import automata.graph.FSAEqualityChecker;

/**
 * This tests to see if two finite state automatons accept the same language.
 * 
 * @author Thomas Finley
 */

public class DFAEqualityAction extends FSAAction {
    public static String ACCEPT_STRINGS = "AcceptStrings";
    public static String REJECT_STRINGS = "RejectStrings";

 
	/**
	 * Instantiates a new <CODE>DFAEqualityAction</CODE>.
	 * 
	 * @param automaton
	 *            the automaton that input will be simulated on
	 * @param environment
	 *            the environment object that we shall add our simulator pane to
	 */
	public DFAEqualityAction(FiniteStateAutomaton automaton,
			Environment environment) {
		super("Compare Equivalence", null);
		this.environment = environment;
		/*
		 * putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke (KeyEvent.VK_R,
		 * MAIN_MENU_MASK+InputEvent.SHIFT_MASK));
		 */
	}

	/**
	 * Runs a comparison with another automaton.
	 * 
	 * @param e
	 *            the action event
	 */
	public void actionPerformed(ActionEvent e) {
		JComboBox combo = new JComboBox();
		// Figure out what existing environments in the program have
		// the type of structure that we need.
		EnvironmentFrame[] frames = Universe.frames();
		for (int i = 0; i < frames.length; i++) {
			if (!isApplicable(frames[i].getEnvironment().getObject())
					|| frames[i].getEnvironment() == environment)
				continue;
			combo.addItem(frames[i]);
		}
		// Set up our automaton.
		FiniteStateAutomaton automaton = (FiniteStateAutomaton) environment
				.getObject();

		if (combo.getItemCount() == 0) {
			JOptionPane.showMessageDialog(Universe
					.frameForEnvironment(environment), "No other FAs around!");
			return;
		}
		if (automaton.getInitialState() == null) {
			JOptionPane.showMessageDialog(Universe
					.frameForEnvironment(environment),
					"This automaton has no initial state!");
			return;
		}
		// Prompt the user.
		int result = JOptionPane.showOptionDialog(Universe
				.frameForEnvironment(environment), combo, "Compare against FA",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
				null, null, null);
		if (result != JOptionPane.YES_OPTION && result != JOptionPane.OK_OPTION)
			return;
		FiniteStateAutomaton other = (FiniteStateAutomaton) ((EnvironmentFrame) combo
				.getSelectedItem()).getEnvironment().getObject();
		if (other.getInitialState() == null) {
			JOptionPane.showMessageDialog(Universe
					.frameForEnvironment(environment),
					"The other automaton has no initial state!");
			return;
		}
		other = (FiniteStateAutomaton) UselessStatesDetector
				.cleanAutomaton(other);
		automaton = (FiniteStateAutomaton) UselessStatesDetector
				.cleanAutomaton(automaton);
		String checkedMessage = checker.equals(other, automaton) ? "They ARE equivalent!"
				: "They AREN'T equivalent!";
		JOptionPane.showMessageDialog(
				Universe.frameForEnvironment(environment), checkedMessage);
	}
	
	public void compareDFAAnswer(){
		JComboBox combo = new JComboBox();
	
		// Set up our automaton.
		FiniteStateAutomaton automaton = (FiniteStateAutomaton) environment
				.getObject();

		if (automaton.getInitialState() == null) {
			JOptionPane.showMessageDialog(Universe
					.frameForEnvironment(environment),
					"This automaton has no initial state!");
        	Map<String,String> testAnswerFlow = new HashMap<String,String>(); 
			testAnswerFlow.put("Result", "The Automaton has no Initial State");
    		prepareAutomatonString(testAnswerFlow);
    		automaton.flowMap.add(testAnswerFlow);
			return;
		}
		if (automaton.getFinalStates().length == 0) {
			JOptionPane.showMessageDialog(Universe
					.frameForEnvironment(environment),
					"Conversion requires at least\n" + "one final state!",
					"No Final States", JOptionPane.ERROR_MESSAGE);
			Map<String,String> testAnswerFlow = new HashMap<String,String>(); 
			testAnswerFlow.put("Result", "The Automaton has no Final State");
    		prepareAutomatonString(testAnswerFlow);
    		automaton.flowMap.add(testAnswerFlow);
			return ;
		}
		
		if(checkIfNFA())
			return;
		
		
		if(OpenAction.openedAutomaton == null){
			JOptionPane.showMessageDialog(Universe
					.frameForEnvironment(environment), "No Other FAs Around!. Either You Have Closed the Problem Description or not Quizzed for One!");
			return;
		}
		FiniteStateAutomaton other = (FiniteStateAutomaton)OpenAction.openedAutomaton;
		
		other = (FiniteStateAutomaton) UselessStatesDetector
				.cleanAutomaton(other);
		FiniteStateAutomaton cleanedAutomaton = (FiniteStateAutomaton) UselessStatesDetector
				.cleanAutomaton(automaton);
		if(!checker.equals(other, cleanedAutomaton)){
			prepareStringsAndDisplay();
		}
		else{
			ConvertFSAToREActionNew.correctAnswerDrawn = true;
        	final long time = System.currentTimeMillis() - automaton.startTime;
        	int elapsedTimesec =(int)( time/(1000F));	
        	Map<String,String> testAnswerFlow = new HashMap<String,String>(); 
    		testAnswerFlow.put("Time" , new Integer(elapsedTimesec).toString());
    		prepareAutomatonString(testAnswerFlow);
    		automaton.flowMap.add(testAnswerFlow);
			automaton.flowList.add("Correct Automaton was drawn in " + elapsedTimesec + " Seconds\n" );
			String message = customSuccessMessage(testAnswerFlow);
    		testAnswerFlow.put("Result", message);
			automaton.cfrReference.performCloseOperation(null);
			automaton.cfrReference.questionFrame.dispose();
		}
	}
	
	
	private String prepareRE(Automaton automaton){
		/*try{
			ConvertFSAToREActionNew.computedRE = "";
			AutomatonEnvironment at = new AutomatonEnvironment(automaton);
			ConvertFSAToREActionNew.secondAction = true;
			ConvertPane pane = new ConvertPane(at);
			ConvertFSAToREActionNew.secondAction = false;
		}*/
		String computedRE = "";
		ConvertFSAToREActionNew.computedRE = "";
		try{
			
			new ConvertFSAToREActionNew(new AutomatonEnvironment(automaton)).checkAndApplyLamdaTransition(automaton);

			computedRE = "";
			ConvertFSAToREActionNew.secondAction = true;

		ConvertPane pane = new ConvertPane(new AutomatonEnvironment(automaton));

		ConvertFSAToREActionNew.secondAction = false;
		environment.add(pane, "Convert FA to RE", new CriticalTag() {
		});
		//environment.setActive(pane);
		environment.remove(pane);		
			
			
		}
			
			
			
			
			
			
	/*		
			
			
			boolean jFlapAutomaton = CustomUtilities.checkIfJFLAPAutomaton(automaton);
			if(jFlapAutomaton){
					computedRE = "";
				ConvertFSAToREActionNew.secondAction = true;
					ConvertPane pane = new ConvertPane(new AutomatonEnvironment(automaton));
		
				ConvertFSAToREActionNew.secondAction = false;
				
		        computedRE = ConvertFSAToREActionNew.computedRE;

			
			}	
			else{

				try{
					FiniteStateAutomaton fsa = (FiniteStateAutomaton) (automaton.clone());
		
					ArrayList<String> dfaText = CustomUtilities.createDFAStr(fsa);      
			       
			        computedRE = ArdenLemma.createRegularExpression(dfaText); 
			        computedRE = computedRE.replace('$',  'λ');
			        return computedRE;
		        	// %% create regular expression using Arden's algorithm 
				}
				catch(Exception e){
					//JOptionPane.showMessageDialog(null, "Error Generating problem. Please Try Again.","Error", JOptionPane.ERROR_MESSAGE);
					//OpenAction.openedAutomaton = null;
					return null;
				}
			        
		   }*/
	
		catch(Exception e){
			ConvertFSAToREActionNew.secondAction = false;
			//JOptionPane.showMessageDialog(null, "Error Generating problem. Please Try Again.","Error", JOptionPane.ERROR_MESSAGE);
		//	OpenAction.openedAutomaton = null;

	        if(ConvertFSAToREActionNew.computedRE == ""){
				//JOptionPane.showMessageDialog(null, "Error Generating problem. Please Try Again.","Error", JOptionPane.ERROR_MESSAGE);
				return null;
	        }
	        computedRE = ConvertFSAToREActionNew.computedRE;
			
		}
		 computedRE = ConvertFSAToREActionNew.computedRE;
        return computedRE;
	}

/*		
        try{
        	FiniteStateAutomaton fsa = (FiniteStateAutomaton)automaton;
    		ArrayList<String> dfaText = CustomUtilities.createDFAStr(fsa);      
        computedRE = ArdenLemma.createRegularExpression(dfaText);                       // %% create regular expression using Arden's algorithm 
        
        }
		catch(Exception e){
		//	JOptionPane.showMessageDialog(null, "Error Generating problem. Please Try Again.","Error", JOptionPane.ERROR_MESSAGE);
		}*/
		
	
	private void prepareStringsAndDisplay(){
		FiniteStateAutomaton userAutomaton = (FiniteStateAutomaton) environment
				.getObject();
		System.out.println(userAutomaton.toString());
		final Map<String,String> testAnswerFlow = new HashMap<String,String>(); 
		testAnswerFlow.put("Result", "DFA drawn was Incorrect");
		prepareAutomatonString(testAnswerFlow);

		 String acceptTheseStrings = "";
		 String rejectTheseStrings = "";
		String originalRE = prepareRE((Automaton)OpenAction.openedAutomaton.clone());
		String userRE = prepareRE((Automaton)userAutomaton.clone());

		
		List<String> originalPossibleStrings = CustomUtilities.getXegerList(originalRE);
		List<String> userPossibleStrings = CustomUtilities.getXegerList(userRE);
		for(String str : originalPossibleStrings){
			if(CustomUtilities.checkInputString(environment,str) != 0){
				acceptTheseStrings += (str + "\n");
			}
		}
		
		AutomatonEnvironment answerEnvironment = new AutomatonEnvironment(OpenAction.openedAutomaton);
		for(String str : userPossibleStrings){

			if(CustomUtilities.checkInputString(answerEnvironment, str) != 0){
				if(!str.equals("\u00F8"))
				rejectTheseStrings += (str + "\n");
			}
		}
		
		JPanel jp = new JPanel();
		JButton jb1 = new JButton("Accept These Strings");
		JButton jb2 = new JButton("Reject These Strings");
		jp.setLayout(new GridBagLayout());
		List<String> incorrectList = new ArrayList<String>();
		String s = "\nIncorrect Answer : \n";
		if(acceptTheseStrings == "\u00F8" || acceptTheseStrings == "" || acceptTheseStrings.length() == 0){
			jb1.setEnabled(false);
		}
		else{
			 s += "Accept [" + acceptTheseStrings.replace("\n", ",") + "]\n";
			 testAnswerFlow.put("Accept", acceptTheseStrings);
			
		}
		
		if(rejectTheseStrings == "\u00F8" || rejectTheseStrings == "" || rejectTheseStrings.length() == 0){
			jb2.setEnabled(false);
		}
		else{
				s += "Reject [" + rejectTheseStrings.replace("\n", ",") + "]" ;
				testAnswerFlow.put("Reject", rejectTheseStrings);
	    }
	
			userAutomaton.flowList.add(s + "\n");
			userAutomaton.flowMap.add(testAnswerFlow);
		
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
		final String s1 = acceptTheseStrings;
		final String s2 = rejectTheseStrings;
		jb1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				/*if(ConvertFSAToREActionNew.hintsUsed.get(ACCEPT_STRINGS) != null){
            		int clickCount = ConvertFSAToREActionNew.hintsUsed.get(ACCEPT_STRINGS);
            		ConvertFSAToREActionNew.hintsUsed.put(ACCEPT_STRINGS,++clickCount);
            	}
            	else
            		ConvertFSAToREActionNew.hintsUsed.put(ACCEPT_STRINGS,1);*/
				if(testAnswerFlow.get(ACCEPT_STRINGS) != null){
            		String clickCount = testAnswerFlow.get(ACCEPT_STRINGS);
            		int click = Integer.parseInt(clickCount);
            		testAnswerFlow.put(ACCEPT_STRINGS,new Integer(++click).toString());
            	}
            	else
            		testAnswerFlow.put(ACCEPT_STRINGS,"1");
				JTextArea jt = new JTextArea(s1);
				jt.setSize(20, 60);
				JOptionPane.showMessageDialog(null,new JScrollPane(jt));
				
			}
		});
		
		jb2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				/*if(ConvertFSAToREActionNew.hintsUsed.get(REJECT_STRINGS) != null){
            		int clickCount = ConvertFSAToREActionNew.hintsUsed.get(REJECT_STRINGS);
            		ConvertFSAToREActionNew.hintsUsed.put(REJECT_STRINGS,++clickCount);
            	}
            	else
            		ConvertFSAToREActionNew.hintsUsed.put(REJECT_STRINGS,1);*/
				
				if(testAnswerFlow.get(REJECT_STRINGS) != null){
            		String clickCount = testAnswerFlow.get(REJECT_STRINGS);
            		int click = Integer.parseInt(clickCount);
            		testAnswerFlow.put(REJECT_STRINGS,new Integer(++click).toString());
            	}
            	else
            		testAnswerFlow.put(REJECT_STRINGS,"1");
				JTextArea jt = new JTextArea(s2);
				jt.setSize(20, 60);
				JOptionPane.showMessageDialog(null,new JScrollPane(jt));
				
			}
		});
		main.add(jp);
		JOptionPane.showMessageDialog(
				Universe.frameForEnvironment(environment),main,"ERROR",JOptionPane.ERROR_MESSAGE);
		
      }
	
	private String customSuccessMessage(Map<String,String> testAnswerFlow){
		String message = null;
		try{
		int originalNumberOfStates = 0,  usrNumberOfStates = 0;
		FiniteStateAutomaton userAutomaton = (FiniteStateAutomaton) environment
				.getObject();
		if(OpenAction.openedAutomaton != null){
			originalNumberOfStates = OpenAction.openedAutomaton.getStates().length;
			usrNumberOfStates = userAutomaton.getStates().length;
			if(originalNumberOfStates == usrNumberOfStates){
				message = "Smallest DFA was drawn!";
				JOptionPane.showMessageDialog(
						Universe.frameForEnvironment(environment),"Congrats!! You've drawn the smallest DFA","Correct Answer",JOptionPane.INFORMATION_MESSAGE);
				return message;
			}
			else if(originalNumberOfStates < usrNumberOfStates){
				final JPanel shorterAnswerPanel = new JPanel();
				JButton jb = new JButton("View a smaller answer");
				shorterAnswerPanel.setLayout(new GridBagLayout());
				GridBagConstraints c = new GridBagConstraints();
				c.fill = GridBagConstraints.BOTH;
				c.weightx = 0;
				c.gridwidth = GridBagConstraints.REMAINDER;
				message = "Correct DFA was drawn. It could have been smaller by " + (usrNumberOfStates - originalNumberOfStates) + ((usrNumberOfStates - originalNumberOfStates)>1? "states" : "state");
				JLabel shorterAnswer = new JLabel("Your Answer is right. You could have made it smaller by " + (usrNumberOfStates - originalNumberOfStates) + ((usrNumberOfStates - originalNumberOfStates)>1?" states" : " state"));
				shorterAnswerPanel.add(shorterAnswer,c);
				shorterAnswerPanel.add(jb,c);
				
				jb.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						
						OpenAction.openedAutomaton.fromShowAnswer = true;
						OpenAction.openedAutomaton.showOnTop = true;
						 Window win = SwingUtilities.getWindowAncestor(shorterAnswerPanel);
						 win.setVisible(false);
						try{
	                	NewAction.createWindow(OpenAction.openedAutomaton);
						}
						catch(Exception e){
							JOptionPane.showInputDialog(e.getStackTrace());
						}
					}
				});
				
				JOptionPane.showMessageDialog(
						Universe.frameForEnvironment(environment), shorterAnswerPanel, "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
				return message;
			}
		}
		
		}
		catch(Exception e){
		}
		JOptionPane.showMessageDialog(
				Universe.frameForEnvironment(environment),"Congrats !! . You've drawn the Right DFA","Correct Answer",JOptionPane.INFORMATION_MESSAGE);
		return "Correct DFA was drawn!";

	}
	private boolean checkIfNFA(){
		final FiniteStateAutomaton automaton = (FiniteStateAutomaton) environment
				.getObject();

		AutomatonChecker ac = new AutomatonChecker();
		if (ac.isNFA(automaton)) {
			JPanel jp = new JPanel();
			JButton jb = new JButton("Highlight Non-Determinism");
			jb.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					
					 new NondeterminismAction((automata.Automaton) automaton,
								environment).actionPerformed(null);
				}
			});
			jp.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.BOTH;
			c.weightx = 0;
			c.gridwidth = GridBagConstraints.REMAINDER;

		
			jp.add(new JLabel("You've drawn an NFA. Please draw a DFA"),c);
			jp.add(jb,c);
			JOptionPane.showMessageDialog(
					Universe.frameForEnvironment(environment), jp, "NFA", JOptionPane.ERROR_MESSAGE);
			automaton.nfaDrawn = true;
			Map<String,String> testAnswerFlow = new HashMap<String,String>();
			testAnswerFlow.put("Result", "An NFA was drawn");
			
    		prepareAutomatonString(testAnswerFlow);
    		automaton.flowMap.add(testAnswerFlow);

			return true;
	    }
		return false;
  }
	
	/** The environment. */
	private Environment environment;

	/** The equality checker. */
	private static FSAEqualityChecker checker = new FSAEqualityChecker();
	private void prepareAutomatonString(Map<String,String> testAnswerFlow){
		FiniteStateAutomaton aut = (FiniteStateAutomaton)environment.getObject();
		String str = "";
		for(State state : aut.getStates()){
			if(str.length() > 1)
				str += "," ;
			str += state.getID() + " " + state.getPoint().x + " " + state.getPoint().y;
			if(aut.isInitialState(state))
				str += " Initial";
			if(aut.isFinalState(state))
				str += " Final";
		}
		testAnswerFlow.put("States", str);
		
		str = "";
		FiniteStateAutomaton ft = null;
		for(Transition t : aut.getTransitions()){
			if(str.length() > 1)
				str += "," ;
			str += t.getFromState().getID() + " " + ((FSATransition)t).getLabel() + " " +  t.getToState().getID();
		}
		testAnswerFlow.put("Transitions", str);

	}
}
