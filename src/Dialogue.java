import java.util.ArrayList;
import java.util.List;

public class Dialogue {

	private List<Text> npcDialogue;
	private List<Text> playerDialogue;
	
	public Dialogue() {
		this.npcDialogue = new ArrayList<>();
		this.playerDialogue = new ArrayList<>();
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
	 * @param a
	 * @return
	 */
	public String getFollowUp(Text a) {
		if(a != null && a.hasTag("followUP")) return a.getTagContent("followUP");
		
			for(int i = 0; i < npcDialogue.size(); i++) {
			if(npcDialogue.get(i).hasTag("followUP")) return npcDialogue.get(i).getTagContent("followUP");
		}
		
		return "§End";
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
	
	@Override
	public boolean equals(Object b) {
		if(b instanceof Dialogue) {
			Dialogue t2 = (Dialogue) b;
			return npcDialogue.equals(t2.npcDialogue) && playerDialogue.equals(t2.playerDialogue);
		}
		
		return false;
	}
}
