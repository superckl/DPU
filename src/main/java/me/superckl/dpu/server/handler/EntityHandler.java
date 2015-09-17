package me.superckl.dpu.server.handler;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import lombok.experimental.ExtensionMethod;
import me.superckl.dpu.Config;
import me.superckl.dpu.DPUMod;
import me.superckl.dpu.common.network.MessageDeleteItem;
import me.superckl.dpu.common.reference.ModData;
import me.superckl.dpu.common.utlilty.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;

@ExtensionMethod(ItemStackHelper.class)
public class EntityHandler {

	private final Map<UUID, Set<UUID>> stash = new ConcurrentHashMap<UUID, Set<UUID>>();
	private final Timer timer = new Timer();

	@SubscribeEvent
	public void onEntityPickupItem(final EntityItemPickupEvent e){
		if(e.entityPlayer == null)
			return;
		final Config c = DPUMod.getInstance().getConfig();
		final int delay = c.getTrackTimer();
		synchronized (this.stash) {
			if(delay > 0 && this.stash.containsKey(e.item.getUniqueID())){
				final Set<UUID> uuids = this.stash.get(e.item.getUniqueID());
				if(uuids.contains(e.entityPlayer.getGameProfile().getId())){
					e.setCanceled(true);
					return;
				}
			}
		}
		final ItemStack item = e.item.getEntityItem();
		for(int j = 0; j < (c.isHotbarOnly() ? 9:e.entityPlayer.inventory.mainInventory.length); j++){
			final ItemStack stack = e.entityPlayer.inventory.mainInventory[j];
			if(stack.ensureExcludeNBT()){
				final NBTTagCompound comp = stack.getTagCompound();
				if(comp.getBoolean("disabled"))
					continue;
				final NBTTagList list = comp.getTagList("items", NBT.TAG_COMPOUND);
				for(int i = 0; i < list.tagCount(); i++){
					final ItemStack dpuStack = ItemStack.loadItemStackFromNBT(list.getCompoundTagAt(i));
					if(dpuStack.isItemEqual(item)){
						e.setCanceled(true);
						if(c.isShortenLife()){
							final int life = c.getShortenLifeTo();
							if(e.item.lifespan-e.item.age > life)
								e.item.age = e.item.lifespan-life;
						}
						if(c.isAllowDelete() && list.getCompoundTagAt(i).getBoolean("dpuDelete")){
							e.item.setDead();
							ModData.ITEM_DELETE_CHANNEL.sendToAllAround(new MessageDeleteItem(e.item.posX, e.item.posY, e.item.posZ), new TargetPoint(e.entityPlayer.dimension, e.item.posX, e.item.posY, e.item.posZ, 20D));
						}else if(delay > 0)
							this.scheduleDelay(e.item.getUniqueID(), e.entityPlayer.getGameProfile().getId(), delay);
						return;
					}
				}
			}
		}
	}

	public void scheduleDelay(final UUID item, final UUID player, final int delay){
		synchronized(this.stash){
			if(this.stash.containsKey(item)){
				final Set<UUID> uuids = this.stash.get(item);
				uuids.add(player);
			}else{
				final Set<UUID> uuids = Collections.synchronizedSet(new HashSet<UUID>());
				uuids.add(player);
				this.stash.put(item, uuids);
			}
		}

		this.timer.schedule(new TimerTask() {

			@Override
			public void run() {
				synchronized (EntityHandler.this.stash) {
					if(EntityHandler.this.stash.containsKey(item)){
						final Set<UUID> uuids = EntityHandler.this.stash.get(item);
						uuids.remove(player);
						if(uuids.isEmpty())
							EntityHandler.this.stash.remove(item);

					}
				}
			}
		}, (long) (delay/20F*1000F));
	}

}
