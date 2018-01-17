							JFLAP ENHANCED TOOL


NOTE: Please ensure that the 'DFA_LIBRARY' and 'Solution' folders are in the same folder as jflapenhanced.jar for the tool to work.


1. Setting Up the Environment


1.1 Ubuntu 

The existence of JRE(Java Runtime Environment) 7 or above is mandatory for the tool to run.

JRE7 can be installed by using the following commands:

Open the Terminal =>On the command line, type:

$ sudo apt-get install openjdk-7-jre

This will install JRE and all the relevant dependencies.



1.2 Windows

1.2.1 JRE 7 can be downloaded from the link provided below

http://www.oracle.com/technetwork/java/javase/downloads/jre7-downloads-1880261.html

1.2.2 JDK (1.6 or above) is also required and can be downloaded from the link provided below:

http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html

1.2.3 Environment Setup

The JDK Bin path needs to be updated in System Properties -> Advanced -> Environment Variables -> System Variables -> Path.


2. Running the Tool(Jar File)


2.1 Unzip jflapenhanced.zip. It contains 'jflapenhanced.jar','DFA_LIBRARY' folder & 'Solution' folder. Please ensure that all three of them are in the same folder.


2.2 Executing the Jar in Ubuntu

Open the Terminal =>On the command line, type:

$ java -jar jflapenhanced.jar


Note: Please ensure case-sensitiveness of commands for proper results.

2.3 Executing the Jar in Windows

Double Click on jflapenhanced.jar file to launch the tool(Requires JRE 7).


3. Operating Procedure

The features can be accessed by clicking on either 'Student Tools' or 'Instructor Tools' button in the main menu.



The Student Tools are:

A. Quiz Me
B. Solve -> Test Against Code & Test Against GUI

        &

The Instructor Tools are:

C. Add Problem to Repository
D. Visualize Trace
E. Create a Graded Quiz


A. Quiz Me

This is an interactive learning mechanism that queries the user with automata construction problems.

The user can choose the form in which the problem appears by clicking on 'Quiz Me'.

A.1 Quiz Me => What Form Do You Want The Problem In?
		->English Description
		->Regular Expression

The user is provided with a window that has the problem statement in the selected form (Either English Description or Regular Expression)


A.2 Once the Problem is Displayed, the user is provided with the following options:
		
		-> Draw DFA -> Test Against Solution
		-> View Possible Strings
		-> Test Input String
		-> Show Me The Answer
                -> Log My Session  



*Draw DFA: The user can draw the answer by clicking on 'Draw DFA' and then check the validity and receive further guidelines by clicking on 'Test Against Solution'.

*View Possible Strings: The user can view a set of sample strings that belong to the language to serve as hints in solving the problem.

*Test Input String: The user can enter custom strings to check if they are accepted or rejected by the automaton. This also serve as hint in solving a given problem.

*Show Me The Answer: The user can choose to view the answer if he is unable to solve the problem.

*Log My Session: If this is checked, a log file (in the form of XML) of the user's Quiz Me session will be stored in the same working directory from which the jar is executed. Timestamps on the XML file can be used to differentiate among the various log files. 


B. Solve 

The two features: 'Test Against Code' & 'Test Against GUI' are available as buttons in the right top corner of the window.


B.1 Test Against Code

The user can draw an automaton by using Jflap's drawing capabilities and subsequently enter a code (based on the given syntax of the inLanguage function) to check if the two match. The user can then receive feedback for various inputs in terms of code Match/Code Mismatch.


B.1.1 Code Input:

*Test Against Code => Save File / Load File


Sample Code for Automaton that accepts even 1s:

public boolean inLanguage(String x) {
  int numOnes = 0;
  for(int i = 0; i < x.length(); i++) {
    if(x.charAt(i) == '1')
      numOnes++;
  }
  return (numOnes % 2 == 0);
}


B.1.2 Running Inputs

*Test Against Code => Run Inputs
The user can enter inputs and then run them using the 'Run Inputs' button.


B.2 Test Against GUI

The user can draw an automaton and then click on Test against GUI and choose from a grid of predicates and then click on 'Translate to English' to get an English Statement corresponding to the input.



C. Save As Problem

This feature can be used by the user to manually add problems to expand the library.

The user should draw the automaton by utilizing the drawing capabilities provided by Jflap and then click on 'Save As problem'.

*Save As Problem => Enter A Problem Description to be Saved

Once the problem description is provided by the user, the problem is saved along with the description in the library.

The three features are available as buttons in the right top corner of the window.


D. Visualize Trace

Clicking on this menu prompts the user to choose an XML file for which the trace can be visualized. The Start, Next and Back buttons can be used to navigate the trace.

Note: If the Log My Session button is checked in the Quiz Me interface, a log file of the user's Quiz Me session will be stored in the same working directory from which the jar is executed. This can be used as input for the Visualize Trace functionality.


E. Create a Graded Quiz

This functionality is currently being updated and hence is currently non-functional.