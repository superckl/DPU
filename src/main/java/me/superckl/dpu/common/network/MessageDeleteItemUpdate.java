package me.superckl.dpu.common.network;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import cpw.mods.fml.common.network.simpleimpl.IMessage;

@AllArgsConstructor
public class MessageDeleteItemUpdate implements IMessage{

	@Getter
	private int selectedIndex;
	@Getter
	private boolean delete;

	public MessageDeleteItemUpdate() {}

	@Override
	public void fromBytes(final ByteBuf buf) {
		this.selectedIndex = buf.readInt();
		this.delete = buf.readBoolean();

	}

	@Override
	public void toBytes(final ByteBuf buf) {
		buf.writeInt(this.selectedIndex).writeBoolean(this.delete);
	}

}
