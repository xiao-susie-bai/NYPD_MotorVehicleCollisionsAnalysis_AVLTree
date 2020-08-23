package nypdproject;
import java.util.ArrayList;


/**
 * this class uses the AVL tree structure to store all the Collisions object.
 * 
 * @author baixiao
 *@version Dec. 8th, 2017
 */
public class CollisionsData {     
	//Nested Node <Collision> class(suitable for AVL Tree)
	/**
	 * this is a nested class which defines what a binary search tree node would be like in an AVL tree
	 * implements Comparable<BSTNode> interface
	 * @author baixiao
	 *
	 */
	protected static class Node<Collision extends Comparable<Collision>> implements Comparable<Node<Collision>> {
		protected Node<Collision>  left;      //**reference to the left subtree 
		protected Node<Collision> right;     //**reference to the right subtree
		protected Collision data;       //Collision data item stored in the node
		protected int height;          //Must have this data field to be prepared for AVL
		
		/**
		 * this is the default constructor of a node used in the AVL tree
		 */
		protected Node (Collision data) {
			this.data = data;
			this.left = null;
			this.right = null;
			this.height = 0;           //default height to be 0(leaf)
		}
		
		/**
		 * getter of the value stored in the node
		 * @return this.data   Collision type
		 */
		public Collision getData() {
			return this.data;
		}
		/**
		 * getter of the left child of the node
		 * @return this.left      Node type
		 */
		public Node<Collision> getLeft() {
			return this.left;
		}
		/**
		 * getter of the right child of the node
		 * @return this.right      Node type
		 */
		public Node<Collision> getRight() {
			return this.right;
		}
		/**
		 * getter of the height of the node
		 * @return this.height     int type
		 */
		public int getHeight() {
			return this.height;
		}
		
		/**
		 * setter of the height of the node
		 * @param h     int type
		 */
		public void setHeight(int h) {
			this.height = h;
		}
		
		/**
		 * setter of the value stored in the node
		 * @param otherData     Collision type
		 */
		public void setData(Collision otherData) {
			this.data = otherData;
		}
		
		/**
		 * setter of the left child of the node
		 * @param newLeft      Node type
		 */
		public void setLeft(Node<Collision> newLeft) {
			this.left = newLeft;
		}
		
		/**
		 * setter of the right child of the node
		 * @param newRight     Node type
		 */
		public void setRight(Node<Collision> newRight) {
			this.right = newRight;
		}
		
		/**
		 * this method is implemented to satisfy requirement of the Comparable interface 
		 * @param o       Node type
		 * @return int   positive if the value stored in the current code is bigger than than the compared node;
		 *               negative 
		 */
		@Override
		public int compareTo(Node<Collision> o) {
			return this.getData().compareTo(o.getData());
		}
		
		/**
		 * this method overrides the toString method in the Object class
		 * use the string representation of its stored value
		 */
		public String toString() {
			return this.data.toString();
		}
		
	}
	
	protected Node<Collision> root;          
	protected int numOfElements;         //record "size"
	private boolean found;              //helper used in "remove" method
	
	private int countTotalCollisions = 0;
	private int countTotalFatalities = 0;
	private int countTotalInjuries = 0;
	private int countPedDeath = 0;
	private int countPedInjury = 0;
	private int countCycDeath = 0;
	private int countCycInjury = 0;
	private int countMotorDeath = 0;
	private int countMotorInjury = 0;

	/**
	 * Default constructor that creates an empty tree.
	 */
	public CollisionsData() {
		this.root = null;
		this.numOfElements = 0;
	}
	
