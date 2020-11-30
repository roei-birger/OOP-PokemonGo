package api;

/**
 * //////////////////////////////////////////
 */
public class location implements geo_location, java.io.Serializable {
    double x, y, z;

    public location(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

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
        if (!(o instanceof location))
            return false;
        location t = (location) o;
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


