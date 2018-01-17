package gui.action;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ArdenLemma {
	static int i;

	public static String createRegularExpression(ArrayList<String> DFAText){
		
		Executor dfa = new Executor();

		// accpets strings from dfatext and prepares the dfa object using its built-in DFA class
		if(dfa.process(DFAText)){
			return dfa.getRE();
		}
		else{
			return null;
		}
			
	}

}

class Executor{
	
private DFA dfa;
	
	public Executor() {}
	
	public void initializeAutomaton() {
		dfa = new DFA();
	}
	
	public void addSymbol(String s) {
		dfa.addSymbol(s);
	}
	
	public void addState(String e) {
		dfa.addState(e);
	}
	
	public void setFinalState(String e) {
		dfa.setFinalState(e);
	}
	
	public void setInitialState(String e) {
		dfa.setInitialState(e);
	}
		
	public void addTransition(String ea, String s, String ep) {
		dfa.addTransition(ea,s,ep);
	}
	
	public int numberTransitions() {
		return dfa.getStates().size()*dfa.getAlphabet().size();
	}
	
	public String getRE() {
		Arden ar = new Arden(dfa);                                         // creates an arden's object.
		return ar.getRE();
	}
	
	

	public boolean process(ArrayList<String> lines) {                                            
		try {
			int size,i;
			String regex = " +";
			String[] transitions;
			String[] symbols = lines.get(0).trim().split(regex);                                   // %% split the text into states and transitions & initialize automaton 
			String[] states = lines.get(1).trim().split(regex);
			String[] finalStates = lines.get(2).trim().split(regex);
			String initial = lines.get(3).trim();
			
			initializeAutomaton();
			size = symbols.length;
			for (i=0; i<size; i++) addSymbol(symbols[i]);
			size = states.length;
			for (i=0; i<size; i++) addState(states[i]);
			size = finalStates.length;
			for (i=0; i<size; i++) setFinalState(finalStates[i]);                                  // CREATES THE DFA OBJECT
			setInitialState(initial);
			for (i=4; i<lines.size();i++) {
				transitions = lines.get(i).trim().split(regex);
				if (transitions.length != 3) {
					return false;
				}
				addTransition(transitions[0],transitions[1],transitions[2]);
			}
			return true;
		} catch (NullPointerException e) {                                                       // return true if dfa is well formed.
			System.out.println("c");
			return false;
		} catch (IndexOutOfBoundsException e) {
			System.out.println("d");
			return false;
		}
		
	}
		public  boolean readFile(String path) {
			try {
		        BufferedReader in = new BufferedReader(new FileReader(path));
		        ArrayList<String> lines = new ArrayList<String>();
		        while (in.ready()) lines.add(in.readLine());
	            in.close();
	            return process(lines);
		    } catch (IOException e) {
		    	System.out.println("Error reading.");
				return false;
		    }
		}
		
		public static String readString() {
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				return br.readLine();
			} catch (IOException e) {
				return "NULL";
			}
		}
}


class Arden {
	private DFA dfa;
	private TreeMap<Integer, StateObject> stateEqns;
	private int numberOfNonInitialStates;

	public Arden(DFA dfa) {                                                                // % in the constructor , the equations are created and stored in stateqens tree map.
		this.dfa = dfa;
		stateEqns = new TreeMap<Integer, StateObject>();
		int i = 1;
		//initializes the map placing it states with their respective equations
		for (DFAState e: this.dfa.getStates()) {
			if (e.compareTo(dfa.getInitialState()) == 0) {
				stateEqns.put(1,new StateObject(e.getEquation(), e.getState()));              // tree map - key number of state and object is state object.- eqns , name
			} else {
				++i;
				stateEqns.put(i, new StateObject(e.getEquation(), e.getState()));
			}
		}
		StateObject.states = this.dfa.getStates();
		checkDeadStates();
		checkIfEmptyEqn();
		numberOfNonInitialStates = i;
	}
	