	/**
	 * Determines the number of elements stored in this BST.
	 * 
	 * @return number of elements in this BST
	 */
	public int size() {
		return this.numOfElements;
	}
	
	
	//private utility methods(encapsulation of specific functionality)
	//*The following one is important! Calculate the balance factor!!!
	/**
	 * calculate the balance factor of a node
	 * @param n    Node type
	 * @return int   equals the height of the right tree minus that of the left tree
	 */
	private int balanceFactor(Node <Collision> n) {
		//＊＊＊＊＊＊原细节错误：“base case哪边是空的对应哪种height"正负"一定要搞清楚！！不要粗心！！
		if (n.left==null) {
			return (n.getHeight());       
		} 
		if (n.right==null) {
			return (-n.getHeight());       
		}
		return (n.right.getHeight()-n.left.getHeight());
	}
	
	//private utility method: update the height of a node
	/**
	 * update the height of a node
	 * @param n  Node type
	 */
	private void updateHeight(Node <Collision> n) {
		//*Key idea: discuss the different scenarios of "n's children"("height" here heavily depends on the node's children)!!!
		if (n.left==null && n.right==null) {
			n.setHeight(0);
		} else if (n.left==null) {
			n.setHeight(n.right.getHeight()+1);
		} else if (n.right==null) {
			n.setHeight(n.left.getHeight()+1);
		} else {
			n.setHeight(Math.max(n.left.getHeight(), n.right.getHeight())+1);
		}
	}
	
	//Four types of Rotations:
	//Firstly, LL Rotation:
	/**
	 * execute the LL rotation if the node as the root of a (sub)tree is unbalanced
	 * @param subroot       Node type
	 * @return Node   the updated root of the (sub)tree
	 */
	private Node <Collision> balanceLL(Node <Collision> subroot) {
		Node <Collision> l = subroot.getLeft();
		subroot.setLeft(l.getRight());
		l.setRight(subroot);
		updateHeight(subroot);
		updateHeight(l);
		return l;
	}
	
	
	//Secondly, RR Rotation:
	/**
	 * execute the RR rotation if the node as the root of a (sub)tree is unbalanced
	 * @param subroot       Node type
	 * @return Node   the updated root of the (sub)tree
	 */
	private Node <Collision> balanceRR(Node <Collision> subroot) {
		Node <Collision> r = subroot.getRight();
		subroot.setRight(r.getLeft());
		r.setLeft(subroot);
		//update height again
		updateHeight(subroot);
		updateHeight(r);
		return r;
	}
	
	//Thirdly, LR Rotation:
	/**
	 * execute the LR rotation if the node as the root of a (sub)tree is unbalanced
	 * @param grandparent     Node type
	 * @return Node   the updated root of the (sub)tree
	 */
	private Node <Collision> balanceLR(Node <Collision> grandparent) {
		Node <Collision> parent = grandparent.getLeft();
		Node <Collision> child = parent.getRight();
		
		parent.setRight(child.getLeft());          //first-stage rotation
		child.setLeft(parent);
		grandparent.setLeft(child.getRight());      //second-stage rotation
		child.setRight(grandparent);
		//update height again
		updateHeight(grandparent);
		updateHeight(parent);
		updateHeight(child);
		return child;                //child now becomes the new root of the subtree we are looking at
	}
	
	//Fourthly, RL Rotation:
	/**
	 * execute the RL rotation if the node as the root of a (sub)tree is unbalanced
	 * @param grandparent   Node type
	 * @return Node   the updated root of the (sub)tree
	 */
	private Node <Collision> balanceRL(Node <Collision> grandparent) {
		Node <Collision> parent = grandparent.getRight();
		Node <Collision> child = parent.getLeft();
		
		parent.setLeft(child.getRight());
		child.setRight(parent);
		grandparent.setRight(child.getLeft());
		child.setLeft(grandparent);
		//update height again
		updateHeight(grandparent);
		updateHeight(parent);
		updateHeight(child);
		return child;               //child now becomes the new root of the subtree we are looking at
	}
	

