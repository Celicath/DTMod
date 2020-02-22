package TheDT.patches;

import TheDT.characters.TheDT;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CtBehavior;

public class MonsterIntentPatch {
	public static AbstractPlayer prevPlayer = null;

	@SpirePatch(clz = AbstractMonster.class, method = "calculateDamage")
	public static class CalculateDamageChangeTarget {
		@SpirePrefixPatch
		public static void Prefix(AbstractMonster __instance, int dmg) {
			if (AbstractDungeon.player instanceof TheDT && ((TheDT) AbstractDungeon.player).front == ((TheDT) AbstractDungeon.player).dragon) {
				prevPlayer = AbstractDungeon.player;
				AbstractDungeon.player = ((TheDT) AbstractDungeon.player).dragon;
			}
		}

		@SpirePostfixPatch
		public static void Postfix(AbstractMonster __instance, int dmg) {
			if (prevPlayer != null) {
				AbstractDungeon.player = prevPlayer;
				prevPlayer = null;
			}
		}
	}
}
