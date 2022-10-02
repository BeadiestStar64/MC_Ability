package mc_ability.beadieststar64.mc_ability.ActiveSkill;

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

import static org.bukkit.Bukkit.*;

public class ActivePickaxeSkillClass extends ExtendedActiveSkill implements Listener {
    public ActivePickaxeSkillClass(MC_Ability plugin) {
        super(plugin);
    }

    public String PlayerOnHand = "";

    public Material[] Pickaxe = {Material.WOODEN_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLDEN_PICKAXE, Material.DIAMOND_PICKAXE, Material.NETHERITE_PICKAXE};
    public Material[] Sword = {Material.WOODEN_SWORD, Material.STONE_SWORD, Material.IRON_SWORD, Material.GOLDEN_SWORD, Material.DIAMOND_SWORD, Material.NETHERITE_SWORD};
    public Material[] Axe = {Material.WOODEN_AXE, Material.STONE_AXE, Material.IRON_AXE, Material.GOLDEN_AXE, Material.DIAMOND_AXE, Material.NETHERITE_AXE};
    public Material[] Hoe = {Material.WOODEN_HOE, Material.STONE_HOE, Material.IRON_HOE, Material.GOLDEN_HOE, Material.GOLDEN_HOE, Material.DIAMOND_HOE, Material.NETHERITE_HOE};

    public ArrayList<Material> PickaxeList = new ArrayList<>(Arrays.asList(Pickaxe));
    public ArrayList<Material> SwordList = new ArrayList<>(Arrays.asList(Sword));
    public ArrayList<Material> AxeList = new ArrayList<>(Arrays.asList(Axe));
    public ArrayList<Material> HoeList = new ArrayList<>(Arrays.asList(Hoe));

    public boolean CoolDown = false; //クールダウンか判定
    public boolean CoolDownTimeDisplay = false; //クールダウンの時間を表示
    public boolean ActivationActiveSkill = false; //スキル発動中か判定
    public boolean ActiveSkillWait = false; //スキル発動待機中か判定
    public double SkillWaitTimer = 5; //スキル待機時間を設定
    public double ActivationSkillCountDownTime = 20; //スキルの効果時間
    public double CoolDownTime = 30; //クールダウンに必要な時間

    public Connection con;
    public final int IS_PICKAXE_SKILL_ACTIVATE_FALSE = 1;
    public final int IS_PICKAXE_SKILL_ACTIVATE_TRUE = 2;

