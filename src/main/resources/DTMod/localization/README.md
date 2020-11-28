# The Dragon Tamer Mod Localization

Similar to base game, each folder contains localization files for a language.
If you add a new folder and compile, this mod will automatically detect the new localization files.

Dragon Tamer mod has some unusual localization texts because of new features.
This document will explain those features to help localization process.

## New features

### Dragon Status Screen

![image](https://user-images.githubusercontent.com/1008668/94883043-97088e00-04a4-11eb-9f6b-eca836ea4cb7.png)
![image](https://user-images.githubusercontent.com/1008668/94883048-996ae800-04a4-11eb-9b18-707d5e31519a.png)
![image](https://user-images.githubusercontent.com/1008668/94883340-62490680-04a5-11eb-91eb-329157777dd6.png)

This screen gives information about the Dragon. It uses `"DTMod:DragonStatus"` from `ui.json` and `"DTMod:DragonGrowth"`
from `character.json.`

The text here uses 'Real Text', which is different from the text engine from base game. In Real Text, you cannot use
`NL` for newlines, or `#b`, `#y` etc. for colored text. Instead, you use `\n` for newlines, and `[#87ceeb]text[]` for
colors. Unlike base game texts, spaces are displayed normally in ZHS, ZHT and JPN languages.

However, the last 6 TEXTs from `"DTMod:DragonGrowth"` do not use Real Text, since they are used in tooltips. (The third
image above shows how they are used in tooltips)

### Choose Attacker Screen

![image](https://user-images.githubusercontent.com/1008668/94883270-33329500-04a5-11eb-9028-97de4b4a822d.png)
![image](https://user-images.githubusercontent.com/1008668/94883273-3594ef00-04a5-11eb-98db-ed51af28ecd6.png)

Some cards let you choose either You or Dragon as the attacker. It uses `"DTMod:ChooseAttackerScreen"` from `ui.json`.

The first 2 TEXTs, `"Choose the attacker"` and `"Choose the character"` are used for the banner. The last 2 TEXTs are
used for `10 Damage`, one for prefix and one for postfix. `10 Damage` is Real Text.

### You and Dragon

![image](https://user-images.githubusercontent.com/1008668/94883693-442fd600-04a6-11eb-8cc3-8a5fbe59de15.png)

The word 'You' and 'Dragon' are colored differently. These colored words are defined in `"DTMod:HighlightWords"` from
`characters.json`. The NAMES should contain `You`, `Dragon` and any other words that needs to be highlighted. If you
add `You.` or `Dragon.` to TEXT, only the `You` and `Dragon` is colored; the remaining `.` is left as white text.

The highlight words are only used in `cards.json`. If you write 'You' or 'Dragon' there, they are automatically
colored similar to base game keywords. However, in any other texts like `relics.json`, they are not colored
automatically. So you need to write as `[#60D0D0]You[]` or `[#60D0D0]Dragon[]`.

## Other

- When you translate bonding keyword, you need to add english "bonding" to "NAMES".
- The word "Tactic" is important similar to "Strike" used in Perfected Strike.
- Maybe I forgot to explain some things. If you have any questions, contact me via discord (celicath#3192).
