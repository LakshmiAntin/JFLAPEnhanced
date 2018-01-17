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





package automata.fsa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import automata.Automaton;

/**
 * This subclass of <CODE>Automaton</CODE> is specifically for a definition of
 * a regular Finite State Automaton.
 * 
 * @author Thomas Finley
 */

public class FiniteStateAutomaton extends Automaton {
	private boolean fromNewAction;
	public boolean nfaDrawn;
	public List<Map<String,String>> flowMap = new ArrayList<Map<String,String>>();
	public List<String> flowList = new ArrayList<String>();

	public long startTime;
	public boolean isFromNewAction() {
		return fromNewAction;
	}

	public void setFromNewAction(boolean fromNewAction) {
		this.fromNewAction = fromNewAction;
	}

	/**
	 * Creates a finite state automaton with no states and no transitions.
	 */
	public FiniteStateAutomaton() {
		super();
	}

	/**
	 * Returns the class of <CODE>Transition</CODE> this automaton must
	 * accept.
	 * 
	 * @return the <CODE>Class</CODE> object for <CODE>automata.fsa.FSATransition</CODE>
	 */
	protected Class getTransitionClass() {
		return automata.fsa.FSATransition.class;
	}
}