    @EventHandler
    public void PreparationActiveSkill(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ActivePlayerFlag Flag = new ActivePlayerFlag(player,plugin);
        if(Flag.Get()) {
            return;
        }
        Flag.Set();
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
        if(CoolDown) {
            CoolDownTimeDisplay = true;
            CoolDownMethod(player);
        }else{
            //スキル発動中か判定
            if(!ActivationActiveSkill){
                //スキル発動待機カウント中か判定
                if(!ActiveSkillWait) {
                    if(PickaxeList.contains(player.getInventory().getItemInMainHand().getType())) {
                        PlayerOnHand = "ツルハシ";
                        TextComponent component = new TextComponent();
                        component.setText(ChatColor.GOLD+""+ChatColor.BOLD+PlayerOnHand +
                        ChatColor.WHITE+"を構えた");
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component);
                        player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_DIAMOND,1,1);
                        ActiveSkillWait = true;
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
                if(SkillWaitTimer <= 0) {
                    cancel();
                    ActiveSkillWait = false;
                    SkillWaitTimer = 5;
                    TextComponent CountDownTime = new TextComponent();
                    CountDownTime.setText(ChatColor.GOLD+""+ ChatColor.BOLD+PlayerOnHand +
                            ChatColor.WHITE+"を下した...");
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, CountDownTime);
                    return;
                }
                if(ActivationActiveSkill){
                    cancel();
                    ActiveSkillWait = false;
                    SkillWaitTimer = 5;
                    return;
                }
                ActiveSkillWait = true;
                SkillWaitTimer--;
            }
        };
        runnable.runTaskTimer(plugin,0,20L);
    }

    @EventHandler
    public void ActiveSkillMethod(BlockBreakEvent event) {
        try {
            con = DriverManager.getConnection("jdbc:sqlite:" + plugin.getDataFolder() + File.separator + "MC_ABILITY.db");
            con.setAutoCommit(false);
        }catch (Exception e) {
            getLogger().warning(e.toString());
        }
        Player player = event.getPlayer();
        //クールダウンか判定
        if(!CoolDown) {
            //スキル発動待機状態か判定
            if(ActiveSkillWait) {
                //スキル発動中か判定
                if(!ActivationActiveSkill) {
                    //ピッケルをメインハンドに持っているか判定
                    if(PickaxeList.contains(player.getInventory().getItemInMainHand().getType())) {
                        ActivationActiveSkill = true;
                        ActiveSkillWait = false;
                        //エンチャントを付与
                        ItemStack pickaxe = player.getInventory().getItemInMainHand();
                        int GetEnchantmentLevel = pickaxe.getEnchantmentLevel(Enchantment.DIG_SPEED);
                        int AddEnchantmentLevel = GetEnchantmentLevel+5;
                        pickaxe.removeEnchantment(Enchantment.DIG_SPEED);
                        if(AddEnchantmentLevel <= 5) {
                            pickaxe.addEnchantment(Enchantment.DIG_SPEED,AddEnchantmentLevel);
                        }else{
                            pickaxe.addUnsafeEnchantment(Enchantment.DIG_SPEED,AddEnchantmentLevel);//バニラ以上のエンチャントレベル付与
                        }
                        player.getInventory().setItemInMainHand(pickaxe);

                        TextComponent ActiveSkillComponent = new TextComponent();
                        ActiveSkillComponent.setText(ChatColor.AQUA+""+ChatColor.BOLD+"アクティブスキル"+
                                ChatColor.GOLD+""+ChatColor.BOLD+"マイナー"+
                                ChatColor.WHITE+"発動!");
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR,ActiveSkillComponent);
                        player.playSound(player.getLocation(), Sound.ITEM_TRIDENT_RIPTIDE_3, 1, 1);
                        BukkitRunnable runnable = new BukkitRunnable() {
                            @Override
                            public void run() {
                                if(ActivationSkillCountDownTime == 0) {
                                    cancel();

                                    ActivationActiveSkill = false;
                                    CoolDown = true;
                                    ActivationSkillCountDownTime = 20;

                                    TextComponent CoolDownStartComponent = new TextComponent();
                                    CoolDownStartComponent.setText(ChatColor.GOLD+""+ChatColor.BOLD+"マイナー"+
                                            ChatColor.WHITE+"を消費した....");

                                    ItemStack BeforePickaxe = player.getInventory().getItemInMainHand();
                                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR,CoolDownStartComponent);

                                    int AfterGetEnchantmentLevel = BeforePickaxe.getEnchantmentLevel(Enchantment.DIG_SPEED);
                                    int AfterAddEnchantment = AfterGetEnchantmentLevel-5;
                                    BeforePickaxe.removeEnchantment(Enchantment.DIG_SPEED);

                                    if(AfterAddEnchantment >= 1 && AfterAddEnchantment <= 5) {
                                        BeforePickaxe.addEnchantment(Enchantment.DIG_SPEED,AfterAddEnchantment);
                                    }else if(AfterAddEnchantment < 5) {
                                        BeforePickaxe.addUnsafeEnchantment(Enchantment.DIG_SPEED, AfterAddEnchantment);
                                    }

                                    CoolDownMethod(player);
                                    return;
                                }
                                ActivationSkillCountDownTime--;
                            }
                        };
                        runnable.runTaskTimer(plugin,0,20L);
                    }
                }
            }
        }
    }

    public void CoolDownMethod(Player player) {
        if(!CoolDownTimeDisplay) {
            BukkitRunnable CoolDownRunnable = new BukkitRunnable() {
                @Override
                public void run() {
                    if(CoolDownTime == 0) {
                        cancel();
                        CoolDown = false;
                        TextComponent CoolDownComponent = new TextComponent();
                        CoolDownComponent.setText(ChatColor.AQUA+"クールダウンが終了しました");
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR,CoolDownComponent);
                        player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
                        return;
                    }
                    CoolDownTime--;
                }
            };
            CoolDownRunnable.runTaskTimer(plugin,0,20L);
        }else{
            TextComponent CoolDownComponent = new TextComponent();
            CoolDownComponent.setText(ChatColor.RED+""+ChatColor.BOLD+"ただいまクールダウン中です!そのスキルを使うにはあと" +
            ChatColor.GOLD+CoolDownTime +
            ChatColor.RED+ChatColor.BOLD+"秒お待ちください!");
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR,CoolDownComponent);
            CoolDownTimeDisplay = false;
            return;
        }
    }
}