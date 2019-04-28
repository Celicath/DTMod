package TheDT.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.RestRoom;

import static TheDT.patches.CustomTags.DT_FORBIDDEN;

public class ForbiddenStrikePatch {
	@SpirePatch(clz = CardGroup.class, method = "getUpgradableCards")
	public static class SendPostPatch {
		@SpirePostfixPatch
		public static CardGroup Postfix(CardGroup __result, CardGroup __instance) {
			if (AbstractDungeon.getCurrRoom() instanceof RestRoom) {
				boolean hasForbidden = false;
				for (AbstractCard c : __result.group) {
					if (c.hasTag(DT_FORBIDDEN)) {
						hasForbidden = true;
						break;
					}
				}
				if (hasForbidden) {
					__result.group.removeIf(c -> !c.hasTag(DT_FORBIDDEN));
				}
			}
			return __result;
		}
	}
}
