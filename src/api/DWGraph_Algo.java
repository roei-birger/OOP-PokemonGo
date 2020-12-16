package api;

import com.google.gson.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * This class creates a graph and includes algorithms:
 * One copies, the other checks if the graph is connected
 * How long it takes to get from one vertex to another
 * What is the shortest path from one vertex to another
 * Saves the graph to the given file name
 * Loads a graph to graph algorithm
 */
public class DWGraph_Algo implements dw_graph_algorithms, java.io.Serializable {

    private directed_weighted_graph my_g;
    private HashMap<Integer, myWay> path;

    /**
     * A default constructor
     */
    public DWGraph_Algo() {
        this.my_g = new DWGraph_DS();
    }

    /**
     * This function preforms a shallow copy
     * (creates another pointer for the graph)
     *
     * @param g
     */
    @Override
    public void init(directed_weighted_graph g) {
        this.my_g = g;
    }

    /**
     * @return the directed_weighted_graph at the DWGraph_Algo.init
     */
    @Override
    public directed_weighted_graph getGraph() {
        return this.my_g;
    }

    /**
     * Builds a deep copy of the received graph,
     * By calling the DWGraph_DS copy constructor.
     *
     * @return the created graph.
     */
    @Override
    public directed_weighted_graph copy() {
        directed_weighted_graph graphToCopy = new DWGraph_DS(my_g);
        return graphToCopy;
    }

    /**
     * This function checks if the graph is connected
     * by checking if there is a path from any vertex in the graph
     * to any other vertex in the graph (Dijkstra algorithm)
     *
     * @return Boolean (true or false)
     */
    @Override
    public boolean isConnected() {
        if (my_g == null) {
            return false;
        }
        dw_graph_algorithms ga = new DWGraph_Algo();
        node_data temp = my_g.getV().iterator().next();
        ga.init(my_g);
        for (node_data i : my_g.getV()) {
            if (ga.shortestPathDist(i.getKey(), temp.getKey()) < 0 || ga.shortestPathDist(temp.getKey(), i.getKey()) < 0) {
                return false;
            }
        }
        return true;
    }


    /**
     * This function returns the length of the shortest path between two vertexes on the graph
     * by checking all the graph vertexes edges and its neighbors edges
     * by pushing them to a priority queue (Dijkstra algorithm)
     *
     * @param src  - start node
     * @param dest - end (target) node
     */
    @Override
    public double shortestPathDist(int src, int dest) {
        path = new HashMap<>();
        if (my_g == null) return -1;
        if (my_g.getNode(src) == null | my_g.getNode(dest) == null) return -1;
        PriorityQueue<myWay> q = new PriorityQueue<>();
        if (src == dest) {
            myWay first = new myWay(src);
            first.setShortDis(0);
            path.put(first.getId(), first);
            return 0;
        }
        myWay first = new myWay(src);
        first.setShortDis(0);
        first.setFlag(1);
        q.add(first);
        myWay temp;
        path.put(first.getId(), first);
        while (!q.isEmpty()) {
            temp = q.poll();
            if (temp.getFlag() == 1) {
                if (temp.getId() == dest) {
                    return temp.getShortDis();
                } else {
                    for (node_data i : ((DWGraph_DS) my_g).getV(temp.getId())) {
                        myWay p;

                        if (!path.containsKey(i.getKey())) {
                            p = new myWay(i.getKey());
                            path.put(p.getId(), p);
                            p.setShortDis(temp.getShortDis() + my_g.getEdge(temp.getId(), p.getId()).getWeight());
                            p.setParent(temp);
                            q.add(p);
                            p.setFlag(1);
                        } else {
                            p = path.get(i.getKey());
                            double sumE = my_g.getEdge(temp.getId(), p.getId()).getWeight();
                            if (temp.getShortDis() + sumE <= p.getShortDis()) {
                                p.setShortDis(temp.getShortDis() + sumE);
                                p.setParent(temp);
                                q.add(p);
                            }
                        }
                    }
                }
                temp.setFlag(2);
            }
        }
        return -1;
    }

    /**
     * This function returns the shortest path between src to dest - as an ordered List of nodes:
     * src--> n1-->n2-->...dest,
     * by using the function "shortestPathDist" map that was created in the function.
     *@param src - start node
     *@param dest - end (target) node
     */
    @Override
    public List<node_data> shortestPath(int src, int dest) {
        double dist = shortestPathDist(src, dest);
        if (dist < 0) return null;
        LinkedList<node_data> finalList = new LinkedList<>();
        myWay tempO = path.get(dest);
        node_data tempN = my_g.getNode(tempO.getId());

        if (dest == src) {
            finalList.addFirst(tempN);
            return finalList;
        }
        finalList.addFirst(tempN);
        while (tempO.getShortDis() > 0) {
            tempO = tempO.getParent();
            tempN = my_g.getNode(tempO.getId());
            finalList.addFirst(tempN);

        }

        return finalList;
    }

