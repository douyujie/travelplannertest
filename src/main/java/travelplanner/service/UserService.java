package travelplanner.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import travelplanner.dao.UserDao;
import travelplanner.model.entity.User;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public void addUser(User user){
        userDao.addUser(user);
    }

    public User getUserByUserName(String userName){
        return userDao.getUserByUserName(userName);
    }

    public User getUserByUserId(int userId){
        return userDao.getUserByUserId(userId);
    }
}
