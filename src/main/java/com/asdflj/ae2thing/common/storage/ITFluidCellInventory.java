package com.asdflj.ae2thing.common.storage;

import java.util.List;

import net.minecraft.inventory.IInventory;

import com.asdflj.ae2thing.common.storage.infinityCell.BaseInventory;

import appeng.api.storage.ICellInventory;
import appeng.api.storage.data.IAEFluidStack;

public interface ITFluidCellInventory extends ICellInventory<IAEFluidStack>, BaseInventory {

    double getIdleDrain();

    IInventory getConfigInventory();

    IInventory getUpgradesInventory();

    boolean canHoldNewFluid();

    long getStoredFluidCount();

    long getRemainingFluidCount();

    long getRemainingFluidTypes();

    int getUnusedFluidCount();

    long getStoredFluidTypes();

    long getTotalFluidTypes();

    List<IAEFluidStack> getContents();

    default long getRemainingFluidCountDist(IAEFluidStack l) {
        return 0;
    }

    @Override
    default boolean canHoldNewItem() {
        return canHoldNewFluid();
    }

    @Override
    default long getStoredItemCount() {
        return getStoredFluidCount();
    }

    @Override
    default long getRemainingItemCount() {
        return getRemainingFluidCount();
    }

    @Override
    default long getRemainingItemTypes() {
        return getRemainingFluidTypes();
    }

    @Override
    default int getUnusedItemCount() {
        return getUnusedFluidCount();
    }

    @Override
    default long getStoredItemTypes() {
        return getStoredFluidTypes();
    }

    @Override
    default long getTotalItemTypes() {
        return getTotalFluidTypes();
    }

    @Override
    default long getRemainingItemsCountDist(IAEFluidStack l) {
        return getRemainingFluidCountDist(l);
    }
}
