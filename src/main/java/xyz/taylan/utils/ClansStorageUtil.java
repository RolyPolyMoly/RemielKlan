package xyz.taylan.utils;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import xyz.taylan.Clans;
import xyz.taylan.models.Clan;

import java.io.IOException;
import java.util.*;

public class ClansStorageUtil {

	private static Map<UUID, Clan> clansList = new HashMap<>();

	private static final FileConfiguration clansStorage = Clans.getPlugin().clansFileManager.getClansConfig();
	private static final FileConfiguration messagesConfig = Clans.getPlugin().messagesFileManager.getMessagesConfig();

	public static void saveClans() throws IOException {
		for (Map.Entry<UUID, Clan> entry : clansList.entrySet()) {
			clansStorage.set("klanlar.veri." + entry.getKey() + ".klanLideri", entry.getValue().getClanOwner());
			clansStorage.set("klanlar.veri." + entry.getKey() + ".klanIsmi", entry.getValue().getClanFinalName());
			clansStorage.set("klanlar.veri." + entry.getKey() + ".klanUnvanı", entry.getValue().getClanPrefix());
			clansStorage.set("klanlar.veri." + entry.getKey() + ".klanUyeleri", entry.getValue().getClanMembers());
			clansStorage.set("klanlar.veri." + entry.getKey() + ".klanMuttefikleri", entry.getValue().getClanAllies());
			clansStorage.set("klanlar.veri." + entry.getKey() + ".klanSavasKazanma", entry.getValue().getWarwin());
			if (entry.getValue().getClanHomeWorld() != null) {
				clansStorage.set("klanlar.veri." + entry.getKey() + ".klanEv.dunyaAdi",
						entry.getValue().getClanHomeWorld());
				clansStorage.set("klanlar.veri." + entry.getKey() + ".klanEv.x", entry.getValue().getClanHomeX());
				clansStorage.set("klanlar.veri." + entry.getKey() + ".klanEv.y", entry.getValue().getClanHomeY());
				clansStorage.set("klanlar.veri." + entry.getKey() + ".klanEv.z", entry.getValue().getClanHomeZ());
				clansStorage.set("klanlar.veri." + entry.getKey() + ".klanEv.yaw", entry.getValue().getClanHomeYaw());
				clansStorage.set("klanlar.veri." + entry.getKey() + ".klanEv.pitch",
						entry.getValue().getClanHomePitch());
			}
		}
		Clans.getPlugin().clansFileManager.saveClansConfig();
	}

	public static void restoreClans() throws IOException {
		clansStorage.getConfigurationSection("klanlar.veri").getKeys(false).forEach(key -> {
			UUID uuid = UUID.fromString(key);
			String clanFinalName = clansStorage.getString("klanlar.veri." + key + ".klanIsmi");
			String clanPrefix = clansStorage.getString("klanlar.veri." + key + ".klanUnvanı");
			List<String> clanMembersConfigSection = clansStorage.getStringList("klanlar.veri." + key + ".klanUyeleri");
			List<String> clanAlliesConfigSection = clansStorage
					.getStringList("klanlar.veri." + key + ".klanMuttefikleri");
			int warwin = clansStorage.getInt("klanlar.veri." + key + ".klanSavasKazanma");
			ArrayList<String> clanMembers = new ArrayList<>(clanMembersConfigSection);
			ArrayList<String> clanAllies = new ArrayList<>(clanAlliesConfigSection);
			String clanHomeWorld = clansStorage.getString("klanlar.veri." + key + ".klanEv.dunyaAdi");
			double clanHomeX = clansStorage.getDouble("klanlar.veri." + key + ".KlanEv.x");
			double clanHomeY = clansStorage.getDouble("klanlar.veri." + key + ".KlanEv.y");
			double clanHomeZ = clansStorage.getDouble("klanlar.veri." + key + ".KlanEv.z");
			float clanHomeYaw = (float) clansStorage.getDouble("klanlar.veri." + key + ".KlanEv.yaw");
			float clanHomePitch = (float) clansStorage.getDouble("klanlar.veri." + key + ".KlanEv.pitch");
			Clan clan = new Clan(key, clanFinalName);
			clan.setClanPrefix(clanPrefix);
			clan.setWarwin(warwin);
			clan.setClanMembers(clanMembers);
			clan.setClanAllies(clanAllies);
			clan.setClanHomeWorld(clanHomeWorld);
			clan.setClanHomeX(clanHomeX);
			clan.setClanHomeY(clanHomeY);
			clan.setClanHomeZ(clanHomeZ);
			clan.setClanHomeYaw(clanHomeYaw);
			clan.setClanHomePitch(clanHomePitch);
			clansList.put(uuid, clan);
		});
	}

