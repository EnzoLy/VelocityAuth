package net.koru.auth;

import com.google.inject.Inject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import net.koru.auth.account.commands.LoginCommand;
import net.koru.auth.account.commands.RegisterCommand;
import net.koru.auth.account.listeners.AuthListeners;
import org.slf4j.Logger;

//This not work because i used maven xd (fuck u gradle)
@Plugin(id = "VelocityKoruAuth", name = "KoruAuth", version = "1.0-SNAPSHOT", description = "Auth for velocity", authors = {"EnzoL_"})
@Getter
public class Auth {

    private final ProxyServer server;
    private final Logger logger;
    private MongoDatabase mongoDatabase;
    private static Auth auth;
    @Inject
    public Auth(ProxyServer server, Logger logger) {
        auth = this;
        this.server = server;
        this.logger = logger;

        loadMongo();
        server.getCommandManager().register("register", new RegisterCommand());
        server.getCommandManager().register("login", new LoginCommand());
    }


    public static Auth get(){
        return auth;
    }

    private void loadMongo(){

        mongoDatabase = new MongoClient(new MongoClientURI("mongodb://root:WAuLfNFbuuhmDume@149.56.107.180:27017/?authSource=admin")).getDatabase("k-auth");

        /*if (mainConfig.getBoolean("MONGO.AUTHENTICATION.ENABLED")) {
            ServerAddress serverAddress = new ServerAddress(mainConfig.getString("MONGO.HOST"),
                mainConfig.getInteger("MONGO.PORT"));

            MongoCredential credential = MongoCredential.createCredential(
                mainConfig.getString("MONGO.AUTHENTICATION.USERNAME"), "admin",
                mainConfig.getString("MONGO.AUTHENTICATION.PASSWORD").toCharArray());

            MongoClient mongoClient = new MongoClient(serverAddress, Collections.singletonList(credential));
            mongoDatabase = mongoClient.getDatabase("testdev");
        } else {
            mongoDatabase = new MongoClient(mainConfig.getString("MONGO.HOST"),
                mainConfig.getInteger("MONGO.PORT")).getDatabase("testdev");
        }*/
    }

    @Subscribe
    public void onInitialize(ProxyInitializeEvent event) {
        server.getEventManager().register(this, new AuthListeners());
    }

}