	private void checkIfEmptyEqn(){
		for(StateObject equation : stateEqns.values()){                                                             
			if(!equation.getEquation().contains("L")){
				boolean foundFinal = false;
				for(DFAState s: dfa.getFinalStates()){
					if(s.getState().equals(equation.getState()) && s.isFinal()){
						foundFinal = true;
						equation.setEquation("{E}");
						break;
					}
				}
				if(!foundFinal)
				equation.setEquation("");

			
			}
		}
	}
	
	private void checkDeadStates(){                                    // ignore dead states.
		for(StateObject equation : stateEqns.values()){
			if(equation.getEquation().contains("L(") && !isFinalState(equation.getState())){
				boolean foundOtherState = false;
				for(DFAState s: dfa.getStates()){
					if(s.getState() != equation.getState() && equation.getEquation().contains("L("+s.getState()+")")){
						foundOtherState = true;
						break;
				}
				}
					if(!foundOtherState && !isFinalState(equation.getEquation())){
						replaceWithEmpty(equation.getState());
					}
			}
		}
	}
	
	
	
	public void replaceWithEmpty(String stateName){
		for(StateObject equation : stateEqns.values()){
			String tempEqn = "";
			String[] split = StateObject.splitSubstitutingString(equation.getEquation());
			for(String s : split){
				if(s.contains("L("+stateName + ")")){
					continue;
				}
				if(tempEqn == "")
					tempEqn = s;
				else
					tempEqn += "U" + s;
					
			}
			equation.setEquation(tempEqn);
		}
	}
	
	public boolean isFinalState(String stateName){
		for(DFAState ds : dfa.getFinalStates()){
			if(ds.getState().equals(stateName))
				return true;
		}
		return false;
	}
	
	
	public String getRE() {                                               // find the last state and loop from there.
		for (int i = numberOfNonInitialStates; i > 0; i--) {

			if (i == numberOfNonInitialStates) {
				if (!stateEqns.get(i).check()) {                         // check if rhs has same state name as lhs and apply arden
					stateEqns.get(i).setEquation(stateEqns.get(i).simplify(stateEqns.get(i).getState(),stateEqns.get(i).getEquation()));
					stateEqns.get(i).arden(i == 1);                    // calls simplify and performs arden and store it in stateEqns tree map.
				}
			} else {
				for (int j = numberOfNonInitialStates; j > i; j--) {                                             // perform substitution.
					stateEqns.get(i).replace(stateEqns.get(j).getState(),stateEqns.get(j).getEquation());
				}
				if (!stateEqns.get(i).check()) {
					stateEqns.get(i).setEquation(stateEqns.get(i).simplify(stateEqns.get(i).getState(),stateEqns.get(i).getEquation()));
					stateEqns.get(i).arden(i==1);                              // check after substitution if it is in arden's form and perform resubstitution.
				}
			}
		}	
		String finalStr = stateEqns.get(1).getEquation();                                         // final string simplification
		String A = "",B = "";
		if((finalStr.split("\\{E\\}")).length > 1){
			finalStr = finalStr.replace("UU", "U");                                                // if two 2 Us are together
			String[] splits = StateObject.splitSubstitutingString(finalStr);
			for (String s : splits) {
				if (s.contains("{E}")) {
					if(s.equals("{E}"))
						s = "λ";
					else
					s = s.replace("{E}", "");
					if (A != "")
						A += "U" + s;
					else
						A = s;
				} else {
					if (B != "")
						B += "U" + s;                                                             
					else
						B = s;

				}
			}
			if(B != "")
			finalStr = B + "U" + "(" + A + "){E}";
			else
				finalStr = "(" + A + ")" + "{E}";	
			}
		finalStr = removeDeadStrings(finalStr);
		finalStr = finalStr.replace("{E}", "");                                        // replace E and U with empty string and +.
		finalStr = finalStr.replace("U", "+");
		finalStr = finalStr.replace("++", "+");

		finalStr = minimizeParenthesis(finalStr);
		return finalStr;                                                              // final regex is provided to to the user
	}
	
