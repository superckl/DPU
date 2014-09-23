package me.superckl.dpu.common.network;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import cpw.mods.fml.common.network.simpleimpl.IMessage;

@AllArgsConstructor
public class MessageNoSearch implements IMessage{

	@Getter
	private int id = -1;

	public MessageNoSearch() {}

	@Override
	public void fromBytes(final ByteBuf buf) {
		this.id = buf.readInt();
	}

	@Override
	public void toBytes(final ByteBuf buf) {
		buf.writeInt(this.id);
	}

}
