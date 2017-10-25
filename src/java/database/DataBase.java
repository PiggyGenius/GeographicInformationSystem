package database;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.LinkedList;

public final class DataBase {
	private static Connection connection;

	private DataBase(){}

	public static Connection getConnection() {
		return DataBase.connection;
	}

	public static void setConnection(Connection connect){
		connection = connect;
	}

	public static List<Line> getBuildingWays(){
		
		if (connection == null) {
			System.out.println("ERROR: connection is null (in getBuildingWays)");
			return null;
		}

		String query = "select id, ST_X((ST_DumpPoints(linestring)).geom), "
		   + "ST_Y((ST_DumpPoints(linestring)).geom) from ways where tags?'building' limit 10";
		// this requests gives a list of nodes, so we need the id of the line to
		// know when a node is the first point of a new line

		List<Line> lineList = new LinkedList<Line>();

        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);

			Integer id;
			Integer currId = null;
			Line line = null;
            while(rs.next()){
				id = rs.getInt(1);

				// if it's the first node of the line, start new line
				if (id != currId) {
					currId = id;
					line = new Line();
					lineList.add(line);
				}
				line.addNode(rs.getFloat(2), rs.getFloat(3));
            }
			return lineList;

        } catch(SQLException e){
            System.err.println("Error: " + e.getMessage());
			return null;
        }
	}
}
