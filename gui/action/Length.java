package gui.action;

public class Length extends TreeNode
{

    public Length()
    {
      numArgs = 1;
    
      args=new TreeNode[numArgs];
      
      argType=new Class[numArgs];
      
      argType[0]=FreeVariable.class;
   	
		setClassName("length");
      
    }
    public void argument(TreeNode a)
    {
      
      args[0]=a;
    
    }
    
    public  Class returnType()
    {
    
      return Integer.class;
     
    }
     
    public Object evaluate()
    {
      
      String x=(String)args[0].evaluate();
      
      return new Integer(x.length());
             
      
    }
        
}
