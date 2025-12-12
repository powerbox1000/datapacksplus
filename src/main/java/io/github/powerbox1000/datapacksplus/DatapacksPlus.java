package io.github.powerbox1000.datapacksplus;

import net.fabricmc.api.ModInitializer;
import java.io.InputStream;
import java.io.InputStreamReader;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatapacksPlus implements ModInitializer {
	public static final String MOD_ID = "datapacksplus";
	public static final String ACTION_REGISTRY_ID = "action_registry";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
			@Override
			public Identifier getFabricId() {
				return Identifier.fromNamespaceAndPath(MOD_ID, ACTION_REGISTRY_ID);
			}
		
			@Override
			public void onResourceManagerReload(ResourceManager manager) {
				ActionRegistry.clear();
				for(Identifier id : manager.listResources(ACTION_REGISTRY_ID, path -> path.toString().endsWith(".json")).keySet()) {
					try(InputStream stream = manager.getResource(id).get().open()) {
						// Expect string command and object payloadSchema
						JsonObject data = JsonParser.parseReader(new InputStreamReader(stream)).getAsJsonObject();
						String command = data.get("command").getAsString();
						JsonObject payloadSchema = data.get("payloadSchema").getAsJsonObject();
						final String idstr = id.toString().replace(ACTION_REGISTRY_ID + "/", "").replace(".json", "");
						ActionRegistry.registerAction(idstr, new Action(command, payloadSchema)); // Action class validates payloadSchema on its own
						LOGGER.info("Loaded action json " + idstr);
					} catch(Exception e) {
						LOGGER.error("Error occurred while loading resource json " + id.toString(), e);
					}
				}
			}
		});
		LOGGER.info("Datapacks+ initialized!");
	}
}