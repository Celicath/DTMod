package TheDT.patches;

import TheDT.characters.TheDT;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.ArrayList;

public class MonsterIntentPatch {
	public static ArrayList<AbstractPower> tmpPowers = null;

	@SpirePatch(clz = AbstractMonster.class, method = "calculateDamage")
	public static class CalculateDamagePrefix {
		@SpirePrefixPatch
		public static void Prefix(AbstractMonster __instance, int dmg) {
			if (AbstractDungeon.player instanceof TheDT && ((TheDT) AbstractDungeon.player).target == ((TheDT) AbstractDungeon.player).dragon) {
				tmpPowers = AbstractDungeon.player.powers;
				AbstractDungeon.player.powers = ((TheDT) AbstractDungeon.player).dragon.powers;
			}
		}
	}

	@SpirePatch(clz = AbstractMonster.class, method = "calculateDamage")
	public static class CalculateDamagePostfix {
		@SpirePostfixPatch
		public static void Postfix(AbstractMonster __instance, int dmg) {
			if (tmpPowers != null) {
				AbstractDungeon.player.powers = tmpPowers;
				tmpPowers = null;
			}
		}
	}
}
