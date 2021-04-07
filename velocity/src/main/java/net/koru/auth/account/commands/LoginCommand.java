package net.koru.auth.account.commands;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.koru.auth.Auth;
import net.koru.auth.account.Account;
import net.koru.auth.utils.PasswordUtils;
import net.koru.auth.utils.PlayerUtils;
import net.koru.auth.utils.TaskUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class LoginCommand implements SimpleCommand {

    @Override
    public void execute(SimpleCommand.Invocation invocation) {
        Player player = (Player) invocation.source();
        String[] args = invocation.arguments();
        Account account = Account.getAccounts().get(player.getUniqueId());

        if(account == null) {
            player.sendMessage(Component.text("Only no premium can use this command").color(NamedTextColor.RED));
            return;
        }

        if(args.length < 2){
            player.sendMessage(Component.text("Used /login (password) " + account.getCapchat()).color(NamedTextColor.RED));
            return;
        }

        if(account.getAttempts() >= 3){
            player.disconnect(Component.text("Too many tries").color(NamedTextColor.RED));
            return;
        }

        String password = args[0];
        String capchat = args[1];

        if(!capchat.equalsIgnoreCase(account.getCapchat())){
            player.sendMessage(Component.text("The capchat are not the same").color(NamedTextColor.RED));
            return;
        }

        if(account.isLogged()){
            player.sendMessage(Component.text("You already logged"));
            return;
        }

        if(PasswordUtils.verify(account, password, account.getSalt())){
            account.setLogged(true);
            player.sendMessage(Component.text("Logged successfully.").color(NamedTextColor.GREEN));
            TaskUtil.runLater(() -> PlayerUtils.sendToBukkit(player, true), 20, TimeUnit.MILLISECONDS);
            Optional<RegisteredServer> toConnect = Auth.get().getServer().getServer("hub");
            toConnect.ifPresent(registeredServer -> player.createConnectionRequest(registeredServer).fireAndForget());
        }else{
            account.setAttempts(account.getAttempts() + 1);
            player.sendMessage(Component.text("Password is incorrect").color(NamedTextColor.RED));
        }
    }
}