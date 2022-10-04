package mc_ability.beadieststar64.mc_ability.PassiveSkill;
import mc_ability.beadieststar64.mc_ability.MC_Ability;
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

    public Material[] Pickaxe = {Material.WOODEN_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLDEN_PICKAXE, Material.DIAMOND_PICKAXE, Material.NETHERITE_PICKAXE};
    public ArrayList<Material> PickaxeList = new ArrayList<>(Arrays.asList(Pickaxe));


    public static Map<Player, Integer> GetPlayerMinerLevel = new HashMap<>();
    public static Map<Player, Double> GetPlayerMinerEXP = new HashMap<>();
    public static Map<Player, Integer> NextPlayerMinerLevel = new HashMap<>();
    public static Map<Player, Double> NextPlayerMinerEXP = new HashMap<>();
    public static Map<Player, Double> SetPlayerMinerEXP = new HashMap<>();


    public static double DisplayMinerBossBarTime = 3;
    public static boolean OverRideMinerLevel = false;

    public Material[] Terracotta = {Material.TERRACOTTA, Material.BLACK_TERRACOTTA, Material.BLUE_TERRACOTTA, Material.ORANGE_TERRACOTTA};
    public ArrayList<Material> TerracottaList = new ArrayList<>(Arrays.asList(Terracotta));

    public PassivePickaxeSkillClass(MC_Ability plugin) {
        super(plugin);
    }

    @EventHandler
    public void PassiveSkillMethod(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        SetPlayerMinerEXP.put(player, 0.0);

        if(PickaxeList.contains(player.getInventory().getItemInMainHand().getType())) {
            if(TerracottaList.contains(block.getType())) {
                SetPlayerMinerEXP.put(player, (SetPlayerMinerEXP.get(player)+2.0));
            }
            if(NextPlayerMinerEXP.get(player) <= GetPlayerMinerEXP.get(player)+SetPlayerMinerEXP.get(player)) {
                GetPlayerMinerLevel.put(player, (int) NextPlayerMinerLevel.get(player));
                GetPlayerMinerEXP.put(player, NextPlayerMinerEXP.get(player)-SetPlayerMinerEXP.get(player));
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

    //改良必須
    public void LevelBarShow(Player player) {
        BossBar MinerLevel = Bukkit.createBossBar("Miner Lv:"+(int)GetPlayerMinerLevel.get(player), BarColor.GREEN, BarStyle.SOLID);

        if(OverRideMinerLevel) {
            MinerLevel.setVisible(false);
        }else {
            MinerLevel.setVisible(true);
            OverRideMinerLevel = true;
            BukkitRunnable runnable = new BukkitRunnable() {
                @Override
                public void run() {
                    if (DisplayMinerBossBarTime <= 0) {
                        cancel();
                        DisplayMinerBossBarTime = 3;
                        OverRideMinerLevel = false;
                        MinerLevel.removePlayer(player);
                    }
                    DisplayMinerBossBarTime--;
                }
            };
            runnable.runTaskTimer(plugin,0,20L);
        }
        MinerLevel.setProgress(GetPlayerMinerEXP.get(player)/NextPlayerMinerEXP.get(player));
        MinerLevel.addPlayer(player);
    }

}