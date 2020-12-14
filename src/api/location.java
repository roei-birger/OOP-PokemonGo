package api;

/**
 *  This class implements a geo_location interface.
 * that represents a geo location <x,y,z>, aka Point3D.
 */
public class location implements geo_location, java.io.Serializable {
    double x, y, z;
    /**
     * Constructs a location with receives data.
     */
    public location(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * This constructor deeply copies the location.
     * This function is essential to copy a graph.
     *
     * @param n
     */
    public location(geo_location n) {
        if (n != null) {
            double tX = n.x();
            double tY = n.y();
            double tZ = n.z();
            this.x = tX;
            this.y = tY;
            this.z = tZ;
        }
    }
    /**
     * @return the x parameter of the location.
     */
    @Override
    public double x() {
        return this.x;
    }
    /**
     * @return the y parameter of the location.
     */
    @Override
    public double y() {
        return this.y;
    }
    /**
     * @return the z parameter of the location.
     */
    @Override
    public double z() {
        return this.z;
    }

    /**
     * @return the distance between two locations.
     */
    @Override
    public double distance(geo_location g) {
        return Math.sqrt(Math.pow((this.x - g.x()), 2) + Math.pow((this.y - g.y()), 2) + Math.pow((this.z - g.z()), 2));
    }
    /**
     * Indicates whether some other location is "equal to" this one.
     * by examining each element in the location obj.
     *
     * @param o (location)
     * @return true if this location is the same as the location; false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof location))
            return false;
        location t = (location) o;
        if (this.x == t.x() & this.y == t.y() & this.z == t.z()) {
            return true;
        }
        return false;
    }
    /**
     * @return a string representation of the location. In general returns a
     * string that "textually represents" this location.
     * The result is a concise but informative representation
     * that is easy for a person to read.
     */
    @Override
    public String toString() {
        return "x=" + x +
                ", y=" + y +
                ", z=" + z;
    }
}


