package gui.action;

public class OR extends TreeNode
{

   public OR()
   {
      numArgs=2;
      
      args=new TreeNode[numArgs];
      
      argType=new Class[numArgs];
      
     argType[numArgs-2]=argType[numArgs-1]= Boolean.class;
	  
	  setClassName("OR");
        
   }
    
   public  Class returnType()
   {
    
      return Boolean.class;
     
   }
     
   public Object evaluate()
   {
      
      boolean x =(Boolean)args[0].evaluate();
      boolean y =(Boolean)args[1].evaluate();
		if(!x)
      return new Boolean(x||y);
      return new Boolean(x);
       
   }
       
}
