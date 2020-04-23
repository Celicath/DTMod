package TheDT.patches;

import TheDT.DTModMain;
import TheDT.cards.AbstractDTCard;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import javassist.CtBehavior;

public class SecondKeywordPatch {
	public static final CharacterStrings charStrings = CardCrawlGame.languagePack.getCharacterString(DTModMain.makeID("HighlightWords"));

	@SpirePatch(clz = AbstractCard.class, method = "initializeDescription")
	public static class ColorYouOrDragonPatch {
		@SpireInsertPatch(locator = TokenInitLocator.class)
		public static void Insert(AbstractCard __instance, @ByRef String[] ___word) {
			if (__instance instanceof AbstractDTCard) {
				for (String t : charStrings.NAMES) {
					if (___word[0].equals(t)) {
						___word[0] = "[#60D0D0]" + t + "[]";
						return;
					}
				}
				for (String t : charStrings.TEXT) {
					if (___word[0].equals(t)) {
						for (String p : charStrings.NAMES) {
							if (t.startsWith(p)) {
								___word[0] = "[#60D0D0]" + p + "[]" + t.substring(p.length());
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
}