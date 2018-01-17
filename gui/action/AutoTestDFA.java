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

import grammar.Grammar;
import grammar.parse.BruteParser;
import grammar.parse.BruteParserEvent;
import grammar.parse.BruteParserListener;
import automata.fsa.FSAStepByStateSimulator;
import automata.mealy.*;
import gui.JTableExtender;
import gui.SplitPaneFactory;
import gui.TableTextSizeSlider;
import gui.editor.ArrowDisplayOnlyTool;
import gui.editor.EditorPane;
import gui.environment.AutomatonEnvironment;
import gui.environment.Environment;
import gui.environment.EnvironmentFactory;
import gui.environment.EnvironmentFrame;
import gui.environment.FrameFactory;
import gui.environment.GrammarEnvironment;
import gui.environment.Profile;
import gui.environment.Universe;
import gui.environment.EnvironmentFactory.EditorPermanentTag;
import gui.environment.tag.CriticalTag;
import gui.environment.tag.Tag;
import gui.grammar.GrammarInputPane;
import gui.grammar.parse.BruteParsePane;
import gui.grammar.parse.UnrestrictedTreePanel;
import gui.sim.TraceWindow;
import gui.sim.multiple.InputTableModel;
import gui.viewer.AutomatonPane;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Arrays;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.tree.TreeNode;


import automata.Automaton;
import automata.AutomatonSimulator;
import automata.Configuration;
import automata.NondeterminismDetector;
import automata.NondeterminismDetectorFactory;
import automata.SimulatorFactory;
import automata.State;
import automata.turing.TMSimulator;
import automata.turing.TuringMachine;
import automata.fsa.FSAAlphabetRetriever;

//import javax.swing.table.TableColumn;
import Solution.SolutionBase;

/**
 * This is the action used for the simulation of multiple inputs on an automaton
 * with no interaction. This method can operate on any automaton.
 * 
 * @author Thomas Finley
 * @modified by Kyung Min (Jason) Lee
 */

