package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.LinkedList;

public final class DataBase {
	private static Connection connection;

	private DataBase(){}

	public static void setConnection(Connection connect){
		connection = connect;
	}

	public static List<Line> getBuildingWays(){
		String query = "select id, ST_X((ST_DumpPoints(linestring)).geom), "
		   + "ST_Y((ST_DumpPoints(linestring)).geom) from ways where tags?'building'";
		// this requests gives a list of nodes, so we need the id of the line to
		// know when a node is the first point of a new line

		List<Line> lineList = new LinkedList<Line>();

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();

			int id;
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
