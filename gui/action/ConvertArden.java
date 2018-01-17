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
import gui.environment.Universe;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import automata.fsa.FiniteStateAutomaton;

/**
 * This action handles the conversion of an FSA to a regular expression.
 * 
 * @author Thomas Finley
 */

public class ConvertArden extends FSAAction{
	/** The automaton environment. */
	public static String computedRE = "";
 
	private  AutomatonEnvironment environment;

	/**
	 * Instantiates a new <CODE>ConvertFSAToREAction</CODE>.
	 * 
	 * @param environment
	 *            the environment
	 */
	public ConvertArden(AutomatonEnvironment environment) {
		super("Arden", null);
		this.environment = environment;
	}

	/**
	 * This method begins the process of converting an automaton to a regular
	 * expression.
	 * 
	 * @param event
	 *            the action event
	 */

public void actionPerformed(ActionEvent event) {
	JFrame frame = Universe.frameForEnvironment(environment);

	if (environment.getAutomaton().getInitialState() == null) {
		JOptionPane.showMessageDialog(frame,
				"Conversion requires an automaton\nwith an initial state!",
				"No Initial State", JOptionPane.ERROR_MESSAGE);
		return;
	}
	if (environment.getAutomaton().getFinalStates().length == 0) {
		JOptionPane.showMessageDialog(frame,
				"Conversion requires at least\n" + "one final state!",
				"No Final States", JOptionPane.ERROR_MESSAGE);
		return;
	}

		FiniteStateAutomaton fsa = (FiniteStateAutomaton) environment.getAutomaton().clone();

		ArrayList<String> dfaText = CustomUtilities.createDFAStr(fsa);      
        try{
        computedRE = ArdenLemma.createRegularExpression(dfaText); 
        computedRE = computedRE.replace('$',  'λ');
        JOptionPane.showInputDialog(computedRE);

        computedRE = computedRE.replace("λ","0{0}");
        computedRE = computedRE.replace("+","|");

        JTextArea TA = new JTextArea(computedRE);
        TA.setSize(40,60);
        JOptionPane.showMessageDialog(null,new JScrollPane(TA));
        	// %% create regular expression using Arden's algorithm 
        
        }
        catch(Exception e){
		JOptionPane.showMessageDialog(null, "Error Generating problem. Please Try Again.","Error", JOptionPane.ERROR_MESSAGE);
		OpenAction.openedAutomaton = null;
		return;
	}
        if(computedRE == ""){
			JOptionPane.showMessageDialog(null, "Error Generating problem. Please Try Again.","Error", JOptionPane.ERROR_MESSAGE);
			OpenAction.openedAutomaton = null;
			return;
        }
	}


	
}
