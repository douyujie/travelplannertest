package travelplanner.dao;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import travelplanner.model.entity.Attraction;
import travelplanner.model.entity.Route;
import travelplanner.model.entity.User;

import java.util.ArrayList;
import java.util.List;

@Repository
public class RouteDao {
    @Autowired
    private SessionFactory sessionFactory;

    public void addRoute(Route route) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.save(route);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public Route getRouteByHashCode(String hashcode) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Route> criteriaQuery = builder.createQuery(Route.class);
            //select plan.id from plan
            Root<Route> root = criteriaQuery.from(Route.class);
            criteriaQuery.select(root).where(builder.equal(root.get("hashcode"), hashcode));
            Route route = session.createQuery(criteriaQuery).getSingleResult();
            session.getTransaction().commit();
            return route;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Route> getRoutesByPlanId(int planId) {
        List<Route> routes = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Route> criteriaQuery = builder.createQuery(Route.class);
            Root<Route> root = criteriaQuery.from(Route.class);
            criteriaQuery.select(root).where(builder.equal(root.get("plan"), planId));
            routes = session.createQuery(criteriaQuery).getResultList();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (routes != null && routes.size() != 0) {
            return routes;
        }
        return null;
    }
}
