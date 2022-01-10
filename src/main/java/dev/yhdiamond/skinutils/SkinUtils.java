package dev.yhdiamond.skinutils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.Scanner;
import java.util.UUID;

public class SkinUtils {

    public static URL getSkinURLFromUsername(String username) throws IOException {
        return getSkinURLFromUUID(getUUIDFromUsername(username));
    }

    public static URL getSkinURLFromUUID(UUID uuid) throws IOException {
        String requestURL = "https://sessionserver.mojang.com/session/minecraft/profile/" + uuid;
        String response = makeGetRequest(requestURL);
        JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();
        JsonArray properties = jsonResponse.getAsJsonArray("properties");
        JsonObject finalObject = properties.get(1).getAsJsonObject();
        String base64 = finalObject.get("name").getAsString();
        String decodedBase64 = new String(Base64.getDecoder().decode(base64.getBytes()));
        JsonObject decodedJson = JsonParser.parseString(decodedBase64).getAsJsonObject();
        return new URL(decodedJson.getAsJsonObject("textures").getAsJsonObject("SKIN").get("url").getAsString());
    }

    public static URL getSkinURLFromUUID(String uuid) throws IOException {
        return getSkinURLFromUUID(UUID.fromString(uuid));
    }

    public static UUID getUUIDFromUsername(String username) throws IOException {
        String requestURL = "https://api.mojang.com/users/profiles/minecraft/" + username;
        String response = makeGetRequest(requestURL);
        return UUID.fromString(JsonParser.parseString(response).getAsJsonObject().get("id").getAsString());
    }

    private static String makeGetRequest(URL url) throws IOException {
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        InputStream is = (InputStream) http.getContent();
        Scanner s = new Scanner(is).useDelimiter("\\A");
        String result = s.hasNext() ? s.next() : "";
        http.disconnect();
        return result;
    }

    private static String makeGetRequest(String url) throws IOException {
        return makeGetRequest(new URL(url));
    }
}
