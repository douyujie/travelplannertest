package travelplanner.dao;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import travelplanner.model.entity.*;

@Repository
public class CityDao {
    @Autowired
    private SessionFactory sessionFactory;

    public void addCity(City city) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.save(city);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            assert session != null;
            session.getTransaction().rollback();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public City getCityByName(String cityName) {
        City city = null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<City> criteriaQuery = builder.createQuery(City.class);
            Root<City> root = criteriaQuery.from(City.class);
            criteriaQuery.select(root).where(builder.equal(root.get("name"), cityName));
            city = session.createQuery(criteriaQuery).getSingleResult();
            session.getTransaction().commit();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return city;
    }

    public City getCityById(int cityId) {
        City city = null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<City> criteriaQuery = builder.createQuery(City.class);
            Root<City> root = criteriaQuery.from(City.class);
            criteriaQuery.select(root).where(builder.equal(root.get("id"), cityId));
            city = session.createQuery(criteriaQuery).getSingleResult();
            session.getTransaction().commit();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return city;
    }
}
