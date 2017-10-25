package database;

import java.util.List;
import java.util.LinkedList;

public class Line {
	private List<Node> nodeList;

	public Line() {
		this.nodeList = new LinkedList<Node>();
	}

	public void addNode(float lon, float lat) {
		this.nodeList.add(new Node(lon, lat));
	}


	public class Node {
		private float lon;
		private float lat;

		public Node(float lon, float lat) {
			this.lon = lon;
			this.lat = lat;
		}

		public float getLon() {
			return this.lon;
		}

		public float getLat() {
			return this.lat;
		}

		@Override
		public String toString() {
			return "(" + this.lon + "," + this.lat + ")";
		}
	}

	@Override
	public String toString() {
		String res = "";
		for (Node n : this.nodeList) {
			res += n.toString() + "\n";
		}
		return res;
	}
}
