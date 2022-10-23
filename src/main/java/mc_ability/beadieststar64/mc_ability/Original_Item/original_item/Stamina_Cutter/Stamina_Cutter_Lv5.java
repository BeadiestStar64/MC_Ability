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

public class Stamina_Cutter_Lv5 extends ExtenderOriginalItemClass {
    public Stamina_Cutter_Lv5(MC_Ability plugin) {
        super(plugin);
    }

    public static ItemStack StaminaAnvilSkillLv5 = new ItemStack(Material.ANVIL);


    public void StaminaCutterLv5() {
        StaminaAnvilSkillLv5 = new ItemStack(Material.ANVIL);
        ItemMeta meta = StaminaAnvilSkillLv5.getItemMeta();

        meta.setDisplayName(ChatColor.LIGHT_PURPLE+"スタミナカッター" +
                ChatColor.WHITE+""+ChatColor.BOLD+" Lv." +
                ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "V");

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.WHITE+"レア度:" +
                ChatColor.LIGHT_PURPLE+"" + ChatColor.BOLD+"極！レア");
        lore.add("");
        lore.add(ChatColor.WHITE+"魔法スキルの一つ");
        lore.add(ChatColor.WHITE+"アクティベートされると");
        lore.add(ChatColor.GOLD+""+ChatColor.BOLD+"全ての道具の耐久値減少が50%低下" + ChatColor.WHITE+"する");
        lore.add(ChatColor.AQUA+"Create by " + ChatColor.GOLD+""+ChatColor.BOLD+"MC_Ability");
        meta.setLore(lore);

        StaminaAnvilSkillLv5.setItemMeta(meta);

        StaminaAnvilSkillLv5.addUnsafeEnchantment(Enchantment.DURABILITY,10);
        StaminaAnvilSkillLv5.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        NamespacedKey StaminaAnvilSkillLv5_name = new NamespacedKey(plugin, "stamina_cutter_lv5");
        ShapedRecipe StaminaAnvilSkillLv5_recipe = new ShapedRecipe(StaminaAnvilSkillLv5_name,StaminaAnvilSkillLv5);
        StaminaAnvilSkillLv5_recipe.shape("IBI","III","AAA");
        StaminaAnvilSkillLv5_recipe.setIngredient('B', Material.COOKED_BEEF);
        StaminaAnvilSkillLv5_recipe.setIngredient('A', Material.ANVIL);
        StaminaAnvilSkillLv5_recipe.setIngredient('I', new RecipeChoice.ExactChoice(PieceOfStamina));

        Bukkit.addRecipe(StaminaAnvilSkillLv5_recipe);
    }
}
