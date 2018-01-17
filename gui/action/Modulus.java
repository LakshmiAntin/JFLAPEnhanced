
package gui.action;
public class Modulus extends TreeNode
{

   public Modulus()
   {
      numArgs = 2;
      
      args = new TreeNode[numArgs];
      
      argType = new Class[numArgs];
      
      argType[numArgs-2] = argType[numArgs-1] = Integer.class;
      
		setClassName( "mod" );
   
      
      
    }
    
   public  Class returnType()
     {
    
      return Integer.class;
     
     }
     
    public Object evaluate()
     {
      
      int x = (Integer)args[0].evaluate();
      int y = (Integer)args[1].evaluate();
      return new Integer( x % y );
      
     }
           
}
