import java.util.ArrayList;
import java.util.List;

class Node {
    int order;
    List<String> values;
    List<List<Node>> keys;
    Node nextKey;
    Node parent;
    boolean isLeaf;

    public Node(int order) {
        this.order = order;
        this.values = new ArrayList<>();
        this.keys = new ArrayList<>();
        this.nextKey = null;
        this.parent = null;
        this.isLeaf = false;
    }

    public void insertAtLeaf(String value, Node key) {
        if (!this.values.isEmpty()) {
            for (int i = 0; i < this.values.size(); i++) {
                if (value.equals(this.values.get(i))) {
                    this.keys.get(i).add(key);
                    break;
                } else if (value.compareTo(this.values.get(i)) < 0) {
                    this.values.add(i, value);
                    this.keys.add(i, new ArrayList<>());
                    this.keys.get(i).add(key);
                    break;
                } else if (i + 1 == this.values.size()) {
                    this.values.add(value);
                    this.keys.add(new ArrayList<>());
                    this.keys.get(i + 1).add(key);
                    break;
                }
            }
        } else {
            this.values.add(value);
            this.keys.add(new ArrayList<>());
            this.keys.get(0).add(key);
        }
    }
}

class BplusTree {
    Node root;

    public BplusTree(int order) {
        this.root = new Node(order);
        this.root.isLeaf = true;
    }

    public void insert(String value, Node key) {
        Node oldNode = this.search(value);
        oldNode.insertAtLeaf(value, key);

        if (oldNode.values.size() == oldNode.order) {
            Node newNode = new Node(oldNode.order);
            newNode.isLeaf = true;
            newNode.parent = oldNode.parent;
            int mid = (int) Math.ceil(oldNode.order / 2.0) - 1;
            newNode.values = new ArrayList<>(oldNode.values.subList(mid + 1, oldNode.values.size()));
            newNode.keys = new ArrayList<>(oldNode.keys.subList(mid + 1, oldNode.keys.size()));
            newNode.nextKey = oldNode.nextKey;
            oldNode.values = new ArrayList<>(oldNode.values.subList(0, mid + 1));
            oldNode.keys = new ArrayList<>(oldNode.keys.subList(0, mid + 1));
            oldNode.nextKey = newNode;
            this.insertInParent(oldNode, newNode.values.get(0), newNode);
        }
    }

    public Node search(String value) {
        Node currentNode = this.root;
        while (!currentNode.isLeaf) {
            for (int i = 0; i < currentNode.values.size(); i++) {
                if (value.equals(currentNode.values.get(i))) {
                    currentNode = currentNode.keys.get(i + 1).get(0);
                    break;
                } else if (value.compareTo(currentNode.values.get(i)) < 0) {
                    currentNode = currentNode.keys.get(i).get(0);
                    break;
                } else if (i + 1 == currentNode.values.size()) {
                    currentNode = currentNode.keys.get(i + 1).get(0);
                    break;
                }
            }
        }
        return currentNode;
    }

    public boolean find
