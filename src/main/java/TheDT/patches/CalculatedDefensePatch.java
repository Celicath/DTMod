package TheDT.patches;

import TheDT.powers.CalculatedDefensePower;
import TheDT.powers.OddArmorPower;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import javassist.CtBehavior;

public class CalculatedDefensePatch {
	@SpirePatch(clz = DamageInfo.class, method = "applyPowers")
	public static class ApplyPowerPatch {
		@SpireInsertPatch(locator = BeforeAtDamageGiveLocator.class)
		public static void Insert(DamageInfo __instance, AbstractCreature owner, AbstractCreature target, @ByRef float[] ___tmp) {
			if (__instance.type == DamageInfo.DamageType.NORMAL) {
				AbstractPower p = target.getPower(CalculatedDefensePower.POWER_ID);
				if (p != null && owner.hasPower(WeakPower.POWER_ID)) {
					___tmp[0] -= p.amount;
					__instance.isModified = true;
				}
				p = target.getPower(OddArmorPower.POWER_ID);
				if (p != null) {
					___tmp[0] -= p.amount;
					__instance.isModified = true;
				}
			}
		}
	}

	@SpirePatch(clz = AbstractMonster.class, method = "calculateDamage")
	public static class CalculateDamagePatch {
		@SpireInsertPatch(locator = BeforeAtDamageGiveLocator2.class)
		public static void Insert(AbstractMonster __instance, int dmg, @ByRef float[] ___tmp) {
			AbstractPower p = AbstractDungeon.player.getPower(CalculatedDefensePower.POWER_ID);
			if (p != null && __instance.hasPower(WeakPower.POWER_ID)) {
				___tmp[0] -= p.amount;
			}

			p = AbstractDungeon.player.getPower(OddArmorPower.POWER_ID);
			if (p != null) {
				___tmp[0] -= p.amount;
			}
		}
	}

	private static class BeforeAtDamageGiveLocator extends SpireInsertLocator {
		@Override
		public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
			Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractCreature.class, "powers");
			return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
		}
	}

	private static class BeforeAtDamageGiveLocator2 extends SpireInsertLocator {
		@Override
		public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
			Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPower.class, "atDamageGive");
			int[] result = LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
			return new int[]{result[0] - 1};
		}
	}
}
