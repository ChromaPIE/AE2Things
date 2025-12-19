package com.asdflj.ae2thing.api.adapter.terminal.item;

import java.util.Arrays;
import java.util.List;

import net.minecraft.item.Item;

import com.glodblock.github.common.item.ItemWirelessFluidTerminal;
import com.glodblock.github.common.item.ItemWirelessInterfaceTerminal;
import com.glodblock.github.common.item.ItemWirelessLevelTerminal;
import com.glodblock.github.common.item.ItemWirelessPatternTerminal;

import appeng.helpers.InventoryAction;

public class FCBaseItemTerminal implements IItemTerminal {

    public static FCBaseItemTerminal instance = new FCBaseItemTerminal();

    @Override
    public List<Class<? extends Item>> getClasses() {
        return Arrays.asList(
            ItemWirelessLevelTerminal.class,
            ItemWirelessFluidTerminal.class,
            ItemWirelessInterfaceTerminal.class,
            ItemWirelessPatternTerminal.class);
    }

    @Override
    public void openCraftAmount() {
        appeng.core.sync.network.NetworkHandler.instance
            .sendToServer(new appeng.core.sync.packets.PacketInventoryAction(InventoryAction.AUTO_CRAFT, 0, 0));
    }

}
