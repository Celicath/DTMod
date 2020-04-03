package TheDT.patches;

import TheDT.cards.AbstractDTCard;
import TheDT.characters.Dragon;
import TheDT.characters.DragonTamer;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import javassist.CtBehavior;

public class DragonPowerPatch {
	@SpirePatch(clz = AbstractRoom.class, method = "applyEndOfTurnPreCardPowers")
	public static class DragonStartTurnPower {
		@SpirePostfixPatch
		public static void Postfix(AbstractRoom __instance) {
			Dragon dragon = DragonTamer.getLivingDragon();
			if (dragon != null) {
				for (AbstractPower p : dragon.powers) {
					p.atEndOfTurnPreEndTurnCards(true);
				}
			}
		}
	}

	@SpirePatch(clz = AbstractPlayer.class, method = "draw", paramtypez = int.class)
	public static class DragonDrawPower {
		@SpireInsertPatch(locator = DrawPowerLocator.class)
		public static void Insert(AbstractPlayer __instance, int numCards, AbstractCard ___c) {
			Dragon dragon = DragonTamer.getLivingDragon();
			if (dragon != null) {
				for (AbstractPower p : dragon.powers) {
					p.onCardDraw(___c);
				}
			}
		}
	}

	public static class DrawPowerLocator extends SpireInsertLocator {
		@Override
		public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
			Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "powers");
			return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
		}
	}
}
