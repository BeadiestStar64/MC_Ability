package mc_ability.beadieststar64.mc_ability.Original_Item;

import mc_ability.beadieststar64.mc_ability.MC_Ability;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerFlagClass {

    private final Player player;
    private final MC_Ability plugin;

    public PlayerFlagClass(Player player, MC_Ability plugin) {
        this.player = player;
        this.plugin = plugin;
    }

    public void SetMeta(boolean Flag) {
        player.setMetadata("PlayerFlagForGUI",new FixedMetadataValue(plugin, Flag));
    }

    public boolean Get() {
        try{
            return player.getMetadata("PlayerFlagForGUI").get(0).asBoolean();
        }catch(Exception e){
            return false;
        }
    }

    public void Set() {
        SetMeta(true);
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                SetMeta(false);
            }
        };
        runnable.runTaskLater(plugin,3);
    }
}