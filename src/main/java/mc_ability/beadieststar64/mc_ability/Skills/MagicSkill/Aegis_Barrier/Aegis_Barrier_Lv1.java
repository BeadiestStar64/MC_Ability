package mc_ability.beadieststar64.mc_ability.Skills.MagicSkill.Aegis_Barrier;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Aegis_Barrier_Lv1 {

    public static ItemStack AegisbarrierLv1 = new ItemStack(Material.SHIELD);

    public void AegisBarreier_lv1() {

        AegisbarrierLv1= new ItemStack(Material.SHIELD);
        ItemMeta meta = AegisbarrierLv1.getItemMeta();
        meta.setDisplayName(ChatColor.LIGHT_PURPLE+"イージスバリア" +
                ChatColor.WHITE+""+ChatColor.BOLD+" Lv." +
                ChatColor.GRAY + "" + ChatColor.BOLD + "I");

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.WHITE +"<レア度> " +
                ChatColor.GRAY+"" + ChatColor.BOLD+"ノーマル");
        lore.add(ChatColor.WHITE + "<タイプ> " + ChatColor.GREEN + "ガード");
        lore.add("");
        lore.add(ChatColor.GOLD + "" + ChatColor.BOLD+"全ての被ダメージを5%軽減" + ChatColor.WHITE+"する");
        lore.add(ChatColor.WHITE + "ただし、他のガードスキルをすべて無効化する");
        lore.add(ChatColor.AQUA+"Create by " + ChatColor.GOLD+""+ChatColor.BOLD+"MC_Ability");
        meta.setLore(lore);


        AegisbarrierLv1.setItemMeta(meta);

        AegisbarrierLv1.addUnsafeEnchantment(Enchantment.DIG_SPEED,10);
    }
}
