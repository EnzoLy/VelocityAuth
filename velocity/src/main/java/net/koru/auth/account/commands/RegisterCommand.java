package net.koru.auth.account.commands;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.koru.auth.Auth;
import net.koru.auth.account.Account;
import net.koru.auth.utils.PasswordUtils;
import net.koru.auth.utils.PlayerUtils;
import net.koru.auth.utils.SaltGenerator;
import net.koru.auth.utils.TaskUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class RegisterCommand implements SimpleCommand {

    @Override
    public void execute(Invocation invocation) {
        Player player = (Player) invocation.source();
        String[] args = invocation.arguments();
        Account account = Account.getAccounts().get(player.getUniqueId());

        if(account == null) {
            player.sendMessage(Component.text("Only no premium can use this command").color(NamedTextColor.RED));
            return;
        }

        if(args.length < 3){
            player.sendMessage(Component.text("Used /register (password) (password) " + account.getCapchat()).color(NamedTextColor.RED));
            return;
        }

        String password = args[0];
        String password2 = args[1];
        String capchat = args[2];

        if(!capchat.equalsIgnoreCase(account.getCapchat())){
            player.sendMessage(Component.text("The capchats are not the same").color(NamedTextColor.RED));
            return;
        }

        if(!password.equalsIgnoreCase(password2)){
            player.sendMessage(Component.text("The passwords are not the same").color(NamedTextColor.RED));
            return;
        }

        if(account.isRegister()){
            player.sendMessage(Component.text("You already register"));
            return;
        }

        String salt = SaltGenerator.generateHex(10);

        account.setPassword(PasswordUtils.hash(password, salt));

        account.setLogged(true);
        account.setRegister(true);
        account.setSalt(salt);

        player.sendMessage(Component.text("Register successfully!").color(NamedTextColor.AQUA));

        TaskUtil.runLater(() -> PlayerUtils.sendToBukkit(player, true), 20, TimeUnit.MILLISECONDS);
        TaskUtil.runAsync(account::save);

        Optional<RegisteredServer> toConnect = Auth.get().getServer().getServer("hub");
        toConnect.ifPresent(registeredServer -> player.createConnectionRequest(registeredServer).fireAndForget());
    }
}