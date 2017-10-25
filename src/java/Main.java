import database.DataBase;
import database.Utils;
import geoexplorer.gui.MapPanel;
import geoexplorer.gui.GeoMainFrame;
import geoexplorer.gui.Point;
import geoexplorer.gui.Polygon;
import geoexplorer.gui.CoordinateConverter;
import controller.BuildingDrawer;

import java.util.List;
import java.util.LinkedList;



public class Main {

	public static final void main(String[] args){
		double x = 0.0;
		double y = 0.0;
		double mapWidth = 1.0;
		MapPanel panel = new MapPanel(x, y, mapWidth);
		GeoMainFrame frame = new GeoMainFrame("Grenoble map", panel);
		DataBase.setConnection(Utils.getConnection());
		List<Polygon> buildingWays = DataBase.getBuildingWays();
        Utils.closeConnection();

		/* Create a few polygons */
		List<Polygon> buildings = new LinkedList<>();
		Polygon polygon;
		for(int i = 0; i <= 5; i++){
			polygon = new Polygon();
			for(int j = 0;j <= 4; j++){
				polygon.addPoint(new Point((i + (j * (1/4)) * 100), j * 100));
			}
			polygon.addPoint(new Point(i * 100, 0));
			buildings.add(polygon);
		}
		/* Create a few polygons */

		/* Draw polygons */
		CoordinateConverter converter = new CoordinateConverter(frame.getWidth(), frame.getHeight(), x, y, mapWidth);
		BuildingDrawer buildingDrawer = new BuildingDrawer(frame, converter);
		buildingDrawer.draw(buildingWays);
		//buildingDrawer.draw(buildings);
		/* Draw polygons */

		for (Polygon p : buildingWays) {
			System.out.println(p);
		}
    }
}
