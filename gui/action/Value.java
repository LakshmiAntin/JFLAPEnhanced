package gui.action;
public class Value extends TreeNode
{

   public Value()
   {
      numArgs = 1;
      
      args = new TreeNode[numArgs];
      
      argType = new Class[numArgs];
      
      argType[0] = FreeVariable.class;
      
		setClassName( "val" );
   
      
      
    }
    
   public  Class returnType()
     {
    
      return Integer.class;
     
     }
     
    public Object evaluate()
     {
      
      String x = (String)args[0].evaluate();
		if(x.length() == 0) throw new ArithmeticException();
      int value = 0;
      for(int i = x.length() - 1, j = 0; i >= 0; i--)
      {
         //value += x.charAt(i)*Math.pow(2, j++);
			value += (x.charAt(i)-'0') * Math.pow(2, j++);
      }
      return new Integer(value);
      
     }
           
}