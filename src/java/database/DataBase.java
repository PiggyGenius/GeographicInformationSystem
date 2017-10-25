package database;

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
		
		String query = "select linestring from ways where tags?'building' limit 10";
		List<Polygon> buildings = new LinkedList<Polygon>();

        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
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




	private static Polygon toPolygon(org.postgis.LineString linestring) {
		Polygon polygon = new Polygon();
		for (org.postgis.Point p : linestring.getPoints()) {
			polygon.addPoint(new Point(p.getX(), p.getY()));
		}
		return polygon;
	}
}