	private String removeDeadStrings(String finalStr){
		String[] spl = StateObject.splitSubstitutingString(finalStr);
		String s1= "";
		for (String s : spl) {
			if (s.contains("{E}")) {
				if (s1 != "")
					s1 = "U" + s;
				else
					s1 = s;
			}
		}
		return s1;

	}
	
	public static String minimizeParenthesis(String str)
 {
		String exp = new String(str);
		Stack beginIndices = new Stack();
		int curIndex = 0;

		if (str.length() <= 2)
			return new String(str);

		while (curIndex < exp.length()) {
			// find all the beginning parenthesis and push them onto the stack
			while (curIndex < exp.length() && exp.charAt(curIndex) != ')') {
				if (exp.charAt(curIndex) == '(') {
					beginIndices.push(new Integer(curIndex));
				}
				++curIndex;
			}

			// pop the index of the begin parenthesis and figure out
			// whether to remove them or not
			if (!beginIndices.isEmpty()) {
				int index = ((Integer) beginIndices.pop()).intValue();
				String temp = exp.substring(index + 1, curIndex);

				if (temp.length() > 0 && temp.charAt(0) == '('
						&& temp.charAt(temp.length() - 1) == ')') {
					if (!findOrOp(temp) && curIndex + 1 < exp.length()
							&& exp.charAt(curIndex + 1) != '*') { // (( )( )) or
																	// (( ))
						if (curIndex + 1 < exp.length()) {
							exp = exp.substring(0, index) + temp
									+ exp.substring(curIndex + 1, exp.length());
						} else {
							exp = exp.substring(0, index) + temp;
						}
						curIndex -= 2;
					} else if (findOrOp(temp)
							&& (index == 0 || (index-1 >= 0 && index-1 < temp.length() && temp.charAt(index - 1) == '+'))
							&& (curIndex + 1 > exp.length() - 1 || (((curIndex +1) < exp.length()) && exp
									.charAt(curIndex + 1) == '+'))) {// (( )+( ))
																	// or ( )+(
																	// )
						if (curIndex + 1 < exp.length()) {
							exp = exp.substring(0, index) + temp
									+ exp.substring(curIndex + 1, exp.length());
						} else {
							exp = exp.substring(0, index) + temp;
						}
						curIndex -= 2;
					}
				}
				// ( + ) or ( )
				else if (index == 0 && curIndex == exp.length() - 1) {
					if (curIndex + 1 < exp.length()) {
						exp = exp.substring(0, index) + temp
								+ exp.substring(curIndex + 1, exp.length());
					} else {
						exp = exp.substring(0, index) + temp;
					}
					curIndex -= 2;
				}
				// +( + )+
				else if ((index == 0 || exp.charAt(index - 1) == '+' || exp
						.charAt(index - 1) == '(')
						&& (curIndex + 1 > exp.length() - 1
								|| exp.charAt(curIndex + 1) == '+' || exp
								.charAt(curIndex + 1) == ')')) {
					if (curIndex + 1 < exp.length()) {
						exp = exp.substring(0, index) + temp
								+ exp.substring(curIndex + 1, exp.length());
					} else {
						exp = exp.substring(0, index) + temp;
					}
					curIndex -= 2;
				} else if (!findOrOp(temp)
						&& (curIndex + 1 > exp.length() - 1 || exp
								.charAt(curIndex + 1) != '*')) {
					if (curIndex + 1 < exp.length()) {
						exp = exp.substring(0, index) + temp
								+ exp.substring(curIndex + 1, exp.length());
					} else {
						exp = exp.substring(0, index) + temp;
					}
					curIndex -= 2;
				} else if (temp.length() == 1) {
					if (curIndex + 1 < exp.length()) {
						exp = exp.substring(0, index) + temp
								+ exp.substring(curIndex + 1, exp.length());
					} else {
						exp = exp.substring(0, index) + temp;
					}
					curIndex -= 2;
				}
			}

			++curIndex;
		}
		String newexp = "";
		for (int i = 0; i < exp.length(); i++) {
			if (exp.charAt(i) == 'L') {
				newexp += exp.charAt(i);
				i++;
				newexp += '(';
				newexp += exp.charAt(i);
				newexp += ')';

			}
			else
				newexp += exp.charAt(i);
		}
		return newexp;
	}
	
	 
	   private static boolean findOrOp(String str)
 {
		int index = 0;

		for (int x = 0; x < str.length(); ++x) {
			switch (str.charAt(x)) {
			case '(':
				++index;
				break;
			case ')':
				--index;
				break;
			case '+':
				if (index == 0)
					return true;
				break;
			}
		}

		return false;
	}

	   
}
	

