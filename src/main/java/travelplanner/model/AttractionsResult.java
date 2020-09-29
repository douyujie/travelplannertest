package travelplanner.model;


import java.util.List;

/**
 * stores the information of the tourist attractions
 * in a city. At most 20 records are stored per API call
 * DONT CHANGE THE FILED NAMES because they're binded with 
 * json data returned from the API call
 */
@lombok.Data
public class AttractionsResult {
    private String status;
    private String next_page_token;
    private double[] coordinate;
    private List<String> allTypes;
    private Attraction[] results;

    @lombok.Data
    public static class Attraction {
        private boolean display = true;
        private boolean checked = false;
        private String business_status;
        private Geometry geometry;
        private String icon;
        private String name;
        private String place_id;
        private List<String> types;
        private float rating;
        private String description;
        private int key;
    }
}
