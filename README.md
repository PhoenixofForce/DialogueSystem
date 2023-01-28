## DialogueSystem
Dialogue System in the command line that uses tags, which are established in other projects of mine

### Tags
A tag has the form of `[tag; key; value]`. For example you can give a quest a tag named `reward` with the content `20 gold`. In your game when the player finishes the quest the `rewards` tag gets loaded and its content gets executed which then gives the player 20 gold. 
Tags are also used by my <a href="https://github.com/PhoenixofForce/Level_Editor"> Level Editor </a>

### Quests
A .quest file and has the form of
```
§1-The final test
[tag; objectiv; #item_coal>=5]
[tag; reward; #gold = (#gold + 20); #exp = (#exp + 15); #item_coal = (#item_coal - 5);]
description:Bring Klaus some coal!

§id-Name
[tag; objectiv; objective key]
[tag; reward; reward key;]
description:some text without being enclosed in ""

....
```

### Dialogues
Dialogues follow a similar pattern:
```
[tag; name; $name="Unknown";]
§Start
$name:Hi.
>Who are you?[tag; followUP; §Name][tag; onSpeak; $name="Klaus";]
>Can I help you?[tag; followUP; §A2]
>About the quest...[tag; followUP; §Quest][tag; condition; #quest1==1]
>Bye[tag; followUP; §End]

$Node Name
$Variable:Text this person says
>Response1[tag; followUp; §nextNodeName]
>Response2[tag; followUp; §nextNodeName]
```
Here are some special tags:
- condition: condition if response is available
- onSpeak: tag content gets executed when this respone was chosen
- finishQuest: finished the quest which id is in the tag content