public class AutoTestDFA extends MultipleSimulateAction {
	/**
	 * Instantiates a new <CODE>AutoTestDFA</CODE>.
	 * 
	 * @param automaton
	 *            the automaton that input will be simulated on
	 * @param environment
	 *            the environment object that we shall add our simulator pane to
	 */
	public AutoTestDFA(Automaton automaton, Environment environment) {
		super(automaton, environment);
		putValue(NAME, "Auto Test DFA...");
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_M,
                MAIN_MENU_MASK));
	}

	public AutoTestDFA(Grammar gram, Environment environment) {
		super(gram, environment);
		putValue(NAME, "Auto Test DFA...");
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_M,
                MAIN_MENU_MASK));
	}
	
	/**
	 * Returns the title for the type of compontent we will add to the
	 * environment.
	 * 
	 * @return in this base class, returns "Multiple Inputs"
	 */
	public String getComponentTitle() {
		return "Auto Test DFA...";
	}

	/**
	 * This will search configurations for an accepting configuration.
	 * 
	 * @param automaton
	 *            the automaton input is simulated on
	 * @param simulator
	 *            the automaton simulator for this automaton
	 * @param configs
	 *            the initial configurations generated from a single input
	 * @param initialInput
	 *            the object that represents the initial input; this is a String
	 *            object in most cases, but for Turing Machines is an array of
	 *            String objects
	 * @param associatedConfigurations
	 *            the first accepting configuration encountered will be added to
	 *            this list, or the last configuration considered if there was
	 *            no accepted configuration
	 * @return <CODE>0</CODE> if this was an accept, <CODE>1</CODE> if
	 *         reject, and <CODE>2</CODE> if the user cancelled the run
	 */
	public int handleInput(Automaton automaton,
			AutomatonSimulator simulator, Configuration[] configs,
			Object initialInput, List associatedConfigurations) {
		JFrame frame = Universe.frameForEnvironment(getEnvironment());
		// How many configurations have we had?
		int numberGenerated = 0;
		// When should the next warning be?
		int warningGenerated = WARNING_STEP;
		Configuration lastConsidered = configs[configs.length - 1];
		while (configs.length > 0) {
			numberGenerated += configs.length;
			// Make sure we should continue.
			if (numberGenerated >= warningGenerated) {
				if (!confirmContinue(numberGenerated, frame)) {
					associatedConfigurations.add(lastConsidered);
					return 2;
				}
				while (numberGenerated >= warningGenerated)
					warningGenerated *= 2;
			}

			// Get the next batch of configurations.
			ArrayList next = new ArrayList();
			for (int i = 0; i < configs.length; i++) {
				lastConsidered = configs[i];
				if (configs[i].isAccept()) {
					associatedConfigurations.add(configs[i]);
					
					return 0;
				} else {
					next.addAll(simulator.stepConfiguration(configs[i]));
				}
			}
			configs = (Configuration[]) next.toArray(new Configuration[0]);
		}
		associatedConfigurations.add(lastConsidered);
		return 1;
	}

	/**
	 * Provides an initialized multiple input table object.
	 * 
	 * @param obj
	 *            the automaton to provide the multiple input table for
	 * @return a table object for this automaton
	 * @see gui.sim.multiple.InputTableModel
	 */
	protected JTableExtender initializeTable(Object obj) {
		boolean multiple = false;
		int inputCount = 0;
        if(this.getEnvironment().myObjects!=null){
        	multiple = true;
        	inputCount = 1;
        }
        //System.out.println("In initialize:" + multiple);
        InputTableModel model = null;
        if(getObject() instanceof Automaton){
        	model = InputTableModel.getModel((Automaton)getObject(), multiple);
        }
        else if(getObject() instanceof Grammar) model = InputTableModel.getModel((Grammar)getObject(), multiple);
		JTableExtender table = new JTableExtender(model, this);
		// In this regular multiple simulate pane, we don't care about
		// the outputs, so get rid of them.
		TableColumnModel tcmodel = table.getColumnModel();
		
		inputCount += model.getInputCount();
		for (int i = model.getInputCount(); i > 0; i--) {
			tcmodel.removeColumn(tcmodel.getColumn(inputCount));
		}
		if(multiple){
            ArrayList autos  = this.getEnvironment().myObjects;
            ArrayList strings = this.getEnvironment().myTestStrings;
            int offset = strings.size();
            int row = 0;
            for(int m = 0; m < autos.size(); m++){      
                for(int k = 0; k < strings.size(); k++){
                    row = k+offset*m;
                    Object currentObj = autos.get(m);
                    if(currentObj instanceof Automaton){
                    	model.setValueAt(((Automaton)currentObj).getFileName(), row, 0); 
                    	 model.setValueAt((String)strings.get(k), row, 1);                    	
                    }
                    else if(currentObj instanceof Grammar){
                    	model.setValueAt(((Grammar)currentObj).getFileName(), row, 0); 
                    	 model.setValueAt((String)strings.get(k), row, 1);
                    }                  
                }
                
            }
            while((model.getRowCount()-1) > (autos.size()*strings.size())){
            	model.deleteRow(model.getRowCount()-2);
            }
		}
		// Set up the last graphical parameters.
		table.setShowGrid(true);
		table.setGridColor(Color.lightGray);
		return table;
	}
	
	public void performAction(Component source){
		if(getObject() instanceof Automaton){
			if (((Automaton)getObject()).getInitialState() == null) {
				JOptionPane.showMessageDialog(source,
						"Simulation requires an automaton\n"
								+ "with an initial state!", "No Initial State",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
        /*
         * If it is a Mealy or Moore machine, it cannot have nondeterminism.
         */
        if(getObject() instanceof MealyMachine) {
            Automaton a = (Automaton)getObject();
            NondeterminismDetector d = NondeterminismDetectorFactory.getDetector(a);
            State[] nd = d.getNondeterministicStates(a);
            if(nd.length > 0){
                JOptionPane.showMessageDialog(source, 
                    "Please remove nondeterminism for simulation.\n" +
                    "Select menu item Test : Highlight Nondeterminism\n" +
                    "to see nondeterministic states.",
                    "Nondeterministic states detected", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        table = initializeTable(getObject());
        
		if(((InputTableModel)table.getModel()).isMultiple){
			getEnvironment().remove(getEnvironment().getActive());
		}
		
		JPanel panel = new JPanel(new BorderLayout());
		JToolBar bar = new JToolBar();
		panel.add(new JScrollPane(table), BorderLayout.CENTER);
		panel.add(bar, BorderLayout.SOUTH);
		panel.add(new TableTextSizeSlider(table), BorderLayout.NORTH);
		TextEditor te = new TextEditor();
		te.buildEditor();
	
		//Load inputs
		bar.add(new AbstractAction("Load Inputs"){
			private String gen(int n, int k, String [] alphabet) {
			  if(k == 0) return "";
			  int m = alphabet.length;
			  return gen(n/m, k-1, alphabet) + alphabet[n % m];
			}

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					InputTableModel model = (InputTableModel) table.getModel();

					/*Get max string length of the input to be generated */
					JFrame frame = new JFrame("A Dialog to Get Integer Input from the User");
        			String name = JOptionPane.showInputDialog(frame, "Input String Length");
        			int length = 0;
          			if (name != null) {
          				length = Integer.parseInt(name);
          			}
                    String [] alphabet = (new FSAAlphabetRetriever()).getAlphabet((Automaton) getObject());
                    Arrays.sort(alphabet);
                    int numLetters = alphabet.length;
                    int i = 0;
                    for(int k = 0; k <= length; k++) {
                      int n = 0;
                      for(int m = (int) Math.pow(numLetters, k); m > 0; m--) {
                        model.setValueAt(gen(n, k, alphabet), i, 0);
                        i++;
                        n++;
                      }
                    }
                    // Make sure any recent changes are registered.
                    table.getCellEditor().stopCellEditing();
				} catch (NullPointerException exception) {
					// We weren't editing anything, so we're OK.
				}
			}
			
		});
		// Add the running input thing.
		bar.add(new AbstractAction("Run Inputs") {
			public void actionPerformed(ActionEvent e) {
				try {
					// Make sure any recent changes are registered.
					table.getCellEditor().stopCellEditing();
				} catch (NullPointerException exception) {
					// We weren't editing anything, so we're OK.
				}
				InputTableModel model = (InputTableModel) table.getModel();
				
				if(getObject() instanceof Automaton){
	                Automaton currentAuto = (Automaton)getObject();
					AutomatonSimulator simulator = SimulatorFactory
							.getSimulator(currentAuto);
					String[][] inputs = model.getInputs();
	                int uniqueInputs = inputs.length;
	                int tapes = 1;
	                if(model.isMultiple){
	                	if (currentAuto instanceof TuringMachine) {
							 tapes = ((TuringMachine)currentAuto).tapes;
	                	}
	                    uniqueInputs = getEnvironment().myTestStrings.size()/tapes;
	                }
                    Runtime thisRuntime = Runtime.getRuntime();
					for (int r = 0; r < inputs.length; r++) {
	                    if(r>0){
	                        if(r%uniqueInputs==0){
	                            currentAuto = (Automaton)getEnvironment().myObjects.get(r/uniqueInputs);
	                           
	                            simulator = SimulatorFactory.getSimulator(currentAuto);                         
	                        }
	                    }
						Configuration[] configs = null;
						Object input = null;
						// Is this a Turing machine?
						if (currentAuto instanceof TuringMachine) {
							 
							configs = ((TMSimulator) simulator)
									.getInitialConfigurations(inputs[r]);
							input = inputs[r];
						} else { // If it's not a Turing machine.
							configs = simulator
									.getInitialConfigurations(inputs[r][0]);
							input = inputs[r][0];
						}
                        
                        String mesg;
                        int solution_result = 2;
						try {
                            solution_result =
                              thisRuntime.exec("java Solution/SolutionBase " +
                              (String)input).waitFor();
                        }
    					catch(Exception e1) {
      						System.out.println(e1);
    					}

						List associated = new ArrayList();
                        if(solution_result == 2) {
                            mesg = mesgException;
                        }
                        else {
                            int dfa_result = handleInput(currentAuto, simulator,
								configs, input, associated);
                            mesg = (solution_result == dfa_result ? mesgMatch : mesgMismatch);
                        }

						Configuration c = null;
						if (associated.size() != 0)
							c = (Configuration) associated.get(0);
                        
                        /*
                         * If it's a Moore or Mealy machine, the output should be
                         * the string not accept/reject.
                         */
                        //MERLIN MERLIN MERLIN MERLIN MERLIN//
                        if(getObject() instanceof MealyMachine)
                        {
                            MealyConfiguration con = (MealyConfiguration) c;
                            model.setResult(r, con.getOutput(), con, 
                                getEnvironment().myTransducerStrings, (r%(uniqueInputs))*(tapes+1));
                        }
						//currentCompare.add()
                        else {
                            model.setResult(r, mesg, c, getEnvironment().myTransducerStrings, (r%(uniqueInputs))*(tapes+1));
                        }
					}
				}
				else if(getObject() instanceof Grammar){
					String[][] inputs = model.getInputs();
					int uniqueInputs = inputs.length;
					Grammar currentGram = (Grammar)getObject();
					BruteParsePane parsePane = new BruteParsePane((GrammarEnvironment)getEnvironment(), currentGram, model);
					parsePane.inputField.setEditable(false);    
                    parsePane.row = -1;
					parsePane.parseMultiple();
				}
			}
			
		});
		if(!((InputTableModel)table.getModel()).isMultiple){
		// Add the clear button.
		bar.add(new AbstractAction("Clear") {
			public void actionPerformed(ActionEvent e) {
				try {
					// Make sure any recent changes are registered.
					table.getCellEditor().stopCellEditing();
				} catch (NullPointerException exception) {
					// We weren't editing anything, so we're OK.
				}
				InputTableModel model = (InputTableModel) table.getModel();              
				model.clear();
			}
		});
		
        /*
         * So that it will show up as Lambday or Epsilon, depending on the
         * profile. Sorry about the cheap hack. 
         * 
         * Jinghui Lim
         */
        String empty = "Lambda";
        if(Universe.curProfile.getEmptyString().equals(Profile.LAMBDA))
            empty = "Lambda";
        else if(Universe.curProfile.getEmptyString().equals(Profile.EPSILON))
            empty = "Epsilon";
		}
		if(((InputTableModel)table.getModel()).isMultiple){
		    
		    bar.add(new AbstractAction("Edit File"){
		        public void actionPerformed(ActionEvent arg0) {		            
		            int k = getMachineIndexBySelectedRow(table);
		            if(k>=0 && k < getEnvironment().myObjects.size()){
		                if(getObject() instanceof Automaton){
		                    Automaton cur = (Automaton)getEnvironment().myObjects.get(k);
		                    EditorPane ep = new EditorPane(cur);
		                    ep.setName(cur.getFileName());
		                    getEnvironment().add(ep, "Edit", new CriticalTag() {
		                    });
		                    getEnvironment().setActive(ep);          
		                }
		                
		                else if(getObject() instanceof Grammar){
		                    Grammar cur = (Grammar)getEnvironment().myObjects.get(k);
		                    GrammarInputPane ep = new GrammarInputPane(cur);      
		                    ep.setName(cur.getFileName());
		                    getEnvironment().add(ep, "Edit", new CriticalTag() {
		                    });
		                    getEnvironment().setActive(ep);
		                }
                    }	            
		        }
		    }); 
		    
        	bar.add(new AbstractAction("Add input string"){
        		public void actionPerformed(ActionEvent arg0) {
                    //add input
                    int inputsNeeded = 1;
                    boolean turing = false;
        			if(getEnvironment().myObjects.get(0) instanceof TuringMachine){      			 
                        turing = true;
                    }
                        Object input = initialInput((Component) getEnvironment().getActive(), "Input");
    
            			if(input instanceof String){
            				String s = (String)input;
            				((ArrayList)getEnvironment().myTestStrings).add(s);
            			}
            			else if(input instanceof String[]){
            				String[] s = (String[]) input;
                            for(int k = 0; k < s.length; k++){
                                ((ArrayList)getEnvironment().myTestStrings).add(s[k]);
                            }
            			}
                        else return;
                    
                    //add expected output
                    if(turing){
                            Object output = initialInput((Component) getEnvironment().getActive(), "Expected Output?");
        
    
                            if(output instanceof String){
                                String s = (String)output;
                                ((ArrayList)getEnvironment().myTransducerStrings).add(s);
                            }
                            else if(output instanceof String[]){
                                String[] s = (String[]) output;
                                for(int k = 0; k < s.length; k++){
                                    ((ArrayList)getEnvironment().myTransducerStrings).add(s[k]);
                                }
                            }
                            else{
                                getEnvironment().myTestStrings.remove(getEnvironment().myTestStrings.size()-1);
                                return;
                            }
                        
                    }
                    //add expected result
                    Object result = initialInput((Component) getEnvironment().getActive(), "Expected Result? (Accept or Reject)");

                    if(result instanceof String){
                        String s = (String)result;
                        ((ArrayList)getEnvironment().myTransducerStrings).add(s);
                    }
                    else if(result instanceof String[]){
                        String[] s = (String[]) result;
                        ((ArrayList)getEnvironment().myTransducerStrings).add(s[0]);
                    }
                    else {
                        getEnvironment().myTestStrings.remove(getEnvironment().myTestStrings.size()-1);
                        getEnvironment().myTransducerStrings.remove(getEnvironment().myTestStrings.size()-1);
                        return;
                    }
                    
        			getEnvironment().remove(getEnvironment().getActive());
                    performAction(getEnvironment().getActive());
                    
                }
            	});
        	
        	bar.add(new AbstractAction("Add file"){
        		public void actionPerformed(ActionEvent arg0) {
        			TestAction test = new TestAction();
        			test.chooseFile(getEnvironment().getActive(), false);
        			getEnvironment().remove(getEnvironment().getActive());
                    performAction(getEnvironment().getActive());
                }
            });
        	
        	bar.add(new AbstractAction("Remove file"){
        		public void actionPerformed(ActionEvent arg0) {
        			int k = getMachineIndexBySelectedRow(table);
                    if(k>=0 && k < getEnvironment().myObjects.size()){
            			getEnvironment().myObjects.remove(k);
            			int row = table.getSelectedRow();
       			
            			int objSize = getEnvironment().myObjects.size();
            			int stringSize = getEnvironment().myTestStrings.size();
            			
            			int beginOffset = row%stringSize;
            			int begin = (row-beginOffset);
            			
            			for(int i = 0; i < (stringSize); i++){
            				((InputTableModel)table.getModel()).deleteRow(begin);      				
            		 }  
            			table.changeSelection(0,0, false, false);
                    }
                }
            	});
        	
        	bar.add(new AbstractAction("Save Results"){
        	    public void actionPerformed(ActionEvent arg0) {
        	        final JFrame frame = new JFrame("Save Location");
        	        
        	        
        	        final JRadioButton defaultLocation = new JRadioButton("Save Results with Original File");
        	        defaultLocation.setMnemonic(KeyEvent.VK_B);
        	        defaultLocation.setActionCommand("Save Results with Original File");
        	        defaultLocation.addActionListener(new ActionListener() {
        	            public void actionPerformed(ActionEvent event){
        	            }
        	        });
        	        final JRadioButton specifyLocation = new JRadioButton("Specify New Location");
        	        specifyLocation.addActionListener(new ActionListener() {
        	            public void actionPerformed(ActionEvent event){
        	            }
        	        });
        	        specifyLocation.setMnemonic(KeyEvent.VK_C);
        	        specifyLocation.setActionCommand("Specify New Location");
        	        defaultLocation.setSelected(true);
        	        ButtonGroup group = new ButtonGroup();
        	        group.add(defaultLocation);
        	        group.add(specifyLocation);
        	        
        	        
        	        JPanel panel = new JPanel();
        	        panel.add(defaultLocation);
        	        panel.add(specifyLocation);
        	        frame.getContentPane().add(panel, BorderLayout.CENTER);
        	        
        	        JButton accept = new JButton("Accept");
        	        accept.addActionListener(new ActionListener() {
        	            public void actionPerformed(ActionEvent event) {
        	                frame.setVisible(false);
                            String filepath = "";
                            boolean failedSave = false;
        	                if(specifyLocation.isSelected()){
        	                    //                                  The save as loop.
        	                    File file = null;
        	                    boolean badname = false;
        	                    while (badname || file ==null) {
        	                        if (!badname) {
                                        Universe.CHOOSER.setFileFilter(null);
                                        Universe.CHOOSER.setDialogTitle("Choose directory to save files in");
                                        Universe.CHOOSER.setFileSelectionMode(Universe.CHOOSER.DIRECTORIES_ONLY);
        	                            int result = Universe.CHOOSER.showSaveDialog(frame);
        	                            if (result != JFileChooser.APPROVE_OPTION)
        	                                break;
        	                            file = Universe.CHOOSER.getSelectedFile();
        	                            
        	                            try {
        	                                // Get the suggested file name.
        	                                filepath = file.getCanonicalPath();
                                            int last = filepath.lastIndexOf("\\");
                                            if(last == -1) filepath = filepath+"/";
                                            else filepath = filepath+"\\";
        	                                
        	                            }
        	                            catch (IOException e) {
        	                                // TODO Auto-generated catch block
        	                                e.printStackTrace();
        	                            }
        	                        }
        	                    }
        	                }
                            if(filepath.equals("")) failedSave = true;
        	                InputTableModel model = (InputTableModel)table.getModel();
        	                String oldfileName = (String)model.getValueAt(0, 0);
        	                String fileName = (String)model.getValueAt(0, 0);
        	                boolean turing = false;
        	                Object machine = getEnvironment().myObjects.get(0);
        	                String base = filepath;
        	                if(machine instanceof Automaton){
        	                    if(machine instanceof TuringMachine){
        	                        turing = true;
        	                    }
        	                    if(failedSave) base = ((Automaton)machine).getFilePath();
        	                }
        	                else if(machine instanceof Grammar){
        	                    if(failedSave) base = ((Grammar)machine).getFilePath();
        	                }
        	                
        	                
        	                try{
            	                FileWriter writer = new FileWriter(base+"results"+fileName+".txt");   
            	                BufferedWriter bwriter = new BufferedWriter(writer);
            	                PrintWriter out = new PrintWriter(bwriter);;
            	                for(int r = 0; r<model.getRowCount(); r++){
            	                    fileName = (String)model.getValueAt(r, 0);                      
            	                    if(!fileName.equals(oldfileName)){
            	                        oldfileName = fileName;  
            	                        out.flush();
            	                        out.close();
            	                        if(fileName.equals("")) break;
            	                        int index = getMachineIndexByName(fileName);
            	                        machine = getEnvironment().myObjects.get(index);
            	                        if(machine instanceof Automaton){                                 
            	                            if(!specifyLocation.isSelected() || failedSave) base = ((Automaton)machine).getFilePath();
            	                        }
            	                        else if(machine instanceof Grammar){
                                            if(!specifyLocation.isSelected() || failedSave) base = ((Grammar)machine).getFilePath();
            	                        }
            	                        bwriter = new BufferedWriter(new FileWriter(base+"results"+fileName+".txt"));
            	                        out = new PrintWriter(bwriter);
            	                    }
            	                    boolean input = false;
            	                    boolean end = false;
            	                    boolean output = false;
            	                    
            	                    for(int c = 1; c < model.getColumnCount(); c++){   
            	                        if((((String)model.getColumnName(c)).startsWith("Input")) && !input){
            	                            out.write("Input: ");
            	                            input = true;
            	                        }
            	                        if((((String)model.getColumnName(c)).startsWith("Output")) && !output && turing){
            	                            out.write("Output: ");
            	                            output = true;
            	                        }
            	                        /*if((((String)model.getColumnName(c)).startsWith("DFA Output")) && !output && turing){
            	                            out.write("DFA Output: ");
            	                            output = true;
            	                        } */
            	                        if(((String)model.getColumnName(c)).startsWith("Result")){
            	                            end = true;
            	                            out.write("Result: ");
            	                        }
            	                        String value = (String)model.getValueAt(r, c);
            	                        
            	                        out.write(value+" ");
            	                        try {
            	                            if(end){
            	                                bwriter.newLine();
            	                            }
            	                        }
            	                        catch (IOException e) {
            	                            
            	                        }
            	                    }                          
            	                }
            	                out.close();
            	            }
                            catch(IOException e){
                                
                            }
                        }
            
        	        });
        	        
        	        frame.getContentPane().add(accept, BorderLayout.SOUTH);
        	        frame.pack();
        	        Point point = new Point(100, 50);
        	        frame.setLocation(point);
        	        frame.setVisible(true);
        	    }
        	});
        	
        }
        
        myPanel = panel;
		// Set up the final view.
        Object finObject = getObject();
        if(finObject instanceof Automaton){
        	AutomatonPane ap = new AutomatonPane((Automaton)finObject);
        	ap.addMouseListener(new ArrowDisplayOnlyTool(ap, ap.getDrawer()));
        	JSplitPane split = SplitPaneFactory.createSplit(getEnvironment(), true,
				0.5, ap, panel);
        	MultiplePane mp = new MultiplePane(split);
        	getEnvironment().add(mp, getComponentTitle(), new CriticalTag() {
    		});
    		getEnvironment().setActive(mp);
        }
        else if(finObject instanceof Grammar){
        	BruteParsePane bp = new BruteParsePane((GrammarEnvironment)getEnvironment(), (Grammar)finObject, (InputTableModel)table.getModel());
        	bp.inputField.setEditable(false);
            if(getEnvironment().myTestStrings != null && getEnvironment().myTestStrings.size()>0) bp.inputField.setText((String)getEnvironment().myTestStrings.get(0));
        	JSplitPane split = SplitPaneFactory.createSplit(getEnvironment(), true,
    				0.5, bp, panel);
          
        	MultiplePane mp = new MultiplePane(split);
        	getEnvironment().add(mp, getComponentTitle(), new CriticalTag() {
    		});
    		getEnvironment().setActive(mp);
        }
		
	}
	
	private int getMachineIndexBySelectedRow(JTable table){
		InputTableModel model = (InputTableModel) table.getModel();
        int row = table.getSelectedRow();
        if(row < 0) return -1;
        String machineFileName = (String)model.getValueAt(row, 0);
        return getMachineIndexByName(machineFileName);
	}
	
	public int getMachineIndexByName(String machineFileName){
	        ArrayList machines = getEnvironment().myObjects;
	        if(machines == null) return -1;
	        for(int k = 0; k < machines.size(); k++){            
	            Object current = machines.get(k);
	            if(current instanceof Automaton){
	            	Automaton cur = (Automaton)current;
	            	if(cur.getFileName().equals(machineFileName)){
	            		return k;
	                }
	            }
	            else if(current instanceof Grammar){
	            	Grammar cur = (Grammar)current;
	            	if(cur.getFileName().equals(machineFileName)){
	            		return k;
	                }
	            }
	            
	        }
	        return -1;
	}
	
	public void viewAutomaton(JTableExtender table){
		InputTableModel model = (InputTableModel) table.getModel();
		 if(model.isMultiple){        	 			
	         int row = table.getSelectedRow();
	         if(row < 0) return;
	         String machineFileName = (String)model.getValueAt(row, 0);           
	         updateView(machineFileName, (String)model.getValueAt(row, 1), table); 
		 }
       else if(this.getEnvironment().getObject() instanceof Grammar){
       updateView(((Grammar)this.getEnvironment().getObject()).getFileName(), (String)model.getValueAt(table.getSelectedRow(), 1), table);
       }
       else if(this.getEnvironment().getObject() instanceof Automaton){
       updateView(((Automaton)this.getEnvironment().getObject()).getFileName(), (String)model.getValueAt(table.getSelectedRow(), 1), table);
   }
		
	}
	
	/**
	 * Handles the creation of the multiple input pane.
	 * 
	 * @param e
	 *            the action event
	 */
	public void actionPerformed(ActionEvent e) {
		performAction((Component)e.getSource());		
	}

	/**
	 * @param machineFileName
     * 
     */
    protected void updateView(String machineFileName, String input, JTableExtender table) {
        ArrayList machines = this.getEnvironment().myObjects;
        Object current = null;
        if(machines != null) current = machines.get(0);
        else current = this.getEnvironment().getObject();
            if(current instanceof Automaton && ((InputTableModel)table.getModel()).isMultiple){
            	int spot = this.getMachineIndexBySelectedRow(table);
            	Automaton cur = null;
            	if(spot != -1) cur = (Automaton)machines.get(spot);
            	else cur = (Automaton)this.getEnvironment().getObject();
                
                    AutomatonPane newAP = new AutomatonPane(cur);
                    newAP.addMouseListener(new ArrowDisplayOnlyTool(newAP, newAP.getDrawer()));
                    JSplitPane split = SplitPaneFactory.createSplit(getEnvironment(), true,
                            0.5, newAP, myPanel);
                    MultiplePane mp = new MultiplePane(split);
                    
                    EnvironmentFrame frame = Universe.frameForEnvironment(getEnvironment());
                    String newTitle = cur.getFileName();
                    if(newTitle != "") frame.setTitle(newTitle);
                    getEnvironment().remove(getEnvironment().getActive());
     
                    
                    getEnvironment().add(mp, getComponentTitle(), new CriticalTag() {
                    });
                    getEnvironment().setActive(mp);
                
            }
            else if(current instanceof Grammar && (table.getSelectedRow() < (table.getRowCount()-1))){
            	int spot = this.getMachineIndexBySelectedRow(table);
            	Grammar cur = null;
            	if(spot != -1) cur = (Grammar)machines.get(spot);
            	else cur = (Grammar)this.getEnvironment().getObject();
                
	                BruteParsePane bp = new BruteParsePane((GrammarEnvironment)getEnvironment(), cur, null);
	                int column = 1;
	                if(spot == -1) column = 0;
	               bp.inputField.setText((String)table.getModel().getValueAt(table.getSelectedRow(), column));
	               //bp.inputField.setEnabled(false); 
                   bp.inputField.setEditable(false);              
	                JSplitPane split = SplitPaneFactory.createSplit(getEnvironment(), true,
	                        0.5, bp, myPanel);
	                MultiplePane mp = new MultiplePane(split);
	                getEnvironment().add(mp, getComponentTitle(), new CriticalTag() {
	                });
	                
	                EnvironmentFrame frame = Universe.frameForEnvironment(getEnvironment());
	                String newTitle = cur.getFileName();
                    if(newTitle != "") frame.setTitle(newTitle);
	                getEnvironment().remove(getEnvironment().getActive());
	 
	                
	                getEnvironment().add(mp, getComponentTitle(), new CriticalTag() {
	                });
	                getEnvironment().setActive(mp);
	            
            }
    }

    /**
	 * This auxillary class is convenient so that the help system can easily
	 * identify what type of component is active according to its class.
	 */
	public class MultiplePane extends JPanel {
		public MultiplePane(JSplitPane split) {
			super(new BorderLayout());
			add(split, BorderLayout.CENTER);
			mySplit = split;
		}
		public JSplitPane mySplit = null;
	}
	/*int col = 4;


	InputTableModel tm = new InputTableModel((Automaton)getObject(), col); */

	protected JTable table = null;
    protected JPanel myPanel = null;

	private Environment environment = null;

    public static final String mesgMatch = " ";
    public static final String mesgMismatch = "ERROR: mismatch";
    public static final String mesgException = "Code exception";
}
