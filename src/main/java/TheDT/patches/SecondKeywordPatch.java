package TheDT.patches;

import TheDT.DTModMain;
import TheDT.cards.AbstractDTCard;
import TheDT.relics.SwitchButton;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DescriptionLine;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
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
				int alpha = MathUtils.clamp((int) (___textColor.a * 255), 0, 255);
				if (alpha != 255) {
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

	@SpirePatch(clz = FontHelper.class, method = "exampleNonWordWrappedText")
	public static class SmartTextZHSPatch {
		@SpireInsertPatch(locator = Locator.class)
		public static void Insert(SpriteBatch sb, BitmapFont font, String msg, float x, float y, Color c, float widthMax, float lineSpacing, @ByRef float[] ___curWidth, @ByRef int[] ___currentLine, String ___word) {
			boolean needFix = ___word.startsWith("[#60D0D0]") || ___word.startsWith("[#60d0d0]");
			if (___word.startsWith("[#2aecd7]") || ___word.startsWith("[#2AECD7]")) {
				AbstractRelic r = RelicLibrary.getRelic(SwitchButton.ID);
				if (r != null) {
					needFix = r.DESCRIPTIONS[0].contains(___word);
				}
			}
			if (needFix) {
				boolean fix = false;
				for (String t : charStrings.NAMES) {
					if (msg.contains(t)) {
						fix = true;
						break;
					}
				}
				if (fix) {
					FontHelper.layout.setText(font, ___word);
					___curWidth[0] += FontHelper.layout.width;
					if (___curWidth[0] > widthMax) {
						___curWidth[0] = 0.0F;
						++___currentLine[0];
						font.draw(sb, ___word, x + ___curWidth[0], y - lineSpacing * ___currentLine[0]);
						___curWidth[0] = FontHelper.layout.width;
					} else {
						font.draw(sb, ___word, x + ___curWidth[0] - FontHelper.layout.width, y - lineSpacing * ___currentLine[0]);
					}
				}
			}
		}

		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
				Matcher finalMatcher = new Matcher.MethodCallMatcher(FontHelper.class, "identifyOrb");
				return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
			}
		}
	}
}