	/**
	 * check the balance factor of the node and decide which rotation to use based on the different cases of balance factors.
	 * @param node    Node type
	 * @return Node    the updated root of the (sub)tree
	 */
	private Node<Collision> balance(Node<Collision> node) {
		if (balanceFactor(node)==-2) {      //left subtree is heavier
			//following: originally "balanceFactor(node.getLeft())<=0"
			if (balanceFactor(node.getLeft())==-1 || balanceFactor(node.getLeft())==0) {        //still left is heavier(or equal)
				node = balanceLL(node);      //APPLY LL ROTATION
			//following: originally just "else"
			} else if (balanceFactor(node.getLeft())==1) {                        //right is heavier
				node = balanceLR(node);      //APPLY LR ROTATION
			}
		} else if (balanceFactor(node)==2) {         //"balanceFactor(node)==2":         //right subtree is heavier
			if (balanceFactor(node.getRight())==1 || balanceFactor(node.getRight())==0) {        //still right is heavier(or equal)
				node = balanceRR(node);       //APPLY RR ROTATION
			} else if (balanceFactor(node.getRight())==-1){
				node = balanceRL(node);       //APPLY RL ROTATION
			}
		}
		return node;          //return the updated node
	}
	
	/**
	 * Add the given data item to the tree. If item is null, the tree does not
	 * change. If item already exists, the tree does not change. 
	 * 
	 * @param item the new element to be added to the tree
	 */
	public void add(Collision item) {
		if (item == null)
			return;
		root = add (root, item);
		
	}
	/**
	 * Actual recursive implementation of add.  
	 * 
	 * @param newData  the new element to be added to the tree
	 * @param node    the root of the (sub)tree where the new element intended to be added
	 * @return Node    the updated root of the (sub)tree
	 */
	private Node<Collision> add(Node<Collision> node, Collision newData) {
		if (node == null) {              //hit "base case" here(actual position to add the new node)
			this.numOfElements++;
			return new Node <Collision>(newData);
		}
		if (node.data.compareTo(newData) > 0)
			node.setLeft(add(node.getLeft(), newData));        //recursive call
		else if (node.data.compareTo(newData) < 0)
			node.setRight(add(node.getRight(), newData));
		//(if the newData is the same as the node's data, do nothing)
		updateHeight(node);
		if (balanceFactor(node)<-1 || balanceFactor(node)>1) {
			node = balance(node);
		}
		return node;          //return the updated node
	}
	

	/**
	 * Remove the item from the tree. If item is null the tree remains unchanged. If
	 * item is not found in the tree, the tree remains unchanged.
	 * 
	 * @param target the item to be removed from this tree 
	 */
	public boolean remove(Collision target) {
		root = recRemove(target, root);
		return found;
	}


	/**
	 * 
	 * Actual recursive implementation of remove method: find the node to remove.  
	 * 
	 * @param target the item to be removed from this tree 
	 * @param node    the root of the (sub)tree where the new element intended to be added
	 * @return Node    the update root of the (sub)tree
	 */
	private Node<Collision> recRemove(Collision target, Node <Collision> node) {
		if (node == null)
			found = false;
		else if (target.compareTo(node.data) < 0)
			node.left = recRemove(target, node.left);
		else if (target.compareTo(node.data) > 0)
			node.right = recRemove(target, node.right );
		else {
			node = removeNode(node);
			found = true;
			this.numOfElements--;
		}
		updateHeight(node);
		if (balanceFactor(node)<-1 || balanceFactor(node)>1) {
			balance(node);
		}
		return node;
	}
	
	/**
	 * Actual recursive implementation of remove method: perform the removal.  
	 * 
	 * @param node  the node to be removed from this tree 
	 * @return a reference to the node itself, or to the modified subtree 
	 */
	private Node<Collision> removeNode(Node<Collision> node) {
		Collision data;
		if (node.left == null)
			return node.right ;
		else if (node.right  == null)
			return node.left;
		else {
			data = getPredecessor(node.left);
			node.data = data;
			node.left = recRemove(data, node.left);
			return node;                //return the updated node
		}
	}
	
