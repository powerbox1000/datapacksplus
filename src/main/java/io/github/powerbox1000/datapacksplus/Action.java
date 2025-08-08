package io.github.powerbox1000.datapacksplus;

import java.util.Map.Entry;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.nbt.*;

/**
 * Represents a custom action
*/
public class Action {
    public String command;
    public JsonObject payloadSchema;
    
    public Action(String command, JsonObject payloadSchema) {
        this.command = command;
        this.payloadSchema = payloadSchema;

        // TODO validate payloadSchema (not the most important of things, since we also do this when handling custom actions, but still useful for pack devs)
    }

    private String traverseCompound(CompoundTag payload, JsonObject schema, String command) {
        // Traverse the payload tree recursively
        for (Entry<String, JsonElement> entry : schema.entrySet()) {
            String key = entry.getKey();
            JsonObject value = entry.getValue().getAsJsonObject();
            String expectedType = value.get("type").getAsString().toLowerCase();
            
            // Validate existence of key and type
            if (!payload.contains(key)) {
                throw new IllegalArgumentException("Payload is missing key '" + key + "'");
            }
            Tag payloadValue = payload.get(key);
            if (!expectedType.equals(payloadValue.getType().getName().toLowerCase())) {
                throw new IllegalArgumentException("Payload key '" + key + "' is of type " + payloadValue.getId() + " but expected " + expectedType);
            }
            
            switch(expectedType) {
                case "compound":
                    command = traverseCompound(payloadValue.asCompound().get(), value.get("children").getAsJsonObject(), command);
                    break;
                case "byte[]":
                case "int[]":
                case "long[]":
                case "list[]":
                    throw new UnsupportedOperationException("Payload type '" + expectedType + "' is not supported");
                default:
                    // Apply mapping
                    command = command.replace(value.get("mapping").getAsString(), payloadValue.toString());
            }
        }

        return command;
    }

    public String mapCommand(Tag payload) {
        // For now assume compound nbt as root
        return traverseCompound(payload.asCompound().get(), payloadSchema, command);
    }
}