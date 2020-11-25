package ex2.src.api;

import com.google.gson.*;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

public class DWGraph_Algo implements dw_graph_algorithms, java.io.Serializable {

    private directed_weighted_graph my_g;
    private HashMap<Integer, myWay> path;

    /**
     * Constructs a DWGraph_DS with not vertices.
     */
    public DWGraph_Algo() {
        this.my_g = new DWGraph_DS();
    }

    /**
     * Passes 'my_g' to be the pointer of the directed_weighted_graph (g) that received.
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
     * Build a deep copy of the received graph.
     * By calling the DWGraph_DS copy constructor.
     *
     * @return the created graph.
     */
    @Override
    public directed_weighted_graph copy() {
        directed_weighted_graph graphToCopy = new DWGraph_DS(my_g);
        return graphToCopy;
    }

    @Override
    public boolean isConnected() {
        if (my_g == null) {
            return false;
        }
        dw_graph_algorithms ga = new DWGraph_Algo();
        ga.init(my_g);
        for (node_data i : my_g.getV()) {
            for (node_data j : my_g.getV()) {
                if (ga.shortestPathDist(i.getKey(), j.getKey()) < 0) {
                    return false;
                }
            }
        }
        return true;
    }

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
     * The method calls "shortestPathDist" method on the graph.
     * After that the method used the myWay HASHMAP that the function created.
     * each time inserts the next vertex into the list.
     * The method will be run until the SRC vertex enters the list.
     * Finally we return the created list.
     *
     * @param src  - start node
     * @param dest - end (target) node
     * @return the the shortest path between src to dest - as an ordered List of nodes.
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
            if (posArray.length>0)
            temp.setLocation(new NodeData.NodeLocation(Double.parseDouble(posArray[0]), Double.parseDouble(posArray[1]), Double.parseDouble(posArray[2])));
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
     * at the "shortestPathDist" method אhe graph is tested to find the shortest
     * path between vertices. Each vertex that found in this way is preserved
     * by this object in order to know which vertex was "his parent"
     * on the way and what is the weight of the edge between them.
     */
    public static class myWay implements Comparable<myWay> {
        private int id;
        private double shortDis;
        private myWay parent;
        private int flag;

        /**
         * Constructs a myWay with a basic fields
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
         * @return
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
