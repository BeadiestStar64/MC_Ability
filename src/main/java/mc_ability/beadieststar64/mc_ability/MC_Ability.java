package mc_ability.beadieststar64.mc_ability;

import mc_ability.beadieststar64.mc_ability.ActiveSkill.ActivePickaxeSkillClass;
import mc_ability.beadieststar64.mc_ability.ActiveSkill.ActiveShovelSkillClass;
import mc_ability.beadieststar64.mc_ability.PassiveSkill.PassivePickaxeSkillClass;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.*;

public final class MC_Ability extends JavaPlugin implements Listener {

    private Connection con;

    public static MC_Ability plugins;

    public int MaxPlayers = 1;


    @Override
    public void onEnable() {

        plugins = this;

        //イベント登録
        getServer().getPluginManager().registerEvents(this,this);
        getServer().getPluginManager().registerEvents(new ActivePickaxeSkillClass(this), this);
        getServer().getPluginManager().registerEvents(new ActiveShovelSkillClass(this), this);
        getServer().getPluginManager().registerEvents(new PassivePickaxeSkillClass(this),this);

        getServer().getConsoleSender().sendMessage(ChatColor.AQUA+"Plugin is Active!");
        //プラグインバージョン設定
        String version = "1.0.4";
        getServer().getConsoleSender().sendMessage(ChatColor.WHITE+"Version is" +
                ChatColor.RED+""+ChatColor.RED+ version +
                ChatColor.WHITE+".");

        //MC_Abilityオリジナルアイテム追加
        mc_ability.beadieststar64.mc_ability.oroginal_item.OriginalItemClass OriginalClass = new mc_ability.beadieststar64.mc_ability.oroginal_item.OriginalItemClass(plugins);
        OriginalClass.OriginalItmCreate();

        //データベースに接続
        try{

            //Config.ymlを作成
            File configFile = new File(getDataFolder(), "config.yml");
            if(!configFile.exists()){
                this.saveDefaultConfig();
            }

            // JDBCドライバーの指定
            Class.forName("org.sqlite.JDBC");
            //データベース設定
            con = DriverManager.getConnection("jdbc:sqlite:" + getDataFolder() + File.separator + "MC_ABILITY.db");
            con.setAutoCommit(false);

            //Statementオブジェクト作成
            Statement stmt = con.createStatement();
            stmt.setQueryTimeout(30);    // タイムアウト設定

            //テーブルの作成
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS PLAYER(" +
                    "MCID" + //MCID
                    ", UUID" + //UUID
                    ", AXE_WOODCUTTER_LEVEL" + //木こりレベル
                    ", AXE_KILLER_LEVEL" + //キラー(斧)レベル
                    ", EXCAVATION_LEVEL" + //掘削レベル
                    ", ENCHANT_LEVEL" + //エンチャントレベル
                    ", GUARD_LEVEL" + //受け身レベル
                    ", HAND_LEVEL" + //素手レベル
                    ", HOE_LEVEL" + //鍬れベル
                    ", MINER_LEVEL" + //採掘レベル
                    ");");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS PLAYER_PASSIVE_SKILL_LEVEL(" +
                    "UUID" + //UUID
                    ", PLAYER_MINER_LEVEL" + //現在のマイナーレベル
                    ", PLAYER_MINER_EXP" + //現在のマイナー経験値
                    ", PLAYER_NEXT_MINER_LEVEL" + //次のマイナーレベル
                    ", PLAYER_NEXT_MINER_EXP" + //次のマイナー経験値
                    ");");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ACTIVESKILL(" +
                    "UUID" +
                    ", PICKAXE_SKILL_ACTIVATE" +
                    ", PICKAXE_SKILL_ACTIVATE_TIME" +
                    ", PICKAXE_SKILL_ACTIVATE_ENCHANTMENT_LEVEL" +
                    ", SHOVEL_SKILL_ACTIVATE" +
                    ", SHOVEL_SKILL_ACTIVATE_TIME" +
                    ", SHOVEL_SKILL_ACTIVATE_ENCHANTMENT_LEVEL" +
                    ");");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ONLINEPLAYER(" +
                    "NAME" +
                    ", UUID" +
                    ");");
            con.commit();
            stmt.close();

            getServer().getConsoleSender().sendMessage("テーブル処理が完了しました");

        }catch (Exception e) {
            getLogger().warning(e.toString());
        }
    }

