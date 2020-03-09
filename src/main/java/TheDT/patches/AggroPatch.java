package TheDT.patches;

import TheDT.DTModMain;
import TheDT.characters.TheDT;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class AggroPatch {
	@SpirePatch(clz = AbstractMonster.class, method = "damage")
	public static class DamagePatch {
		private static AbstractGameAction prevAction = null;

		@SpirePostfixPatch
		public static void Postfix(AbstractMonster __instance, DamageInfo info) {
			if (AbstractDungeon.player instanceof TheDT && info.type != DamageInfo.DamageType.THORNS && info.type != DamageInfo.DamageType.HP_LOSS) {
				if (prevAction != AbstractDungeon.actionManager.currentAction) {
					if (info.owner == AbstractDungeon.player) {
						DTModMain.yourAggro++;
					} else if (info.owner == ((TheDT) AbstractDungeon.player).dragon) {
						DTModMain.dragonAggro++;
					}
				}
				prevAction = AbstractDungeon.actionManager.currentAction;
			}
		}
	}
}