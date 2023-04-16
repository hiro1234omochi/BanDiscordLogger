package dev.omochi.bandiscordlogger;

import net.dv8tion.jda.api.entities.Guild;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CommandCatch implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("autoban") || command.getName().equalsIgnoreCase("aban")) { //親コマンドの判定
            if (args.length != 2) {
                sender.sendMessage(ChatColor.RED+"引数が足りません。");
                sender.sendMessage(ChatColor.RED+"/autoban [player名] [reason（理由）]");
                sender.sendMessage(ChatColor.RED+"の書式で書いてください。");
                return true;
            } //サブコマンドの個数

            String playername = args[0]; //1個目のサブコマンド
            String reason = args[1]; //2個目のサブコマンド

            OfflinePlayer PlayerNameOfflineplayer=Bukkit.getOfflinePlayer(playername);
            if(!PlayerNameOfflineplayer.hasPlayedBefore() && !PlayerNameOfflineplayer.isOnline()){
                sender.sendMessage(ChatColor.RED+"対象はこのサーバーに入ったことがないため実行できませんでした");
                return true;
            }
            if(!(sender instanceof Player)){
                sender.sendMessage(ChatColor.RED+"コンソールからは実行できません");
                return true;
            }


            final String uuid = PlayerNameOfflineplayer.getUniqueId().toString();


            List<String> playerBanIpList=EventCatch.UuidToIps(uuid);

            playerBanIpList.removeAll(BanDiscordLogger.IgnoreIp);
            //Ignoreは削除

            List<String> playerBanPlayerList=EventCatch.IpsToUuids(playerBanIpList);


            int ReasonNumber = BanDiscordLogger.BanContain.size()+1;
            for(String BanUuid:playerBanPlayerList){
                DatabaseContain tempDB=new DatabaseContain();
                tempDB.setNumber(BanDiscordLogger.BanContain.size()+1);


                final String parent="banMembers.No"+tempDB.getNumber();
                final OfflinePlayer BanPlayer = Bukkit.getOfflinePlayer(UUID.fromString(BanUuid));
                if(!BanPlayer.hasPlayedBefore() && !BanPlayer.isOnline()){
                    sender.sendMessage(ChatColor.RED+"エラーが発生しました。サーバーをプレイしたことのないプレイヤーがdatabaseに登録されています。(");
                    return true;
                }
                final String BanMcid =BanPlayer.getName();

                boolean isDuplication=false;
                for(DatabaseContain d:BanDiscordLogger.BanContain){
                    if (d.getBanMcid().equals(BanMcid)) {
                        isDuplication = true;
                        break;
                    }
                }
                if(isDuplication){
                    sender.sendMessage(ChatColor.RED+BanMcid+"はすでにbanされているので実行されませんでした。");
                    continue;
                }
                tempDB.setBanMcid(BanMcid);
                BanDiscordLogger.database.set(parent+".ban.mcid",BanMcid);

                tempDB.setBanUuid(BanUuid);
                BanDiscordLogger.database.set(parent+".ban.uuid",BanUuid);


                String ReasonEach;
                if(tempDB.getNumber()==ReasonNumber){
                    ReasonEach=reason;
                }else{
                    ReasonEach="No"+ReasonNumber+"と同じ";
                }
                tempDB.setBanreason(ReasonEach);
                BanDiscordLogger.database.set(parent+".ban.reason",ReasonEach);

                List<String> BanUuidIpList=EventCatch.UuidToIps(BanUuid);

                final String BanIp=BanUuidIpList.get(BanUuidIpList.size()-1);
                tempDB.setBanIp(BanIp);
                BanDiscordLogger.database.set(parent+".ban.IpAddress",BanIp);

                final boolean isBanJava=!BanDiscordLogger.floodgateApi.isFloodgateId(UUID.fromString(BanUuid));
                tempDB.setBanJava(isBanJava);
                BanDiscordLogger.database.set(parent+".ban.isJava",isBanJava);


                final String ByMcid=sender.getName();
                tempDB.setByMcid(ByMcid);
                BanDiscordLogger.database.set(parent+".by.mcid",ByMcid);

                final String ByUuid=((Player)sender).getUniqueId().toString();
                tempDB.setByUuid(ByUuid);
                BanDiscordLogger.database.set(parent+".by.uuid",ByUuid);

                final boolean isIsbyJava=!BanDiscordLogger.floodgateApi.isFloodgateId(UUID.fromString(ByUuid));
                tempDB.setIsbyJava(isIsbyJava);
                BanDiscordLogger.database.set(parent+".by.isJava",isIsbyJava);

                final Long TimeUNIX= System.currentTimeMillis() / 1000;
                tempDB.setTimeUNIX(TimeUNIX);
                BanDiscordLogger.database.set(parent+".TimeUNIX",TimeUNIX);

                BanDiscordLogger.BanContain.add(tempDB);
                SendBanLog.Send(tempDB);

                sender.sendMessage(ChatColor.GREEN+BanMcid+"は正常にbanされました。");
                sender.sendMessage(ChatColor.GREEN+BanIp+"は正常にbanされました。");

                BanPlayer.banPlayer(reason);
                Bukkit.banIP(BanIp);

            }
            BanDiscordLogger.DatabaseSave();
            BanDiscordLogger.ProcessNumber=BanDiscordLogger.BanContain.size();
            BanDiscordLogger.config.set("ProcessNumber",BanDiscordLogger.ProcessNumber);
            BanDiscordLogger.plugin.saveConfig();
        }
        return true;
    }


}
