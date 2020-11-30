package api;


import java.util.Collection;
import java.util.HashMap;

/**
 * This class implements node_info interface that represents the set of operations applicable on a
 * node (vertex) in an directional weighted graph.
 * The vertex's neighbors and the connected edges are implemented by HashMap for high efficiency.
 */
public class NodeData implements node_data, java.io.Serializable {

    private static int id = 0;
    private HashMap<Integer, node_data> myNeighbors;
    private HashMap<Integer, edge_data> edges;
    private int key;
    private int tag;
    private String info = "";
    private geo_location location;
    private double weight;


    /**
     * Constructs a NodeInfo with minimal fields
     */
    public NodeData() {
        this.key = id++;
        this.tag = 0;
        this.info = "";
        this.myNeighbors = new HashMap<>();
        this.edges = new HashMap<>();
        this.location = null;

    }

    /**
     * Constructs a NodeInfo with the same id that was received.
     *
     * @param k
     */
    public NodeData(int k) {
        this.key = k;
        this.tag = 0;
        this.info = "";
        this.myNeighbors = new HashMap<>();
        this.edges = new HashMap<>();
        this.location = null;
    }

    /**
     * Constructs a NodeInfo with the same fields
     * as the specified NodeInfo object that was received.
     *
     * @param node
     */
    public NodeData(node_data node) {
        if (node != null) {
            this.myNeighbors = new HashMap<>();
            this.edges = new HashMap<>();
            this.tag = node.getTag();
            this.info = node.getInfo();
            this.key = node.getKey();
            if (node.getLocation() == null) this.location = null;
            else this.location = new NodeLocation(node.getLocation());
            this.weight = node.getWeight();
        }
    }

    /**
     * @return the node's key.
     */
    @Override
    public int getKey() {
        return this.key;
    }

    @Override
    public geo_location getLocation() {
        return this.location;
    }

    @Override
    public void setLocation(geo_location p) {
        this.location = new NodeLocation(p);
    }

    @Override
    public double getWeight() {
        return this.weight;
    }

    @Override
    public void setWeight(double w) {
        this.weight = w;
    }


    /**
     * @return the node's info.
     */
    @Override
    public String getInfo() {
        return this.info;
    }

    /**
     * Sets the info of the vertex.
     *
     * @param s
     */
    @Override
    public void setInfo(String s) {
        this.info = s;
    }

    /**
     * @return the node's tag.
     */
    @Override
    public int getTag() {
        return this.tag;
    }

    /**
     * Sets the tag of the vertex.
     *
     * @param t
     */
    @Override
    public void setTag(int t) {
        this.tag = t;
    }

    /**
     * @return a collection with all the Neighbor nodes of this vertex.
     */
    public Collection<node_data> getNi() {
        return this.myNeighbors.values();
    }

    public edge_data getEdge(int dest) {
        if (hasNi(dest))
            return (this.edges.get(dest));
        return null;
    }

    /**
     * @return a collection of the vertex edges.
     */
    public Collection<edge_data> getEd() {
        return this.edges.values();
    }

    /**
     * @param key
     * @return true if have an edge between the vertices.
     */
    public boolean hasNi(int key) {
        return this.edges.containsKey(key);
    }

    /**
     * This function make edge between this vertex and node_info (t).
     *
     * @param t
     */
    public void addNi(node_data t, double weight) {
        if (!myNeighbors.containsKey(t.getKey()) & t.getKey() != this.key & weight >= 0) {
            this.myNeighbors.put(t.getKey(), t);
            edge_data temp = new EdgeNode(key, t.getKey(), weight);
            this.edges.put(t.getKey(), temp);
        } else if (myNeighbors.containsKey(t.getKey()) & t.getKey() != this.key & weight >= 0) {
            ((EdgeNode) edges.get(t.getKey())).setWeight(weight);
        }
    }

    /**
     * Removes the node and the edges between this vertex and others.
     *
     * @param node
     */
    public void removeNode(node_data node) {
        if (myNeighbors.containsKey(node.getKey())) {
            this.myNeighbors.remove(node.getKey());
            this.edges.remove(node.getKey());
        }
    }

