import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedList;

public final class DataBase {
	private static connection;

	private DataBase(){	}

	public static void setConnection(Connection connect){
		connection = connect;
	}

	public static getBuildingWays(){
        String query = "select tags->'name' as nom, ST_X(geom) as longitude, ST_Y(geom) as latitude from nodes where tags->'name' like ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, args[0]);
            ResultSet rs = statement.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            System.out.println(rsmd.getColumnName(1) + " | " + rsmd.getColumnName(2) + " | " + rsmd.getColumnName(3));
            while(rs.next()){
                System.out.println(rs.getString(1) + ", " + rs.getFloat(2) + ", " + rs.getFloat(3));
            }
        } catch(SQLException e){
            System.err.println("Error: " + e.getMessage());
        }
	}
}
