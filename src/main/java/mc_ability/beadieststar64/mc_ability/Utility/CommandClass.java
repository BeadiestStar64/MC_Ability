package mc_ability.beadieststar64.mc_ability.Utility;

import mc_ability.beadieststar64.mc_ability.MC_Ability;
import mc_ability.beadieststar64.mc_ability.PassiveSkill.PassivePickaxeSkillClass;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandClass implements CommandExecutor, TabCompleter {

    public MC_Ability plugin;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, String[] args) {
        Player player = (Player) sender;
        if(command.getName().equalsIgnoreCase("MC_Ability_PassiveSkill")) {
            if(args.length > 0) {
                if(args[0].equalsIgnoreCase("Show")) {
                    if(args[1].equalsIgnoreCase("Miner")) {
                        if(args[2].equalsIgnoreCase("EXP")) {
                            //指定したプレイヤーのPassive SkillのMinerのEXPを表示
                            player.sendMessage(ChatColor.GOLD+""+ChatColor.BOLD+"===== Miner_EXP =====");
                            player.sendMessage(ChatColor.WHITE+"Minerの経験値は" +
                                    ChatColor.GREEN+""+ChatColor.BOLD+PassivePickaxeSkillClass.GetPlayerMinerEXP +
                                    ChatColor.WHITE+"です。");
                            return true;
                        }else if(args[2].equalsIgnoreCase("Level")){
                            //指定したプレイヤーのPassive SkillのMinerのlevelを表示
                            player.sendMessage(ChatColor.GOLD+""+ChatColor.BOLD+"===== Miner_LEVEL =====");
                            player.sendMessage(ChatColor.WHITE+"Minerのレベルは" +
                                    ChatColor.GREEN+""+ChatColor.BOLD+PassivePickaxeSkillClass.GetPlayerMinerLevel +
                                    ChatColor.WHITE+"です。");
                        }
                    }else if(args[1].equalsIgnoreCase("Digger")) {

                    }
                }
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(command.getName().equalsIgnoreCase("MC_Ability_PassiveSkill")) {
            if (args.length == 1) {
                if (args[0].length() == 0) {
                    return Arrays.asList("Show", "Set", "Delete");
                }
            } else if (args.length == 2) {
                if (args[1].length() == 0) {
                    return Arrays.asList("Miner", "Digger");
                } else {
                    if ("Miner".startsWith(args[0])) {
                        return Collections.singletonList("Miner");
                    } else if ("EXP".startsWith(args[0])) {
                        return Collections.singletonList("EXP");
                    }
                }
            } else if (args.length == 3) {
                if (args[2].length() == 0) {
                    return Arrays.asList("EXP", "Level");
                } else {
                    if ("EXP".startsWith(args[0])) {
                        return Collections.singletonList("EXP");
                    } else if ("Level".startsWith(args[0])) {
                        return Collections.singletonList("Level");
                    }
                }
            }
        }
        return null;
    }
}