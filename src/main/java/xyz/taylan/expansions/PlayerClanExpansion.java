package xyz.taylan.expansions;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import xyz.taylan.Clans;
import xyz.taylan.models.Clan;
import xyz.taylan.utils.ClansStorageUtil;
import xyz.taylan.utils.ColorUtils;

import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class PlayerClanExpansion extends PlaceholderExpansion {

    private final Clans plugin;

    public PlayerClanExpansion(Clans plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "remielKlan";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Taylan";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.1.1";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        Clan clanOwner = ClansStorageUtil.findClanByOfflineOwner(player);
        Clan clanMember = ClansStorageUtil.findClanByOfflinePlayer(player);
        if (params.equalsIgnoreCase("klanIsmi")){
            //%clansLite_clanName%
            if (clanOwner != null){
                return ColorUtils.translateColorCodes(clanOwner.getClanFinalName() + "&r ");
            }else if (clanMember != null){
                return ColorUtils.translateColorCodes(clanMember.getClanFinalName() + "&r ");
            }else {
                return "";
            }
        }

        if (params.equalsIgnoreCase("klanUnvan")){
            //%clansLite_clanPrefix%
            if (clanOwner != null){
                return ColorUtils.translateColorCodes(clanOwner.getClanPrefix() + "&r ");
            }else if (clanMember != null){
                return ColorUtils.translateColorCodes(clanMember.getClanPrefix() + "&r ");
            }else {
                return "";
            }
        }

        

        if (params.equalsIgnoreCase("klanEviAyarli")){
            //%clansLite_clanHomeSet%
            if (clanOwner != null){
                return String.valueOf(ClansStorageUtil.isHomeSet(clanOwner));
            }else if (clanMember != null){
                return String.valueOf(ClansStorageUtil.isHomeSet(clanMember));
            }else {
                return "";
            }
        }
        return null;
    }
}
