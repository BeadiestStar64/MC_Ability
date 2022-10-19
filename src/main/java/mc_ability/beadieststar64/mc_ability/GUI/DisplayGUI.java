package mc_ability.beadieststar64.mc_ability.GUI;

import mc_ability.beadieststar64.mc_ability.MC_Ability;
import mc_ability.beadieststar64.mc_ability.Original_Item.OriginalItemClass;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.Arrays;

public class DisplayGUI extends ExtenderForGUI implements Listener {

    static OriginalItemClass originalItemClass;

    public DisplayGUI(MC_Ability plugin) {
        super(plugin);
    }

    @EventHandler
    public void judgeShowGUI(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action[] action = {Action.RIGHT_CLICK_BLOCK, Action.RIGHT_CLICK_AIR};
        ArrayList<Action> actions = new ArrayList<>(Arrays.asList(action));

        PlayerFlagForGUI Flag = new PlayerFlagForGUI(player, plugin);
        if(Flag.Get()) {
            return;
        }
        Flag.Set();

        //ここがうまく動かない...
        if (actions.contains(event.getAction())) {// Checking if they right clicked
            if (player.getInventory().getItemInMainHand().getType() == Material.BOOK) {
                if (player.getInventory().getItemInMainHand().getItemMeta().getDisplayName() == ChatColor.GOLD + "Ability Setting Book") {//最大の問題児 こいつのせいでここから先に進めない(´；ω；`)ｳｩｩ
                    Bukkit.getLogger().info("Test Passed!"); //Passしてない(´；ω；`)ｳｩｩ
                    player.openInventory(PlayerGUI.inv.get(player));
                }
            }
        }
    }

}