package travelplanner.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import travelplanner.dao.RouteDao;
import travelplanner.model.entity.Route;

import java.util.List;

@Service
public class RouteService {

    @Autowired
    private RouteDao routeDao;

    public void addRoute(Route route) {
        routeDao.addRoute(route);
    }

    public Route getRouteByHashCode(String hashcode) {
        return routeDao.getRouteByHashCode(hashcode);
    }

    public List<Route> getRoutesByPlanId(int planId) {
        return routeDao.getRoutesByPlanId(planId);
    }
}
