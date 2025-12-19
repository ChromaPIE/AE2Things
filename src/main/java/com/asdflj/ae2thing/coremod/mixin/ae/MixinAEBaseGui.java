package com.asdflj.ae2thing.coremod.mixin.ae;

import static com.asdflj.ae2thing.client.render.RenderHelper.canDrawPlus;
import static com.asdflj.ae2thing.client.render.RenderHelper.drawPlus;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.asdflj.ae2thing.AE2Thing;
import com.asdflj.ae2thing.api.AE2ThingAPI;
import com.asdflj.ae2thing.client.render.RenderHelper;

import appeng.api.storage.data.IAEItemStack;
import appeng.client.gui.AEBaseGui;
import appeng.client.gui.slots.VirtualMESlot;
import appeng.client.gui.widgets.GuiScrollbar;

@Mixin(value = AEBaseGui.class)
public abstract class MixinAEBaseGui extends GuiScreen {

    @Shadow(remap = false)
    public abstract int getGuiLeft();

    @Shadow(remap = false)
    public abstract int getGuiTop();

    @Shadow(remap = false)
    protected List<VirtualMESlot> virtualSlots;

    @Shadow(remap = false)
    protected abstract GuiScrollbar getScrollBar();

    @Inject(
        method = "drawGuiContainerBackgroundLayer",
        at = @At(
            value = "INVOKE",
            remap = false,
            target = "Lappeng/client/gui/AEBaseGui;drawBG(IIII)V",
            shift = At.Shift.AFTER))
    @SuppressWarnings({ "unchecked" })
    private void drawPin(float f, int x, int y, CallbackInfo ci) {
        if (!AE2ThingAPI.instance()
            .terminal()
            .isPinTerminal(this)) return;
        if (this.virtualSlots.isEmpty() || AE2ThingAPI.instance()
            .getPinned()
            .isEmpty()) return;
        if (!this.virtualSlots.isEmpty() && this.getScrollBar() != null
            && this.getScrollBar()
                .getCurrentScroll() == 0) {
            VirtualMESlot firstSlot = this.virtualSlots.get(0);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.bindTexture();
            this.drawTexturedModalRect(this.getGuiLeft() + firstSlot.getX() - 1, this.getGuiTop() + 17, 0, 0, 195, 18);
        }
    }

    @Inject(method = "drawVirtualSlot", at = @At("HEAD"), remap = false)
    private void drawVirtualSlotHead(VirtualMESlot slotIn, CallbackInfo ci) {
        if (canDrawPlus && slotIn.getAEStack() != null) {
            IAEItemStack is = slotIn.getAEStack() instanceof IAEItemStack ais ? ais : null;
            if (is != null && is.isCraftable() && is.getStackSize() > 0) {
                int x = slotIn.getX();
                int y = slotIn.getY();
                drawPlus(x, y);
            }
        }
    }

    @Inject(method = "drawVirtualSlot", at = @At("TAIL"), remap = false)
    private void drawVirtualSlotTail(VirtualMESlot slotIn, CallbackInfo ci) {
        RenderHelper.drawPinnedSlot(slotIn, this);
    }

    private void bindTexture() {
        final ResourceLocation loc = AE2Thing.resource("textures/gui/pinned.png");
        Minecraft.getMinecraft()
            .getTextureManager()
            .bindTexture(loc);
    }

    @Inject(method = "handleMouseInput", at = @At("HEAD"))
    public void handleMouseInput(CallbackInfo ci) {
        if (this.getScrollBar() != null) {
            if (!Mouse.isButtonDown(0)) {
                ((AccessorGuiScrollbar) this.getScrollBar()).setIsLatestClickOnScrollbar(false);
            }
        }
    }
}
