package me.axyss.quantumcubes.data;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.axyss.quantumcubes.utils.MCHeadsApi;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.sql.*;
import java.util.Base64;


public class HeadDatabase {
    private final Connection connection;

    public HeadDatabase(Path dbPath) {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS heads (id INTEGER PRIMARY KEY, texture TEXT)").execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void refreshHeadData() throws URISyntaxException, IOException {
        String sql = "INSERT OR IGNORE INTO heads (id, texture) VALUES (?, ?)";
        JsonArray headArray = MCHeadsApi.fetchAllHeads();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false); // Crucial for batch processing
            for (JsonElement headObject: headArray) {
                statement.setString(1, ((JsonObject) headObject).get("id").getAsString());
                statement.setString(2, extractTextureLink(((JsonObject) headObject).get("value").getAsString()));
                statement.addBatch();
            }
            statement.executeBatch();
            connection.commit();
            System.out.println("Committed changes!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static String extractTextureLink(String base64Blob) {
        String decodedString = new String(Base64.getDecoder().decode(base64Blob));
        JsonObject parsedJson = (JsonObject) JsonParser.parseString(decodedString);
        return parsedJson.getAsJsonObject("textures").getAsJsonObject("SKIN").get("url").getAsString();
    }

    public URL getHeadTextureURL(String headId) {
        try (PreparedStatement selectStatement = connection.prepareStatement("SELECT texture FROM heads WHERE id = ?")) {
            selectStatement.setString(1, headId);
            String textureUrl = selectStatement.executeQuery().getString("texture");
            return new URL(textureUrl);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}


