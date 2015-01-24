package me.superckl.dpu.proxy;

import me.superckl.dpu.client.handler.InputHandler;
import me.superckl.dpu.common.network.MessageConfigSync;
import me.superckl.dpu.common.network.MessageDeleteItem;
import me.superckl.dpu.common.network.MessageHandlerConfigSync;
import me.superckl.dpu.common.network.MessageHandlerDeleteItem;
import me.superckl.dpu.common.network.MessageHandlerNoSearch;
import me.superckl.dpu.common.network.MessageNoSearch;
import me.superckl.dpu.common.reference.KeyBindings;
import me.superckl.dpu.common.reference.ModData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class ClientProxy extends CommonProxy{

	private final Minecraft mc = Minecraft.getMinecraft();

	@Override
	public void registerHandlers() {
		super.registerHandlers();
		FMLCommonHandler.instance().bus().register(new InputHandler());
		ModData.ITEM_DELETE_CHANNEL.registerMessage(MessageHandlerDeleteItem.class, MessageDeleteItem.class, 1, Side.CLIENT);
		ModData.CONFIG_SYNC_CHANNEL.registerMessage(MessageHandlerConfigSync.class, MessageConfigSync.class, 0, Side.CLIENT);
		ModData.GUI_UPDATE_CHANNEL.registerMessage(MessageHandlerNoSearch.class, MessageNoSearch.class, 0, Side.CLIENT);
	}

	@Override
	public void registerBindings() {
		KeyBindings.DISABLE_EXCLUDE = new KeyBinding("key.dpu.disableexclude", Keyboard.KEY_X, "key.categories.dpu");
		ClientRegistry.registerKeyBinding(KeyBindings.DISABLE_EXCLUDE);
	}

}
