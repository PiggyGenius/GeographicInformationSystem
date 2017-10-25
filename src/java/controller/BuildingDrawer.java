package controller;

import geoexplorer.gui.GeoMainFrame;
import geoexplorer.gui.CoordinateConverter;
import geoexplorer.gui.Polygon;
import geoexplorer.gui.Point;

import java.util.HashMap;
import java.util.List;
import java.awt.Graphics2D;

public class BuildingDrawer {
	private GeoMainFrame frame;
	private CoordinateConverter converter;

    public BuildingDrawer(GeoMainFrame frame, CoordinateConverter converter){
		this.frame = frame;
		this.converter = converter;
    }

	public void draw(List<Polygon> buildings){
		for(Polygon polygon: buildings){
			polygon.draw((Graphics2D) this.frame.getGraphics(), this.converter);
		}
		//Polygon polygon = new Polygon();
		//polygon.addPoint(new Point(0.0, 0.0));
		//polygon.addPoint(new Point(0.0, 100.0));
		//polygon.addPoint(new Point(100.0, 100.0));
		//polygon.addPoint(new Point(100.0, 0.0));
		//polygon.addPoint(new Point(0.0, 0.0));
		//polygon.draw((Graphics2D) this.frame.getGraphics(), this.converter);
	}
}
