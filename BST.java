package project212.phase2;

class BSTNode<T> {
    public int key;
    public T data;
    public BSTNode<T> left, right;

    public BSTNode(int key, T data) {
        this.key = key;
        this.data = data;
        left = right = null;
    }
}

public class BST<T> {
    BSTNode<T> root;
    private BSTNode<T> current;

    public BST() {
        current = root = null;
    }

    public boolean empty() {
        return root == null;
    }

    public boolean full() {
        return false;
    }

    public boolean findKey(int k) {
        BSTNode<T> p = root;
        while (p != null) {
            current = p;
            if (k == p.key)
                return true;
            else if (k < p.key)
                p = p.left;
            else
                p = p.right;
        }
        return false;
    }

    public boolean insert(int k, T val) {
        if (root == null) {
            current = root = new BSTNode<T>(k, val);
            return true;
        }
        BSTNode<T> p = current;
        if (findKey(k)) {
            current = p;
            return false;
        }
        BSTNode<T> tmp = new BSTNode<T>(k, val);
        if (k < current.key)
            current.left = tmp;
        else
            current.right = tmp;
        current = tmp;
        return true;
    }

    public boolean removeKey(int k) {
        if (root == null) return false;
        root = removeRec(root, k);
        return true;
    }

    private BSTNode<T> removeRec(BSTNode<T> node, int key) {
        if (node == null) return null;

        if (key < node.key)
            node.left = removeRec(node.left, key);
        else if (key > node.key)
            node.right = removeRec(node.right, key);
        else {
            // no child
            if (node.left == null && node.right == null)
                return null;
            // one child
            if (node.left == null)
                return node.right;
            if (node.right == null)
                return node.left;
            // two children
            BSTNode<T> successor = minValueNode(node.right);
            node.key = successor.key;
            node.data = successor.data;
            node.right = removeRec(node.right, successor.key);
        }
        return node;
    }

    private BSTNode<T> minValueNode(BSTNode<T> node) {
        BSTNode<T> current = node;
        while (current.left != null)
            current = current.left;
        return current;
    }

    public void inOrder() {
        if (root == null) {
            System.out.println("Empty tree");
            return;
        }
        inOrder(root);
    }

    private void inOrder(BSTNode<T> node) {
        if (node == null) return;
        inOrder(node.left);
        System.out.println("Key: " + node.key + " | Data: " + node.data);
        inOrder(node.right);
    }

    public void findRoot() {
        current = root;
    }

    public int curkey() {
        return current.key;
    }

    public T retrieve() {
        if (current == null) return null;
        return current.data;
    }

    public BSTNode<T> getRoot() {
        return root;
    }

    // delete Product safely
    public void delete(Product p) {
        if (p == null) return;
        removeKey(p.getProductId());
    }
}
