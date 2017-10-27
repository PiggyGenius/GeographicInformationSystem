import models.DataBase;
import models.Utils;
import models.Corners;
import geoexplorer.gui.MapPanel;
import geoexplorer.gui.GeoMainFrame;
import geoexplorer.gui.Point;
import geoexplorer.gui.Polygon;
import geoexplorer.gui.LineString;
import geoexplorer.gui.CoordinateConverter;
import models.Quartier;
import controllers.Drawer;
import java.sql.SQLException;

import java.util.List;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Map;



public class Main {

	private static boolean plotBuildings = false;
	private static boolean plotRoads = false;
	private static boolean plotNoisePollution = false;
	private static boolean plotStations = false;
	private static boolean plotSchools = false;
	private static boolean plotDensities = false;

	public static final void main(String[] args){

		parseArgs(args);

		double x = 0.0;
		double y = 0.0;
		double mapWidth = 500.0;

		DataBase.setConnection(Utils.getConnection());

		List<Quartier> quartiers = null;
		List<Polygon> buildingWays = null;
		List<LineString> roads = null;
		List<Polygon> noisePollution = null;
		List<Point> stations = null;
        Map<Polygon, Long> grid = null;

        if (plotDensities) {
            try {
                grid = DataBase.getDensities(2, 2); // Few subdivisions to make execution faster
            } catch(SQLException e) {
                System.err.println("Error requesting the database: " + e.getMessage());
                return;
            }
        }
        if (plotSchools) quartiers = DataBase.getQuartierSchool();
        if (plotBuildings) buildingWays = DataBase.getBuildingWays();
        if (plotRoads) roads = DataBase.getRoadWays();
        if (plotNoisePollution) noisePollution = DataBase.getNoisePollutedZones();
        if (plotStations) stations = DataBase.getTransportStations();

        Utils.closeConnection();

		MapPanel map = new MapPanel(x, y, mapWidth);
		GeoMainFrame frame = new GeoMainFrame("Grenoble map", map);

		Drawer drawer = new Drawer(map);
		if (plotDensities) {
			Logger.getLogger(Main.class.getName()).log(Level.INFO, "Drawing densities...");
            drawer.drawDensities(grid);
        }
		if (plotBuildings) {
			Logger.getLogger(Main.class.getName()).log(Level.INFO, "Drawing buildings...");
			drawer.drawPolygons(buildingWays);
		}
		if (plotRoads) {
			Logger.getLogger(Main.class.getName()).log(Level.INFO, "Drawing roads...");
			drawer.drawLineStrings(roads);
		}
		if (plotNoisePollution) {
			Logger.getLogger(Main.class.getName()).log(Level.INFO, "Drawing noise pollution...");
			drawer.drawPolygons(noisePollution);
		}
		if (plotStations) {
			Logger.getLogger(Main.class.getName()).log(Level.INFO, "Drawing transport stations...");
			drawer.drawPoints(stations);
		}
		if (plotSchools) {
			Logger.getLogger(Main.class.getName()).log(Level.INFO, "Drawing schools...");
			drawer.drawAmenity(quartiers);
			printColorLegend();
		}

		map.autoAdjust();
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

	private static void parseArgs(String[] args) {
		if (args.length == 0) {
			plotBuildings = true;
			plotRoads = true;
			plotNoisePollution = true;
			plotStations = true;
			plotSchools = true;
			return;
		}

		for (String s : args) {
			switch (s) {
				case "buildings":
					plotBuildings = true; break;
				case "roads":
					plotRoads = true; break;
				case "noise":
					plotNoisePollution = true; break;
				case "stations":
					plotStations = true; break;
				case "schools":
					plotSchools = true; break;
				case "densities":
					plotDensities = true; break;
			}
		}
	}
}
