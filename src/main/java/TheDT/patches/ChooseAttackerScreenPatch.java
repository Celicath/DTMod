package TheDT.patches;

import TheDT.actions.ChooseAttackerAction;
import TheDT.screens.ChooseAttackerScreen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.screens.CardRewardScreen;

import java.util.ArrayList;

public class ChooseAttackerScreenPatch {
	@SpirePatch(clz = CardRewardScreen.class, method = "chooseOneOpen")
	public static class ScreenOpenPatch {
		@SpirePrefixPatch
		public static SpireReturn<Void> Prefix(CardRewardScreen __instance, ArrayList<AbstractCard> choices) {
			if (ChooseAttackerAction.activeThis != null && choices == ChooseAttackerAction.dummyChoice) {
				__instance.rewardGroup = choices;
				ChooseAttackerScreen.open(__instance);
				return SpireReturn.Return(null);
			} else {
				ChooseAttackerAction.activeThis = null;
				return SpireReturn.Continue();
			}
		}
	}

	@SpirePatch(clz = CardRewardScreen.class, method = "update")
	public static class ScreenUpdatePatch {
		@SpirePrefixPatch
		public static SpireReturn<Void> Prefix(CardRewardScreen __instance) {
			if (ChooseAttackerAction.activeThis != null) {
				ChooseAttackerScreen.update();

				return SpireReturn.Return(null);
			} else {
				return SpireReturn.Continue();
			}
		}
	}

	@SpirePatch(clz = CardRewardScreen.class, method = "render")
	public static class ScreenRenderPatch {
		@SpirePrefixPatch
		public static SpireReturn<Void> Prefix(CardRewardScreen __instance, SpriteBatch sb) {
			if (ChooseAttackerAction.activeThis != null) {
				ChooseAttackerScreen.render(sb);

				return SpireReturn.Return(null);
			} else {
				return SpireReturn.Continue();
			}
		}
	}

	@SpirePatch(clz = CardRewardScreen.class, method = "reopen")
	public static class ScreenReopenPatch {
		@SpirePrefixPatch
		public static SpireReturn<Void> Prefix(CardRewardScreen __instance) {
			if (ChooseAttackerAction.activeThis != null) {
				ChooseAttackerScreen.reopen();

				return SpireReturn.Return(null);
			} else {
				return SpireReturn.Continue();
			}
		}
	}

	@SpirePatch(clz = CardRewardScreen.class, method = "open")
	public static class ScreenResetPatch1 {
		@SpirePrefixPatch
		public static void Prefix(CardRewardScreen __instance, ArrayList<AbstractCard> cards, RewardItem rItem, String header) {
			ChooseAttackerAction.activeThis = null;
		}
	}

	@SpirePatch(clz = CardRewardScreen.class, method = "customCombatOpen")
	public static class ScreenResetPatch2 {
		@SpirePrefixPatch
		public static void Prefix(CardRewardScreen __instance, ArrayList<AbstractCard> choices, String text, boolean skippable) {
			ChooseAttackerAction.activeThis = null;
		}
	}

	@SpirePatch(clz = CardRewardScreen.class, method = "draftOpen")
	@SpirePatch(clz = CardRewardScreen.class, method = "discoveryOpen", paramtypez = {})
	@SpirePatch(clz = CardRewardScreen.class, method = "foreignInfluenceOpen")
	@SpirePatch(clz = CardRewardScreen.class, method = "codexOpen")
	public static class ScreenResetPatch3 {
		@SpirePrefixPatch
		public static void Prefix(CardRewardScreen __instance) {
			ChooseAttackerAction.activeThis = null;
		}
	}

	@SpirePatch(clz = CardRewardScreen.class, method = "discoveryOpen", paramtypez = {AbstractCard.CardType.class})
	public static class ScreenResetPatch4 {
		@SpirePrefixPatch
		public static void Prefix(CardRewardScreen __instance, AbstractCard.CardType type) {
			ChooseAttackerAction.activeThis = null;
		}
	}

	@SpirePatch(clz = CardRewardScreen.class, method = "carveRealityOpen")
	public static class ScreenResetPatch5 {
		@SpirePrefixPatch
		public static void Prefix(CardRewardScreen __instance, ArrayList<AbstractCard> cardsToPickBetween) {
			ChooseAttackerAction.activeThis = null;
		}
	}
}
