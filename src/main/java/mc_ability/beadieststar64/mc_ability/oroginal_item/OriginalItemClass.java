package mc_ability.beadieststar64.mc_ability.oroginal_item;

import mc_ability.beadieststar64.mc_ability.MC_Ability;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

public class OriginalItemClass extends ExtenderOriginalItemClass implements Listener {
    public OriginalItemClass(MC_Ability plugin) {
        super(plugin);
    }

    public void OriginalItmCreate() {
        // 新たに作成するレシピのマテリアルを宣言
        ItemStack item = new ItemStack(Material.BOOK);

        // 新たに作成するアイテムのマテリアルを宣言
        ItemMeta meta = item.getItemMeta();

        // 剣のプロパティを変更した後、次の変数を初期化

        // これはアイテムの名前を設定
        meta.setDisplayName(ChatColor.GOLD + "Ability Setting Book");

        // 1番目に宣言したマテリアルを編集したマテリアルに設定
        item.setItemMeta(meta);

        // Add the custom enchantment to make the emerald sword special
        // In this case, we're adding the permission that modifies the damage value on level 5
        // Level 5 is represented by the second parameter. You can change this to anything compatible with a sword
        //item.addEnchantment(Enchantment.DAMAGE_ALL, 5);
        item.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,10);
        item.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        // create a NamespaceKey for your recipe
        NamespacedKey key = new NamespacedKey(plugin, "setting_book");

        // Create our custom recipe variable
        ShapedRecipe recipe = new ShapedRecipe(key, item);

        // Here we will set the places. E and S can represent anything, and the letters can be anything. Beware; this is case sensitive.
        recipe.shape("EGE", "GBG", "EGE");

        // Set what the letters represent.
        // E = Emerald, S = Stick
        recipe.setIngredient('E', Material.EMERALD);
        recipe.setIngredient('B', Material.BOOK);
        recipe.setIngredient('G', Material.GOLD_INGOT);

        // Finally, add the recipe to the bukkit recipes
        Bukkit.addRecipe(recipe);
    }
}