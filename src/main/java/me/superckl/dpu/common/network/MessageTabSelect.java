package me.superckl.dpu.common.network;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import cpw.mods.fml.common.network.simpleimpl.IMessage;

@AllArgsConstructor
public class MessageTabSelect implements IMessage{

	@Getter
	@Setter
	private boolean onlyActive;

	public MessageTabSelect(){

	}

	@Override
	public void fromBytes(final ByteBuf buf) {
		this.onlyActive = buf.readBoolean();
	}

	@Override
	public void toBytes(final ByteBuf buf) {
		buf.writeBoolean(this.onlyActive);
	}

}
