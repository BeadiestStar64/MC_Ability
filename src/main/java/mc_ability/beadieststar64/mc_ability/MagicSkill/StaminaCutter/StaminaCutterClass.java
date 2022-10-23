package mc_ability.beadieststar64.mc_ability.MagicSkill.StaminaCutter;

import mc_ability.beadieststar64.mc_ability.DataBase.SQLite;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class StaminaCutterClass {

    public static Map<Player, Integer> StaminaCutterLv1_MaxStamina = new HashMap<>();
    public static Map<Player, Integer> StaminaCutterLv1_NowStamina = new HashMap<>();
    public static Map<Player, Boolean> StaminaCutterLv1_Activate = new HashMap<>();

    public static Map<Player, Integer> StaminaCutterLv2_MaxStamina = new HashMap<>();
    public static Map<Player, Integer> StaminaCutterLv2_NowStamina = new HashMap<>();
    public static Map<Player, Boolean> StaminaCutterLv2_Activate = new HashMap<>();

    public static Map<Player, Integer> StaminaCutterLv3_MaxStamina = new HashMap<>();
    public static Map<Player, Integer> StaminaCutterLv3_NowStamina = new HashMap<>();
    public static Map<Player, Boolean> StaminaCutterLv3_Activate = new HashMap<>();

    public static Map<Player, Integer> StaminaCutterLv4_MaxStamina = new HashMap<>();
    public static Map<Player, Integer> StaminaCutterLv4_NowStamina = new HashMap<>();
    public static Map<Player, Boolean> StaminaCutterLv4_Activate = new HashMap<>();

    public static Map<Player, Integer> StaminaCutterLv5_MaxStamina = new HashMap<>();
    public static Map<Player, Integer> StaminaCutterLv5_NowStamina = new HashMap<>();
    public static Map<Player, Boolean> StaminaCutterLv5_Activate = new HashMap<>();

    public void SetValues(Player player) {
        if(!StaminaCutterLv1_Activate.containsKey(player)) {
            SQLite sqlite = new SQLite();
            sqlite.GetPlayersMagicSkillData(player);
        }
    }

}