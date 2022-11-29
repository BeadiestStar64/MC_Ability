package mc_ability.beadieststar64.mc_ability.Skills.PassiveSkill;

import mc_ability.beadieststar64.mc_ability.MC_Ability;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

public class PassivePlayerFlag {
    private final Player player;
    private final MC_Ability plugin;

    public PassivePlayerFlag(Player player, MC_Ability plugin) {
        this.player = player;
        this.plugin = plugin;
    }

    public void SetMeta(boolean Flag) {
        player.setMetadata("PlayerFlag",new FixedMetadataValue(plugin, Flag));
    }

    public boolean Get() {
        try{
            return player.getMetadata("PlayerFlag").get(0).asBoolean();
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