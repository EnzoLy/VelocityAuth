package net.koru.auth.utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import lombok.experimental.UtilityClass;
import net.koru.auth.Auth;

@UtilityClass
public class PlayerUtils {

    public void sendToBukkit(Player player, boolean movement){
        ByteArrayDataOutput buf = ByteStreams.newDataOutput();
        if(movement){
            buf.writeUTF("movement");
        }else{
            buf.writeUTF("no-movement");
        }
        buf.writeUTF(player.getUniqueId().toString());
        ServerConnection serverConnection = player.getCurrentServer().get();
        serverConnection.sendPluginMessage(Auth.get().getChannelIdentifier(), buf.toByteArray());
    }

}