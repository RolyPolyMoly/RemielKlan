package xyz.taylan.listeners;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import net.kyori.adventure.text.minimessage.MiniMessage;
import xyz.taylan.Clans;
import xyz.taylan.commands.ClanCommand;
import xyz.taylan.models.Clan;
import xyz.taylan.utils.ClansStorageUtil;

public class CommandListener implements Listener {
	private HashMap<Player, String> klanbilgi = new HashMap<Player, String>();

	@EventHandler
	public void commandevent(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		if (ClanCommand.warmap2.containsKey(player)) {
			event.setCancelled(true);
		}

	}
	@EventHandler
	public void playerdeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		if (ClanCommand.warmap2.containsKey(player)) {
			ClanCommand.warmap2.remove(player);
			Clan klan = ClansStorageUtil.findClanByPlayer(player);
			Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
			Team isim = scoreboard.getTeam(klan.getClanFinalName());
			player.sendMessage(klan.getClanFinalName());
			isim.removeEntry(player.getName());


			new BukkitRunnable() {

				@Override
				public void run() {
					if (isim.getSize() <= 0) {
						Clan klan2 = ClansStorageUtil.findClanByPlayer(player.getKiller());
						Clans.getPlugin().getServer().broadcast(
								MiniMessage.get().parse("&eKlan Savaşını" + klan2 + "&eisimli klan kazandı!"));
						klan2.setWarwin(klan2.getWarwin() + 1);
						ClanCommand.warmap2.clear();
					}

				}
			}.runTaskLater(Clans.getPlugin(), 100L);
		}
	}
}
