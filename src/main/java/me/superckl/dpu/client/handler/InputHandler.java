package me.superckl.dpu.client.handler;

import lombok.experimental.ExtensionMethod;
import me.superckl.dpu.common.network.MessageDisableExcludifier;
import me.superckl.dpu.common.reference.KeyBindings;
import me.superckl.dpu.common.reference.ModData;
import me.superckl.dpu.common.utlilty.ItemStackHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;

@ExtensionMethod(ItemStackHelper.class)
public class InputHandler{

	private final Minecraft mc = Minecraft.getMinecraft();

	@SubscribeEvent
	public void onKeyInput(final KeyInputEvent e){
		if(KeyBindings.DISABLE_EXCLUDE.isPressed()){
			final ItemStack stack = this.mc.thePlayer.getHeldItem();
			if(!stack.ensureExcludeNBT())
				return;
			final NBTTagCompound comp = stack.getTagCompound();
			comp.setBoolean("disabled", !comp.getBoolean("disabled"));
			ModData.ITEM_DELETE_CHANNEL.sendToServer(new MessageDisableExcludifier());
		}
	}

}
