package mc_ability.beadieststar64.mc_ability.Original_Item.original_item;

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

public class Magic_Ability_Show extends ExtenderOriginalItemClass {

    public Magic_Ability_Show(MC_Ability plugin) {
        super(plugin);
    }

    public static ItemStack AbilityBook = new ItemStack(Material.BOOK);

    public void AbilityBook() {
        // 新たに作成するレシピのマテリアルを宣言
        AbilityBook = new ItemStack(Material.BOOK);

        // 新たに作成するアイテムのマテリアルを宣言
        ItemMeta meta = AbilityBook.getItemMeta();

        // 剣のプロパティを変更した後、次の変数を初期化

        // これはアイテムの名前を設定
        meta.setDisplayName(ChatColor.GOLD + "Ability Setting Book");

        //add Lore
        List<String> Lore = new ArrayList<>();

        Lore.add(ChatColor.WHITE+"MC_Abilityの魔法スキルのインベントリを表示します。" );
        Lore.add("");
        Lore.add(ChatColor.AQUA+"Create by " + ChatColor.GOLD+""+ChatColor.BOLD+"MC_Ability");

        meta.setLore(Lore);

        // 1番目に宣言したマテリアルを編集したマテリアルに設定
        AbilityBook.setItemMeta(meta);

        // Add the custom enchantment to make the emerald sword special
        // In this case, we're adding the permission that modifies the damage value on level 5
        // Level 5 is represented by the second parameter. You can change this to anything compatible with a sword
        //item.addEnchantment(Enchantment.DAMAGE_ALL, 5);
        AbilityBook.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,10);
        AbilityBook.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        // create a NamespaceKey for your recipe
        NamespacedKey Ability_Book = new NamespacedKey(plugin, "setting_book");

        // Create our custom recipe variable
        ShapedRecipe recipe = new ShapedRecipe(Ability_Book, AbilityBook);

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