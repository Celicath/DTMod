package TheDT.patches;

import TheDT.DTModMain;
import TheDT.characters.Dragon;
import TheDT.characters.DragonTamer;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Ginger;
import com.megacrit.cardcrawl.relics.OddMushroom;
import com.megacrit.cardcrawl.relics.Turnip;

import java.util.Arrays;
import java.util.HashSet;

public class DragonRelicPatch {
	private static final String[] TEXT = CardCrawlGame.languagePack.getUIString(DTModMain.makeID("DragonRelicDescription")).TEXT;
	public static final HashSet<String> relicPatchlist = new HashSet<>(Arrays.asList(
		Ginger.ID,
		Turnip.ID,
		OddMushroom.ID
	));

	public static void addTipsToRelics(AbstractRelic relic) {
		if (AbstractDungeon.player instanceof DragonTamer || AbstractDungeon.player instanceof Dragon) {
			if (relicPatchlist.contains(relic.relicId)) {
				relic.tips.add(new PowerTip(TEXT[0], TEXT[1]));
			}
		}
	}

	@SpirePatch(clz = AbstractRelic.class, method = "initializeTips")
	public static class RelicTipPatch {
		@SpirePostfixPatch
		public static void Postfix(AbstractRelic __instance) {
			addTipsToRelics(__instance);
		}
	}


	@SpirePatch(clz = AbstractPlayer.class, method = "hasRelic")
	public static class HasRelicPatch {
		@SpirePostfixPatch
		public static boolean Postfix(boolean __result, AbstractPlayer __instance, String targetID) {
			if (!__result && relicPatchlist.contains(targetID) && __instance instanceof Dragon) {
				return ((Dragon) __instance).master.hasRelic(targetID);
			} else {
				return __result;
			}
		}
	}
}
