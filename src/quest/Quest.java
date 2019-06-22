package quest;

import script.TagHandler;
import script.Tree;

import java.util.ArrayList;
import java.util.List;

public class Quest {

	private int id;
	private String name;

	private QuestState state;

	private List<Tree> objectivs;
	private Tree reward;

	private String descr;

	public Quest(int id, String name) {
		this.objectivs = new ArrayList<>();

		this.id = id;
		this.name = name;

		state = QuestState.UNACTIV;
	}

	public void opt() {
		((ArrayList) objectivs).trimToSize();
	}

	public boolean isActivateable() {
		return state == QuestState.UNACTIV;
	}

	public boolean activate(TagHandler th) {
		if(isActivateable()) {
			state = QuestState.ACTIV;
			th.setValue("quest" + id, 1);

			System.out.println("Quest angenommen: " + name);

			return true;
		}

		return false;
	}

	public boolean isFinishable(TagHandler th) {
		if(state != QuestState.ACTIV) return false;

		for(Tree o: objectivs) {
			if(!(boolean) o.get(th)) return false;
		}

		return true;
	}

	public boolean finish(TagHandler th) {
		if(isFinishable(th)) {
			state = QuestState.FINISHED;

			System.out.println("Quest beendet: " + name);

			th.setValue("quest" + id, 2);
			reward.get(th);

			return true;
		}

		return false;
	}

	public void setReward(Tree t) {
		this.reward = t;
	}

	public void addObjectiv(Tree o) {
		this.objectivs.add(o);
	}

	public String getName() {
		return name;
	}

	public int getID() {
		return id;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public enum QuestState {
		ACTIV, UNACTIV, FAILED, FINISHED;		//1 - 0 - -1 - 2
	}
}
