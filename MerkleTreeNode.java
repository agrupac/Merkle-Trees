/**
 * This class is used to represent nodes in the Merkle Tree.
 */
public class MerkleTreeNode{
    /**
    * The parent of a node.
    */
    private MerkleTreeNode parent;
    /**
    * The left child of a node.
    */
    private MerkleTreeNode left;
    /**
    * The right child of a node.
    */
    private MerkleTreeNode right;
    /**
    * The string value of a node.
    */
    private String str;
 /**
    * A default constructor.
    */
	public MerkleTreeNode(){
		parent = null;
        left = null;
        right = null;
        str = null;
	}
 /**
    * A constructor to set fields using arguments.
    * @param parent the parent node.
    * @param left the left child node.
    * @param right the right child node.
    * @param str the value of the node.
    */
	public MerkleTreeNode(MerkleTreeNode parent, MerkleTreeNode left, MerkleTreeNode right, String str){
		this.parent = parent;
        this.left = left;
        this.right = right;
        this.str = str;
	}
 /**
    * A getter for parent node.
    * @return parent the parent node.
    */
	public MerkleTreeNode getParent(){
		return parent;
	}
 /**
    * A getter for left node.
    * @return left the left node.
    */
	public MerkleTreeNode getLeft(){
		return left;
	}
 /**
    * A getter for right node.
    * @return right the right node.
    */
	public MerkleTreeNode getRight(){
		return right;
	}
 /**
    * A getter for node value.
    * @return str the node value.
    */
	public String getStr(){
		return str;
	}
 /**
    * A setter for parent node.
    * @param parent the parent node.
    * @throws IllegalArgumentException if parent is null.
    */
	public void setParent(MerkleTreeNode parent) throws IllegalArgumentException{
		if(parent == null) throw new IllegalArgumentException("Invalid argument.");
        this.parent = parent;
	}
 /**
    * A setter for left child node.
    * @param left the left child node.
    * @throws IllegalArgumentException if left is null.
    */
	public void setLeft(MerkleTreeNode left) throws IllegalArgumentException{
        if(left == null) throw new IllegalArgumentException("Invalid argument.");
        this.left = left;
	}
 /**
    * A setter for right child node.
    * @param right the right child node.
    * @throws IllegalArgumentException if right is null.
    */
	public void setRight(MerkleTreeNode right) throws IllegalArgumentException{
        if(right == null) throw new IllegalArgumentException("Invalid argument.");
        this.right = right;
	}
 /**
    * A setter for node value.
    * @param str the node value.
    * @throws IllegalArgumentException if str is null.
    */
	public void setStr(String str) throws IllegalArgumentException{
        if(str == null) throw new IllegalArgumentException("Invalid argument.");
        this.str = str;
	}

}
