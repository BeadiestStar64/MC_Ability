package mc_ability.beadieststar64.mc_ability.Utility.DataBase;

import mc_ability.beadieststar64.mc_ability.MC_Ability;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class MySQL {

    public static Connection con;
    public static MC_Ability plugin;

    //MySQlのDB接続時のデータ
    public static String User = "";
    public static String PassWord = "";
    public static String Host = "";
    public static String DataBaseName = "";
    public static int Port;

    public void onActivate(MC_Ability extended) {
        plugin = extended;

        final String URL
                = "jdbc:mysql://" + Host +":" + Port +"/" + DataBaseName;
        final String USER = User;
        final String PASS = PassWord;

        Bukkit.getLogger().info("接続先:" + URL);

        try {

            con = DriverManager.getConnection(URL, USER, PASS);
            con.setAutoCommit(false);

            //Statementオブジェクト作成
            Statement stmt = con.createStatement();
            stmt.setQueryTimeout(30);    // タイムアウト設定

            //テーブル作成
            stmt.executeUpdate("create table if not exists player(" +
                    "mcid text" +
                    ", uuid text" +
                    ");");

        }catch (Exception e) {
            MC_Ability.ErrorLog(e);
        }
    }

    public void ConnectionDataBase(Player player) {

    }

    public void onDisable() {
        try {
            con.close();
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.WHITE + "[MC_Ability]" +
                    ChatColor.GOLD + "MySQLから切断しました。");
        }catch (Exception e) {
            MC_Ability.ErrorLog(e);
        }
    }
}
