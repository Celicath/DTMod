package TheDT.patches;

import TheDT.characters.DragonTamer;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CtBehavior;

public class MonsterTargetPatch {
	public static AbstractPlayer prevPlayer = null;

	@SpirePatch(clz = GameActionManager.class, method = "getNextAction")
	public static class ChangeTargetPatch {
		@SpireInsertPatch(locator = BeforeTakeTurnLocator.class)
		public static void InsertBefore(GameActionManager __instance, AbstractMonster ___m) {
			if (AbstractDungeon.player instanceof DragonTamer && DragonTamer.isCurrentTargetDragon(___m)) {
				prevPlayer = AbstractDungeon.player;
				AbstractDungeon.player = ((DragonTamer) AbstractDungeon.player).dragon;
			}
		}

		@SpireInsertPatch(locator = AfterTakeTurnLocator.class)
		public static void InsertAfter(GameActionManager __instance) {
			if (prevPlayer != null) {
				AbstractDungeon.player = prevPlayer;
				prevPlayer = null;
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

	private static class AfterTakeTurnLocator extends SpireInsertLocator {
		@Override
		public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
			Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractMonster.class, "applyTurnPowers");
			return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
		}
	}
}
