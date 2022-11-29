package mc_ability.beadieststar64.mc_ability.GUI;

import mc_ability.beadieststar64.mc_ability.MC_Ability;
import mc_ability.beadieststar64.mc_ability.MagicSkill.StaminaCutter.StaminaCutterClass;
import mc_ability.beadieststar64.mc_ability.Original_Item.original_item.Stamina_Cutter.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class PlayerGUI extends ExtenderForGUI implements Listener {

    public StaminaCutterClass cutterClass = new StaminaCutterClass();

    public static Map<Player, Inventory> inv = new HashMap<>();
    public static Map<Player, Inventory> inv2 = new HashMap<>();
    public static Map<Player, Inventory> inv3 = new HashMap<>();

    public static Map<Player, Integer> menus = new HashMap<Player, Integer>();

    public ItemStack StaminaCutterLv1 = new ItemStack(Material.ANVIL);
    public ItemStack StaminaCutterLv2 = new ItemStack(Material.ANVIL);
    public ItemStack StaminaCutterLv3 = new ItemStack(Material.ANVIL);
    public ItemStack StaminaCutterLv4 = new ItemStack(Material.ANVIL);
    public ItemStack StaminaCutterLv5 = new ItemStack(Material.ANVIL);


    public PlayerGUI(MC_Ability plugin) {
        super(plugin);
    }

    public void CreateGUI() {
        Bukkit.getLogger().info("[MC_Ability] GUIの作成に成功しました。");
    }

    public void SetGUI(Player player) {

        inv.put(player, Bukkit.createInventory(player, 18, ChatColor.DARK_GREEN + "" + ChatColor.BOLD +"魔法スキル一覧"));

        StaminaCut(player);
        MagnetChest(player);
        CriticalBoost(player);

        //Cancel Item
        ItemStack item = new ItemStack(Material.BARRIER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Close Menu");
        item.setItemMeta(meta);
        inv.get(player).setItem(8, item);

    }

    @EventHandler
    public void onOpenGUI(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        PlayerFlagForGUI flag = new PlayerFlagForGUI(player, plugin);
        if(flag.Get()) {
            return;
        }
        flag.Set();

        Action[] action = {Action.RIGHT_CLICK_BLOCK, Action.RIGHT_CLICK_AIR};
        ArrayList<Action> List = new ArrayList<>(Arrays.asList(action));

        if (List.contains(event.getAction())) {// Checking if they right clicked
            if (player.getInventory().getItemInMainHand().getType().equals(Material.BOOK)) {
                if (player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Ability Setting Book")) {//最大の問題児 こいつのせいでここから先に進めない(´；ω；`)ｳｩｩ
                    player.openInventory(inv.get(player));
                }
            }
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        PlayerFlagForGUI flag = new PlayerFlagForGUI(player, plugin);
        if(flag.Get()) {
            return;
        }
        flag.Set();

        if (!event.getInventory().equals(inv3.get(player))) {
            if (!event.getInventory().equals(inv2.get(player))) {
                if (!event.getInventory().equals(inv.get(player))) {
                    return;
                }
            }
        }
        if (event.getCurrentItem() == null) {
            return;
        }
        if (event.getCurrentItem().getItemMeta() == null) {
            return;
        }
        if (event.getCurrentItem().getItemMeta().getDisplayName() == null) {
            return;
        }

        event.setCancelled(true);

        if (event.getClickedInventory() == inv.get(player)) {
            switch (event.getSlot()) {
                case 0:
                    menus.put(player, 1);
                    SecondGUI(player);
                    player.openInventory(inv2.get(player));
                    break;

                case 1:
                    menus.put(player, 2);
                    SecondGUI(player);
                    player.openInventory(inv3.get(player));

                case 2:

                case 3:

                case 4:

                case 5:

                case 6:

                case 7:

                case 8:
                    player.closeInventory();
            }
        } else if (event.getClickedInventory() == inv2.get(player)) {

            switch (menus.get(player)) {
                case 1:
                    SutaminaCutter(event,player);
                    break;
            }
        }
    }

    /*
    まずはインベントリを作り、持っているアイテムに応じてスキル発動。

    Memo: player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1, 1);
     */

    public void SecondGUI(Player player) {

        switch (menus.get(player)){
            case 1:
                //スタミナカッター
                inv2.put(player, Bukkit.createInventory(player, 18, ChatColor.DARK_GREEN + "" +ChatColor.BOLD + "スタミナカッター"));

                inv2.get(player).setItem(0, Stamina_Cutter_Lv1.StaminaAnvilSkillLv1);
                inv2.get(player).setItem(2, Stamina_Cutter_Lv2.StaminaAnvilSkillLv2);
                inv2.get(player).setItem(4, Stamina_Cutter_Lv3.StaminaAnvilSkillLv3);
                inv2.get(player).setItem(6, Stamina_Cutter_Lv4.StaminaAnvilSkillLv4);
                inv2.get(player).setItem(8, Stamina_Cutter_Lv5.StaminaAnvilSkillLv5);
                ShowDurability(player);

                for(int i = 0; i < inv2.get(player).getSize(); i++) {
                    if(inv2.get(player).getItem(i) == null) {
                        ItemStack item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
                        inv2.get(player).setItem(i, item);
                    }
                }
                break;

            case 2:

        }
    }

    public void SutaminaCutter(InventoryClickEvent event,Player player) {
        if (event.getSlot() == 0) {

            Map<Player, Boolean> IncludeInventory = new HashMap<Player, Boolean>();

            IncludeInventory.put(player, false);

            for (int i = 0; i < 36; i++) {
                if (player.getInventory().getItem(i) == null) {
                    continue;
                }

                ItemStack item = Stamina_Cutter_Lv1.StaminaAnvilSkillLv1;

                if (player.getInventory().getItem(i).getI18NDisplayName().equals(item.getI18NDisplayName())) {
                    player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "スタミナカッター Lv.1" +
                            ChatColor.WHITE + "発動!!");

                    item.setAmount(1);
                    player.getInventory().removeItem(item);
                    cutterClass.StaminaCutterLv1_Activate.put(player, true);

                    IncludeInventory.put(player, true);
                    break;
                }
            }
            if (!IncludeInventory.get(player)) {
                player.sendMessage(ChatColor.RED + "インベントリに「スタミナカッター Lv.1」を持つ必要があります!!");
            }

            inv2.get(player).close();

        } else if (event.getSlot() == 2) {

            Map<Player, Boolean> IncludeInventory = new HashMap<Player, Boolean>();

            IncludeInventory.put(player, false);

            for (int i = 0; i < 36; i++) {
                if (player.getInventory().getItem(i) == null) {
                    continue;
                }

                ItemStack item = Stamina_Cutter_Lv2.StaminaAnvilSkillLv2;

                if (player.getInventory().getItem(i).getI18NDisplayName().equals(item.getI18NDisplayName())) {

                    player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "スタミナカッター Lv.2" +
                            ChatColor.WHITE + "発動!!");

                    item.setAmount(1);
                    player.getInventory().removeItem(item);

                    cutterClass.StaminaCutterLv2_Activate.put(player, true);
                    IncludeInventory.put(player, true);
                    break;
                }
            }
            if (!IncludeInventory.get(player)) {
                player.sendMessage(ChatColor.RED + "インベントリに「スタミナカッター Lv.2」を持つ必要があります!!");
            }

            inv2.get(player).close();

        } else if (event.getSlot() == 4) {

            Map<Player, Boolean> IncludeInventory = new HashMap<Player, Boolean>();

            IncludeInventory.put(player, false);

            for (int i = 0; i < 36; i++) {
                if (player.getInventory().getItem(i) == null) {
                    continue;
                }

                ItemStack item = Stamina_Cutter_Lv3.StaminaAnvilSkillLv3;

                if (player.getInventory().getItem(i).getI18NDisplayName().equals(item.getI18NDisplayName())) {

                    player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "スタミナカッター Lv.3" +
                            ChatColor.WHITE + "発動!!");

                    item.setAmount(1);
                    player.getInventory().removeItem(item);

                    cutterClass.StaminaCutterLv3_Activate.put(player, true);
                    IncludeInventory.put(player, true);
                    break;
                }
            }
            if (!IncludeInventory.get(player)) {
                player.sendMessage(ChatColor.RED + "インベントリに「スタミナカッター Lv.3」を持つ必要があります!!");
            }

            inv2.get(player).close();

        } else if (event.getSlot() == 6) {

            Map<Player, Boolean> IncludeInventory = new HashMap<Player, Boolean>();

            IncludeInventory.put(player, false);

            for (int i = 0; i < 36; i++) {
                if (player.getInventory().getItem(i) == null) {
                    continue;
                }

                ItemStack item = Stamina_Cutter_Lv4.StaminaAnvilSkillLv4;

                if (player.getInventory().getItem(i).getI18NDisplayName().equals(item.getI18NDisplayName())) {

                    player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "スタミナカッター Lv.4" +
                            ChatColor.WHITE + "発動!!");

                    item.setAmount(1);
                    player.getInventory().removeItem(item);

                    cutterClass.StaminaCutterLv4_Activate.put(player, true);
                    IncludeInventory.put(player, true);
                    break;
                }
            }
            if (!IncludeInventory.get(player)) {
                player.sendMessage(ChatColor.RED + "インベントリに「スタミナカッター Lv.4」を持つ必要があります!!");
            }

            inv2.get(player).close();

        } else if (event.getSlot() == 8) {

            Map<Player, Boolean> IncludeInventory = new HashMap<Player, Boolean>();

            IncludeInventory.put(player, false);

            for (int i = 0; i < 36; i++) {
                if (player.getInventory().getItem(i) == null) {
                    continue;
                }

                ItemStack item = Stamina_Cutter_Lv5.StaminaAnvilSkillLv5;

                if (player.getInventory().getItem(i).getI18NDisplayName().equals(item.getI18NDisplayName())) {
                    player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "スタミナカッター Lv.5" +
                            ChatColor.WHITE + "発動!!");

                    item.setAmount(1);
                    player.getInventory().removeItem(item);

                    cutterClass.StaminaCutterLv5_Activate.put(player, true);
                    IncludeInventory.put(player, true);
                    break;
                }
            }
            if (!IncludeInventory.get(player)) {
                player.sendMessage(ChatColor.RED + "インベントリに「スタミナカッター Lv.5」を持つ必要があります!!");
            }

            inv2.get(player).close();

        }
    }

    public void StaminaCut(Player player) {
        ItemStack item = new ItemStack(Material.ANVIL);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.GRAY + "" + ChatColor.BOLD + "スタミナカッター");

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.AQUA + "" + ChatColor.BOLD + "スタミナカッターの設定をします");
        lore.add("");
        lore.add(ChatColor.GOLD + "Create by " + ChatColor.AQUA + "" + ChatColor.BOLD + "MC_ABility");

        meta.setLore(lore);

        item.setItemMeta(meta);

        item.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, 5);
        item.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        inv.get(player).setItem(0, item);
    }

    public void MagnetChest(Player player) {
        ItemStack item = new ItemStack(Material.CHEST);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.GOLD + "マグネットチェスト");

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.AQUA + "" + ChatColor.BOLD + "マグネットチェストの設定をします");
        lore.add("");
        lore.add(ChatColor.GOLD + "Create by " + ChatColor.AQUA + "" + ChatColor.BOLD + "MC_ABility");

        meta.setLore(lore);
        item.setItemMeta(meta);

        item.addUnsafeEnchantment(Enchantment.MENDING, 1);
        item.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        inv.get(player).setItem(1, item);
    }

    public void ShowDurability(Player player) {

        StaminaCutterClass cutterClass = new StaminaCutterClass();
        cutterClass.SetValues(player);

        ItemStack item1 = new ItemStack(Material.EMERALD_BLOCK);
        ItemStack item2 = new ItemStack(Material.EMERALD_BLOCK);
        ItemStack item3 = new ItemStack(Material.EMERALD_BLOCK);
        ItemStack item4 = new ItemStack(Material.EMERALD_BLOCK);
        ItemStack item5 = new ItemStack(Material.EMERALD_BLOCK);

        ItemMeta meta1 = item1.getItemMeta();
        ItemMeta meta2 = item2.getItemMeta();
        ItemMeta meta3 = item3.getItemMeta();
        ItemMeta meta4 = item4.getItemMeta();
        ItemMeta meta5 = item5.getItemMeta();

        String Display = "";
        Map<Player, Integer> NowDurability = new HashMap<>();
        Map<Player, Integer> MaxDurability = new HashMap<>();

        int x = 0;

        for(int i = 0; i <=5; i++) {
            switch (i) {
                case 1:

                    x = ((StaminaCutterClass.StaminaCutterLv1_NowStamina.get(player))/(StaminaCutterClass.StaminaCutterLv1_MaxStamina.get(player)));
                    NowDurability.put(player, StaminaCutterClass.StaminaCutterLv1_NowStamina.get(player));
                    MaxDurability.put(player, StaminaCutterClass.StaminaCutterLv1_MaxStamina.get(player));
                    break;

                case 2:

                    x = ((StaminaCutterClass.StaminaCutterLv2_NowStamina.get(player))/(StaminaCutterClass.StaminaCutterLv2_MaxStamina.get(player)));
                    NowDurability.put(player, StaminaCutterClass.StaminaCutterLv2_NowStamina.get(player));
                    MaxDurability.put(player, StaminaCutterClass.StaminaCutterLv2_MaxStamina.get(player));
                    break;

                case 3:

                    x = ((StaminaCutterClass.StaminaCutterLv3_NowStamina.get(player))/(StaminaCutterClass.StaminaCutterLv3_MaxStamina.get(player)));
                    NowDurability.put(player, StaminaCutterClass.StaminaCutterLv3_NowStamina.get(player));
                    MaxDurability.put(player, StaminaCutterClass.StaminaCutterLv3_MaxStamina.get(player));
                    break;

                case 4:

                    x = ((StaminaCutterClass.StaminaCutterLv4_NowStamina.get(player))/(StaminaCutterClass.StaminaCutterLv4_MaxStamina.get(player)));
                    NowDurability.put(player, StaminaCutterClass.StaminaCutterLv4_NowStamina.get(player));
                    MaxDurability.put(player, StaminaCutterClass.StaminaCutterLv4_MaxStamina.get(player));
                    break;

                case 5:

                    x = ((StaminaCutterClass.StaminaCutterLv5_NowStamina.get(player))/(StaminaCutterClass.StaminaCutterLv5_MaxStamina.get(player)));
                    NowDurability.put(player, StaminaCutterClass.StaminaCutterLv5_NowStamina.get(player));
                    MaxDurability.put(player, StaminaCutterClass.StaminaCutterLv5_MaxStamina.get(player));
                    break;
            }

            if(x >= 0.8) {
                Display = ChatColor.WHITE + "現在の耐久値: " + ChatColor.AQUA + NowDurability.get(player) +
                        ChatColor.WHITE + "" + ChatColor.BOLD + " / " +
                        ChatColor.WHITE + "最大耐久値: " + ChatColor.WHITE + "" + ChatColor.BOLD + MaxDurability.get(player);
            }else if(x < 0.8 && x >= 0.6) {
                Display = ChatColor.WHITE + "現在の耐久値: " + ChatColor.GREEN + NowDurability.get(player) +
                        ChatColor.WHITE + "" + ChatColor.BOLD + " / " +
                        ChatColor.WHITE + "最大耐久値: " + ChatColor.WHITE + "" + ChatColor.BOLD + MaxDurability.get(player);
            }else if(x < 0.6 && x >= 0.2) {
                Display = ChatColor.WHITE + "現在の耐久値: " + ChatColor.YELLOW + NowDurability.get(player) +
                        ChatColor.WHITE + "" + ChatColor.BOLD + " / " +
                        ChatColor.WHITE + "最大耐久値: " + ChatColor.WHITE + "" + ChatColor.BOLD + MaxDurability.get(player);
            }else{
                Display = ChatColor.WHITE + "現在の耐久値: " + ChatColor.RED + NowDurability.get(player) +
                        ChatColor.WHITE + "" + ChatColor.BOLD + " / " +
                        ChatColor.WHITE + "最大耐久値: " + ChatColor.WHITE + "" + ChatColor.BOLD + MaxDurability.get(player);
            }

            switch (i) {
                case 1:
                    meta1.setDisplayName(Display);
                    item1.setItemMeta(meta1);
                    if(StaminaCutterClass.StaminaCutterLv1_Activate.get(player)) {
                        List<String> lore = new ArrayList<>();
                        lore.add(ChatColor.GOLD + "" + ChatColor.BOLD + "ただいま発動中です!");
                        meta1.setLore(lore);
                        item1.setItemMeta(meta1);

                        item1.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
                        item1.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    }
                    inv2.get(player).setItem(9, item1);
                    break;

                case 2:
                    meta2.setDisplayName(Display);
                    item2.setItemMeta(meta2);
                    if(StaminaCutterClass.StaminaCutterLv2_Activate.get(player)) {
                        List<String> lore = new ArrayList<>();
                        lore.add(ChatColor.GOLD + "" + ChatColor.BOLD + "ただいま発動中です!");
                        meta2.setLore(lore);
                        item2.setItemMeta(meta2);

                        item2.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
                        item2.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    }
                    inv2.get(player).setItem(11, item2);
                    break;

                case 3:
                    meta3.setDisplayName(Display);
                    item3.setItemMeta(meta3);
                    if(StaminaCutterClass.StaminaCutterLv3_Activate.get(player)) {
                        List<String> lore = new ArrayList<>();
                        lore.add(ChatColor.GOLD + "" + ChatColor.BOLD + "ただいま発動中です!");
                        meta3.setLore(lore);
                        item3.setItemMeta(meta3);

                        item3.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
                        item3.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    }
                    inv2.get(player).setItem(13, item3);
                    break;

                case 4:
                    meta4.setDisplayName(Display);
                    item4.setItemMeta(meta4);
                    if(StaminaCutterClass.StaminaCutterLv4_Activate.get(player)) {
                        List<String> lore = new ArrayList<>();
                        lore.add(ChatColor.GOLD + "" + ChatColor.BOLD + "ただいま発動中です!");
                        meta4.setLore(lore);
                        item4.setItemMeta(meta4);

                        item4.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
                        item4.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    }
                    inv2.get(player).setItem(15, item4);
                    break;

                case 5:
                    meta5.setDisplayName(Display);
                    item5.setItemMeta(meta5);
                    if(StaminaCutterClass.StaminaCutterLv5_Activate.get(player)) {
                        List<String> lore = new ArrayList<>();
                        lore.add(ChatColor.GOLD + "" + ChatColor.BOLD + "ただいま発動中です!");
                        meta5.setLore(lore);
                        item5.setItemMeta(meta5);

                        item5.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
                        item5.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    }
                    inv2.get(player).setItem(17, item5);
                    break;
            }
        }
    }

    public void CriticalBoost(Player player) {

    }
}