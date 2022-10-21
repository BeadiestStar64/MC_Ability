package mc_ability.beadieststar64.mc_ability.GUI;

import mc_ability.beadieststar64.mc_ability.MC_Ability;
import mc_ability.beadieststar64.mc_ability.MagicSkill.StaminaCutterClass;
import mc_ability.beadieststar64.mc_ability.Original_Item.OriginalItemClass;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
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

        if (!event.getInventory().equals(inv2.get(player))) {
            if (!event.getInventory().equals(inv.get(player))) {
                return;
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
            if (event.getSlot() == 0) {
                menus.put(player, 1);

                SecondGUI(player);
                player.openInventory(inv2.get(player));
            } else if (event.getSlot() == 8) {
                player.closeInventory();
            }
        } else if (event.getClickedInventory() == inv2.get(player)) {
            if (event.getSlot() == 0) {
                for (int i = 0; i < 36; i++) {
                    if(player.getInventory().getItem(i) == null) {
                        continue;
                    }
                    if (player.getInventory().getItem(i).getI18NDisplayName().equals(StaminaCutterLv1.getI18NDisplayName())) {
                        inv2.get(player).close();
                        player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "スタミナカッター Lv.1" +
                                ChatColor.WHITE + "発動!!");

                        StaminaCutterLv1.setAmount(1);
                        player.getInventory().removeItem(StaminaCutterLv1);

                        cutterClass.StaminaCutterLv2_Activate.put(player, true);
                        break;
                    }else{
                        player.sendMessage(ChatColor.RED + "インベントリに「スタミナカッター Lv.1」を持つ必要があります!!");
                    }
                }
            }else if(event.getSlot() == 2) {
                for (int i = 0; i < 36; i++) {
                    if(player.getInventory().getItem(i) == null) {
                        continue;
                    }
                    if (player.getInventory().getItem(i).getI18NDisplayName().equals(StaminaCutterLv2.getI18NDisplayName())) {
                        inv2.get(player).close();
                        player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "スタミナカッター Lv.2" +
                                ChatColor.WHITE + "発動!!");

                        StaminaCutterLv2.setAmount(1);
                        player.getInventory().removeItem(StaminaCutterLv2);

                        cutterClass.StaminaCutterLv2_Activate.put(player, true);
                        break;
                    }else{
                        player.sendMessage(ChatColor.RED + "インベントリに「スタミナカッター Lv.2」を持つ必要があります!!");
                    }
                }
            }else if(event.getSlot() == 4) {
                for (int i = 0; i < 36; i++) {
                    if(player.getInventory().getItem(i) == null) {
                        continue;
                    }
                    if (player.getInventory().getItem(i).getI18NDisplayName().equals(StaminaCutterLv3.getI18NDisplayName())) {
                        inv2.get(player).close();
                        player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "スタミナカッター Lv.3" +
                                ChatColor.WHITE + "発動!!");

                        StaminaCutterLv3.setAmount(1);
                        player.getInventory().removeItem(StaminaCutterLv3);

                        cutterClass.StaminaCutterLv3_Activate.put(player, true);
                        break;
                    }else{
                        player.sendMessage(ChatColor.RED + "インベントリに「スタミナカッター Lv.3」を持つ必要があります!!");
                    }
                }
            }else if(event.getSlot() == 6) {
                for (int i = 0; i < 36; i++) {
                    if(player.getInventory().getItem(i) == null) {
                        continue;
                    }
                    if (player.getInventory().getItem(i).getI18NDisplayName().equals(StaminaCutterLv4.getI18NDisplayName())) {
                        inv2.get(player).close();
                        player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "スタミナカッター Lv.4" +
                                ChatColor.WHITE + "発動!!");

                        StaminaCutterLv4.setAmount(1);
                        player.getInventory().removeItem(StaminaCutterLv4);

                        cutterClass.StaminaCutterLv4_Activate.put(player, true);
                        break;
                    }else{
                        player.sendMessage(ChatColor.RED + "インベントリに「スタミナカッター Lv.4」を持つ必要があります!!");
                    }
                }
            }else if(event.getSlot() == 8) {
                for (int i = 0; i < 36; i++) {
                    if(player.getInventory().getItem(i) == null) {
                        continue;
                    }
                    if (player.getInventory().getItem(i).getI18NDisplayName().equals(StaminaCutterLv5.getI18NDisplayName())) {
                        inv2.get(player).close();
                        player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "スタミナカッター Lv.5" +
                                ChatColor.WHITE + "発動!!");

                        StaminaCutterLv5.setAmount(1);
                        player.getInventory().removeItem(StaminaCutterLv5);

                        cutterClass.StaminaCutterLv5_Activate.put(player, true);
                        break;
                    }else{
                        player.sendMessage(ChatColor.RED + "インベントリに「スタミナカッター Lv.5」を持つ必要があります!!");
                    }
                }
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

                StaminaCutterLv1(player);
                StaminaCutterLv2(player);
                StaminaCutterLv3(player);
                StaminaCutterLv4(player);
                StaminaCutterLv5(player);
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

    public void StaminaCutterLv1(Player player) {
        StaminaCutterLv1 = new ItemStack(Material.ANVIL);
        ItemMeta meta = StaminaCutterLv1.getItemMeta();

        meta.setDisplayName(ChatColor.GRAY+"スタミナカッター" +
                ChatColor.WHITE+""+ChatColor.BOLD+" Lv.1");

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.WHITE+"レア度:" +
                ChatColor.GRAY+"" + ChatColor.BOLD+"ノーマル");
        lore.add("");
        lore.add(ChatColor.WHITE+"魔法スキルの一つ");
        lore.add(ChatColor.WHITE+"アクティベートされると");
        lore.add(ChatColor.GOLD+""+ChatColor.BOLD+"全ての道具の耐久値減少が10%低下" + ChatColor.WHITE+"する");
        lore.add(ChatColor.AQUA+"Create by " + ChatColor.GOLD+""+ChatColor.BOLD+"MC_Ability");
        meta.setLore(lore);

        StaminaCutterLv1.setItemMeta(meta);

        StaminaCutterLv1.addUnsafeEnchantment(Enchantment.DURABILITY,10);
        StaminaCutterLv1.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        inv2.get(player).setItem(0, StaminaCutterLv1);
    }

    public void StaminaCutterLv2(Player player) {
        StaminaCutterLv2 = new ItemStack(Material.ANVIL);
        ItemMeta meta = StaminaCutterLv2.getItemMeta();

        meta.setDisplayName(ChatColor.AQUA+"スタミナカッター" +
                ChatColor.WHITE+""+ChatColor.BOLD+" Lv.2");

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.WHITE+"レア度:" +
                ChatColor.AQUA+"" + ChatColor.BOLD+"レア");
        lore.add("");
        lore.add(ChatColor.WHITE+"魔法スキルの一つ");
        lore.add(ChatColor.WHITE+"アクティベートされると");
        lore.add(ChatColor.GOLD+""+ChatColor.BOLD+"全ての道具の耐久値減少が20%低下" + ChatColor.WHITE+"する");
        lore.add(ChatColor.AQUA+"Create by " + ChatColor.GOLD+""+ChatColor.BOLD+"MC_Ability");
        meta.setLore(lore);

        StaminaCutterLv2.setItemMeta(meta);

        StaminaCutterLv2.addUnsafeEnchantment(Enchantment.DURABILITY,10);
        StaminaCutterLv2.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        inv2.get(player).setItem(2, StaminaCutterLv2);
    }

    public void StaminaCutterLv3(Player player) {
        StaminaCutterLv3 = new ItemStack(Material.ANVIL);
        ItemMeta meta = StaminaCutterLv3.getItemMeta();

        meta.setDisplayName(ChatColor.GOLD+"スタミナカッター" +
                ChatColor.WHITE+""+ChatColor.BOLD+" Lv.3");

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.WHITE+"レア度:" +
                ChatColor.GOLD+"" + ChatColor.BOLD+"スーパーレア");
        lore.add("");
        lore.add(ChatColor.WHITE+"魔法スキルの一つ");
        lore.add(ChatColor.WHITE+"アクティベートされると");
        lore.add(ChatColor.GOLD+""+ChatColor.BOLD+"全ての道具の耐久値減少が30%低下" + ChatColor.WHITE+"する");
        lore.add(ChatColor.AQUA+"Create by " + ChatColor.GOLD+""+ChatColor.BOLD+"MC_Ability");
        meta.setLore(lore);

        StaminaCutterLv3.setItemMeta(meta);

        StaminaCutterLv3.addUnsafeEnchantment(Enchantment.DURABILITY,10);
        StaminaCutterLv3.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        inv2.get(player).setItem(4, StaminaCutterLv3);
    }

    public void StaminaCutterLv4(Player player) {
        StaminaCutterLv4 = new ItemStack(Material.ANVIL);
        ItemMeta meta = StaminaCutterLv4.getItemMeta();

        meta.setDisplayName(ChatColor.RED+"スタミナカッター" +
                ChatColor.WHITE+""+ChatColor.BOLD+" Lv.4");

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.WHITE+"レア度:" +
                ChatColor.RED+"" + ChatColor.BOLD+"ハイパーレア");
        lore.add("");
        lore.add(ChatColor.WHITE+"魔法スキルの一つ");
        lore.add(ChatColor.WHITE+"アクティベートされると");
        lore.add(ChatColor.GOLD+""+ChatColor.BOLD+"全ての道具の耐久値減少が40%低下" + ChatColor.WHITE+"する");
        lore.add(ChatColor.AQUA+"Create by " + ChatColor.GOLD+""+ChatColor.BOLD+"MC_Ability");
        meta.setLore(lore);

        StaminaCutterLv4.setItemMeta(meta);

        StaminaCutterLv4.addUnsafeEnchantment(Enchantment.DURABILITY,10);
        StaminaCutterLv4.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        inv2.get(player).setItem(6, StaminaCutterLv4);
    }

    public void StaminaCutterLv5(Player player) {
        StaminaCutterLv5 = new ItemStack(Material.ANVIL);
        ItemMeta meta = StaminaCutterLv5.getItemMeta();

        meta.setDisplayName(ChatColor.LIGHT_PURPLE+"スタミナカッター" +
                ChatColor.WHITE+""+ChatColor.BOLD+" Lv.5");

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.WHITE+"レア度:" +
                ChatColor.LIGHT_PURPLE+"" + ChatColor.BOLD+"極！レア");
        lore.add("");
        lore.add(ChatColor.WHITE+"魔法スキルの一つ");
        lore.add(ChatColor.WHITE+"アクティベートされると");
        lore.add(ChatColor.GOLD+""+ChatColor.BOLD+"全ての道具の耐久値減少が50%低下" + ChatColor.WHITE+"する");
        lore.add(ChatColor.AQUA+"Create by " + ChatColor.GOLD+""+ChatColor.BOLD+"MC_Ability");
        meta.setLore(lore);

        StaminaCutterLv5.setItemMeta(meta);

        StaminaCutterLv5.addUnsafeEnchantment(Enchantment.DURABILITY,10);
        StaminaCutterLv5.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        inv2.get(player).setItem(8, StaminaCutterLv5);
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
}