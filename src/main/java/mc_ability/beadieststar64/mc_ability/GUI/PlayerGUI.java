package mc_ability.beadieststar64.mc_ability.GUI;

import mc_ability.beadieststar64.mc_ability.MC_Ability;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerGUI extends ExtenderForGUI implements Listener {
    private String GUITitle;
    private Integer GUISize;
    public static Map<Player, Inventory> inv = new HashMap<>();
    public static Map<Player, String> Data = new HashMap<>();
    public static Map<String, ItemStack[]> menus = new HashMap<String, ItemStack[]>();

    public PlayerGUI(MC_Ability plugin) {
        super(plugin);
    }

    public void CreateGUI() {
        inv = new HashMap<>();
        Bukkit.getLogger().info("[MC_Ability] GUIの作成に成功しました。");
    }

    public void SetGUI(Player player) {

        inv.put(player, Bukkit.createInventory(player, 9, ChatColor.GOLD+"Test GUI"));

        Map<Player, ItemStack> item = new HashMap<>();
        Map<Player, ItemMeta> meta = new HashMap<>();
        item.put(player, new ItemStack(Material.DIAMOND_BLOCK));
        meta.put(player, item.get(player).getItemMeta());

        meta.get(player).setDisplayName(ChatColor.GREEN+""+ChatColor.BOLD+"Click here!");
        List<String> Lore = new ArrayList<String>();
        Lore.add(ChatColor.GRAY + "ここを押してね♥");
        meta.get(player).setLore(Lore);
        item.get(player).setItemMeta(meta.get(player));
        inv.get(player).setItem(0, item.get(player));

        //Cancel Item
        item.get(player).setType(Material.BARRIER);
        meta.get(player).setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Close Menu");
        Lore.clear();
        meta.get(player).setLore(Lore);
        item.get(player).setItemMeta(meta.get(player));
        inv.get(player).setItem(8, item.get(player));

    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if(!event.getInventory().equals(inv.get(player))) {
            return;
        }
        if(event.getCurrentItem() == null) {
            return;
        }
        if(event.getCurrentItem().getItemMeta() == null) {
            return;
        }
        if(event.getCurrentItem().getItemMeta().getDisplayName() == null) {
            return;
        }

        if(event.getClickedInventory() == inv.get(player)) {

            event.setCancelled(true);

            if(event.getSlot() == 0 && event.getClickedInventory() == inv.get(player)) {

                player.sendMessage(ChatColor.GOLD+""+ChatColor.BOLD+"おめでとう！");
                player.sendMessage(ChatColor.WHITE+"test GUIは完成だ！");
                player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1, 1);

                player.closeInventory();
            }else if(event.getSlot() == 8) {
                player.closeInventory();
            }
        }
    }

    public void saveInvs() {
        for(Map.Entry<String, ItemStack[]> entry : menus.entrySet()) {
            plugin.getConfig().set("data." + entry.getKey(), entry.getValue());
        }
        plugin.saveConfig();
    }

    public void restoreInvs() {
        plugin.getConfig().getConfigurationSection("data").getKeys(false).forEach(key ->{
            @SuppressWarnings("unchecked")
            ItemStack[] content = ((List<ItemStack>) plugin.getConfig().get("data." + key)).toArray(new ItemStack[0]);
            menus.put(key, content);
        });
    }

    @EventHandler
    public void onGUIClose(InventoryCloseEvent event) {
        if(event.getView().getTitle().contains(event.getPlayer().getName() + "'s Private GUI")) {
            menus.put(event.getPlayer().getUniqueId().toString(), event.getInventory().getContents());
        }
    }
}