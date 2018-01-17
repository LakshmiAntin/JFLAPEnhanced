
package gui.action;

import java.io.File;

import grammar.Grammar;
import gui.environment.Environment;
import gui.environment.Universe;

import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import automata.Automaton;
import automata.AutomatonSimulator;
import automata.Configuration;

import automata.Automaton;
import file.XMLCodec;
import automata.*;
import automata.fsa.*;

import gui.environment.Environment;
import gui.environment.EnvironmentFrame;
import gui.environment.Universe;

import java.awt.event.ActionEvent;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import automata.UselessStatesDetector;
import automata.fsa.FiniteStateAutomaton;
import automata.graph.FSAEqualityChecker;
import Solution.SolutionBase;

import java.net.*;

/**
 * This is the action used for the simulation of input on an automaton with no
 * interaction. This method can operate on any automaton. It uses a special
 * exception for the case of the multiple tape Turing machine.
 * 
 * @author Nitish B
 */

public class JFFtoDFA_1 extends TextAndInput {

	public JFFtoDFA_1(Grammar gram,
			Environment environment) {
		super(gram, environment);
		putValue(NAME, "Check DFA With I/P...");
		putValue(ACCELERATOR_KEY, null);
		this.environment = environment;
	}
	/**
	 * Instantiates a new <CODE>NoInteractionSimulateAction</CODE>.
	 * 
	 * @param auto
	 *            the automaton that input will be simulated on
	 * @param environment
	 *            the environment object that we shall add our simulator pane to
	 */
	public JFFtoDFA_1(Automaton automaton,
			Environment environment) {
		super(automaton, environment);
		putValue(NAME, "Check DFA With I/P...");
		putValue(ACCELERATOR_KEY, null);
		this.environment = environment;
	}

	/**
	 * Reports a configuration that accepted.
	 * 
	 * @param configuration
	 *            the configuration that accepted
	 * @param component
	 *            the parent component of dialogs brought up
	 * @return <CODE>true</CODE> if we should continue searching, or <CODE>false</CODE>
	 *         if we should halt
	 */
	protected boolean reportConfiguration(Configuration configuration,
			Component component) {
		JComponent past = (JComponent) gui.sim.TraceWindow
				.getPastPane(configuration);
		past.setPreferredSize(new java.awt.Dimension(300, 400));
		String[] options = { "Keep looking", "I'm done" };
		int result = JOptionPane.showOptionDialog(component, past,
				"Accepting configuration found!", JOptionPane.YES_NO_OPTION,
				JOptionPane.INFORMATION_MESSAGE, null, options, null);
		return result == 0;
	}

	/**
	 * Confirms if the user wants to keep searching. This should be called
	 * periodically to give the user a chance to break out of infinite loops.
	 * 
	 * @param generated
	 *            the number of configurations generated sofar
	 * @param component
	 *            the parent component of dialogs brought up
	 * @return <CODE>true</CODE> if we should continue searching, or <CODE>false</CODE>
	 *         if we should halt
	 */
	protected boolean confirmContinue(int generated, Component component) {
		int result = JOptionPane.showConfirmDialog(component, generated
				+ " configurations have been generated.  "
				+ "Should we continue?");
		return result == JOptionPane.YES_OPTION;
	}

