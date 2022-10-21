package mc_ability.beadieststar64.mc_ability.DataBase;

import mc_ability.beadieststar64.mc_ability.ActiveSkill.ActivePickaxeSkillClass;
import mc_ability.beadieststar64.mc_ability.GUI.PlayerGUI;
import mc_ability.beadieststar64.mc_ability.MC_Ability;
import mc_ability.beadieststar64.mc_ability.Original_Item.OriginalItemClass;
import mc_ability.beadieststar64.mc_ability.PassiveSkill.PassivePickaxeSkillClass;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class SQLite {

    public MC_Ability plugin;
    public static Connection con;

    public void onActivate(MC_Ability extended) {
        plugin = extended;
        try {
            // JDBCドライバーの指定
            Class.forName("org.sqlite.JDBC");
            //データベース設定
            con = DriverManager.getConnection("jdbc:sqlite:" + plugin.getDataFolder() + File.separator + "MC_ABILITY.db");
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

                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS MAGIC_SKILL(" +
                        "MCID" +
                        ", UUID" +
                        ", IS_STAMINA_CUT_LV_ONE" +
                        ", IS_STAMINA_CUT_LV_TWO" +
                        ");");
                con.commit();
                stmt.close();

            }catch (Exception e) {
                MC_Ability.ErrorLog(e);
            }
            plugin.getServer().getConsoleSender().sendMessage("テーブル処理が完了しました");

            //イベント登録
            plugin.RegisterEvents();

            //コマンド登録
            plugin.RegisterCommand();

        }catch (Exception e) {
            MC_Ability.ErrorLog(e);
        }
    }

    public void ConnectionDataBase(Player player) {

        plugin.getLogger().info(player + "さんがログインしました。");

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
                if (!PlayerName.equals(player.getName())) {
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
                plugin.getServer().getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + "新規プレイヤー情報が見つかりませんでした。データを追加します。");

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
                        ", IS_STAMINA_CUT_LV_ONE" +
                        ", IS_STAMINA_CUT_LV_TWO" +
                        ") VALUES (" +
                        "?" +
                        ", ?" +
                        ", 0" +
                        ", 0" +
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
                plugin.getLogger().info("データを追加しました");
            } else {
                plugin.getServer().getConsoleSender().sendMessage(ChatColor.WHITE + "[MC_Ability SQLite]" + ChatColor.AQUA + "データが見つかりました。データ追加は回避されます。");
            }

            //UUIDを参照してMCIDに変更がないか確認
            if (!UpdatePlayer.isEmpty()) {
                PreparedStatement prepStmt2 = con.prepareStatement("");
                PreparedStatement prepStmt3 = con.prepareStatement("");
                PreparedStatement prepStmt4 = con.prepareStatement("");

                prepStmt = con.prepareStatement(UpdatePlayer);
                prepStmt.setString(1, player.getName());
                prepStmt.setString(2, player.getUniqueId().toString());
                prepStmt.addBatch();

                prepStmt2 = con.prepareStatement(UpdateActiveSkill);
                prepStmt2.setString(1, player.getName());
                prepStmt2.setString(2, player.getUniqueId().toString());
                prepStmt2.addBatch();

                prepStmt3 = con.prepareStatement(UpdatePassiveSkill);
                prepStmt3.setString(1, player.getName());
                prepStmt3.setString(2, player.getUniqueId().toString());
                prepStmt3.addBatch();

                prepStmt4 = con.prepareStatement(UpdateGUI);
                prepStmt4.setString(1, player.getName());
                prepStmt4.setString(2, player.getUniqueId().toString());
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

            plugin.getLogger().info("test:" + MC_Ability.IsPlayerLogOut.get(player));

        }catch (Exception e) {
            MC_Ability.ErrorLog(e);
        }
    }

    public void InsertPassivePickaxeSkillQuery (Player player) {
        plugin.getLogger().info(player.getName() + "さんのPassive Skill情報を参照します。");
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
            MC_Ability.ErrorLog(e);
        }
    }

    public void InsertActivePickaxeSkillQuery (Player player) {
        plugin.getLogger().info(player.getName() + "さんのActive Skill情報を参照します。");
        String SQL = ("SELECT IS_MINER_ACTIVATE,REMAINING_MINER_TIME,MAX_MINER_TIME,MINER_ENCHANTMENT_LEVEL,IS_MINER_COOL_DOWN,REMAINING_COOL_DOWN_TIME,MAX_COOL_DOWN_TIME,IS_PLAYER_LOG_OUT FROM ACTIVE_SKILL WHERE UUID=?");

        try {
            PreparedStatement prepStmt = con.prepareStatement(SQL);
            prepStmt.setString(1, player.getUniqueId().toString());
            ResultSet RS = prepStmt.executeQuery();
            con.commit();
            if (RS.next()) {
                if(RS.getInt(1) == 0) {
                    ActivePickaxeSkillClass.ActivationActiveSkill.put(player, false);
                }else if(RS.getInt(1) == 1) {
                    ActivePickaxeSkillClass.ActivationActiveSkill.put(player, true);
                }
                ActivePickaxeSkillClass.ActivationSkillCountDownTime.put(player, RS.getInt(2));
                ActivePickaxeSkillClass.MaxActivationActiveSkillCountDownTime.put(player, RS.getInt(3));
                ActivePickaxeSkillClass.AddEnchantmentLevel.put(player, RS.getInt(4));
                if(RS.getInt(5) == 0) {
                    ActivePickaxeSkillClass.CoolDown.put(player, false);
                }else if(RS.getInt(5) == 1) {
                    ActivePickaxeSkillClass.CoolDown.put(player, true);
                }
                ActivePickaxeSkillClass.CoolDownTime.put(player, RS.getInt(6));
                ActivePickaxeSkillClass.MaxCoolDownTime.put(player, RS.getInt(7));
                if(RS.getInt(8) == 0) {
                    MC_Ability.IsPlayerLogOut.put(player, false);
                }else if(RS.getInt(8) == 1) {
                    MC_Ability.IsPlayerLogOut.put(player, true);
                }
            }else{
                plugin.getServer().getConsoleSender().sendMessage(ChatColor.WHITE + "[MC_Ability]" + ChatColor.RED +"エラーが発生しました!!!");
                plugin.getServer().getConsoleSender().sendMessage(ChatColor.WHITE + "原因:" + ChatColor.RED + player.getName());
            }
            RS.close();
            prepStmt.close();
            plugin.getLogger().info(player.getName() + "さんのPassive Skill情報を挿入しました。");
        }catch (Exception e) {
            MC_Ability.ErrorLog(e);
        }
    }

    public void UpdataPassivePickaxeSkill(Player player) {
        plugin.getLogger().info(player + "のPassive Skill情報を更新します。");
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

            plugin.getLogger().info(player.getName() + "さんのPassive Skill情報を更新しました。");
        }catch (Exception e) {
            MC_Ability.ErrorLog(e);
        }
    }

    public void UpdataActivePickaxeSkill(Player player) {
        plugin.getLogger().info(player + "さんのActive Skill情報を更新します。");
        try {
            PreparedStatement prepStmt = con.prepareStatement("UPDATE ACTIVE_SKILL SET IS_MINER_ACTIVATE=? WHERE UUID=?" );
            Map<Player, Integer> IsMinerActivateBoolean = new HashMap<>();
            IsMinerActivateBoolean.put(player, 1);
            if(!ActivePickaxeSkillClass.ActivationActiveSkill.get(player)) {
                IsMinerActivateBoolean.put(player, 0);
            }
            prepStmt.setInt(1,IsMinerActivateBoolean.get(player));
            prepStmt.setString(2,player.getUniqueId().toString());
            prepStmt.addBatch();

            PreparedStatement prepStmt2 = con.prepareStatement("UPDATE ACTIVE_SKILL SET REMAINING_MINER_TIME=? WHERE UUID=?");
            prepStmt2.setInt(1, ActivePickaxeSkillClass.ActivationSkillCountDownTime.get(player));
            prepStmt2.setString(2,player.getUniqueId().toString());
            prepStmt2.addBatch();

            PreparedStatement prepStmt3 = con.prepareStatement("UPDATE ACTIVE_SKILL SET MAX_MINER_TIME=? WHERE UUID=?");
            prepStmt3.setInt(1, ActivePickaxeSkillClass.MaxActivationActiveSkillCountDownTime.get(player));
            prepStmt3.setString(2,player.getUniqueId().toString());
            prepStmt3.addBatch();

            PreparedStatement prepStmt4 = con.prepareStatement("UPDATE ACTIVE_SKILL SET MINER_ENCHANTMENT_LEVEL=? WHERE UUID=?");
            prepStmt4.setInt(1, ActivePickaxeSkillClass.AddEnchantmentLevel.get(player));
            prepStmt4.setString(2,player.getUniqueId().toString());
            prepStmt4.addBatch();

            PreparedStatement prepStmt5 = con.prepareStatement("UPDATE ACTIVE_SKILL SET IS_MINER_COOL_DOWN=? WHERE UUID=?");
            Map<Player, Integer> IsMinerCoolDownBoolean = new HashMap<>();
            IsMinerCoolDownBoolean.put(player, 1);
            if(!ActivePickaxeSkillClass.CoolDown.get(player)) {
                IsMinerCoolDownBoolean.put(player, 0);
            }
            prepStmt5.setInt(1, IsMinerCoolDownBoolean.get(player));
            prepStmt5.setString(2, player.getUniqueId().toString());
            prepStmt5.addBatch();

            PreparedStatement prepStmt6 = con.prepareStatement("UPDATE ACTIVE_SKILL SET REMAINING_COOL_DOWN_TIME=? WHERE UUID=?");
            prepStmt6.setInt(1, ActivePickaxeSkillClass.CoolDownTime.get(player));
            prepStmt6.setString(2,player.getUniqueId().toString());
            prepStmt6.addBatch();

            PreparedStatement prepStmt7 = con.prepareStatement("UPDATE ACTIVE_SKILL SET MAX_COOL_DOWN_TIME=? WHERE UUID=?");
            prepStmt7.setInt(1, ActivePickaxeSkillClass.MaxCoolDownTime.get(player));
            prepStmt7.setString(2,player.getUniqueId().toString());
            prepStmt7.addBatch();

            PreparedStatement prepStmt8 = con.prepareStatement("UPDATE ACTIVE_SKILL SET IS_PLAYER_LOG_OUT=? WHERE UUID=?");
            Map<Player, Integer> IsPlayerLogOut = new HashMap<>();
            IsPlayerLogOut.put(player, 1);
            if(!ActivePickaxeSkillClass.IsPlayerLogOut.get(player)) {
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

            plugin.getLogger().info(player.getName() + "さんのActive Skill情報を更新しました。");
        }catch (Exception e) {
            MC_Ability.ErrorLog(e);
        }
    }

    public void onDisable() {
        try {
            con.close();
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.WHITE + "[MC_Ability]" +
                    ChatColor.GOLD + "SQLiteから切断しました。");
        }catch (Exception e) {
            MC_Ability.ErrorLog(e);
        }
    }
}