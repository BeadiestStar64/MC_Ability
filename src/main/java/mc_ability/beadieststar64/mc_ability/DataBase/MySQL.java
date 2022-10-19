package mc_ability.beadieststar64.mc_ability.DataBase;

import mc_ability.beadieststar64.mc_ability.MC_Ability;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.DriverManager;

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

        }catch (Exception e) {
            MC_Ability.ErrorLog(e);
        }
    }

    public void onDisable() {
        try {
            con.close();
        }catch (Exception e) {
            MC_Ability.ErrorLog(e);
        }
    }
}
