package mc_ability.beadieststar64.mc_ability.Original_Item;

import mc_ability.beadieststar64.mc_ability.MC_Ability;
import mc_ability.beadieststar64.mc_ability.Original_Item.original_item.*;
import mc_ability.beadieststar64.mc_ability.Original_Item.original_item.Stamina_Cutter.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OriginalItemClass extends ExtenderOriginalItemClass implements Listener {

    public Magic_Ability_Show abilitybookClass = new Magic_Ability_Show(plugin);

    public Stamina_Cutter_Base_Class CutterBaseClass = new Stamina_Cutter_Base_Class(plugin);
    public Stamina_Cutter_Lv1 cutterLv1Class = new Stamina_Cutter_Lv1(plugin);
    public Stamina_Cutter_Lv2 cutterLv2Class = new Stamina_Cutter_Lv2(plugin);
    public Stamina_Cutter_Lv3 cutterLv3Class = new Stamina_Cutter_Lv3(plugin);
    public Stamina_Cutter_Lv4 cutterLv4Class = new Stamina_Cutter_Lv4(plugin);
    public Stamina_Cutter_Lv5 cutterLv5Class = new Stamina_Cutter_Lv5(plugin);

    public OriginalItemClass(MC_Ability plugin) {
        super(plugin);
    }

    public void OriginalItmCreate() {

        abilitybookClass.AbilityBook();

        CutterBaseClass.BaseItem();
        cutterLv1Class.StaminaCutterLv1();
        cutterLv2Class.StaminaCutterLv2();
        cutterLv3Class.StaminaCutterLv3();
        cutterLv4Class.StaminaCutterLv4();
        cutterLv5Class.StaminaCutterLv5();

        ShiningDiamond();

        Bukkit.getLogger().info("アイテム登録が完了しました。");
    }

    //以下、魔法スキルGUIアイテム
    //グレー→ノーマル  アクア→レア  ゴールド→スーパーレア 赤→ハイパーレア    紫→ウルトラレア

    public void RemoveRecipe() {

    }

    public void ShiningDiamond() {
        ItemStack ShiningDiamond = new ItemStack(Material.DIAMOND);
        ItemMeta ShiningDiamond_meta = ShiningDiamond.getItemMeta();

        ShiningDiamond_meta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Shining Diamond");

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GREEN + "地下5000mから発掘された超貴重なダイヤ");
        lore.add("");
        lore.add(ChatColor.AQUA+"Create by " + ChatColor.GOLD+""+ChatColor.BOLD+"MC_Ability");
        ShiningDiamond_meta.setLore(lore);

        ShiningDiamond.setItemMeta(ShiningDiamond_meta);

        ShiningDiamond.addUnsafeEnchantment(Enchantment.DURABILITY, 10);

        NamespacedKey name = new NamespacedKey(plugin, "shining_diamond");
        ShapedRecipe recipe = new ShapedRecipe(name, ShiningDiamond);

        recipe.shape("RGR"," D ","RGR");
        recipe.setIngredient('R', Material.FIREWORK_ROCKET);
        recipe.setIngredient('G', Material.GLOWSTONE_DUST);
        recipe.setIngredient('D', Material.DIAMOND);

        Bukkit.addRecipe(recipe);

        ItemStack SunFlower = new ItemStack(Material.SUNFLOWER);
        ItemMeta SunFlower_meta = SunFlower.getItemMeta();

        SunFlower_meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "大輪の輝き");

        List<String> SunFlower_lore = new ArrayList<>();
        SunFlower_lore.add(ChatColor.AQUA + "長い夏を乗り越え、夏終わりが近づいたある日。");
        SunFlower_lore.add(ChatColor.AQUA + "一輪の向日葵が綺麗に咲いた。");
        SunFlower_lore.add("");
        SunFlower_lore.add(ChatColor.AQUA+"Create by " + ChatColor.GOLD+""+ChatColor.BOLD+"MC_Ability");
        SunFlower_meta.setLore(SunFlower_lore);

        AttributeModifier mod = new AttributeModifier(UUID.randomUUID(), "", 20, AttributeModifier.Operation.ADD_NUMBER,EquipmentSlot.HAND);
        SunFlower_meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, mod);

        SunFlower.setItemMeta(SunFlower_meta);

        SunFlower.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 10);
        SunFlower.addUnsafeEnchantment(Enchantment.DURABILITY, 5);
        SunFlower.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 7);

        NamespacedKey SunFlower_name = new NamespacedKey(plugin, "god_sunflower");
        ShapedRecipe SunFlower_recipe = new ShapedRecipe(SunFlower_name, SunFlower);

        SunFlower_recipe.shape("WWW"," S ","NDN");
        SunFlower_recipe.setIngredient('S', new RecipeChoice.ExactChoice(ShiningDiamond));
        SunFlower_recipe.setIngredient('N', Material.NETHERITE_AXE);
        SunFlower_recipe.setIngredient('D', Material.DIAMOND_SWORD);
        SunFlower_recipe.setIngredient('W', Material.WITHER_SKELETON_SKULL);
        Bukkit.addRecipe(SunFlower_recipe);
    }
}