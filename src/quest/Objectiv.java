package quest;

import script.Parser;
import script.TagHandler;
import script.Tree;

public class Objectiv {

	private String spec;
	private int oCount;

	private Tree achieved;

	public Objectiv(String spec, int count) {
		this.oCount = count;
		this.spec = spec;

		achieved = Parser.loadScript(Parser.BOOLEAN, "#" + spec + ">=" + count);
	}

	public boolean achieved(TagHandler th) {
		return (boolean) achieved.get(th);
	}

	public int getCount() {
		return oCount;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public void setCount(int oCount) {
		this.oCount = oCount;
	}
}