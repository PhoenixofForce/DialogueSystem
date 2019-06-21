package quest;

import script.Parser;
import script.TagHandler;

import java.util.ArrayList;
import java.util.List;

public class Quest {

	private int id;
	private String name;

	private QuestState state;

	private String questGiver, questRewarder;
	private List<Objectiv> objectivs;
	private List<Reward> rewards;

	private String descr;

	public Quest(int id, String name) {
		this.objectivs = new ArrayList<>();
		rewards = new ArrayList<>();

		this.id = id;
		this.name = name;

		state = QuestState.UNACTIV;
	}

	public void opt() {
		((ArrayList) objectivs).trimToSize();
		((ArrayList) rewards).trimToSize();
	}

	public boolean isFinishable(TagHandler th) {
		if(state != QuestState.ACTIV) return false;

		for(Objectiv o: objectivs) {
			if(!o.achieved(th)) return false;
		}

		return true;
	}

	public boolean finish(TagHandler th) {
		if(isFinishable(th)) {
			state = QuestState.FINISHED;

			for(Reward r: rewards) r.get(th);
			for(Objectiv o: objectivs) Parser.loadScript(Parser.COMMAND, "#" + o.getSpec() + "=(#" + o.getSpec() + "-" + o.getCount()+ ");").get(th);

			return true;
		}

		return false;
	}


	//TESTONLY
	public void setState(QuestState state) {
		this.state = state;
	}


	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

	public QuestState getState() {
		return state;
	}

	public String getQuestGiver() {
		return questGiver;
	}

	public void setQuestGiver(String questGiver) {
		this.questGiver = questGiver;
	}

	public String getQuestRewarder() {
		return questRewarder;
	}

	public Reward getReward(int i) {
		return i < rewards.size()? rewards.get(i): null;
	}

	public void addReward(Reward r) {
		rewards.add(r);
	}

	public Objectiv getObjectiv(int i) {
		return i < objectivs.size()? objectivs.get(i): null;
	}

	public void addObjectiv(Objectiv o) {
		objectivs.add(o);
	}

	public void setQuestRewarder(String questRewarder) {
		this.questRewarder = questRewarder;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public enum QuestState {
		ACTIV, UNACTIV, FAILED, FINISHED;
	}
}
