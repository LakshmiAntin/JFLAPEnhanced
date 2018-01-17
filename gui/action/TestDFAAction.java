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

import gui.environment.EnvironmentFrame;
import gui.environment.Universe;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.OutputStream;

import java.net.URI;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import org.apache.commons.io.IOUtils;



/**
 * This action handles quitting.
 * 
 * @author Thomas Finley
 */

public class TestDFAAction extends RestrictedAction {
	/** A simple object to get the class off for resource reading. */
	private static Object OBJECT = new Object(); 
	private boolean randomREFlow;
	public boolean isRandomREFlow() {
		return randomREFlow;
	}

	public void setRandomREFlow(boolean randomREFlow) {
		this.randomREFlow = randomREFlow;
	}

	/**
	 * Instantiates a new <CODE>QuitAction</CODE>.
	 */
	public TestDFAAction() {
		super("Test DFA", null);
		//putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Q,
			//	MAIN_MENU_MASK));
	}

	/**
	 * This begins the process of quitting. If this method returns, you know it
	 * did not succeed. Quitting may not succeed if there is an unsaved document
	 * and the user elects to cancel the process.
	 */
	public static void beginQuit() {
		EnvironmentFrame[] frames = Universe.frames();
		for (int i = 0; i < frames.length; i++)
			if (!frames[i].close())
				return;
		
		//modified by Moti Ben-Ari
		if (gui.Main.getDontQuit())
			NewAction.closeNew();
		else
			System.exit(0);
		
	}

	/**
	 * In repsonding to events, it cycles through all registered windows in the
	 * <CODE>gui.environment.Universe</CODE> and closes them all, or at least
	 * until the user does something that stops a close, at which point the quit
	 * terminates.
	 */
	public void actionPerformed(ActionEvent e) {
		//beginQuit();
	
			
		
	/*	Object[] possibleValues = {"Easy", "Hard"};
		Object selectedValue = JOptionPane.showInputDialog(null,
	            "Select the Level of Difficulty", "Quiz Me",
	            JOptionPane.INFORMATION_MESSAGE, null,
	            possibleValues, possibleValues[0]);
	if (selectedValue==possibleValues[0]){
		File f = new File("DFA_LIBRARY" + System.getProperty("file.separator") + "Easy");
		if(f.isDirectory()){
			int length =  (f.listFiles(new FilenameFilter() {
			        @Override
			        public boolean accept(File dir, String name) {
			            return name.toLowerCase().endsWith(".jff");
			        }
			    })).length;
			if(length > 0)
				openRandomFile("Easy",length);
			else
				JOptionPane.showInputDialog("The DFA Library does not contain problems in Easy Category");

		}
			
		
	}
	else if(selectedValue==possibleValues[1]){
		File f = new File("DFA_LIBRARY" + System.getProperty("file.separator") + "Hard");
		if(f.isDirectory()){
			int length =  (f.listFiles(new FilenameFilter() {
			        @Override
			        public boolean accept(File dir, String name) {
			            return name.toLowerCase().endsWith(".jff");
			        }
			    })).length;
		//	int length = f.listFiles().length;
			if(length > 0)
				openRandomFile("Hard",length);
			else
				JOptionPane.showInputDialog("The DFA Library does not contain problems in Hard Category");


		}
			
	}*/
		
		File f = new File("DFA_LIBRARY");
		if(f.isDirectory()){
			int length =  (f.listFiles(new FilenameFilter() {
			        @Override
			        public boolean accept(File dir, String name) {
			            return name.toLowerCase().endsWith(".jff");
			        }
			    })).length;
			if(length > 0)
				openRandomFile(length);
		}
		
}
	
/*	public void openRandomFile(String type,int limit){

		Random rand = new Random();
		int face = 1 + rand.nextInt(limit);
		String fileNameIndex = new Integer(face).toString();
		try{
		File f = getDFAResource(type,fileNameIndex);
			
			String text = isRandomREFlow()? null : getDFAText(type, fileNameIndex);

			boolean result = new OpenAction().openFromOther(text,f);
			if(!result)
				openRandomFile(type, limit);

		}
		catch(Exception e){
			//JOptionPane.showMessageDialog(null,"Error Showing Problem! Please try again");
			openRandomFile(type,limit);

		}

	}*/
	
	public void openRandomFile(int limit){

		Random rand = new Random();
		int face = 1 + rand.nextInt(limit);
		String fileNameIndex = new Integer(face).toString();
		try{
		File f = getDFAResource(fileNameIndex);
			
			String text = isRandomREFlow()? null : getDFAText(fileNameIndex);

			boolean result = new OpenAction().openFromOther(text,f,face);
			if(!result)
				openRandomFile(limit);

		}
		catch(Exception e){
			//JOptionPane.showMessageDialog(null,"Error Showing Problem! Please try again");
			openRandomFile(limit);

		}
	}
	
	
	/*private File getDFAResource(String type, String fileNameIndex) throws Exception{

		File f = null;
		f = new File("DFA_LIBRARY" + System.getProperty("file.separator") + type + System.getProperty("file.separator") + fileNameIndex + ".jff");	
		return f;
	}*/
		
		private File getDFAResource(String fileNameIndex) throws Exception{

			File f = null;
			f = new File("DFA_LIBRARY" + System.getProperty("file.separator") + fileNameIndex + ".jff");	
			return f;
		}
	
/*	private String getDFAText(String type, String fileNameIndex) throws Exception{

		File f = null;
		StringBuilder stringBuilder = new StringBuilder();
	
		 f = new File("DFA_LIBRARY" + System.getProperty("file.separator") +  type + System.getProperty("file.separator")  + fileNameIndex + ".txt");
			
			FileReader reader = new FileReader(f);
			BufferedReader br = new BufferedReader(reader);
			for(String line = br.readLine(); line != null; line = br.readLine()) {
			    stringBuilder.append(line + "\n");
			}
			br.close();
	

			String text = stringBuilder.toString();
			return text;
	}*/
		
		private String getDFAText(String fileNameIndex) throws Exception{

			File f = null;
			StringBuilder stringBuilder = new StringBuilder();
		
			 f = new File("DFA_LIBRARY" + System.getProperty("file.separator") + fileNameIndex + ".txt");
				
				FileReader reader = new FileReader(f);
				BufferedReader br = new BufferedReader(reader);
				for(String line = br.readLine(); line != null; line = br.readLine()) {
				    stringBuilder.append(line + "\n");
				}
				br.close();
		

				String text = stringBuilder.toString();
				return text;
		}

}
