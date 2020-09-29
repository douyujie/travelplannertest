package travelplanner.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import travelplanner.model.BaseResponse;
import travelplanner.model.UserPlanData;
import travelplanner.model.entity.*;
import travelplanner.service.*;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class PlanController {

    @Autowired
    private UserService userService;

    @Autowired
    private PlanService planService;

    @Autowired
    private RouteService routeService;

    @Autowired
    private RouteAttractionService routeAttractionService;

    @Autowired
    private AttractionService attractionService;

    @Autowired
    private CityService cityService;

    @Autowired
    private UserPlanService userPlanService;

    @PostMapping(value = "/addplan")
    @ResponseBody
    public BaseResponse<String> addPlan(@RequestBody UserPlanData userPlanData) {
        try {
            String username = userPlanData.getUsername();
            UserPlanData.PlanData planData = userPlanData.getPlanDataList().get(0);
            String planName = planData.getPlanName();
            String cityName = planData.getCity();
            List<UserPlanData.RouteData> routeDataList = planData.getRouteDataList();

            City city = cityService.getCityByName(cityName);
            if (city == null) {
                city = new City();
                city.setName(cityName);
                cityService.addCity(city);
            }

            String planHashCode = String.valueOf(planData.hashCode());
            Plan plan = planService.getPlanByHashCode(planHashCode);
            if (plan == null) {
                plan = new Plan();
                plan.setCity(city);
                plan.setHashcode(planHashCode);
                planService.addPlan(plan);
            }

            for (UserPlanData.RouteData routeData : routeDataList) {
                String routeHashCode = String.valueOf(routeData.hashCode());
                Route route = routeService.getRouteByHashCode(routeHashCode);
                if (route == null) {
                    route = new Route();
                    route.setHashcode(routeHashCode);
                    route.setDay(routeData.getDay());
                    route.setPlan(plan);
                    routeService.addRoute(route);
                }

                List<UserPlanData.AttractionData> attractionDataList = routeData.getAttractionDataList();
                for (int i = 0; i < attractionDataList.size(); i++) {
                    UserPlanData.AttractionData attractionData = attractionDataList.get(i);

                    int hashCode = attractionData.hashCode();
                    Attraction attraction = attractionService.getAttractionByHashCode(String.valueOf(hashCode));
                    if (attraction == null) {
                        attraction = new Attraction();
                        attraction.setName(attractionData.getAttractionName());
                        attraction.setLatitude(attractionData.getLatitude());
                        attraction.setLongitude(attractionData.getLongitude());
                        attraction.setType(attractionData.getType());
                        attraction.setRating(attractionData.getRating());
                        attraction.setHashcode(String.valueOf(hashCode));
                        attractionService.addAttraction(attraction);
                    }

                    RouteAttraction routeAttraction = routeAttractionService
                            .getRouteAttractionsByRouteIdAndAttractionId(route.getId(), plan.getId());
                    if (routeAttraction == null || routeAttraction.getOrder() != i)  {
                        routeAttraction = new RouteAttraction();
                        routeAttraction.setRoute(route);
                        routeAttraction.setAttraction(attraction);
                        routeAttraction.setOrder(i);
                        routeAttractionService.addRouteAttraction(routeAttraction);
                    }
                }

                User user = userService.getUserByUserName(username);
                UserPlan userPlan = userPlanService
                        .getUserPlanByUserIdAndPlanId(user.getId(), plan.getId());
                if (userPlan == null) {
                    planService.addUserPlan(plan, user);
                }
            }
            return new BaseResponse<>("200", null, "Add plan succeeded");
        } catch (Exception e) {
            e.printStackTrace();
            return new BaseResponse<>("500", null, e.getMessage());
        }
    }

    @GetMapping(value = "/allplans")
    @ResponseBody
    public BaseResponse<UserPlanData> getAllPlansByUserName(@RequestParam(value="username") String username) {
        try {
            // Initialize UserPlanData
            UserPlanData userPlanData = new UserPlanData();
            userPlanData.setPlanDataList(new ArrayList<>());
            // Get PlanList and values of attributes from PlanList
            User user = userService.getUserByUserName(username);
            List<Plan> planList = planService.getAllPlansByUserId(user.getId());
            if (planList == null || planList.size() == 0) {
                return new BaseResponse<>("200", null, "No saved plan is available");
            }
            userPlanData.setUsername(username);
            for (Plan plan : planList) {
                String planName = plan.getName();
                int planId = plan.getId();
                String city = plan.getCity().getName();
                int cityId = plan.getCity().getId();
                // Set values of attributes in PlanData
                UserPlanData.PlanData planData = new UserPlanData.PlanData();
                planData.setPlanName(planName);
                planData.setPlanId(planId);
                planData.setCity(city);
                planData.setCityId(cityId);
                List<Route> routeList = plan.getRoutes();
                List<UserPlanData.RouteData> routeDataList = new ArrayList<>();
                for (Route route : routeList) {
                    UserPlanData.RouteData routeData = new UserPlanData.RouteData();
                    routeData.setRouteId(route.getId());
                    routeData.setDay(route.getDay());
                    List<Attraction> attractionList = attractionService.getAttractionsByRouteId(route.getId());
                    List<UserPlanData.AttractionData> attractionDataList = new ArrayList<>();
                    for (Attraction attraction : attractionList) {
                        UserPlanData.AttractionData attractionData = new UserPlanData.AttractionData();
                        attractionData.setAttractionName(attraction.getName());
                        attractionData.setAttactionId(attraction.getId());
                        attractionData.setLatitude(attraction.getLatitude());
                        attractionData.setLongitude(attraction.getLongitude());
                        attractionData.setType(attraction.getType());
                        attractionData.setRating(attraction.getRating());
                        attractionDataList.add(attractionData);
                    }
                    routeData.setAttractionDataList(attractionDataList);
                    routeDataList.add(routeData);
                }
                planData.setRouteDataList(routeDataList);
                userPlanData.getPlanDataList().add(planData);
            }
            return new BaseResponse<>("200", userPlanData, "Get all plans succeeded");
        } catch (Exception e) {
            e.printStackTrace();
            return new BaseResponse<>("500", null, e.getMessage());
        }
    }

    @PostMapping(value = "/saverecommendedplan")
    @ResponseBody
    public BaseResponse<String> saveRecommendedPlanByIds(@RequestParam("username") String username,
                                                         @RequestParam("planid") int planid) {
        try {
            int userId = userService.getUserByUserName(username).getId();
            planService.savePlanByIds(planid, userId);
            return new BaseResponse<>("200", null, "Save recommended plan by ids succeeded");
        } catch (Exception e) {
            e.printStackTrace();
            return new BaseResponse<>("500", null, e.getMessage());
        }
    }

    private String getSHA512(String input){
        String output = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            digest.reset();
            digest.update(input.getBytes("utf8"));
            output = String.format("%0128x", new BigInteger(1, digest.digest()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

    @GetMapping(value = "/getrecommendationplans")
    @ResponseBody
    public BaseResponse<UserPlanData> getRecommendationPlansByUserId(@RequestParam(value = "username") String username, @RequestParam(value = "cityid")
            int cityId){
        try {
            int userId = userService.getUserByUserName(username).getId();
            UserPlanData userPlanData = new UserPlanData();
            userPlanData.setPlanDataList(new ArrayList<>());

            List<Plan> plans = planService.getRecommendationPlansByUserId(userId, cityId);
            if (plans == null || plans.size() == 0) {
                return new BaseResponse<>("200", null, "No saved plan is available");
            }

            userPlanData.setUsername(null);
            for (Plan plan: plans){
                String planName = plan.getName();
                int planId = plan.getId();
                String city = plan.getCity().getName();
                // Set values of attributes in PlanData
                UserPlanData.PlanData planData = new UserPlanData.PlanData();
                planData.setPlanName(planName);
                planData.setPlanId(planId);
                planData.setCity(city);
                planData.setCityId(cityId);
                List<Route> routeList = plan.getRoutes();
                List<UserPlanData.RouteData> routeDataList = new ArrayList<>();
                for (Route route : routeList) {
                    UserPlanData.RouteData routeData = new UserPlanData.RouteData();
                    routeData.setRouteId(route.getId());
                    routeData.setDay(route.getDay());
                    List<Attraction> attractionList = attractionService.getAttractionsByRouteId(route.getId());
                    List<UserPlanData.AttractionData> attractionDataList = new ArrayList<>();
                    for (Attraction attraction : attractionList) {
                        UserPlanData.AttractionData attractionData = new UserPlanData.AttractionData();
                        attractionData.setAttractionName(attraction.getName());
                        attractionData.setAttactionId(attraction.getId());
                        attractionData.setLatitude(attraction.getLatitude());
                        attractionData.setLongitude(attraction.getLongitude());
                        attractionData.setType(attraction.getType());
                        attractionData.setRating(attraction.getRating());
                        attractionDataList.add(attractionData);
                    }
                    routeData.setAttractionDataList(attractionDataList);
                    routeDataList.add(routeData);
                }
                planData.setRouteDataList(routeDataList);
                userPlanData.getPlanDataList().add(planData);
            }
            return new BaseResponse<UserPlanData>("200", userPlanData, "All recommendation result");
        } catch (Exception e){
            return new BaseResponse<>("500",null, e.getMessage());
        }
    }

    @DeleteMapping(value = "/deleteplan")
    @ResponseBody
    public BaseResponse<String> deletePlanByIds(@RequestParam(value = "username") String username, @RequestParam(value = "planid") int planId){
        try {
            int userId = userService.getUserByUserName(username).getId();
            planService.deletePlanByIds(userId, planId);
            return new BaseResponse<>("200", null, "delete successfully");
        }catch (Exception e){
            return new BaseResponse<>("500",null,"server failed to connect");
        }
    }
}