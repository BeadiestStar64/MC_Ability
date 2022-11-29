package mc_ability.beadieststar64.mc_ability.Utility.Difficulty;

import mc_ability.beadieststar64.mc_ability.MC_Ability;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

import static mc_ability.beadieststar64.mc_ability.PassiveSkill.ExtendedPassiveSkill.plugin;

public class Difficulty_Class implements Listener {

    public static Inventory Difficulty_GUI1 = Bukkit.createInventory(null, 27, "難易度選択");

    public void Setting() {
        EasyGUI();
        NormalGUI();
        HardGUI();
        HellGUI();
        Lunatic();
    }

    public void Call(Player player) {
        Inventory Difficulty_GUI = Bukkit.createInventory(player, 27, "難易度選択");
    }

    public void EasyGUI() {
        ItemStack EasyMode = new ItemStack(Material.POPPY);
        ItemMeta EasyMeta = EasyMode.getItemMeta();

        if(plugin.Difficulty.contains("easy")) {
            EasyMeta.setDisplayName(ChatColor.WHITE + "難易度: " + ChatColor.GREEN + "" + ChatColor.BOLD + "EASY" +
                    ChatColor.WHITE + "" + ChatColor.RED + "" + ChatColor.BOLD + "(選択中)");
        }else {
            EasyMeta.setDisplayName(ChatColor.WHITE + "難易度: " + ChatColor.GREEN + "" + ChatColor.BOLD + "EASY");
        }

        List<String> Lore = new ArrayList<>();
        Lore.add(ChatColor.WHITE + "マイクラを始めたばかりの方におすすめの難易度です。");
        Lore.add("");
        Lore.add(ChatColor.WHITE + "<推奨マイクラ難易度> " + ChatColor.GREEN  + "" + ChatColor.BOLD + "イージー" );
        Lore.add("");
        Lore.add(ChatColor.AQUA+"Create by " + ChatColor.GOLD+""+ChatColor.BOLD+"MC_Ability");
        EasyMeta.setLore(Lore);

        EasyMode.setItemMeta(EasyMeta);

        if(plugin.Difficulty.contains("easy")) {
            EasyMode.addUnsafeEnchantment(Enchantment.DURABILITY, 5);
            EasyMode.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        Difficulty_GUI1.setItem(0, EasyMode);

    }

    public void NormalGUI() {
        ItemStack NormalMode = new ItemStack(Material.IRON_PICKAXE);
        ItemMeta NormalMeta = NormalMode.getItemMeta();

        if(plugin.Difficulty.contains("normal")) {
            NormalMeta.setDisplayName(ChatColor.WHITE + "難易度: " + ChatColor.AQUA + "" + ChatColor.BOLD + "NORMAL" +
                    ChatColor.WHITE + "" + ChatColor.RED + "" + ChatColor.BOLD + "(選択中)");
        }else {
            NormalMeta.setDisplayName(ChatColor.WHITE + "難易度: " + ChatColor.AQUA + "" + ChatColor.BOLD + "NORMAL");
        }

        List<String> Lore = new ArrayList<>();
        Lore.add(ChatColor.WHITE + "マイクラ初心者～中級者におすすめの設定です。");
        Lore.add("");
        Lore.add(ChatColor.RED + "注意！");
        Lore.add(ChatColor.WHITE + "マルチでNORMAL以上を選択する際は、必ずWorld Guardを有効にしてください。");
        Lore.add("");
        Lore.add(ChatColor.WHITE + "<推奨マイクラ難易度> " + ChatColor.AQUA  + "" + ChatColor.BOLD + "ノーマル～ハード" );
        Lore.add("");
        Lore.add(ChatColor.AQUA+"Create by " + ChatColor.GOLD+""+ChatColor.BOLD+"MC_Ability");
        NormalMeta.setLore(Lore);

        NormalMode.setItemMeta(NormalMeta);

        if(plugin.Difficulty.contains("normal")) {
            NormalMode.addUnsafeEnchantment(Enchantment.DURABILITY, 5);
            NormalMode.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        Difficulty_GUI1.setItem(2, NormalMode);
    }

    public void HardGUI() {
        ItemStack HardMode = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta HardMeta = HardMode.getItemMeta();

        if(plugin.Difficulty.contains("hard")) {
            HardMeta.setDisplayName(ChatColor.WHITE + "難易度: " + ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "HARD" +
                    ChatColor.WHITE + "" + ChatColor.RED + "" + ChatColor.BOLD + "(選択中)");
        }else {
            HardMeta.setDisplayName(ChatColor.WHITE + "難易度: " + ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "HARD");
        }

        List<String> Lore = new ArrayList<>();
        Lore.add(ChatColor.WHITE + "マイクラ中級者～上級者におすすめの設定です。");
        Lore.add("");
        Lore.add(ChatColor.WHITE + "<推奨マイクラ難易度> " + ChatColor.LIGHT_PURPLE  + "" + ChatColor.BOLD + "ノーマル～ハードコア" );
        Lore.add("");
        Lore.add(ChatColor.AQUA+"Create by " + ChatColor.GOLD+""+ChatColor.BOLD+"MC_Ability");
        HardMeta.setLore(Lore);

        HardMode.setItemMeta(HardMeta);

        if(plugin.Difficulty.contains("hard")) {
            HardMode.addUnsafeEnchantment(Enchantment.DURABILITY, 5);
            HardMode.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        Difficulty_GUI1.setItem(4, HardMode);
    }

    public void HellGUI() {
        ItemStack HellMode = new ItemStack(Material.SKELETON_SKULL);
        ItemMeta HellMeta = HellMode.getItemMeta();

        if(plugin.Difficulty.contains("hell")) {
            HellMeta.setDisplayName(ChatColor.WHITE + "難易度: " + ChatColor.RED + "" + ChatColor.BOLD + "HELL" +
                    ChatColor.WHITE + "" + ChatColor.RED + "" + ChatColor.BOLD + "(選択中)");
        }else {
            HellMeta.setDisplayName(ChatColor.WHITE + "難易度: " + ChatColor.RED + "" + ChatColor.BOLD + "HELL");
        }

        List<String> Lore = new ArrayList<>();
        Lore.add(ChatColor.WHITE + "マイクラ上級者におすすめの設定です。");
        Lore.add("");
        Lore.add(ChatColor.WHITE + "<推奨マイクラ難易度> " + ChatColor.RED  + "" + ChatColor.BOLD + "ノーマル～ハードコア" );
        Lore.add("");
        Lore.add(ChatColor.AQUA+"Create by " + ChatColor.GOLD+""+ChatColor.BOLD+"MC_Ability");
        HellMeta.setLore(Lore);

        HellMode.setItemMeta(HellMeta);

        if(plugin.Difficulty.contains("hell")) {
            HellMode.addUnsafeEnchantment(Enchantment.DURABILITY, 5);
            HellMode.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        Difficulty_GUI1.setItem(6, HellMode);
    }

    public void Lunatic() {
        ItemStack LunaticMode = new ItemStack(Material.WITHER_SKELETON_SKULL);
        ItemMeta LunaticMeta = LunaticMode.getItemMeta();

        if(plugin.Difficulty.contains("lunatic")) {
            LunaticMeta.setDisplayName(ChatColor.WHITE + "難易度: " + ChatColor.GRAY + "" + ChatColor.BOLD + "LUNATIC" +
                    ChatColor.WHITE + "" + ChatColor.RED + "" + ChatColor.BOLD + "(選択中)");
        }else {
            LunaticMeta.setDisplayName(ChatColor.WHITE + "難易度: " + ChatColor.GRAY + "" + ChatColor.BOLD + "LUNATIC");
        }

        List<String> Lore = new ArrayList<>();
        Lore.add(ChatColor.WHITE + "マイクラ上級者、又はYouTuber向けです。");
        Lore.add("");
        Lore.add(ChatColor.RED + "" + ChatColor.BOLD + "！注意！ " + ChatColor.WHITE + "難易度LUNATICでは、全ての");
        Lore.add(ChatColor.WHITE + "ツールスキル・マジックスキルが" + ChatColor.RED + "" + ChatColor.BOLD + "無効化" + ChatColor.WHITE + "されます。");
        Lore.add("");
        Lore.add(ChatColor.WHITE + "<推奨マイクラ難易度> " + ChatColor.GRAY  + "" + ChatColor.BOLD + "ハード～ハードコア" );
        Lore.add("");
        Lore.add(ChatColor.AQUA+"Create by " + ChatColor.GOLD+""+ChatColor.BOLD+"MC_Ability");
        LunaticMeta.setLore(Lore);

        LunaticMode.setItemMeta(LunaticMeta);

        if(plugin.Difficulty.contains("lunatic")) {
            LunaticMode.addUnsafeEnchantment(Enchantment.DURABILITY, 5);
            LunaticMode.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        Difficulty_GUI1.setItem(8, LunaticMode);
    }


    @EventHandler
    public void SelectDifficulty(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (!event.getInventory().equals(Difficulty_GUI1)) {
            return;
        }
        if (event.getCurrentItem() == null) {
            return;
        }
        if (event.getCurrentItem().getItemMeta() == null) {
            return;
        }
        if (event.getCurrentItem().getItemMeta().getDisplayName() == null) {
            return;
        }

        event.setCancelled(true);

        if(event.getInventory() == Difficulty_GUI1) {
            switch (event.getSlot()) {
                case 0:
                    player.sendMessage(ChatColor.WHITE + "MC_Abilityの難易度を" + ChatColor.GREEN + "" + ChatColor.BOLD + "EASY" + ChatColor.WHITE + "に変更しました");
                    plugin.Difficulty = "easy";
                    Bukkit.getLogger().info("難易度が" + plugin.Difficulty + "に変更されました");
                    plugin.SaveDifficulty();
                    player.closeInventory();
                    break;

                case 2:
                    player.sendMessage(ChatColor.WHITE + "MC_Abilityの難易度を" + ChatColor.AQUA + "" + ChatColor.BOLD + "NORMAL" + ChatColor.WHITE + "に変更しました");
                    plugin.Difficulty = "normal";
                    Bukkit.getLogger().info("難易度が" + plugin.Difficulty + "に変更されました");
                    plugin.SaveDifficulty();
                    player.closeInventory();
                    break;

                case 4:
                    player.sendMessage(ChatColor.WHITE + "MC_Abilityの難易度を" + ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "HARD" + ChatColor.WHITE + "に変更しました");
                    plugin.Difficulty = "hard";
                    Bukkit.getLogger().info("難易度が" + plugin.Difficulty + "に変更されました");
                    plugin.SaveDifficulty();
                    player.closeInventory();
                    break;

                case 6:
                    player.sendMessage(ChatColor.WHITE + "MC_Abilityの難易度を" + ChatColor.RED + "" + ChatColor.BOLD + "HELL" + ChatColor.WHITE + "に変更しました");
                    plugin.Difficulty = "hell";
                    Bukkit.getLogger().info("難易度が" + plugin.Difficulty + "に変更されました");
                    plugin.SaveDifficulty();
                    player.closeInventory();
                    break;

                case 8:
                    player.sendMessage(ChatColor.WHITE + "MC_Abilityの難易度を" + ChatColor.GRAY + "" + ChatColor.BOLD + "LUNATIC" + ChatColor.WHITE + "に変更しました");
                    plugin.Difficulty = "lunatic";
                    Bukkit.getLogger().info("難易度が" + plugin.Difficulty + "に変更されました");
                    plugin.SaveDifficulty();
                    player.closeInventory();
                    break;
            }
        }
    }
}
