package project212.phase2;
class BSTNode<T> {
	public int key;
	public T data;
	public BSTNode<T> left,right;
	
public BSTNode(int key, T data) {
	this.key = key;
	this.data = data;
	left = right = null;
}
}
public class BST<T>{
	private BSTNode<T> root, current;
	// int num_comp = 0; for للمقارنة
	public BST() {
		current = root = null;
	}
	public boolean empty() {
		return root == null;
	}
	public boolean full() {
		return false;
	}
	public boolean findKey(int k) { //FIND
		BSTNode<T> p = root;
		while (p!= null) {
			current = p;
			if(k == p.key) {
				return true;}
				else if(k < p.key) {
					p = p.left;
				}
				else {
					p = p.right;
				}
			}
		return false;
		}
	public boolean insert(int k, T val) { //INSERT
		if(root == null) {
			current = root = new BSTNode<T>(k, val);
			return true;
		}
		BSTNode<T> p = current;
		if(findKey(k)) { //THE KEY IS ALREADY EXIST
			current = p ;
			return false;
		}
		BSTNode<T> tmp = new BSTNode<T>(k, val);
		if(k < current.key) {
			current.left = tmp;
		}else {
			current.right = tmp;
		}
		current = tmp;
		return true;
	}
	public boolean removeKey(int k) { //REMOVE
		//serch for the k
		int k1 = k;
		BSTNode<T> p = root;
		BSTNode<T> q = null;
		while (p != null) {
			if(k1 < p.key) {
				q = p;
				p = p.left;
			}else if(k1 > p.key) {
				q = p;
				p = p.right;
			}
			else {//found the key
				if(p.left != null && p.right != null){
					BSTNode<T> min = p.right;
					q = p;
					while (min.left != null) {
						q = min;
						min = min.left;
					}
					p.key = min.key;
					p.data = min.data;
					k1 = min.key;
					p = min; //now fall bake to either case 1 or case 2
				}
				}
				if(p.left != null) {
					p = p.left;
				}else {
					p = p.right;
				}
				if(q == null) {
					root = p;
				}else {
					if(k1 < q.key) {
						q.left = p;
					}else {
						q.right = p;
					}
					current = root;
					return true;
				}
			}
			return false; //not found
		}
		

		//INORDER
		public void inOrder() {
			if(root == null)
				System.out.println("empty tree");
			else
				inOrder(root);
		}
		private void inOrder(BSTNode<T>p) {
			if(p == null) return;
			inOrder(p.left);
			System.out.println("key"+ p.key+" , data = "+p.data);
			inOrder(p.right);	
			}
		//HELPING METHODS
		public void findRoot() {
			current = root;
	}
		public int curkey() {
			return current.key;
		}
		
		public T retrieve() {
		    if (current == null)
		        return null;
		    return current.data;
		}

	
		public BSTNode<T> getRoot() {
		    return root;
		}

	
	
	}
	
