package me.superckl.dpu.common.network;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import cpw.mods.fml.common.network.simpleimpl.IMessage;

@AllArgsConstructor
public class MessageScrollUpdate implements IMessage{

	@Getter
	@Setter
	private float scroll;

	public MessageScrollUpdate() {}

	@Override
	public void fromBytes(final ByteBuf buf) {
		this.scroll = buf.readFloat();
	}

	@Override
	public void toBytes(final ByteBuf buf) {
		buf.writeFloat(this.scroll);
	}

}
