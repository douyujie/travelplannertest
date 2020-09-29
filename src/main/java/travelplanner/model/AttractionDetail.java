package travelplanner.model;

/**
 * stores the detailed information of a tourist attraction.
 * DONT CHANGE THE FILED NAMES because they're binded with 
 * json data returned from the API call
 */
@lombok.Data
public class AttractionDetail {
    private String status;
    private Detail result;

    @lombok.Data
    public static class Detail {
        private String name;
        private Geometry geometry;
        private String business_status;
        private String description;

        private String formatted_address;
        private String formatted_phone_number;
        private String international_phone_number;
        private Photo[] photos;
        private OpeningHours opening_hours;

        private String icon;
        private String[] types;

        private int price_level;
        private Double rating;
        private int user_ratings_total;
        private Review[] reviews;

        private String url;
        private String website;
    }

    @lombok.Data
    public static class OpeningHours {
        private boolean opening;
        private String[] weekday_text;
    }

    @lombok.Data
    public static class Photo {
        private double height;
        private double width;
        private String photo_reference;
    }

    @lombok.Data
    public static class Review {
        private String author_name;
        private String language;
        private double rating;
        private String relative_time_description;
        String text;
        long time;
    }
}
