package travelplanner.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import travelplanner.dao.UserPlanDao;
import travelplanner.model.entity.UserPlan;

import java.util.List;

@Service
public class UserPlanService {

    @Autowired
    private UserPlanDao userPlanDao;

    public List<UserPlan> getAllUserPlans(){
        return userPlanDao.getAllUserPlans();
    }

    public List<UserPlan> getUserPlansByUserId(int userId) {
        return userPlanDao.getUserPlansByUserId(userId);
    }

    public UserPlan getUserPlanByUserIdAndPlanId(int userId, int planId) {
        return userPlanDao.getUserPlanByUserIdAndPlanId(userId, planId);
    }
}
