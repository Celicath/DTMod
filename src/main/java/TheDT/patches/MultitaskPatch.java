package TheDT.patches;

import TheDT.Interfaces.OnApplyWeakPower;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import javassist.CtBehavior;

public class MultitaskPatch {
	public static AbstractPlayer prevPlayer = null;

	@SpirePatch(clz = ApplyPowerAction.class, method = "update")
	public static class DragonWeakPatch {
		@SpireInsertPatch(locator = Locator.class)
		public static void Insert(ApplyPowerAction __instance, AbstractPower ___powerToApply) {
			if (___powerToApply.ID.equals(WeakPower.POWER_ID) && __instance.source.isPlayer) {
				for (AbstractPower p : AbstractDungeon.player.powers) {
					if (p instanceof OnApplyWeakPower) {
						((OnApplyWeakPower) p).onApplyWeak(___powerToApply.amount);
					}
				}
			}
		}
	}

	private static class Locator extends SpireInsertLocator {
		@Override
		public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
			Matcher finalMatcher = new Matcher.NewExprMatcher(FlashAtkImgEffect.class);
			return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
		}
	}
}
