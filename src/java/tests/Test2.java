package test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import database.Utils;

public class Test2 {

    public static final void main(String[] args){
        if(args.length != 1){
            System.out.println("Usage: Test2 <string>");
            return;
        }
        Connection connection = Utils.getConnection();
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
        Utils.closeConnection();
    }
}
