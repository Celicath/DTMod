package TheDT.patches;

import TheDT.DTModMain;
import TheDT.cards.AbstractDTCard;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import javassist.CtBehavior;

public class SecondKeywordPatch {
	public static final CharacterStrings charStrings = CardCrawlGame.languagePack.getCharacterString(DTModMain.makeID("HighlightWords"));
	public static final String colorCodeLeft = "[#60D0D0";
	public static final String colorCodeRight = "]";
	public static final String colorCode = colorCodeLeft + colorCodeRight;

	@SpirePatch(clz = AbstractCard.class, method = "initializeDescription")
	public static class ColorYouOrDragonPatch {
		@SpireInsertPatch(locator = TokenInitLocator.class)
		public static void Insert(AbstractCard __instance, @ByRef String[] ___word) {
			if (__instance instanceof AbstractDTCard) {
				for (String t : charStrings.NAMES) {
					if (___word[0].equals(t)) {
						___word[0] = colorCode + t + "[]";
						return;
					}
				}
				for (String t : charStrings.TEXT) {
					if (___word[0].equals(t)) {
						for (String p : charStrings.NAMES) {
							if (t.startsWith(p)) {
								___word[0] = colorCode + p + "[]" + t.substring(p.length());
								return;
							}
						}
					}
				}
			}
		}

		private static class TokenInitLocator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
				Matcher finalMatcher = new Matcher.MethodCallMatcher(StringBuilder.class, "setLength");
				int[] all = LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
				return new int[]{all[1]};
			}
		}
	}

	@SpirePatch(clz = AbstractCard.class, method = "renderDescription")
	public static class ProperAlphaRenderPatch {
		@SpireInsertPatch(locator = RenderLocator.class)
		public static void Insert(AbstractCard __instance, SpriteBatch sb, Color ___textColor, @ByRef String[] ___tmp) {
			if (__instance instanceof AbstractDTCard && ___tmp[0].contains(colorCode)) {
				int alpha = MathUtils.clamp((int) (___textColor.a * 255), 0, 255);
				if (alpha != 255) {
					String str = colorCodeLeft + Integer.toHexString(alpha).toUpperCase() + colorCodeRight;
					___tmp[0] = ___tmp[0].replace(colorCode, str);
				}
			}
		}

		private static class RenderLocator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
				Matcher finalMatcher = new Matcher.MethodCallMatcher(FontHelper.class, "renderRotatedText");
				int[] all = LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
				return new int[]{all[all.length - 1]};
			}
		}
	}
}
