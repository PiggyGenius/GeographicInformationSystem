import geoexplorer.gui.MapPanel;
import geoexplorer.gui.GeoMainFrame;
import geoexplorer.gui.Polygon;
import database.DataBase;
import database.Utils;

import java.util.List;



public class Main {

    public static final void main(String[] args){
		// MapPanel panel = new MapPanel(0.0, 0.0, 500.0);
		// GeoMainFrame frame = new GeoMainFrame("Grenoble map", panel);
		DataBase.setConnection(Utils.getConnection());
		List<Polygon> buildingWays = DataBase.getBuildingWays();
        Utils.closeConnection();

		for (Polygon p : buildingWays) {
			System.out.println(p);
		}
    }
}
