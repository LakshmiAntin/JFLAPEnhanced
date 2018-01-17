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

import gui.environment.*;
import gui.menu.MenuBarCreator;
import gui.pumping.CFPumpingLemmaChooser;
import gui.pumping.RegPumpingLemmaChooser;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Serializable;
import javax.swing.*;

import automata.mealy.MealyMachine;
import automata.mealy.MooreMachine;
import automata.Automaton;
import automata.State;
import automata.fsa.FSATransition;
import automata.fsa.FiniteStateAutomaton;
import automata.graph.LayoutAlgorithmFactory;
import automata.graph.layout.VertexMover;


/**
 * The <CODE>NewAction</CODE> handles when the user decides to create some new
 * environment, that is, some sort of new automaton, or grammar, or regular
 * expression, or some other such editable object.
 * 
 * @author Thomas Finley
 */

public class NewAction extends RestrictedAction {
	/**
	 * Instantiates a new <CODE>NewAction</CODE>.
	 */
	public NewAction() {
		super("New...", null);
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N,
				MAIN_MENU_MASK));
	}

	/**
	 * Shows the new machine dialog box.
	 * 
	 * @param event
	 *            the action event
	 */
	public void actionPerformed(ActionEvent event) {
		showNew();
	}
	
	/**
	 * Dispose of environment dialog
	 * by Moti Ben-Ari
	 */
    public static void closeNew() {
        DIALOG.dispose();
        DIALOG = null;
    }

	/**
	 * Shows the new environment dialog.
	 */
	public static void showNew() {
		if (DIALOG == null)
			DIALOG = new NewDialog();
		DIALOG.setVisible(true);
		DIALOG.toFront();
	}

	/**
	 * Hides the new environment dialog.
	 */
	public static void hideNew() {
		DIALOG.setVisible(false);
	}

	/**
	 * Called once a type of editable object is choosen. The editable object is
	 * passed in, the dialog is hidden, and the window is created.
	 * 
	 * @param object
	 *            the object that we are to edit
	 */
	public static void createWindow(Serializable object) {
		DIALOG.setVisible(false);
		FrameFactory.createFrame(object);
	}
	
	public static EnvironmentFrame createCustomWindow(Serializable object) {
		EnvironmentFrame ef = FrameFactory.createFrame(object);
		
		return ef;
	}


	/** The dialog box that allows one to create new environments. */
	private static class NewDialog extends JFrame {
		/**
		 * Instantiates a <CODE>NewDialog</CODE> instance.
		 */
		public NewDialog() {
			// super((java.awt.Frame)null, "New Document");
			super("JFLAP 7.0");
			getContentPane().setLayout(new GridLayout(0, 1));
			initMenu();
			initComponents();
			setResizable(false);
			this.pack();
			this.setLocation(50, 50);

			this.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent event) {
					if (Universe.numberOfFrames() > 0) {
						NewDialog.this.setVisible(false);
					} else {
						QuitAction.beginQuit();
					}
				}
			});
		}

		private void initMenu() {
			// Mini menu!
			JMenuBar menuBar = new JMenuBar();
			JMenu menu = new JMenu("File");
			if (Universe.CHOOSER != null) {
				MenuBarCreator.addItem(menu, new OpenAction());
			}
			try {
				SecurityManager sm = System.getSecurityManager();
				if (sm != null)
					sm.checkExit(0);
				MenuBarCreator.addItem(menu, new QuitAction());
			} catch (SecurityException e) {
				// Well, can't exit anyway.
			}
            menuBar.add(menu);
			menu = new JMenu("Help");
			MenuBarCreator.addItem(menu, new NewHelpAction());
			MenuBarCreator.addItem(menu, new AboutAction());
			menuBar.add(menu);
            menu = new JMenu("Batch");
            MenuBarCreator.addItem(menu, new TestAction());
            menuBar.add(menu);
            menu = new JMenu("Preferences");

            JMenu tmPrefMenu = new JMenu("Turing Machine Preferences");
            tmPrefMenu.add(Universe.curProfile.getTuringFinalCheckBox());
            tmPrefMenu.add(Universe.curProfile.getAcceptByFinalStateCheckBox());
            tmPrefMenu.add(Universe.curProfile.getAcceptByHaltingCheckBox());
            tmPrefMenu.add(Universe.curProfile.getAllowStayCheckBox());

            MenuBarCreator.addItem(menu, new EmptyStringCharacterAction());
//            menu.add(Universe.curProfile.getTuringFinalCheckBox());
            menu.add(new SetUndoAmountAction());

            menu.add(tmPrefMenu);

            menuBar.add(menu);
			setJMenuBar(menuBar);
		}

		private void initComponents() {
			JButton button = null;
			// Let's hear it for sloth!

			button = new JButton("Finite Automaton");
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					FiniteStateAutomaton fsa = new FiniteStateAutomaton();
					fsa.setFromNewAction(true);
					createWindow(fsa);
				}
			});
			getContentPane().add(button);
            
            button = new JButton("Mealy Machine");
            button.addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        createWindow(new MealyMachine());
                    }
                });
            getContentPane().add(button);
            button = new JButton("Moore Machine");
            button.addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        createWindow(new MooreMachine());
                    }
                });
            getContentPane().add(button);

			button = new JButton("Pushdown Automaton");
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Object[] possibleValues = {"Multiple Character Input", "Single Character Input"};
					Object selectedValue = JOptionPane.showInputDialog(null,
				            "Type of PDA Input", "Input",
				            JOptionPane.INFORMATION_MESSAGE, null,
				            possibleValues, possibleValues[0]);
					if (selectedValue==possibleValues[0]){
						createWindow(new automata.pda.PushdownAutomaton());
					}else if(selectedValue==possibleValues[1]){
						createWindow(new automata.pda.PushdownAutomaton(true));
					}
				}
			});
			getContentPane().add(button);

			button = new JButton("Turing Machine");
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					createWindow(new automata.turing.TuringMachine(1));
				}
			});
			getContentPane().add(button);

			button = new JButton("Multi-Tape Turing Machine");
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (INTS == null) {
						INTS = new Integer[4];
						for (int i = 0; i < INTS.length; i++)
							INTS[i] = new Integer(i + 2);
					}
					Number n = (Number) JOptionPane.showInputDialog(
							NewDialog.this.getContentPane(), "How many tapes?",
							"Multi-tape Machine", JOptionPane.QUESTION_MESSAGE,
							null, INTS, INTS[0]);
					if (n == null)
						return;
					createWindow(new automata.turing.TuringMachine(n.intValue()));
				}

				private Integer[] INTS = null;
			});
			getContentPane().add(button);

			button = new JButton("Grammar");
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					createWindow(new grammar.cfg.ContextFreeGrammar());
				}
			});
			getContentPane().add(button);

			button = new JButton("L-System");
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					createWindow(new grammar.lsystem.LSystem());
				}
			});
			getContentPane().add(button);

			button = new JButton("Regular Expression");
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					createWindow(new regular.RegularExpression());
				}
			});
			getContentPane().add(button);
            
            button = new JButton("Regular Pumping Lemma");
            button.addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        createWindow(new RegPumpingLemmaChooser());
                    }
                });
            getContentPane().add(button);
            
            button = new JButton("Context-Free Pumping Lemma");
            button.addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        createWindow(new CFPumpingLemmaChooser());
                    }
                });
            getContentPane().add(button);
				
				
            button = new JButton(tool1);
            button.addActionListener(new ActionListener()
                {
					 	  public void actionPerformed(ActionEvent e)
                    {
                     	frame = new JFrame(tool1);
								frame.setLayout(new GridLayout(0,1));
								frame.setVisible(true);
								frame.setSize(220, 80);
								frame.setResizable(false);
								
								JButton button1 = new JButton("Quiz");
								JButton button2 = new JButton("Solve");

								frame.add(button1);
								frame.add(button2);

								button1.addActionListener(new ActionListener()
                			{
                    			public void actionPerformed(ActionEvent e)
                    			{
										quizAction();
										frame.dispose();
 									}
								});

								button2.addActionListener(new ActionListener()
                			{
                    			public void actionPerformed(ActionEvent e)
                    			{
									  solveAction();
									  frame.dispose();
									}
								});

	                    }
                });
            getContentPane().add(button);


            button = new JButton(tool2);
            button.addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                    	frame = new JFrame(tool2);
                    	frame.setLayout(new GridLayout(0,1));
						frame.setVisible(true);
						frame.setSize(250, 100);
						frame.setResizable(false);
						
						JButton button1 = new JButton("Add Problem to Repository");
						JButton button2 = new JButton("Visualize Trace");
						JButton button3 = new JButton("Create a Graded Quiz");

						frame.add(button1);
						frame.add(button2);
						frame.add(button3);

					button1.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							FiniteStateAutomaton fsa = new FiniteStateAutomaton();
							fsa.fromSaveProblem = true;
							createWindow(fsa);
							frame.dispose();
						}
					});

					button2.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							new ViewFlowAction().actionPerformed(null);
							frame.dispose();
						}
					});
					
					button3.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							JOptionPane.showMessageDialog(null, "This feature is currently being Updated! Please Bear With Us.");
							frame.dispose();
						}
					});
					
                    }
                });
            getContentPane().add(button);

		}
	}
	
	private static void quizAction()
	{
		 frame.setVisible(false);
		 Object[] possibleValues = { "English Description", "Random Regular Expression" };
		 Object selectedValue = JOptionPane.showInputDialog(null, "What Form Do You Want the Problem in?",
		                        "Quiz Me", JOptionPane.INFORMATION_MESSAGE,  null, possibleValues, possibleValues[0]);
		
		if (selectedValue == possibleValues[0]) {

		   new TestDFAAction().actionPerformed(null);
		} 
		
		else if (selectedValue == possibleValues[1]) {
		   new QuizMeAction().actionPerformed(null);
		}
		
		frame.setVisible(true);
	}
	
	private static void solveAction()
	{
		frame.setVisible(false);
		FiniteStateAutomaton fsa = new automata.fsa.FiniteStateAutomaton();
      fsa.fromSolve = true;
      createWindow(fsa);
	}

	/** The universal dialog. */
	private static NewDialog DIALOG = null;
	private static JFrame frame = null;
	private static final String tool1 = "Student Tools";
	private static final String tool2 = "Instructor Tools";
	}
