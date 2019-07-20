package TheDT.patches;

import TheDT.powers.CalculatedDefensePower;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.WeakPower;

public class CalculatedDefensePatch {
	@SpirePatch(clz = DamageInfo.class, method = "applyPowers")
	public static class PowerActivate1 {
		@SpirePrefixPatch
		public static void Prefix(DamageInfo __instance, AbstractCreature owner, AbstractCreature target) {
			CalculatedDefensePower.activated = owner.hasPower(WeakPower.POWER_ID);
		}
	}

	@SpirePatch(clz = DamageInfo.class, method = "applyEnemyPowersOnly")
	public static class PowerActivate2 {
		@SpirePrefixPatch
		public static void Prefix(DamageInfo __instance, AbstractCreature target) {
			CalculatedDefensePower.activated = false;
		}
	}
}
