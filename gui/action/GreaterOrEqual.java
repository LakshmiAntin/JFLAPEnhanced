package gui.action;
public class GreaterOrEqual extends TreeNode
{

   public GreaterOrEqual()
   {
      numArgs=2;
   
      args=new TreeNode[numArgs];
      
      argType=new Class[numArgs];
      
      argType[numArgs-2]=argType[numArgs-1]= Integer.class;
      
     setClassName("is greater than or equal to"); 
      
    }
    
   public  Class returnType()
    {
    
      return  Boolean.class;
     
     }
     
     public Object evaluate()
     {
      
      int x=(Integer)args[0].evaluate();
      int y=(Integer)args[1].evaluate();
      return new Boolean(x >= y);
      
       
     }
         
}
