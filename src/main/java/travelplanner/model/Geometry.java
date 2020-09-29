package travelplanner.model;


/**
 * This class includes three coordinates that
 * describe a place: center, northeast and southwest
 */
@lombok.Data
public class Geometry {
    private Location location;
    private Viewport viewport;

    @lombok.Data
    public static class Location {
        private double lat;
        private double lng;
    }

    @lombok.Data
    public static class Viewport {
        private Location northeast;
        private Location southwest;
    }
}
