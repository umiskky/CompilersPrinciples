package regex2NFA;
import java.util.ArrayList;
import java.util.List;

/**
 * @author umiskky
 * @version 0.0.1
 * @date 2021/05/15
 */

public class Graph {
    private List<Edge> edges;
    private Node start;
    private Node end;

    public Graph() {
        edges = new ArrayList<Edge>();
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public Node getStart() {
        return start;
    }

    public void setStart(Node start) {
        this.start = start;
    }

    public Node getEnd() {
        return end;
    }

    public void setEnd(Node end) {
        this.end = end;
    }

    public void reset() {
        Node.reset();
    }

    /**
     * 根据操作符和操作对象来进行连接、联合、闭包处理
     * 由参数来判断调用构建NFA的函数
     * 处理a*时会调用处理单字符闭包的函数，对应的也有处理NFA闭包的函数
     */
    public void Star(Object obj) {
        if (obj.getClass().getName().equals(Graph.class.getName())) {
            addStar((Graph) obj);
            return;
        }
        if (obj.getClass().getName().equals(Character.class.getName())) {
            addStar((Character) obj);
            return;
        } else {
            throw new RuntimeException("You have an error in your Regex syntax");
        }
    }

    public void Union(Object obj1, Object obj2) {
        if (obj1.getClass().getName().equals(Character.class.getName())) {
            if (obj2.getClass().getName().equals(Graph.class.getName())) {
                addUnion((Character) obj1, (Graph) obj2);
                return;
            }
            if (obj2.getClass().getName().equals(Character.class.getName())) {
                addUnion((Character) obj1, (Character) obj2);
                return;
            }
        }
        if (obj1.getClass().getName().equals(Graph.class.getName())) {
            if (obj2.getClass().getName().equals(Graph.class.getName())) {
                addUnion((Graph) obj1, (Graph) obj2);
                return;
            }
            if (obj2.getClass().getName().equals(Character.class.getName())) {
                addUnion((Graph) obj1, (Character) obj2);
                return;
            }
        } else {
            throw new RuntimeException("You have an error in your Regex syntax");
        }
    }

    public void Concat(Object obj1, Object obj2) {
        if (obj1.getClass().getName().equals(Character.class.getName())) {
            if (obj2.getClass().getName().equals(Graph.class.getName())) {
                addConcat((Character) obj1, (Graph) obj2);
                return;
            }
            if (obj2.getClass().getName().equals(Character.class.getName())) {
                addConcat((Character) obj1, (Character) obj2);
                return;
            }
        }
        if (obj1.getClass().getName().equals(Graph.class.getName())) {
            if (obj2.getClass().getName().equals(Graph.class.getName())) {
                addConcat((Graph) obj1, (Graph) obj2);
                return;
            }
            if (obj2.getClass().getName().equals(Character.class.getName())) {
                addConcat((Graph) obj1, (Character) obj2);
                return;
            }
        } else {
            throw new RuntimeException("You have an error in your Regex syntax");
        }
    }

    /**
     * 处理NFA闭包
     */
    public void addStar(Graph graph) {
        Node begNode = new Node();
        Node endNode = new Node();
        Edge edge1 = new Edge(begNode, endNode, "epsilon");
        Edge edge2 = new Edge(begNode, graph.getStart(), "epsilon");
        Edge edge3 = new Edge(graph.getEnd(), endNode, "epsilon");
        /**
         * 改动的地方
         */
        Edge edge4 = new Edge(graph.getEnd(), graph.getStart(), "epsilon");
        for (int i = 0; i < graph.getEdges().size(); i++) {
            this.edges.add(graph.getEdges().get(i));
        }
        this.edges.add(edge1);
        this.edges.add(edge2);
        this.edges.add(edge3);
        /**
         * 改动的地方
         */
        this.edges.add(edge4);
        this.start = begNode;
        this.end = endNode;
    }

    /**
     * 处理单字符闭包
     */
    public void addStar(Character character) {
        Node nodeCenter = new Node();
        Node nodebeg = new Node();
        Node nodeend = new Node();
        Edge edgeLink = new Edge(nodeCenter, nodeCenter, character.toString());
        Edge edgeEpsilonBeg = new Edge(nodebeg, nodeCenter, "epsilon");
        Edge edgeepsilonEnd = new Edge(nodeCenter, nodeend, "epsilon");
        this.edges.add(edgeLink);
        this.edges.add(edgeEpsilonBeg);
        this.edges.add(edgeepsilonEnd);
        this.start = nodebeg;
        this.end = nodeend;
    }

    public void addUnion(Character character, Graph graph) {
        Node begNode = new Node();
        Node endNode = new Node();
        Edge edge1 = new Edge(begNode, graph.getStart(), "epsilon");
        Edge edge2 = new Edge(graph.getEnd(), endNode, "epsilon");
        Edge edge3 = new Edge(begNode, endNode, character.toString());
        for (int i = 0; i < graph.getEdges().size(); i++) {
            this.edges.add(graph.getEdges().get(i));
        }
        this.edges.add(edge1);
        this.edges.add(edge2);
        this.edges.add(edge3);
        this.start = begNode;
        this.end = endNode;
    }

    public void addUnion(Graph graph, Character character) {
        Node begNode = new Node();
        Node endNode = new Node();
        Edge edge1 = new Edge(begNode, graph.getStart(), "epsilon");
        Edge edge2 = new Edge(graph.getEnd(), endNode, "epsilon");
        Edge edge3 = new Edge(begNode, endNode, character.toString());
        for (int i = 0; i < graph.getEdges().size(); i++) {
            this.edges.add(graph.getEdges().get(i));
        }
        this.edges.add(edge1);
        this.edges.add(edge2);
        this.edges.add(edge3);
        this.start = begNode;
        this.end = endNode;
    }

    public void addUnion(Graph graph1, Graph graph2) {
        Node begNode = new Node();
        Node endNode = new Node();
        Edge edge1 = new Edge(begNode, graph1.getStart(), "epsilon");
        Edge edge2 = new Edge(begNode, graph2.getStart(), "epsilon");
        Edge edge3 = new Edge(graph1.getEnd(), endNode, "epsilon");
        Edge edge4 = new Edge(graph2.getEnd(), endNode, "epsilon");
        this.start = begNode;
        this.end = endNode;
        for (int i = 0; i < graph1.getEdges().size(); i++) {
            this.edges.add(graph1.getEdges().get(i));
        }
        for (int i = 0; i < graph2.getEdges().size(); i++) {
            this.edges.add(graph2.getEdges().get(i));
        }
        this.edges.add(edge1);
        this.edges.add(edge2);
        this.edges.add(edge3);
        this.edges.add(edge4);
    }

    public void addUnion(Character characterOne, Character characterTwo) {
        Node nodebeg = new Node();
        Node nodeend = new Node();
        Edge edgeOne = new Edge(nodebeg, nodeend, characterOne.toString());
        Edge edgeTwo = new Edge(nodebeg, nodeend, characterTwo.toString());
        edges.add(edgeOne);
        edges.add(edgeTwo);
        start = nodebeg;
        end = nodeend;
    }

    public void addConcat(Character character, Graph graph) {
        Node begNode = new Node();
        Edge edge = new Edge(begNode, graph.getStart(), character.toString());
        for (int i = 0; i < graph.getEdges().size(); i++) {
            this.edges.add(graph.getEdges().get(i));
        }
        this.edges.add(edge);
        this.start = begNode;
        this.end = graph.getEnd();
    }

    public void addConcat(Graph graph, Character character) {
        Node endNode = new Node();
        Edge edge = new Edge(graph.getEnd(), endNode, character.toString());
        for (int i = 0; i < graph.getEdges().size(); i++) {
            this.edges.add(graph.getEdges().get(i));
        }
        this.edges.add(edge);
        this.start = graph.getStart();
        this.end = endNode;
    }

    public void addConcat(Graph graph1, Graph graph2) {
        Edge edge = new Edge(graph1.getEnd(), graph2.getStart(), "epsilon");
        this.start = graph1.getStart();
        this.end = graph2.getEnd();
        for (int i = 0; i < graph1.getEdges().size(); i++) {
            this.edges.add(graph1.getEdges().get(i));
        }
        for (int i = 0; i < graph2.getEdges().size(); i++) {
            this.edges.add(graph2.getEdges().get(i));
        }
        this.edges.add(edge);
    }

    public void addConcat(Character characterOne, Character characterTwo) {
        Node begNode = new Node();
        Node midNode = new Node();
        Node endNode = new Node();
        Edge edge1 = new Edge(begNode, midNode, characterOne.toString());
        Edge edge2 = new Edge(midNode, endNode, characterTwo.toString());
        this.start = begNode;
        this.end = endNode;
        this.edges.add(edge1);
        this.edges.add(edge2);
    }

    @Override
    public String toString() {
        String printString = "Start=" + this.start + "  End=" + this.end + "\n";
        for (int i = 0; i < edges.size(); i++) {
            printString += edges.get(i) + "\n";
        }
        return printString;
    }
}

