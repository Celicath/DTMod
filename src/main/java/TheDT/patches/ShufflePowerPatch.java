package TheDT.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.actions.common.ShuffleAction;
import com.megacrit.cardcrawl.actions.defect.ShuffleAllAction;

public class ShufflePowerPatch {
	@SpirePatch(clz = ShuffleAction.class, method = "update")
	public static class ShuffleActionPatch {
		@SpirePrefixPatch
		public static void Prefix(ShuffleAction __instance, boolean ___triggerRelics) {
			if (___triggerRelics) {
				TheDT.DTMod.onShuffle();
			}
		}
	}

	@SpirePatch(clz = ShuffleAllAction.class, method = SpirePatch.CONSTRUCTOR)
	public static class ShuffleAllActionPatch {
		@SpirePostfixPatch
		public static void Postfix(ShuffleAllAction __instance) {
			TheDT.DTMod.onShuffle();
		}
	}

	@SpirePatch(clz = EmptyDeckShuffleAction.class, method = SpirePatch.CONSTRUCTOR)
	public static class EmptyDeckShuffleActionPatch {
		@SpirePostfixPatch
		public static void Postfix(EmptyDeckShuffleAction __instance) {
			TheDT.DTMod.onShuffle();
		}
	}
}
