package TheDT.patches;

import TheDT.DTMod;
import TheDT.powers.BondingPower;
import TheDT.relics.MythicalSkillbook;
import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CtBehavior;

import java.util.ArrayList;
import java.util.HashSet;

public class MythicalSkillbookPatch {
	public static HashSet<AbstractGameAction> actionList = new HashSet<>();
	public static HashSet<AbstractGameAction> badActionList = new HashSet<>();

	@SpirePatch(clz = AbstractPlayer.class, method = "useCard")
	public static class MarkActionPatch {
		@SpirePrefixPatch
		public static void Prefix(AbstractPlayer __instance, AbstractCard c, AbstractMonster monster, int energyOnUse) {
			badActionList.clear();
			MythicalGameState.reset();
			if (AbstractDungeon.player.hasRelic(MythicalSkillbook.ID)) {
				badActionList.addAll(AbstractDungeon.actionManager.actions);
			}
		}

		@SpireInsertPatch(locator = UseCardLocator.class)
		public static void Insert(AbstractPlayer __instance, AbstractCard c, AbstractMonster monster, int energyOnUse) {
			actionList.clear();
			MythicalGameState.reset();
			if (AbstractDungeon.player.hasRelic(MythicalSkillbook.ID)) {
				actionList.addAll(AbstractDungeon.actionManager.actions);

				DTMod.logger.debug("Current card : " + c.name);

				int prev = actionList.size();
				actionList.removeAll(badActionList);
				DTMod.logger.debug("action count = " + prev + " - " + badActionList.size() + " = " + actionList.size());
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
			if (actionList.contains(__instance.currentAction)) {
				MythicalGameState.save();
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
			if (actionList.contains(__instance.currentAction)) {
				actionList.remove(__instance.currentAction);

				if (MythicalGameState.checkDiff()) {
					DTMod.logger.debug("Current card IS a Bonding card.");
					if (AbstractDungeon.player.hasRelic(MythicalSkillbook.ID)) {
						AbstractDungeon.player.getRelic(MythicalSkillbook.ID).flash();
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
