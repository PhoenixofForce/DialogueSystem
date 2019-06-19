package script;

import java.util.HashMap;
import java.util.Map;

public class TagHandler {

	private Map<String, Integer> values;

	public TagHandler() {
		values = new HashMap<>();
	}

	/**
	 * @param key the key of the value to be returned
	 * @return the value of the given key in the game save state
	 **/
	public int getValue(String key) {
		return values.getOrDefault(key, 0);
	}

	/**
	 * searches for all set values in the saveGame, where the key contains the given string and where the value is one of the given values or anything when given no values
	 *
	 * @param key   the String that every key has to contain
	 * @param value that the found values may be, or nothing to allow every value
	 * @return the amount of keys found in the save state
	 **/
	public int getKeyAmount(String key, int... value) {
		String[] keySet = values.keySet().toArray(new String[0]);
		int amount = 0;
		for (String s : keySet) {
			if (s.contains(key)) {
				if (value.length == 0) {
					amount++;
				} else {
					for (int v : value) {
						if (v == values.get(s)) amount++;
					}
				}
			}
		}
		return amount;
	}

	/**
	 * @param key   the key to be set in the saveGame
	 * @param value the value that should be assigned to the key in the current saveGame
	 **/
	public void setValue(String key, int value) {
		values.put(key, value);
	}

	public boolean hasKey(String key) {
		return values.containsKey(key);
	}

	/**
	 * removes all values from the loaded saveGame
	 **/
	public void clearValues() {
		values.clear();
	}
}
