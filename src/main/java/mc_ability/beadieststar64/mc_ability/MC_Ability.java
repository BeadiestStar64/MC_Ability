package mc_ability.beadieststar64.mc_ability;

import mc_ability.beadieststar64.mc_ability.ActiveSkill.ActivePickaxeSkillClass;
import mc_ability.beadieststar64.mc_ability.DataBase.MySQL;
import mc_ability.beadieststar64.mc_ability.DataBase.SQLite;
import mc_ability.beadieststar64.mc_ability.GUI.PlayerGUI;
import mc_ability.beadieststar64.mc_ability.MagicSkill.StaminaCutter.BlockBreakClass;
import mc_ability.beadieststar64.mc_ability.PassiveSkill.PassivePickaxeSkillClass;
import mc_ability.beadieststar64.mc_ability.Utility.CommandClass;
import mc_ability.beadieststar64.mc_ability.Original_Item.OriginalItemClass;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public final class MC_Ability extends JavaPlugin implements Listener {

    public static MC_Ability plugins;

    public static SQLite sqlite = new SQLite();
    public static MySQL mysql = new MySQL();

    public static PassivePickaxeSkillClass passivePickaxeSkillClass = new PassivePickaxeSkillClass(plugins);
    public static PlayerGUI playerGUI = new PlayerGUI(plugins);

    public static ActivePickaxeSkillClass activePickaxeSkillClass = new ActivePickaxeSkillClass(plugins);

    public static int MaxPlayers = 1;
    public static boolean Disable = false;
    public static Map<Player, Integer> GUICount = new HashMap<>();
    public static Map<Player, Boolean> IsPlayerLogOut = new HashMap<>();

    public static int UseDateBase = 0;

    //プラグインバージョン設定
    private final String version = "1.2.8";

    @Override
    public void onEnable() {

        plugins = this;

        getServer().getConsoleSender().sendMessage(ChatColor.WHITE + "[MC_Ability]" + ChatColor.AQUA + "MC_Ability has been activate!");
        getServer().getConsoleSender().sendMessage(ChatColor.WHITE + "[MC_Ability] Version is" +
                ChatColor.RED + version +
                ChatColor.WHITE + "!!");

        //Config.ymlを作成
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            this.saveDefaultConfig();
        }

        String UseDataBaseName = this.getConfig().getString("DataBase.Type");
        //DB選択
        if(UseDataBaseName == "null") {
            getServer().getConsoleSender().sendMessage(ChatColor.WHITE + "[MC_Ability]" +
                    "使用DBが未選択です！" +
                    ChatColor.GOLD + "SQLite" +
                    ChatColor.WHITE + "が使用DBとして選択されます！");
            UseDateBase = 1;
            this.getConfig().getString("DataBase.Type" , "sqlite");
            sqlite.onActivate(plugins);
        }else{
            if(UseDataBaseName.equals("SQLite")) {
                getServer().getConsoleSender().sendMessage(ChatColor.WHITE + "[MC_Ability]" +
                        ChatColor.GOLD + "SQLite" +
                        ChatColor.WHITE + "が使用DBとして選択されています！");
                UseDateBase = 1;
                sqlite.onActivate(plugins);
            }else if(UseDataBaseName.equals("MySQL")) {
                getServer().getConsoleSender().sendMessage(ChatColor.WHITE + "[MC_Ability]" +
                        ChatColor.GOLD + "MySQL" +
                        ChatColor.WHITE + "が使用DBとして選択されています！");
                UseDateBase = 2;
                GetMySQL();
                mysql.onActivate(plugins);
            }else{
                getServer().getConsoleSender().sendMessage(ChatColor.WHITE + "[MC_Ability]" +
                        "選択された" + UseDataBaseName + "は未対応DBです！" +
                        ChatColor.GOLD + "SQLite" +
                        ChatColor.WHITE + "が使用DBとして選択されます！");
                UseDateBase = 1;
                this.getConfig().getString("DataBase.Type" , "sqlite");
                sqlite.onActivate(plugins);
            }
        }
        //MC_Abilityオリジナルアイテム追加
        OriginalItemClass originalItemClass = new OriginalItemClass(plugins);
        originalItemClass.OriginalItmCreate();

        PlayerGUI playerGUI = new PlayerGUI(this);
        playerGUI.CreateGUI();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        switch (UseDateBase) {
            case 1:
                sqlite.ConnectionDataBase(player);
                break;
            case 2:
                mysql.ConnectionDataBase(player);
        }

        if(IsPlayerLogOut.get(player) == true) {
            //ここでスキル前に修復
            activePickaxeSkillClass.Restore(player);
        }

        //セットアップ
        activePickaxeSkillClass.SetUp(player);
        passivePickaxeSkillClass.SetMethod(player);
        playerGUI.SetGUI(player);
    }

    @EventHandler
    public void PlayerLeaveEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Disable = true;
        GUICount.put(player, 1);

        PlayerGUI playerGUI = new PlayerGUI(this);

        switch (UseDateBase) {
            case 1:
                sqlite.UpdataPassivePickaxeSkill(player);
                sqlite.UpdataActivePickaxeSkill(player);
                break;

            case 2:

        }
        passivePickaxeSkillClass.DeleteHashMap(player);
        getServer().getConsoleSender().sendMessage(ChatColor.DARK_PURPLE+"ばいばい！");
        MaxPlayers--;
    }

    public static void ErrorLog(Exception e) {
        Bukkit.getLogger().warning(e.toString());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Disable = true;

        OriginalItemClass originalItemClass = new OriginalItemClass(plugins);
        originalItemClass.RemoveRecipe();

        getServer().getConsoleSender().sendMessage(ChatColor.WHITE + "[MC_Ability]" + ChatColor.RED + "プラグインを終了します");

        switch (UseDateBase) {
            case 1:
                sqlite.onDisable();
                break;
            case 2:
                mysql.onDisable();
                break;
        }

        getServer().getConsoleSender().sendMessage(ChatColor.WHITE + "[MC_Ability]" + ChatColor.RED + "プラグインの停止処理が正常に終了しました");
    }

    public void RegisterCommand() {
        getCommand("MC_Ability_PassiveSkill").setExecutor(new CommandClass());
        getCommand("MC_Ability_OpenGUI").setExecutor(new CommandClass());
    }

    public void RegisterEvents() {
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new ActivePickaxeSkillClass(this), this);
        getServer().getPluginManager().registerEvents(new PassivePickaxeSkillClass(this), this);
        getServer().getPluginManager().registerEvents(new PlayerGUI(this), this);
    }

    public void GetMySQL() {
        mysql.User = getConfig().getString("DataBase.Login_User");
        mysql.PassWord = getConfig().getString("DataBase.Login_Password");
        mysql.Host = getConfig().getString("DataBase.Host_Name");
        mysql.Port = getConfig().getInt("DataBase.Port");
        mysql.DataBaseName = getConfig().getString("DataBase.DataBaseName");
    }
}