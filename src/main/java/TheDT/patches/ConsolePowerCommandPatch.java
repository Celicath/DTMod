package TheDT.patches;

import TheDT.characters.DragonTamer;
import basemod.ConsoleTargetedPower;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import javassist.CtBehavior;

public class ConsolePowerCommandPatch {
	@SpirePatch(clz = ConsoleTargetedPower.class, method = "updateTargetMode")
	public static class ConsoleTargetDragonPatch {
		@SpireInsertPatch(locator = Locator.class)
		public static void Insert(ConsoleTargetedPower __instance, @ByRef AbstractCreature[] ___hoveredCreature) {
			if (AbstractDungeon.player instanceof DragonTamer) {
				AbstractCreature m = ((DragonTamer) AbstractDungeon.player).dragon;
				if (m.hb.hovered && !m.isDead) {
					___hoveredCreature[0] = m;
				}
			}
		}
	}

	private static class Locator extends SpireInsertLocator {
		@Override
		public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
			Matcher finalMatcher = new Matcher.FieldAccessMatcher(InputHelper.class, "justClickedLeft");
			return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
		}
	}
}
