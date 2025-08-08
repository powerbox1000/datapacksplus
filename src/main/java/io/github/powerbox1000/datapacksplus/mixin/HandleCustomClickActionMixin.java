package io.github.powerbox1000.datapacksplus.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import io.github.powerbox1000.datapacksplus.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.Tag;

@Mixin(MinecraftServer.class)
public abstract class HandleCustomClickActionMixin {
	@Inject(method = "handleCustomClickAction(Lnet/minecraft/resources/ResourceLocation;Ljava/util/Optional;)V", at = @At("HEAD"), cancellable = true)
	private void handleCustomClickAction(ResourceLocation id, Optional<Tag> payload, CallbackInfo info) {
		if (ActionRegistry.getActionRegistry().containsKey(id.toString())) {
			// Get player
			String name = null;
			try {
				name = payload.get().asCompound().get().get("__datapacksplus:playername__").asString().get(); // Added by OpenDialogMixin before sending to client
				DatapacksPlus.LOGGER.info("Executing action " + id + " from player " + name);
			} catch (NullPointerException e) {
				DatapacksPlus.LOGGER.warn("Executing action " + id + " as server (fallback due to missing player name)");
			}

			// Execute command
			try {
				String command = ActionRegistry.getActionRegistry().get(id.toString()).mapCommand(payload.get());
				Commands commandManager = ((MinecraftServer) (Object) this).getCommands();
				CommandSourceStack source;
				if (name != null) {
					source = ((MinecraftServer) (Object) this).getPlayerList().getPlayerByName(name).createCommandSourceStack();
				} else {
					source = ((MinecraftServer) (Object) this).createCommandSourceStack();
				}
				source = source.withPermission(((MinecraftServer) (Object) this).getFunctionCompilationLevel()).withSuppressedOutput();
				commandManager.performPrefixedCommand(source, command);
			} catch (IllegalArgumentException e) {
				DatapacksPlus.LOGGER.error("Error processing payload", e);
			} catch (UnsupportedOperationException e) {
				DatapacksPlus.LOGGER.error("Encountered unsupported payload type in schema", e);
			} catch (Exception e) {
				DatapacksPlus.LOGGER.error("Unexpected error", e);
			}
			info.cancel();
		}
	}
}