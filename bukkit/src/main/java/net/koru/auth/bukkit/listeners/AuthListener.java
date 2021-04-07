package net.koru.auth.bukkit.listeners;

import com.google.common.collect.Lists;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import lombok.Getter;
import net.koru.auth.bukkit.Auth;
import net.koru.auth.bukkit.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.List;
import java.util.UUID;

public class AuthListener implements Listener, PluginMessageListener {

    @Getter private static List<UUID> notLoggedPlayers = Lists.newArrayList();

    public AuthListener(){
        Bukkit.getMessenger().registerIncomingPluginChannel(Auth.get(), "k-auth",  this);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event){
        Player player = event.getPlayer();

        if(!notLoggedPlayers.contains(player.getUniqueId())) return;

        Location to = event.getTo();
        Location from = event.getFrom();

        if(!Utils.hasMoved(from, to)) return;

        player.teleport(from);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();

        if(!notLoggedPlayers.contains(player.getUniqueId())) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();

        notLoggedPlayers.remove(player.getUniqueId());
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] bytes) {
        if(!channel.equalsIgnoreCase("k-auth")) return;
        ByteArrayDataInput in = ByteStreams.newDataInput(bytes);
        String subChannel = in.readUTF();
        if(subChannel.equalsIgnoreCase("no-movement")){
            String data = in.readUTF();
            notLoggedPlayers.add(UUID.fromString(data));
        }else if(subChannel.equalsIgnoreCase("movement")){
            String data = in.readUTF();
            notLoggedPlayers.remove(UUID.fromString(data));
        }
    }
}