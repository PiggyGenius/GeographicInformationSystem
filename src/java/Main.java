import geoexplorer.gui.MapPanel;
import geoexplorer.gui.GeoMainFrame;
import database.Utils;

public class Main {

    public static final void main(String[] args){
		MapPanel panel = new MapPanel(0.0, 0.0, 500.0);
		GeoMainFrame frame = new GeoMainFrame("Grenoble map", panel);
		DataBase.setConnection(Utils.getConnection());
        Utils.closeConnection();
    }
}
