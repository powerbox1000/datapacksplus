package io.github.powerbox1000.datapacksplus;

import java.util.HashMap;

public class ActionRegistry {
    private static HashMap<String, Action> actionRegistry = new HashMap<>(); // id -> action

    public static void clear() {
        actionRegistry.clear();
    }

    public static void registerAction(String id, Action action) {
        actionRegistry.put(id, action);
    }
    
    public static HashMap<String, Action> getActionRegistry() {
		return actionRegistry;
	}
}