class DFAState implements Comparable<DFAState> {
	private String state;
	private TreeMap<Alphabet, DFAState> transitions;
	private boolean isFinal = false;

	public DFAState(String state) {
		super();
		this.state = state;
		transitions = new TreeMap<Alphabet, DFAState>();
	}

	// Returns the equation that takes the "state" state to a final state
	public String getEquation() {
		String L = "";
		int i = 0;
		Set<Alphabet> keyset = transitions.keySet();
		Iterator<Alphabet> it = keyset.iterator();
		while (it.hasNext()) {
			Alphabet s = it.next();
			if (it.hasNext()) {
				L = L.concat(s.toString() + "L(" + transitions.get(s) + ")U");
				i += transitions.get(s).toString().length() + 2;
			} else {
				L = L.concat(s.toString() + "L(" + transitions.get(s) + ")");
				i += transitions.get(s).toString().length() + 1;
			}
		}
		if (isFinal)
			L = L.concat("U{E}");
		return L;
	}

	// Returns the symbols that make the transition from "state" to "and"
	public String alphabetTransition(DFAState e) {
		String st = "";
		if (transitions.containsValue(e)) {
			for (Alphabet s : transitions.keySet()) {
				if (transitions.get(s).getState().equals(e.getState())) {
					if (st.equals("")) {
						st = s.getSymbol();
					} else {
						st += "U" + s.getSymbol();
					}
				}
			}
		}
		if (st.equals(""))
			return "{}";
		return st;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Override
	public int compareTo(DFAState o) {
		return state.compareTo(o.getState());
	}

	public void addTransition(Alphabet s, DFAState e) {
		transitions.put(s, e);
	}

	public DFAState transition(Alphabet s) {
		return transitions.get(s);
	}

	public boolean isFinal() {
		return isFinal;
	}

	public void setFinal() {
		isFinal = true;
	}

	@Override
	public String toString() {
		return state;
	}
}

 
 
class DFA {
	private TreeSet<Alphabet> aplhabet;
	private TreeSet<DFAState> states;
	private TreeSet<DFAState> finalStates;
	private DFAState initialState;

	public DFA() {
		aplhabet = new TreeSet<Alphabet>();
		states = new TreeSet<DFAState>();
		finalStates = new TreeSet<DFAState>();
	}

	public void setInitialState(String state) {
		for (DFAState e : states) {
			if (e.getState().compareTo(state) == 0)
				initialState = e;
		}
	}

	public void addState(String s) {
		DFAState newState = new DFAState(s);
		states.add(newState);
	}

	public void addTransition(String state, String symbol, String state2) {
		DFAState is = null;
		DFAState fs = null;
		Alphabet s = null;
		for (DFAState e : states) {
			// Handle States
			if (e.getState().compareTo(state) == 0)
				is = e;
			if (e.getState().compareTo(state2) == 0)
				fs = e;
		}
		for (Alphabet sim : aplhabet) {
			// Gets the symbol
			if (sim.getSymbol().compareTo(symbol) == 0)
				s = sim;
		}
		// Adds the transition
		if (is != null && fs != null && s != null)
			is.addTransition(s, fs);
	}

	public void setFinalState(String s) {
		for (DFAState e : states) {
			if (e.getState().compareTo(s) == 0) {
				e.setFinal();
				finalStates.add(e);
			}
		}
	}

	public void addSymbol(String s) {
		Alphabet newAlph = new Alphabet(s);
		aplhabet.add(newAlph);
	}

