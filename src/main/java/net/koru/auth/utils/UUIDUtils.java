package net.koru.auth.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.experimental.UtilityClass;

import java.net.URL;
import java.util.Scanner;

@UtilityClass
public class UUIDUtils {

    public boolean isPremium(String name){
        return getPremiumUUID(name) != null;
    }

    public String getPremiumUUID(String name){
        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name); //Idk if this supports many connections maybe need change
            Scanner scanner = new Scanner(url.openStream());
            String line = scanner.nextLine();
            scanner.close();
            JsonObject obj = (new Gson()).fromJson(line, JsonObject.class);
            return obj.get("id").getAsString();
        } catch (Exception ex) {
            return null;
        }
    }

}