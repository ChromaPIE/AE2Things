package com.asdflj.ae2thing.coremod.mixin.nei;

import static codechicken.nei.NEIClientUtils.translate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.asdflj.ae2thing.api.AE2ThingAPI;
import com.asdflj.ae2thing.api.adapter.terminal.ICraftingTerminalAdapter;
import com.asdflj.ae2thing.client.event.CraftTracking;
import com.asdflj.ae2thing.util.Ae2ReflectClient;

import appeng.api.AEApi;
import appeng.api.config.FuzzyMode;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.storage.data.IDisplayRepo;
import appeng.api.storage.data.IItemList;
import appeng.client.gui.AEBaseGui;
import appeng.client.me.ItemRepo;
import appeng.util.item.AEItemStack;
import codechicken.nei.PositionedStack;
import codechicken.nei.api.IGuiContainerOverlay;
import codechicken.nei.recipe.GuiOverlayButton;

@Mixin(GuiOverlayButton.class)
public abstract class MixinGuiOverlayButton {

    @Shadow(remap = false)
    @Final
    public GuiContainer firstGui;

    @Inject(method = "handleHotkeys", at = @At("TAIL"), remap = false)
    private void handleHotkeys(GuiContainer gui, int mousex, int mousey, Map<String, String> hotkeys,
        CallbackInfoReturnable<Map<String, String>> cir) {
        if (gui instanceof IGuiContainerOverlay gur) {
            if (gur.getFirstScreen() != null && AE2ThingAPI.instance()
                .terminal()
                .isCraftingTerminal(gur.getFirstScreen())) {
                hotkeys.put(translate("gui.request_missing_item.key"), translate("gui.request_missing_item"));
                hotkeys.put(
                    translate("gui.request_missing_item_no_preview.key"),
                    translate("gui.request_missing_item_no_preview"));
            }
        }
    }

    private IItemList<IAEItemStack> items = null;

    @Inject(method = "overlayRecipe", at = @At("TAIL"), remap = false)
    public void overlayRecipe(boolean shift, CallbackInfo ci) {
        GuiOverlayButton here = (GuiOverlayButton) (Object) this;
        if (GuiScreen.isShiftKeyDown() || GuiScreen.isCtrlKeyDown()) {
            moveItems();
        }
        if (!GuiScreen.isCtrlKeyDown() || !(firstGui instanceof AEBaseGui gui)) return;
        final List<PositionedStack> ingredients = here.handlerRef.handler
            .getIngredientStacks(here.handlerRef.recipeIndex);
        IItemList<IAEItemStack> list = null;
        if (AE2ThingAPI.instance()
            .terminal()
            .isCraftingTerminal(gui)) {
            IDisplayRepo repo = AE2ThingAPI.instance()
                .terminal()
                .getCraftingTerminal()
                .get(gui.inventorySlots.getClass())
                .gerRepo(gui);
            if (repo instanceof ItemRepo) {
                list = copyItemList(Ae2ReflectClient.getList((ItemRepo) repo));
            }
        }
        final List<ItemStack> invStacks = firstGui.inventorySlots.inventorySlots.stream()
            .filter(
                s -> s != null && s.getStack() != null
                    && s.getStack().stackSize > 0
                    && s.isItemValid(s.getStack())
                    && s.canTakeStack(firstGui.mc.thePlayer))
            .map(
                s -> s.getStack()
                    .copy())
            .collect(Collectors.toCollection(ArrayList::new));

        out: for (PositionedStack stack : ingredients) {
            Optional<ItemStack> used = invStacks.stream()
                .filter(is -> is.stackSize > 0 && stack.contains(is))
                .findAny();
            if (used.isPresent()) {
                ItemStack is = used.get();
                is.stackSize -= 1;
            } else if (list != null) {
                IAEItemStack item = AEItemStack.create(stack.item);
                IAEItemStack stored = list.findPrecise(item);
                if (stored != null) {
                    if (stored.getStackSize() > 0) {
                        stored.decStackSize(1);
                        continue;
                    }
                }
                for (IAEItemStack is : list.findFuzzy(item, FuzzyMode.IGNORE_ALL)) {
                    if (is.getStackSize() > 0 && stack.contains(is.getItemStack())) {
                        is.decStackSize(1);
                        continue out;
                    }
                }
                if (stored != null && stored.isCraftable()) {
                    addMissingItem(stored);
                    continue;
                }
                for (IAEItemStack is : list.findFuzzy(item, FuzzyMode.IGNORE_ALL)) {
                    if (is.isCraftable() && stack.contains(is.getItemStack())) {
                        addMissingItem(is);
                        break;
                    }
                }
            }
        }
        if (this.items != null) {
            MinecraftForge.EVENT_BUS.post(new CraftTracking(this.items));
        }
    }

    private void moveItems() {
        GuiOverlayButton here = (GuiOverlayButton) (Object) this;
        if (AE2ThingAPI.instance()
            .terminal()
            .isCraftingTerminal(this.firstGui)) {
            ICraftingTerminalAdapter adapter = AE2ThingAPI.instance()
                .terminal()
                .getCraftingTerminal()
                .get(this.firstGui.inventorySlots.getClass());
            adapter.moveItems(this.firstGui, here.handlerRef.handler, here.handlerRef.recipeIndex);
        }
    }

    private void addMissingItem(IAEItemStack stored) {
        if (this.items == null) {
            this.items = AEApi.instance()
                .storage()
                .createPrimitiveItemList();
        }
        IAEItemStack is = stored.copy();
        is.setStackSize(1);
        this.items.add(is);
    }

    private IItemList<IAEItemStack> copyItemList(IItemList<IAEItemStack> list) {
        IItemList<IAEItemStack> result = AEApi.instance()
            .storage()
            .createItemList();
        if (list == null) return null;
        for (IAEItemStack is : list) {
            result.add(is.copy());
        }
        return result;
    }
}
