package TheDT.patches;

import TheDT.characters.TheDT;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.AbstractStance;
import javassist.CtBehavior;

public class MonsterDamagePatch {
	public static AbstractPlayer prevPlayer = null;

	@SpirePatch(clz = AbstractMonster.class, method = "applyPowers")
	public static class ChangeTargetPatch {
		@SpirePrefixPatch
		public static void Prefix(AbstractMonster __instance) {
			if (AbstractDungeon.player instanceof TheDT && ((TheDT) AbstractDungeon.player).isCurrentTargetDragon(__instance)) {
				prevPlayer = AbstractDungeon.player;
				AbstractDungeon.player = ((TheDT) AbstractDungeon.player).dragon;
			}
		}

		@SpirePostfixPatch
		public static void Postfix(AbstractMonster __instance) {
			if (prevPlayer != null) {
				AbstractDungeon.player = prevPlayer;
				prevPlayer = null;
			}
		}
	}
}
