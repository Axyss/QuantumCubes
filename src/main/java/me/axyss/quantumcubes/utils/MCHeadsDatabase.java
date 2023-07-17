package me.axyss.quantumcubes.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MCHeadsDatabase {
    public static URL getMinecraftTexturesLink(int headId) throws IOException, URISyntaxException {
        URL mcHeadLink = new URI("https://minecraft-heads.com/custom-heads/%s".formatted(String.valueOf(headId))).toURL();
        HttpURLConnection connection = (HttpURLConnection) mcHeadLink.openConnection();
        Scanner s = new Scanner(new InputStreamReader(connection.getInputStream())).useDelimiter("\\A");
        return MCHeadsDatabase.parseMinecraftTexturesLink(s.next());

    }

    private static URL parseMinecraftTexturesLink(String htmlDocument) throws URISyntaxException, MalformedURLException {
        Pattern pattern = Pattern.compile("http(s)?://textures.minecraft.net/texture/.*?(?=\")");
        Matcher matcher = pattern.matcher(htmlDocument);
        if (matcher.find()) {
            return new URI(matcher.group()).toURL();
        }
        return null;
    }
}
