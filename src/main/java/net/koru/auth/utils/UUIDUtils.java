package net.koru.auth.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.experimental.UtilityClass;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@UtilityClass
public class UUIDUtils {

    private String URL_MOJANG = "https://api.mojang.com/users/profiles/minecraft/";
    private String URL_CLOUD_PROTECTED = "https://mcapi.cloudprotected.net/uuid/";
    private String URL_MINETOOLS = "https://api.minetools.eu/uuid/";

    public boolean isPremium(String name){
        return getPremiumUUID(name) != null;
    }

    public String getPremiumUUID(String name){
        return getPremiumUUID(name, URL_MOJANG);
    }

    public String getPremiumUUID(String name, String urlString){
        try {
            URL url = new URL(urlString + name); //Idk if this supports many connections maybe need change

            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            httpURLConnection.connect();
            int responseCode = httpURLConnection.getResponseCode();
            if(responseCode != 200){
                if(urlString.equalsIgnoreCase(URL_MOJANG)) return getPremiumUUID(name, URL_MINETOOLS);
                else if(urlString.equalsIgnoreCase(URL_MINETOOLS)) return getPremiumUUID(name, URL_CLOUD_PROTECTED);
                else if(urlString.equalsIgnoreCase(URL_CLOUD_PROTECTED)) return getPremiumUUID(name, URL_MOJANG);
            }
            JsonObject obj = (new Gson()).fromJson(new InputStreamReader(httpURLConnection.getInputStream()), JsonObject.class);
            System.out.println(obj);
            return obj.get("id").getAsString();
        } catch (Exception ex) {
            return null;
        }
    }

}