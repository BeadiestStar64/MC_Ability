package mc_ability.beadieststar64.mc_ability.MagicSkill.StaminaCutter;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

public class BlockBreakClass {

    public static double DurabilityLevel = 0;
    public static Map<Player, Double> PlayerDurabilityProbality = new HashMap<>();

    public void BlockBreak(Player player) {

        if(player.getInventory().getItemInMainHand().containsEnchantment(Enchantment.DURABILITY)) {
            DurabilityLevel = player.getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.DURABILITY);
        }

        double DurabilityProbality = Math.random();

        PlayerDurabilityProbality.put(player, 100/(100/(DurabilityLevel + 1)));

        if(DurabilityProbality <= PlayerDurabilityProbality.get(player)) {
            ItemStack item = player.getInventory().getItemInMainHand();
            ItemMeta meta = item.getItemMeta();
            int durability = (player.getInventory().getItemInMainHand().getDurability() - 1);
            item.setDurability((short) durability);
            item.setItemMeta(meta);
        }
    }
}