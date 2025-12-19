package com.asdflj.ae2thing.api.adapter.terminal.item;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

import com.glodblock.github.common.item.ItemBaseWirelessTerminal;

public class FCBaseTerminalHandler implements ITerminalHandler {

    @Override
    public void openGui(ItemStack item, ITerminalHandler terminal, TerminalItems items, EntityPlayerMP player) {
        if (item == null) return;
        if (item.getItem() instanceof ItemBaseWirelessTerminal t) {
            t.openGui(item, player.worldObj, player, null);
        }
    }
}
