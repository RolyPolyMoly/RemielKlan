package xyz.taylan;

import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.taylan.commands.ClanAdmin;
import xyz.taylan.commands.ClanCommand;
import xyz.taylan.expansions.PlayerClanExpansion;
import xyz.taylan.files.ClansFileManager;
import xyz.taylan.files.MessagesFileManager;
import xyz.taylan.listeners.ClanChat;
import xyz.taylan.listeners.CommandListener;
import xyz.taylan.listeners.PlayerDamage;
import xyz.taylan.utils.ClansStorageUtil;
import xyz.taylan.utils.ColorUtils;
import xyz.taylan.utils.TaskTimerUtils;

public final class Clans extends JavaPlugin {

	private final PluginDescriptionFile pluginInfo = getDescription();
	private final String pluginVersion = pluginInfo.getVersion();
	Logger logger = this.getLogger();

	private static Clans plugin;
	public MessagesFileManager messagesFileManager;
	public ClansFileManager clansFileManager;

	@Override
	public void onEnable() {
		// Plugin startup logic
		plugin = this;

		// Server version compatibility check
		if (!(Bukkit.getServer().getVersion().contains("1.13") || Bukkit.getServer().getVersion().contains("1.14")
				|| Bukkit.getServer().getVersion().contains("1.15") || Bukkit.getServer().getVersion().contains("1.16")
				|| Bukkit.getServer().getVersion().contains("1.17")
				|| Bukkit.getServer().getVersion().contains("1.18"))) {
			logger.warning(ColorUtils.translateColorCodes("&4-------------------------------------------"));
			logger.warning(ColorUtils
					.translateColorCodes("&6RemielKlan: &4Bu eklenti sadece aşağıdaki versiyonları destekliyor."));
			logger.warning(ColorUtils.translateColorCodes("&6RemielKlan: &41.16.x"));
			logger.warning(ColorUtils.translateColorCodes("&6RemielKlan: &41.17.x"));
			logger.warning(ColorUtils.translateColorCodes("&6RemielKlan: &41.18.x"));
			logger.warning(ColorUtils.translateColorCodes("&6RemielKlan: &4Eklenti devre dışı bırakılıyor!"));
			logger.warning(ColorUtils.translateColorCodes("&4-------------------------------------------"));
			Bukkit.getPluginManager().disablePlugin(this);
		} else {
			logger.info(ColorUtils.translateColorCodes("&a-------------------------------------------"));
			logger.info(ColorUtils.translateColorCodes("&6RemielKlan: &aSürüm Uyumlu!"));
			logger.info(ColorUtils.translateColorCodes("&6RemielKlan: &6Kuruluma devam ediliyor..."));
			logger.info(ColorUtils.translateColorCodes("&a-------------------------------------------"));
		}

		// Load the plugin configs
		getConfig().options().copyDefaults();
		saveDefaultConfig();

		// Load messages.yml
		this.messagesFileManager = new MessagesFileManager();
		messagesFileManager.MessagesFileManager(this);

		// Load clans.yml
		this.clansFileManager = new ClansFileManager();
		clansFileManager.ClansFileManager(this);
		if (clansFileManager.getClansConfig().contains("clans.data")) {
			try {
				ClansStorageUtil.restoreClans();
			} catch (IOException e) {
				logger.severe(
						ColorUtils.translateColorCodes("&6RemielKlan: &4clans.yml adlı dosyadan veriler yüklenemedi!"));
				logger.severe(ColorUtils.translateColorCodes("&6RemielKlan: &4Eklenti devre dışı bırakılıyor!"));
				e.printStackTrace();
				Bukkit.getPluginManager().disablePlugin(this);
			}
		}

		// Register the plugin commands
		this.getCommand("klan").setExecutor(new ClanCommand());
		this.getCommand("klanadmin").setExecutor(new ClanAdmin());

		// Register the plugin events
		this.getServer().getPluginManager().registerEvents(new ClanChat(), this);
		this.getServer().getPluginManager().registerEvents(new CommandListener(), this);
		this.getServer().getPluginManager().registerEvents(new PlayerDamage(), this);
		// Register PlaceHolderAPI hooks
		if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
			new PlayerClanExpansion(this).register();
			logger.info(ColorUtils.translateColorCodes("-------------------------------------------"));
			logger.info(ColorUtils.translateColorCodes("&6RemielKlan: &3PlaceholderAPI bulundu!"));
			logger.info(ColorUtils.translateColorCodes("&6RemielKlan: &3Placeholderlar ayarlandı."));
			logger.info(ColorUtils.translateColorCodes("-------------------------------------------"));
		} else {
			logger.warning(ColorUtils.translateColorCodes("-------------------------------------------"));
			logger.warning(ColorUtils.translateColorCodes("&6RemielKlan: &cPlaceholderAPI bulunamadı!"));
			logger.warning(ColorUtils.translateColorCodes("&6RemielKlan: &cPlaceholderlar devre dışı."));
			logger.warning(ColorUtils.translateColorCodes("-------------------------------------------"));
		}

