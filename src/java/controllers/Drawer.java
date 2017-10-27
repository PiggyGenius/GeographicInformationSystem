package controllers;

import geoexplorer.gui.GeoMainFrame;
import geoexplorer.gui.MapPanel;
import geoexplorer.gui.CoordinateConverter;
import geoexplorer.gui.Polygon;
import geoexplorer.gui.LineString;
import geoexplorer.gui.Point;
import models.Quartier;

import java.util.HashMap;
import java.util.List;

public class Drawer{
	private MapPanel map;

    public Drawer(MapPanel map){
		this.map = map;
    }

	public void drawPolygons(List<Polygon> polygons){
		if (polygons != null) {
			for(Polygon polygon: polygons){
				map.addPrimitive(polygon);
			}
		}
	}

	public void drawLineStrings(List<LineString> lines) {
		if (lines != null) {
			for(LineString linestring: lines){
				map.addPrimitive(linestring);
			}
		}
	}

	public void drawAmenity(List<Quartier> quartierList){
		if (quartierList != null) {
			Point point;
			for(Quartier quartier: quartierList){
				point = new Point(quartier.getLon(), quartier.getLat(), quartier.getColor());
				point.setShape(Point.CIRCLE);
				map.addPrimitive(point);
			}
		}
	}

	public void drawPoints(List<Point> pointList) {
		if (pointList != null) {
			for (Point point: pointList) {
				point.setShape(Point.SQUARE);
				map.addPrimitive(point);
			}
		}
	}
}