	/**
	 *Returns the information held in the rightmost node of subtree(not the "left subtree" rooted at "subtree" here, so you need to pass in "the left child of the node" later 
	 * 
	 * @param subtree root of the subtree within which to search for the rightmost node 
	 * @return Collision data stored in the rightmost node of subtree  
	 */
	private Collision getPredecessor(Node<Collision> subtree) {
		if (subtree==null) throw new NullPointerException("getPredecessor called with an empty subtree");
		Node<Collision> temp = subtree;
		while (temp.getRight() != null)
			temp = temp.getRight() ;
		return temp.getData();
	}
	
	
	/**
	 * Produces tree like string representation of this BST.
	 * @return string containing tree-like representation of this BST.
	 */
	public String toStringTreeFormat() {

		StringBuilder s = new StringBuilder();

		preOrderPrint(root, 0, s);
		return s.toString();
	}

	/**
	 * Actual recursive implementation of preorder traversal to produce tree-like string
	 * representation of this tree.
	 * 
	 * @param tree the root of the current subtree
	 * @param level level (depth) of the current recursive call in the tree to
	 *   determine the indentation of each item
	 * @param output the string that accumulated the string representation of this
	 *   BST
	 */
	private void preOrderPrint(Node<Collision> tree, int level, StringBuilder output) {
		if (tree != null) {
			String spaces = "\n";
			if (level > 0) {
				for (int i = 0; i < level - 1; i++)
					spaces += "   ";
				spaces += "|--";
			}
			output.append(spaces);
			output.append(tree.data);      //PREORDER: first visit the subroot itself
			preOrderPrint(tree.left, level + 1, output);        //PREORDER: then visit the subroot's two children
			preOrderPrint(tree.right , level + 1, output);
		}
		// uncomment the part below to show "null children" in the output
		else {
			String spaces = "\n";
			if (level > 0) {
				for (int i = 0; i < level - 1; i++)
					spaces += "   ";
				spaces += "|--";
			}
			output.append(spaces);
			output.append("null");
		}
	}
	
	//additional methods for visualization:
	/**
	 * Returns a string representation of this tree using an inorder traversal . 
	 * @see java.lang.Object#toString()
	 * @return string representation of this tree 
	 */
	public String toString() {
		StringBuilder s = new StringBuilder();
		inOrderPrint(root, s);
		return s.toString();
	}
	/**
	 * Actual recursive implementation of inorder traversal to produce string
	 * representation of this tree.
	 * 
	 * @param tree the root of the current subtree
	 * @param s the string that accumulated the string representation of this BST
	 */
	private void inOrderPrint(Node <Collision> tree, StringBuilder s) {
		if (tree != null) {
			inOrderPrint(tree.left, s);        //INORDER: visit the left subtree first!!
			s.append(tree.data.toString() + "  ");     //INORDER: the visit 
			inOrderPrint(tree.right , s);
		}
	}
	
	
	/**
	 * public method to search for nodes in accord with the specified zip code and date range, then generate a report based on the results found
	 * @param zip   String type
	 * @param dateBegin     Date type
	 * @param dateEnd       Date type
	 * @return String   a string representation of the results(fatalities, injuries, etc.) found
	 */
	public String getReport(String zip, Date dateBegin, Date dateEnd) {
		//***Order defined in Collision class: "zip" comes first, then compare "date", "key" comes last
		String result = "Motor Vehicle Collisions for zipcode ";
		result += zip + " (" + dateBegin.toString() + " - " + dateEnd.toString() + ")\n";
		result += "======================================================================\n";
		//binary search the desired nodes through the entire tree(called the private utility method performing the specific task)
		//*****VERY IMPORTANT: first clear up all the internal data fields that work as counters for each search!!!
		
		countTotalCollisions = 0;
		countTotalFatalities = 0;
		countTotalInjuries = 0;
		countPedDeath = 0;
		countPedInjury = 0;
		countCycDeath = 0;
		countCycInjury = 0;
		countMotorDeath = 0;
		countMotorInjury = 0;
		
		binarySearchCollisions(this.root, zip, dateBegin, dateEnd);
		result += "Total number of collisions: "+Integer.toString(this.countTotalCollisions)+"\n";
		result += "Number of fatalities: "+Integer.toString(this.countTotalFatalities)+"\n";
		result += "         pedestrians: "+Integer.toString(this.countPedDeath)+"\n";
		result += "            cyclists: "+Integer.toString(this.countCycDeath)+"\n";
		result += "           motorists: "+Integer.toString(this.countMotorDeath)+"\n";
		result += "Number of injuries: "+Integer.toString(this.countTotalInjuries)+"\n";
		result += "       pedestrians: "+Integer.toString(this.countPedInjury)+"\n";
		result += "          cyclists: "+Integer.toString(this.countCycInjury)+"\n";
		result += "         motorists: "+Integer.toString(this.countMotorInjury)+"\n";
		return result;
	}
	
