package TheDT.patches;

import TheDT.cards.AbstractDTCard;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.screens.select.HandCardSelectScreen;
import javassist.CtBehavior;

import java.util.ArrayList;

public class DragonVariableColorPatch {
	@SpirePatch(clz = HandCardSelectScreen.class, method = "render")
	public static class HandCardSelectScreenPatch {
		@SpireInsertPatch(locator = Locator.class)
		public static void Insert(HandCardSelectScreen __instance, SpriteBatch sb) {
			if (__instance.upgradePreviewCard instanceof AbstractDTCard) {
				AbstractDTCard card = (AbstractDTCard) __instance.upgradePreviewCard;
				if (card.upgradedDTDragonDamage) {
					card.isDTDragonDamageModified = true;
				}
				if (card.upgradedDTDragonBlock) {
					card.isDTDragonBlockModified = true;
				}
			}
		}
	}

	private static class Locator extends SpireInsertLocator {
		@Override
		public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
			Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractCard.class, "isCostModified");
			ArrayList<Matcher> expectedMatches = new ArrayList<Matcher>() {
				{
					add(new Matcher.MethodCallMatcher(AbstractCard.class, "applyPowers"));
				}
			};
			return LineFinder.findInOrder(ctMethodToPatch, expectedMatches, finalMatcher);
		}
	}
}
