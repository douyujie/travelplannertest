package travelplanner.model;

import travelplanner.Util;

import java.util.List;

@lombok.Data
public class UserPlanData {

    private String username;

    List<PlanData> planDataList;

    @lombok.Data
    public static class PlanData {
        private int planId;
        private String planName;
        private String city;
        private int cityId;

        private List<RouteData> routeDataList;

        @Override
        public int hashCode() {
            int res = 0;
            for (RouteData route : routeDataList) {
                res += route.hashCode() + city.hashCode() + planName.hashCode();
            }
            return res;
        }
    }

    @lombok.Data
    public static class RouteData {
        int routeId;
        int day;
        private List<AttractionData> attractionDataList;

        @Override
        public int hashCode() {
            int res = day;
            for (AttractionData attraction : attractionDataList) {
                res += attraction.hashCode();
            }
            return res;
        }
    }

    @lombok.Data
    public static class AttractionData {
        private String attractionName;
        private int attactionId;
        private float latitude;
        private float longitude;
        private String type;
        private float rating;

        @Override
        public int hashCode() {
            return (int)(latitude * 101 + longitude);
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof AttractionData) {
                AttractionData attractionData = (AttractionData) o;
                return attractionData.latitude == this.latitude &&
                        attractionData.longitude == this.longitude;
            }
            return false;
        }
    }
}
