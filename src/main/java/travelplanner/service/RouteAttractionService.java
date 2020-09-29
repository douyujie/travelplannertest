package travelplanner.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import travelplanner.dao.RouteAttractionDao;
import travelplanner.model.entity.RouteAttraction;

import java.util.List;

@Service
public class RouteAttractionService {

    @Autowired
    private RouteAttractionDao routeAttractionDao;

    public void addRouteAttraction(RouteAttraction routeAttraction) {
        routeAttractionDao.addRouteAttraction((routeAttraction));
    }

    public List<RouteAttraction> getRouteAttractionsByRouteId(int routeId) {
        return routeAttractionDao.getRouteAttractionsByRouteId(routeId);
    }

    public RouteAttraction getRouteAttractionsByRouteIdAndAttractionId(int routeId, int attractionId) {
        return routeAttractionDao.getRouteAttractionsByRouteIdAndAttractionId(routeId, attractionId);
    }
}
