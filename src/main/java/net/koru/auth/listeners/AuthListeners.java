package net.koru.auth.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.event.connection.PreLoginEvent;
import com.velocitypowered.api.proxy.Player;
import net.koru.auth.utils.UUIDUtils;
import net.kyori.adventure.text.Component;

public class AuthListeners {

    @Subscribe
    public void onPreLogin(PreLoginEvent event){
        String name = event.getUsername();
        if(UUIDUtils.isPremium(name)){
            event.setResult(PreLoginEvent.PreLoginComponentResult.forceOnlineMode());
        }
    }

    @Subscribe
    public void onLogin(LoginEvent event){
        Player player = event.getPlayer();
        if(!player.isOnlineMode()){
            player.sendMessage(Component.text("Necesitas logearte"));
        }
    }

}