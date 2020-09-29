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
import travelplanner.service.RouteAttractionService;

import java.util.ArrayList;
import java.util.List;

@Repository
public class AttractionDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private RouteAttractionService routeAttractionService;

    public void addAttraction(Attraction attraction) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.save(attraction);
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

    public Attraction getAttractionByHashCode(String hashcode) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Attraction> criteriaQuery = builder.createQuery(Attraction.class);
            //select plan.id from plan
            Root<Attraction> root = criteriaQuery.from(Attraction.class);
            criteriaQuery.select(root).where(builder.equal(root.get("hashcode"), hashcode));
            Attraction attraction = session.createQuery(criteriaQuery).getSingleResult();
            session.getTransaction().commit();
            return attraction;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Attraction getAttractionById(int attractionId) {
        Attraction attraction = null;
        try {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Attraction> criteriaQuery = builder.createQuery(Attraction.class);
            Root<Attraction> root = criteriaQuery.from(Attraction.class);
            criteriaQuery.select(root).where(builder.equal(root.get("id"), attractionId));
            attraction = session.createQuery(criteriaQuery).getSingleResult();
            session.getTransaction().commit();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return attraction;
    }

    public List<Attraction> getAttractionsByRouteId(int routeId) {
        List<Attraction> attractions = new ArrayList<>();
        try {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            List<RouteAttraction> routeAttractions = routeAttractionService.getRouteAttractionsByRouteId(routeId);
            for (RouteAttraction routeAttraction : routeAttractions) {
                attractions.add(routeAttraction.getAttraction());
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (attractions != null && attractions.size() != 0) {
            return attractions;
        }
        return null;
    }
}