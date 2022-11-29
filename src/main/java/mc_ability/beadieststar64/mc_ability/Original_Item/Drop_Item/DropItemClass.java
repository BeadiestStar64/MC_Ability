package mc_ability.beadieststar64.mc_ability.Original_Item.Drop_Item;

import mc_ability.beadieststar64.mc_ability.MC_Ability;
import mc_ability.beadieststar64.mc_ability.Original_Item.ExtenderOriginalItemClass;
import mc_ability.beadieststar64.mc_ability.Original_Item.Un_Create_Item.Enderman_Tears;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class DropItemClass extends ExtenderOriginalItemClass implements Listener {
    public DropItemClass(MC_Ability plugin) {
        super(plugin);
    }

    @EventHandler
    public void EnemyDeath(EntityDeathEvent event) {
        event.getDrops().clear();

        Entity entity = event.getEntity();

        if(entity instanceof Enderman) {
            entity.getLocation().getWorld().dropItem(entity.getLocation(), Enderman_Tears.Enderman_tears);
        }
    }
}
