
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
import travelplanner.model.entity.Plan;
import travelplanner.model.entity.User;
import travelplanner.model.entity.UserPlan;
import travelplanner.service.UserPlanService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class PlanDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private UserPlanService userPlanService;

    public void addPlan(Plan plan) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.saveOrUpdate(plan);
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

    public void addUserPlan(Plan plan, User user) {
        Session session = null;
        UserPlan userPlan = new UserPlan();
        userPlan.setUser(user);
        userPlan.setPlan(plan);
        userPlan.setId(1);
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.saveOrUpdate(plan);
            session.save(userPlan);
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

    public boolean hashcodeExists(String hashcode) {
        List<Plan> planList = null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Plan> criteriaQuery = builder.createQuery(Plan.class);
            //select plan.id from plan
            Root<Plan> root = criteriaQuery.from(Plan.class);
            criteriaQuery.select(root).where(builder.equal(root.get("hashcode"), hashcode));
            planList = session.createQuery(criteriaQuery).getResultList();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return !(planList == null || planList.size() == 0);
    }

    public Plan getPlanByHashCode(String hashcode) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Plan> criteriaQuery = builder.createQuery(Plan.class);
            //select plan.id from plan
            Root<Plan> root = criteriaQuery.from(Plan.class);
            criteriaQuery.select(root).where(builder.equal(root.get("hashcode"), hashcode));
            Plan plan = session.createQuery(criteriaQuery).getSingleResult();
            session.getTransaction().commit();
            return plan;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Plan getPlanByPlanId(int planId) {
        Plan plan = null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Plan> criteriaQuery = builder.createQuery(Plan.class);
            //select plan.id from plan
            Root<Plan> root = criteriaQuery.from(Plan.class);
            criteriaQuery.select(root).where(builder.equal(root.get("id"), planId));
            plan = session.createQuery(criteriaQuery).getSingleResult();
            session.getTransaction().commit();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return plan;
    }

    public List<Plan> getAllPlansByUserId(int userId){
        User user = null;
        List<Plan> plans = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {

            session.beginTransaction();
            // get UserPlan with the same userId
            List<UserPlan> userPlanList = userPlanService.getUserPlansByUserId(userId);
            // where user_id = userId
            for (UserPlan userPlan : userPlanList) {
                plans.add(userPlan.getPlan());
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return plans;
    }

    public void savePlanByIds(int plan_id, int user_id){
        Session session = null;
        try {
            UserPlan userPlan = new UserPlan();
            session = sessionFactory.openSession();
            session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            // save plan
            CriteriaQuery<Plan> criteriaQueryPlan = builder.createQuery(Plan.class);
            //select plan_id from plan
            Root<Plan> rootPlan = criteriaQueryPlan.from(Plan.class);
            // where plan_id == plan.id
            criteriaQueryPlan.select(rootPlan).where(builder.equal(rootPlan.get("id"), plan_id));
            Plan plan = session.createQuery(criteriaQueryPlan).getSingleResult();
            userPlan.setPlan(plan);
            // save user
            CriteriaQuery<User> criteriaQueryUser = builder.createQuery(User.class);
            Root<User> rootUser = criteriaQueryUser.from(User.class);
            criteriaQueryUser.select(rootUser).where(builder.equal(rootUser.get("id"), user_id));
            User user = session.createQuery(criteriaQueryUser).getSingleResult();
            userPlan.setUser(user);
            // set id of userplan

            //save userPlan
            session.saveOrUpdate(userPlan);
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

    public List<Plan> getRecommendationPlansByUserId(int userId, int cityId){
        Set<Plan> plans = new HashSet<>();
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            List<UserPlan> allUserPlanList = userPlanService.getAllUserPlans();

            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<UserPlan> query = criteriaBuilder.createQuery(UserPlan.class);

            Root<UserPlan> root = query.from(UserPlan.class);
            // where user_id != userId

            query.select(root).where(criteriaBuilder.equal(root.get("user"), userId));
            List<UserPlan> singleUserPlanList = session.createQuery(query).getResultList();
            Set<Plan> singlePlanSet = new HashSet<>();
            for (UserPlan uP: singleUserPlanList){
                singlePlanSet.add(uP.getPlan());
            }
            // no need to see repetitive result, besides, transfer UserPlan to Plan as we need not show userId back to User
            for (UserPlan uP: allUserPlanList) {
                Plan tempPlan = uP.getPlan();
                if (tempPlan.getCity().getId() == cityId && !plans.contains(tempPlan) && !singlePlanSet.contains(tempPlan)) {
                    plans.add(tempPlan);
                }
            }
            session.getTransaction().commit();
        }catch (Exception e){
            e.printStackTrace();
            if (session != null){
                session.getTransaction().rollback();
            }
        }finally{
            if (session != null){
                session.close();
            }
        }
        return new ArrayList<>(plans);
    }

    public void deletePlanByIds(int userId, int planId){
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<UserPlan> query = criteriaBuilder.createQuery(UserPlan.class);
            Root<UserPlan> root = query.from(UserPlan.class);
            query.select(root).where(criteriaBuilder.equal(root.get("user"), userId), criteriaBuilder.equal(root.get("plan"), planId));
            UserPlan userPlan = session.createQuery(query).getSingleResult();
            session.delete(userPlan);
            session.getTransaction().commit();
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
    }
}