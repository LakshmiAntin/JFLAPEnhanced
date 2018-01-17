package gui;
import java.io.*;
import java.util.TreeSet;

public class RegexParser
{
	public static TreeSet<Character> t = new TreeSet<Character>();
	static int level;
	static int tablevel;
	public static String finalResult = "";
	public static String parse(String regex, int mode)
	{ 
		regex = improve(regex);
		alpha(regex);
		char[] reg = regex.toCharArray();
		regex = "";
		//int inner = 0;
		//int mode = 0;
		int start = -1;
		int end = -1;
		int count = 0;
		//char[] send = null;
		String send = "";
		for(char x:reg)
		{
			if(x == '+' && mode == 0){
				finalResult += (tabs(level)+regex +  "\n" + tabs(level) + "or \n");
				regex = "";
			}
			else if(x == '+' && mode > 0);
			else if(x == '*' && mode == 0 && ((reg.length)-1 == count) && reg[count-1] == ')'){
				finalResult += (regex + "\n" + tabs(level+1) +  " zero or more times");
				regex = "";
			}
			
			else if(x == '*' && mode == 0 && ((reg.length)-1 == count)){
				finalResult += (regex + "\n" + tabs(level) + " zero or more times");
				regex = "";
			}
			else if(x == '*' && mode == 0 && reg[count-1] == ')'){
				finalResult += (tabs(level)+ regex+ "\n" + tabs(level+1) + " zero or more times"  + "\n");
				if((reg.length)-1 != count && reg[count+1] != '+')
				finalResult += ("followed by \n");
				regex = "";
			}
			
			else if(x == '*' && mode == 0){
				finalResult+= (tabs(level)+ regex+" zero or more times"  + "\n");
				if((reg.length)-1 != count && reg[count+1] != '+')
					 finalResult+= ("followed by \n");
				regex = "";
			}
			else if(x == '.' && mode == 0){
				finalResult+= (tabs(level) + regex + "\n" + tabs(level) +"followed by \n");
				regex = "";
			}
			else if(x == '.' && mode > 0);
			else if(x == '*' && mode > 0);
			else if(x == '(' && mode == 0)
			{
				if(count !=0 && isAlpha(reg[count-1])){
					finalResult+= (tabs(level) + regex + "\n" + tabs(level) + "followed by\n");
				}
				start = count;
				tablevel++;
				//System.out.println("Braces start at: " + start);
				mode++;
			}
			else if(x == '(' && mode > 0)
			{
				mode++;
				tablevel++;

			}
			else if(x == ')' && mode > 1)
			{
				mode--;
				tablevel--;

			}
			else if(x == ')' && mode == 1)
			{
				tablevel--;
				send = "";
				end = count;
				//System.out.println("Braces end at: " + end);
				//send = new char[end-start-1];
				for(int i = start  ;  i < end-1  ;  i++)
				{
					//send[i-start] = reg[i+1];
					//System.out.println("Adding to send:bet" + send[i-start] + "ween");
					send += reg[i + 1];
				}
				//System.out.println(send.toString());
				level++;
				regex = parse(send.toString(),mode - 1);
				finalResult+=(tabs(level) +  regex + "\n");
				if(count !=0 && (reg.length - 1) > count && reg[count+1] != '*'){
					finalResult+= ("followed by\n");

				}
				//recentLevel = level;
				level--;
				regex = "";
				mode = 0;
			}
			else if(mode > 0); //regex += x;
			else 
				regex += x;
			count++;
		}
		return regex;
	}
	
	public static void main(String[] args)
	{
		System.out.println("Please enter the regular expression to be parsed!");
		String reg = "";
		try
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			reg = br.readLine();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		System.out.println("A language starting with");
		System.out.print(parse(reg, 0));
	}
	
	private static String tabs(int mode)
	{
		String tab = "";
		for(int i = 1;i <= mode;i++)
			tab += "\t";
		return tab;
	}
	
	private static void print(String regex)
	{
		System.out.println(regex);
	}
	
	static boolean isAlpha(char ch)
	{
   	if((ch>='a'&&ch<='z')||(ch>='A'&&ch<='Z') || (ch>='0'&&ch<='9'))
        return true;
    	else 
        return false;
	}
	
	private static String improve(String regex)
	{
		char[] reg = regex.toCharArray();
		regex = "";
		for(char x:reg)
		{
			if(x == ' ')
				continue;
			regex += x;
		}
		
		reg = regex.toCharArray();
		regex = "";
		for(int i = 0;i<reg.length;i++)
		{
			if(i > 0 && isAlpha(reg[i]) && isAlpha(reg[i-1]))
				regex += '.';
			regex += reg[i];
		}
		
		//System.out.println("String:" + regex);
		return regex;
	}
	
	private static void print(char[] reg)
	{
		System.out.println(reg.toString());
	}
	
	public static void alpha(String regex)
	{
		String finalResul = "";
		//TreeSet<Character> t = new TreeSet<Character>();
		char[] arr = regex.toCharArray();
		
		for(char x:arr)
		{
			if(x != '.' && x != '*' && x != '+' && x != '(' && x != ')')
				t.add(x);
		}
	}
	
	public static String appendInitialString(){
       String finalResul = "This is a language over the strings" + " {";
		for(char x:t)
		{
			finalResul += x +", ";
		}
		t = new TreeSet<Character>();
		finalResul = finalResul.substring(0, finalResul.length()-2);
		
		finalResul += "}.\n\nIt consists of\n";
		return finalResul;
	}

	
	/*private static char[] toCharArray(String regex)
	{
		int len = regex.length();
		char[] reg = new char[len];
		for(int i = 0;i < len; i++)
		{
			reg[i] = regex.charAt(i);
		}
		return reg;
	}*/
}