	public TreeSet<DFAState> getStates() {
		return states;
	}

	public TreeSet<DFAState> getFinalStates() {
		return finalStates;
	}

	public DFAState getInitialState() {
		return initialState;
	}

	public TreeSet<Alphabet> getAlphabet() {
		return aplhabet;
	}

}

 
  
class StateObject {

	private String eq;
	private String state;
	public static TreeSet<DFAState> states;
	private boolean isSimplified;

	public StateObject(String equation, String state) {
		this.eq = equation;
		this.state = state;
		check();
	}

	public boolean check() {
		if (eq.contains("L(" + state + ")"))
			isSimplified = false;
		else
			isSimplified = true;
		return isSimplified;
	}

	// Arranges all L's together
	public String simplify(String state, String equation) {
		String[] splitU = splitSubstitutingString(equation);
		int cont = 0;

		for (String s : splitU) {
			if (s.contains("L(" + state + ")"))
				cont++;                                                                              //   if there are more than 1 L(B) - more than 1 state name then simplify
		}                                                                                              // take common for subsequent arden operation of getRE(). 

		if (cont <= 1) {
			isSimplified = true;
		} else {
			String temp = "";
			String A = "";
			String B = "";

			for (String s : splitU) {
				if (s.contains("L(" + state + ")")) {
					if (A == "")                                                                 
						A = s.replace("L(" + state + ")", "");
					else
						A += "U" + s.replace("L(" + state + ")", "");
				}
			}
			A = "(" + A + ")";
			A += "L(" + state + ")";
			int multipleEpsilons = 0;
			for (String s : splitU) {
				if (s.contains("{E}"))
					multipleEpsilons++;                                                                              //   if there are more than 1 L(B) - more than 1 state name then simplify
			}                                                                                              // take common for subsequent arden operation of getRE(). 

			for (int i = 0; i < splitU.length; i++) {                                   // Take Common.
				if (!splitU[i].contains("L(" + state + ")")) {

					if (splitU[i].contains("{E}")) {
						if (B == ""){
							if(multipleEpsilons > 1 && splitU[i].equals("{E}"))
								B = "λ";
							else
							B = splitU[i].replace("{E}", "");
						}
						else{
							if(multipleEpsilons > 1 && splitU[i].equals("{E}"))
								B += "U" + "λ";
							else
							B += "U" + splitU[i].replace("{E}", "");
						}
					} else {
						if (temp.equals(""))
							temp = temp.concat(splitU[i]);
						else
							temp = temp.concat("U" + splitU[i]);
					}
				}
			}
			if (B != "") {
				B = parentesis(B);
				B = B.replace("U", "+");
				B += "{E}";
				if (temp.contains("U")
						|| (temp.length() != 1 && !temp.equals("") && !temp
								.equals("{E}")))
					temp = "(" + temp + ")";
				temp = Arden.minimizeParenthesis(temp);
				if (temp != "")
					temp = temp + "U" + B;
				else
					temp = B;
			}
			if (temp.equals("")) {
				equation = A;
			} else {
				equation = A + "U" + temp;
			}
			isSimplified = true;
		}
		return equation;
	}

