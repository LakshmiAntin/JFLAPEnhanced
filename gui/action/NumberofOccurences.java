package gui.action;

public class NumberofOccurences extends TreeNode
{

    public NumberofOccurences()
    {
      numArgs = 2;
      args=new TreeNode[numArgs];
      argType=new Class[numArgs];
      
      argType[0] = StringConstants.class;
   	argType[1] = FreeVariable.class;
		
		setClassName("occurs in");
    }
	 
    public  Class returnType()
    {
    
      return Integer.class;
     
    }
     
    public Object evaluate()
    {
      
      String x = (String)args[0].evaluate();
		String y = (String)args[1].evaluate();
		
		int no_occurences = 0;
		int index = -1;
		
		while(index < y.length())
		{
			index = y.indexOf(x, index + 1);
			if(index == -1)
				break;
			no_occurences++;
		}
		
      return new Integer(no_occurences);
    }
        
}