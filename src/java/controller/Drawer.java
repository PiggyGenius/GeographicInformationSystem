package controller;

import geoexplorer.gui.GeoMainFrame;
import geoexplorer.gui.MapPanel;
import geoexplorer.gui.CoordinateConverter;
import geoexplorer.gui.Polygon;
import geoexplorer.gui.LineString;
import geoexplorer.gui.Point;

import java.util.HashMap;
import java.util.List;
import java.awt.Graphics2D;

public class Drawer{
	private MapPanel map;
	private CoordinateConverter converter;

    public Drawer(MapPanel map, CoordinateConverter converter){
		this.map = map;
		this.converter = converter;
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
}
