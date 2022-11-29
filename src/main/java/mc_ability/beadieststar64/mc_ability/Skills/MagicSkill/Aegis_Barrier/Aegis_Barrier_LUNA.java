package mc_ability.beadieststar64.mc_ability.Skills.MagicSkill.Aegis_Barrier;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Aegis_Barrier_LUNA {

    public static ItemStack AegisbarrierLUNA = new ItemStack(Material.SHIELD);

    public void AegisBarreier_LUNA() {

        AegisbarrierLUNA= new ItemStack(Material.SHIELD);
        ItemMeta meta = AegisbarrierLUNA.getItemMeta();
        meta.setDisplayName(ChatColor.LIGHT_PURPLE+"イージスバリア" +
                ChatColor.WHITE+""+ChatColor.BOLD+"・" +
                ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "LUNA");

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.WHITE +"<レア度> " +
                ChatColor.LIGHT_PURPLE+"" + ChatColor.BOLD+"LUNATIC");
        lore.add(ChatColor.WHITE + "<タイプ> " + ChatColor.GREEN + "ガード");
        lore.add("");
        lore.add(ChatColor.GOLD + "" + ChatColor.BOLD+"全ての被ダメージを10%軽減" + ChatColor.WHITE+"する");
        lore.add(ChatColor.WHITE + "更に敵エンティティからの被ダメージを5%追加で軽減する");
        lore.add(ChatColor.WHITE + "ルナティックエネミーからの被ダメージを15%追加で軽減する");
        lore.add(ChatColor.WHITE + "ただし、他のガードスキルをすべて無効化する");
        lore.add(ChatColor.AQUA+"Create by " + ChatColor.GOLD+""+ChatColor.BOLD+"MC_Ability");
        meta.setLore(lore);


        AegisbarrierLUNA.setItemMeta(meta);

        AegisbarrierLUNA.addUnsafeEnchantment(Enchantment.DIG_SPEED,10);
    }
}
