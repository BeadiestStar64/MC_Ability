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

public class Stamina_Cutter_Lv1 extends ExtenderOriginalItemClass {

    public Stamina_Cutter_Lv1(MC_Ability plugin) {
        super(plugin);
    }

    public static ItemStack StaminaAnvilSkillLv1 = new ItemStack(Material.ANVIL);

    public void StaminaCutterLv1() {

        StaminaAnvilSkillLv1= new ItemStack(Material.ANVIL);
        ItemMeta meta = StaminaAnvilSkillLv1.getItemMeta();
        meta.setDisplayName(ChatColor.GRAY+"スタミナカッター" +
                ChatColor.WHITE+""+ChatColor.BOLD+" Lv." +
                ChatColor.GRAY + "" + ChatColor.BOLD + "I");

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.WHITE+"レア度:" +
                ChatColor.GRAY+"" + ChatColor.BOLD+"ノーマル");
        lore.add("");
        lore.add(ChatColor.WHITE+"魔法スキルの一つ");
        lore.add(ChatColor.WHITE+"アクティベートされると");
        lore.add(ChatColor.GOLD+""+ChatColor.BOLD+"全ての道具の耐久値減少が10%低下" + ChatColor.WHITE+"する");
        lore.add(ChatColor.AQUA+"Create by " + ChatColor.GOLD+""+ChatColor.BOLD+"MC_Ability");
        meta.setLore(lore);

        StaminaAnvilSkillLv1.setItemMeta(meta);

        StaminaAnvilSkillLv1.addUnsafeEnchantment(Enchantment.DURABILITY,10);

        NamespacedKey StaminaAnvilSkillLv1_name = new NamespacedKey(plugin, "stamina_cutter_lv1");
        ShapedRecipe StaminaAnvilSkillLv1_recipe = new ShapedRecipe(StaminaAnvilSkillLv1_name,StaminaAnvilSkillLv1);
        StaminaAnvilSkillLv1_recipe.shape("BBB"," I ","AAA");
        StaminaAnvilSkillLv1_recipe.setIngredient('B', Material.COOKED_BEEF);
        StaminaAnvilSkillLv1_recipe.setIngredient('A', Material.ANVIL);
        StaminaAnvilSkillLv1_recipe.setIngredient('I', new RecipeChoice.ExactChoice(PieceOfStamina));

        Bukkit.addRecipe(StaminaAnvilSkillLv1_recipe);

    }
}
