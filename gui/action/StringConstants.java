package gui.action;

public class StringConstants extends TreeNode
{
private String string_argument;

   public StringConstants(String str)
   {
      string_argument = str;
      setClassName("\"" + string_argument + "\"");
      args = new TreeNode[0];  
      argType = new Class[0];
      
    }
    
   public  Class returnType()
    {
    
      return String.class;
     
     }
     
     public Object evaluate()
     {
      	return new String(string_argument);
     }
     
     public void setArg(String str)
     {
      	string_argument = str;
     }     
}
