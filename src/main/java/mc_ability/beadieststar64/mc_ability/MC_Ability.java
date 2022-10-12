package mc_ability.beadieststar64.mc_ability;

import mc_ability.beadieststar64.mc_ability.ActiveSkill.ActivePickaxeSkillClass;
import mc_ability.beadieststar64.mc_ability.GUI.DisplayGUI;
import mc_ability.beadieststar64.mc_ability.GUI.PlayerGUI;
import mc_ability.beadieststar64.mc_ability.PassiveSkill.PassivePickaxeSkillClass;
import mc_ability.beadieststar64.mc_ability.Utility.CommandClass;
import mc_ability.beadieststar64.mc_ability.Utility.SetUpFiles;
import mc_ability.beadieststar64.mc_ability.oroginal_item.OriginalItemClass;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public final class MC_Ability extends JavaPlugin implements Listener {

    public static Connection con;

    public static MC_Ability plugins;

    public static PassivePickaxeSkillClass passivePickaxeSkillClass = new PassivePickaxeSkillClass(plugins);
    public static PlayerGUI playerGUI = new PlayerGUI();

    public static ActivePickaxeSkillClass activePickaxeSkillClass = new ActivePickaxeSkillClass(plugins);

    public static SetUpFiles setUpFiles = new SetUpFiles();

    public int MaxPlayers = 1;
    public static boolean IsOnlinePlayer = false;
    public static boolean Disable = false;

    //プラグインバージョン設定
    private final String version = "1.1.0";


    @Override
    public void onEnable() {

        plugins = this;

        //イベント登録
        RegisterEvents();

        //コマンド登録
        RegisterCommand();

        //MC_Abilityオリジナルアイテム追加
        OriginalItemClass originalItemClass = new OriginalItemClass(plugins);
        originalItemClass.OriginalItmCreate();

        getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "MC_Ability has been activate!");

        getServer().getConsoleSender().sendMessage(ChatColor.WHITE + "Version is" +
                ChatColor.RED + "" + ChatColor.RED + version +
                ChatColor.WHITE + ".");

        //データベースに接続
        try {

            //Config.ymlを作成
            File configFile = new File(getDataFolder(), "config.yml");
            if (!configFile.exists()) {
                this.saveDefaultConfig();
            }

            //SQLiteロック回避用のファイル作成
            SetUpFiles.SetUp();
            SetUpFiles.get().addDefault("MC_Ability", "player:");
            SetUpFiles.get().options().copyDefaults(true);
            SetUpFiles.SaveFile();

            // JDBCドライバーの指定
            Class.forName("org.sqlite.JDBC");
            //データベース設定
            con = DriverManager.getConnection("jdbc:sqlite:" + getDataFolder() + File.separator + "MC_ABILITY.db");
            con.setAutoCommit(false);

            //Statementオブジェクト作成
            Statement stmt = con.createStatement();
            stmt.setQueryTimeout(30);    // タイムアウト設定

            try {

                /*
                DBの種類

                1. プレイヤー情報
                2. Passive Skillのレベル・経験値
                3. Active Skillの発動状況・残り持続時間・最大持続時間・エンチャントレベル・クールダウン状況・残りクールダウン時間・最大クール時間・発動中にログアウトしたか
                4. GUI情報(マテリアル・アイテム名・説明1・説明2・説明3・説明4・説明5・説明6・付与エンチャント名・付与エンチャントレベル・登録キー)

                 */

                //テーブルの作成
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS PLAYER(" +
                        "MCID" + //MCID
                        ", UUID" + //UUID
                        ");");

                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS PASSIVE_SKILL(" +
                        "MCID" +
                        ", UUID" + //UUID
                        ", PLAYER_MINER_LEVEL" + //現在のマイナーレベル
                        ", PLAYER_MINER_EXP" + //現在のマイナー経験値
                        ", PLAYER_NEXT_MINER_LEVEL" + //次のマイナーレベル
                        ", PLAYER_NEXT_MINER_EXP" + //次のマイナー経験値
                        ", PLAYER_DIGGER_LEVEL" + //現在の掘削レベル
                        ", PLAYER_DIGGER_EXP" + //現在の採掘経験値
                        ", PLAYER_NEXT_DIGGER_LEVEL" +
                        ", PLAYER_NEXT_DIGGER_EXP" +
                        ", PLAYER_HUNT_LEVEL" +
                        ", PLAYER_HUNT_EXP" +
                        ", PLAYER_NEXT_HUNT_PLAYER" +
                        ", PLAYER_NEXT_HUNT_EXP" +
                        ", PLAYER_FISHING_LEVEL" +
                        ", PLAYER_FISHING_EXP" +
                        ", PLAYER_NEXT_FISHING_LEVEL" +
                        ", PLAYER_NEXT_FISHING_EXP" +
                        ");");

                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ACTIVE_SKILL(" +
                        "MCID" +
                        ", UUID" +
                        ", IS_MINER_ACTIVATE" + //マイナーが発動しているか
                        ", REMAINING_MINER_TIME" + //残りマイナー発動の時間
                        ", MAX_MINER_TIME" + //マイナーの効果時間
                        ", MINER_ENCHANTMENT_LEVEL" + //マイナーで付与されたエンチャントレベル
                        ", IS_MINER_COOL_DOWN" + //マイナーでクールダウンが有効か
                        ", REMAINING_COOL_DOWN_TIME" + //残りクールダウン時間
                        ", MAX_COOL_DOWN_TIME" + //クールダウンの時間
                        ", IS_PLAYER_LOG_OUT" + //マイナー発動中にログアウトしたか
                        ");");

                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS GUI(" +
                        "MCID" +
                        ", UUID" +
                        ", GUI_SIZE" +
                        ", GUI_TITLE" +
                        ", FIRST_INVENTORY_ITEM_MATERIAL" +
                        ", FIRST_INVENTORY_ITEM_NAME" +
                        ", FIRST_INVENTORY_ITEM_LORE1" +
                        ", FIRST_INVENTORY_ITEM_LORE2" +
                        ", FIRST_INVENTORY_ITEM_LORE3" +
                        ", FIRST_INVENTORY_ITEM_LORE4" +
                        ", FIRST_INVENTORY_ITEM_LORE5" +
                        ", FIRST_INVENTORY_ITEM_LORE6" +
                        ", FIRST_INVENTORY_ITEM_ADD_ENCHANTMENT_NAME" +
                        ", FIRST_INVENTORY_ITEM_ADD_ENCHANTMENT_LEVEL" +
                        ", FIRST_INVENTORY_ITEM_REGISTER_KEY" +
                        ", SECOND_INVENTORY_ITEM_MATERIAL" +
                        ", SECOND_INVENTORY_ITEM_NAME" +
                        ", SECOND_INVENTORY_ITEM_LORE1" +
                        ", SECOND_INVENTORY_ITEM_LORE2" +
                        ", SECOND_INVENTORY_ITEM_LORE3" +
                        ", SECOND_INVENTORY_ITEM_LORE4" +
                        ", SECOND_INVENTORY_ITEM_LORE5" +
                        ", SECOND_INVENTORY_ITEM_LORE6" +
                        ", SECOND_INVENTORY_ITEM_ADD_ENCHANTMENT_NAME" +
                        ", SECOND_INVENTORY_ITEM_ADD_ENCHANTMENT_LEVEL" +
                        ", SECOND_INVENTORY_ITEM_REGISTER_KEY" +
                        ", THIRD_INVENTORY_ITEM_MATERIAL" +
                        ", THIRD_INVENTORY_ITEM_NAME" +
                        ", THIRD_INVENTORY_ITEM_LORE1" +
                        ", THIRD_INVENTORY_ITEM_LORE2" +
                        ", THIRD_INVENTORY_ITEM_LORE3" +
                        ", THIRD_INVENTORY_ITEM_LORE4" +
                        ", THIRD_INVENTORY_ITEM_LORE5" +
                        ", THIRD_INVENTORY_ITEM_LORE6" +
                        ", THIRD_INVENTORY_ITEM_ADD_ENCHANTMENT_NAME" +
                        ", THIRD_INVENTORY_ITEM_ADD_ENCHANTMENT_LEVEL" +
                        ", THIRD_INVENTORY_ITEM_REGISTER_KEY" +
                        ", FORTH_INVENTORY_ITEM_MATERIAL" +
                        ", FORTH_INVENTORY_ITEM_NAME" +
                        ", FORTH_INVENTORY_ITEM_LORE1" +
                        ", FORTH_INVENTORY_ITEM_LORE2" +
                        ", FORTH_INVENTORY_ITEM_LORE3" +
                        ", FORTH_INVENTORY_ITEM_LORE4" +
                        ", FORTH_INVENTORY_ITEM_LORE5" +
                        ", FORTH_INVENTORY_ITEM_LORE6" +
                        ", FORTH_INVENTORY_ITEM_ADD_ENCHANTMENT_NAME" +
                        ", FORTH_INVENTORY_ITEM_ADD_ENCHANTMENT_LEVEL" +
                        ", FORTH_INVENTORY_ITEM_REGISTER_KEY" +
                        ", FIFTH_INVENTORY_ITEM_MATERIAL" +
                        ", FIFTH_INVENTORY_ITEM_NAME" +
                        ", FIFTH_INVENTORY_ITEM_LORE1" +
                        ", FIFTH_INVENTORY_ITEM_LORE2" +
                        ", FIFTH_INVENTORY_ITEM_LORE3" +
                        ", FIFTH_INVENTORY_ITEM_LORE4" +
                        ", FIFTH_INVENTORY_ITEM_LORE5" +
                        ", FIFTH_INVENTORY_ITEM_LORE6" +
                        ", FIFTH_INVENTORY_ITEM_ADD_ENCHANTMENT_NAME" +
                        ", FIFTH_INVENTORY_ITEM_ADD_ENCHANTMENT_LEVEL" +
                        ", FIFTH_INVENTORY_ITEM_REGISTER_KEY" +
                        ", SIXTH_INVENTORY_ITEM_MATERIAL" +
                        ", SIXTH_INVENTORY_ITEM_NAME" +
                        ", SIXTH_INVENTORY_ITEM_LORE1" +
                        ", SIXTH_INVENTORY_ITEM_LORE2" +
                        ", SIXTH_INVENTORY_ITEM_LORE3" +
                        ", SIXTH_INVENTORY_ITEM_LORE4" +
                        ", SIXTH_INVENTORY_ITEM_LORE5" +
                        ", SIXTH_INVENTORY_ITEM_LORE6" +
                        ", SIXTH_INVENTORY_ITEM_ADD_ENCHANTMENT_NAME" +
                        ", SIXTH_INVENTORY_ITEM_ADD_ENCHANTMENT_LEVEL" +
                        ", SIXTH_INVENTORY_ITEM_REGISTER_KEY" +
                        ", SEVENTH_INVENTORY_ITEM_MATERIAL" +
                        ", SEVENTH_INVENTORY_ITEM_NAME" +
                        ", SEVENTH_INVENTORY_ITEM_LORE1" +
                        ", SEVENTH_INVENTORY_ITEM_LORE2" +
                        ", SEVENTH_INVENTORY_ITEM_LORE3" +
                        ", SEVENTH_INVENTORY_ITEM_LORE4" +
                        ", SEVENTH_INVENTORY_ITEM_LORE5" +
                        ", SEVENTH_INVENTORY_ITEM_LORE6" +
                        ", SEVENTH_INVENTORY_ITEM_ADD_ENCHANTMENT_NAME" +
                        ", SEVENTH_INVENTORY_ITEM_ADD_ENCHANTMENT_LEVEL" +
                        ", SEVENTH_INVENTORY_ITEM_REGISTER_KEY" +
                        ", EIGHTH_INVENTORY_ITEM_MATERIAL" +
                        ", EIGHTH_INVENTORY_ITEM_NAME" +
                        ", EIGHTH_INVENTORY_ITEM_LORE1" +
                        ", EIGHTH_INVENTORY_ITEM_LORE2" +
                        ", EIGHTH_INVENTORY_ITEM_LORE3" +
                        ", EIGHTH_INVENTORY_ITEM_LORE4" +
                        ", EIGHTH_INVENTORY_ITEM_LORE5" +
                        ", EIGHTH_INVENTORY_ITEM_LORE6" +
                        ", EIGHTH_INVENTORY_ITEM_ADD_ENCHANTMENT_NAME" +
                        ", EIGHTH_INVENTORY_ITEM_ADD_ENCHANTMENT_LEVEL" +
                        ", EIGHTH_INVENTORY_ITEM_REGISTER_KEY" +
                        ");");
                con.commit();
                stmt.close();

            }catch (Exception e) {
                ErrorLog(e);
            }
            getServer().getConsoleSender().sendMessage("テーブル処理が完了しました");

        }catch (Exception e) {
            ErrorLog(e);
        }
    }

    @EventHandler
    public void ConnectionDataBase(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        Map<String, Boolean> IncludePlayer = new HashMap<String, Boolean>();

        try {
            //MC_ABILITYテーブルにプレイヤーが含まれるか確認
            IncludePlayer.put(player.getUniqueId().toString(),false);

            String PlayerName = "";
            String UpdatePlayer = "";
            String UpdateActiveSkill = "";
            String UpdatePassiveSkill = "";
            String UpdateGUI = "";

            PreparedStatement prepStmt = con.prepareStatement("SELECT MCID FROM PLAYER WHERE UUID=?");
            prepStmt.setString(1, player.getUniqueId().toString());
            ResultSet rs = prepStmt.executeQuery();
            con.commit();
            if (rs.next()) {
                IncludePlayer.put(player.getUniqueId().toString(),true);
                PlayerName = rs.getString(1);
                MaxPlayers++;
                if (!PlayerName.equals(event.getPlayer().getName())) {
                    UpdatePlayer = "UPDATE PLAYER SET MCID=? WHERE UUID=?";
                    UpdateActiveSkill = "UPDATE ACTIVE_SKILL SET MCID=? WHERE UUID=?";
                    UpdatePassiveSkill = "UPDATE PASSIVE_SKILL SET MCID=? WHERE UUID=?";
                    UpdateGUI = "UPDATE GUI SET MCID=? WHERE UUID=?";
                }
            }
            rs.close();
            prepStmt.close();

            //新規プレイヤーならデーターを追加
            if (!IncludePlayer.get(player.getUniqueId().toString())) {
                getServer().getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + "新規プレイヤー情報が見つかりませんでした。データを追加します。");

                prepStmt = con.prepareStatement("INSERT INTO PLAYER(" +
                        "MCID" +
                        ", UUID" +
                        ") VALUES (" +
                        "?" +
                        ", ?" +
                        ");");
                prepStmt.setString(1, player.getName());
                prepStmt.setString(2, player.getUniqueId().toString());
                prepStmt.addBatch();

                PreparedStatement prepStmt2 = con.prepareStatement("INSERT INTO PASSIVE_SKILL(" +
                        "MCID" +
                        ", UUID" + //UUID
                        ", PLAYER_MINER_LEVEL" + //現在のマイナーレベル
                        ", PLAYER_MINER_EXP" + //現在のマイナー経験値
                        ", PLAYER_NEXT_MINER_LEVEL" + //次のマイナーレベル
                        ", PLAYER_NEXT_MINER_EXP" + //次のマイナー経験値
                        ", PLAYER_DIGGER_LEVEL" + //現在の掘削レベル
                        ", PLAYER_DIGGER_EXP" + //現在の採掘経験値
                        ", PLAYER_NEXT_DIGGER_LEVEL" +
                        ", PLAYER_NEXT_DIGGER_EXP" +
                        ", PLAYER_HUNT_LEVEL" +
                        ", PLAYER_HUNT_EXP" +
                        ", PLAYER_NEXT_HUNT_PLAYER" +
                        ", PLAYER_NEXT_HUNT_EXP" +
                        ", PLAYER_FISHING_LEVEL" +
                        ", PLAYER_FISHING_EXP" +
                        ", PLAYER_NEXT_FISHING_LEVEL" +
                        ", PLAYER_NEXT_FISHING_EXP" +
                        ") VALUES (" +
                        "?" +
                        ", ?" +
                        ", 1" +
                        ", 0" +
                        ", 2" +
                        ", 20" +
                        ", 1" +
                        ", 0" +
                        ", 2" +
                        ", 20" +
                        ", 1" +
                        ", 0" +
                        ", 2" +
                        ", 20" +
                        ", 1" +
                        ", 0" +
                        ", 2" +
                        ", 20" +
                        ");");
                prepStmt2.setString(1, player.getName());
                prepStmt2.setString(2, player.getUniqueId().toString());
                prepStmt2.addBatch();

                PreparedStatement prepStmt3 = con.prepareStatement("INSERT INTO ACTIVE_SKILL(" +
                        "MCID" +
                        ", UUID" +
                        ", IS_MINER_ACTIVATE" +
                        ", REMAINING_MINER_TIME" +
                        ", MAX_MINER_TIME" +
                        ", MINER_ENCHANTMENT_LEVEL" +
                        ", IS_MINER_COOL_DOWN" +
                        ", REMAINING_COOL_DOWN_TIME" +
                        ", MAX_COOL_DOWN_TIME" +
                        ", IS_PLAYER_LOG_OUT" +
                        ") VALUES (" +
                        "?" +
                        ", ?" +
                        ", 0" +
                        ", 20" +
                        ", 20" +
                        ", 0" +
                        ", 0" +
                        ", 30" +
                        ", 30" +
                        ", 0" +
                        ");");
                prepStmt3.setString(1, player.getName());
                prepStmt3.setString(2, player.getUniqueId().toString());
                prepStmt3.addBatch();

                PreparedStatement prepStmt4 = con.prepareStatement("INSERT INTO GUI(" +
                        "MCID" +
                        ", UUID" +
                        ", GUI_SIZE" +
                        ", GUI_TITLE" +
                        ", FIRST_INVENTORY_ITEM_MATERIAL" +
                        ", FIRST_INVENTORY_ITEM_NAME" +
                        ", FIRST_INVENTORY_ITEM_LORE1" +
                        ", FIRST_INVENTORY_ITEM_LORE2" +
                        ", FIRST_INVENTORY_ITEM_LORE3" +
                        ", FIRST_INVENTORY_ITEM_LORE4" +
                        ", FIRST_INVENTORY_ITEM_LORE5" +
                        ", FIRST_INVENTORY_ITEM_LORE6" +
                        ", FIRST_INVENTORY_ITEM_ADD_ENCHANTMENT_NAME" +
                        ", FIRST_INVENTORY_ITEM_ADD_ENCHANTMENT_LEVEL" +
                        ", FIRST_INVENTORY_ITEM_REGISTER_KEY" +
                        ", SECOND_INVENTORY_ITEM_MATERIAL" +
                        ", SECOND_INVENTORY_ITEM_NAME" +
                        ", SECOND_INVENTORY_ITEM_LORE1" +
                        ", SECOND_INVENTORY_ITEM_LORE2" +
                        ", SECOND_INVENTORY_ITEM_LORE3" +
                        ", SECOND_INVENTORY_ITEM_LORE4" +
                        ", SECOND_INVENTORY_ITEM_LORE5" +
                        ", SECOND_INVENTORY_ITEM_LORE6" +
                        ", SECOND_INVENTORY_ITEM_ADD_ENCHANTMENT_NAME" +
                        ", SECOND_INVENTORY_ITEM_ADD_ENCHANTMENT_LEVEL" +
                        ", SECOND_INVENTORY_ITEM_REGISTER_KEY" +
                        ", THIRD_INVENTORY_ITEM_MATERIAL" +
                        ", THIRD_INVENTORY_ITEM_NAME" +
                        ", THIRD_INVENTORY_ITEM_LORE1" +
                        ", THIRD_INVENTORY_ITEM_LORE2" +
                        ", THIRD_INVENTORY_ITEM_LORE3" +
                        ", THIRD_INVENTORY_ITEM_LORE4" +
                        ", THIRD_INVENTORY_ITEM_LORE5" +
                        ", THIRD_INVENTORY_ITEM_LORE6" +
                        ", THIRD_INVENTORY_ITEM_ADD_ENCHANTMENT_NAME" +
                        ", THIRD_INVENTORY_ITEM_ADD_ENCHANTMENT_LEVEL" +
                        ", THIRD_INVENTORY_ITEM_REGISTER_KEY" +
                        ", FORTH_INVENTORY_ITEM_MATERIAL" +
                        ", FORTH_INVENTORY_ITEM_NAME" +
                        ", FORTH_INVENTORY_ITEM_LORE1" +
                        ", FORTH_INVENTORY_ITEM_LORE2" +
                        ", FORTH_INVENTORY_ITEM_LORE3" +
                        ", FORTH_INVENTORY_ITEM_LORE4" +
                        ", FORTH_INVENTORY_ITEM_LORE5" +
                        ", FORTH_INVENTORY_ITEM_LORE6" +
                        ", FORTH_INVENTORY_ITEM_ADD_ENCHANTMENT_NAME" +
                        ", FORTH_INVENTORY_ITEM_ADD_ENCHANTMENT_LEVEL" +
                        ", FORTH_INVENTORY_ITEM_REGISTER_KEY" +
                        ", FIFTH_INVENTORY_ITEM_MATERIAL" +
                        ", FIFTH_INVENTORY_ITEM_NAME" +
                        ", FIFTH_INVENTORY_ITEM_LORE1" +
                        ", FIFTH_INVENTORY_ITEM_LORE2" +
                        ", FIFTH_INVENTORY_ITEM_LORE3" +
                        ", FIFTH_INVENTORY_ITEM_LORE4" +
                        ", FIFTH_INVENTORY_ITEM_LORE5" +
                        ", FIFTH_INVENTORY_ITEM_LORE6" +
                        ", FIFTH_INVENTORY_ITEM_ADD_ENCHANTMENT_NAME" +
                        ", FIFTH_INVENTORY_ITEM_ADD_ENCHANTMENT_LEVEL" +
                        ", FIFTH_INVENTORY_ITEM_REGISTER_KEY" +
                        ", SIXTH_INVENTORY_ITEM_MATERIAL" +
                        ", SIXTH_INVENTORY_ITEM_NAME" +
                        ", SIXTH_INVENTORY_ITEM_LORE1" +
                        ", SIXTH_INVENTORY_ITEM_LORE2" +
                        ", SIXTH_INVENTORY_ITEM_LORE3" +
                        ", SIXTH_INVENTORY_ITEM_LORE4" +
                        ", SIXTH_INVENTORY_ITEM_LORE5" +
                        ", SIXTH_INVENTORY_ITEM_LORE6" +
                        ", SIXTH_INVENTORY_ITEM_ADD_ENCHANTMENT_NAME" +
                        ", SIXTH_INVENTORY_ITEM_ADD_ENCHANTMENT_LEVEL" +
                        ", SIXTH_INVENTORY_ITEM_REGISTER_KEY" +
                        ", SEVENTH_INVENTORY_ITEM_MATERIAL" +
                        ", SEVENTH_INVENTORY_ITEM_NAME" +
                        ", SEVENTH_INVENTORY_ITEM_LORE1" +
                        ", SEVENTH_INVENTORY_ITEM_LORE2" +
                        ", SEVENTH_INVENTORY_ITEM_LORE3" +
                        ", SEVENTH_INVENTORY_ITEM_LORE4" +
                        ", SEVENTH_INVENTORY_ITEM_LORE5" +
                        ", SEVENTH_INVENTORY_ITEM_LORE6" +
                        ", SEVENTH_INVENTORY_ITEM_ADD_ENCHANTMENT_NAME" +
                        ", SEVENTH_INVENTORY_ITEM_ADD_ENCHANTMENT_LEVEL" +
                        ", SEVENTH_INVENTORY_ITEM_REGISTER_KEY" +
                        ", EIGHTH_INVENTORY_ITEM_MATERIAL" +
                        ", EIGHTH_INVENTORY_ITEM_NAME" +
                        ", EIGHTH_INVENTORY_ITEM_LORE1" +
                        ", EIGHTH_INVENTORY_ITEM_LORE2" +
                        ", EIGHTH_INVENTORY_ITEM_LORE3" +
                        ", EIGHTH_INVENTORY_ITEM_LORE4" +
                        ", EIGHTH_INVENTORY_ITEM_LORE5" +
                        ", EIGHTH_INVENTORY_ITEM_LORE6" +
                        ", EIGHTH_INVENTORY_ITEM_ADD_ENCHANTMENT_NAME" +
                        ", EIGHTH_INVENTORY_ITEM_ADD_ENCHANTMENT_LEVEL" +
                        ", EIGHTH_INVENTORY_ITEM_REGISTER_KEY" +
                        ") VALUES (" +
                        "?" +
                        ", ?" +
                        ", 9" +
                        ", '\"ChatColor.AQUA+\\\"\\\"+ChatColor.BOLD\"+\\\"MC_Ability 魔法スキル\\\"\"'" +
                        ", 'AIR'" +
                        ", NULL" +
                        ", NULL" +
                        ", NULL" +
                        ", NULL" +
                        ", NULL" +
                        ", NULL" +
                        ", NULL" +
                        ", NULL" +
                        ", 0" +
                        ", NULL" +
                        ", 'AIR'" +
                        ", NULL" +
                        ", NULL" +
                        ", NULL" +
                        ", NULL" +
                        ", NULL" +
                        ", NULL" +
                        ", NULL" +
                        ", NULL" +
                        ", 0" +
                        ", NULL" +
                        ", 'AIR'" +
                        ", NULL" +
                        ", NULL" +
                        ", NULL" +
                        ", NULL" +
                        ", NULL" +
                        ", NULL" +
                        ", NULL" +
                        ", NULL" +
                        ", 0" +
                        ", NULL" +
                        ", 'AIR'" +
                        ", NULL" +
                        ", NULL" +
                        ", NULL" +
                        ", NULL" +
                        ", NULL" +
                        ", NULL" +
                        ", NULL" +
                        ", NULL" +
                        ", 0" +
                        ", NULL" +
                        ", 'AIR'" +
                        ", NULL" +
                        ", NULL" +
                        ", NULL" +
                        ", NULL" +
                        ", NULL" +
                        ", NULL" +
                        ", NULL" +
                        ", NULL" +
                        ", 0" +
                        ", NULL" +
                        ", 'AIR'" +
                        ", NULL" +
                        ", NULL" +
                        ", NULL" +
                        ", NULL" +
                        ", NULL" +
                        ", NULL" +
                        ", NULL" +
                        ", NULL" +
                        ", 0" +
                        ", NULL" +
                        ", 'AIR'" +
                        ", NULL" +
                        ", NULL" +
                        ", NULL" +
                        ", NULL" +
                        ", NULL" +
                        ", NULL" +
                        ", NULL" +
                        ", NULL" +
                        ", 0" +
                        ", NULL" +
                        ", 'BARRIER'" +
                        ", '\"ChatColor.RED+\\\"\\\"+ChatColor.BOLD\"+\\\"GUIを閉じる\\\"\"'" +
                        ", NULL" +
                        ", NULL" +
                        ", NULL" +
                        ", NULL" +
                        ", NULL" +
                        ", NULL" +
                        ", NULL" +
                        ", 0" +
                        ", '\"close_gui\"'" +
                        ");");
                prepStmt4.setString(1, player.getName());
                prepStmt4.setString(2, player.getUniqueId().toString());
                prepStmt4.addBatch();

                int[] cnt = prepStmt.executeBatch();
                int[] cnt2 = prepStmt2.executeBatch();
                int[] cnt3 = prepStmt3.executeBatch();
                int[] cnt4 = prepStmt4.executeBatch();

                con.commit();
                prepStmt.close();
                getLogger().info("データを追加しました");
            } else {
                getServer().getConsoleSender().sendMessage(ChatColor.WHITE + "[MC_Ability SQLite]" + ChatColor.AQUA + "データが見つかりました。データ追加は回避されます。");
            }

            //UUIDを参照してMCIDに変更がないか確認
            if (!UpdatePlayer.isEmpty()) {
                PreparedStatement prepStmt2 = con.prepareStatement("");
                PreparedStatement prepStmt3 = con.prepareStatement("");
                PreparedStatement prepStmt4 = con.prepareStatement("");

                prepStmt = con.prepareStatement(UpdatePlayer);
                prepStmt.setString(1, event.getPlayer().getName());
                prepStmt.setString(2, event.getPlayer().getUniqueId().toString());
                prepStmt.addBatch();

                prepStmt2 = con.prepareStatement(UpdateActiveSkill);
                prepStmt2.setString(1, event.getPlayer().getName());
                prepStmt2.setString(2, event.getPlayer().getUniqueId().toString());
                prepStmt2.addBatch();

                prepStmt3 = con.prepareStatement(UpdatePassiveSkill);
                prepStmt3.setString(1, event.getPlayer().getName());
                prepStmt3.setString(2, event.getPlayer().getUniqueId().toString());
                prepStmt3.addBatch();

                prepStmt4 = con.prepareStatement(UpdateGUI);
                prepStmt4.setString(1, event.getPlayer().getName());
                prepStmt4.setString(2, event.getPlayer().getUniqueId().toString());
                prepStmt4.addBatch();

                prepStmt.executeBatch();
                prepStmt2.executeBatch();
                prepStmt3.executeBatch();
                prepStmt4.executeBatch();

                con.commit();
                prepStmt.close();
            }

            //プレイヤーのスキル経験値を取得
            InsertPassivePickaxeSkillQuery(player);

            //プレイヤーのアクティブスキルデータを取得
            InsertActivePickaxeSkillQuery(player);

            if(ActivePickaxeSkillClass.IsPlayerLogOut.get(player)) {
                //ここでスキル前に修復
                activePickaxeSkillClass.Repairing(player);
            }

            MaxPlayers++;

            //セットアップ
            activePickaxeSkillClass.SetUp(player);
            passivePickaxeSkillClass.SetMethod(player);
            playerGUI.CreateGUI(player);

        }catch (Exception e) {
            ErrorLog(e);
        }
    }

    @EventHandler
    public void PlayerLeaveEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        UpdataPassivePickaxeSkill(player);
        UpdataActivePickaxeSkill(player);

        getServer().getConsoleSender().sendMessage(ChatColor.DARK_PURPLE+"ばいばい！");
        MaxPlayers--;

    }

    public void ErrorLog(Exception e) {
        getLogger().warning(e.toString());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Disable = true;

        OriginalItemClass originalItemClass = new OriginalItemClass(plugins);
        originalItemClass.RemoveRecipe();

        getLogger().info("プラグインが停止しました");
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "プラグインを終了します");
    }

    public void RegisterCommand() {
        getCommand("MC_Ability_PassiveSkill").setExecutor(new CommandClass());
        getCommand("MC_Ability_OpenGUI").setExecutor(new CommandClass());
    }

    public void RegisterEvents() {
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new ActivePickaxeSkillClass(this), this);
        getServer().getPluginManager().registerEvents(new PassivePickaxeSkillClass(this), this);
        getServer().getPluginManager().registerEvents(new PlayerGUI(), this);
        getServer().getPluginManager().registerEvents(new DisplayGUI(this), this);
    }

    public void InsertPassivePickaxeSkillQuery (Player player) {
        String SQL = ("SELECT PLAYER_MINER_LEVEL,PLAYER_MINER_EXP,PLAYER_NEXT_MINER_LEVEL,PLAYER_NEXT_MINER_EXP FROM PASSIVE_SKILL WHERE UUID=?");
        try {
            PreparedStatement prepStmt = con.prepareStatement(SQL);
            prepStmt.setString(1, player.getUniqueId().toString());
            ResultSet RS = prepStmt.executeQuery();
            con.commit();
            if (RS.next()) {
                PassivePickaxeSkillClass.GetPlayerMinerLevel.put(player, RS.getInt(1));
                PassivePickaxeSkillClass.GetPlayerMinerEXP.put(player, RS.getDouble(2));
                PassivePickaxeSkillClass.NextPlayerMinerLevel.put(player, RS.getInt(3));
                PassivePickaxeSkillClass.NextPlayerMinerEXP.put(player, RS.getDouble(4));
            }
            RS.close();
            prepStmt.close();

        }catch (Exception e) {
            ErrorLog(e);
        }
    }

    public void InsertActivePickaxeSkillQuery (Player player) {
        getLogger().info("Active Skill情報を参照します。");
        String SQL = ("SELECT IS_MINER_ACTIVATE,REMAINING_MINER_TIME,MAX_MINER_TIME,MINER_ENCHANTMENT_LEVEL,IS_MINER_COOL_DOWN,REMAINING_COOL_DOWN_TIME,MAX_COOL_DOWN_TIME,IS_PLAYER_LOG_OUT FROM ACTIVE_SKILL WHERE UUID=?");

        try {
            PreparedStatement prepStmt = con.prepareStatement(SQL);
            prepStmt.setString(1, player.getUniqueId().toString());
            getLogger().info("SQL文は" + prepStmt);
            ResultSet RS = prepStmt.executeQuery();
            con.commit();
            if (RS.next()) {
                if(RS.getInt(1) == 0) {
                    ActivePickaxeSkillClass.ActivationActiveSkill.put(player, false);
                }else if(RS.getInt(1) == 1) {
                    ActivePickaxeSkillClass.ActivationActiveSkill.put(player, true);
                }
                getLogger().info("ActivationActiveSkillは" + ActivePickaxeSkillClass.ActivationActiveSkill + "です。");

                ActivePickaxeSkillClass.ActivationSkillCountDownTime.put(player, RS.getInt(2));
                getLogger().info("ActivationSkillCountDownTimeは" + ActivePickaxeSkillClass.ActivationSkillCountDownTime + "です。");

                ActivePickaxeSkillClass.MaxActivationActiveSkillCountDownTime.put(player, RS.getInt(3));
                getLogger().info("MaxActivationActiveSkillCountDownTimeは" + ActivePickaxeSkillClass.MaxActivationActiveSkillCountDownTime + "です。");

                ActivePickaxeSkillClass.AddEnchantmentLevel.put(player, RS.getInt(4));
                getLogger().info("AddEnchantmentLevelは" + ActivePickaxeSkillClass.AddEnchantmentLevel + "です。");

                if(RS.getInt(5) == 0) {
                    ActivePickaxeSkillClass.CoolDown.put(player, false);
                }else if(RS.getInt(5) == 1) {
                    ActivePickaxeSkillClass.CoolDown.put(player, true);
                }
                getLogger().info("CoolDownは" + ActivePickaxeSkillClass.CoolDown + "です。");

                ActivePickaxeSkillClass.CoolDownTime.put(player, RS.getInt(6));
                getLogger().info("CoolDownTimeは" + ActivePickaxeSkillClass.CoolDownTime + "です。");

                ActivePickaxeSkillClass.MaxCoolDownTime.put(player, RS.getInt(7));
                getLogger().info("MaxCoolDownTimeは" + ActivePickaxeSkillClass.MaxCoolDownTime + "です。");

                if(RS.getInt(8) == 0) {
                    ActivePickaxeSkillClass.IsPlayerLogOut.put(player, false);
                }else if(RS.getInt(8) == 1) {
                    ActivePickaxeSkillClass.IsPlayerLogOut.put(player, true);
                }
                getLogger().info("IsPlayerLogOutは" + ActivePickaxeSkillClass.IsPlayerLogOut + "です。");
            }else{
                getLogger().info("これはやばい！やばいですよ！");
            }
            RS.close();
            prepStmt.close();

        }catch (Exception e) {
            ErrorLog(e);
        }
    }

    public void UpdataPassivePickaxeSkill(Player player) {
        getLogger().info(player + "のPassive Skill情報を更新します。");
        try {

            PreparedStatement prepStmt = con.prepareStatement("UPDATE PASSIVE_SKILL SET PLAYER_MINER_LEVEL=? WHERE UUID=?" );
            prepStmt.setDouble(1, PassivePickaxeSkillClass.GetPlayerMinerLevel.get(player));
            prepStmt.setString(2,player.getUniqueId().toString());
            prepStmt.addBatch();

            PreparedStatement prepStmt2 = con.prepareStatement("UPDATE PASSIVE_SKILL SET PLAYER_MINER_EXP=? WHERE UUID=?");
            prepStmt2.setDouble(1, PassivePickaxeSkillClass.GetPlayerMinerEXP.get(player));
            prepStmt2.setString(2,player.getUniqueId().toString());
            prepStmt2.addBatch();

            PreparedStatement prepStmt3 = con.prepareStatement("UPDATE PASSIVE_SKILL SET PLAYER_NEXT_MINER_LEVEL=? WHERE UUID=?");
            prepStmt3.setDouble(1, PassivePickaxeSkillClass.NextPlayerMinerLevel.get(player));
            prepStmt3.setString(2,player.getUniqueId().toString());
            prepStmt3.addBatch();

            PreparedStatement prepStmt4 = con.prepareStatement("UPDATE PASSIVE_SKILL SET PLAYER_NEXT_MINER_EXP=? WHERE UUID=?");
            prepStmt4.setDouble(1, PassivePickaxeSkillClass.NextPlayerMinerEXP.get(player));
            prepStmt4.setString(2,player.getUniqueId().toString());
            prepStmt4.addBatch();

            int[] cnt = prepStmt.executeBatch();
            int[] cnt2 = prepStmt2.executeBatch();
            int[] cnt3 = prepStmt3.executeBatch();
            int[] cnt4 = prepStmt4.executeBatch();

            con.commit();
            prepStmt.close();
            passivePickaxeSkillClass.DeleteHashMap(player);

            getLogger().info(player + "のPassive Skill情報を更新しました。");
        }catch (Exception e) {
            ErrorLog(e);
        }
    }

    public void UpdataActivePickaxeSkill(Player player) {
        getLogger().info(player + "のActive Skill情報を更新します。");
        try {
            getLogger().info("IS_MINER_ACTIVATEは" + ActivePickaxeSkillClass.ActivationActiveSkill.get(player) + "です。");
            PreparedStatement prepStmt = con.prepareStatement("UPDATE ACTIVE_SKILL SET IS_MINER_ACTIVATE=? WHERE UUID=?" );
            Map<Player, Integer> IsMinerActivateBoolean = new HashMap<>();
            IsMinerActivateBoolean.put(player, 1);
            if(ActivePickaxeSkillClass.ActivationActiveSkill.get(player)) {
                IsMinerActivateBoolean.put(player, 0);
            }
            prepStmt.setInt(1,IsMinerActivateBoolean.get(player));
            prepStmt.setString(2,player.getUniqueId().toString());
            prepStmt.addBatch();

            getLogger().info("REMAINING_MINER_TIMEは" + ActivePickaxeSkillClass.ActivationSkillCountDownTime.get(player) + "です。");
            PreparedStatement prepStmt2 = con.prepareStatement("UPDATE ACTIVE_SKILL SET REMAINING_MINER_TIME=? WHERE UUID=?");
            prepStmt2.setInt(1, ActivePickaxeSkillClass.ActivationSkillCountDownTime.get(player));
            prepStmt2.setString(2,player.getUniqueId().toString());
            prepStmt2.addBatch();

            getLogger().info("MAX_MINER_TIMEは" + ActivePickaxeSkillClass.MaxActivationActiveSkillCountDownTime.get(player) + "です。");
            PreparedStatement prepStmt3 = con.prepareStatement("UPDATE ACTIVE_SKILL SET MAX_MINER_TIME=? WHERE UUID=?");
            prepStmt3.setInt(1, ActivePickaxeSkillClass.MaxActivationActiveSkillCountDownTime.get(player));
            prepStmt3.setString(2,player.getUniqueId().toString());
            prepStmt3.addBatch();

            getLogger().info("MINER_ENCHANTMENT_LEVELは" + ActivePickaxeSkillClass.AddEnchantmentLevel.get(player) + "です。");
            PreparedStatement prepStmt4 = con.prepareStatement("UPDATE ACTIVE_SKILL SET MINER_ENCHANTMENT_LEVEL=? WHERE UUID=?");
            prepStmt4.setInt(1, ActivePickaxeSkillClass.AddEnchantmentLevel.get(player));
            prepStmt4.setString(2,player.getUniqueId().toString());
            prepStmt4.addBatch();

            getLogger().info("IS_MINER_COOL_DOWNは" + ActivePickaxeSkillClass.CoolDown.get(player) + "です。");
            PreparedStatement prepStmt5 = con.prepareStatement("UPDATE ACTIVE_SKILL SET IS_MINER_COOL_DOWN=? WHERE UUID=?");
            Map<Player, Integer> IsMinerCoolDownBoolean = new HashMap<>();
            IsMinerCoolDownBoolean.put(player, 1);
            if(ActivePickaxeSkillClass.CoolDown.get(player)) {
                IsMinerCoolDownBoolean.put(player, 0);
            }
            prepStmt5.setInt(1, IsMinerCoolDownBoolean.get(player));
            prepStmt5.setString(2, player.getUniqueId().toString());
            prepStmt5.addBatch();

            getLogger().info("REMAINING_COOL_DOWN_TIMEは" + ActivePickaxeSkillClass.CoolDownTime.get(player) + "です。");
            PreparedStatement prepStmt6 = con.prepareStatement("UPDATE ACTIVE_SKILL SET REMAINING_COOL_DOWN_TIME=? WHERE UUID=?");
            prepStmt6.setInt(1, ActivePickaxeSkillClass.CoolDownTime.get(player));
            prepStmt6.setString(2,player.getUniqueId().toString());
            prepStmt6.addBatch();

            getLogger().info("MAX_COOL_DOWN_TIMEは" + ActivePickaxeSkillClass.MaxCoolDownTime.get(player) + "です。");
            PreparedStatement prepStmt7 = con.prepareStatement("UPDATE ACTIVE_SKILL SET MAX_COOL_DOWN_TIME=? WHERE UUID=?");
            prepStmt7.setInt(1, ActivePickaxeSkillClass.MaxCoolDownTime.get(player));
            prepStmt7.setString(2,player.getUniqueId().toString());
            prepStmt7.addBatch();

            getLogger().info("IS_PLAYER_LOG_OUTは" + ActivePickaxeSkillClass.IsPlayerLogOut.get(player) + "です。");
            PreparedStatement prepStmt8 = con.prepareStatement("UPDATE ACTIVE_SKILL SET IS_PLAYER_LOG_OUT=? WHERE UUID=?");
            Map<Player, Integer> IsPlayerLogOut = new HashMap<>();
            IsPlayerLogOut.put(player, 1);
            if(ActivePickaxeSkillClass.IsPlayerLogOut.get(player)) {
                IsPlayerLogOut.put(player, 0);
            }
            prepStmt8.setInt(1, IsPlayerLogOut.get(player));
            prepStmt8.setString(2, player.getUniqueId().toString());
            prepStmt8.addBatch();

            int[] cnt = prepStmt.executeBatch();
            int[] cnt2 = prepStmt2.executeBatch();
            int[] cnt3 = prepStmt3.executeBatch();
            int[] cnt4 = prepStmt4.executeBatch();
            int[] cnt5 = prepStmt5.executeBatch();
            int[] cnt6 = prepStmt6.executeBatch();
            int[] cnt7 = prepStmt7.executeBatch();
            int[] cnt8 = prepStmt8.executeBatch();

            con.commit();
            prepStmt.close();
            passivePickaxeSkillClass.DeleteHashMap(player);

            getLogger().info(player + "のActive Skill情報を更新しました。");
        }catch (Exception e) {
            ErrorLog(e);
        }
    }

}