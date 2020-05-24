package TheDT.patches;

import TheDT.cards.ForbiddenStrike;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.RestRoom;

public class ForbiddenStrikePatch {
	@SpirePatch(clz = CardGroup.class, method = "getUpgradableCards")
	public static class SendPostPatch {
		@SpirePostfixPatch
		public static CardGroup Postfix(CardGroup __result, CardGroup __instance) {
			if (AbstractDungeon.getCurrRoom() instanceof RestRoom) {
				int forbiddenLevel = 0;
				for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
					if (c instanceof ForbiddenStrike) {
						forbiddenLevel = 2;
					}
				}
				if (forbiddenLevel == 2) {
					__result.group.removeIf(c -> !(c instanceof ForbiddenStrike));
				} else if (forbiddenLevel == 1) {
					__result.group.removeIf(c -> !c.hasTag(AbstractCard.CardTags.STRIKE));
				}
			}
			return __result;
		}
	}
}
