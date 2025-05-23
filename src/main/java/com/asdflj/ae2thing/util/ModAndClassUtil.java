package com.asdflj.ae2thing.util;

import java.lang.reflect.Field;

import appeng.api.config.ActionItems;
import appeng.api.config.Settings;
import appeng.core.localization.ButtonToolTips;
import cpw.mods.fml.common.Loader;

public final class ModAndClassUtil {

    public static boolean NEE = false;
    public static boolean GT5NH = false;
    public static boolean GT5 = false;
    public static boolean NEI = false;
    public static boolean FTR = false;
    public static boolean BACKPACK = false;
    public static boolean ADVENTURE_BACKPACK = false;
    public static boolean HODGEPODGE = false;
    public static boolean THE = false;
    public static boolean WAILA = false;
    public static boolean WCT = false;
    public static boolean IC2 = false;
    public static boolean NECHAR = false;
    public static boolean NECH = false;
    public static boolean BOTANIA = false;
    public static boolean HBM_AE_ADDON = false;
    public static boolean CORE_MOD = false;
    public static boolean TIC = false;
    public static boolean PH = false;
    public static boolean FIND_IT = false;
    public static boolean BLOCK_RENDER = false;
    public static boolean BAUBLES = false;
    public static boolean isTypeFilter;
    public static boolean isCraftStatus;
    public static boolean isDoubleButton;
    public static boolean isBeSubstitutionsButton;

    @SuppressWarnings("all")
    public static void init() {
        try {
            Class<?> filter = Class.forName("appeng.core.features.registries.ItemDisplayRegistry");
            isTypeFilter = true;
        } catch (ClassNotFoundException e) {
            isTypeFilter = false;
        }
        try {
            Field d = Settings.class.getDeclaredField("CRAFTING_STATUS");
            if (d == null) isCraftStatus = false;
            isCraftStatus = true;
        } catch (NoSuchFieldException e) {
            isCraftStatus = false;
        }
        try {
            Field d = ActionItems.class.getDeclaredField("DOUBLE");
            if (d == null) isDoubleButton = false;
            isDoubleButton = true;
        } catch (NoSuchFieldException e) {
            isDoubleButton = false;
        }
        try {
            Field d = ButtonToolTips.class.getDeclaredField("BeSubstitutionsDescEnabled");
            isBeSubstitutionsButton = true;
        } catch (NoSuchFieldException e) {
            isBeSubstitutionsButton = false;
        }
        if (Loader.isModLoaded("gregtech") && !Loader.isModLoaded("gregapi")) {
            try {
                Class.forName("gregtech.api.recipe.RecipeMap");
                GT5NH = true;
            } catch (ClassNotFoundException e) {
                GT5 = true;
            }
        }
        if (Loader.isModLoaded("thaumicenergistics")) THE = true;
        if (Loader.isModLoaded("Forestry")) FTR = true;
        if (Loader.isModLoaded("Backpack")) BACKPACK = true;
        if (Loader.isModLoaded("adventurebackpack")) ADVENTURE_BACKPACK = true;
        if (Loader.isModLoaded("NotEnoughItems")) NEI = true;
        if (Loader.isModLoaded("hodgepodge")) HODGEPODGE = true;
        if (Loader.isModLoaded("Waila")) WAILA = true;
        if (Loader.isModLoaded("IC2")) IC2 = true;
        if (Loader.isModLoaded("nechar")) NECHAR = true;
        if (Loader.isModLoaded("nech")) NECH = true;
        if (Loader.isModLoaded("neenergistics")) NEE = true;
        if (Loader.isModLoaded("Botania")) BOTANIA = true;
        if (Loader.isModLoaded("dreamcraft")) CORE_MOD = true;
        if (Loader.isModLoaded("hbmaeaddon")) HBM_AE_ADDON = true;
        if (Loader.isModLoaded("TConstruct")) TIC = true;
        if (Loader.isModLoaded("programmablehatches")) PH = true;
        if (Loader.isModLoaded("findit")) FIND_IT = true;
        if (Loader.isModLoaded("ae2wct")) WCT = true;
        if (Loader.isModLoaded("blockrenderer6343")) BLOCK_RENDER = true;
        if (Loader.isModLoaded("Baubles")) BAUBLES = true;
    }
}
