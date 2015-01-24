package me.superckl.dpu.common.utlilty;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import lombok.Cleanup;
import lombok.Getter;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.event.HoverEvent.Action;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public class VersionChecker {

	@Getter
	private final AtomicBoolean done = new AtomicBoolean(false);
	@Getter
	private final AtomicBoolean needsUpdates = new AtomicBoolean(false);
	@Getter
	private volatile String newVersion = "";
	@Getter
	private volatile String updateURL = "";
	@Getter
	private final Set<UUID> notified = new HashSet<UUID>();

	private VersionChecker(){}

	public static VersionChecker start(final String modID, final String modVersion, final String mcVersion){
		final VersionChecker checker = new VersionChecker();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					final String[] split = modVersion.split("[.]");
					final int major = Integer.parseInt(split[0]), minor = Integer.parseInt(split[1]), build = Integer.parseInt(split[2]);
					final URL url = new URL(StringHelper.build("http://bot.notenoughmods.com/", mcVersion, ".json"));
					final URLConnection conn = url.openConnection();
					conn.addRequestProperty("User-Agent", modID.concat(" version check"));
					conn.setDoOutput(true);
					@Cleanup
					final
					BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					final JsonArray array = (JsonArray) new JsonParser().parse(reader.readLine());
					boolean found = false;
					for(final JsonElement element:array){
						final JsonObject obj = (JsonObject) element;
						if(obj.get("modid").getAsString().equals(modID)){
							final String nVersion = obj.get("version").getAsString();
							final String[] split2 = nVersion.split("[.]");
							final int major2 = Integer.parseInt(split2[0]), minor2 = Integer.parseInt(split2[1]), build2 = Integer.parseInt(split2[2]);
							if(major2 < major || major2 == major && minor2 < minor || major2 == major && minor2 == minor && build2 <= build)
								break;
							checker.needsUpdates.set(true);
							checker.newVersion = nVersion;
							checker.updateURL = obj.get("longurl").getAsString();
							LogHelper.info(StringHelper.build("A new version of ", modID, " is available: ", nVersion));
							found = true;
							break;
						}
					}
					if(!found)
						LogHelper.info("No update found.");
					checker.done.set(true);
				} catch (final Exception e) {
					checker.done.set(true);
					checker.needsUpdates.set(false);
					LogHelper.error("Failed to perform a version check!");
					e.printStackTrace();
				}

			}
		}).start();
		return checker;
	}

	@SubscribeEvent(receiveCanceled = false)
	public void onPlayerLogin(final PlayerLoggedInEvent e){
		if(e.player.worldObj.isRemote || !this.done.get() || !this.needsUpdates.get() || this.notified.contains(e.player.getGameProfile().getId()))
			return;
		final ChatComponentTranslation chat = new ChatComponentTranslation("msg.update.text", this.newVersion);
		chat.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.AQUA).setChatHoverEvent(new HoverEvent(Action.SHOW_TEXT, new ChatComponentTranslation("msg.linkopen.text")))
				.setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, this.updateURL)));
		e.player.addChatMessage(chat);
		this.notified.add(e.player.getGameProfile().getId());
	}

}
