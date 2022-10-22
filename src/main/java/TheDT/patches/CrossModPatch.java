package TheDT.patches;

import TheDT.characters.Dragon;
import TheDT.characters.DragonTamer;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import javassist.CtBehavior;

public class CrossModPatch {
	@SpirePatch(cls = "com.evacipated.cardcrawl.mod.hubris.actions.monsterOrbs.MonsterLightningOrbEvokeAction", method = "update", optional = true)
	public static class HubrisSneckoLightningOrbPatch {
		@SpireInsertPatch(locator = HubrisSneckoLightningOrbLocator.class)
		public static void Insert(AbstractGameAction __instance, AbstractMonster ___owner, @ByRef AbstractPlayer[] ___p) {
			AbstractCreature c = DragonTamer.getCurrentTarget(___owner);
			if (c instanceof AbstractMonster) {
				MonsterTargetPatch.redirectTarget = c;
			} else if (AbstractDungeon.player instanceof DragonTamer && c instanceof Dragon) {
				___p[0] = (AbstractPlayer) c;
			}
		}

		public static class HubrisSneckoLightningOrbLocator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
				Matcher finalMatcher = new Matcher.NewExprMatcher(DamageAction.class);
				return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
			}
		}
	}

	@SpirePatch(cls = "com.evacipated.cardcrawl.mod.hubris.orbs.monster.MonsterMiasma", method = "onEvoke", optional = true)
	public static class HubrisSneckoMiasmaOrbPatch {
		@SpireInsertPatch(locator = HubrisSneckoMiasmaOrbLocator.class)
		public static void Insert(AbstractOrb __instance, AbstractMonster ___owner, @ByRef AbstractPlayer[] ___p) {
			AbstractCreature c = DragonTamer.getCurrentTarget(___owner);
			if (c instanceof AbstractMonster) {
				MonsterTargetPatch.redirectTarget = c;
			} else if (AbstractDungeon.player instanceof DragonTamer && c instanceof Dragon) {
				___p[0] = (AbstractPlayer) c;
			}
		}

		@SpirePostfixPatch
		public static void Postfix(AbstractOrb __instance) {
			MonsterTargetPatch.redirectTarget = null;
		}

		public static class HubrisSneckoMiasmaOrbLocator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
				Matcher finalMatcher = new Matcher.NewExprMatcher(ApplyPowerAction.class);
				return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
			}
		}
	}

	@SpirePatch(cls = "Gensokyo.monsters.act2.Tenshi", method = "takeSecondTurn", optional = true)
	public static class GensokyoTenshiPatch {
		public static AbstractPlayer prevPlayer = null;

		@SpirePrefixPatch
		public static void Prefix(AbstractMonster __instance) {
			AbstractCreature c = DragonTamer.getCurrentTarget(__instance);
			if (c instanceof AbstractMonster) {
				MonsterTargetPatch.redirectTarget = c;
			} else if (AbstractDungeon.player instanceof DragonTamer && c instanceof Dragon) {
				if (prevPlayer != null) {
					System.out.println("BUG - GensokyoTenshiPatch!");
				}
				prevPlayer = AbstractDungeon.player;
				AbstractDungeon.player = (AbstractPlayer) c;
			}
		}

		@SpirePostfixPatch
		public static void Postfix(AbstractMonster __instance) {
			if (prevPlayer != null) {
				AbstractDungeon.player = prevPlayer;
				prevPlayer = null;
			}
			MonsterTargetPatch.redirectTarget = null;
		}
	}

	@SpirePatch(cls = "patches.PatchStiletto", method = "Insert", optional = true)
	public static class ReliquaryStilettoPatch {
		@SpirePrefixPatch
		public static SpireReturn<Void> Prefix(AbstractCreature _____instance) {
			if (_____instance == DragonTamer.getLivingDragon()) {
				return SpireReturn.Return();
			} else {
				return SpireReturn.Continue();
			}
		}
	}
}
