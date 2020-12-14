package gameClient.util;
/**
 * This class represents a simple 1D range of shape [min,max]
 * @author boaz_benmoshe
 *
 */
public class Range {
	private double _min, _max;

	/**
	 * Constructs a Range with receives data.
	 */
	public Range(double min, double max) {
		set_min(min);
		set_max(max);
	}

	/**
	 * This constructor deeply copies the Range.
	 * @param x
	 */
	public Range(Range x) {
		this(x._min, x._max);
	}
	
	public boolean isIn(double d) {
		boolean inSide = false;
		if(d>=this.get_min() && d<=this.get_max()) {inSide=true;}
		return inSide;
	}
	/**
	 * @return a string representation of the Range. In general returns a
	 * string that "textually represents" this Range.
	 * The result is a concise but informative representation
	 * that is easy for a person to read.
	 */
	public String toString() {
		String ans = "["+this.get_min()+","+this.get_max()+"]";
		if(this.isEmpty()) {ans = "Empty Range";}
		return ans;
	}

	public boolean isEmpty() {
		return this.get_min()>this.get_max();
	}

	/**
	 * @return the _max parameter of the Range.
	 */
	public double get_max() {
		return _max;
	}

	/**
	 * @return the length of the Range.
	 */
	public double get_length() {
		return _max-_min;
	}

	/**
	 *  set's the _max parameter of the Range.
	 *
	 * @param _max
	 */

	private void set_max(double _max) {
		this._max = _max;
	}

	/**
	 * @return the _min parameter of the Range.
	 */
	public double get_min() {
		return _min;
	}

	/**
	 *  set's the _min parameter of the Range.
	 *
	 * @param _min
	 */
	private void set_min(double _min) {
		this._min = _min;
	}


	public double getPortion(double d) {
		double d1 = d-_min;
		double ans = d1/get_length();
		return ans;
	}

	public double fromPortion(double p) {
		return _min+p* get_length();
	}
}
