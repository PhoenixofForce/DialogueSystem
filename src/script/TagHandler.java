package script;

import java.util.HashMap;
import java.util.Map;

public class TagHandler {

	private Map<String, Integer> integers;
	private Map<String, String> strings;

	public TagHandler() {
		integers = new HashMap<>();
		strings = new HashMap<>();
	}

	/**
	 * @param key the key of the value to be returned
	 * @return the value of the given key in the game save state
	 **/
	public int getInt(String key) {
		return integers.getOrDefault(key, 0);
	}

	public String getString(String key) {
		return strings.getOrDefault(key, "");
	}

	/**
	 * @param key   the key to be set in the saveGame
	 * @param value the value that should be assigned to the key in the current saveGame
	 **/
	public void setValue(String key, int value) {
		integers.put(key, value);
	}

	public void setValue(String key, String value) {
		strings.put(key, value);
	}

	public boolean hasKey(String key) {
		return integers.containsKey(key);
	}

	/**
	 * removes all values from the loaded saveGame
	 **/
	public void clearValues() {
		integers.clear();
		strings.clear();
	}
}
