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
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

import static mc_ability.beadieststar64.mc_ability.Original_Item.original_item.Stamina_Cutter.Stamina_Cutter_Base_Class.PieceOfStamina;

public class Stamina_Cutter_Lv4 extends ExtenderOriginalItemClass {

    public Stamina_Cutter_Lv4(MC_Ability plugin) {
        super(plugin);
    }

    public static ItemStack StaminaAnvilSkillLv4 = new ItemStack(Material.ANVIL);


    public void StaminaCutterLv4() {
        StaminaAnvilSkillLv4 = new ItemStack(Material.ANVIL);
        ItemMeta meta = StaminaAnvilSkillLv4.getItemMeta();

        meta.setDisplayName(ChatColor.RED+"スタミナカッター" +
                ChatColor.WHITE+""+ChatColor.BOLD+" Lv." +
                ChatColor.RED + "" + ChatColor.BOLD + "IV");

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.WHITE+"レア度:" +
                ChatColor.RED+"" + ChatColor.BOLD+"ハイパーレア");
        lore.add("");
        lore.add(ChatColor.WHITE+"魔法スキルの一つ");
        lore.add(ChatColor.WHITE+"アクティベートされると");
        lore.add(ChatColor.GOLD+""+ChatColor.BOLD+"全ての道具の耐久値減少が40%低下" + ChatColor.WHITE+"する");
        lore.add(ChatColor.AQUA+"Create by " + ChatColor.GOLD+""+ChatColor.BOLD+"MC_Ability");
        meta.setLore(lore);

        StaminaAnvilSkillLv4.setItemMeta(meta);

        StaminaAnvilSkillLv4.addUnsafeEnchantment(Enchantment.DURABILITY,10);
        StaminaAnvilSkillLv4.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        NamespacedKey StaminaAnvilSkillLv4_name = new NamespacedKey(plugin, "stamina_cutter_lv4");
        ShapedRecipe StaminaAnvilSkillLv4_recipe = new ShapedRecipe(StaminaAnvilSkillLv4_name,StaminaAnvilSkillLv4);
        StaminaAnvilSkillLv4_recipe.shape("IBB","III","AAA");
        StaminaAnvilSkillLv4_recipe.setIngredient('B', Material.COOKED_BEEF);
        StaminaAnvilSkillLv4_recipe.setIngredient('A', Material.ANVIL);
        StaminaAnvilSkillLv4_recipe.setIngredient('I', new RecipeChoice.ExactChoice(PieceOfStamina));

        Bukkit.addRecipe(StaminaAnvilSkillLv4_recipe);
    }
}