	//*****private utility method for search the appropriate entries in accord with the specified zip code and date range
	/**
	 * private method to perform the recursive binary search in order to find the desired node(s) and accumulate the counters
	 * @param node  the current root of the (sub)tree we are looking at
	 * @param zip   String type  desired zip code
	 * @param dateBegin  Date type    desired starting date
	 * @param dateEnd    Date type    desired ending date
	 */
	public void binarySearchCollisions(Node<Collision> node, String zip, Date dateBegin, Date dateEnd) {
		if (node==null) return;         //base case
		if (node.getData().getZip().compareTo(zip)<0) {
			binarySearchCollisions(node.getRight(), zip, dateBegin, dateEnd);
		} else if (node.getData().getZip().compareTo(zip)>0) {
			binarySearchCollisions(node.getLeft(), zip, dateBegin, dateEnd);
		} else {                //zip matches!
			if (node.getData().getDate().compareTo(dateBegin)<0) {
				binarySearchCollisions(node.getRight(), zip, dateBegin, dateEnd);
			} else if (node.getData().getDate().compareTo(dateEnd)>0) {
				binarySearchCollisions(node.getLeft(), zip, dateBegin, dateEnd);
			} else {               //date exactly matches
				//****accumulate the counter data fields internally now
				this.countTotalCollisions++;
				this.countTotalFatalities += node.getData().getPersonsKilled();
				this.countTotalInjuries += node.getData().getPersonsInjured();
				this.countPedDeath += node.getData().getPedestriansKilled();
				this.countPedInjury += node.getData().getPedestriansInjured();
				this.countCycDeath += node.getData().getCyclistsKilled();
				this.countCycInjury += node.getData().getCyclistsInjured();
				this.countMotorDeath += node.getData().getMotoristsKilled();
				this.countMotorInjury += node.getData().getMotoristsInjured();
				
				//CONTINUE EXPLORING THE TWO SIDES AFTER FINDING AN APPROPRIATE NODE
				binarySearchCollisions(node.getLeft(), zip, dateBegin, dateEnd);
				binarySearchCollisions(node.getRight(), zip, dateBegin, dateEnd);
				
			}
		}
		
	}
	
	/*
	//debug
	public static void main(String[] args) {
		ArrayList<String> me= new ArrayList<>();
		me.add("11/1/2017");
		me.add("0");
		me.add("bronx");
		me.add("10460");
		me.add("40.83");
		me.add("-73.88");
		me.add("40.83, -73.88");
		me.add("southern boulevard");
		me.add("east 173");
		me.add("");
		me.add("0");
		me.add("0");
		me.add("0");
		me.add("0");
		me.add("0");
		me.add("0");
		me.add("0");
		me.add("0");
		me.add("following");
		me.add("unspecified");
		me.add("k");
		me.add("k");
		me.add("k");
		me.add("3781550");
		me.add("s");
		me.add("9");
		Collision one = new Collision(me);
		CollisionsData avlTree = new CollisionsData();
		avlTree.add(one);
		System.out.println(avlTree.toString());
	}
	//debug end
	*/
	
}
