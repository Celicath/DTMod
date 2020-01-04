package TheDT.patches;

import TheDT.characters.TheDT;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CtBehavior;

public class MonsterTargetPatch {
	public static AbstractPlayer prevPlayer = null;

	@SpirePatch(clz = GameActionManager.class, method = "getNextAction")
	public static class BeforeTakeTurn {
		@SpireInsertPatch(locator = BeforeTakeTurnLocator.class)
		public static void Insert(GameActionManager __instance) {
			if (AbstractDungeon.player instanceof TheDT && ((TheDT) AbstractDungeon.player).front == ((TheDT) AbstractDungeon.player).dragon) {
				prevPlayer = AbstractDungeon.player;
				AbstractDungeon.player = ((TheDT) AbstractDungeon.player).dragon;
			}
		}
	}

	private static class BeforeTakeTurnLocator extends SpireInsertLocator {
		@Override
		public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
			Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractMonster.class, "takeTurn");
			return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
		}
	}


	@SpirePatch(clz = GameActionManager.class, method = "getNextAction")
	public static class AfterTakeTurn {
		@SpireInsertPatch(locator = AfterTakeTurnLocator.class)
		public static void Insert(GameActionManager __instance) {
			if (prevPlayer != null) {
				AbstractDungeon.player = prevPlayer;
				prevPlayer = null;
			}
		}
	}

	private static class AfterTakeTurnLocator extends SpireInsertLocator {
		@Override
		public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
			Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractMonster.class, "applyTurnPowers");
			return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
		}
	}
}
