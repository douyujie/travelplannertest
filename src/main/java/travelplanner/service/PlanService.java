package travelplanner.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import travelplanner.dao.PlanDao;
import travelplanner.model.entity.Plan;
import travelplanner.model.entity.User;

import java.util.List;

@Service
public class PlanService {
    @Autowired
    private PlanDao planDao;

    public void addPlan(Plan plan) {
        planDao.addPlan(plan);
    }
    public void addUserPlan(Plan plan, User user) {
        planDao.addUserPlan(plan, user);
    }

    public boolean hashcodeExists(String hashcode) {
        return planDao.hashcodeExists(hashcode);
    }

    public Plan getPlanByHashCode(String hashcode) {
        return planDao.getPlanByHashCode(hashcode);
    }

    public Plan getPlanByPlanId(int planId) {
        return planDao.getPlanByPlanId(planId);
    }

    public List<Plan> getAllPlansByUserId(int userId) {
        return planDao.getAllPlansByUserId(userId);
    }

    public void savePlanByIds(int planId, int userId) {
        planDao.savePlanByIds(planId, userId);
    }

    public List<Plan> getRecommendationPlansByUserId(int userId, int cityId) {
        return planDao.getRecommendationPlansByUserId(userId, cityId);
    }

    public void deletePlanByIds(int userId, int planId) {
        planDao.deletePlanByIds(userId, planId);
    }
}