    /**
     * @return a string representation of the NodeInfo. In general returns a
     * string that "textually represents" this NodeInfo.
     * The result is a concise but informative representation
     * that is easy for a person to read.
     */
    @Override
    public String toString() {
        return "" + key;
    }

    /**
     * Indicates whether some other NodeInfo is "equal to" this one.
     * by examining each element in the NodeInfo obj.
     *
     * @param o (NodeInfo)
     * @return true if this NodeInfo is the same as the NodeInfo; false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof NodeData))
            return false;
        HashMap e = ((NodeData) o).edges;
        NodeData node = (NodeData) o;

        if (this.tag != node.getTag() | !(this.info.equals(node.getInfo())))
            return false;

        if (this.key != node.getKey() | !(e.equals(this.edges)))
            return false;

        if (this.weight != node.getWeight())
            return false;

        if ((this.getLocation() == null && node.getLocation() == null))
            return true;

        if ((this.getLocation() == null && node.getLocation() != null) || (this.getLocation() != null && node.getLocation() == null))
            return false;

        if (!this.getLocation().equals(node.getLocation())) {
            return false;
        }

        return true;

    }

    /**
     * //////////////////////////////////////////
     */
    public static class NodeLocation implements geo_location, java.io.Serializable {
        double x, y, z;

        public NodeLocation(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public NodeLocation(geo_location n) {
            if (n != null) {
                double tX = n.x();
                double tY = n.y();
                double tZ = n.z();
                this.x = tX;
                this.y = tY;
                this.z = tZ;
            }
        }

        @Override
        public double x() {
            return this.x;
        }

        @Override
        public double y() {
            return this.y;
        }

        @Override
        public double z() {
            return this.z;
        }

        @Override
        public double distance(geo_location g) {
            return Math.sqrt(Math.pow((this.x - g.x()), 2) + Math.pow((this.y - g.y()), 2) + Math.pow((this.z - g.z()), 2));
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof NodeLocation))
                return false;
            NodeLocation t = (NodeLocation) o;
            if (this.x == t.x() & this.y == t.y() & this.z == t.z()) {
                return true;
            }
            return false;
        }

        @Override
        public String toString() {
            return "x=" + x +
                    ", y=" + y +
                    ", z=" + z;
        }
    }


    /**
     * This class represents the set of operations applicable on a
     * edge in a directed weighted graph.
     * each edge contain the key nodes are connect and the weight of the edge.
     */
    private static class EdgeNode implements edge_data, java.io.Serializable {
        int first_key;
        int second_key;
        double weight;
        int tag = 0;
        String info = "";

        /**
         * Constructs a EdgeNode with receives data.
         */
        public EdgeNode(int first_key, int second_key, double weight) {
            this.weight = weight;
            this.first_key = first_key;
            this.second_key = second_key;
        }

        /**
         * @return the first node's key.
         */
        public int getSrc() {
            return this.first_key;
        }

        /**
         * @return the second node's key.
         */
        public int getDest() {
            return this.second_key;
        }

        /**
         * @return the weight of the edge.
         */
        public double getWeight() {
            return this.weight;
        }


        @Override
        public String getInfo() {
            return this.info;
        }

        @Override
        public void setInfo(String s) {
            this.info = s;
        }

        @Override
        public int getTag() {
            return this.tag;
        }

        @Override
        public void setTag(int t) {
            this.tag = t;
        }

        /**
         * Sets the weight of the edge.
         *
         * @param weight
         */
        public void setWeight(double weight) {
            this.weight = weight;
        }

        /**
         * @return a string representation of the EdgeNode. In general returns a
         * string that "textually represents" this EdgeNode.
         * The result is a concise but informative representation
         * that is easy for a person to read.
         */
        @Override
        public String toString() {
            return "" + second_key;
        }

        /**
         * Indicates whether some other edge is "equal to" this one.
         * by examining each element in the edge obj.
         *
         * @param o (edge)
         * @return true if this edge is the same as the edge; false otherwise.
         */
        @Override
        public boolean equals(Object o) {
            if (!(o instanceof EdgeNode))
                return false;
            EdgeNode t = (EdgeNode) o;
            if (this.weight == t.weight & this.first_key == t.first_key & this.second_key == t.second_key &
                    this.tag == t.tag & this.info.equals(t.info)) {
                return true;
            }
            return false;
        }


    }


}
