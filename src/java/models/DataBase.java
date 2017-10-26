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

import geoexplorer.gui.Polygon;
import geoexplorer.gui.Point;
import geoexplorer.gui.LineString;

public final class DataBase {

	public static final String GrenobleSQL = 
			"ST_XMin(bbox) > 5.7 "
			+ "AND ST_XMax(bbox) < 5.8 "
			+ "AND ST_YMin(bbox) > 45.1 "
			+ "AND ST_YMax(bbox) < 45.2 ";

	private static Connection connection;

	private DataBase(){}

	public static Connection getConnection() {
		return DataBase.connection;
	}

	public static void setConnection(Connection connect) {
		connection = connect;
	}

	public static List<Quartier> getQuartierSchool(){
		List<Quartier> quartiers = new LinkedList<Quartier>();
		ResultSet rs = getQuartierAmenity("school");
		try {
			while (rs.next())
				quartiers.add(new Quartier(rs.getString(1), rs.getFloat(2), rs.getFloat(3), rs.getInt(4)));
			return quartiers;
		} catch(SQLException e){
			System.err.println("Error: " + e.getMessage());
			return null;
		}
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
		String query = "SELECT ST_Transform(linestring, 2154) FROM ways "
			+ "WHERE " + GrenobleSQL + " AND tags?'" + tag + "' "
			//+ "LIMIT 10000"
			;
        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery(query);
        } catch(SQLException e){
            System.err.println("Error: " + e.getMessage());
			return null;
		}
	}

	private static ResultSet getQuartierAmenity(String amenity){
		// the_geom is already in srid 2154
		String query = "SELECT quartier.quartier, ST_X(ST_Centroid(quartier.the_geom)) AS X, ST_Y(ST_Centroid(quartier.the_geom)) AS Y, COUNT(ways.id) FROM quartier, ways "
			+ "WHERE ways.tags->'amenity' = '" + amenity + "' "
			+ "AND ST_Contains(quartier.the_geom, ST_Transform(ways.linestring, 2154)) "
			+ "GROUP BY quartier.quartier, X, Y"
			;
		try {
			Statement statement = connection.createStatement();
			return statement.executeQuery(query);
		} catch(SQLException e){
			System.err.println("Error: " + e.getMessage());
			return null;
		}
	}

	/** Answer to question 13 */
	public static List<LineString> getBoundaries() {
		List<LineString> boundaries = new LinkedList<LineString>();
		String query = "SELECT ST_Transform(linestring, 2154) FROM ways "
			+ "WHERE " + GrenobleSQL + "AND "
			+ "tags->'boundary' = 'administrative' "
			;
		try {
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				org.postgis.PGgeometry geom = (org.postgis.PGgeometry)rs.getObject(1);
				boundaries.add(toLineString(((org.postgis.LineString)geom.getGeometry())));
			}
			return boundaries;
		} catch (SQLException e) {
			System.err.println("Error: " + e.getMessage());
			return null;
		}
	}


	/** Answer to question 14b */
	public static List<Polygon> getNoisePollutedZones() {
		List<Polygon> res = new LinkedList<Polygon>();
		String query = "SELECT ST_Buffer(ST_Transform(linestring, 2154), 200) FROM ways "
			+ "WHERE " + GrenobleSQL+ "AND ("
			+ "tags->'aeroway' = 'aerodrome'"
			+ "OR tags->'highway' = 'motorway'"
			+ "OR tags->'railway' = 'rail'"
			+ ")"
			;
		try {
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				org.postgis.PGgeometry geom = (org.postgis.PGgeometry)rs.getObject(1);

				if (geom.getGeometry() instanceof org.postgis.Polygon) {
					org.postgis.Polygon pol = (org.postgis.Polygon)geom.getGeometry();
					assert(pol.numRings() == 1);
					res.add(toPolygon(pol.getRing(0)));
				} else if (geom.getGeometry() instanceof org.postgis.MultiPolygon) {
					org.postgis.MultiPolygon multiPol = (org.postgis.MultiPolygon)geom.getGeometry();
					for (org.postgis.Polygon p : multiPol.getPolygons()) {
						assert(p.numRings() == 1);
						res.add(toPolygon(p.getRing(0)));
					}
				} else {
					System.out.println("ERROR");
				}
			}
		} catch(SQLException e) {
            System.err.println("Error: " + e.getMessage());
			return null;
		}
		return res;
	}



	private static Polygon toPolygon(org.postgis.PointComposedGeom geom) {
		Polygon polygon = new Polygon();
		for (org.postgis.Point p : geom.getPoints()) {
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
