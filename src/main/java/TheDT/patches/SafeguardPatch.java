package TheDT.patches;

import TheDT.characters.DragonTamer;
import TheDT.powers.SafeguardPower;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.BarricadePower;

public class SafeguardPatch {
	public static boolean shouldPatch = false;

	@SpirePatch(clz = GameActionManager.class, method = "getNextAction")
	public static class GetNextActionChecker {
		@SpirePrefixPatch
		public static void Prefix(GameActionManager __instance) {
			shouldPatch = true;
		}

		@SpirePostfixPatch
		public static void Postfix(GameActionManager __instance) {
			shouldPatch = false;
		}
	}

	@SpirePatch(clz = AbstractCreature.class, method = "hasPower")
	public static class HasBarricadePatch {
		@SpirePostfixPatch
		public static boolean Postfix(boolean __result, AbstractCreature __instance, String targetID) {
			if (!shouldPatch || __result) {
				return __result;
			}
			if (targetID.equals(BarricadePower.POWER_ID) && __instance.hasPower(SafeguardPower.POWER_ID)) {
				if (__instance == AbstractDungeon.player) {
					return DragonTamer.isRearYou();
				} else if (__instance == DragonTamer.getLivingDragon()) {
					return !DragonTamer.isFrontDragon();
				} else {
					return __result;
				}
			} else {
				return __result;
			}
		}
	}
}
