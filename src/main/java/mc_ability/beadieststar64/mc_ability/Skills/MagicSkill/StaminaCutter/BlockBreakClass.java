package mc_ability.beadieststar64.mc_ability.Skills.MagicSkill.StaminaCutter;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class BlockBreakClass implements Listener {

    public static double DurabilityLevel = 0;
    public static Map<Player, Double> PlayerDurabilityProbality = new HashMap<>();
    public static Map<Player, Double> DurabulityProbability = new HashMap<>();
    public static Map<Player, Double> DurabulityEnchantmentLevel = new HashMap<>();
    public static Map<Player, Integer> BeforeDamage = new HashMap<>();

    public Material[] armors = {Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS,
    Material.CHAINMAIL_HELMET, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_LEGGINGS, Material.CHAINMAIL_BOOTS,
    Material.GOLDEN_HELMET, Material.GOLDEN_CHESTPLATE, Material.GOLDEN_LEGGINGS, Material.GOLDEN_BOOTS,
    Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS,
    Material.NETHERITE_HELMET, Material.NETHERITE_CHESTPLATE, Material.NETHERITE_LEGGINGS, Material.NETHERITE_BOOTS};

    public ArrayList<Material> ArmorList = new ArrayList<>(Arrays.asList(armors));
    public void Setting(Player player) {

    }

    /*
    1.耐久値減少処理をするか分岐
    2.耐久値が減少しない、かつ前回から耐久値が減っているなら、耐久値を+1する。
    3.耐久値が減少しない、かつ前回から耐久値が減っていないなら、return。
    4.耐久値が減少、かつ前回から耐久値が減っているならreturn。
    5.耐久値が減少、かつ前回から耐久値がげっていないなら、耐久値を-1する。
     */

    @EventHandler
    public void Durability (PlayerItemDamageEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        double Random = Math.random() * 100;
        double MiniRandom = Random / 100;
        DurabulityEnchantmentLevel.put(player, (double) item.getEnchantmentLevel(Enchantment.DURABILITY));
        if(ArmorList.contains(item)) {
            DurabulityProbability.put(player, (60+(40/(DurabulityEnchantmentLevel.get(player)+1))));
        }else {
            DurabulityProbability.put(player, (100/(DurabulityEnchantmentLevel.get(player)+1)));
        }

        if(DurabulityProbability.get(player) < 1.0) {
            MiniDurabilityMethod(player, MiniRandom, item);
        }

    }

    public void MiniDurabilityMethod(Player player, double MiniRandom, ItemStack item) {
        if(MiniRandom <= DurabulityProbability.get(player)) {

        }
    }
}