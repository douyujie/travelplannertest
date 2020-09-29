package travelplanner.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import travelplanner.dao.AttractionDao;
import travelplanner.model.entity.Attraction;

import java.util.List;

@Service
public class AttractionService {

    @Autowired
    private AttractionDao attractionDao;

    public void addAttraction(Attraction attraction) {
        attractionDao.addAttraction((attraction));
    }

    public Attraction getAttractionByHashCode(String hashcode) {
        return attractionDao.getAttractionByHashCode(hashcode);
    }

    public Attraction getAttractionById(int attractionId) {
        return attractionDao.getAttractionById(attractionId);
    }

    public List<Attraction> getAttractionsByRouteId(int routeId) {
        return attractionDao.getAttractionsByRouteId(routeId);
    }
}
