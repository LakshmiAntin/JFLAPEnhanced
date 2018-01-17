package gui.action;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.GroupLayout;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.util.LinkedList;
import javax.swing.JOptionPane;

public class FrontEnd 
{
   private JFrame fr1;
	private JPanel fr2;
	private JPanel fr3;
   
   private studentCode studentcode;
   private studentCode studentobject;
   private studentCode code;
   private Class classType;
	
   private String displayString;
   private String listString;
   private JButton LengthButton;
   private JButton OkButton;
   private JButton CancelButton;
   private JButton IntConstantButton;
   private JButton TranslateButton;
   private JButton LesserOrEqualButton;
   private JButton StringConstantButton;
   private JButton NotEqualButton;
   private JButton ModulusButton;
   private JButton NumberOfOccurenceButton;
   private JButton AndButton;
   private JButton OrButton;
   private JButton NotButton;
   private JButton LesserThanButton;
   private JButton GreaterThanButton;
   private JButton EqualsButton;
   private JButton GreaterOrEqualButton;
   private JButton XButton;
	private JButton StartsButton;
	private JButton EndsButton;
	private JButton ValButton;
	private JButton FollowedBy;	
   private JLabel Label;
   private JTextArea TextArea;
   private LinkedList<studentCode> list;
   private LinkedList<studentCode> finalList;
   private JButton[] int_type;
   private JButton[] boolean_type;
   private JButton[] string_type;
   private JButton[] freeVariable_type;
   private int nullCount;
   private String[] alphabet_type;
   private int count;
   public FrontEnd()
   {
      fr1 = new JFrame("Expression");
		fr2 = new JPanel();
		fr3 = new JPanel();
      TextArea = new JTextArea();
      LengthButton = new JButton("Length");
      ModulusButton = new JButton("Modulus");
      IntConstantButton = new JButton("Integer");
      AndButton = new JButton("AND");
      OrButton = new JButton("OR");
      NotButton = new JButton("NOT");
      GreaterThanButton = new JButton(">");
      LesserThanButton = new JButton("<");
      EqualsButton = new JButton("=");
      GreaterOrEqualButton = new JButton(">=");
      TranslateButton = new JButton("Translate to English");
      IntConstantButton = new JButton("Int Const");
      Label = new JLabel("Expression :");
      LesserOrEqualButton = new JButton("<=");
      StringConstantButton = new JButton("Str Const");
      NotEqualButton = new JButton("!=");
      NumberOfOccurenceButton = new JButton("count");
		ValButton = new JButton("val");
		StartsButton = new JButton("starts with");
		EndsButton = new JButton("ends with");
      OkButton = new JButton("OK");
      XButton = new JButton("x");
      FollowedBy = new JButton("followed By");
      CancelButton = new JButton("Cancel");
   	
      count = 0;
      int_type = new JButton[5];
      boolean_type = new JButton[11];
      string_type = new JButton[1];
      freeVariable_type = new JButton[1];
      
      int_type[0] = LengthButton;
      int_type[1] = ModulusButton;
      int_type[2] = NumberOfOccurenceButton;
      int_type[3] = IntConstantButton;
		int_type[4] = ValButton;
      
      boolean_type[0] = AndButton;
      boolean_type[1] = OrButton;
      boolean_type[2] = NotButton;
      boolean_type[3] = GreaterThanButton;
      boolean_type[4] = LesserThanButton;
      boolean_type[5] = EqualsButton;
      boolean_type[6] = GreaterOrEqualButton;
      boolean_type[7] = LesserOrEqualButton;
      boolean_type[8] = NotEqualButton;
		boolean_type[9] = StartsButton;
		boolean_type[10] = EndsButton;
      
      string_type[0]  = StringConstantButton;
   	
      freeVariable_type[0] = XButton;
      list = new LinkedList<studentCode>();
      finalList = new LinkedList<studentCode>();
      studentcode = new studentCode();
      displayString = new String("x is such that ");
   	listString = "";
      enable(boolean_type);
   }
  
    

