package gui.action;

public class EndsWith extends TreeNode
{

    public EndsWith()
    {
      numArgs = 2;
      args=new TreeNode[numArgs]; 
      argType=new Class[numArgs];
      
      argType[0] = FreeVariable.class;
   	argType[1] = StringConstants.class;
		
		setClassName("ends with");    
    }
	 
    public  Class returnType()
    {
      return Boolean.class;  
    }
     
    public Object evaluate()
    {
      String x = (String)args[0].evaluate();
		String y = (String)args[1].evaluate();
      return new Boolean(x.endsWith(y));
    }
        
}
