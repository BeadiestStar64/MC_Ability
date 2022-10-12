package mc_ability.beadieststar64.mc_ability.ActiveSkill;

import com.sun.tools.javac.jvm.Items;
import mc_ability.beadieststar64.mc_ability.MC_Ability;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.bukkit.Bukkit.*;

public class ActivePickaxeSkillClass extends ExtendedActiveSkill implements Listener {

    public static Map<Player, String> PlayerOnHand = new HashMap<>();

    public Material[] Pickaxe = {Material.WOODEN_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLDEN_PICKAXE, Material.DIAMOND_PICKAXE, Material.NETHERITE_PICKAXE};
    public Material[] Sword = {Material.WOODEN_SWORD, Material.STONE_SWORD, Material.IRON_SWORD, Material.GOLDEN_SWORD, Material.DIAMOND_SWORD, Material.NETHERITE_SWORD};
    public Material[] Axe = {Material.WOODEN_AXE, Material.STONE_AXE, Material.IRON_AXE, Material.GOLDEN_AXE, Material.DIAMOND_AXE, Material.NETHERITE_AXE};
    public Material[] Hoe = {Material.WOODEN_HOE, Material.STONE_HOE, Material.IRON_HOE, Material.GOLDEN_HOE, Material.GOLDEN_HOE, Material.DIAMOND_HOE, Material.NETHERITE_HOE};

    public ArrayList<Material> PickaxeList = new ArrayList<>(Arrays.asList(Pickaxe));
    public ArrayList<Material> SwordList = new ArrayList<>(Arrays.asList(Sword));
    public ArrayList<Material> AxeList = new ArrayList<>(Arrays.asList(Axe));
    public ArrayList<Material> HoeList = new ArrayList<>(Arrays.asList(Hoe));

    public static Map<Player, Boolean> CoolDown = new HashMap<>(); //クールダウンか判定
    public static Map<Player, Integer> MaxCoolDownTime = new HashMap<>(); //最大クールダウン時間
    public static Map<Player, Integer> CoolDownTime = new HashMap<>(); //クールダウンに必要な時間
    public static Map<Player, Boolean> CoolDownTimeDisplay = new HashMap<>(); //クールダウンの時間を表示
    public static Map<Player, Boolean> ActivationActiveSkill = new HashMap<>(); //スキル発動中か判定
    public static Map<Player, Boolean> ActiveSkillWait = new HashMap<>(); //スキル発動待機中か判定
    public static Map<Player, Integer> SkillWaitTimer = new HashMap<>(); //スキル待機時間を設定
    public static Map<Player, Integer> ActivationSkillCountDownTime = new HashMap<>(); //スキルの効果時間
    public static Map<Player, Integer> MaxActivationActiveSkillCountDownTime = new HashMap<>(); //スキルの最大効果時間
    public static Map<Player, Boolean> IsPlayerLogOut = new HashMap<>();

    public static Map<Player, ItemStack> pickaxe = new HashMap<>();
    public static Map<Player, Integer> GetEnchantmentLevel = new HashMap<>();
    public static Map<Player, Integer> AddEnchantmentLevel = new HashMap<>();
    public static Map<Player, ItemStack> BeforePickaxe = new HashMap<>();
    public static Map<Player, Integer> AfterGetEnchantmentLevel = new HashMap<>();
    public static Map<Player, Integer> AfterAddEnchantment = new HashMap<>();

    public ActivePickaxeSkillClass(MC_Ability plugin) {
        super(plugin);
    }
    public final int IS_PICKAXE_SKILL_ACTIVATE_FALSE = 1;
    public final int IS_PICKAXE_SKILL_ACTIVATE_TRUE = 2;

    public void SetUp(Player player) {
        CoolDown.put(player, false);
        CoolDownTimeDisplay.put(player, false);
        ActivationActiveSkill.put(player, false);
        ActiveSkillWait.put(player, false);
        SkillWaitTimer.put(player, 5);
        ActivationSkillCountDownTime.put(player, 20);
        CoolDownTime.put(player, 30);
    }

