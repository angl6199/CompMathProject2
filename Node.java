import java.util.ArrayList;
import java.util.List;

public class Node {

    private String data = null;

    private List<Node> children = new ArrayList<>();

    private Node parent = null;

    private int remaining;

    public boolean deuda;

    public Node(String data) {
        this.data = data;
    }

    public void addChild(Node child) {
        child.setParent(this);
        this.children.add(child);
    }

    public void addChildren(List<Node> children) {
        children.forEach(each -> each.setParent(this));
        this.children.addAll(children);
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

    public int getRemaining() {
        return remaining;
    }

    public void setRemaining(int remaining) {
        this.remaining = remaining;
    }

    public boolean getDeuda() {
        return deuda;
    }

    public void setDeuda(boolean deuda) {
        this.deuda = deuda;
    }

    public void setParent(Node parent) {
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