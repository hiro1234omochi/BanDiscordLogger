package dev.omochi.bandiscordlogger;

import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class SendBanLog {
    public static void Send(DatabaseContain databaseContain){
        EmbedBuilder eb = new EmbedBuilder();

        if(databaseContain.isIsbyJava()) {
            eb.setAuthor(databaseContain.getByMcid() + "により実行 (" + databaseContain.getByUuid() + ")", null, "https://minotar.net/avatar/" + databaseContain.getByUuid());
        }else{
            eb.setAuthor(databaseContain.getByMcid() + "により実行 (-)", null, "https://minotar.net/avatar/" + databaseContain.getByUuid());
        }

        eb.setTitle("Ban通知 No"+databaseContain.getNumber());



        eb.setColor(new Color(255, 0, 54));

        eb.addField("Ban者のMCID", databaseContain.getBanMcid(), true);
        eb.setThumbnail("https://minotar.net/avatar/" + databaseContain.getBanUuid());
        if(databaseContain.isBanJava()) {
            eb.addField("Ban者のUUID", databaseContain.getBanUuid(), true);
        }else {
            eb.addField("Ban者のUUID","-", true);
        }



        eb.addField("reason（理由）", databaseContain.getBanreason(), false);
        eb.addField("ip address", databaseContain.getBanIp(), false);

        eb.setImage("https://www.lemon-server.net/wp-content/uploads/2022/12/cropped-logo.png");

        eb.setFooter(convertUnitTimeToJST(Long.toString(databaseContain.getTimeUNIX())));

        BanDiscordLogger.textChannel.sendMessage(eb.build()).queue();
    }
    private static String convertUnitTimeToJST(String unitTime) {

        // 日付の表示形式
        final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH時mm分ss秒");

        // UNIXタイムスタンプを日本時間に変換
        return Instant.ofEpochSecond(Long.valueOf(unitTime))
                .atZone(ZoneId.of("Etc/GMT-9"))
                .format(dateFormat);
    }

}
