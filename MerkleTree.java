import java.util.*;
/**
* This class represents a complete MerkleTree.
*/
public class MerkleTree {
	/**
    * The root node of the tree.
    */
	public static MerkleTreeNode root;
	/**
    * The number of files (leaves) in a tree.
    */
	public static int numberOfFiles;
    /**
    * The leaves of the tree.
    */
    public static ArrayList<MerkleTreeNode> leaves;
	/**
    * All the parents of a given row in the tree. For use in the constructMerkleTree() method.
    */
	private static ArrayList<MerkleTreeNode> parents;
	/**
    * All the children of a given row in the tree. For use in the constructMerkleTree() method.
    */
	private static ArrayList<MerkleTreeNode> children;
	/**
    * Generates an entire Merkle tree from given files.
	* @param files the values for leaf nodes of the tree.
	* @return root.getStr() the value of the root node.
	* @throws IllegalArgumentException if files array is null.
    */
	public String constructMerkleTree(String[] files) throws IllegalArgumentException{
		//if files is empty or length is not a number divisible by 2 other than 1
		if(files == null || (files.length != 1 && files.length % 2 != 0) ) throw new IllegalArgumentException("Invalid argument.");

		numberOfFiles = files.length;
		leaves = new ArrayList<MerkleTreeNode>();
		parents = new ArrayList<MerkleTreeNode>();
		//generate leaves
		for(int i = 0; i < numberOfFiles; i++){
			MerkleTreeNode node = new MerkleTreeNode();
			node.setStr(files[i]);
			leaves.add(node);
		}
		//if there is only one file, root = leaf
		if(leaves.size() == 1){
			root = leaves.get(0);
		}
		//otherwise generate parents until root is reached
		else{
			children = new ArrayList<MerkleTreeNode>(leaves);
			while(parents.size() != 1){
				parents.clear();
				for(int j = 0; j < children.size(); j = j + 2){
					MerkleTreeNode parent = new MerkleTreeNode(null, children.get(j), children.get(j + 1), Hashing.cryptHash(children.get(j).getStr() + children.get(j + 1).getStr()));
					children.get(j).setParent(parent);
					children.get(j + 1).setParent(parent);
					parents.add(parent);
				}
				children = new ArrayList<MerkleTreeNode>(parents);
			}
			root = parents.get(0);
			parents.clear();
		}

		return root.getStr();
	}
	/**
    * Verifies that a given file of the tree has not been modified.
	* @param rootValue a root hash to compare with the tree's root hash.
	* @param fileIndex the index of the file to verify.
	* @param file the file value to use in rehashing of the tree.
	* @return false if the hash of a node and its sibling does not equal the value of either its parent or uncle node, true otherwise.
	* @throws IllegalArgumentException if any parameter is null or fileIndex is out of bounds.
    */
	public static boolean verifyIntegrity(String rootValue, int fileIndex, String file) throws IllegalArgumentException{
		//if a value is null or index is out of bounds
		if(rootValue == null || file == null || fileIndex < 0 || fileIndex >= numberOfFiles) throw new IllegalArgumentException("Invalid argument.");
		//if root value has been changed
		if(rootValue != root.getStr()) return false;

		MerkleTreeNode currentNode = leaves.get(fileIndex);
		MerkleTreeNode sibling;
		MerkleTreeNode uncle;
		boolean leaf = true;
		String nodeAndSibling;

		while(currentNode.getParent() != null){
			//locate sibling node
			//if currentNode is left child
			if(currentNode.equals(currentNode.getParent().getLeft())){
				sibling = currentNode.getParent().getRight();
				//if currentNode is a leaf
				if(leaf){
					nodeAndSibling = file + sibling.getStr();
				}
				else{
					nodeAndSibling = currentNode.getStr() + sibling.getStr();
				}
			}
			//if currentNode is right child
			else{
				sibling = currentNode.getParent().getLeft();
				//if currentNode is a leaf
				if(leaf){
					nodeAndSibling = sibling.getStr() + file;
				}
				else{
					nodeAndSibling = sibling.getStr() + currentNode.getStr();
				}
			}
			//locate uncle node
			//if parent is root
			if(currentNode.getParent().getParent() == null){
				uncle = null;
			}
			//if parent is left child of its parent
			else if(currentNode.getParent().equals(currentNode.getParent().getParent().getLeft())){
				uncle = currentNode.getParent().getParent().getRight();
			}
			//if parent is right child of its parent
			else{
				uncle = currentNode.getParent().getParent().getLeft();
			}
			//check if hash of node and sibling is equal to parent string or uncle string
			//if parent is root
			if(uncle == null){
				if(!currentNode.getParent().getStr().equals(Hashing.cryptHash(nodeAndSibling))){
					return false;
				}
			}
			//if parent is not root
			else{
				if(!currentNode.getParent().getStr().equals(Hashing.cryptHash(nodeAndSibling)) && !uncle.getStr().equals(Hashing.cryptHash(nodeAndSibling))){
					return false;
				}
			}
			//move up the tree
			currentNode = currentNode.getParent();
			leaf = false;
		}

		return true;
	}
	/**
    * Swaps the locations of two nodes at the two indicies.
	* @param fileIndex1 the first leaf index.
	* @param fileIndex2 the second leaf index.
	* @return root.getStr() the new root hash after rehashing the tree.
	* @throws IllegalArgumentException if indicies are out of bounds.
    */
	public String swapFile(int fileIndex1, int fileIndex2) throws IllegalArgumentException{
		//if either index is out of bounds
		if(fileIndex1 < 0 || fileIndex2 < 0 || fileIndex1 > numberOfFiles - 1 || fileIndex2 >= numberOfFiles) throw new IllegalArgumentException("Invalid argument.");

		MerkleTreeNode temp = leaves.get(fileIndex1);
		leaves.set(fileIndex1, leaves.get(fileIndex2));
		leaves.set(fileIndex2, temp);

		String[] newleaves = new String[leaves.size()];
		for(int i = 0; i < newleaves.length; i++){
			newleaves[i] = leaves.get(i).getStr();
		}

		constructMerkleTree(newleaves);

		return root.getStr();
	}
	/**
    * Recursively travels the tree and adds node values to a given arraylist at each level from left to right. Used in convertToDynamic() method.
	* @param node the root of the tree/subtree.
	* @param depth the current depth of the root.
	* @param list where node values are stored.
    */
	private static void levelTraversal(MerkleTreeNode node, int depth, ArrayList<String> list){
		if(depth == 1){
			list.add(node.getStr());
		}
		else if(depth > 1){
			levelTraversal(node.getLeft(), depth - 1, list);
			levelTraversal(node.getRight(), depth - 1, list);
		}
		return;
	}
	/**
    * Finds the depth of the tree based on the root node. Used in convertToDynamic() method.
	* @param node the root node.
	* @return depth the depth of the tree.
    */
	private static int getTreeDepth(MerkleTreeNode node){
		int depth = 1;
		while(node.getLeft() != null){
			depth++;
			node = node.getLeft();
		}
		return depth;
	}
    /**
    * Converts Merkle tree to dynamic array representation.
	* @return tree the tree as an arraylist.
    */
    public static ArrayList<String> convertToDynamic(){
		ArrayList<String> tree = new ArrayList<String>();

		for(int i = 1; i <= getTreeDepth(root); i++){
			levelTraversal(root, i, tree);
		}

		return tree;
    }
    /**
    * Verifies that a given file of the array form of the tree has not been modified.
	* @param rootValue a root hash to compare with the array's root hash.
	* @param fileIndex the index of the file to verify.
	* @param file the file value to use in rehashing of the array.
	* @param dynamicMerkle the tree as an array.
	* @return false if the hash of a node and its sibling does not equal the value of either its parent or uncle node, true otherwise.
	* @throws IllegalArgumentException if any parameter is null or fileIndex is out of bounds.
    */
    public static boolean verifyIntegrityDynamic(String rootValue, int fileIndex, String file, ArrayList<String> dynamicMerkle) {
		//if any value is null or index is out of bounds
		if(rootValue == null || file == null || fileIndex < 0 || fileIndex >= numberOfFiles || dynamicMerkle == null) throw new IllegalArgumentException("Invalid argument.");
		//if root value has been changed
		if(rootValue != dynamicMerkle.get(0)) return false;

		int index = fileIndex + numberOfFiles - 1;
		String sibling;
		String uncle;
		boolean leaf = true;
		String nodeAndSibling;

		while(index != 0){
			//locate sibling node
			//if currentNode is left child
			if(index % 2 != 0){
				sibling = dynamicMerkle.get(index + 1);
				//if currentNode is a leaf
				if(leaf){
					nodeAndSibling = file + sibling;
				}
				else{
					nodeAndSibling = dynamicMerkle.get(index) + sibling;
				}
			}
			//if currentNode is right child
			else{
				sibling = dynamicMerkle.get(index - 1);
				//if currentNode is a leaf
				if(leaf){
					nodeAndSibling = sibling + file;
				}
				else{
					nodeAndSibling = sibling + dynamicMerkle.get(index);
				}
			}
			//locate uncle node
			//if parent is root
			if((index - 1) / 2 == 0){
				uncle = null;
			}
			//if parent is left child of its parent
			else if(((index - 1) / 2) % 2 != 0){
				uncle = dynamicMerkle.get((index - 1) / 2 + 1);
			}
			//if parent is right child of its parent
			else{
				uncle = dynamicMerkle.get((index - 1) / 2 - 1);
			}
			//check if hash of node and sibling is equal to parent string or uncle string
			//if parent is root
			if(uncle == null){
				if(!dynamicMerkle.get((index - 1) / 2).equals(Hashing.cryptHash(nodeAndSibling))){
					return false;
				}
			}
			//if parent is not root
			else{
				if(!dynamicMerkle.get((index - 1) / 2).equals(Hashing.cryptHash(nodeAndSibling)) && !uncle.equals(Hashing.cryptHash(nodeAndSibling))){
					return false;
				}
			}
			//move up the tree
			index = (index - 1) / 2;
			leaf = false;
		}

		return true;

	}
}
