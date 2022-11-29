package mc_ability.beadieststar64.mc_ability.Skills.ActiveSkill;

import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class Comet_Glitter_Class implements Listener {

    @EventHandler
    public void PlayerDie(PlayerDeathEvent event) {

        Entity Killer = event.getEntity().getLastDamageCause().getEntity();
        Player player = event.getPlayer();

        Bukkit.getLogger().info(Killer.toString());

        if (event.getEntity().getLastDamageCause() instanceof EntityDamageByEntityEvent) { // Check if the last damage cause event is instanceof EntityDamageByEntityEvent
            EntityDamageByEntityEvent lastDamageCause = (EntityDamageByEntityEvent)event.getEntity().getLastDamageCause();
            if (lastDamageCause.getDamager() instanceof Arrow) { // Check if the damager is an arrow
                Arrow arrow = (Arrow)lastDamageCause.getDamager();
                if (arrow.getShooter() instanceof Skeleton) { // Check if the shooter is skeleton
                    //ここに処理を入れる
                    event.setDeathMessage("Killed by skeleton"); // Modify death message
                }
            }else{
                Entity Attacker = lastDamageCause.getDamager();

            }
        }
    }
}