   public void actionPerformed(final String[] alphabet)
   {
      alphabet_type = alphabet;
      fr1.setVisible(true);
		fr2.setVisible(true);
      fr1.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

      TextArea.setLineWrap(true);
		fr1.setLayout(new GridLayout(4,1));
		fr1.add(Label);
		fr1.add(TextArea);
		fr1.setSize(500,300);
		fr1.setResizable(false);
		
		fr2.setLayout(new GridLayout(4,5));
		fr2.add(LengthButton);
		fr2.add(AndButton);
		fr2.add(GreaterThanButton);
		fr2.add(LesserThanButton);
		fr2.add(IntConstantButton);
		fr2.add(ModulusButton);
		fr2.add(OrButton);
		fr2.add(LesserOrEqualButton);
		fr2.add(GreaterOrEqualButton);
		fr2.add(StringConstantButton);
		fr2.add(NumberOfOccurenceButton);
		fr2.add(NotButton);
		fr2.add(EqualsButton);
		fr2.add(NotEqualButton);
		fr2.add(XButton);
		fr2.add(ValButton);
		fr2.add(StartsButton);
		fr2.add(EndsButton);
      fr2.add(FollowedBy);
	
		fr3.setLayout(new FlowLayout(FlowLayout.CENTER));
		fr3.setPreferredSize(new Dimension(300,100));
		fr3.add(OkButton);
		fr3.add(CancelButton);
		fr3.add(TranslateButton);
		
		fr1.add(fr2);
		fr1.add(fr3);
		fr2.setVisible(true);
    
   
      LengthButton.addActionListener(
            new ActionListener() {
               public void actionPerformed(ActionEvent evt) {               
                  Length  tnode = new Length();
                  addToTree(tnode);            
               }                 
            });
				
		 ModulusButton.addActionListener(
            new ActionListener() {
               public void actionPerformed(ActionEvent evt) {
                  Modulus tnode = new Modulus();
                  addToTree(tnode);        
               }         
            });
				
				
		 ValButton.addActionListener(
            new ActionListener() {
               public void actionPerformed(ActionEvent evt) {               
                  Value  tnode = new Value();
                  addToTree(tnode);            
               }                 
            });
		
      OrButton.addActionListener(
            new ActionListener() {
               public void actionPerformed(ActionEvent evt) {               
                  OR tnode = new OR();
                  addToTree(tnode);
               }          
            });
				
		 StartsButton.addActionListener(
            new ActionListener() {
               public void actionPerformed(ActionEvent evt) {               
                  StartsWith tnode = new StartsWith();
                  addToTree(tnode);
               }          
            });
				
		 EndsButton.addActionListener(
            new ActionListener() {
               public void actionPerformed(ActionEvent evt) {               
                  EndsWith tnode = new EndsWith();
                  addToTree(tnode);
               }          
            });
				
      NotButton.addActionListener(
            new ActionListener() {
               public void actionPerformed(ActionEvent evt) {               
                  NOT tnode = new NOT();
                  addToTree(tnode);                 
               }   
            });
    
     AndButton.addActionListener(
      new ActionListener() {
         public void actionPerformed(ActionEvent evt) {
            AND tnode = new AND();
            addToTree(tnode);         
         }            
      });
   			
      GreaterThanButton.addActionListener(
            new ActionListener() {
               public void actionPerformed(ActionEvent evt) {
                  GreaterThan tnode = new GreaterThan();
                  addToTree(tnode);        
               }            
            });
   							
      LesserThanButton.addActionListener(
            new ActionListener() {
               public void actionPerformed(ActionEvent evt) {
                  LesserThan tnode = new LesserThan();
                  addToTree(tnode);        
               }
            });
   			
      EqualsButton.addActionListener(
            new ActionListener() {
               public void actionPerformed(ActionEvent evt) {
                  Equals tnode = new Equals();
                  addToTree(tnode);        
               }            
            });
   			
      GreaterOrEqualButton.addActionListener(
            new ActionListener() {
               public void actionPerformed(ActionEvent evt) {
                  GreaterOrEqual tnode = new GreaterOrEqual();
                  addToTree(tnode);        
               }            
            });
   			       						
      LesserOrEqualButton.addActionListener(
            new ActionListener() {
               public void actionPerformed(ActionEvent evt) {
                  LesserOrEqual tnode = new LesserOrEqual();
                  addToTree(tnode);        
               }            
            });
   			
      NotEqualButton.addActionListener(
            new ActionListener() {
               public void actionPerformed(ActionEvent evt) {
                  NotEqual tnode = new NotEqual();
                  addToTree(tnode);        
               }            
            });
   			
      XButton.addActionListener(
            new ActionListener() {
               public void actionPerformed(ActionEvent evt) {
                  FreeVariable tnode = new FreeVariable();
                  addToTree(tnode);        
               }            
            });
   
      NumberOfOccurenceButton.addActionListener(
            new ActionListener() {
               public void actionPerformed(ActionEvent evt) {
                  NumberofOccurences tnode = new NumberofOccurences();
                  addToTree(tnode);        
               }            
            });
   			
      StringConstantButton.addActionListener(
            new ActionListener() {
               public void actionPerformed(ActionEvent evt) {
                  boolean flag = true;
                  String input = JOptionPane.showInputDialog("Enter String");
                  if( input != null)
                  {
                     for( int i = 0; i < input.length() ; i++ )
                     {
                        int j;
                        for( j=0 ; j < alphabet_type.length ; j++ )
                        {
                           if( alphabet_type[j].charAt(0) == input.charAt(i))
                           {
                              break;
                           }
                        }
                        if( j == alphabet_type.length )
                        {
                           flag = false;
                           JOptionPane.showMessageDialog(fr1, "OOPS!! DFA's alphabet and the string constant you entered doesn't match ","Invalid Input.", JOptionPane.WARNING_MESSAGE);
                           break;	
                        }
                     }
                     if(flag){
                        StringConstants tnode = new StringConstants(input);
                        addToTree(tnode);
                     }        
                  }
               }            
            });
   			
      IntConstantButton.addActionListener(
            new ActionListener() {
               public void actionPerformed(ActionEvent evt) { 
                  try
                  {
                     String input = JOptionPane.showInputDialog("Enter Integer");
                     if( input != null)
                     {
                        int constant = Integer.parseInt(input);
								if(constant < 0)
								throw new RuntimeException();
                        IntegerConstants tnode = new IntegerConstants(constant);
                        addToTree(tnode);
                     }
                           
                  }
                  catch (RuntimeException e1)
                  {
                     JOptionPane.showMessageDialog(fr1, "OOPS!! Input has to be an integer greater than zero.","Invalid Input.", JOptionPane.WARNING_MESSAGE);
                  }
                  catch (Exception e2)
                  {
                     JOptionPane.showMessageDialog(fr1, "OOPS!! Input has to be an Integer.","Invalid Input.", JOptionPane.WARNING_MESSAGE);
                  }     
               }	     
            });
   			           
      TranslateButton.addActionListener(
            new ActionListener() {
               public void actionPerformed(ActionEvent evt) {  
                  TreeNode root = studentcode.getRoot();
                  JOptionPane.showMessageDialog(fr1, print_list());	
               }                            
            });
   			
      OkButton.addActionListener(
            new ActionListener() {
               public void actionPerformed(ActionEvent evt) {  
                  fr1.dispose();             
                  list.add(studentcode);
                  finalList = list;
                  
               }           
            });
      FollowedBy.addActionListener(
              new ActionListener() {
               public void actionPerformed(ActionEvent evt) { 
            listString = listString +  exp(studentcode.getRoot()) + " and ";
            list.add(studentcode);
            studentcode = new studentCode();
            enable(boolean_type);
                       
               }           
            });
   			
   					
      CancelButton.addActionListener(
            new ActionListener() {
               public void actionPerformed(ActionEvent evt) {
                  studentcode.setRoot();
                  fr1.dispose();        
               }           
            });
 	   fr1.pack();
   
   }
   private void enable(JButton[] button)
   {
      LengthButton.setEnabled(false);
      ModulusButton.setEnabled(false);
      AndButton.setEnabled(false);
      OrButton.setEnabled(false);
      NotButton.setEnabled(false);
      GreaterThanButton.setEnabled(false);
      LesserThanButton.setEnabled(false);
      EqualsButton.setEnabled(false);
      OkButton.setEnabled(false);
      GreaterOrEqualButton.setEnabled(false);
      LesserOrEqualButton.setEnabled(false);
      NotEqualButton.setEnabled(false);
      NumberOfOccurenceButton.setEnabled(false);
      IntConstantButton.setEnabled(false);
      StringConstantButton.setEnabled(false);
      TranslateButton.setEnabled(false);
      XButton.setEnabled(false);
		StartsButton.setEnabled(false);
		EndsButton.setEnabled(false);
		ValButton.setEnabled(false);
      FollowedBy.setEnabled(false);
     
    
      if(button != null)  
      {	
         for(int i=0; i<button.length; i++)
            button[i].setEnabled(true);
				ValButton.setEnabled(false);
            if(button == int_type){ 
               if(alphabet_type.length==2 && (alphabet_type[0].charAt(0)== '0' || alphabet_type[0].charAt(0)=='1') && (alphabet_type[1].charAt(0)=='1' || alphabet_type[1].charAt(0)=='0')){
                  ValButton.setEnabled(true);
					}
				}

      }		
      else
      {
         OkButton.setEnabled(true);
         TranslateButton.setEnabled(true);
         FollowedBy.setEnabled(true);
         list.add(studentcode);
         //listString = listString+exp(studentcode());
         TextArea.setText(listString+exp(studentcode.getRoot()));
      }
   }  
	
