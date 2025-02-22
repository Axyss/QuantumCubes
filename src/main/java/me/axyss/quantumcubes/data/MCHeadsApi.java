package me.axyss.quantumcubes.data;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Scanner;

class MCHeadsApi {
    private static final String endpoint = "https://minecraft-heads.com/scripts/api.php?cat=%s&ids=true";
    private static final String[] categories = {"alphabet", "animals", "blocks", "decoration", "food-drinks",
                                                "humans", "humanoid", "miscellaneous", "monsters", "plants"};
    private static final Gson gson = new Gson();

    public static JsonArray fetchAllHeads() throws URISyntaxException, IOException {
        JsonArray retrieved = new JsonArray();

        for (String category: categories) {
            URL mcHeadLink = new URI(endpoint.formatted(category)).toURL();
            HttpURLConnection connection = (HttpURLConnection) mcHeadLink.openConnection();
            Scanner s = new Scanner(new InputStreamReader(connection.getInputStream())).useDelimiter("\\A");
            retrieved.addAll(gson.fromJson(s.next(), JsonArray.class));
        }
        return retrieved;
    }
}