	public static void createClan(Player player, String clanName) {
		UUID ownerUUID = player.getUniqueId();
		String ownerUuidString = player.getUniqueId().toString();
		clansList.put(ownerUUID, new Clan(ownerUuidString, clanName));
	}

	public static boolean isClanExisting(Player player) {
		UUID uuid = player.getUniqueId();
		if (clansList.containsKey(uuid)) {
			return true;
		}
		return false;
	}

	public static boolean deleteClan(Player player) throws IOException {
		UUID uuid = player.getUniqueId();
		String key = uuid.toString();
		if (findClanByOwner(player) != null) {
			if (isClanOwner(player)) {
				clansList.remove(uuid);
				clansStorage.set("klanlar.veri." + key, null);
				Clans.getPlugin().clansFileManager.saveClansConfig();
			}
			return true;
		}
		return false;
	}

	public static boolean isClanOwner(Player player) {
		UUID uuid = player.getUniqueId();
		String ownerUUID = uuid.toString();
		Clan clan = clansList.get(uuid);
		if (clan != null) {
			if (clan.getClanOwner() == null) {
				return false;
			} else {
				if (clan.getClanOwner().equals(ownerUUID)) {
					return true;
				}
			}
		}
		return false;
	}

	public static Clan findClanByOwner(Player player) {
		UUID uuid = player.getUniqueId();
		Clan clan = clansList.get(uuid);
		return clan;
	}

	public static Clan findClanByOfflineOwner(OfflinePlayer offlinePlayer) {
		UUID uuid = offlinePlayer.getUniqueId();
		Clan clan = clansList.get(uuid);
		return clan;
	}

	public static Clan findClanByPlayer(Player player) {
		for (Clan clan : clansList.values()) {
			if (findClanByOwner(player) != null) {
				return clan;
			}
			if (clan.getClanMembers() != null) {
				for (String member : clan.getClanMembers()) {
					if (member.equals(player.getUniqueId().toString())) {
						return clan;
					}
				}
			}
		}
		return null;
	}

	public static Clan findClanByOfflinePlayer(OfflinePlayer player) {
		for (Clan clan : clansList.values()) {
			if (findClanByOfflineOwner(player) != null) {
				return clan;
			}
			if (clan.getClanMembers() != null) {
				for (String member : clan.getClanMembers()) {
					if (member.equals(player.getUniqueId().toString())) {
						return clan;
					}
				}
			}
		}
		return null;
	}

	public static void updatePrefix(Player player, String prefix) {
		UUID uuid = player.getUniqueId();
		if (!isClanOwner(player)) {
			player.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("klan-lideri-olmanız-gerek")));
			return;
		}
		Clan clan = clansList.get(uuid);
		clan.setClanPrefix(prefix);
	}

	public static boolean addClanMember(Clan clan, Player player) {
		UUID uuid = player.getUniqueId();
		String memberUUID = uuid.toString();
		clan.addClanMember(memberUUID);
		return true;
	}

	public static Set<Map.Entry<UUID, Clan>> getClans() {
		return clansList.entrySet();
	}

	public static Set<UUID> getRawClansList() {
		return clansList.keySet();
	}

	public static void addClanAlly(Player clanOwner, Player allyClanOwner) {
		UUID ownerUUID = clanOwner.getUniqueId();
		UUID uuid = allyClanOwner.getUniqueId();
		String allyUUID = uuid.toString();
		Clan clan = clansList.get(ownerUUID);
		clan.addClanAlly(allyUUID);
	}

	public static void removeClanAlly(Player clanOwner, Player allyClanOwner) {
		UUID ownerUUID = clanOwner.getUniqueId();
		UUID uuid = allyClanOwner.getUniqueId();
		String allyUUID = uuid.toString();
		Clan clan = clansList.get(ownerUUID);
		clan.removeClanAlly(allyUUID);
	}

	public static boolean isHomeSet(Clan clan) {
		if (clan.getClanHomeWorld() != null) {
			return true;
		}
		return false;
	}
}
