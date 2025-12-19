package com.asdflj.ae2thing.api.adapter.terminal.item;

import static com.asdflj.ae2thing.nei.NEI_TH_Config.getConfigValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.asdflj.ae2thing.api.Constants;
import com.asdflj.ae2thing.nei.ButtonConstants;
import com.glodblock.github.common.item.ItemBaseWirelessTerminal;
import com.glodblock.github.common.item.ItemWirelessUltraTerminal;
import com.glodblock.github.util.NameConst;
import com.glodblock.github.util.UltraTerminalModes;

import appeng.util.Platform;

public class FCUltraTerminal implements IItemTerminal {

    public static FCUltraTerminal instance = new FCUltraTerminal();

    @Override
    public List<Class<? extends Item>> getClasses() {
        return Arrays.asList(ItemWirelessUltraTerminal.class);
    }

    @Override
    public boolean supportBaubles() {
        return true;
    }

    @Override
    public List<TerminalItems> getMainInvTerminals() {
        List<TerminalItems> terminal = new ArrayList<>();
        for (int i = 0; i < player().inventory.mainInventory.length; ++i) {
            ItemStack item = player().inventory.getStackInSlot(i);
            terminal.addAll(getTerminalItems(item, i));
        }
        return terminal;
    }

    private List<TerminalItems> getTerminalItems(ItemStack source, int slot) {
        List<TerminalItems> terminal = new ArrayList<>();
        if (source != null && source.getItem() instanceof ItemWirelessUltraTerminal terminalItem) {
            NBTTagCompound tag = this.newNBT();
            tag.setInteger(Constants.SLOT, slot);
            if (getConfigValue(ButtonConstants.ULTRA_TERMINAL_MODE)) {
                for (UltraTerminalModes mode : UltraTerminalModes.values()) {
                    ItemStack t = source.copy();
                    ItemBaseWirelessTerminal.setMode(t, mode);
                    NBTTagCompound data = Platform.openNbtData(t);
                    if (data.hasKey("display")) {
                        terminal.add(
                            new TerminalItems(
                                source,
                                t,
                                t.getDisplayName() + " "
                                    + I18n.format(
                                        NameConst.TT_ULTRA_TERMINAL + "." + ItemBaseWirelessTerminal.getMode(t)),
                                tag));
                    } else {
                        terminal.add(new TerminalItems(source, t, tag));
                    }
                }
            } else {
                terminal.add(new TerminalItems(source, source, tag));
            }
        }
        return terminal;
    }

    @Override
    public List<TerminalItems> getBaublesInvTerminals(IInventory handler) {
        List<TerminalItems> terminal = new ArrayList<>();
        for (int i = 0; i < handler.getSizeInventory(); ++i) {
            ItemStack item = handler.getStackInSlot(i);
            terminal.addAll(getTerminalItems(item, i));
        }
        return terminal;
    }

    @Override
    public void openCraftAmount() {
        appeng.core.sync.network.NetworkHandler.instance.sendToServer(
            new appeng.core.sync.packets.PacketInventoryAction(appeng.helpers.InventoryAction.AUTO_CRAFT, 0, 0));
    }

}
