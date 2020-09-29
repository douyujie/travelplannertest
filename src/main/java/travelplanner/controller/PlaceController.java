package travelplanner.controller;

import org.springframework.web.bind.annotation.*;
import travelplanner.model.*;
import travelplanner.rpc.GoogleMapClient;
import travelplanner.rpc.WikiClient;

import org.springframework.stereotype.Controller;

import java.util.*;


/**
 * the controller is defined to get the information of places 
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
public class PlaceController {
	/**
	 * Return tourist attractions of the input city
	 */
    @GetMapping ("/search")
	@ResponseBody
    public BaseResponse<AttractionsResult> getNearbyPlaces(@RequestParam("city") String city) {
        GoogleMapClient client = new GoogleMapClient();
//        WikiClient wikiClient = new WikiClient();
        Set<String> allTypes = new HashSet<>();
        allTypes.add("All");
        try {
        	AttractionsResult res = client.getAttractionsResult(city);

            for (int i = 0; i < res.getResults().length; i++) {
                AttractionsResult.Attraction cur = res.getResults()[i];

                cur.setKey(i);

                List<String> originTypes = res.getResults()[i].getTypes();
                List<String> modifiedTypes = new ArrayList<>(originTypes);
                HashSet<String> set = new HashSet<>(
                        Arrays.asList("tourist_attraction","point_of_interest", 
                        "establishment", "general_contractor", "premise"));

                modifiedTypes.removeIf(set::contains);

                for (int j = 0; j < modifiedTypes.size(); j++) {
                    String modifiedType = modifiedTypes.get(j).replace("_", " ");
                    modifiedTypes.set(j, modifiedType);
                    allTypes.add(modifiedType);
                }

                if (modifiedTypes.size() == 0) modifiedTypes.add("attraction");
                cur.setTypes(modifiedTypes);

//                String description = wikiClient.getWikiDescription(cur.getName());
//                cur.setDescription(description);

            }
            res.setAllTypes(new ArrayList<>(allTypes));
            System.out.println(allTypes);

        	return new BaseResponse<>("200", res, "");
        } catch (Exception e) {
        	System.out.println(e.getMessage());
        	return null;
        }
    }
    

    /**
     * Return the detail of a place
     * @param id: returned by the google Map API in getNearbyPlaces
     */
    @GetMapping("/detail")
	@ResponseBody
    public BaseResponse<AttractionDetail> getPlaceDetail(@RequestParam("id") String id) {
        GoogleMapClient client = new GoogleMapClient();
        WikiClient wikiClient = new WikiClient();
        try {
        	AttractionDetail res = client.getPlaceDetailResult(id);
        	res.getResult().setDescription(wikiClient.getWikiDescription(res.getResult().getName()));
        	return new BaseResponse<>("200", res, "");
        } catch (Exception e) {
        	System.out.println(e.getMessage());
        	return null;
        }
    }

    @RequestMapping("/login")
    @ResponseBody
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout) {
        return "login";
    }
}
