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

import static org.bukkit.Bukkit.getLogger;
import static org.bukkit.Bukkit.getPlayer;

public class PassivePickaxeSkillClass extends ExtendedPassiveSkill implements Listener {

    public Material[] Pickaxe = {Material.WOODEN_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLDEN_PICKAXE, Material.DIAMOND_PICKAXE, Material.NETHERITE_PICKAXE};
    public ArrayList<Material> PickaxeList = new ArrayList<>(Arrays.asList(Pickaxe));

    //static最恐！
    public static double GetPlayerMinerLevel;
    public static double GetPlayerMinerEXP;
    public static double NextPlayerMinerLevel;
    public static double NextPlayerMinerEXP;
    public static double SetPlayerMinerEXP = 0;
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

        if(PickaxeList.contains(player.getInventory().getItemInMainHand().getType())) {
            if(TerracottaList.contains(block.getType())) {
                SetPlayerMinerEXP = SetPlayerMinerEXP+2;
            }
            if(NextPlayerMinerEXP <= GetPlayerMinerEXP+SetPlayerMinerEXP) {
                GetPlayerMinerLevel++;
                GetPlayerMinerEXP = NextPlayerMinerEXP-SetPlayerMinerEXP;
                NextPlayerMinerLevel++;
                NextPlayerMinerEXP = (int) (NextPlayerMinerEXP *1.5);

                //レベルアップ通知
                player.sendMessage(ChatColor.GREEN+""+ChatColor.BOLD+"採掘レベル" +
                        ChatColor.WHITE+"が" +
                        ChatColor.GOLD+""+ChatColor.BOLD+GetPlayerMinerLevel +
                        ChatColor.WHITE+"にレベルアップしました！");
                player.playSound(player.getLocation(),Sound.ENTITY_PLAYER_LEVELUP, 1,1);
            }else{
                GetPlayerMinerEXP = GetPlayerMinerEXP+SetPlayerMinerEXP;
            }

            SetPlayerMinerEXP = 0;

            LevelBarShow(player);
        }
    }

    //改良必須
    public void LevelBarShow(Player player) {
        BossBar MinerLevel = Bukkit.createBossBar("Miner Lv:"+GetPlayerMinerLevel, BarColor.GREEN, BarStyle.SOLID);

        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (DisplayMinerBossBarTime == 0) {
                    cancel();
                    DisplayMinerBossBarTime = 3;
                    OverRideMinerLevel = false;
                    MinerLevel.removeAll();
                }
                DisplayMinerBossBarTime--;
                if (!OverRideMinerLevel) {
                    MinerLevel.setVisible(true);
                    OverRideMinerLevel = true;
                } else {
                    MinerLevel.setVisible(false);
                }
            }
        };
        runnable.runTaskTimer(plugin,0,20L);
        MinerLevel.setProgress(GetPlayerMinerEXP/NextPlayerMinerEXP);
        MinerLevel.addPlayer(player);
    }
}