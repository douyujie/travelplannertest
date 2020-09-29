package travelplanner.model;



/**
 * class that stores several possible candidates
 * for a search. e.g. When we search Washington, it can be 
 * Washington, or Washington DC, google map API can
 * return several places especially when the search
 * item is obscure
 *
 */
@lombok.Data
public class PlaceCoordinate {
    private Candidate[] candidates;

    @lombok.Data
    public static class Candidate {
        private Geometry geometry;
    }
}