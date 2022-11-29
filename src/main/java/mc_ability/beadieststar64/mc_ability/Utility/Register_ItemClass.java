package mc_ability.beadieststar64.mc_ability.Utility;

import mc_ability.beadieststar64.mc_ability.Utility.GUI.PlayerGUI;
import mc_ability.beadieststar64.mc_ability.Skills.MagicSkill.Aegis_Barrier.Aegis_Barrier_LUNA;
import mc_ability.beadieststar64.mc_ability.Skills.MagicSkill.Aegis_Barrier.Aegis_Barrier_Lv1;
import mc_ability.beadieststar64.mc_ability.Original_Item.OriginalItemClass;
import mc_ability.beadieststar64.mc_ability.Original_Item.Un_Create_Item.MainProcess;
import mc_ability.beadieststar64.mc_ability.Utility.Difficulty.Difficulty_Class;

import static mc_ability.beadieststar64.mc_ability.MC_Ability.plugins;

public class Register_ItemClass {

    public void Register_Item() {
        OriginalItemClass originalItemClass = new OriginalItemClass(plugins);
        originalItemClass.OriginalItmCreate();

        PlayerGUI playerGUI = new PlayerGUI(plugins);
        playerGUI.CreateGUI();

        MainProcess main = new MainProcess();
        main.CreateItems();

        Difficulty_Class difficulty_class = new Difficulty_Class();
        difficulty_class.Setting();

        Aegis_Barrier_Lv1 Aegis_Barrier = new Aegis_Barrier_Lv1();
        Aegis_Barrier.AegisBarreier_lv1();

        Aegis_Barrier_LUNA Aegis_Barrier_L = new Aegis_Barrier_LUNA();
        Aegis_Barrier_L.AegisBarreier_LUNA();
    }
}
