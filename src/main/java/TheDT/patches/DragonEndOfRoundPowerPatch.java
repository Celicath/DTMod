package TheDT.patches;

import TheDT.cards.AbstractDTCard;
import TheDT.characters.Dragon;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CtBehavior;

public class DragonEndOfRoundPowerPatch {
	@SpirePatch(clz = MonsterGroup.class, method = "applyEndOfTurnPowers")
	public static class EndOfRoundPower {
		@SpireInsertPatch(locator = EndOfTurnPowerLocator.class)
		public static void Insert(MonsterGroup __instance) {
			Dragon dragon = AbstractDTCard.getDragon();
			if (dragon != null) {
				for (AbstractPower p : dragon.powers) {
					p.atEndOfRound();
				}
			}
		}
	}

	private static class EndOfTurnPowerLocator extends SpireInsertLocator {
		@Override
		public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
			Matcher finalMatcher = new Matcher.FieldAccessMatcher(MonsterGroup.class, "monsters");
			int[] all = LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
			return new int[]{all[1]};
		}
	}
}
