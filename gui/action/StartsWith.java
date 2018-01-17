package gui.action;

public class StartsWith extends TreeNode
{

    public StartsWith()
    {
      numArgs = 2;
    
      args=new TreeNode[numArgs];
      
      argType=new Class[numArgs];
      
      argType[0] = FreeVariable.class;
   	argType[1] = StringConstants.class;
		
		setClassName("starts with");    
    }
	 
    public  Class returnType()
    {
    
      return Boolean.class;
     
    }
     
    public Object evaluate()
    {
      
      String x = (String)args[0].evaluate();
		String y = (String)args[1].evaluate();
      return new Boolean(x.startsWith(y));
    }
        
}
