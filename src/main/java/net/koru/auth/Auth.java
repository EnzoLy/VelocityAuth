package net.koru.auth;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import net.koru.auth.account.commands.LoginCommand;
import net.koru.auth.account.commands.RegisterCommand;
import net.koru.auth.listeners.AuthListeners;
import org.slf4j.Logger;

//This not work because i used maven xd (fuck u gradle)
@Plugin(id = "VelocityKoruAuth", name = "KoruAuth", version = "1.0-SNAPSHOT", description = "Auth for velocity", authors = {"EnzoL_"})
public class Auth {

    private final ProxyServer server;
    private final Logger logger;

    @Inject
    public Auth(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;

        server.getCommandManager().register("register", new RegisterCommand());
        server.getCommandManager().register("login", new LoginCommand());
    }

    @Subscribe
    public void onInitialize(ProxyInitializeEvent event) {
        server.getEventManager().register(this, new AuthListeners());
    }

}