	// Apply the logic of Arden's Lemma
	public void arden(boolean isInitial) {
		String A = "", B = "";
		String A1 = "", B1 = "";

		String[] temp, splitL;
		String[] splitElements = eq.split("\\(|\\)");

		for (int i = 0; i < splitElements.length; i++) {                                        // splits the equation by parenthesis.
			if (splitElements[i].contains("L")) {                                            // check if there is an L
				if (splitElements[i].compareTo("L") == 0                                      // check if there is a required name next to it l(B).
						&& splitElements[i + 1].compareTo(state) == 0) {
					int count = 0;
					int index = eq.indexOf("L(" + state);
					index = index - 1;

					for (int in = index; in >= 0; in--) {
						if (eq.charAt(in) == ')')
							count++;
						if (eq.charAt(in) == '(') {
							count--;
						}
						if (eq.charAt(in) == 'U') {
							if (count == 0) {

								break;
							}
						}
						A = eq.charAt(in) + A;

					}
					break;
				}
				if (splitElements[i + 1].compareTo(state) == 0) {

					temp = splitElements[i].split("U");
					A = temp[temp.length - 1].replace("L", "");
					int id = eq.indexOf(A + "L(" + state + ")") - 1;
					if (id >= 0 && eq.charAt(id) == ')') {
						int count = 0;
						while (id >= 0) {
							if (eq.charAt(id) == ')') {
								count++;
							}
							if (eq.charAt(id) == '(') {
								count--;
							}
							if (eq.charAt(id) == 'U') {
								if (count == 0) {
									break;
								}
							}
							A = eq.charAt(id) + A;
							id--;
						}
					}
					break;
				}
			}
		}

		B = eq.replace(A + "L(" + state + ")", "");
		if (B.contains("L(" + state + ")")) {
			B = eq.replace(parentesis(A) + "L(" + state + ")", "");

		}
		B = B.replace("()", "");

		B = B.replace("UU", "U");
		if (splitStr(A).length != 1) {
			A = "(" + A + ")";
		}
		A += "*";
		A = A.replace("U", "+");
		// splitL = B.split("U");
		B = simplifyBeforeReplace("", B);

		if ((B.split("\\{E\\}")).length > 1) {
			String[] splits = splitSubstitutingString(B);
			for (String s : splits) {
				if (s.contains("{E}")) {
					s = s.replace("{E}", "");
					if (s.length() == 0)
						s = "λ";

					if (A1 != "")

						A1 += "U" + s;
					else
						A1 = s;
				} else {
					if (B1 != "")
						B1 += "U" + s;
					else
						B1 = s;

				}
			}
			if (B1 != "")
				B = B1 + "U" + "(" + A1 + "){E}";
			else
				B = "(" + A1 + ")" + "{E}";
		}
		splitL = splitSubstitutingString(B);
		B = "";
		for (String s : splitL) {
			if (!s.equals(")") && !s.equals("(")) {
				if (!s.equals("")) {
					if (!B.equals("")) {
						if (s.equals("{E}")) {
							if (!isInitial)
								B += "U" + A + s;
							else
								B += "U" + s;
						} else {
							if (!isInitial)
								B += "U" + A + s;
							else
								B += "U" + s;
						}

					} else {
						if (!isInitial)
							B = A + s;
						else
							B = s;
					}
				}
			}
		}

		if (!isInitial) {
			if (B != "") {
				B = "(" + B + ")";
				eq = B;
			} else
				eq = A;
		} else {
			if (!B.equals("({E})")) {
				if (B.contains("U")) {
					if (B.charAt(0) != '(' && B.charAt(B.length() - 1) != ')')
						B = "(" + B + ")";
				} else {
					if (B.length() >= 2)
						if (B.charAt(0) == '('
								&& B.charAt(B.length() - 1) == ')')
							B = B.substring(1, B.length() - 1);
				}
				eq = A + B;
				eq = eq.replace("U", "+");
			}
			else
				eq = A;
		}

	}

	public void replace(String state, String equation) {
		equation = simplifyBeforeReplace(state, equation);
		equation = equation.replace("UU", "U");
		if (equation != "" && splitStr(equation).length != 1)
			equation = "(" + equation + ")";
		if (eq.contains("L(" + state + ")")) {
			eq = simplifyBeforeReplace(state, eq);
			equation = equation.replace("UU", "U");

			eq = eq.replace("L(" + state + ")", equation);
			eq = eq.replace("UU", "U");
			simplify(equation);
		}
	}

	private String simplifyBeforeReplace(String state, String equation) {
		for (DFAState s : StateObject.states) {
			Matcher m = Pattern.compile("L\\(" + s.getState() + "\\)").matcher(
					equation);
			int count;
			for (count = 0; m.find(); count++)
				;
			if (count > 1) {
				equation = simplify(s.getState(), equation);

			}
		}

		equation = equation.replace("UU", "U");
		return equation;
	}

