package gui.action;
public abstract class TreeNode
{

   protected TreeNode parent;
	protected TreeNode args[];
	protected Class argType[];
   protected String className;
   protected int numArgs;
   protected int countNotNull = 0;
   
   public void setClassName(String name)
	{
      className = name;
   }

   public String getClassName()
   {
      return className;
   }
  @Override
	public String toString()
	{
 		 String ans = className;
  		 if(numArgs > 0)
		 {
	    	ans += "(" + args[0].toString();
	    	for(int i = 1; i < args.length; i++)
	      ans += ", " + args[i].toString();
	    	ans += ")";
		 }
 		 return ans;
	}
  
   public TreeNode getFirstNullArg()
	{
      for(int i = 0; i < args.length; i++)
		{
         if(args[i] == null)
			{ 
            return args[i];
			}
      }
      return null;
   }

   public void  setFirstNullArg(TreeNode tnode)
   {
      for(int i = 0; i<args.length; i++)
      {
         if ( this instanceof Modulus && i == 1 )
         {
            if( tnode instanceof IntegerConstants )
            {
                int check = (Integer)tnode.evaluate();
                
                if( check == 0 )
                {
                  throw new RuntimeException();
                }
              }
            }                
         if(args[i] == null)
         {
            args[i] = tnode;
            break;
         }
      }
   }
	
   public Class nextNullArgType()
   {
		if(argType != null)
		{
         for(int i = 0; i < argType.length ;i++)
         {
            if(args[i] == null)
               return argType[i];
         }
		}	
		return null;
   }
	
   public void setParent(TreeNode tnode)
   {
      parent = tnode;
   }

   public TreeNode getParent()
   {
      return parent;
   }
	
	public int getNumArgs()
	{
		return numArgs;
	}

   public abstract Object evaluate();

   public abstract Class  returnType();

}