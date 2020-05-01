package TheDT.patches;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.screens.runHistory.TinyCard;

public class TinyCardPatch {
	private static final Color BG_COLOR = new Color(0.8f, 0.5f, 0.2f, 1.0f);
	private static final Color DESC_COLOR = new Color(0.4f, 0.32f, 0.24f, 1.0f);

	@SpirePatch(clz = TinyCard.class, method = "getIconBackgroundColor")
	public static class GetIconBackgroundColor {
		@SpirePostfixPatch
		public static Color Postfix(Color __result, TinyCard __instance, AbstractCard card) {
			return card.color == CardColorEnum.DT_ORANGE ? BG_COLOR : __result;
		}
	}

	@SpirePatch(clz = TinyCard.class, method = "getIconDescriptionColor")
	public static class GetIconDescriptionColor {
		@SpirePostfixPatch
		public static Color Postfix(Color __result, TinyCard __instance, AbstractCard card) {
			return card.color == CardColorEnum.DT_ORANGE ? DESC_COLOR : __result;
		}
	}
}
