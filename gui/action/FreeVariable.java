package gui.action;
public class FreeVariable extends TreeNode
{
   public FreeVariable()
   {
     numArgs = 0;
     args = null;
	  setClassName("x");
   }

   public void setName(String str)
   {
      className = str;
   }
   
   public Class returnType()
   {
      return String.class;
   }
   
   @Override
   public String toString() { return "x"; }
   public Object evaluate() { return className; }
   
}