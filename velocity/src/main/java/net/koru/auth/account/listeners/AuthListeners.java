package net.koru.auth.account.listeners;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.event.connection.PreLoginEvent;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.Player;
import net.koru.auth.account.Account;
import net.koru.auth.utils.PlayerUtils;
import net.koru.auth.utils.SaltGenerator;
import net.koru.auth.utils.TaskUtil;
import net.koru.auth.utils.UUIDUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.concurrent.TimeUnit;

public class AuthListeners {

    /*
        If player is premium force check is login into minecraft.net
        and if he is not logged in, kick the player
     */
    @Subscribe
    public void onPreLogin(PreLoginEvent event){
        if(UUIDUtils.isPremium(event.getUsername())) event.setResult(PreLoginEvent.PreLoginComponentResult.forceOnlineMode());
    }


    @Subscribe(order = PostOrder.NORMAL)
    public void onLogin(LoginEvent event){
        Player player = event.getPlayer();
        if(!player.isOnlineMode()){
            Account account = Account.load(player);

            Account.getAccounts().put(player.getUniqueId(), account);

            account.getIps().add(player.getRemoteAddress().getHostName()); //Add max register per ip

            account.setCapchat(SaltGenerator.generateCaptcha(6));

            if(!account.isRegister()){
                player.sendMessage(Component.text("Register using /register (password) (password) " + account.getCapchat()).color(NamedTextColor.GREEN));
            }else{
                player.sendMessage(Component.text("Login using /login (password) " + account.getCapchat()).color(NamedTextColor.GREEN));
            }
        }else {
            player.sendMessage(Component.text("Automatic logged.").color(NamedTextColor.GREEN));
        }
    }

    @Subscribe
    public void onJoin(ServerConnectedEvent event){
        Player player = event.getPlayer();
        if(!player.isOnlineMode()) {
            TaskUtil.runLater(() -> PlayerUtils.sendToBukkit(player, false), 20, TimeUnit.MILLISECONDS);
        }
    }

    @Subscribe
    public void onChat(PlayerChatEvent event){
        Player player = event.getPlayer();
        if(!player.isOnlineMode()) {
            Account account = Account.load(player);

            if(!account.isLogged() || !account.isRegister()){
                player.sendMessage(Component.text("Register using /register (password) (password)").color(NamedTextColor.GREEN));
                event.setResult(PlayerChatEvent.ChatResult.denied());
            }
        }
    }

    @Subscribe
    public void onLeave(DisconnectEvent event){
        Player player = event.getPlayer();
        Account account = Account.getAccounts().get(player.getUniqueId());
        if(account != null){
            account.setLogged(false);
            TaskUtil.runAsync(account::save);
            Account.getAccounts().remove(player.getUniqueId());
        }
    }

}