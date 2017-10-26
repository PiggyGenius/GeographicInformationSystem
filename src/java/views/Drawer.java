package views;

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

	public void drawPolygons(List<Polygon> polygons){
		if (polygons != null) {
			for(Polygon polygon: polygons){
				map.addPrimitive(polygon);
			}
			map.autoAdjust();
		} else {
			System.err.println("Error: null parameter in drawPolygons.");
		}
	}

	public void drawLineStrings(List<LineString> lines) {
		if (lines != null) {
			for(LineString linestring: lines){
				map.addPrimitive(linestring);
			}
			map.autoAdjust();
		} else {
			System.err.println("Error: null parameter in drawLineStrings.");
		}
	}
}
