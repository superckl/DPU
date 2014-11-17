package me.superckl.dpu.common.network;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import cpw.mods.fml.common.network.simpleimpl.IMessage;

@AllArgsConstructor
public class MessageConfigSync implements IMessage{

	@Getter
	@Setter(onParam = @_(@NonNull))
	private NBTTagCompound tag;

	public MessageConfigSync() {}

	@Override
	public void fromBytes(final ByteBuf buf) {
		final PacketBuffer buffer = new PacketBuffer(buf);
		try {
			this.tag = buffer.readNBTTagCompoundFromBuffer();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void toBytes(final ByteBuf buf) {
		final PacketBuffer buffer = new PacketBuffer(buf);
		try {
			buffer.writeNBTTagCompoundToBuffer(this.tag);
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

}
