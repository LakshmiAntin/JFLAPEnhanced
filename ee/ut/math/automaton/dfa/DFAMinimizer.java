package ee.ut.math.automaton.dfa;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import ee.ut.math.automaton.core.State;
import ee.ut.math.automaton.core.Transition;
import ee.ut.math.automaton.core.TransitionType;

/**
 * DFA minimizer
 *
 */
public class DFAMinimizer {
	
	
	/**
	 * Minimize {@link DFA}
	 * 
	 * @param dfa DFA
	 * 
	 * @return minimized DFA
	 */
	public static DFA minimize(DFA dfa) {
		Map<State, Set<State>> stateSetMapping = new HashMap<State, Set<State>>();
		
		Set<Set<State>> sets = partition(dfa, stateSetMapping);
		
		return createMinimizedAutomaton(dfa, stateSetMapping, sets);
	}
	
	
	
	private static Set<Set<State>> partition(DFA automaton, Map<State, Set<State>> stateSetMapping) {
		
		Set<Set<State>> sets = initSets(automaton, stateSetMapping);
		
		Set<Set<State>> partition = null;
		
		while(!sets.equals(partition)) {
			partition = sets;
			sets = new LinkedHashSet<Set<State>>();
			
			for(Set<State> set : partition) {
				split(automaton, set, stateSetMapping, sets);
			}
		}
		
		return sets;
	}
	
	
	
	private static Set<Set<State>> initSets(DFA automaton, Map<State, Set<State>> stateSetMapping) {
		Set<Set<State>> sets = new LinkedHashSet<Set<State>>();
		
		Set<State> finalStates = new LinkedHashSet<State>();
		Set<State> nonFinalStates = new LinkedHashSet<State>();
				
		for(State state : automaton.getStates()) {
			Set<State> set = state.isAccept() ? finalStates : nonFinalStates;
			
			set.add(state);
			stateSetMapping.put(state, set);
		}
		
		sets.add(finalStates);
		sets.add(nonFinalStates);
		
		return sets;
	}
	
	
	
	private static void split(DFA automaton, Set<State> set, 
				Map<State, Set<State>> stateSetMapping, Set<Set<State>> sets) {
		
		Set<State> firstSet = null;
		Set<State> secondSet = null;
		
		boolean splitted = false;
		
		for(Character c : automaton.getAlphabet()) {
			firstSet = new LinkedHashSet<State>();
			secondSet = new LinkedHashSet<State>();
								
			Set<State> firstToSet = null;
			
			boolean first = true;
			
			for(State state : set) {
				State toState = automaton.getState(state, c);
				
				Set<State> toSet = toState == null ? null : stateSetMapping.get(toState);
				
				if(first) {
					firstToSet = toSet;
					
					firstSet.add(state);
					
					first = false;
				} else if(firstToSet == null && toSet == null || 
						firstToSet != null && firstToSet.equals(toSet)) {
					firstSet.add(state);
				} else {
					secondSet.add(state);
				}
			}
						
			if(!secondSet.isEmpty()) {
				splitted = true;
				
				break;
			}
		}
		
		
		if(splitted) {
			for(State state : firstSet) {
				stateSetMapping.put(state, firstSet);
			}
			
			for(State state : secondSet) {
				stateSetMapping.put(state, secondSet);
			}
			
			sets.add(firstSet);
			sets.add(secondSet);
		} else {
			sets.add(set);
		}
	}
	
	
	
	private static DFA createMinimizedAutomaton(
			DFA automaton, Map<State, Set<State>> stateSetMapping,
			Set<Set<State>> sets) {
		
		DFA minimizedAutomaton = new DFA();
		minimizedAutomaton.setAlphabet(automaton.getAlphabet());
		
		Map<Set<State>, State> minimizedSetStateMapping = new HashMap<Set<State>, State>();
		
		for(Set<State> set : sets) {			
			State minimizedState = new DFAState();
			
			minimizedSetStateMapping.put(set, minimizedState);
		}
		
		LinkedList<State> minimizedStates = new LinkedList<State>();
		
		for(Set<State> set : sets) {
			if(set.isEmpty()) { // non-final sets can be empty
				continue;
			}
			State state = set.iterator().next();
			
			State minimizedState = minimizedSetStateMapping.get(set);
			
			for(Character c : automaton.getAlphabet()) {
				State toState = automaton.getState(state, c);
				
				if(toState != null) {
					Set<State> toStateSet = stateSetMapping.get(toState);
					
					State minimizedToState = minimizedSetStateMapping.get(toStateSet);
					
					new Transition(minimizedState, minimizedToState, TransitionType.CHARACTER, c);
				}
			}
									
			boolean initial = false;
			for(State s : set) {
				if(automaton.getInitialState().equals(s)) {
					initial = true;					
					break;
				}
			}
			
			if(initial) {
				minimizedStates.addFirst(minimizedState);
			} else {
				minimizedStates.addLast(minimizedState);
			}
			
			if(state.isAccept()) {
				minimizedState.setAccept(true);
			}
		}
		
		boolean first = true;
		for(State minimizedState : minimizedStates) {
			if(first) {
				minimizedAutomaton.addState(minimizedState);
				minimizedAutomaton.setInitialState(minimizedState);				
				first = false;
			} else {
				minimizedAutomaton.addState(minimizedState);
			}
		}
		
		return minimizedAutomaton;
	}
	
}
