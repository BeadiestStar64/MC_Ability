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

public class Stamina_Cutter_Lv3 extends ExtenderOriginalItemClass {

    public Stamina_Cutter_Lv3(MC_Ability plugin) {
        super(plugin);
    }

    public static ItemStack StaminaAnvilSkillLv3 = new ItemStack(Material.ANVIL);

    public void StaminaCutterLv3() {
        StaminaAnvilSkillLv3 = new ItemStack(Material.ANVIL);
        ItemMeta meta = StaminaAnvilSkillLv3.getItemMeta();

        meta.setDisplayName(ChatColor.GOLD+"スタミナカッター" +
                ChatColor.WHITE+""+ChatColor.BOLD+" Lv." +
                ChatColor.GOLD + "" + ChatColor.BOLD + "III");

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.WHITE+"レア度:" +
                ChatColor.GOLD+"" + ChatColor.BOLD+"スーパーレア");
        lore.add("");
        lore.add(ChatColor.WHITE+"魔法スキルの一つ");
        lore.add(ChatColor.WHITE+"アクティベートされると");
        lore.add(ChatColor.GOLD+""+ChatColor.BOLD+"全ての道具の耐久値減少が30%低下" + ChatColor.WHITE+"する");
        lore.add(ChatColor.AQUA+"Create by " + ChatColor.GOLD+""+ChatColor.BOLD+"MC_Ability");
        meta.setLore(lore);

        StaminaAnvilSkillLv3.setItemMeta(meta);

        StaminaAnvilSkillLv3.addUnsafeEnchantment(Enchantment.DURABILITY,10);
        StaminaAnvilSkillLv3.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        NamespacedKey StaminaAnvilSkillLv3_name = new NamespacedKey(plugin, "stamina_cutter_lv3");
        ShapedRecipe StaminaAnvilSkillLv3_recipe = new ShapedRecipe(StaminaAnvilSkillLv3_name,StaminaAnvilSkillLv3);
        StaminaAnvilSkillLv3_recipe.shape("BBB","III","AAA");
        StaminaAnvilSkillLv3_recipe.setIngredient('B', Material.COOKED_BEEF);
        StaminaAnvilSkillLv3_recipe.setIngredient('A', Material.ANVIL);
        StaminaAnvilSkillLv3_recipe.setIngredient('I', new RecipeChoice.ExactChoice(PieceOfStamina));

        // Finally, add the recipe to the bukkit recipes
        Bukkit.addRecipe(StaminaAnvilSkillLv3_recipe);
    }
}
