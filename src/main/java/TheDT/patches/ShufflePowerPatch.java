package TheDT.patches;

import TheDT.DTModMain;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.actions.common.PutOnDeckAction;
import com.megacrit.cardcrawl.actions.common.ShuffleAction;
import com.megacrit.cardcrawl.actions.defect.ShuffleAllAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class ShufflePowerPatch {
	@SpirePatch(clz = ShuffleAction.class, method = "update")
	public static class ShuffleActionPatch {
		@SpirePrefixPatch
		public static void Prefix(ShuffleAction __instance, boolean ___triggerRelics) {
			if (___triggerRelics) {
				DTModMain.onShuffle();
			}
		}
	}

	static PutOnDeckAction patchNeededAction = null;

	@SpirePatch(clz = ShuffleAllAction.class, method = "update")
	public static class ShuffleAllActionPatch {
		@SpirePostfixPatch
		public static void Postfix(ShuffleAllAction __instance) {
			for (AbstractGameAction action : AbstractDungeon.actionManager.actions) {
				if (action instanceof PutOnDeckAction) {
					patchNeededAction = (PutOnDeckAction) action;
					break;
				}
			}
		}
	}

	@SpirePatch(clz = PutOnDeckAction.class, method = "update")
	public static class PutOnDeckActionPatch {
		@SpirePostfixPatch
		public static void Postfix(PutOnDeckAction __instance) {
			if (__instance == patchNeededAction) {
				DTModMain.onShuffle();
				patchNeededAction = null;
			}
		}
	}

	@SpirePatch(clz = EmptyDeckShuffleAction.class, method = SpirePatch.CONSTRUCTOR)
	public static class EmptyDeckShuffleActionPatch {
		@SpirePostfixPatch
		public static void Postfix(EmptyDeckShuffleAction __instance) {
			DTModMain.onShuffle();
		}
	}
}
