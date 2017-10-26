package controller;

import geoexplorer.gui.GeoMainFrame;
import geoexplorer.gui.MapPanel;
import geoexplorer.gui.CoordinateConverter;
import geoexplorer.gui.Polygon;
import geoexplorer.gui.LineString;
import geoexplorer.gui.Point;
import model.Quartier;

import java.util.HashMap;
import java.util.List;
import java.awt.Color;

public class Drawer{
	private MapPanel map;

    public Drawer(MapPanel map){
		this.map = map;
    }

	public void drawBuildings(List<Polygon> buildings){
		for(Polygon polygon: buildings){
			map.addPrimitive(polygon);
		}
		map.autoAdjust();
	}

	public void drawRoads(List<LineString> roads){
		for(LineString linestring: roads){
			map.addPrimitive(linestring);
		}
		map.autoAdjust();
	}

	public void drawAmenity(List<Quartier> quartierList){
		Point point;
		for(Quartier quartier: quartierList){
			point = new Point(quartier.getLon(), quartier.getLat(), Color.RED);
			point.setShape(Point.CROSS);
			map.addPrimitive(point);
		}
		map.autoAdjust();
	}
}
