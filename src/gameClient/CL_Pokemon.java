package gameClient;

import api.edge_data;
import gameClient.util.Point3D;
import org.jetbrains.annotations.NotNull;


/**
 * This class represents a Pokemon in the game.
 */
public class CL_Pokemon implements Comparable<CL_Pokemon> {
    private edge_data _edge;
    private double _value;
    private int _type;
    private Point3D _pos;
    private double min_dist;
    private int min_ro;

    /**
     * Constructs a CL_Pokemon with receives data.
     *
     * @param p
     * @param t
     * @param v
     * @param s
     * @param e
     */
    public CL_Pokemon(Point3D p, int t, double v, double s, edge_data e) {
        _type = t;
        _value = v;
        set_edge(e);
        _pos = p;
        min_dist = -1;
        min_ro = -1;
    }

    /**
     * @return a string representation of the CL_Pokemon. In general returns a
     * string that "textually represents" this CL_Pokemon.
     * The result is a concise but informative representation
     * that is easy for a person to read.
     */
    public String toString() {
        return "F:{v=" + _value + ", t=" + _type + ", E=" + _edge + "}";
    }

    /**
     * @return the _edge parameter of the CL_Pokemon.
     */
    public edge_data get_edge() {
        return _edge;
    }

    /**
     * Sets the _edge parameter of the CL_Pokemon.
     *
     * @param _edge
     */
    public void set_edge(edge_data _edge) {
        this._edge = _edge;
    }

    /**
     * @return the _pos parameter of the CL_Pokemon.
     */
    public Point3D getLocation() {
        return _pos;
    }

    /**
     * @return the _type parameter of the CL_Pokemon.
     */
    public int getType() {
        return _type;
    }

    /**
     * @return the _value parameter of the CL_Pokemon.
     */
    public double getValue() {
        return _value;
    }

    /**
     * The method defines a linear order between two sides in a graph for comparing CL_Pokemon.
     *
     * @param o
     * @return @return 0 if equals , 1 if the given obj are grater and -1 if else.
     */
    @Override
    public int compareTo(@NotNull CL_Pokemon o) {
        if (this.getValue() < o.getValue())
            return 1;
        else if (this.getValue() > o.getValue())
            return -1;
        else return 0;
    }
}
