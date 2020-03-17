package TheDT.patches;

import TheDT.cards.AbstractDTCard;
import TheDT.characters.DragonTamer;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import javassist.CtBehavior;

public class SecondKeywordPatch {
	@SpirePatch(clz = AbstractCard.class, method = "initializeDescription")
	public static class ColorYouOrDragonPatch {
		@SpireInsertPatch(locator = TokenInitLocator.class)
		public static void Insert(AbstractCard __instance, @ByRef String[] ___word) {
			if (__instance instanceof AbstractDTCard) {
				for (String t : DragonTamer.charStrings.TEXT) {
					if (___word[0].equals(t)) {
						___word[0] = "[#60D0D0]" + t + "[]";
						return;
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