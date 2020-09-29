package travelplanner.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import travelplanner.dao.CityDao;
import travelplanner.model.entity.City;

@Service
public class CityService {

    @Autowired
    private CityDao cityDao;

    public void addCity(City city) {
        cityDao.addCity((city));
    }

    public City getCityByName(String name) {
        return cityDao.getCityByName(name);
    }

    public City getCityById(int cityId) {
        return cityDao.getCityById(cityId);
    }
}