   private void ButtonEnable(Class classType)
   {
      if(classType == null)
         enable(null);	
      else if(classType.equals(Integer.class))
         enable(int_type);
      else if(classType.equals(Boolean.class))
         enable(boolean_type);
      else if(classType.equals(FreeVariable.class))
         enable(freeVariable_type);
      else if(classType.equals(StringConstants.class))
         enable(string_type);
   
   }
   private void addToTree(TreeNode tnode)
   {
      Class nextType = studentcode.buildTree(tnode);
		nullCount = 0;
      //listString =  exp(studentcode());
      TextArea.setText(listString+exp(studentcode.getRoot()));
      ButtonEnable(nextType);
   }
	
   private void print(TreeNode root)
   {
      if(root.getNumArgs() == 2)
      {
         if( root instanceof NumberofOccurences )
         {
            displayString = displayString + "number of times ";
            
         }
         
         print(root.args[0]);
         if(root.getClassName()=="x")
            displayString = displayString + " it ";
         else
         displayString = displayString + root.getClassName() + " ";
         print(root.args[1]);
         
      }		
      else if(root.getNumArgs() == 1)
      {
         if(root.getClassName()=="x")
            displayString = displayString + " it ";
         else
         displayString = displayString + root.getClassName() + " of ";
         print(root.args[0]);  
      }
      
      else{
      if(root.getClassName()=="x")
            displayString = displayString + " it ";
         else
         displayString = displayString + root.getClassName() + " ";
      }
   
   }
  
