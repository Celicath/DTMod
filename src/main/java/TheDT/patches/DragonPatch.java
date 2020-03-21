package TheDT.patches;

import TheDT.characters.Dragon;
import TheDT.characters.DragonTamer;
import TheDT.powers.SafeguardPower;
import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.BarricadePower;
import com.megacrit.cardcrawl.powers.BlurPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import javassist.CtBehavior;

public class DragonPatch {
	@SpirePatch(clz = GameActionManager.class, method = "getNextAction")
	public static class DragonStartTurn {
		@SpireInsertPatch(locator = StartTurnLocator.class)
		public static void Insert(GameActionManager __instance) {
			if (AbstractDungeon.player instanceof DragonTamer) {
				Dragon dragon = ((DragonTamer) AbstractDungeon.player).dragon;
				if ((!dragon.hasPower(BarricadePower.POWER_ID)) && (!dragon.hasPower(BlurPower.POWER_ID))) {
					dragon.loseBlock();
				}
			}
		}
	}

	private static class StartTurnLocator extends SpireInsertLocator {
		@Override
		public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
			Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "hasPower");
			return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
		}
	}

	@SpirePatch(clz = AbstractRoom.class, method = "endBattle")
	public static class DragonBattleEnd {
		public static void Prefix(AbstractRoom __instance) {
			if (AbstractDungeon.player instanceof DragonTamer) {
				Dragon dragon = ((DragonTamer) AbstractDungeon.player).dragon;
				TempHPField.tempHp.set(dragon, 0);
				if (dragon.isDying) {
					dragon.heal(1);
					dragon.isDying = false;
					dragon.isDead = false;
				}
			}
		}
	}
}
