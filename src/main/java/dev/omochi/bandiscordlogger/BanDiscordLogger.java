package dev.omochi.bandiscordlogger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;

public final class BanDiscordLogger extends JavaPlugin {
    private static BanDiscordLogger plugin;
    // private static TextChannel textChannel;
    @Override
    public void onEnable() {
        plugin=this;

        // Plugin startup logic


        saveDefaultConfig();
        FileConfiguration config = getConfig();
        //
        this.getConfig().options().copyDefaults(true);
        saveConfig();

        String BotToken=config.getLong("token");
        JDA jda;
        try{
            JDA jda = new JDABuilder().setToken(BotToken).build();
        } catch (LoginException e) {
            throw new RuntimeException(e);
        }
        try {
            jda.awaitReady();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Long ChannelId=config.getLong("ChannelId");


        textChannel =jda.getTextChannelById(ChannelId);
        textChannel.sendMessage("a").queue();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static BanDiscordLogger getPlugin() {
        return plugin;
    }

    public static gettextChannel getTextChannel() {
        return textChannel;
    }

}
