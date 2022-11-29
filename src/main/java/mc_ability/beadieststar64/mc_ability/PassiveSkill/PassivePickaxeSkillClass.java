package mc_ability.beadieststar64.mc_ability.PassiveSkill;
import com.sun.org.apache.xpath.internal.operations.Bool;
import jdk.jfr.Enabled;
import mc_ability.beadieststar64.mc_ability.DataBase.SQLite;
import mc_ability.beadieststar64.mc_ability.MC_Ability;
import mc_ability.beadieststar64.mc_ability.MagicSkill.StaminaCutter.BlockBreakClass;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Boss;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.checkerframework.checker.units.qual.A;

import java.io.File;
import java.net.ConnectException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.bukkit.Bukkit.getLogger;
import static org.bukkit.Bukkit.getPlayer;

public class PassivePickaxeSkillClass extends ExtendedPassiveSkill implements Listener {

    public static SQLite sqLite;

    public Material[] Pickaxe = {Material.WOODEN_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLDEN_PICKAXE, Material.DIAMOND_PICKAXE, Material.NETHERITE_PICKAXE};
    public ArrayList<Material> PickaxeList = new ArrayList<>(Arrays.asList(Pickaxe));


    public static Map<Player, Integer> GetPlayerMinerLevel = new HashMap<>();
    public static Map<Player, Double> GetPlayerMinerEXP = new HashMap<>();
    public static Map<Player, Integer> NextPlayerMinerLevel = new HashMap<>();
    public static Map<Player, Double> NextPlayerMinerEXP = new HashMap<>();
    public static Map<Player, Double> SetPlayerMinerEXP = new HashMap<>();


    public static Map<Player, Double> DisplayMinerBossBarTime = new HashMap<>();
    public static Map<Player, Boolean> OverRideMinerLevel = new HashMap<>();
    public static Map<Player, Boolean> bool = new HashMap<>();
    public static Map<Player, Boolean> finalBool = new HashMap<>();

    public Material[] Terracotta = {Material.TERRACOTTA, Material.BLACK_TERRACOTTA, Material.BLUE_TERRACOTTA, Material.ORANGE_TERRACOTTA};
    public ArrayList<Material> TerracottaList = new ArrayList<>(Arrays.asList(Terracotta));

    public static Map<Player, BossBar> MinerLevelBossBar = new HashMap<>();
    BossBar MinerLevel = Bukkit.createBossBar(ChatColor.GREEN+""+ChatColor.BOLD+"採掘", BarColor.GREEN, BarStyle.SOLID);

    public void SetMethod(Player player) {
        DisplayMinerBossBarTime.put(player, 5.0);
        OverRideMinerLevel.put(player, false);
        bool.put(player, false);
    }

    public PassivePickaxeSkillClass(MC_Ability plugin) {
        super(plugin);
    }

    @EventHandler
    public void PassiveSkillMethod(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        SetPlayerMinerEXP.put(player, 0.0);

        BlockBreakClass breakclass = new BlockBreakClass();

        if(!NextPlayerMinerLevel.containsKey(player) || !OverRideMinerLevel.containsKey(player) || !bool.containsKey(player)) {
            SetMethod(player);
            sqLite.InsertPassivePickaxeSkillQuery(player);
        }

        if(PickaxeList.contains(player.getInventory().getItemInMainHand().getType())) {
            if(TerracottaList.contains(block.getType())) {
                SetPlayerMinerEXP.put(player, (SetPlayerMinerEXP.get(player)+2.0));
            }
            if(NextPlayerMinerEXP.get(player) <= GetPlayerMinerEXP.get(player)+SetPlayerMinerEXP.get(player)) {
                GetPlayerMinerLevel.put(player, (int) NextPlayerMinerLevel.get(player));
                GetPlayerMinerEXP.put(player, (NextPlayerMinerEXP.get(player)-GetPlayerMinerEXP.get(player)));
                NextPlayerMinerLevel.put(player, GetPlayerMinerLevel.get(player)+1);
                NextPlayerMinerEXP.put(player, (NextPlayerMinerEXP.get(player) *2.0));

                //レベルアップ通知
                player.sendMessage(ChatColor.GREEN+""+ChatColor.BOLD+"採掘レベル" +
                        ChatColor.WHITE+"が" +
                        ChatColor.GOLD+""+ChatColor.BOLD+GetPlayerMinerLevel.get(player) +
                        ChatColor.WHITE+"にレベルアップしました！");
                player.playSound(player.getLocation(),Sound.ENTITY_PLAYER_LEVELUP, 1,1);
            }else{
                GetPlayerMinerEXP.put(player, (GetPlayerMinerEXP.get(player) + SetPlayerMinerEXP.get(player)));
            }

            SetPlayerMinerEXP.put(player, 0.0);

            LevelBarShow(player);
        }
    }

    public void LevelBarShow (Player player) {
        if(MinerLevelBossBar.containsKey(player)) {
            MinerLevel.setProgress(GetPlayerMinerEXP.get(player)/NextPlayerMinerEXP.get(player));
            MinerLevel.setTitle(ChatColor.WHITE+""+ChatColor.BOLD+"採掘 Lv."+ChatColor.GOLD+GetPlayerMinerLevel.get(player));
            MinerLevelBossBar.put(player, MinerLevel);
            MinerLevel.addPlayer(player);
        }else{
            MinerLevel.setProgress(GetPlayerMinerEXP.get(player)/NextPlayerMinerEXP.get(player));
            MinerLevel.setTitle(ChatColor.WHITE+""+ChatColor.BOLD+"採掘 Lv."+ChatColor.GOLD+GetPlayerMinerLevel.get(player));
            MinerLevelBossBar.put(player, MinerLevel);
            MinerLevel.addPlayer(player);
        }

        if(OverRideMinerLevel.get(player)) {
            bool.put(player, true);
        }else if(!OverRideMinerLevel.get(player)){
            bool.put(player, false);
            DisplayMinerBossBarTime.put(player, 5.0);
        }
        CountDown(player);
    }

    public void CountDown(Player player) {

        finalBool.put(player, bool.get(player));

        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                //ボスバーが未表示の時
                OverRideMinerLevel.put(player, true);

                if(DisplayMinerBossBarTime.get(player) <= 0.0) {
                    cancel();
                    DisplayMinerBossBarTime.put(player, 5.0);
                    MinerLevel.removePlayer(player);
                    MinerLevelBossBar.put(player, MinerLevel);
                    OverRideMinerLevel.put(player, false);
                    return;
                }

                DisplayMinerBossBarTime.put(player, (DisplayMinerBossBarTime.get(player) - 1.0));

                if(finalBool.get(player)) {
                    cancel();
                    OverRideMinerLevel.put(player, false);
                    bool.put(player, false);
                    DisplayMinerBossBarTime.put(player, 5.0);
                    return;
                }
            }
        };
        runnable.runTaskTimer(plugin, 0, 20L);
    }

    public void DeleteHashMap (Player player) {
        GetPlayerMinerLevel.remove(player);
        GetPlayerMinerEXP.remove(player);
        NextPlayerMinerLevel.remove(player);
        NextPlayerMinerEXP.remove(player);
        SetPlayerMinerEXP.remove(player);
        DisplayMinerBossBarTime.remove(player);
        OverRideMinerLevel.remove(player);
        DisplayMinerBossBarTime.remove(player);
        OverRideMinerLevel.remove(player);
        finalBool.remove(player);

    }
}