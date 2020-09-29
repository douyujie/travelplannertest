package travelplanner.rpc;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

import travelplanner.model.*;
import travelplanner.Const;

import java.util.Iterator;

/**
 * this class calls Wiki API for description of name of place
 *
 */
public class WikiClient {
    private static final String DESCRIPTION_URL =
            Const.DESCRIPTION_BASE_URL + "titles=%s";

    private RestTemplate restTemplate;

    public WikiClient() {
        restTemplate = new RestTemplate();
    }

    /**
     * @param name: name of place
     * @return the description of place from Wiki API
     * object
     */
    public String getWikiDescription(String name) {
        PlaceDescription placeDescription = new PlaceDescription();
        name = name.replaceAll("\\s{2,}", " ").trim();
        String[] nameStrings = name.split(" ");
        StringBuilder nameSb = new StringBuilder();
        for (String nameString : nameStrings) {
            nameSb.append(nameString);
            nameSb.append("+");
        }
        nameSb.deleteCharAt(nameSb.length() - 1);
        String url = String.format(DESCRIPTION_URL, nameSb.toString());
        ResponseEntity<JsonNode> wikiJson = restTemplate.getForEntity(url, JsonNode.class);
        JsonNode wikiJsonRoot = wikiJson.getBody();
        String res = "";
        if (wikiJsonRoot != null && wikiJsonRoot.get(Const.QUERY) != null &&
                wikiJsonRoot.get(Const.QUERY).get(Const.PAGES) != null) {
            JsonNode wikiJsonPage = wikiJsonRoot.get(Const.QUERY).get(Const.PAGES);
            Iterator<String> wikiPageIds = wikiJsonPage.fieldNames();
            String wikiPageId = wikiPageIds.next();
            JsonNode wikiJsonData = wikiJsonPage.get(wikiPageId);
            placeDescription.setPageId(wikiJsonData.get(Const.PAGEID).intValue());
            placeDescription.setName(wikiJsonData.get(Const.TITLE).textValue());
            placeDescription.setDescription(wikiJsonData.get(Const.EXTRACT).textValue());
            if (name.toLowerCase().equals(placeDescription.getName().toLowerCase())) {
                res = placeDescription.getDescription();
            } else {
                res = Const.DESCRIPTION_NOT_AVAILABLE;
            }
        }
        return res;
    }
}
