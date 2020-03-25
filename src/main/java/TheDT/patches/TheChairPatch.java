package TheDT.patches;

import TheDT.relics.TheChair;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class TheChairPatch {
	@SpirePatch(clz = CardGroup.class, method = "initializeDeck")
	public static class InitializeDeckPatch {
		public static int prevHandSize = -1;

		@SpirePrefixPatch
		public static void Prefix(CardGroup __instance, CardGroup masterDeck) {
			if (AbstractDungeon.player.hasRelic(TheChair.ID)) {
				prevHandSize = AbstractDungeon.player.masterHandSize;
				AbstractDungeon.player.masterHandSize = 0;
			}
		}

		@SpirePostfixPatch
		public static void Postfix(CardGroup __instance, CardGroup masterDeck) {
			if (prevHandSize != -1) {
				AbstractDungeon.player.masterHandSize = prevHandSize;
				prevHandSize = -1;
			}
		}
	}
}
