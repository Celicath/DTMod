package TheDT.patches;

import TheDT.actions.AddAggroAction;
import TheDT.characters.TheDT;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class AggroPatch {
	@SpirePatch(clz = AbstractMonster.class, method = "damage")
	public static class DamagePatch {
		@SpirePostfixPatch
		public static void Postfix(AbstractMonster __instance, DamageInfo info) {
			if (AbstractDungeon.player instanceof TheDT && info.type != DamageInfo.DamageType.THORNS && info.type != DamageInfo.DamageType.HP_LOSS) {
				if (info.owner == AbstractDungeon.player) {
					AbstractDungeon.actionManager.addToTop(new AddAggroAction(false, 1));
				} else if (info.owner == ((TheDT) AbstractDungeon.player).dragon) {
					AbstractDungeon.actionManager.addToTop(new AddAggroAction(true, 1));
				}
			}
		}
	}
}