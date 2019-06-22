import dialogue.Dialogue;
import dialogue.Text;
import quest.Quest;
import script.Parser;
import script.TagHandler;

import java.io.*;
import java.util.*;

public class Main {

	public static final String PARAGRAPH = "§";
	public static final String ANSWER = ">";
	public static final String NAME_SPLIT = ":";
	public static final String TAG_START = "\\[tag";
	public static final String TAG_START_STRING = "[tag";
	
	public static void main(String[] args) {

		TagHandler th = new TagHandler();

		File q = new File("dialogue/test.quest");
		Map<Integer, Quest> questList = new HashMap<>();
		try {
			BufferedReader r = new BufferedReader(new FileReader(q));
			String line = r.readLine();

			Quest current = null;
			while(line != null) {
				while(line.length() == 0)line = r.readLine();

				if(line.startsWith(PARAGRAPH)) {
					if(current != null) {
						current.opt();
						questList.put(current.getID(), current);
					}

					current = new Quest(Integer.parseInt(line.split("-")[0].substring(1)), line.split("-")[1]);
				}


				else if(line.startsWith(TAG_START_STRING)) {
					String tagType = line.split(";")[1].trim();
					String tagContent = line.substring(line.indexOf(tagType) + tagType.length() + 1);
					tagContent = tagContent.substring(0, tagContent.length()-1).trim();

					if(tagType.equalsIgnoreCase("objectiv")) current.addObjectiv(Parser.loadScript(Parser.BOOLEAN, tagContent));
					else if(tagType.equalsIgnoreCase("reward")) current.setReward(Parser.loadScript(Parser.COMMAND_BLOCK, tagContent));
				}


				else if(line.startsWith("description")) {
					current.setDescr(line.split(":")[1]);
				}

				line = r.readLine();
			}

			current.opt();
			questList.put(current.getID(), current);

			r.close();
		} catch (IOException e) {
			e.printStackTrace();
		}




		//if(true) return;






		File f = new File("dialogue/test.diag");
		
		Map<String, Dialogue> dialogueTree = new HashMap<>();
		try {
			BufferedReader r = new BufferedReader(new FileReader(f));
			String line = r.readLine();
			
			Dialogue current = null;
			String name = null;
			
			//PARSE PRE TAGS
			while(line.startsWith(TAG_START_STRING)) {
				String tagType = line.split(";")[1].trim();
				String tagContent = line.substring(line.indexOf(tagType) + tagType.length() + 1);
				tagContent = tagContent.substring(0, tagContent.length()-1).trim();

				Parser.loadScript(Parser.COMMAND_BLOCK, tagContent).get(th);

				line = r.readLine();
			}


			//PARSE DIALOGUE
			while(line != null) {
				
				//LOAD "CHECKPOINT"
				if(line.startsWith(PARAGRAPH)) {
					if(current != null) {
						current.opt();
						dialogueTree.put(name, current);
					}
					
					name = line;
					current = new Dialogue();
				}
				
				
				//LOAD ANSWERS
				else if(line.startsWith(ANSWER)) {
					line = line.substring(1);
					
					String st = TAG_START;
					String message = line.split(st)[0];
					
					Text t = new Text("player", message);
					
					//PARSE ANSWER TAGS
					String[] tags = line.split(TAG_START);
					for(int i = 1; i < tags.length; i++) {
						String tagType = tags[i].split(";")[1].trim();
						String tagContent = tags[i].substring(tags[i].indexOf(tagType) + tagType.length() + 1);
						tagContent = tagContent.substring(0, tagContent.length()-1).trim();
					
						t.addTag(tagType, tagContent);
					}
					
					current.addAnswer(t);
				}
				
				
				//LOAD TAG SOLO
				else if(line.startsWith(TAG_START_STRING)) {
					Text t = new Text("", "");
					
					String[] tags = line.split(TAG_START);
					for(int i = 1; i < tags.length; i++) {
						String tagType = tags[i].split(";")[1].trim();
						String tagContent = tags[i].substring(tags[i].indexOf(tagType) + tagType.length() + 1);
						tagContent = tagContent.substring(0, tagContent.length()-1).trim();
					
						t.addTag(tagType, tagContent);
					}
					
					current.addAnswer(t);
				}
				
				
				//LOAD NORMAL TEXT
				else if(line.length() > 0){
					String speaker = line.split(NAME_SPLIT)[0];
					String message = line.split(NAME_SPLIT)[1].split(TAG_START)[0];
					
					
					Text t = new Text(speaker, message);
					
					String[] tags = line.split(TAG_START);
					for(int i = 1; i < tags.length; i++) {
						String tagType = tags[i].split(";")[1].trim();
						String tagContent = tags[i].substring(tags[i].indexOf(tagType) + tagType.length() + 1);
						tagContent = tagContent.substring(0, tagContent.length()-1).trim();
					
						t.addTag(tagType, tagContent);
					}
					
					current.addText(t);
				}
				
				line = r.readLine();
			}

			current.opt();
			dialogueTree.put(name, current);
			r.close();
		} catch (IOException e) {
			e.printStackTrace();
		}







		Scanner s = new Scanner(System.in);
		Dialogue d = dialogueTree.get("§Start");

		while(d != null) {

			System.out.println(d.buildText(th, questList));	//PRINT DIALOGUE AND POSSIBLE ANSWERS
			if(d.getAnswers().size() == 0) {

				//JUMP TO NEXT D
				String st = d.getFollowUp(th, -1);
				d = dialogueTree.get(st);
				continue;
			}

			//DO ANSWER
			int i = s.nextInt();
			d = dialogueTree.get(d.getFollowUp(th, i));
		}

		System.out.println("[Der Dialog ist vorbei]");
		s.close();
	}
}
