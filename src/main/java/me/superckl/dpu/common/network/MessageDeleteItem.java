package me.superckl.dpu.common.network;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import cpw.mods.fml.common.network.simpleimpl.IMessage;

@AllArgsConstructor
public class MessageDeleteItem implements IMessage{

	@Getter
	private double x,y,z;

	public MessageDeleteItem() {}

	@Override
	public void fromBytes(final ByteBuf buf) {
		this.x = buf.readDouble();
		this.y = buf.readDouble();
		this.z = buf.readDouble();
	}

	@Override
	public void toBytes(final ByteBuf buf) {
		buf.writeDouble(this.x).writeDouble(this.y).writeDouble(this.z);

	}

}
