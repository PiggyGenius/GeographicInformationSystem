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
import java.util.Map;
import java.util.HashMap;
import models.Corners;

import geoexplorer.gui.Polygon;
import geoexplorer.gui.Point;
import geoexplorer.gui.LineString;

public final class DataBase {

	public static final String GrenobleSQL = 
			"ST_XMin(bbox) > 5.7 "
			+ "AND ST_XMax(bbox) < 5.8 "
			+ "AND ST_YMin(bbox) > 45.1 "
			+ "AND ST_YMax(bbox) < 45.2 ";
    private static String WAYS_LINESTRING_SRID = "4326";

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
		String query = "SELECT quartier.quartier, ST_X(ST_Centroid(quartier.the_geom)) AS X, "
			+ "ST_Y(ST_Centroid(quartier.the_geom)) AS Y, COUNT(ways.id) FROM quartier, ways "
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
	public static List<Point> getTransportStations() {
		List<Point> stations = new LinkedList<Point>();
		String query = "SELECT ST_Transform(geom, 2154) FROM nodes "
			+ "WHERE ST_X(geom) BETWEEN 5.7 AND 5.8 "
			+ "AND ST_Y(geom) BETWEEN 45.1 AND 45.2 "
			+ "AND tags?'public_transport'";
		try {
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				org.postgis.PGgeometry geom = (org.postgis.PGgeometry)rs.getObject(1);
				stations.add(toPoint(((org.postgis.Point)geom.getGeometry())));
			}
			return stations;
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

	/** Answer to question 14c */
    public static Corners getCorners() throws SQLException {
        Corners c = new Corners();
        String query = "SELECT MAX(ST_XMax(linestring)) as xmax, " +
                       "MAX(ST_YMax(linestring)) as ymax, "+
                       "MIN(ST_XMin(linestring)) as xmin, " +
                       "MIN(ST_YMin(linestring)) as ymin " +
                       "FROM ways";

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        double xMin = -1, xMax = -1, yMin = -1, yMax = -1;
        while (rs.next()) { // There is one row
            c.xMin = (double)rs.getObject(3);
            c.xMax = (double)rs.getObject(1);
            c.yMin = (double)rs.getObject(4);
            c.yMax = (double)rs.getObject(2);
        }

        return c;
    }

	public static Map<Polygon, Long> getDensities(int nrows, int ncols) throws SQLException {
        Corners corners = DataBase.getCorners();
        double dx = corners.xMax - corners.xMin;
        double dy = corners.yMax - corners.yMin;
        double xSize = dx / ncols;
        double ySize = dy / nrows;
        Map<Polygon, Long> areas = new HashMap<>();

        String query = (
            "CREATE OR REPLACE FUNCTION ST_CreateFishnet(" +
            "nrow integer, ncol integer," +
            "xsize float8, ysize float8,"+
            "x0 float8 DEFAULT 0, y0 float8 DEFAULT 0,"+
            "OUT \"row\" integer, OUT col integer,"+
            "OUT geom geometry)"+
            "RETURNS SETOF record AS\n"+
            "$$"+
            "SELECT i + 1 AS row, j + 1 AS col, ST_Translate(cell, j * $3 + $5, i * $4 + $6) AS geom"+
            "FROM generate_series(0, $1 - 1) AS i,"+
            "generate_series(0, $2 - 1) AS j,"+
            "("+
            "SELECT ('POLYGON((0 0, 0 '||$4||', '||$3||' '||$4||', '||$3||' 0,0 0))')::geometry AS cell"+
            ") AS foo;"+
            "$$ LANGUAGE sql IMMUTABLE STRICT;"
        );
        Statement statement = connection.createStatement();
        ResultSet rs = null;
        
        try {
            statement.executeQuery(query);
        } catch(SQLException e) {
        }

        // /!\ SQL injections
        query = (
            "SELECT ST_Transform(ST_SetSRID(grid.geom, 4326), 2154) , COUNT(ways) " +
            "FROM ST_CreateFishnet(" +
            nrows + ", " + ncols + ", " + xSize + ", " + ySize + ", " + corners.xMin + ", " + corners.yMin +
            ") as grid, ways " +
            "WHERE ST_Contains(ST_SetSRID(grid.geom, " + WAYS_LINESTRING_SRID + "), ST_Centroid(ways.linestring)) " +
            "GROUP BY grid.geom;"
        );
        statement = connection.createStatement();
        rs = statement.executeQuery(query);

        while (rs.next()) { // There is one row
		    org.postgis.PGgeometry geom = (org.postgis.PGgeometry)rs.getObject(1);
            org.postgis.Polygon poly = (org.postgis.Polygon)geom.getGeometry();
            areas.put(toPolygon(poly), (Long)rs.getObject(2));
        }

        return areas;
    }
    
    private static Polygon toPolygon(org.postgis.Polygon poly) {
		Polygon polygon = new Polygon();
		for (int i = 0; i < poly.numPoints(); i++) {
		    org.postgis.Point p = poly.getPoint(i);
			polygon.addPoint(new Point(p.getX(), p.getY()));
		}
		return polygon;
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

	private static Point toPoint(org.postgis.Point point) {
		return new Point(point.getX(), point.getY());
	}
}
