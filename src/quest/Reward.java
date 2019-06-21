package quest;

import script.Parser;
import script.TagHandler;

public class Reward {

	private String spec;
	private int count;

	public Reward(String spec, int count) {
		this.spec = spec;
		this.count = count;
	}

	public void get(TagHandler th) {
		String line = "#" + spec + "=(#" + spec + "+" + count + ");";
		Parser.loadScript(Parser.COMMAND, line).get(th);
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
