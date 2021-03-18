package net.koru.auth.account;

import com.google.common.collect.Maps;
import com.velocitypowered.api.proxy.Player;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Getter @Setter
public class Account {

    @Getter private static Map<UUID, Account> accounts = Maps.newHashMap();

    private final UUID uuid;
    private String password; //Add method to encryption
    private boolean logged;
    private boolean register;
    private String ip;

    public void save(){
        //Add mongo to save
    }

    public static Account load(Player player){

        Account account;

        if(Account.getAccounts().containsKey(player.getUniqueId())) account = Account.getAccounts().get(player.getUniqueId()); //Change this for mongo checks
        else Account.getAccounts().put(player.getUniqueId(), account = new Account(player.getUniqueId()));

        return account;
    }

}