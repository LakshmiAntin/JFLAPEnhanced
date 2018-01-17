package gui.action;

import java.io.*;
import java.util.*;
import java.lang.Object;
import java.lang.*;
import org.apache.commons.io.IOUtils;
import javax.swing.JOptionPane;

public class TryJava {

  public static String printLines(String name, InputStream ins) throws Exception {
    String line = null;
    BufferedReader in = new BufferedReader(
        new InputStreamReader(ins));
    String newS = new String();
    while ((line = in.readLine()) != null) {
       // JOptionPane.showMessageDialog(null, line, "Compiler Message", JOptionPane.ERROR_MESSAGE);
        System.out.println(name + " " + line+"\n\n");

        newS = newS.concat(line);
        newS = newS.concat("\n");
        System.out.println(newS);
    }
    String indented = newS.replaceAll("(?m)^", "\t");
    return indented;
  }



  public static void runProcess(String command) throws Exception {
    Process pro = Runtime.getRuntime().exec(command);

    String instr = printLines(command + " stdout:", pro.getInputStream());
    String errstr = printLines(command + " stderr:", pro.getErrorStream());
   // System.out.println("Out if!!!");
    if (pro != null && pro.exitValue() != 0) {
       // JOptionPane.showMessageDialog(null, line, "Compiler Message", JOptionPane.ERROR_MESSAGE);
        JOptionPane.showMessageDialog(null, errstr, "Compiler Message", JOptionPane.ERROR_MESSAGE);
    }
  
    //System.out.println("After if!!!"); 
    //JOptionPane.showMessageDialog(null, pro.getInputStream(), "Compiler Message", JOptionPane.ERROR_MESSAGE);
    pro.waitFor();
    System.out.println(command + " exitValue() " + pro.exitValue());
  }

  /*public static void main(String[] args) {
    try {
      runProcess("javac /home/nitish/Documents/Java/Main.java");
      runProcess("java /home/nitish/Documents/Java/Main");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }*/
}