    /**
     * This function saves the directed weighted graph to the given
     * file name - in JSON format
     * @param file - the file name .
     * @return true - iff the file was successfully saved
     */
    @Override
    public boolean save(String file) {

        JsonObject jsonObject = new JsonObject();
        JsonArray nodesArray = new JsonArray();
        JsonArray edgesArray = new JsonArray();
        for (node_data i : my_g.getV()) {
            JsonObject nodeTemp = new JsonObject();
            if (i.getLocation() == null) {
                nodeTemp.addProperty("pos", ",,");
            } else {
                String nodeTempLocation = i.getLocation().x() + "," + i.getLocation().y() + "," + i.getLocation().z();
                nodeTemp.addProperty("pos", nodeTempLocation);
            }
            nodeTemp.addProperty("id", i.getKey());
            nodesArray.add(nodeTemp);
        }

        for (node_data i : my_g.getV()) {
            for (edge_data k : my_g.getE(i.getKey())) {
                JsonObject edgeTemp = new JsonObject();
                edgeTemp.addProperty("src", k.getSrc());
                edgeTemp.addProperty("w", k.getWeight());
                edgeTemp.addProperty("dest", k.getDest());
                edgesArray.add(edgeTemp);
            }
        }

        jsonObject.add("Nodes", nodesArray);
        jsonObject.add("Edges", edgesArray);


        try {
            File f = new File(file);
            Gson gson = new Gson();
            FileWriter fileWriter = new FileWriter(f);
            fileWriter.write(gson.toJson(jsonObject));
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * This function loads a graph to graph algorithm from JSON format file.
     * If the file was successfully loaded - the graph will be changed,
     * If graph was not loaded the original graph should remain "as is".
     * @param file - file name
     * @return true - if the graph was successfully loaded.
     */
    @Override
    public boolean load(String file) {

        JsonObject jasonObject;
        String f;
        directed_weighted_graph g1 = new DWGraph_DS();

        try {
            f = new String(Files.readAllBytes(Paths.get(file)));

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        jasonObject = JsonParser.parseString(f).getAsJsonObject();

        JsonArray NodesArray = jasonObject.getAsJsonArray("Nodes");
        for (JsonElement i : NodesArray) {
            String[] posArray = i.getAsJsonObject().get("pos").getAsString().split(",");
            node_data temp = new NodeData(i.getAsJsonObject().get("id").getAsInt());
            if (posArray.length > 0)
                temp.setLocation(new location(Double.parseDouble(posArray[0]), Double.parseDouble(posArray[1]), Double.parseDouble(posArray[2])));
            g1.addNode(temp);
        }

        JsonArray EdgesArray = jasonObject.getAsJsonArray("Edges");
        for (JsonElement i : EdgesArray) {
            g1.connect(i.getAsJsonObject().get("src").getAsInt(), i.getAsJsonObject().get("dest").getAsInt(), i.getAsJsonObject().get("w").getAsDouble());
        }

        init(g1);
        return true;
    }


    /**
     * This class represents a support object for the "shortestPath" method,
     * at the "shortestPathDist" method the graph is tested to find the shortest
     * path between vertices. Each vertex that is found in this way is preserved
     * by this object in order to know which vertex was "his parent"
     * on the way and what is the weight of the edge between them.
     */
    public static class myWay implements Comparable<myWay> {
        private int id;
        private double shortDis;
        private myWay parent;
        private int flag;

        /**
         * Constructs a myWay with a basic fields.
         */
        public myWay(int id) {
            this.id = id;
            this.shortDis = Double.POSITIVE_INFINITY;
            this.parent = null;
            this.flag = 0;


        }

        /**
         * @return the id.
         */
        public int getId() {
            return this.id;
        }

        /**
         * @return the ShortDis.
         */
        public double getShortDis() {
            return this.shortDis;
        }

        /**
         * Sets the shortDis of the edge.
         *
         * @param shortDis
         */
        public void setShortDis(double shortDis) {
            this.shortDis = shortDis;
        }

        /**
         * @return the parent.
         */
        public myWay getParent() {
            return this.parent;
        }

        /**
         * Sets the parent of the vertex.
         *
         * @param parent
         */
        public void setParent(myWay parent) {
            this.parent = parent;
        }

        /**
         * @return the flag.
         */
        public int getFlag() {
            return this.flag;
        }

        /**
         * Sets the flag of the vertex.
         *
         * @param flag
         */
        public void setFlag(int flag) {
            this.flag = flag;
        }

        /**
         * The method defines a linear order between two sides in a graph for comparing edges.
         *
         * @param o
         * @return 0 if equal, 1 if the given obj are grater and -1 if else.
         */
        @Override
        public int compareTo(myWay o) {
            if (this.shortDis < o.getShortDis())
                return -1;
            else if (this.shortDis > o.getShortDis())
                return 1;
            else return 0;
        }

        /**
         * @return a string representation of the myWay. In general returns a
         * string that "textually represents" this myWay.
         * The result is a concise but informative representation
         * that is easy for a person to read.
         */
        @Override
        public String toString() {
            return
                    "" + id;
        }
    }


}
