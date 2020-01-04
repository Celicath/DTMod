package TheDT.patches;

import TheDT.characters.TheDT;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CtBehavior;

import java.util.ArrayList;

public class MonsterIntentPatch {
	@SpirePatch(clz = AbstractMonster.class, method = "calculateDamage")
	public static class CalculateDamageChange {
		@SpireInsertPatch(locator = TargetChangeLocator.class, localvars = {"target"})
		public static void Insert(AbstractMonster __instance, int dmg, @ByRef AbstractPlayer[] target) {
			if (AbstractDungeon.player instanceof TheDT && ((TheDT) AbstractDungeon.player).front == ((TheDT) AbstractDungeon.player).dragon) {
				target[0] = ((TheDT) AbstractDungeon.player).dragon;
			}
		}
	}

	private static class TargetChangeLocator extends SpireInsertLocator {
		@Override
		public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
			Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "hasBlight");
			return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
		}
	}
}
