package me.superckl.dpu.common.network;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import cpw.mods.fml.common.network.simpleimpl.IMessage;

@AllArgsConstructor
public class MessageSearchUpdate implements IMessage{

	@Getter
	@Setter
	private String text;
	@Getter
	@Setter
	private boolean onlyActive;

	public MessageSearchUpdate() {}

	@Override
	public void fromBytes(final ByteBuf buf) {
		this.text = new String(buf.readBytes(buf.readInt()).array());
		this.onlyActive = buf.readBoolean();
	}

	@Override
	public void toBytes(final ByteBuf buf) {
		final byte[] bytes = this.text.getBytes();
		buf.writeInt(bytes.length);
		buf.writeBytes(bytes);
		buf.writeBoolean(this.onlyActive);
	}

}
