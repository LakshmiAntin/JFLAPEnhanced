
package gui.action;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

/*
 * A simple Text Editor.  This demonstrates the use of a 
 * JFileChooser for the user to select a file to read from or write to.
 * It also demonstrates reading and writing text files.
 */
public class TextEditor implements ActionListener {
	// Size of editing text area.
	private static final int NUM_ROWS = 25;
	private static final int NUM_COLS = 50;
	private JFrame frame;
	// Buttons to save and load files.
	private JButton saveButton; /*loadButton, testOneButton, testAllButton*/;

	// Area where the user does the editing
	private JTextArea text;

	private String defaultText = "\npublic static boolean inLanguage(String x)\n"+
								  "{\n" +
								    /*"if (myString == null && myString.isEmpty())\n"+
								    "\treturn false;\n"+
								    "else {\n"+*/
 								  	"\t/* your code here */\n" +
								  	"\n" +
								  	"\treturn false;\n" +
								  //	"\t}\n"+
								  "}" ;

	// Creates the GUI
	public void buildEditor() {
		frame = new JFrame();
		JPanel buttonPanel = new JPanel();
		saveButton = new JButton("Save File");
		//loadButton = new JButton("Load File");
		/*testOneButton = new JButton("Test One");
		testAllButton = new JButton("Test All");*/
		buttonPanel.add(saveButton);
		//buttonPanel.add(loadButton);
		/*buttonPanel.add(testOneButton);
		buttonPanel.add(testAllButton);*/

		text = new JTextArea(defaultText, NUM_ROWS, NUM_COLS);
		text.setFont(new Font("monospaced", Font.PLAIN, 14));
		JScrollPane textScroller = new JScrollPane(text);
		Container contentPane = frame.getContentPane();
		contentPane.add(textScroller, BorderLayout.CENTER);
		contentPane.add(buttonPanel, BorderLayout.NORTH);

		saveButton.addActionListener(this);
		//loadButton.addActionListener(this);
		/*testOneButton.addActionListener(this);
		testAllButton.addActionListener(this);*/
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
    		@Override
    		public void windowClosing(java.awt.event.WindowEvent windowEvent) {
        		return;
        		}
		});
		frame.pack();
		frame.setVisible(true);
		//frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	// Listener for button clicks that loads the
	// specified files and puts it in the
	// editor.
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == saveButton) {
			saveFile();
		} /*else 
		if (event.getSource() == loadButton)
		{
			loadFile();
		}*/
		else 
		if (event.getSource() == frame)
		{
				System.out.println("In frame");
		}
		
	}

	// Display a file chooser so the user can select a file
	// to save to. Then write the contents of the text area
	// to that file. Does nothing if the user cancels out
	// of the file chooser.
	private void saveFile() {
		File file;

		String header = "package Solution;\n"+
						 "\nimport java.util.*;\n"+
						 "import java.lang.*;\n"+
						 "import java.io.*;\n\n"+
						 "public class SolutionBase\n"+
						 "{\n" ;

		String footer = "\n}" ;
		String main = "public static void main(String [] args) {\n"+
                        "\tString input = \"\";\n"+
                        "\tif(args.length > 0) input = args[0];\n"+
                        "\tint answer = 2;\n"+
                        "\ttry{\n"+
                        "\t\tanswer = (inLanguage(input) ? 0 : 1);\n"+
                        "\t} catch(Exception e) { }\n"+
                        "\tSystem.exit(answer);\n"+
    					"}\n";


			try {
				TryJava tj = new TryJava();
				//tj.runProcess("mkdir Solution");
				String fileName = "Solution" + System.getProperty("file.separator") + "SolutionBase.java";
				file = new File(fileName);
				// Now write to the file
				PrintWriter output = new PrintWriter(new FileWriter(file));
				output.println(header);
				output.println(text.getText());
				output.println(main);
				output.println(footer);
				output.close();
				JOptionPane.showMessageDialog(text, "File saved successfully");
				//System.out.println(path);
				tj.runProcess("javac "+fileName);
			} 
			catch (IOException e) {
				JOptionPane.showMessageDialog(text, "Can't save file "
						+ e.getMessage());
			}
			catch (Exception ex) {
				System.out.println(ex.getMessage());
			}
	}

}
