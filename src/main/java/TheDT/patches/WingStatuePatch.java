package TheDT.patches;

import TheDT.cards.AbstractDTCard;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;

public class WingStatuePatch {
	@SpirePatch(clz = CardHelper.class, method = "hasCardWithXDamage")
	public static class DragonStartTurn {
		@SpirePostfixPatch
		public static boolean Postfix(boolean __result, int damage) {
			if (!__result) {
				for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
					if (c instanceof AbstractDTCard && c.type == AbstractCard.CardType.ATTACK && ((AbstractDTCard) c).dtBaseDragonDamage >= damage) {
						return true;
					}
				}
			}
			return __result;
		}
	}
}
