package ex2.src.api;


import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class DWGraph_DS implements directed_weighted_graph, java.io.Serializable {

    private HashMap<Integer, node_data> vertices;
    private int mc;
    private int edSize;
    private int noSize;

    /**
     * Constructs a DWGraph_DS with not vertices.
     */
    public DWGraph_DS() {
        this.mc = 0;
        this.vertices = new HashMap<>();
        this.edSize = 0;
        this.noSize = 0;
    }

    /**
     * @param gr
     */
    public DWGraph_DS(directed_weighted_graph gr) {
        if (gr == null) return;
        if (gr != null) {
            this.vertices = new HashMap<>();
            Iterator<node_data> t = gr.getV().iterator();
            while (t.hasNext()) {
                node_data tempNode = t.next();
                node_data tempNode2 = new NodeData(tempNode);
                addNode(tempNode2);
            }
            for (node_data nodeTemp : gr.getV()) {
                Iterator<node_data> t2 = ((NodeData) nodeTemp).getNi().iterator();
                while (t2.hasNext()) {
                    node_data i = new NodeData(t2.next());
                    connect(nodeTemp.getKey(), i.getKey(), gr.getEdge(nodeTemp.getKey(), i.getKey()).getWeight());
                }
            }

            this.edSize = gr.edgeSize();
            this.noSize = gr.nodeSize();
            this.mc = gr.edgeSize() + gr.nodeSize();
        }
    }

    /**
     * @param key - the node_id
     * @return the node of this key, null if none.
     */
    @Override
    public node_data getNode(int key) {
        if (this.vertices.containsKey(key)) {
            return this.vertices.get(key);
        }
        return null;
    }

    /**
     * @param node1
     * @param node2
     * @return true if node2 is a neighbor of node1.
     */
    public boolean hasEdge(int node1, int node2) {
        if (vertices.containsKey(node1) & vertices.containsKey(node2))
            return (((NodeData) getNode(node1)).hasNi(node2));
        return false;
    }

    /**
     * @param src
     * @param dest
     * @return the data of the edge from src to node2.
     */
    @Override
    public edge_data getEdge(int src, int dest) {
        if (hasEdge(src, dest))
            return ((NodeData) this.getNode(src)).getEdge(dest);
        return null;
    }

    /**
     * Add a new node to the graph with the node_data that received.
     *
     * @param n
     */
    @Override
    public void addNode(node_data n) {
        if (!vertices.containsKey(n.getKey())) {
            this.vertices.put(n.getKey(), n);
            if (vertices.size() == (noSize + 1)) {
                this.noSize++;
                this.mc++;
            }
        }
    }

    /**
     * This function makes an edge  from node1 to node2
     * by inserting one node into the neighbor's list of the others node,
     * in addition inserting one node into the edges list of the others node.
     *
     * @param src
     * @param dest
     */
    @Override
    public void connect(int src, int dest, double w) {
        if (w >= 0 & vertices.containsKey(src) && vertices.containsKey(dest) & src != dest) {
            if (!hasEdge(src, dest)) {
                this.edSize++;
            }

            if (getEdge(src, dest) == null || w != getEdge(src, dest).getWeight())
                this.mc++;

            ((NodeData) this.vertices.get(src)).addNi(this.vertices.get(dest), w);

        }
    }

    /**
     * @return a collection with all graph's nodes.
     */
    @Override
    public Collection<node_data> getV() {
        if (this.vertices == null) return null;
        return this.vertices.values();
    }

    /**
     * @return a collection with all the Neighbor nodes of the vertex that was received.
     */
    public Collection<node_data> getV(int node_id) {
        if (vertices.containsKey(node_id))
            return ((NodeData) this.vertices.get(node_id)).getNi();
        return null;
    }

    public Collection<edge_data> getE(int node_id) {
        if (vertices.containsKey(node_id))
            return ((NodeData) this.vertices.get(node_id)).getEd();
        return null;
    }

    /**
     * The function passes over all the neighbors of the vertex and removes the common edges.
     * Finally delete the vertex from the graph.
     *
     * @param key
     * @return the deleted vertex.
     */
    @Override
    public node_data removeNode(int key) {
        if (!this.vertices.containsKey(key)) return null;
        node_data myNode = getNode(key);
        int myNodeSize = ((NodeData) myNode).getEd().size();
        Iterator<node_data> itr = getV().iterator();
        if (itr.hasNext()) {
            node_data t;
            while (itr.hasNext()) {
                t = itr.next();
                if (t.getKey() != key && (hasEdge(t.getKey(), key))) {
                    ((NodeData) t).removeNode(myNode);
                    this.edSize--;
                    this.mc++;
                }
            }
        }

        this.edSize -= myNodeSize;
        this.mc+= myNodeSize;
        this.vertices.remove(key);
        this.noSize--;
        this.mc++;
        return myNode;
    }

    /**
     * @param src
     * @param dest
     * @return
     */
    @Override
    public edge_data removeEdge(int src, int dest) {
        if (src != dest & vertices.containsKey(src) & vertices.containsKey(dest) & hasEdge(src, dest)) {
            edge_data temp = getEdge(src, dest);
            ((NodeData) this.vertices.get(src)).removeNode(this.vertices.get(dest));
            this.edSize--;
            this.mc++;
            return temp;
        }
        return null;
    }

    /**
     * @return the number of vertices (nodes) in the graph.
     */
    @Override
    public int nodeSize() {
        return this.noSize;
    }

    /**
     * @return the number of edges in the graph.
     */
    @Override
    public int edgeSize() {
        return this.edSize;
    }

    /**
     * @return the Mode Count of the changes made in the graph.
     */
    @Override
    public int getMC() {
        return this.mc;
    }

    /**
     * @return a string representation of the DWGraph_DS. In general returns a
     * string that "textually represents" this DWGraph_DS.
     * The result is a concise but informative representation
     * that is easy for a person to read.
     */
    @Override
    public String toString() {
        return "{" + vertices +
                ", mc=" + mc +
                ", edSize=" + edSize +
                ", noSize=" + noSize +
                '}';
    }

    /**
     * Indicates whether some other WGraph_DS is "equal to" this one.
     * by examining each element in the DWGraph_DS obj.
     *
     * @param o (DWGraph_DS)
     * @return true if this DWGraph_DS is the same as the DWGraph_DS; false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DWGraph_DS))
            return false;
        DWGraph_DS w = (DWGraph_DS) o;
        if (w.edgeSize() != this.edSize | w.nodeSize() != this.noSize)
            return false;

        return w.vertices.equals(this.vertices);
    }

}



