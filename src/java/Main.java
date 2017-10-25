import geoexplorer.gui.MapPanel;
import geoexplorer.gui.GeoMainFrame;
import database.DataBase;
import database.Line;
import database.Utils;

import java.util.List;


public class Main {

    public static final void main(String[] args){
		// MapPanel panel = new MapPanel(0.0, 0.0, 500.0);
		// GeoMainFrame frame = new GeoMainFrame("Grenoble map", panel);
		DataBase.setConnection(Utils.getConnection());

		List<Line> buildingWays = DataBase.getBuildingWays();
		if (buildingWays == null) {
			System.out.println("NULL");
		}
		for (Line l : buildingWays) {
			System.out.println(l);
			System.out.println("---------------");
		}

        Utils.closeConnection();
    }
}
