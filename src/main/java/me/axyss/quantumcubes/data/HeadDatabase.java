package me.axyss.quantumcubes.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.sql.*;
import java.time.Instant;
import java.util.Base64;

public class HeadDatabase {
    private final Connection connection;
    private final Path dbPath;

    public HeadDatabase(Path dbPath) {
        this.dbPath = dbPath;
        boolean isNewDatabase = !Files.exists(dbPath);

        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS heads (id INTEGER PRIMARY KEY, texture TEXT)").execute();
            if (isNewDatabase) refreshHeadData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public long getSecondsSinceLastRefresh() {
        try {
            FileTime fileTime = Files.getLastModifiedTime(dbPath);
            return Instant.now().getEpochSecond() - fileTime.toInstant().getEpochSecond();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void refreshHeadData() {
        String sql = "INSERT OR IGNORE INTO heads (id, texture) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            JsonArray headArray = MCHeadsApi.fetchAllHeads();
            connection.setAutoCommit(false); // Crucial for batch processing
            for (JsonElement headObject: headArray) {
                statement.setString(1, ((JsonObject) headObject).get("id").getAsString());
                statement.setString(2, extractTextureLink(((JsonObject) headObject).get("value").getAsString()));
                statement.addBatch();
            }
            statement.executeBatch();
            connection.commit();
        } catch (SQLException | URISyntaxException | IOException e) {
            throw new RuntimeException(e);
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
        } catch (MalformedURLException e) {
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}


