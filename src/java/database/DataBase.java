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

	public static void setConnection(Connection connect){
		connection = connect;
	}

	public static List<Polygon> getBuildingWays(){
		
		// String query = "select id, ST_X((ST_DumpPoints(linestring)).geom), "
		//    + "ST_Y((ST_DumpPoints(linestring)).geom) from ways where tags?'building' limit 10";

		String query = "select ST_AsText(linestring) from ways where tags?'building' limit 10";

		List<Polygon> buildings = new LinkedList<Polygon>();

        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);

            while(rs.next()) {
				buildings.add(parseLineString(rs.getString(1)));
			}
			return buildings;
        } catch(SQLException e){
            System.err.println("Error: " + e.getMessage());
			return null;
        }
	}

	private static Polygon parseLineString(String linestring) {
		Polygon polygon = new Polygon(Color.green, Color.red);
		Pattern pattern = Pattern.compile(floatRegex + " " + floatRegex);
		Matcher matcher = pattern.matcher(linestring);
		String[] point;
		while (matcher.find()) {
			point = matcher.group().trim().split(" ");
			polygon.addPoint(new Point(Float.parseFloat(point[0]), 
									   Float.parseFloat(point[1])));
		}
		return polygon;
	}
}
