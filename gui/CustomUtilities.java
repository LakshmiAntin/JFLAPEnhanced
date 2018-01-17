package gui;

import gui.action.ConvertFSAToREActionNew;
import gui.action.MultipleSimulateAction;
import gui.action.OpenAction;
import gui.action.Xeger;
import gui.environment.AutomatonEnvironment;
import gui.environment.Environment;
import gui.pumping.ComputerFirstPane;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.mozilla.javascript.tools.shell.JSConsole;

import automata.Automaton;
import automata.AutomatonSimulator;
import automata.Configuration;
import automata.SimulatorFactory;
import automata.State;
import automata.Transition;
import automata.fsa.FSATransition;
import automata.fsa.FiniteStateAutomaton;

public class CustomUtilities {
	
	public static boolean skipStepsForREToDFA;

	public static int checkInputString(Environment environment,String input){
		Configuration[] configs = null;
		FiniteStateAutomaton userAutomaton = (FiniteStateAutomaton) environment
				.getObject();
    	AutomatonSimulator as = SimulatorFactory
				.getSimulator(userAutomaton);
    	configs = as
				.getInitialConfigurations(input);
    	List associated = new ArrayList();
    	try{
    		MultipleSimulateAction ms = new MultipleSimulateAction(userAutomaton,environment);
    	int result = ms.handleInput(userAutomaton,  as,
				configs, input, associated);
    	
    	return result;
    	}
    	catch(Exception ex){
    	}
    	return 1;
	}
	
	
	public static List<String> getXegerList(String computedRE){
		List<String> stringsAdded = new ArrayList<String>();
		if(computedRE == null || computedRE == "")
			return stringsAdded;
		computedRE = computedRE.replace("λ", "0{0}");
		computedRE = computedRE.replace('+', '|');
		Xeger generator = new Xeger(computedRE);
		List<String> singleLen = new ArrayList<String>();
		List<String> doubleLen = new ArrayList<String>();
		List<String> tripleLen = new ArrayList<String>();
		List<String> fourLen = new ArrayList<String>();
		List<String> fiveLen = new ArrayList<String>();
		List<String> SixLen = new ArrayList<String>();
		List<String> remainingString = new ArrayList<String>();
	


		for(int i=0 ; i<2000 ; i++){
			if(stringsAdded.size() < 15){
		String result = generator.generate();
		if(result.length() == 1 && !stringsAdded.contains(result)){
			singleLen.add(result);
			stringsAdded.add(result);
		}
		else if(result.length() == 2 && !stringsAdded.contains(result)){
			doubleLen.add(result);
			stringsAdded.add(result);
		}
		else if(result.length() == 3 && !stringsAdded.contains(result)){
			tripleLen.add(result);
			stringsAdded.add(result);
		}
		else if(result.length() == 4 && !stringsAdded.contains(result)){
			fourLen.add(result);
			stringsAdded.add(result);
		}
		else if(result.length() == 5 && !stringsAdded.contains(result)){
				fiveLen.add(result);
				stringsAdded.add(result);
		}
		else if(result.length() == 6 && !stringsAdded.contains(result)){
					SixLen.add(result);
					stringsAdded.add(result);
		}
				else{
					if(result.length() > 6 && (remainingString.size()<5) && !stringsAdded.contains(result)){
						remainingString.add(result);
			
					stringsAdded.add(result);	
					}
				}
			}
		}
		Collections.sort(stringsAdded ,new t());
		
/*		
		 String endResult = "";
			
			for(String s: stringsAdded)
				endResult += s + "\n";
			
			JOptionPane.showMessageDialog(null, new JScrollPane(new JTextArea(endResult)));*/
			return stringsAdded;

	}
	
	public static class t implements Comparator<String> {
		public int compare(String a, String b) {
			return a.length() - b.length();

		}
	}
	
	public static ArrayList<String> createDFAStr(FiniteStateAutomaton fsa){
		   ArrayList<String> DFAtext = new ArrayList<String>();	       // fsmStr+= "#alphabet\n";
	        List<String> alphs = new ArrayList<String>();
	        String alph = "";
	        for(Transition t: fsa.getTransitions()){
	        	String label = ((FSATransition)t).getLabel();
	        	if(!alphs.contains(label)){
	        		alphs.add(label);
	        		alph += label + " " ;
	        	}
	        }	        
	        DFAtext.add(alph.trim());
	        Map<String,String> m = new HashMap<String,String>();
	        String states = "";
	        for(State s: fsa.getStates()){
	        	int index = Integer.parseInt(Character.toString(s.getName().charAt(1)));
	        	m.put(s.getName(), Character.toString((char)('A' + index)));
	        	states += m.get(s.getName()) + " " ;
	        }
	        DFAtext.add(states.trim());

	        String finalStates = "";
	        for(State s: fsa.getFinalStates()){
	        	finalStates += m.get(s.getName()) + " " ;
	        }
	        DFAtext.add(finalStates.trim());

	        DFAtext.add(m.get(fsa.getInitialState().getName()));

	      
	    
	        for(Transition t: fsa.getTransitions()){
	        	FSATransition ft = ((FSATransition)t);
	        	 DFAtext.add(m.get(ft.getFromState().getName()) + " " + ft.getLabel() + " " + m.get(ft.getToState().getName())) ;
    }
	        
	        
		return DFAtext;
	}
	
	public static boolean checkIfJFLAPAutomaton(Automaton automaton){
		int noOfFinalStates = automaton.getFinalStates().length;
		if(noOfFinalStates > 1)
			return false;
		State initial = automaton.getInitialState();
		for(State s :automaton.getFinalStates()){
			if(s.getName().equals(initial.getName()))
				return false;
		}
		return true;
	}

}
