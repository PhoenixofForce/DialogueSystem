package dialogue;

import java.util.HashMap;
import java.util.Map;

public class Text {

	private String speaker, message;
	private Map<String, String> tags;
	
	public Text(String speaker, String message) {
		this.speaker = speaker;
		this.message = message;
		
		tags = new HashMap<>();
	}
	
	public Text(String speaker, String message, String tagName, String tagContent) {
		this(speaker, message);
	
		tags.put(tagName, tagContent);
	}
	
	public void addTag(String name, String content) {
		tags.put(name, content);
	}
	
	public String getSpeaker() {
		return speaker;
	}
	
	public String getMessage() {
		return message;
	}
	
	public String getTagContent(String tagName) {
		return tags.get(tagName);
	}
	
	public boolean hasTag(String tagName) {
		return tags.containsKey(tagName);
	}

	@Override
	public String toString() {
		return String.format("[%s] >> %s", speaker, message);
	}
	
	@Override
	public boolean equals(Object b) {
		if(b instanceof Text) {
			Text t2 = (Text) b;
			return speaker.equals(t2.speaker) && message.equals(t2.message) && tags.equals(t2.tags);
		}
		
		return false;
	}
}