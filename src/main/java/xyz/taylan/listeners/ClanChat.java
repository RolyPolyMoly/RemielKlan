package xyz.taylan.listeners;

import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import xyz.taylan.models.Clan;
import xyz.taylan.utils.ClansStorageUtil;
import xyz.taylan.utils.ColorUtils;

public class ClanChat implements Listener {

    @EventHandler
    public void onChatPlayer (AsyncPlayerChatEvent event) {
        String clanMergeTag = "{CLAN}";
        Player player = event.getPlayer();
        String format = event.getFormat();
        if (ClansStorageUtil.findClanByPlayer(player) == null){
            format = StringUtils.replace(format, clanMergeTag, "");
        }
        else if (ClansStorageUtil.findClanByOwner(player) != null){
            Clan clan = ClansStorageUtil.findClanByOwner(player);
            format = StringUtils.replace(format, clanMergeTag, ColorUtils.translateColorCodes(clan.getClanPrefix() + " &r"));
            event.setFormat(format);
            return;
        }else {
            Clan clan = ClansStorageUtil.findClanByPlayer(player);
            format = StringUtils.replace(format, clanMergeTag, ColorUtils.translateColorCodes(clan.getClanPrefix() + " &r"));
            event.setFormat(format);
            return;
        }
        event.setFormat(format);
    }
}
