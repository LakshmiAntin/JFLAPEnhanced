package gui.action;

import java.io.*;
import java.util.*;

public class StringGen 
{
	public static String[] printAllBinary(final int length)
	{
		if (length > 63)
		{
			throw new IllegalArgumentException("Current implementation supports only length < 63");
		}
		//System.out.println((2^length));
		int pow = (int)Math.pow(2,length);
		String[] str = new String[pow];

		final long max = 1 << length;

		for (int i = 0;i<max;++i)
		{
			long currentNumber = i;
			final char[] buffer = new char[length];
			int bufferPosition = buffer.length;

			while(bufferPosition > 0)
			{
				buffer[--bufferPosition] = (char) (48 + (currentNumber & 1));
				currentNumber >>>=1;
			}
			
			str[i] = new String(buffer);
			//System.out.println(str[i]);
			//str += (i<max-1?"\n":"");
		}
		//String name = str.getClass().getName();
		//System.out.println(name);
		
		return str;
	}

	/*public static void main(String[] args) 
	{
		int length = 0;
		try
		{
			length = Integer.parseInt(args[0]);
		}
		catch (ArrayIndexOutOfBoundsException ae) 
		{
			System.err.println("No arguments supplied!"); 	
		}
		catch (NumberFormatException ne)
		{
			System.err.println("Enter only numbers!");
		}
		int pow = (int)Math.pow(2,length);
		String[] input = new String[pow];

		for (int i = 0;i <= length;++i)
		{
			input = printAllBinary(i);
			int pow1 = (int)Math.pow(2,i);
			
			for(int j = 0; j < pow1; ++j)
			{
				
				System.out.println(input[j]);
			}
		} */
}