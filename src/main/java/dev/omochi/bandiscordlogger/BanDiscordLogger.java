package dev.omochi.bandiscordlogger;

import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.geysermc.floodgate.api.FloodgateApi;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class BanDiscordLogger extends JavaPlugin {
    public static BanDiscordLogger plugin;
    public static String BotToken;
    public static long ChannelId;
    public static TextChannel textChannel;
    public static FileConfiguration config;
    public static FileConfiguration database;
    public static File DatabaseFile;
    public static int ProcessNumber;
    public static JDA jda;
    public static List<DatabaseContain> BanContain;
    public static FloodgateApi floodgateApi;
    public static List<List<String>> UuidAndIp;
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new EventCatch(), this);
        plugin=this;

        floodgateApi= FloodgateApi.getInstance();


        getCommand("autoban").setExecutor(new CommandCatch());
        // Plugin startup
        config = getConfig();
        //
        this.getConfig().options().copyDefaults(true);
        saveConfig();

        BotToken=config.getString("token");
        try{
            jda = JDABuilder.createDefault(BotToken).build();
        } catch (LoginException e) {
            throw new RuntimeException(e);
        }
        try {
            jda.awaitReady();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ProcessNumber= config.getInt("ProcessNumber");

        ChannelId=config.getLong("ChannelId");

        textChannel =jda.getTextChannelById(ChannelId);

        DatabaseYamlFile();
        UuidAndIp=new ArrayList<List<String>>();

        if(database.contains("UuidAndIp")){
            UuidAndIp=(List<List<String>>)database.getList("UuidAndIp");
        }else{
            database.set("UuidAndIp",UuidAndIp);
            DatabaseSave();
        }

        BanContain=new ArrayList<DatabaseContain>();
        if(database.getConfigurationSection("banMembers")!=null) {
            for (String key : database.getConfigurationSection("banMembers").getKeys(false)) {
                DatabaseContain tempDB = new DatabaseContain();
                String parent = "banMembers." + key;

                tempDB.setNumber(Integer.valueOf(key.substring(2)));
                //Noを除外した数字を取得

                tempDB.setBanMcid(database.getString(parent + ".ban.mcid"));
                tempDB.setBanUuid(database.getString(parent + ".ban.uuid"));
                tempDB.setBanreason(database.getString(parent + ".ban.reason"));
                tempDB.setBanIp(database.getString(parent + ".ban.IpAddress"));
                tempDB.setBanJava(database.getBoolean(parent + ".ban.isJava"));

                tempDB.setByMcid(database.getString(parent + ".by.mcid"));
                tempDB.setByUuid(database.getString(parent + ".by.uuid"));
                tempDB.setIsbyJava(database.getBoolean(parent + ".by.isJava"));

                tempDB.setTimeUNIX(database.getLong(parent + ".TimeUNIX"));

                BanContain.add(tempDB);
            }
        }
        for(DatabaseContain c:BanContain){
            if(ProcessNumber<c.getNumber()){
                SendBanLog.Send(c);
            }
        }
        ProcessNumber=BanContain.size();
        config.set("ProcessNumber",ProcessNumber);
        saveConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        jda.shutdownNow();
    }
    public void DatabaseYamlFile(){
        DatabaseFile = new File(getDataFolder() + File.separator +"database.yml");
        if(!DatabaseFile.exists()) {
            getLogger().info("Creating new database...");
            try {
                DatabaseFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        database = YamlConfiguration.loadConfiguration(DatabaseFile);

    }
    public static void DatabaseSave(){
        try {
            database.save(DatabaseFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
