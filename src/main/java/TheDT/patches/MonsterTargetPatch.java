package TheDT.patches;

import TheDT.characters.Dragon;
import TheDT.characters.DragonTamer;
import TheDT.powers.TauntPower;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import javassist.CtBehavior;

public class MonsterTargetPatch {
	public static AbstractPlayer prevPlayer = null;

	public static AbstractCreature redirectTarget = null;

	@SpirePatch(clz = GameActionManager.class, method = "getNextAction")
	public static class ChangeTargetPatch {
		@SpireInsertPatch(locator = BeforeTakeTurnLocator.class)
		public static void Insert(GameActionManager __instance, AbstractMonster ___m) {
			AbstractCreature target = DragonTamer.getCurrentTarget(___m);

			AbstractPower p = ___m.getPower(TauntPower.POWER_ID);
			if (p != null) {
				p.flashWithoutSound();
			}

			if (target instanceof AbstractMonster) {
				redirectTarget = target;
			} else if (AbstractDungeon.player instanceof DragonTamer && target instanceof Dragon) {
				if (prevPlayer != null) {
					System.out.println("BUG - MonsterTargetPatch!");
				}
				prevPlayer = AbstractDungeon.player;
				AbstractDungeon.player = ((DragonTamer) AbstractDungeon.player).dragon;
			}
		}

		@SpirePostfixPatch
		public static void Postfix(GameActionManager __instance) {
			if (prevPlayer != null) {
				AbstractDungeon.player = prevPlayer;
				prevPlayer = null;
			}
			redirectTarget = null;
		}
	}

	private static class BeforeTakeTurnLocator extends SpireInsertLocator {
		@Override
		public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
			Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractMonster.class, "takeTurn");
			return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
		}
	}

	@SpirePatch(clz = GameActionManager.class, method = "addToBottom")
	@SpirePatch(clz = GameActionManager.class, method = "addToTop")
	public static class ForceRedirectPatch {
		@SpirePrefixPatch
		public static void Prefix(GameActionManager __instance, @ByRef AbstractGameAction[] action) {
			if (redirectTarget != null) {
				if (action[0].target == AbstractDungeon.player) {
					action[0].target = redirectTarget;

					if (action[0] instanceof ApplyPowerAction) {
						action[0] = new ApplyPowerAction(redirectTarget, action[0].source, new VulnerablePower(redirectTarget, 1, true), 1);
					}
				}
			}
		}
	}
}
