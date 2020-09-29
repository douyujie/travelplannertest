package travelplanner.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import travelplanner.model.entity.Plan;
import travelplanner.model.entity.RouteAttraction;
import travelplanner.model.entity.User;
import travelplanner.model.entity.UserPlan;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserPlanDao {
    @Autowired
    private SessionFactory sessionFactory;

    public List<UserPlan> getAllUserPlans() {
        List<UserPlan> allUserPlans = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<UserPlan> criteriaQuery = builder.createQuery(UserPlan.class);
            Root<UserPlan> root = criteriaQuery.from(UserPlan.class);
            criteriaQuery.select(root);
            allUserPlans = session.createQuery(criteriaQuery).getResultList();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return allUserPlans;
    }

    public UserPlan getUserPlanByUserIdAndPlanId(int userId, int planId) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<UserPlan> query = criteriaBuilder.createQuery(UserPlan.class);
            Root<UserPlan> root = query.from(UserPlan.class);
            query.select(root).where(criteriaBuilder.equal(root.get("user"), userId),
                    criteriaBuilder.equal(root.get("plan"), planId));
            UserPlan userPlan = session.createQuery(query).getSingleResult();
            session.getTransaction().commit();
            return userPlan;
        }catch (Exception e) {
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

    public List<UserPlan> getUserPlansByUserId(int userId) {
        List<UserPlan> userPlans = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<UserPlan> criteriaQuery = builder.createQuery(UserPlan.class);
            Root<UserPlan> root = criteriaQuery.from(UserPlan.class);
            criteriaQuery.select(root).where(builder.equal(root.get("user"), userId));
            userPlans = session.createQuery(criteriaQuery).getResultList();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userPlans;
    }
}