   private String exp(TreeNode root)
   {
      final String FirstNull = " * ";
      final String NextNull = " . ";
     
      if(root == null) {
        nullCount++;
        return (nullCount == 1) ? FirstNull : NextNull;
      }

      String ans = "";
     
      switch(root.getNumArgs()) {
      case 0:{
         if(root.getClassName()=="x")
            ans = root.getClassName() + (count);
         else
             ans = root.getClassName() + " ";
        break;}
      case 1:{
         if(root.getClassName()=="x")
            ans = root.getClassName() + (count);
         else
        ans = root.getClassName() + " of " + exp(root.args[0]);
        break;}
      case 2:{
         if(root instanceof NumberofOccurences)
           ans = "number of times ";
         if(root.getClassName()=="x")
            ans += (exp(root.args[0]) + " " + root.getClassName()+(count)
             + " " + exp(root.args[1]));
         else

         ans += (exp(root.args[0]) + " " + root.getClassName()
             + " " + exp(root.args[1]));
         break;}
      }
      return ans;
   }
  
   public LinkedList<studentCode> getStudentObject()
   {
      return finalList;
   }
   private  String  print_list()
   {
         String disp = new String(); 
        for(int i = 0; i < list.size() - 1 ; i++)
        {
            print(list.get(i).getRoot());
            disp = displayString + " and ";
            displayString=" x" + (i+1) + " is such that ";      
        }
        print(list.get(list.size() - 1).getRoot());
        disp = disp + displayString;
        return disp;
   }

}
