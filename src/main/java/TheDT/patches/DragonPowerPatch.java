package TheDT.patches;

import TheDT.cards.AbstractDTCard;
import TheDT.characters.Dragon;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class DragonPowerPatch {
	@SpirePatch(clz = AbstractRoom.class, method = "applyEndOfTurnPreCardPowers")
	public static class DragonStartTurn {
		@SpirePostfixPatch
		public static void Postfix(AbstractRoom __instance) {
			Dragon dragon = AbstractDTCard.getDragon();
			if (dragon != null) {
				for (AbstractPower p : dragon.powers) {
					p.atEndOfTurnPreEndTurnCards(true);
				}
			}
		}
	}
}
