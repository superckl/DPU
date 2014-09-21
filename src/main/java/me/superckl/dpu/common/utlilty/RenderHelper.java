package me.superckl.dpu.common.utlilty;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

public class RenderHelper {

	public static void drawTexturedRect(final ResourceLocation texture, final double x, final double y, final int u, final int v, final int width, final int height, final int imageWidth, final int imageHeight, final double scale) {
		RenderHelper.drawTexturedRect(texture, x, y, 0D, u, v, width, height, imageWidth, imageHeight, scale);
	}

	public static void drawTexturedRect(final ResourceLocation texture, final double x, final double y, final double z, final int u, final int v, final int width, final int height, final int imageWidth, final int imageHeight, final double scale) {
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		final double minU = (double)u / (double)imageWidth;
		final double maxU = (double)(u + width) / (double)imageWidth;
		final double minV = (double)v / (double)imageHeight;
		final double maxV = (double)(v + height) / (double)imageHeight;
		final Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(x + scale*width, y + scale*height, z, maxU, maxV);
		tessellator.addVertexWithUV(x + scale*width, y, z, maxU, minV);
		tessellator.addVertexWithUV(x, y, z, minU, minV);
		tessellator.addVertexWithUV(x, y + scale*height, z, minU, maxV);
		tessellator.draw();
	}

}