	private void simplify(String equation) {
		String strToAppend = "";
		if (equation != "" && equation.charAt(0) == '('
				&& equation.charAt(equation.length() - 1) == ')') {
			int index = eq.indexOf(equation) - 1;
			int startIndex = 0;
			int count = 0;
			boolean foundBracket = false;
			for (int i = index; i >= 0; i--) {
				if (eq.charAt(i) == 'U') {
					if (count == 0) {
						startIndex = i + 1;
						break;
					}

				}
				if (eq.charAt(i) == '(') {
					/*
					 * startIndex = i+1; foundBracket = true; break;
					 */
					count--;
				}
				if (eq.charAt(i) == ')') {
					count++;
				}

				strToAppend = Character.toString(eq.charAt(i)) + strToAppend;

			}

			String strEqn = equation.substring(1, equation.length() - 1);
			String[] elementsOfEqn = splitSubstitutingString(strEqn);
			strEqn = "";

			for (String s : elementsOfEqn) {
				if (strEqn != "")
					strEqn += "U";
				strEqn += strToAppend + s;
			}

			eq = eq.replace(
					eq.substring(startIndex,
							eq.indexOf(equation) + equation.length()), strEqn);
			if (foundBracket) {
				strEqn = "(" + strEqn + ")";
				simplify(strEqn);
			}
			if (eq.indexOf(equation) > 0)
				simplify(equation);
		}
	}

	// Strip the excess parentheses
	private String parentesis(String s) {
		boolean temParentesis = false;
		if (!s.equals(""))
			temParentesis = s.charAt(0) == '('
					&& s.charAt(s.length() - 1) == ')';
		if (!temParentesis) {
			if (s.length() > 1 && !s.contains("L(")) {
				s = "(" + s + ")";
			}
		} else {
			s = s.substring(1, s.length() - 1);
		}
		return s;
	}

	public static String[] splitSubstitutingString(String substitutingStr) {
		int count = 0;
		String s = "";
		for (char c : substitutingStr.toCharArray()) {
			if (c == '(') {
				count++;

			}
			if (c == ')') {
				count--;
			}
			if (c == 'U') {
				if (count == 0) {
					c = 'T';
				}

			}
			s += Character.toString(c);

		}
		String str[] = s.split("T");
		return str;
	}

	public String getEquation() {
		return eq;
	}

	public void setEquation(String l) {
		eq = l;
	}

	public boolean isSimplified() {
		return isSimplified;
	}

	public String getState() {
		return state;
	}

	@Override
	public String toString() {
		return eq;
	}

	private static boolean areParenthesesBalanced(String string) {
		int count = 0;
		for (int i = 0; i < string.length(); i++) {
			if (string.charAt(i) == '(')
				count++;
			else if (string.charAt(i) == ')')
				count--;
			if (count < 0)
				return false;
		}
		return count == 0;
	}

	public static String[] splitStr(String expression) {
		if (areParenthesesBalanced(expression)) {
			ArrayList se = new ArrayList(); // Subexpressions.
			int start = 0;
			int level = 0;
			for (int i = 0; i < expression.length(); i++) {
				char c = expression.charAt(i);
				if (c == ')') {
					level--;
					continue;
				}
				if (c == '(')
					level++;
				if (!(c == '(' && level == 1) && level != 0)
					continue;
				if (c == '+') {
					throw new IllegalArgumentException("+ Error !");
				}
				if (c == '*')
					continue;
				// Not an operator, and on the first level!
				if (i == 0)
					continue;
				se.add(expression.substring(start, i));
				start = i;
			}
			se.add(expression.substring(start));
			return (String[]) se.toArray(new String[0]);
		}
		return expression.split("$");
	}
}
  

class Alphabet implements Comparable<Alphabet> {

	private String symbol;

	public Alphabet(String simbolo) {
		this.symbol = simbolo;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String simbolo) {
		this.symbol = simbolo;
	}

	@Override
	public int compareTo(Alphabet o) {
		return symbol.compareTo(o.getSymbol());
	}

	@Override
	public String toString() {
		return symbol;
	}
}
