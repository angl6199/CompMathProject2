import java.util.ArrayList;
import java.util.List;


public class Node {

    private String data = null;

    private List<Node> children = new ArrayList<>();

    private Node parent = null;

    public Node(String data) {
        this.data = data;
    }

    public void addChild(Node child) {
        child.setParent(this);
        this.children.add(child);
    }


    public List<Node> getChildren() {
        return children;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    private void setParent(Node parent) {
        this.parent = parent;
    }

    public Node getParent() {
        return parent;
    }

    @Override
    public String toString() {
        return data;
    }

    public void deleteNode() {
        if (parent != null) {
            this.parent.getChildren().remove(this);
        }
    }

}