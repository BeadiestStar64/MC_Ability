package mc_ability.beadieststar64.mc_ability.Utility;

import mc_ability.beadieststar64.mc_ability.MC_Ability;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class SetUpFiles {

    public static MC_Ability plugin;
    public static File file;
    public static FileConfiguration playerFile;

    public static void SetUp() {
        file = new File(Bukkit.getServer().getPluginManager().getPlugin("MC_Ability").getDataFolder(), "player.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            }catch (Exception e) {
                plugin.ErrorLog(e);
            }
        }

        playerFile = YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration get() {
        return playerFile;
    }

    public static void SaveFile() {

        try {
            playerFile.save(file);
        }catch (Exception e) {
            plugin.ErrorLog(e);
        }
    }

    public static void LoadFile() {
        playerFile = YamlConfiguration.loadConfiguration(file);
    }
}