package net.koru.auth.account;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.velocitypowered.api.proxy.Player;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.koru.auth.Auth;
import org.bson.Document;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Getter @Setter
public class Account {

    @Getter private static Map<UUID, Account> accounts = Maps.newHashMap();
    private static MongoCollection<Document> collection = Auth.get().getMongoDatabase().getCollection("profiles");

    private final UUID uuid;
    private String password;
    private boolean logged;
    private boolean register;
    private List<String> ips = Lists.newArrayList();
    private int attempts;
    private String salt;
    private String capchat;

    public void save(){
        Document document = new Document();

        document.put("uuid", uuid.toString());
        document.put("ip", ips);
        document.put("register", register);
        document.put("password", password);
        document.put("salt", salt);

        collection.replaceOne(Filters.eq("uuid", uuid.toString()), document, new ReplaceOptions().upsert(true));
    }

    public static Account load(Player player){
        return load(player.getUniqueId());
    }

    public static Account load(UUID uuid){
        Document document = collection.find(Filters.eq("uuid", uuid.toString())).first();

        if(document == null) {
            return new Account(uuid);
        }

        Account account = new Account(uuid);

        account.setIps((List<String>) document.get("ip"));
        account.setPassword(document.getString("password"));
        account.setRegister(document.getBoolean("register"));
        account.setSalt(document.getString("salt"));
        return account;
    }

}