package me.axyss.quantumcubes.data;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MCHeadsApi {
    private static final Pattern texturesLink = Pattern.compile("http(s)?://textures.minecraft.net/texture/.*?(?=\")");
    private static final String[] categories = {"alphabet", "animals", "blocks", "decoration", "food-drinks",
                                                "humans", "humanoid", "miscellaneous", "monsters", "plants"};

    @Deprecated
    public static URL getMinecraftTexturesLink(int headId) throws IOException, URISyntaxException {
        URL mcHeadLink = new URI("https://minecraft-heads.com/custom-heads/%s".formatted(String.valueOf(headId))).toURL();
        HttpURLConnection connection = (HttpURLConnection) mcHeadLink.openConnection();
        Scanner s = new Scanner(new InputStreamReader(connection.getInputStream())).useDelimiter("\\A");
        return MCHeadsApi.parseMinecraftTexturesLink(s.next());

    }

    @Deprecated
    private static URL parseMinecraftTexturesLink(String htmlDocument) throws URISyntaxException, MalformedURLException {
        Matcher matcher = texturesLink.matcher(htmlDocument);
        if (matcher.find()) {
            return new URI(matcher.group()).toURL();
        }
        return null;
    }

    public static JsonArray fetchAllHeads() throws URISyntaxException, IOException {
        Gson gson = new Gson();
        JsonArray retrieved = new JsonArray();
        String ENDPOINT = "https://minecraft-heads.com/scripts/api.php?cat=%s&ids=true";

        for (String category: categories) {
            URL mcHeadLink = new URI(ENDPOINT.formatted(category)).toURL();
            HttpURLConnection connection = (HttpURLConnection) mcHeadLink.openConnection();
            Scanner s = new Scanner(new InputStreamReader(connection.getInputStream())).useDelimiter("\\A");
            retrieved.addAll(gson.fromJson(s.next(), JsonArray.class));
        }
        return retrieved;
    }
}
