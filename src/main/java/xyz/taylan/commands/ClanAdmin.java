package xyz.taylan.commands;

import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import xyz.taylan.Clans;
import xyz.taylan.utils.ClansStorageUtil;
import xyz.taylan.utils.ColorUtils;

public class ClanAdmin implements CommandExecutor {

    Logger logger = Clans.getPlugin().getLogger();
    private static final FileConfiguration messagesConfig = Clans.getPlugin().messagesFileManager.getMessagesConfig();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("kaydet")) {
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("klan-kaydetme-başlat")));
                    try {
                        ClansStorageUtil.saveClans();
                    } catch (IOException e) {
                        e.printStackTrace();
                        sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("klan-kaydetme-hatası-1")));
                        sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("klan-kaydetme-hatası-2")));
                    }
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("kaydetme-tamamlandı")));
                }
                if (args[0].equalsIgnoreCase("reload")) {
                    Clans.getPlugin().reloadConfig();
                    Clans.getPlugin().clansFileManager.reloadClansConfig();
                    Clans.getPlugin().messagesFileManager.reloadMessagesConfig();
                    sender.sendMessage(ColorUtils.translateColorCodes(messagesConfig.getString("eklenti-yenileme-başarılı")));
                }

            }else {
                sender.sendMessage(ColorUtils.translateColorCodes(
                        "&6RemielKlan kullanımı:&3" +
                                "\n/klanadmin kaydet" +
                                "\n/klanadmin reload"
                ));
            }
        }

        if (sender instanceof ConsoleCommandSender) {
            if (args.length > 0){
                if (args[0].equalsIgnoreCase("kaydet")) {
                    logger.info(ColorUtils.translateColorCodes(messagesConfig.getString("klan-kaydetme-başlat")));
                    try {
                        ClansStorageUtil.saveClans();
                    } catch (IOException e) {
                        logger.severe(ColorUtils.translateColorCodes(messagesConfig.getString("klan-kaydetme-hatası-1")));
                        logger.severe(ColorUtils.translateColorCodes(messagesConfig.getString("klan-kaydetme-hatası-2")));
                        e.printStackTrace();
                    }
                    logger.info(ColorUtils.translateColorCodes(messagesConfig.getString("kaydetme-tamamlandı")));
                }
                if (args[0].equalsIgnoreCase("reload")) {
                    Clans.getPlugin().reloadConfig();
                    Clans.getPlugin().clansFileManager.reloadClansConfig();
                    Clans.getPlugin().messagesFileManager.reloadMessagesConfig();
                    logger.info(ColorUtils.translateColorCodes(messagesConfig.getString("eklenti-yenileme-başarılı")));
                }
            }else {
                sender.sendMessage(ColorUtils.translateColorCodes(
                		 "&6RemielKlan kullanımı:&3" +
                                 "\n/klanadmin kaydet" +
                                 "\n/klanadmin reload"
                ));
            }
        }
        return true;
    }
}