    @EventHandler
    public void ConnectionDataBase(PlayerJoinEvent event) {
        Player player = event.getPlayer();

            try {
                //MC_ABILITYテーブルにプレイヤーが含まれるか確認
                boolean IncludePlayer = false;
                String PlayerName = "";
                String UpdatePlayer = "";
                PreparedStatement prepStmt = con.prepareStatement("SELECT MCID FROM PLAYER WHERE UUID=?");
                prepStmt.setString(1, player.getUniqueId().toString());
                ResultSet rs = prepStmt.executeQuery();
                con.commit();
                if (rs.next()) {
                    IncludePlayer = true;
                    PlayerName = rs.getString(1);
                    MaxPlayers++;
                    if (!PlayerName.equals(event.getPlayer().getName())) {
                        UpdatePlayer = "UPDATE users SET playername=? WHERE uuid=?";
                    }
                }
                rs.close();
                prepStmt.close();

                //新規プレイヤーならデーターを追加
                if (!IncludePlayer) {
                    getServer().getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + "新規プレイヤー情報が見つかりませんでした。データを追加します。");
                    prepStmt = con.prepareStatement("INSERT INTO PLAYER(" +
                            "MCID" +
                            ", UUID" +
                            ", AXE_WOODCUTTER_LEVEL" +
                            ", AXE_KILLER_LEVEL" +
                            ", EXCAVATION_LEVEL" +
                            ", ENCHANT_LEVEL" +
                            ", GUARD_LEVEL" +
                            ", HAND_LEVEL" +
                            ", HOE_LEVEL" +
                            ", MINER_LEVEL" +
                            ") VALUES (" +
                            " ? " +
                            ",?" +
                            ",0" +
                            ",0" +
                            ",0" +
                            ",0" +
                            ",0" +
                            ",0" +
                            ",0" +
                            ",0" +
                            ");");
                    prepStmt.setString(1, player.getName());
                    prepStmt.setString(2, player.getUniqueId().toString());
                    prepStmt.addBatch();
                    PreparedStatement prepStmt2 = con.prepareStatement("INSERT INTO PLAYER_PASSIVE_SKILL_LEVEL(" +
                            "UUID" +
                            ", PLAYER_MINER_LEVEL" +
                            ", PLAYER_MINER_EXP" +
                            ", PLAYER_NEXT_MINER_LEVEL" +
                            ", PLAYER_NEXT_MINER_EXP" +
                            ") VALUES (" +
                            " ?" +
                            ", 1" +
                            ", 0" +
                            ", 2" +
                            ", 10" +
                            ");");
                    prepStmt2.setString(1, player.getUniqueId().toString());
                    prepStmt2.addBatch();
                    PreparedStatement prepStmt3 = con.prepareStatement("INSERT INTO ACTIVESKILL(" +
                            "UUID" +
                            ", PICKAXE_SKILL_ACTIVATE" +
                            ", PICKAXE_SKILL_ACTIVATE_TIME" +
                            ", PICKAXE_SKILL_ACTIVATE_ENCHANTMENT_LEVEL" +
                            ", SHOVEL_SKILL_ACTIVATE" +
                            ", SHOVEL_SKILL_ACTIVATE_TIME" +
                            ", SHOVEL_SKILL_ACTIVATE_ENCHANTMENT_LEVEL" +
                            ") VALUES (" +
                            "?" +
                            ",1" +
                            ",0" +
                            ",0" +
                            ",1" +
                            ",0" +
                            ",0" +
                            ");");
                    prepStmt3.setString(1, player.getUniqueId().toString());
                    prepStmt3.addBatch();

                    int[] cnt = prepStmt.executeBatch();
                    int[] cnt2 = prepStmt2.executeBatch();
                    int[] cnt3 = prepStmt3.executeBatch();

                    con.commit();
                    prepStmt.close();
                    getLogger().info("データを追加しました");
                } else {
                    getServer().getConsoleSender().sendMessage(ChatColor.WHITE + "[MC_Ability SQLite]" + ChatColor.AQUA + "データが見つかりました。データ追加は回避されます。");
                }

                //UUIDを参照してMCIDに変更がないか確認
                if (!UpdatePlayer.isEmpty()) {
                    prepStmt = con.prepareStatement(UpdatePlayer);
                    prepStmt.setString(1, event.getPlayer().getName());
                    prepStmt.setString(2, event.getPlayer().getUniqueId().toString());
                    prepStmt.addBatch();
                    prepStmt.executeBatch();
                    con.commit();
                    prepStmt.close();
                }

                //プレイヤーのスキル経験値を取得
                PreparedStatement PrepStmt = con.prepareStatement("SELECT " +
                        "PLAYER_MINER_LEVEL" +
                        ",PLAYER_MINER_EXP" +
                        ",PLAYER_NEXT_MINER_LEVEL" +
                        ",PLAYER_NEXT_MINER_EXP" +
                        " FROM PLAYER_PASSIVE_SKILL_LEVEL" +
                        " WHERE UUID=?");
                PrepStmt.setString(1, player.getUniqueId().toString());
                getLogger().info("SQL文は" + PrepStmt);
                ResultSet RS = PrepStmt.executeQuery();
                con.commit();
                if (RS.next()) {
                    PassivePickaxeSkillClass.GetPlayerMinerLevel = RS.getInt(1);
                    PassivePickaxeSkillClass.GetPlayerMinerEXP = RS.getInt(2);
                    PassivePickaxeSkillClass.NextPlayerMinerLevel = RS.getInt(3);
                    PassivePickaxeSkillClass.NextPlayerMinerEXP = RS.getInt(4);
                    getLogger().info("値は"+ PassivePickaxeSkillClass.GetPlayerMinerLevel +
                            "、"+ PassivePickaxeSkillClass.GetPlayerMinerEXP
                            +"、"+ PassivePickaxeSkillClass.NextPlayerMinerLevel
                            +"、"+ PassivePickaxeSkillClass.NextPlayerMinerEXP);
                }
                RS.close();
                PrepStmt.close();
                MaxPlayers++;
            }catch (Exception e) {
                getLogger().warning(e.toString());
            }
        }


    @EventHandler
    public void PlayerLeaveEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        try {
            PreparedStatement prepStmt = con.prepareStatement("UPDATE PLAYER_PASSIVE_SKILL_LEVEL SET PLAYER_MINER_LEVEL=? WHERE UUID=?" );
            prepStmt.setDouble(1, PassivePickaxeSkillClass.GetPlayerMinerLevel);
            prepStmt.setString(2,player.getUniqueId().toString());
            prepStmt.addBatch();
            PreparedStatement prepStmt2 = con.prepareStatement("UPDATE PLAYER_PASSIVE_SKILL_LEVEL SET PLAYER_MINER_EXP=? WHERE UUID=?");
            prepStmt2.setDouble(1, PassivePickaxeSkillClass.GetPlayerMinerEXP);
            prepStmt2.setString(2,player.getUniqueId().toString());
            prepStmt2.addBatch();
            PreparedStatement prepStmt3 = con.prepareStatement("UPDATE PLAYER_PASSIVE_SKILL_LEVEL SET PLAYER_NEXT_MINER_LEVEL=? WHERE UUID=?");
            prepStmt3.setDouble(1, PassivePickaxeSkillClass.NextPlayerMinerLevel);
            prepStmt3.setString(2,player.getUniqueId().toString());
            prepStmt3.addBatch();
            PreparedStatement prepStmt4 = con.prepareStatement("UPDATE PLAYER_PASSIVE_SKILL_LEVEL SET PLAYER_NEXT_MINER_EXP=? WHERE UUID=?");
            prepStmt4.setDouble(1, PassivePickaxeSkillClass.NextPlayerMinerEXP);
            prepStmt4.setString(2,player.getUniqueId().toString());
            prepStmt4.addBatch();

            int[] cnt = prepStmt.executeBatch();
            int[] cnt2 = prepStmt2.executeBatch();
            int[] cnt3 = prepStmt3.executeBatch();
            int[] cnt4 = prepStmt4.executeBatch();

            con.commit();
            prepStmt.close();
        }catch (Exception e) {
            getLogger().warning(e.toString());
        }
        getServer().getConsoleSender().sendMessage(ChatColor.DARK_PURPLE+"ばいばい！");
        MaxPlayers--;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("プラグインが停止しました");
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "プラグインを終了します");
    }
}