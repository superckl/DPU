package me.superckl.dpu.common.network;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.superckl.dpu.common.utlilty.LogHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import cpw.mods.fml.common.network.simpleimpl.IMessage;

@AllArgsConstructor
public class MessageItemSelect implements IMessage{

	@Getter
	private ItemStack stack;
	@Getter
	private int selectedIndex;
	@Getter
	private boolean added;

	public MessageItemSelect() {}

	@Override
	public void fromBytes(final ByteBuf buf) {
		LogHelper.info("beginning read");
		final PacketBuffer buffer = new PacketBuffer(buf);
		try {
			this.stack = buffer.readItemStackFromBuffer();
			this.selectedIndex = buffer.readInt();
			this.added = buffer.readBoolean();
		} catch (final IOException e) {
			e.printStackTrace();
		}
		LogHelper.info("finishing read");
	}

	@Override
	public void toBytes(final ByteBuf buf) {
		LogHelper.info("Beginning write");
		final PacketBuffer buffer = new PacketBuffer(buf);
		try {
			buffer.writeItemStackToBuffer(this.stack);
			buffer.writeInt(this.selectedIndex);
			buffer.writeBoolean(this.added);
		} catch (final IOException e) {
			e.printStackTrace();
		}
		LogHelper.info("Finishing write");
	}

}
