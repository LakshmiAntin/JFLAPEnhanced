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

import ee.ut.math.automaton.regex.RegularExpression;
import gui.AboutBox;
import gui.ParseTreeTester;
import gui.environment.AutomatonEnvironment;
import gui.environment.EnvironmentFactory;

import java.awt.Point;
import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JWindow;

import automata.Automaton;
import automata.fsa.FiniteStateAutomaton;

/**
 * This action will display a small about box that lists the tool version
 * number, and other version.
 * 
 * @author Thomas Finley
 */

public class QuizMeAction extends RestrictedAction {
	/**
	 * Instantiates a new <CODE>AboutAction</CODE>.
	 */
	public QuizMeAction() {
		super("Quiz Me...", null); 
	}

	/**
	 * Shows the about box.
	 */
	public void actionPerformed(ActionEvent e) {
		Object[] possibleValues = {"Easy", "Medium" , "Hard"};
		Object selectedValue = JOptionPane.showInputDialog(null,
	            "Select the Level of Difficulty", "Regular Expression",
	            JOptionPane.INFORMATION_MESSAGE, null,
	            possibleValues, possibleValues[0]);
		
		generateRegex(selectedValue.toString(),possibleValues);

		}
	
	public static void generateRegex(String selectedValue, Object[] possibleValues){
		String regExpString = "";

		if (selectedValue==possibleValues[0])
			regExpString = ParseTreeTester.getRE(1);
	
		else if(selectedValue == possibleValues[1])
			regExpString = ParseTreeTester.getRE(2);
		else if(selectedValue == possibleValues[2])
			regExpString = ParseTreeTester.getRE(3);
		else
				return;
		
		String newregExpString = regExpString.replaceAll("\\+", "|");
		FiniteStateAutomaton dfa = RegularExpression.convertToDFA(newregExpString);
	
	OpenAction.openedAutomaton = dfa;
	AutomatonEnvironment env = (AutomatonEnvironment) EnvironmentFactory.getEnvironment((Automaton)dfa);
	ConvertFSAToREActionNew.computedRE = regExpString;
	new ConvertFSAToREActionNew(
			env).displayData(ConvertFSAToREActionNew.fromFlow.QUIZME, selectedValue, "Problem Description : \n "+regExpString);
	}
/*public static void main(String s[]){
		FiniteStateAutomaton fsa = new FiniteStateAutomaton();
		fsa.setFromNewAction(true);
		NewAction.createWindow(fsa);
	}*/
			
	}


