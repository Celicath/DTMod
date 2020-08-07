package TheDT.patches;

import TheDT.relics.AncientBone;
import TheDT.relics.TheChair;
import basemod.BaseMod;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CtBehavior;

import java.util.ArrayList;

public class TheChairPatch {
	@SpirePatch(clz = CardGroup.class, method = "initializeDeck")
	public static class InitializeDeckPatch {
		public static int prevHandSize = -1;

		@SpireInsertPatch(locator = BeforeExtraDrwingLocator.class)
		public static void Insert(CardGroup __instance, CardGroup masterDeck, ArrayList<AbstractCard> ___placeOnTop) {
			if (AbstractDungeon.player.hasRelic(TheChair.ID)) {
				prevHandSize = AbstractDungeon.player.masterHandSize;
				AbstractDungeon.player.masterHandSize = 0;
				if (AbstractDungeon.player.hasRelic(AncientBone.ID) && ___placeOnTop.size() >= BaseMod.MAX_HAND_SIZE) {
					AbstractDungeon.player.masterHandSize = ___placeOnTop.size() - BaseMod.MAX_HAND_SIZE + 1;
				}
			}
		}

		private static class BeforeExtraDrwingLocator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
				Matcher finalMatcher = new Matcher.MethodCallMatcher(ArrayList.class, "size");
				return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
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
