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
import gui.environment.Environment;
import gui.environment.Universe;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

/**
 * The <CODE>SaveAction</CODE> is an action to save a serializable object
 * contained in an environment to a file.
 * 
 * @author Thomas Finley
 */

public class SaveAction extends SaveAsAction {
	public static String easyPath = "DFA_LIBRARY" + System.getProperty("file.separator") + "Easy";
	public static String hardPath = "DFA_LIBRARY" + System.getProperty("file.separator") + "Hard";
	public static String filePath = "DFA_LIBRARY" ;

	private  boolean fromSaveAsProblem;
	
	
	/**
	 * Instantiates a new <CODE>SaveAction</CODE>. 
	 * 
	 * @param environment
	 *            the environment that holds the serializable
	 */
	public SaveAction(Environment environment) {
		super(environment);
		putValue(NAME, "Save");
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S,
				MAIN_MENU_MASK));
		this.environment = environment;
	}

	/**
	 * If a save was attempted, call the methods that handle the saving of the
	 * serializable object to a file.
	 * 
	 * @param event
	 *            the action event
	 */
	public void actionPerformed(ActionEvent event) {
		if(this.fromSaveAsProblem){
			if(new ConvertFSAToREActionNew((AutomatonEnvironment)this.environment).testBeforeSave()){
				String problemDescription = JOptionPane.showInputDialog(null,"Enter a Problem Description to be Saved","Problem Description",JOptionPane.NO_OPTION);
				if(problemDescription == null || problemDescription.length() == 0){
					JOptionPane.showMessageDialog(null, "The Problem Cannot be Saved Without a Description!", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
					Universe.frameForEnvironment(environment).saveAsProblem(problemDescription,false);
			
		}
			
		}
		
		else
		Universe.frameForEnvironment(environment).save(false);
	}
	
/*	public void saveAsProblem(String type, String problemDescription){
		Universe.frameForEnvironment(environment).saveAsProblem(type,problemDescription,false);
	}*/

	/** The environment this action will handle saving for. */
	private Environment environment;
	
	public void setSaveAsProblem(boolean saveAsProblem){
		this.fromSaveAsProblem = saveAsProblem;
		if(saveAsProblem)
			putValue(NAME, "Save As Problem");
	}
}
