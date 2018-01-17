package gui.action;
public class studentCode
{
   private TreeNode root;
   private FreeVariable v;
   private Class classType;
	private TreeNode parent;

   public studentCode()
   {
      v = new FreeVariable();
      classType = Integer.class;
      root = null;
		parent = null;
   }
   
   public Boolean inLanguage(String x)
   {
		v.setName(x);
      return ((Boolean)root.evaluate());
   }

   public Class buildTree(TreeNode tnode)
   {	
		if(tnode instanceof FreeVariable)
		{
			
        		 tnode = v;
		}
		
		if(parent == null) // should only be true right in the beginning
		{
			root = tnode;
		}
		tnode.setParent(parent);
		if(parent!=null)
		{
			parent.setFirstNullArg(tnode);
		}
		parent = tnode;
		while(parent != null && parent.nextNullArgType() == null)
		{
			parent = parent.getParent();
		}
		if(parent == null)
		{
			return null; // tree is fully built!! :)
		}
		return parent.nextNullArgType();
   }

   public TreeNode getRoot()
	{
		return root;
	}
	public void setRoot()
	{
		root = null;
	}
}