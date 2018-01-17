package Solution;

import java.util.*;
import java.lang.*;
import java.io.*;

public class SolutionBase
{


public static boolean inLanguage(String x)
{
	return false;
}
public static void main(String [] args) {
	String input = "";
	if(args.length > 0) input = args[0];
	int answer = 2;
	try{
		answer = (inLanguage(input) ? 0 : 1);
	} catch(Exception e) { }
	System.exit(answer);
}


}
