package me.axyss.quantumcubes.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MCHeadsDatabase {
    private static final Pattern texturesLink = Pattern.compile("http(s)?://textures.minecraft.net/texture/.*?(?=\")");

    public static URL getMinecraftTexturesLink(int headId) throws IOException, URISyntaxException {
        URL mcHeadLink = new URI("https://minecraft-heads.com/custom-heads/%s".formatted(String.valueOf(headId))).toURL();
        HttpURLConnection connection = (HttpURLConnection) mcHeadLink.openConnection();
        Scanner s = new Scanner(new InputStreamReader(connection.getInputStream())).useDelimiter("\\A");
        return MCHeadsDatabase.parseMinecraftTexturesLink(s.next());

    }

    private static URL parseMinecraftTexturesLink(String htmlDocument) throws URISyntaxException, MalformedURLException {
        Matcher matcher = texturesLink.matcher(htmlDocument);
        if (matcher.find()) {
            return new URI(matcher.group()).toURL();
        }
        return null;
    }
}
