package gui;

import java.util.TreeSet;

import javax.swing.JOptionPane;

class ParseTree
{
    char value;
    ParseTree left;
    ParseTree right;
	 int depth;
    
    public static String parseToRegex(ParseTree root)
    {
        String a = "";
        String b = "";
        String c = "";
        
        if(root.value == '+')
        {
            a = parseToRegex(root.left);
            c = parseToRegex(root.right);
            b = a + "+" + c;
        }
        else if(root.value == '*')
        {
            a = parseToRegex(root.left);
            b = "(" + a + ")*";
        }
        else if(root.value == '.')
        {
            a = parseToRegex(root.left);
            c = parseToRegex(root.right);
            b = a + c;
        }
		  else
		  		b = Character.toString(root.value);
        return b;
    }
	 
	 public static ParseTree addOr(ParseTree a, ParseTree b)
    {
        ParseTree p = new ParseTree();
        p.value = '+';
        p.left = a;
        p.right = b;
        p.depth = max(a.depth, b.depth) + 1;
        return p;
    }
    
    public static int max(int a, int b)
    {
        if(a>b)
            return a;
        else
            return b;
    }
    
    public static ParseTree addStar(ParseTree a)
    {
        ParseTree p = new ParseTree();
        p.value = '*';
        p.left = a;
        p.right = null;
        p.depth = a.depth + 1;
        return p;
    }
    
    public static ParseTree addConcat(ParseTree a, ParseTree b)
    {
        ParseTree p = new ParseTree();
        p.value = '.';
        p.left = a;
        p.right = b;
        p.depth = max(a.depth, b.depth) + 1;
        return p;
    }
    
    public static ParseTree createTree(int depth)
    {
        double rand = -1;
        ParseTree p = null;
        //randomize the operation
        rand = Math.random();
        
        if(depth == 0)
            return null;
        else if(depth == 1)
        {
            p = new ParseTree();
            if(rand < 0.3333)
                p.value = 'a';
            else if(rand < 0.6666)
                p.value = 'b';
            else
                p.value = 'c';
            p.depth = 0;
        }
        else
        {
            if(rand < 0.3333)
                p = addOr(createTree(depth - 1), createTree(depth - 1));
            else if(rand < 0.6666)
                p = addStar(createTree(depth - 1));
            else
                p = addConcat(createTree(depth - 1), createTree(depth - 1));
        }
        return p;
    }
}

public class ParseTreeTester
{
    public static String regExp(int level)
    {	
    	ParseTree p = new ParseTree();
         int depth = 0;
		  
		  int diff = level;
		
		  if(diff == 1)
			  depth = 3;
		  else if(diff == 2)
				depth = 4;
		  else
				depth = 5;


		  
		  String ans = "";
		  String desc = "";

          p = ParseTree.createTree(depth);
		  
		  ans = ParseTree.parseToRegex(p);

		  RegexParser.finalResult = "";
		  RegexParser.t = new TreeSet<Character>();

		  String temp =  RegexParser.parse(ans,0);

		  RegexParser.finalResult += temp;
		  
		  if(RegexLibrary.h.get(ans) != null){
			 
			  desc = RegexLibrary.h.get(RegexLibrary.h.get(ans));
		  }
		  desc = "A language starting with \n " + RegexParser.finalResult;
    	  System.out.println(desc);    
		  System.out.println(ans);
		  
		  return ans + "\n\n" + desc;
		
    }
    
    public static String getRE(int level)
    {	
    	ParseTree p = new ParseTree();
         int depth = 0;
		  
		  int diff = level;
		
		  if(diff == 1)
			  depth = 3;
		  else if(diff == 2)
				depth = 4;
		  else
				depth = 5;


		  
		  String ans = "";

          p = ParseTree.createTree(depth);
		  
		  ans = ParseTree.parseToRegex(p);
		  return ans;
    }
	 
	 /*private static String refine(String str)
	 {
	     
	 }*/
}