    @EventHandler
    public void PreparationActiveSkill(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ActivePlayerFlag Flag = new ActivePlayerFlag(player,plugin);
        if(Flag.Get()) {
            return;
        }
        Flag.Set();

        if(!CoolDown.containsKey(player) || !CoolDownTimeDisplay.containsKey(player) || !ActivationActiveSkill.containsKey(player) || !ActiveSkillWait.containsKey(player) || !SkillWaitTimer.containsKey(player) || !ActivationSkillCountDownTime.containsKey(player) || !CoolDownTime.containsKey(player)) {
            CoolDown.put(player, false);
            CoolDownTimeDisplay.put(player, false);
            ActivationActiveSkill.put(player, false);
            ActiveSkillWait.put(player, false);
            SkillWaitTimer.put(player, 5);
            ActivationSkillCountDownTime.put(player, 20);
            CoolDownTime.put(player, 30);
        }

        if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if(player.getInventory().getItemInOffHand().getType() == Material.AIR) {
                ActiveSkillMethod(player);
            }else{
                if(player.isSneaking()) {
                    ActiveSkillMethod(player);
                }
            }
        }
    }

    public void ActiveSkillMethod(Player player) {
        //クールダウンか判定
        if(CoolDown.get(player)) {
            CoolDownTimeDisplay.put(player, true);
            CoolDownMethod(player);
        }else{
            //スキル発動中か判定
            if(!ActivationActiveSkill.get(player)){
                //スキル発動待機カウント中か判定
                if(!ActiveSkillWait.get(player)) {
                    if(PickaxeList.contains(player.getInventory().getItemInMainHand().getType())) {
                        PlayerOnHand.put(player, "ツルハシ");
                        TextComponent component = new TextComponent();
                        component.setText(ChatColor.GOLD+""+ChatColor.BOLD+PlayerOnHand.get(player) +
                        ChatColor.WHITE+"を構えた");
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component);
                        player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_DIAMOND,1,1);
                        ActiveSkillWait.put(player, true);
                        ActiveSkillWaitTimer(player);
                    }
                }
            }
        }
    }

    public void ActiveSkillWaitTimer(Player player) {
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                if(SkillWaitTimer.get(player) <= 0) {
                    cancel();
                    ActiveSkillWait.put(player, false);
                    SkillWaitTimer.put(player, 5);
                    TextComponent CountDownTime = new TextComponent();
                    CountDownTime.setText(ChatColor.GOLD+""+ ChatColor.BOLD+PlayerOnHand.get(player) +
                            ChatColor.WHITE+"を下した...");
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, CountDownTime);
                    return;
                }
                if(ActivationActiveSkill.get(player) || MC_Ability.Disable){
                    cancel();
                    ActiveSkillWait.put(player, false);
                    SkillWaitTimer.put(player, 5);
                    return;
                }
                ActiveSkillWait.put(player, true);
                SkillWaitTimer.put(player, (SkillWaitTimer.get(player) - 1));
            }
        };
        runnable.runTaskTimer(plugin,0,20L);
    }

    @EventHandler
    public void ActiveSkillMethod(BlockBreakEvent event) {
        Player player = event.getPlayer();
        //クールダウンか判定
        if(!CoolDown.get(player)) {
            //スキル発動待機状態か判定
            if(ActiveSkillWait.get(player)) {
                //スキル発動中か判定
                if(!ActivationActiveSkill.get(player)) {
                    //ピッケルをメインハンドに持っているか判定
                    if(PickaxeList.contains(player.getInventory().getItemInMainHand().getType())) {
                        ActivationActiveSkill.put(player, true);
                        ActiveSkillWait.put(player, false);
                        IsPlayerLogOut.put(player, true);

                        //エンチャントを付与
                        pickaxe.put(player, player.getInventory().getItemInMainHand());
                        GetEnchantmentLevel.put(player, pickaxe.get(player).getEnchantmentLevel(Enchantment.DIG_SPEED));
                        AddEnchantmentLevel.put(player, (GetEnchantmentLevel.get(player)+5));
                        pickaxe.get(player).removeEnchantment(Enchantment.DIG_SPEED);

                        if(AddEnchantmentLevel.get(player) <= 5) {
                            pickaxe.get(player).addEnchantment(Enchantment.DIG_SPEED,AddEnchantmentLevel.get(player));
                        }else{
                            pickaxe.get(player).addUnsafeEnchantment(Enchantment.DIG_SPEED,AddEnchantmentLevel.get(player));//バニラ以上のエンチャントレベル付与
                        }
                        player.getInventory().setItemInMainHand(pickaxe.get(player));

                        TextComponent ActiveSkillComponent = new TextComponent();
                        ActiveSkillComponent.setText(ChatColor.AQUA+""+ChatColor.BOLD+"アクティブスキル"+
                                ChatColor.GOLD+""+ChatColor.BOLD+"マイナー"+
                                ChatColor.WHITE+"発動!");
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR,ActiveSkillComponent);
                        player.playSound(player.getLocation(), Sound.ITEM_TRIDENT_RIPTIDE_3, 1, 1);
                        BukkitRunnable runnable = new BukkitRunnable() {
                            @Override
                            public void run() {
                                if(ActivationSkillCountDownTime.get(player) == 0) {
                                    cancel();

                                    ActivationActiveSkill.put(player, false);
                                    CoolDown.put(player, true);
                                    ActivationSkillCountDownTime.put(player, 20);

                                    TextComponent CoolDownStartComponent = new TextComponent();
                                    CoolDownStartComponent.setText(ChatColor.GOLD+""+ChatColor.BOLD+"マイナー"+
                                            ChatColor.WHITE+"を消費した....");

                                    BeforePickaxe.put(player, player.getInventory().getItemInMainHand());
                                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR,CoolDownStartComponent);

                                    AfterGetEnchantmentLevel.put(player, BeforePickaxe.get(player).getEnchantmentLevel(Enchantment.DIG_SPEED));
                                    AfterAddEnchantment.put(player,(AfterGetEnchantmentLevel.get(player)-5));
                                    BeforePickaxe.get(player).removeEnchantment(Enchantment.DIG_SPEED);

                                    if(AfterAddEnchantment.get(player) >= 1 && AfterAddEnchantment.get(player) <= 5) {
                                        BeforePickaxe.get(player).addEnchantment(Enchantment.DIG_SPEED,AfterAddEnchantment.get(player));
                                    }else if(AfterAddEnchantment.get(player) > 5) {
                                        BeforePickaxe.get(player).addUnsafeEnchantment(Enchantment.DIG_SPEED, AfterAddEnchantment.get(player));
                                    }

                                    CoolDownMethod(player);
                                    return;
                                }

                                if(MC_Ability.Disable) {
                                    cancel();
                                }
                                ActivationSkillCountDownTime.put(player, (ActivationSkillCountDownTime.get(player) - 1));
                            }
                        };
                        runnable.runTaskTimer(plugin,0,20L);
                    }
                }
            }
        }
    }

    public void CoolDownMethod(Player player) {
        if(!CoolDownTimeDisplay.get(player)) {
            BukkitRunnable CoolDownRunnable = new BukkitRunnable() {
                @Override
                public void run() {
                    if(CoolDownTime.get(player) == 0) {
                        cancel();
                        CoolDown.put(player, false);
                        TextComponent CoolDownComponent = new TextComponent();
                        CoolDownComponent.setText(ChatColor.AQUA+"クールダウンが終了しました");
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR,CoolDownComponent);
                        player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
                        return;
                    }

                    if(MC_Ability.Disable) {
                        cancel();
                    }
                    CoolDownTime.put(player, (CoolDownTime.get(player) - 1));
                }
            };
            CoolDownRunnable.runTaskTimer(plugin,0,20L);
        }else{
            TextComponent CoolDownComponent = new TextComponent();
            CoolDownComponent.setText(ChatColor.RED+""+ChatColor.BOLD+"ただいまクールダウン中です!そのスキルを使うにはあと" +
            ChatColor.GOLD+CoolDownTime.get(player) +
            ChatColor.RED+ChatColor.BOLD+"秒お待ちください!");
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR,CoolDownComponent);
            CoolDownTimeDisplay.put(player, false);
            return;
        }
    }

    public void Repairing (Player player) {

    }
}