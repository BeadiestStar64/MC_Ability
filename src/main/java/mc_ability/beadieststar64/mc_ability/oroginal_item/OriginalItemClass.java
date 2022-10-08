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

import java.util.ArrayList;
import java.util.List;

public class OriginalItemClass extends ExtenderOriginalItemClass implements Listener {

    public static ItemMeta meta;

    public OriginalItemClass(MC_Ability plugin) {
        super(plugin);
    }

    public void OriginalItmCreate() {
        AbilityBook();
        StaminaAnvilSkillLv1();
        StaminaAnvilLv2();
    }

    public void AbilityBook() {
        // 新たに作成するレシピのマテリアルを宣言
        ItemStack AbilityBook = new ItemStack(Material.BOOK);

        // 新たに作成するアイテムのマテリアルを宣言
        meta = AbilityBook.getItemMeta();

        // 剣のプロパティを変更した後、次の変数を初期化

        // これはアイテムの名前を設定
        meta.setDisplayName(ChatColor.GOLD + "Ability Setting Book");

        //add Lore
        List<String> Lore = new ArrayList<>();

        Lore.add(ChatColor.WHITE+"MC_Abilityの魔法スキルのインベントリを表示します。" );
        Lore.add(ChatColor.AQUA+"Create by" + ChatColor.GOLD+""+ChatColor.BOLD+"MC_Ability");

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
        NamespacedKey key = new NamespacedKey(plugin, "setting_book");

        // Create our custom recipe variable
        ShapedRecipe recipe = new ShapedRecipe(key, AbilityBook);

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

    //以下、魔法スキルGUIアイテム
    //グレー→ノーマル  アクア→レア  ゴールド→スーパーレア 赤→ハイパーレア    紫→ウルトラレア

    public void StaminaAnvilSkillLv1() {
        ItemStack StaminaBoostSkillLv1 = new ItemStack(Material.ANVIL);
        ItemMeta meta = StaminaBoostSkillLv1.getItemMeta();
        meta.setDisplayName(ChatColor.GRAY+"スタミナブースト" +
                ChatColor.WHITE+""+ChatColor.BOLD+" Lv.1");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.WHITE+"レア度:" +
                ChatColor.GRAY+"" + ChatColor.BOLD+"ノーマル");
        lore.add("");
        lore.add(ChatColor.WHITE+"魔法スキルの一つ");
        lore.add(ChatColor.WHITE+"アクティベートされると");
        lore.add(ChatColor.GOLD+""+ChatColor.BOLD+"全ての道具の耐久値減少が10%低下" + ChatColor.WHITE+"する");
        lore.add(ChatColor.AQUA+"Create by" + ChatColor.GOLD+""+ChatColor.BOLD+"MC_Ability");
        meta.setLore(lore);

        StaminaBoostSkillLv1.setItemMeta(meta);

        StaminaBoostSkillLv1.addUnsafeEnchantment(Enchantment.DURABILITY,10);
        NamespacedKey key = new NamespacedKey(plugin, "stamina_lv1");
        ShapedRecipe recipe = new ShapedRecipe(key, StaminaBoostSkillLv1);
        recipe.shape("NNN","NBN","NNN");
        recipe.setIngredient('N', Material.NETHERITE_BLOCK);
        recipe.setIngredient('B', Material.BOOKSHELF);
        Bukkit.addRecipe(recipe);

    }

    public void StaminaAnvilLv2() {
        ItemStack StaminaBoostSkillLv1 = new ItemStack(Material.ANVIL);
        ItemMeta meta = StaminaBoostSkillLv1.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA+"スタミナブースト" +
                ChatColor.WHITE+""+ChatColor.BOLD+" Lv.2");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.WHITE+"レア度:" +
                ChatColor.AQUA+"" + ChatColor.BOLD+"レア");
        lore.add("");
        lore.add(ChatColor.WHITE+"魔法スキルの一つ");
        lore.add(ChatColor.WHITE+"アクティベートされると");
        lore.add(ChatColor.GOLD+""+ChatColor.BOLD+"全ての道具の耐久値減少が20%低下" + ChatColor.WHITE+"する");
        lore.add(ChatColor.AQUA+"Create by" + ChatColor.GOLD+""+ChatColor.BOLD+"MC_Ability");
        meta.setLore(lore);

        StaminaBoostSkillLv1.setItemMeta(meta);

        StaminaBoostSkillLv1.addUnsafeEnchantment(Enchantment.DURABILITY,10);
        NamespacedKey key = new NamespacedKey(plugin, "stamina_lv2");
        ShapedRecipe recipe = new ShapedRecipe(key, StaminaBoostSkillLv1);
        recipe.shape("NDN","NBN","NDN");
        recipe.setIngredient('N', Material.NETHERITE_BLOCK);
        recipe.setIngredient('B', Material.BOOKSHELF);
        recipe.setIngredient('D', Material.DIAMOND);
        Bukkit.addRecipe(recipe);
    }
}