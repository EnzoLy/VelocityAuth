package net.koru.auth.bukkit;

import net.koru.auth.bukkit.listeners.AuthListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Auth extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new AuthListener(), this);
    }

    public static Auth get(){
        return getPlugin(Auth.class);
    }

}