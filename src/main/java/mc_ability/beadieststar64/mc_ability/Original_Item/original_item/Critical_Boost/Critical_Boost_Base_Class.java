package mc_ability.beadieststar64.mc_ability.Original_Item.original_item.Critical_Boost;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Critical_Boost_Base_Class implements Listener {

    @EventHandler
    public void AddDamage(EntityDamageByEntityEvent event) {
        Entity Damager = event.getDamager();

        if(Damager instanceof Player) {
            double Damage = event.getFinalDamage();
            Player player = ((Player) Damager).getPlayer();

            if(player.getFallDistance() > 0.0F && !player.isOnGround() && event.getDamage() > event.getDamage()*0.848) {
                double SetDamage = Damage * 3;
                player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + SetDamage + ChatColor.WHITE + "のダメージを与えた！");
                TextComponent Component = new TextComponent();
                Component.setText(ChatColor.GOLD + "" + ChatColor.BOLD +"クリティカルダメージブースト!!");
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR,Component);
                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1, 1);
                event.setDamage(SetDamage);
                return;
            }
        }
    }
}