		// Plugin startup message
		logger.info(ColorUtils.translateColorCodes("-------------------------------------------"));
		logger.info(ColorUtils.translateColorCodes("&6RemielKlan: &3Eklentinin yapımcısı: Taylan"));
		logger.info(ColorUtils.translateColorCodes("&6RemielKlan: &3Eklenti başarıyla yüklendi!"));
		logger.info(ColorUtils.translateColorCodes("&6RemielKlan: &3Eklenti Sürümü: &d&l" + pluginVersion));
		logger.info(ColorUtils.translateColorCodes("-------------------------------------------"));

		// Start auto save task
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				TaskTimerUtils.runClansAutoSaveOne();
				logger.info(ColorUtils.translateColorCodes(
						messagesFileManager.getMessagesConfig().getString("otomatik-kayit-aktif")));
			}
		}, 100L);
	}

	@Override
	public void onDisable() {
		// Plugin shutdown logic

		// Safely stop the auto save tasks if running
		try {
			if (Bukkit.getScheduler().isCurrentlyRunning(TaskTimerUtils.taskID1)
					|| Bukkit.getScheduler().isQueued(TaskTimerUtils.taskID1)) {
				Bukkit.getScheduler().cancelTask(TaskTimerUtils.taskID1);
			}
			if (Bukkit.getScheduler().isCurrentlyRunning(TaskTimerUtils.taskID2)
					|| Bukkit.getScheduler().isQueued(TaskTimerUtils.taskID2)) {
				Bukkit.getScheduler().cancelTask(TaskTimerUtils.taskID2);
			}
		} catch (Exception e) {
			logger.info(ColorUtils.translateColorCodes("-------------------------------------------"));
			logger.info(ColorUtils.translateColorCodes("&6RemielKlan: &3Eklentinin yapımcısı: Taylan"));
			logger.info(ColorUtils.translateColorCodes("&6RemielKlan: &3Eklenti devre dışı bırakılıyor!"));
		}

		// Save clansList HashMap to clans.yml
		if (!ClansStorageUtil.getRawClansList().isEmpty()) {
			try {
				ClansStorageUtil.saveClans();
				logger.info(
						ColorUtils.translateColorCodes("&6RemielKlan: &3Tüm klanlar clans.yml dosyasına başarıyla kaydedildi."));
			} catch (IOException e) {
				logger.severe(ColorUtils.translateColorCodes("&6RemielKlan: &4clans.yml dosyasını kaydetme başarısız!"));
				e.printStackTrace();
			}
		}

		// Final plugin shutdown message


		logger.info(ColorUtils.translateColorCodes("&6RemielKlan: &3Eklenti devre dışı bırakıldı!"));
		logger.info(ColorUtils.translateColorCodes("-------------------------------------------"));
	}

	public static Clans getPlugin() {
		return plugin;
	}
}
