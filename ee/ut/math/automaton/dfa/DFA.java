package ee.ut.math.automaton.dfa;

import ee.ut.math.automaton.core.Automaton;
import ee.ut.math.automaton.core.State;
import ee.ut.math.automaton.core.Transition;

/**
 * Deterministic finite automaton
 *
 */
public class DFA extends Automaton {
	private static final long serialVersionUID = -7798808243731550723L;

	public State getState(State state, Character character) {
		for(Transition transition : state.getOutgoing()) {
			if(transition.isCharacter() && 
					transition.getCharacter().equals(character)) {
				return transition.getTo();
			} 
		}		
		return null;
	}

}
