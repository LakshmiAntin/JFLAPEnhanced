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





package gui.editor;

import gui.deterministic.TrapStateTool;
import gui.deterministic.TrapTransitionTool;
import gui.viewer.AutomatonDrawer;
import gui.viewer.AutomatonPane;
import gui.viewer.SelectionDrawer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;

import automata.Automaton;
import automata.Note;
import automata.fsa.FiniteStateAutomaton;

/**
 * This is a view that holds a tool bar and the canvas where the automaton is
 * displayed.
 * 
 * @author Thomas Finley
 */

public class EditorPane extends JComponent {
	/**
	 * Instantiates a new editor pane for the given automaton.
	 * 
	 * @param automaton
	 *            the automaton to create the editor pane for
	 */
	public EditorPane(Automaton automaton) {
		this(new SelectionDrawer(automaton));
	}

	/**
	 * Instantiates a new editor pane with a tool box.
	 */
	public EditorPane(Automaton automaton, ToolBox box) {
		this(new SelectionDrawer(automaton), box);
	}

	/**
	 * Instantiates a new editor pane with a given automaton drawer.
	 * 
	 * @param drawer
	 *            the special automaton drawer for this editor
	 */
	public EditorPane(AutomatonDrawer drawer) {
		this(drawer, new DefaultToolBox());
	}

	/**
	 * Instantiates a new editor pane with a given automaton drawer.
	 * 
	 * @param drawer
	 *            the special automaton drawer for this editor
	 * @param box
	 *            the tool box to get the tools from
	 */
	public EditorPane(AutomatonDrawer drawer, ToolBox box) {
		this(drawer, box, false);
	}

	/**
	 * Instantiates a new editor pane with a given automaton drawer.
	 * 
	 * @param drawer
	 *            the special automaton drawer for this editor
	 * @param box
	 *            the tool box to get teh tools from
	 * @param fit
	 *            <CODE>true</CODE> if the editor should resize its view to
	 *            fit the automaton; note that this can be <I>very</I> annoying
	 *            if the automaton changes
	 */
	public EditorPane(AutomatonDrawer drawer, ToolBox box, boolean fit) {
		pane = new EditCanvas(drawer, fit);
		pane.setCreator(this);
		this.drawer = drawer;
		this.automaton = drawer.getAutomaton();
		this.setLayout(new BorderLayout());

		JPanel superpane = new JPanel();
		superpane.setLayout(new BorderLayout());
		superpane.add(new JScrollPane(pane,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS), BorderLayout.CENTER);
		superpane.setBorder(new BevelBorder(BevelBorder.LOWERED));
		if((automaton instanceof FiniteStateAutomaton && automaton.fromShowAnswer)){
		box = new ToolBox() {
			public List tools(AutomatonPane view, AutomatonDrawer drawer) {
				LinkedList tools = new LinkedList();
				tools.add(new ArrowNontransitionTool(view, drawer));
				
				return tools;
			}
		};
		//box.add(button);
		
		}
		toolbar = new gui.editor.ToolBar(pane, drawer, box);
		if((automaton instanceof FiniteStateAutomaton && automaton.fromShowAnswer)){
			JButton button = new JButton("Click Arrow to Move States");
			button.setEnabled(false);
			toolbar.add(button);
		}
		
		if((automaton instanceof FiniteStateAutomaton && ((FiniteStateAutomaton)automaton).isFromNewAction())){
			JButton button = new JButton("Feedback");
			//button.setEnabled(false);
			button.setAlignmentX(Component.RIGHT_ALIGNMENT);
			toolbar.add(Box.createRigidArea(new Dimension(350, 0)));
			button.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					
			
						String downloadURL = "https://docs.google.com/forms/d/1-EvO3zExbg52wPCuVFOnrLvmNz3Yq-HfQYSCob_HmSA/viewform?usp=send_form";
					    java.awt.Desktop myNewBrowserDesktop = java.awt.Desktop.getDesktop();
					
					 try
					  {
					       java.net.URI myNewLocation = new java.net.URI(downloadURL);
					      myNewBrowserDesktop.browse(myNewLocation);
						
				
					  }
					     catch (Exception e) {
					    	 JOptionPane.showMessageDialog(null, "FeedBack Form Could Not Be Opened. Please Try Again Later.");
					     }
					      
					  
					    
					  
			
					
				}
			});
			toolbar.add(button);
		}
		pane.setToolBar(toolbar);

		this.add(superpane, BorderLayout.CENTER);
		this.add(toolbar, BorderLayout.NORTH);
		this.add(new AutomatonSizeSlider(pane, drawer), BorderLayout.SOUTH);

		ArrayList notes = drawer.getAutomaton().getNotes();
		for(int k = 0; k < notes.size(); k++){
			((Note)notes.get(k)).initializeForView(pane);
		}
	}

	/**
	 * Returns the toolbar for this editor pane.
	 * 
	 * @return the toolbar of this editor pane
	 */
	public gui.editor.ToolBar getToolBar() {
		return toolbar;
	}

	/**
	 * Returns the automaton drawer for the editor pane canvas.
	 * 
	 * @return the drawer that draws the automaton being edited
	 */
	public AutomatonDrawer getDrawer() {
		return pane.getDrawer();
	}

	/**
	 * Returns the automaton pane.
	 * 
	 * @return the automaton pane
	 */
	public EditCanvas getAutomatonPane() {
		return pane;
	}

	/**
	 * Prints this component. This will print only the automaton section of the
	 * component.
	 * 
	 * @param g
	 *            the graphics object to paint to
	 */
	public void printComponent(Graphics g) {
		pane.print(g);
	}

	/**
	 * Children are not painted here.
	 * 
	 * @param g
	 *            the graphics object to paint to
	 */
	public void printChildren(Graphics g) {

	}

	/**
	 * Returns the automaton pane.
	 * 
	 * @return the automaton pane
	 */
	public Automaton getAutomaton() {
		return automaton;
	}

	/** The automaton. */
	protected Automaton automaton;

	/** The automaton drawer. */
	protected AutomatonDrawer drawer;

	/** The automaton pane. */
	protected EditCanvas pane;

	/** The tool bar. */
	protected gui.editor.ToolBar toolbar;
}
