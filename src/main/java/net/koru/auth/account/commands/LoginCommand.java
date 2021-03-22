package net.koru.auth.account.commands;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.koru.auth.account.Account;
import net.koru.auth.utils.common.exception.InvalidHashException;
import net.koru.auth.utils.common.security.PasswordUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

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

        if(args.length < 1){
            player.sendMessage(Component.text("Used /login (password)").color(NamedTextColor.RED));
            return;
        }

        String password = args[0];

        if(account.isLogged()){
            player.sendMessage(Component.text("You already logged"));
            return;
        }

        try {
            if(PasswordUtils.verifyPassword(password, account.getPassword())){
                account.setLogged(true);
                player.sendMessage(Component.text("Logged successfully.").color(NamedTextColor.GREEN));
            }else{
                player.sendMessage(Component.text("Password is incorrect").color(NamedTextColor.RED)); //Kick after 3 attempts
            }
        } catch (InvalidHashException e) {
            e.printStackTrace();
        }
    }
}