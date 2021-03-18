package net.koru.auth.account.commands;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.koru.auth.account.Account;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

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

        if(args.length < 2){
            player.sendMessage(Component.text("Used /register (password) (password)").color(NamedTextColor.RED));
            return;
        }

        String password = args[0];
        String password2 = args[1];

        if(!password.equalsIgnoreCase(password2)){
            player.sendMessage(Component.text("The passwords are not the same").color(NamedTextColor.RED));
            return;
        }

        if(account.isRegister()){
            player.sendMessage(Component.text("You already register"));
            return;
        }

        account.setPassword(password); //Add method to encrypt

        account.setLogged(true);
        account.setRegister(true);

        player.sendMessage(Component.text("Register successfully!").color(NamedTextColor.AQUA));
    }
}