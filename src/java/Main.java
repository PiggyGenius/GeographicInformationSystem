import database.DataBase;
import database.Utils;
import geoexplorer.gui.MapPanel;
import geoexplorer.gui.GeoMainFrame;
import geoexplorer.gui.Point;
import geoexplorer.gui.Polygon;
import geoexplorer.gui.LineString;
import geoexplorer.gui.CoordinateConverter;
import controller.Drawer;
import model.Quartier;

import java.util.List;
import java.util.LinkedList;



public class Main {

	public static final void main(String[] args){
		double x = 0.0;
		double y = 0.0;
		double mapWidth = 500.0;

		DataBase.setConnection(Utils.getConnection());
		//List<Polygon> buildingWays = DataBase.getBuildingWays();
		//List<LineString> roads = DataBase.getRoadWays();
		List<Quartier> quartiers = DataBase.getQuartierSchool();
        Utils.closeConnection();

		MapPanel map = new MapPanel(x, y, mapWidth);
		GeoMainFrame frame = new GeoMainFrame("Grenoble map", map);

		Drawer drawer = new Drawer(map);
		//drawer.drawRoads(roads);
		//drawer.drawBuildings(buildingWays);

		for(Quartier quartier: quartiers){
			System.out.println(quartier);
		}
    }
}
