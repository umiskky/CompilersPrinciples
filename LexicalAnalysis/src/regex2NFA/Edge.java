/**
 * @author umiskky
 * @version 0.0.1
 * @date 2021/05/15
 */
package regex2NFA;

/**
 * 连接两状态结点的边
 * 标签label 即 驱动字符 其中 epsilon 即 ε
 */
public class Edge {
    private Node begin;
    private Node end;
    private String label;
    private Edge edge;

    public Edge() {
        super();
    }

    public Edge(Node begin, Node end) {
        super();
        this.begin = begin;
        this.end = end;
    }

    public Edge(Node begin, Node end, String label) {
        super();
        this.begin = begin;
        this.end = end;
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Edge getEdge() {
        return edge;
    }

    public void setEdge(Edge edge) {
        this.edge = edge;
    }

    public Node getBegin() {
        return begin;
    }

    public void setBegin(Node begin) {
        this.begin = begin;
    }

    public Node getEnd() {
        return end;
    }

    public void setEnd(Node end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "Edge [begin="+begin+", end="+end+", label="+label+"]";
    }

}

