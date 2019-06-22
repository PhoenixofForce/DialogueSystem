package dialogue;

import quest.Quest;
import script.Parser;
import script.TagHandler;
import script.Tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Dialogue {

	private List<Text> npcDialogue;
	private List<Text> playerDialogue;
	
	public Dialogue() {
		this.npcDialogue = new ArrayList<>();
		this.playerDialogue = new ArrayList<>();
	}

	public void opt() {
		((ArrayList<Text>) npcDialogue).trimToSize();
		((ArrayList<Text>) playerDialogue).trimToSize();
	}
	
	public void addText(Text t) {
		npcDialogue.add(t);
	}
	
	public void addAnswer(Text t) {
		playerDialogue.add(t);
	}
	
	public List<Text> getAnswers() {
		return playerDialogue;
	}
	
	/**
	 * a==null returns (unevaluated, first) followUp tag of NPC dialogue 
	 * 
	 * @param answerIndex
	 * @return
	 */
	public String getFollowUp(TagHandler th, int answerIndex) {

		List<Text> filtered = playerDialogue.stream().filter(t -> speakable(t, th)).collect(Collectors.toList());
		if(answerIndex >= 0 && filtered.get(answerIndex).hasTag("followUP")) {

			if(filtered.get(answerIndex).hasTag("onSpeak")) {
				Tree tree = Parser.loadScript(Parser.COMMAND_BLOCK, filtered.get(answerIndex).getTagContent("onSpeak"));
				tree.get(th);
			}

			return filtered.get(answerIndex).getTagContent("followUP");
		}

		filtered = npcDialogue.stream().filter(t -> speakable(t, th)).collect(Collectors.toList());
		for(int i = 0; i < filtered.size(); i++) {
			if(filtered.get(i).hasTag("followUP")) return npcDialogue.get(i).getTagContent("followUP");
		}
		
		return null;
	}

	public String buildText(TagHandler th, Map<Integer, Quest> quests) {
		String out = "";

		for(int i = 0; i < npcDialogue.size(); i++) {
			Text t = npcDialogue.get(i);

			if(t.hasTag("condition")) {
				Tree tree = Parser.loadScript(Parser.BOOLEAN, t.getTagContent("condition"));

				if(!(boolean) tree.get(th)) continue;
			}

			if(t.hasTag("onSpeak")) {
				Tree tree = Parser.loadScript(Parser.COMMAND_BLOCK, t.getTagContent("onSpeak"));
				tree.get(th);
			}

			if(t.hasTag("startQuest")) quests.get(Integer.parseInt(t.getTagContent("startQuest"))).activate(th);
			if(t.hasTag("finishQuest")) quests.get(Integer.parseInt(t.getTagContent("finishQuest"))).finish(th);

			String name = t.getSpeaker();
			if(name.startsWith("$")) name = th.getString(name.substring(1));

			String message = t.getMessage();

			out += "[" + name + "] >> " + message + "\n";
		}

		List<Text> filtered = playerDialogue.stream().filter(t -> speakable(t, th)).collect(Collectors.toList());
		for(int i = 0; i < filtered.size(); i++) {
			Text t = filtered.get(i);

			if(t.hasTag("condition")) {
				Tree tree = Parser.loadScript(Parser.BOOLEAN, t.getTagContent("condition"));

				if(!(boolean) tree.get(th)) continue;
			}

			out += "[" + i + "] " + t.getMessage() + (i == playerDialogue.size()-1? "": "\n");
		}

		return out;
	}

	@Override 
	public String toString() {
		String out = "";
		//ADD TEXT
		for(int i = 0; i < npcDialogue.size(); i++) {
			out += npcDialogue.get(i);
		}
		
		//ADD ANSWERS
		for(int i = 0; i < playerDialogue.size(); i++) {
			out += "[" + i + "] " + playerDialogue.get(i).getMessage() + (i == playerDialogue.size()-1? "": "\n");
		}
		return out;
	}


	private boolean speakable(Text t, TagHandler th) {
		if(t.hasTag("condition")) {
			Tree tree = Parser.loadScript(Parser.BOOLEAN, t.getTagContent("condition"));

			return (boolean) tree.get(th);
		}

		return true;
	}




	@Override
	public boolean equals(Object b) {
		if(b instanceof Dialogue) {
			Dialogue t2 = (Dialogue) b;
			return npcDialogue.equals(t2.npcDialogue) && playerDialogue.equals(t2.playerDialogue);
		}
		
		return false;
	}
}
