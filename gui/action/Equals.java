package gui.action;
public class Equals extends TreeNode
{

   public Equals()
   {
      numArgs=2;
   
      args=new TreeNode[numArgs];
      
      argType=new Class[numArgs];
      
      argType[numArgs-2]=argType[numArgs-1]= Integer.class;
      
     setClassName("is equal to"); 
      
    }
    
   public  Class returnType()
    {
    
      return  Boolean.class;
     
     }
     
     public Object evaluate()
     {
      
      int x=(Integer)args[0].evaluate();
      int y=(Integer)args[1].evaluate();
      return new Boolean(x==y);
      
       
     }
         
}
