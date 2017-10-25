package controller;

import geoexplorer.gui.GeoMainFrame;
import geoexplorer.gui.MapPanel;
import geoexplorer.gui.CoordinateConverter;
import geoexplorer.gui.Polygon;
import geoexplorer.gui.Point;

import java.util.HashMap;
import java.util.List;
import java.awt.Graphics2D;

public class BuildingDrawer {
	private MapPanel map;
	private CoordinateConverter converter;

    public BuildingDrawer(MapPanel map, CoordinateConverter converter){
		this.map = map;
		this.converter = converter;
    }

	public void draw(List<Polygon> buildings){
		for(Polygon polygon: buildings){
			map.addPrimitive(polygon);
		}
		map.autoAdjust();
		//Polygon polygon = new Polygon();
		//polygon.addPoint(new Point(0.0, 0.0));
		//polygon.addPoint(new Point(0.0, 100.0));
		//polygon.addPoint(new Point(100.0, 100.0));
		//polygon.addPoint(new Point(100.0, 0.0));
		//polygon.addPoint(new Point(0.0, 0.0));
		//polygon.draw((Graphics2D) this.frame.getGraphics(), this.converter);
	}
}
