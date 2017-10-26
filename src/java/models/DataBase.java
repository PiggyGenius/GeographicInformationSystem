package models;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.LinkedList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.awt.Color;

import geoexplorer.gui.Polygon;
import geoexplorer.gui.Point;
import geoexplorer.gui.LineString;

public final class DataBase {

	private static String floatRegex = "-?[0-9]+\\.[0-9]+";

	private static Connection connection;

	private DataBase(){}

	public static Connection getConnection() {
		return DataBase.connection;
	}

	public static void setConnection(Connection connect) {
		connection = connect;
	}

	public static List<Polygon> getBuildingWays() {
		List<Polygon> buildings = new LinkedList<Polygon>();
		ResultSet rs = getWays("building");
		try {
			while (rs.next()) {
				org.postgis.PGgeometry geom = (org.postgis.PGgeometry)rs.getObject(1);
				buildings.add(toPolygon(((org.postgis.LineString)geom.getGeometry())));
			}
			return buildings;
		} catch(SQLException e){
			System.err.println("Error: " + e.getMessage());
			return null;
		}
	}

	public static List<LineString> getRoadWays() {
		List<LineString> roads = new LinkedList<LineString>();
		ResultSet rs = getWays("highway");
		try {
			while (rs.next()) {
				org.postgis.PGgeometry geom = (org.postgis.PGgeometry)rs.getObject(1);
				roads.add(toLineString(((org.postgis.LineString)geom.getGeometry())));
			}
			return roads;
		} catch(SQLException e){
			System.err.println("Error: " + e.getMessage());
			return null;
		}
	}

	private static ResultSet getWays(String tag){
		String query = "SELECT linestring FROM ways "
			+ "WHERE tags?'" + tag + "' "
			+ "AND ST_XMin(bbox) > 5.7 "
			+ "AND ST_XMax(bbox) < 5.8 "
			+ "AND ST_YMin(bbox) > 45.1 "
			+ "AND ST_YMax(bbox) < 45.2 "
			//+ "LIMIT 1000"
			;
        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery(query);
        } catch(SQLException e){
            System.err.println("Error: " + e.getMessage());
			return null;
		}
	}

	private static Polygon toPolygon(org.postgis.LineString linestring) {
		Polygon polygon = new Polygon();
		for (org.postgis.Point p : linestring.getPoints()) {
			polygon.addPoint(new Point(p.getX(), p.getY()));
		}
		return polygon;
	}

	private static LineString toLineString(org.postgis.LineString linestring) {
		LineString line = new LineString();
		for (org.postgis.Point p : linestring.getPoints()) {
			line.addPoint(new Point(p.getX(), p.getY()));
		}
		return line;
	}
}
