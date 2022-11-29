package mc_ability.beadieststar64.mc_ability.Utility;

import mc_ability.beadieststar64.mc_ability.Utility.GUI.PlayerGUI;
import mc_ability.beadieststar64.mc_ability.MC_Ability;
import mc_ability.beadieststar64.mc_ability.Skills.PassiveSkill.PassivePickaxeSkillClass;
import mc_ability.beadieststar64.mc_ability.Utility.Difficulty.Difficulty_Class;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CommandClass implements CommandExecutor, TabCompleter {

    public MC_Ability plugin;

    //改良必須
    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, String[] args) {
        Player player = null;
        if(!(sender instanceof Player)) {
            sender.sendMessage("コンソールからは実行出来ません!!");
            return true;
        }else {

            player = (Player) sender;

            if(command.getName().equalsIgnoreCase("MC_Ability_PassiveSkill")) { //Passive Skill関連
                if(args.length > 0) {
                    if(args[0].equalsIgnoreCase("Show")) {
                        if(args[1].equalsIgnoreCase("Miner")) {
                            if(args[2].equalsIgnoreCase("NowEXP")) {
                                //指定したプレイヤーのPassive SkillのMinerのEXPを表示
                                player.sendMessage(ChatColor.GOLD+""+ChatColor.BOLD+"===== Miner_NOW_EXP =====");
                                player.sendMessage(ChatColor.WHITE+"Minerの経験値は" +
                                        ChatColor.GREEN+""+ChatColor.BOLD+PassivePickaxeSkillClass.GetPlayerMinerEXP.get(player) +
                                        ChatColor.WHITE+"です。");
                                return true;
                            }else if(args[2].equalsIgnoreCase("NowLevel")){
                                //指定したプレイヤーのPassive SkillのMinerのlevelを表示
                                player.sendMessage(ChatColor.GOLD+""+ChatColor.BOLD+"===== Miner_NOW_LEVEL =====");
                                player.sendMessage(ChatColor.WHITE+"Minerのレベルは" +
                                        ChatColor.GREEN+""+ChatColor.BOLD+PassivePickaxeSkillClass.GetPlayerMinerLevel.get(player) +
                                        ChatColor.WHITE+"です。");
                                return true;
                            }else if(args[2].equalsIgnoreCase("NextEXP")) {
                                player.sendMessage(ChatColor.GOLD+""+ChatColor.BOLD+"===== Miner_NEXT_EXP =====");
                                player.sendMessage(ChatColor.WHITE+"次のレベルアップの経験値は" +
                                        ChatColor.GREEN+""+ChatColor.BOLD+PassivePickaxeSkillClass.NextPlayerMinerEXP.get(player) +
                                        ChatColor.WHITE+"です。");
                                player.sendMessage(ChatColor.WHITE+"あと" +
                                        ChatColor.GREEN+""+ChatColor.BOLD+(PassivePickaxeSkillClass.NextPlayerMinerEXP.get(player) - PassivePickaxeSkillClass.GetPlayerMinerEXP.get(player)) +
                                        ChatColor.WHITE+"EXPでレベルアップします。");
                                return true;
                            }else if(args[2].equalsIgnoreCase("NextLevel")) {
                                player.sendMessage(ChatColor.GOLD+""+ChatColor.BOLD+"===== Miner_NEXT_LEVEL =====");
                                player.sendMessage(ChatColor.WHITE+"次のレベルアップの経験値は" +
                                        ChatColor.GREEN+""+ChatColor.BOLD+PassivePickaxeSkillClass.NextPlayerMinerLevel.get(player) +
                                        ChatColor.WHITE+"です。");
                                return true;
                            }
                        }else if(args[1].equalsIgnoreCase("Digger")) {
                        }
                    }else if(command.getName().equalsIgnoreCase("Set")) {
                        if(args[1].equalsIgnoreCase("Miner")) {
                            if(args[2].equalsIgnoreCase("EXP")) {
                                //指定したプレイヤーのPassive SkillのMinerのEXPを設定
                                player.sendMessage(ChatColor.GOLD+""+ChatColor.BOLD+"===== Miner_EXP =====");
                                return true;
                            }else if(args[2].equalsIgnoreCase("Level")){
                                //指定したプレイヤーのPassive SkillのMinerのlevelを表示
                                player.sendMessage(ChatColor.GOLD+""+ChatColor.BOLD+"===== Miner_LEVEL =====");
                                return true;
                            }
                        }
                    }
                }

            }else if(command.getName().equalsIgnoreCase("MC_Ability_OpenGUI")) { //Magic Skill関連
                PlayerGUI playerGUI = new PlayerGUI(plugin);
                if(!playerGUI.inv.containsKey(player)) {
                    playerGUI.SetGUI(player);
                }
                player.openInventory(playerGUI.inv.get(player));
                return true;

            }else if(command.getName().equalsIgnoreCase("MC_Ability_Difficulty")) {
                Difficulty_Class difficulty = new Difficulty_Class();
                if(!difficulty.Difficulty_GUI1.isEmpty()) {
                    difficulty.Setting();
                }
                difficulty.Call(player);
                player.openInventory(difficulty.Difficulty_GUI1);
                return true;

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
                    return Arrays.asList("NowEXP", "NowLevel","NextEXP","NextLevel");
                } else {
                    if ("NowEXP".startsWith(args[0])) {
                        return Collections.singletonList("NowEXP");
                    } else if ("NowLevel".startsWith(args[0])) {
                        return Collections.singletonList("NowLevel");
                    }else if ("NextEXP".startsWith(args[0])) {
                        return Collections.singletonList("NextEXP");
                    }else{
                        return Collections.singletonList("NextLevel");
                    }
                }
            }
        }
        return null;
    }
}