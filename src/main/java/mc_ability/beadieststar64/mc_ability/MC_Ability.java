package mc_ability.beadieststar64.mc_ability;

import mc_ability.beadieststar64.mc_ability.ActiveSkill.ActivePickaxeSkillClass;
import mc_ability.beadieststar64.mc_ability.DataBase.MySQL;
import mc_ability.beadieststar64.mc_ability.DataBase.SQLite;
import mc_ability.beadieststar64.mc_ability.GUI.DisplayGUI;
import mc_ability.beadieststar64.mc_ability.GUI.PlayerGUI;
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
    public static boolean IsOnlinePlayer = false;
    public static boolean Disable = false;
    public static Map<Player, Integer> GUICount = new HashMap<>();
    public static Map<Player, Boolean> IsPlayerLogOut = new HashMap<>();

    public static int UseDateBase = 0;

    //プラグインバージョン設定
    private final String version = "1.2.2";

    @Override
    public void onEnable() {

        plugins = this;

        if(this.getConfig().contains("data")) {
            PlayerGUI playerGUI = new PlayerGUI(plugins);
            playerGUI.restoreInvs();
        }

        getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "MC_Ability has been activate!");

        getServer().getConsoleSender().sendMessage(ChatColor.WHITE + "Version is" +
                ChatColor.RED + version +
                ChatColor.WHITE + ".");

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

        if(!PlayerGUI.menus.isEmpty()) {
            PlayerGUI playerGUI = new PlayerGUI(plugins);
            playerGUI.saveInvs();
        }

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
        getCommand("pv").setExecutor(new CommandClass());
    }

    public void RegisterEvents() {
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new ActivePickaxeSkillClass(this), this);
        getServer().getPluginManager().registerEvents(new PassivePickaxeSkillClass(this), this);
        getServer().getPluginManager().registerEvents(new PlayerGUI(this), this);
        getServer().getPluginManager().registerEvents(new DisplayGUI(this), this);
    }

    /*
    public void PutGUI(Player player) {
        GUICount.put(player, (GUICount.get(player) + 1));
        String SQL = ("");
        String InsertData = ("");

        switch (GUICount.get(player)) {
            case 1:
                SQL = ("UPDATE GUI SET FIRST_INVENTORY_ITEM_MATERIAL=? WHERE UUID=?");
                PlayerGUI.inv.get(player);
                break;
            case 2:
                SQL = ("UPDATE GUI SET FIRST_INVENTORY_ITEM_NAME WHERE=? UUID=?");
                break;
            case 3:
                SQL = ("UPDATE GUI SET FIRST_INVENTORY_ITEM_LORE1 WHERE=? UUID=?");
                break;
            case 4:
                SQL = ("UPDATE GUI SET FIRST_INVENTORY_ITEM_LORE2 WHERE=? UUID=?");
                break;
            case 5:
                SQL = ("UPDATE GUI SET FIRST_INVENTORY_ITEM_LORE3 WHERE=? UUID=?");
                break;
            case 6:
                SQL = ("UPDATE GUI SET FIRST_INVENTORY_ITEM_LORE4 WHERE=? UUID=?");
                break;
            case 7:
                SQL = ("UPDATE GUI SET FIRST_INVENTORY_ITEM_LORE5 WHERE=? UUID=?");
                break;
            case 8:
                SQL = ("UPDATE GUI SET FIRST_INVENTORY_ITEM_LORE6 WHERE=? UUID=?");
                break;
            case 9:
                SQL = ("UPDATE GUI SET FIRST_INVENTORY_ITEM_ADD_ENCHANTMENT_NAME=? WHERE UUID=?");
                break;
            case 10:
                SQL = ("UPDATE GUI SET FIRST_INVENTORY_ITEM_ADD_ENCHANTMENT_LEVEL=? WHERE UUID=?");
                break;
            case 11:
                SQL = ("UPDATE GUI SET FIRST_INVENTORY_ITEM_REGISTER_KEY=? WHERE UUID=?");
                break;
        }

        try {
            PreparedStatement prepStmt = con.prepareStatement(SQL);
            prepStmt.setString(1, InsertData);
            prepStmt.setString(2, player.getUniqueId().toString());
        }catch (Exception e) {
            ErrorLog(e);
        }
    }

     */

    public void GetMySQL() {
        mysql.User = getConfig().getString("DataBase.Login_User");
        mysql.PassWord = getConfig().getString("DataBase.Login_Password");
        mysql.Host = getConfig().getString("DataBase.Host_Name");
        mysql.Port = getConfig().getInt("DataBase.Port");
        mysql.DataBaseName = getConfig().getString("DataBase.DataBaseName");
    }
}