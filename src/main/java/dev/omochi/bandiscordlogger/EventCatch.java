package dev.omochi.bandiscordlogger;

import net.dv8tion.jda.api.entities.Guild;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EventCatch implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerJoinEvent e) {
        List<String> i_list=new ArrayList<String>();
        for(byte b:e.getPlayer().getAddress().getAddress().getAddress()){
            i_list.add(Integer.valueOf(Byte.toUnsignedInt(b)).toString());
        }
        String ip=String.join(".",i_list);

        String uuid=e.getPlayer().getUniqueId().toString();

        boolean isMatch=false;
        boolean isMatch2=false;


        List<String> keep_l = null;
        for(List<String> l: BanDiscordLogger.UuidAndIp){
            if(l.get(0).equals(uuid)){
                isMatch=true;
                for(String s:l.subList(1,l.size())){//最初は削除

                    if(!s.equals(ip)){
                        keep_l=l;
                        //l.add(ip);
                        //java.util.ConcurrentModificationExceptionが発生してしまうため修正済み
                    }else{
                        isMatch2=true;
                    }

                }
            }
        }
        if(isMatch) {
            if(!isMatch2){
                keep_l.add(ip);
            }
        }else{
            List<String> tempUuidAndIp = new ArrayList<String>();
            tempUuidAndIp.add(uuid);
            tempUuidAndIp.add(ip);
            BanDiscordLogger.UuidAndIp.add(tempUuidAndIp);
        }
        BanDiscordLogger.database.set("UuidAndIp",BanDiscordLogger.UuidAndIp);


        BanDiscordLogger.DatabaseSave();
        BanDiscordLogger.ProcessNumber=BanDiscordLogger.BanContain.size();
        BanDiscordLogger.plugin.saveConfig();

    }
    public static List<String> IpToUuids(String ip){
        List<String> uuids=new ArrayList<String>();

        for(List<String> s:BanDiscordLogger.UuidAndIp){
            for(String s2:s){
                if(s2.equals(ip)){
                    uuids.add(s.get(0));
                }
            }
        }
        uuids=new ArrayList<String>(new LinkedHashSet<String>(uuids));//重複削除
        return uuids;
    }
    public static List<String> IpsToUuids(List<String> ips){
        List<String> uuids=new ArrayList<String>();

        for(String ip:ips){
            List<String> uuids2=IpToUuids(ip);
            uuids= Stream.concat(uuids.stream(), uuids2.stream())
                    .collect(Collectors.toList());
        }
        uuids=new ArrayList<String>(new LinkedHashSet<String>(uuids));//重複削除
        return uuids;
    }
    public static List<String> UuidToIps(String uuid){
        List<String> ips=new ArrayList<String>();

        for(List<String> s:BanDiscordLogger.UuidAndIp){
            if(s.get(0).equals(uuid)) {
                for (String s2 : s.subList(1,s.size())) {//最初は除外
                    ips.add(s2);
                }
            }
        }
        ips=new ArrayList<String>(new LinkedHashSet<String>(ips));//重複削除
        return ips;
    }
    public static List<String> UuidsToIps(List<String> uuids){
        List<String> ips=new ArrayList<String>();
        for(String uuid:uuids){
            List<String> ips2=UuidToIps(uuid);
            ips= Stream.concat(ips.stream(), ips.stream())
                    .collect(Collectors.toList());
        }
        ips=new ArrayList<String>(new LinkedHashSet<String>(ips));//重複削除
        return ips;
    }
}
