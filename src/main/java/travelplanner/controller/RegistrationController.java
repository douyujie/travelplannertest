package travelplanner.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import travelplanner.model.BaseResponse;
import travelplanner.model.entity.User;
import travelplanner.service.UserService;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

//    @RequestMapping(value = "/user/registration", method = RequestMethod.GET)
//    public ModelAndView getRegistrationForm(){
//        User user = new User();
//        return new ModelAndView("register", "user", user);
//    }

    @RequestMapping(value = "/user/registration", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse<String> registerUser(@RequestBody User user) {
        try{
            if (userService.getUserByUserName(user.getUsername()) == null){
            userService.addUser(user);
            return new BaseResponse<>("200", null, "Registration succeeded");}
        else{
            return new BaseResponse<>("409", null, "User already exists");}}
        catch (Exception e){
            e.printStackTrace();
            return new BaseResponse<>("500", null, e.getMessage());
        }
    }
}