	/**
	 * This will search configurations for an accepting configuration.
	 * 
	 * @param automaton
	 *            the automaton input is simulated on
	 * @param simulator
	 *            the automaton simulator for this automaton
	 * @param configs
	 *            the initial configurations generated
	 * @param initialInput
	 *            the object that represents the initial input; this is a String
	 *            object in most cases, but may differ for multiple tape turing
	 *            machines
	 */
	public void handleInteraction(Automaton automaton,
			AutomatonSimulator simulator, Configuration[] configs,
			Object initialInput) {

		boolean retval = validate_dfa(automaton);
		if (retval == false)
			return;

		
    	String name = "Solution.SolutionBase";
		SolutionBase obj;
		boolean solution_result;
		String myString = initialInput.toString();
		System.out.println(myString);
		try
		{
			String fileName = "Solution/SolutionBase.java";
			File file = new File(fileName);
			String path = file.getAbsolutePath();
			//System.out.println(path);

			TryJava tj = new TryJava();
			tj.runProcess("javac "+path);
			
			Class type = ClassLoader.getSystemClassLoader().loadClass(name);
			obj = (SolutionBase)type.newInstance();
			//System.out.println(initialInput.toString());
			solution_result = obj.inLanguage(myString);
			//Method met = type.getMethod("solve");

			/*File file1  = new File("JFLAP.jar");
 			URL url = file1.toURL();  
 			URL[] urls = new URL[]{url};
 			ClassLoader cl = new URLClassLoader(urls);

 			Class cls = cl.loadClass("Solution.SolutionBase");
 			solution_result = cls.inLanguage(myString); */
		}

		catch (Exception ex) 
		{
			/*JOptionPane.showMessageDialog(Universe
				.frameForEnvironment(environment),
				"Solution Class not found:" +name);*/
			return;
		}

		JFrame frame = Universe.frameForEnvironment(environment);
		// How many configurations have we had?
		int numberGenerated = 0;
		// When should the next warning be?
		int warningGenerated = WARNING_STEP;
		// How many have accepted?
		int numberAccepted = 0;
		while (configs.length > 0) {
			numberGenerated += configs.length;
			// Make sure we should continue.
			if (numberGenerated >= warningGenerated) {
				if (!confirmContinue(numberGenerated, frame))
					return;
				while (numberGenerated >= warningGenerated)
					warningGenerated *= 2;
			}
			// Get the next batch of configurations.
			ArrayList next = new ArrayList();
			for (int i = 0; i < configs.length; i++) {
				if (configs[i].isAccept()) {
					numberAccepted++;
					if (!reportConfiguration(configs[i], frame))
						return;
				} else {
					next.addAll(simulator.stepConfiguration(configs[i]));
				}
			}
			configs = (Configuration[]) next.toArray(new Configuration[0]);
		}

		if ((numberAccepted == 0 && solution_result == true) ||
			(numberAccepted == 1 && solution_result == false))
		{
			JOptionPane.showMessageDialog(frame, "DFA does not match with the solution!");
			return;
		}
		else
		{
			JOptionPane.showMessageDialog(frame, "Congratulations! DFA matches with the solution!");
		}
		/*
		if (numberAccepted == 0) {
			JOptionPane.showMessageDialog(frame, "The input was rejected.");
			return;
		}
		*/
		/*
		JOptionPane.showMessageDialog(frame, numberAccepted + " configuration"
				+ (numberAccepted == 1 ? "" : "s")
				+ " accepted, and\nother possibilities are exhausted.");
				*/
	}
	
	//copied!!!

	public void findMissingTransitions(Automaton automaton) {
		AlphabetRetriever far = new FSAAlphabetRetriever();
		Minimizer m = new Minimizer();
		String[] alphabet = far.getAlphabet(automaton);
		State[] states = automaton.getStates();
		for (int k = 0; k < states.length; k++) {
			Transition[] transitions = automaton
					.getTransitionsFromState(states[k]);
			for (int j = 0; j < alphabet.length; j++) {
				if (!m.isTransitionOnTerminal(transitions, alphabet[j])) {
					JOptionPane.showMessageDialog(Universe
				.frameForEnvironment(environment), "The Automaton is missing a transition "
									+"from state "+states[k].getName() +" on input "+alphabet[j]);
					return;
				}
			}
		}
	}
	
	public boolean validate_dfa(Automaton a) 
	{	

		AutomatonChecker ac = new AutomatonChecker();
		boolean nfa = ac.isNFA(a);

		Minimizer m = new Minimizer();
		boolean needs_trap = m.needsTrapState(a);

		if (nfa == true)
		{
			JOptionPane.showMessageDialog(Universe
				.frameForEnvironment(environment), "The Automata is not a DFA! Please "
					+"input a DFA.");
			return false;
		}
		
		else if (needs_trap == true)
		{
			/*JOptionPane.showMessageDialog(Universe
				.frameForEnvironment(environment), "The Automata is missing Tansitions!");*/
			findMissingTransitions(a);
			return false;
		}
		
		return true;
	}

	/** The environment. */
	private Environment environment = null;

	/** The steps in warnings. */
	protected static final int WARNING_STEP = 500;
}
