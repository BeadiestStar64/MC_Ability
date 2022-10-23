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

public class Stamina_Cutter_Lv2 extends ExtenderOriginalItemClass {

    public Stamina_Cutter_Lv2(MC_Ability plugin) {
        super(plugin);
    }

    public static ItemStack StaminaAnvilSkillLv2 = new ItemStack(Material.ANVIL);

    public void StaminaCutterLv2() {
        StaminaAnvilSkillLv2 = new ItemStack(Material.ANVIL);
        ItemMeta meta = StaminaAnvilSkillLv2.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA+"スタミナブースト" +
                ChatColor.WHITE+""+ChatColor.BOLD+" Lv." +
                ChatColor.AQUA + "" + ChatColor.BOLD + "II");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.WHITE+"レア度:" +
                ChatColor.AQUA+"" + ChatColor.BOLD+"レア");
        lore.add("");
        lore.add(ChatColor.WHITE+"魔法スキルの一つ");
        lore.add(ChatColor.WHITE+"アクティベートされると");
        lore.add(ChatColor.GOLD+""+ChatColor.BOLD+"全ての道具の耐久値減少が20%低下" + ChatColor.WHITE+"する");
        lore.add(ChatColor.AQUA+"Create by " + ChatColor.GOLD+""+ChatColor.BOLD+"MC_Ability");
        meta.setLore(lore);

        StaminaAnvilSkillLv2.setItemMeta(meta);

        StaminaAnvilSkillLv2.addUnsafeEnchantment(Enchantment.DURABILITY,10);

        NamespacedKey StaminaAnvilSkillLv2_name = new NamespacedKey(plugin, "stamina_cutter_lv2");
        ShapedRecipe StaminaAnvilSkillLv2_recipe = new ShapedRecipe(StaminaAnvilSkillLv2_name,StaminaAnvilSkillLv2);
        StaminaAnvilSkillLv2_recipe.shape("BBB","II ","AAA");
        StaminaAnvilSkillLv2_recipe.setIngredient('B', Material.COOKED_BEEF);
        StaminaAnvilSkillLv2_recipe.setIngredient('A', Material.ANVIL);
        StaminaAnvilSkillLv2_recipe.setIngredient('I', new RecipeChoice.ExactChoice(PieceOfStamina));

        Bukkit.addRecipe(StaminaAnvilSkillLv2_recipe);
    }
}
