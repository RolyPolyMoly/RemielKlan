package xyz.taylan.listeners;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import xyz.taylan.Clans;
import xyz.taylan.models.Clan;
import xyz.taylan.utils.ClansStorageUtil;
import xyz.taylan.utils.ColorUtils;

import java.util.*;

public class PlayerDamage implements Listener {

	private static final FileConfiguration messagesConfig = Clans.getPlugin().messagesFileManager.getMessagesConfig();

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerHit(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Player) {
			Player hurtPlayer = (Player) event.getEntity();
			String hurtUUID = hurtPlayer.getUniqueId().toString();
			if (event.getDamager() instanceof Player) {
				Player attackingPlayer = (Player) event.getDamager();
				attackingPlayer.setInvulnerable(false);
				Clan attackingClan = ClansStorageUtil.findClanByOwner(attackingPlayer);

				if (attackingClan != null) {
					ArrayList<String> attackingClanMembers = attackingClan.getClanMembers();
					if (attackingClanMembers.contains(hurtUUID) || attackingClan.getClanOwner().equals(hurtUUID)) {

						event.setCancelled(true);
						attackingPlayer.sendMessage(
								ColorUtils.translateColorCodes(messagesConfig.getString("klandasa-vuramazsin")));

					}
				}

				else {
					Clan attackingClanByPlayer = ClansStorageUtil.findClanByPlayer(attackingPlayer);
					if (attackingClanByPlayer != null) {
						ArrayList<String> attackingMembers = attackingClanByPlayer.getClanMembers();
						if (attackingMembers.contains(hurtUUID)
								|| attackingClanByPlayer.getClanOwner().equals(hurtUUID)) {

							event.setCancelled(true);
							attackingPlayer.sendMessage(
									ColorUtils.translateColorCodes(messagesConfig.getString("klandasa-vuramazsin")));

						}
					}
				}
			}
		}
	}
}