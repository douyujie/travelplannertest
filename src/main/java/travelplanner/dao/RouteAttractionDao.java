package travelplanner.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.RouteMatcher;
import travelplanner.model.entity.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RouteAttractionDao {

    @Autowired
    private SessionFactory sessionFactory;

    public void addRouteAttraction(RouteAttraction routeAttraction) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            if (routeAttraction.getId() == null) {
                routeAttraction.prePersiste();
            }
            session.saveOrUpdate(routeAttraction);
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

    public RouteAttraction getRouteAttractionsByRouteIdAndAttractionId(int routeId, int attractionId) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<RouteAttraction> query = criteriaBuilder.createQuery(RouteAttraction.class);
            Root<RouteAttraction> root = query.from(RouteAttraction.class);
            query.select(root).where(criteriaBuilder.equal(root.get("route"), routeId), criteriaBuilder.equal(root.get("attraction"), attractionId));
            RouteAttraction routeAttraction = session.createQuery(query).getSingleResult();
            session.getTransaction().commit();
            return routeAttraction;
        } catch (Exception e) {
            e.printStackTrace();
            if (session != null){
                session.getTransaction().rollback();
            }
        }finally {
            if (session != null){
                session.close();
            }
        }
        return null;
    }

    public List<RouteAttraction> getRouteAttractionsByRouteId(int routeId) {
        List<RouteAttraction> routeAttractions = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<RouteAttraction> criteriaQuery = builder.createQuery(RouteAttraction.class);
            Root<RouteAttraction> root = criteriaQuery.from(RouteAttraction.class);
            criteriaQuery.select(root).where(builder.equal(root.get("route"), routeId));
            routeAttractions = session.createQuery(criteriaQuery).getResultList();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return routeAttractions;
    }
}
