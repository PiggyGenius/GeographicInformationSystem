import models.DataBase;
import models.Utils;
import geoexplorer.gui.MapPanel;
import geoexplorer.gui.GeoMainFrame;
import geoexplorer.gui.Point;
import geoexplorer.gui.Polygon;
import geoexplorer.gui.LineString;
import geoexplorer.gui.CoordinateConverter;
import models.Quartier;
import views.Drawer;

import java.util.List;
import java.util.LinkedList;



public class Main {

	public static final void main(String[] args){
		double x = 0.0;
		double y = 0.0;
		double mapWidth = 500.0;

		DataBase.setConnection(Utils.getConnection());
		List<Quartier> quartiers = DataBase.getQuartierSchool();
		List<Polygon> buildingWays = DataBase.getBuildingWays();
		List<LineString> roads = DataBase.getRoadWays();
		List<Polygon> noisePollution = DataBase.getNoisePollutedZones();
		List<LineString> boundaries = DataBase.getBoundaries();
        Utils.closeConnection();

		MapPanel map = new MapPanel(x, y, mapWidth);
		GeoMainFrame frame = new GeoMainFrame("Grenoble map", map);

		Drawer drawer = new Drawer(map);
		drawer.drawPolygons(buildingWays);
		drawer.drawAmenity(quartiers);
		drawer.drawLineStrings(roads);
		drawer.drawPolygons(noisePollution);
		drawer.drawLineStrings(boundaries);
		
		printColorLegend();
		drawer.drawLineStrings(boundaries);
    }

	private static void printColorLegend(){
		System.out.println("Number of schools in quartier---> Color of the circle");
		System.out.println("1 --> black");
		System.out.println("2 --> blue");
		System.out.println("3 --> cyan");
		System.out.println("4 --> red");
		System.out.println("5 --> gray");
		System.out.println("6 --> green");
		System.out.println("7 --> lightGray");
		System.out.println("9 --> orange");
		System.out.println("10 --> pink");
		System.out.println("12 --> yellow");
		System.out.println("13 --> magenta");
	}
}
