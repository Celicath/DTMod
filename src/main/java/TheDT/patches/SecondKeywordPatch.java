package TheDT.patches;

import TheDT.DTModMain;
import TheDT.cards.AbstractDTCard;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DescriptionLine;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import javassist.CtBehavior;

public class SecondKeywordPatch {
	public static final CharacterStrings charStrings = CardCrawlGame.languagePack.getCharacterString(DTModMain.makeID("HighlightWords"));
	public static final String colorCodeLeft = "[#60d0d0";
	public static final String colorCodeRight = "]";
	public static final String colorCode = colorCodeLeft + colorCodeRight;

	@SpirePatch(clz = AbstractCard.class, method = "initializeDescription")
	public static class ColorYouOrDragonPatch {
		@SpireInsertPatch(locator = Locator.class)
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

		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
				Matcher finalMatcher = new Matcher.MethodCallMatcher(StringBuilder.class, "setLength");
				int[] all = LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
				return new int[]{all[1]};
			}
		}
	}

	@SpirePatch(clz = AbstractCard.class, method = "initializeDescriptionCN")
	public static class ColorYouOrDragonPatchCN {
		@SpireInsertPatch(locator = Locator.class)
		public static void Insert(AbstractCard __instance, @ByRef String[] ___word, float ___CN_DESC_BOX_WIDTH, GlyphLayout ___gl, StringBuilder ___sbuilder, @ByRef float[] ___currentWidth) {
			boolean colored = false;
			if (__instance instanceof AbstractDTCard) {
				for (String t : charStrings.NAMES) {
					if (___word[0].equals(t)) {
						___word[0] = colorCode + t + "[]";
						colored = true;
						break;
					}
				}
				for (String t : charStrings.TEXT) {
					if (___word[0].equals(t)) {
						for (String p : charStrings.NAMES) {
							if (t.startsWith(p)) {
								___word[0] = colorCode + p + "[]" + t.substring(p.length());
								colored = true;
								break;
							}
						}
					}
				}
			}

			if (colored) {
				___gl.setText(FontHelper.cardDescFont_N, ___word[0]);
				if (___currentWidth[0] + ___gl.width > ___CN_DESC_BOX_WIDTH) {
					__instance.description.add(new DescriptionLine(___sbuilder.toString().trim(), ___currentWidth[0]));
					___sbuilder.setLength(0);
					___currentWidth[0] = ___gl.width;
				} else {
					___currentWidth[0] += ___gl.width;
				}
				___sbuilder.append(___word[0]);
				___word[0] = "";
			}
		}

		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
				Matcher finalMatcher = new Matcher.MethodCallMatcher(String.class, "toCharArray");
				return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
			}
		}
	}

	@SpirePatch(clz = AbstractCard.class, method = "renderDescription")
	@SpirePatch(clz = AbstractCard.class, method = "renderDescriptionCN")
	public static class ProperAlphaRenderPatch {
		@SpireInsertPatch(locator = Locator.class)
		public static void Insert(AbstractCard __instance, SpriteBatch sb, Color ___textColor, @ByRef String[] ___tmp) {
			if (__instance instanceof AbstractDTCard && ___tmp[0].contains(colorCode)) {
				if (___textColor.a < 1.0f) {
					int alpha = MathUtils.clamp((int) (___textColor.a * 255), 0, 255);
					String str = colorCodeLeft + Integer.toHexString(alpha).toUpperCase() + colorCodeRight;
					___tmp[0] = ___tmp[0].replace(colorCode, str);
				}
			}
		}

		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
				Matcher finalMatcher = new Matcher.MethodCallMatcher(FontHelper.class, "renderRotatedText");
				int[] all = LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
				return new int[]{all[all.length - 1]};
			}
		}
	}
}
