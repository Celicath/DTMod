package TheDT.patches;

import TheDT.characters.Dragon;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.watcher.BlockReturnPower;
import javassist.CtBehavior;

public class TalkToTheHandPatch {
	@SpirePatch(clz = BlockReturnPower.class, method = "onAttacked")
	public static class DragonStartTurn {
		@SpireInsertPatch(locator = Locator.class)
		public static SpireReturn<Integer> Insert(BlockReturnPower __instance, DamageInfo info, int damageAmount, int ___amount) {
			if (info.owner instanceof Dragon) {
				AbstractDungeon.actionManager.addToTop(new GainBlockAction(info.owner, ___amount, Settings.FAST_MODE));
				return SpireReturn.Return(damageAmount);
			} else {
				return SpireReturn.Continue();
			}
		}
	}

	private static class Locator extends SpireInsertLocator {
		@Override
		public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
			Matcher finalMatcher = new Matcher.NewExprMatcher(GainBlockAction.class);
			return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
		}
	}
}
