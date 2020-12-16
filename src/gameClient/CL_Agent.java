package gameClient;

import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;
import gameClient.util.Point3D;
import org.json.JSONObject;

/**
 * This class represents an agent in the game.
 */
public class CL_Agent {
    public static final double EPS = 0.0001;
    private static int _count = 0;
    private static int _seed = 3331;
    private int _id;
    //	private long _key;
    private geo_location _pos;
    private double _speed;
    private edge_data _curr_edge;
    private node_data _curr_node;
    private directed_weighted_graph _gg;
    private CL_Pokemon _curr_fruit;
    private long _sg_dt;
    private double _value;

    /**
     * Constructs a CL_Agent from received data.
     *
     * @param g,start_node
     */
    public CL_Agent(directed_weighted_graph g, int start_node) {
        _gg = g;
        setMoney(0);
        this._curr_node = _gg.getNode(start_node);
        _pos = _curr_node.getLocation();
        _id = -1;
        setSpeed(0);
    }

    /**
     * Updates the agent's parameters from data in the JSON file.
     *
     * @param json
     */
    public void update(String json) {
        JSONObject line;
        try {
            // "GameServer":{"graph":"A0","pokemons":3,"agents":1}}
            line = new JSONObject(json);
            JSONObject ttt = line.getJSONObject("Agent");
            int id = ttt.getInt("id");
            if (id == this.getID() || this.getID() == -1) {
                if (this.getID() == -1) {
                    _id = id;
                }
                double speed = ttt.getDouble("speed");
                String p = ttt.getString("pos");
                Point3D pp = new Point3D(p);
                int src = ttt.getInt("src");
                int dest = ttt.getInt("dest");
                double value = ttt.getDouble("value");
                this._pos = pp;
                this.setCurrNode(src);
                this.setSpeed(speed);
                this.setNextNode(dest);
                this.setMoney(value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @return the key of _curr_node of the CL_Agent
     */
    //@Override
    public int getSrcNode() {
        return this._curr_node.getKey();
    }

    /**
     * @return the CL_Agent data as JSON format.
     */
    public String toJSON() {
        int d = this.getNextNode();
        String ans = "{\"Agent\":{"
                + "\"id\":" + this._id + ","
                + "\"value\":" + this._value + ","
                + "\"src\":" + this._curr_node.getKey() + ","
                + "\"dest\":" + d + ","
                + "\"speed\":" + this.getSpeed() + ","
                + "\"pos\":\"" + _pos.toString() + "\""
                + "}"
                + "}";
        return ans;
    }

    /**
     * Sets the value parameter of the CL_Agent.
     *
     * @param v
     */
    private void setMoney(double v) {
        _value = v;
    }

    /**
     * Sets the next dest of the CL_Agent.
     *
     * @param dest
     */
    public boolean setNextNode(int dest) {
        boolean ans = false;
        int src = this._curr_node.getKey();
        this._curr_edge = _gg.getEdge(src, dest);
        if (_curr_edge != null) {
            ans = true;
        } else {
            _curr_edge = null;
        }
        return ans;
    }

    /**
     * Sets the _curr_node parameter of the CL_Agent.
     *
     * @param src
     */
    public void setCurrNode(int src) {
        this._curr_node = _gg.getNode(src);
    }

    /**
     * @return true if the CL_Agent is moving
     */
    public boolean isMoving() {
        return this._curr_edge != null;
    }

    /**
     * @return a string representation of the CL_Agent. In general returns a
     * string that "textually represents" this CL_Agent.
     * The result is a concise but informative representation
     * that is easy for a person to read.
     */
    public String toString() {
        String ans = " |" + this.getID() + "," + _pos + ", " + isMoving() + "," + this.getValue() + "," + this._curr_node.getKey() + "| ";
        return ans;
    }

    /**
     * @return the _id parameter of the CL_Agent.
     */
    public int getID() {
        // TODO Auto-generated method stub
        return this._id;
    }

    /**
     * @return the _pos parameter of the CL_Agent.
     */
    public geo_location getLocation() {
        // TODO Auto-generated method stub
        return _pos;
    }

    /**
     * @return the _value parameter of the CL_Agent.
     */
    public double getValue() {
        // TODO Auto-generated method stub
        return this._value;
    }

    /**
     * @return the next dest of the CL_Agent.
     */
    public int getNextNode() {
        int ans = -2;
        if (this._curr_edge == null) {
            ans = -1;
        } else {
            ans = this._curr_edge.getDest();
        }
        return ans;
    }

    /**
     * @return the _speed parameter of the CL_Agent.
     */
    public double getSpeed() {
        return this._speed;
    }

    /**
     * Sets the _speed parameter of the CL_Agent.
     *
     * @param v
     */
    public void setSpeed(double v) {
        this._speed = v;
    }

    /**
     * @return the _sg_dt parameter of the CL_Agent.
     */
    public long get_sg_dt() {
        return _sg_dt;
    }


}
