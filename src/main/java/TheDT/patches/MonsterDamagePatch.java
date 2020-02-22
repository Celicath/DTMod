package TheDT.patches;

import TheDT.characters.TheDT;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.stances.AbstractStance;
import javassist.CtBehavior;

public class MonsterDamagePatch {
	public static AbstractPlayer prevPlayer = null;

	@SpirePatch(clz = DamageInfo.class, method = "applyPowers")
	public static class ChangeTargetPatch {
		@SpireInsertPatch(locator = BeforeStanceLocator.class)
		public static void InsertBefore(DamageInfo __instance, AbstractCreature owner, AbstractCreature target) {
			if (AbstractDungeon.player instanceof TheDT && ((TheDT) AbstractDungeon.player).front == ((TheDT) AbstractDungeon.player).dragon) {
				prevPlayer = AbstractDungeon.player;
				AbstractDungeon.player = ((TheDT) AbstractDungeon.player).dragon;
			}
		}

		@SpireInsertPatch(locator = AfterStanceLocator.class)
		public static void InsertAfter(DamageInfo __instance, AbstractCreature owner, AbstractCreature target) {
			if (prevPlayer != null) {
				AbstractDungeon.player = prevPlayer;
				prevPlayer = null;
			}
		}
	}

	private static class BeforeStanceLocator extends SpireInsertLocator {
		@Override
		public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
			Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractStance.class, "atDamageReceive");
			return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
		}
	}

	private static class AfterStanceLocator extends SpireInsertLocator {
		@Override
		public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
			int[] result = (new BeforeStanceLocator()).Locate(ctMethodToPatch);
			return new int[]{result[0] + 1};
		}
	}
}
