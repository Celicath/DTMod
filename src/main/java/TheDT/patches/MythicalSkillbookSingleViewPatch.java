package TheDT.patches;

import TheDT.DTMod;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.screens.SingleRelicViewPopup;

public class MythicalSkillbookSingleViewPatch {
	public static final String[] TEXT = CardCrawlGame.languagePack.getUIString(DTMod.makeID("JokeRelic")).TEXT;

	@SpirePatch(clz = SingleRelicViewPopup.class, method = "initializeLargeImg")
	public static class RarityLabelPatch {
		@SpireInsertPatch(rloc = 0, localvars = {"relic", "rarityLabel"})
		public static void Insert(SingleRelicViewPopup __instance, AbstractRelic relic, @ByRef String[] rarityLabel) {
			if (relic.relicId.equals(DTMod.MythicalSkillbookID)) {
				rarityLabel[0] = TEXT[0];
			}
		}
	}
}
