package mc_ability.beadieststar64.mc_ability.Original_Item.original_item.Stamina_Cutter;

import mc_ability.beadieststar64.mc_ability.MC_Ability;
import mc_ability.beadieststar64.mc_ability.Original_Item.ExtenderOriginalItemClass;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Stamina_Cutter_Base_Class extends ExtenderOriginalItemClass {

    public Stamina_Cutter_Base_Class(MC_Ability plugin) {
        super(plugin);
    }

    public static ItemStack PieceOfStamina = new ItemStack(Material.IRON_INGOT);

    public void BaseItem() {
        PieceOfStamina = new ItemStack(Material.IRON_INGOT);
        ItemMeta meta = PieceOfStamina.getItemMeta();

        meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "スタミナカッターの欠片");

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.AQUA + "スタミナカッターの欠片");
        lore.add(ChatColor.AQUA + "これを使う数によって、レベルが変わる");
        lore.add("");
        lore.add(ChatColor.AQUA+"Create by " + ChatColor.GOLD+""+ChatColor.BOLD+"MC_Ability");
        meta.setLore(lore);

        PieceOfStamina.setItemMeta(meta);

        PieceOfStamina.addUnsafeEnchantment(Enchantment.MENDING, 5);
        PieceOfStamina.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        NamespacedKey PieceOfStamina_Name = new NamespacedKey(plugin, "piece_of_tamina");
        ShapedRecipe recipe = new ShapedRecipe(PieceOfStamina_Name, PieceOfStamina);
        recipe.shape("EEE","EIE","EEE");
        recipe.setIngredient('E', Material.ENDER_PEARL);
        recipe.setIngredient('I', Material.IRON_INGOT);
        Bukkit.addRecipe(recipe);

    }
}