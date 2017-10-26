package models;
import java.awt.Color;

public class Quartier {
	private String name;
	private double lon;
	private double lat;
	private int amenityCount;
	// We fix on 13 colors since we have 13 schools at most (could be generic, no time)
	// No quartier with 8 school so we can reuse magenta
	private static Color[] color = {Color.black, Color.blue, Color.cyan, Color.red, Color.gray, Color.green, Color.lightGray, Color.magenta, Color.orange, Color.pink, Color.darkGray, Color.yellow, Color.magenta};
	

	public Quartier(String name, double lon, double lat, int amenityCount){
		this.name = name;
		this.lon = lon;
		this.lat = lat;
		this.amenityCount = amenityCount;
	}

	/** @param name Set name of Quartier */
	public void setName(String name) {
		this.name = name;
	}

	/** @return name */
	public String getName() {
		return name;
	}

	/** @param lon Set lon of Quartier */
	public void setLon(double lon) {
		this.lon = lon;
	}

	/** @return lon */
	public double getLon() {
		return lon;
	}

	/** @param lat Set lat of Quartier */
	public void setLat(double lat) {
		this.lat = lat;
	}

	/** @return lat */
	public double getLat() {
		return lat;
	}

	/** @param amenityCount Set amenityCount of Quartier */
	public void setAmenityCount(int amenityCount) {
		this.amenityCount = amenityCount;
	}

	/** @return amenityCount */
	public int getAmenityCount() {
		return amenityCount;
	}

	@Override
	public String toString(){
		return this.name + ": " + this.lon + "; " + this.lat + " --> " + this.amenityCount;
	}

	/** @return color of the quartier */
	public Color getColor() {
		return color[this.amenityCount - 1];
	}
}
