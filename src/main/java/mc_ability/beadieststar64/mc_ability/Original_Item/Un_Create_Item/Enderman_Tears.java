package mc_ability.beadieststar64.mc_ability.Original_Item.Un_Create_Item;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Enderman_Tears {

    public static ItemStack Enderman_tears = new ItemStack(Material.GHAST_TEAR);

    public void CreateItem() {
        CreateEndermansTears();
    }

    public void CreateEndermansTears() {
        ItemMeta EndermansTears_Meta = Enderman_tears.getItemMeta();

        EndermansTears_Meta.setDisplayName(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "エンダーマンの涙");

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "度重なる襲撃で疲れ果てたエンダーマンがこぼした涙。");
        lore.add(ChatColor.AQUA + "集めることで" + ChatColor.GOLD + "" + ChatColor.BOLD + "スタミナカッターの欠片" +
                ChatColor.AQUA + "を作る事ができそうだ。");
        lore.add(ChatColor.AQUA+"Create by " + ChatColor.GOLD+""+ChatColor.BOLD+"MC_Ability");
        EndermansTears_Meta.setLore(lore);

        EndermansTears_Meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        Enderman_tears.setItemMeta(EndermansTears_Meta);

        Enderman_tears.addUnsafeEnchantment(Enchantment.DURABILITY,10);

    }
}