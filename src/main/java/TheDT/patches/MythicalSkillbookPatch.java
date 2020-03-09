package TheDT.patches;

import TheDT.DTModMain;
import TheDT.powers.BondingPower;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CtBehavior;

import java.util.HashMap;
import java.util.HashSet;

public class MythicalSkillbookPatch {
	public static HashMap<AbstractGameAction, AbstractCard> actionCardMap = new HashMap<>();
	public static HashSet<AbstractGameAction> badActionList = new HashSet<>();

	@SpirePatch(clz = AbstractPlayer.class, method = "useCard")
	public static class MarkActionPatch {
		@SpirePrefixPatch
		public static void Prefix(AbstractPlayer __instance, AbstractCard c, AbstractMonster monster, int energyOnUse) {
			badActionList.clear();
			if (AbstractDungeon.player.hasRelic(DTModMain.MythicalSkillbookID)) {
				badActionList.addAll(AbstractDungeon.actionManager.actions);
			}
		}

		@SpireInsertPatch(locator = UseCardLocator.class)
		public static void Insert(AbstractPlayer __instance, AbstractCard c, AbstractMonster monster, int energyOnUse) {
			if (AbstractDungeon.player.hasRelic(DTModMain.MythicalSkillbookID)) {
				DTModMain.logger.debug("Current card : " + c.name);

				int prev = AbstractDungeon.actionManager.actions.size();
				int count = 0;
				for (AbstractGameAction action : AbstractDungeon.actionManager.actions) {
					if (!badActionList.contains(action)) {
						count++;
						actionCardMap.put(action, c);
					}
				}
				DTModMain.logger.debug("action count = " + prev + " - " + badActionList.size() + " = " + count);
			}
		}
	}

	private static class UseCardLocator extends SpireInsertLocator {
		@Override
		public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
			Matcher finalMatcher = new Matcher.NewExprMatcher(UseCardAction.class);
			return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
		}
	}

	@SpirePatch(clz = GameActionManager.class, method = "getNextAction")
	public static class ActionStartPatch {
		@SpireInsertPatch(locator = ActionStartLocator.class)
		public static void Insert(GameActionManager __instance) {
			if (actionCardMap.containsKey(__instance.currentAction)) {
				MythicalGameState.save(actionCardMap.get(__instance.currentAction));
			}
		}
	}

	private static class ActionStartLocator extends SpireInsertLocator {
		@Override
		public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
			Matcher finalMatcher = new Matcher.FieldAccessMatcher(GameActionManager.class, "phase");
			return LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
		}
	}

	@SpirePatch(clz = GameActionManager.class, method = "update")
	public static class ActionFinishPatch {
		@SpireInsertPatch(locator = ActionFinishLocator.class)
		public static void Insert(GameActionManager __instance) {
			if (actionCardMap.containsKey(__instance.currentAction)) {
				AbstractCard c = actionCardMap.get(__instance.currentAction);
				actionCardMap.remove(__instance.currentAction);

				if (MythicalGameState.checkDiff(c, __instance.currentAction)) {
					if (AbstractDungeon.player.hasRelic(DTModMain.MythicalSkillbookID)) {
						DTModMain.logger.info("Current card [" + c.name + "] IS a Bonding card.");
						AbstractDungeon.player.getRelic(DTModMain.MythicalSkillbookID).flash();
						AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(
								AbstractDungeon.player,
								AbstractDungeon.player,
								new BondingPower(AbstractDungeon.player, AbstractDungeon.player, 1),
								1
						));
					}
				}
			}
		}
	}

	private static class ActionFinishLocator extends SpireInsertLocator {
		@Override
		public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
			Matcher finalMatcher = new Matcher.FieldAccessMatcher(GameActionManager.class, "previousAction");
			return LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
		}
	}
}
