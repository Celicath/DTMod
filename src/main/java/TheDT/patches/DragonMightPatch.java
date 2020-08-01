package TheDT.patches;

import TheDT.DTModMain;
import TheDT.characters.Dragon;
import TheDT.characters.DragonTamer;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.TextAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.BeatOfDeathPower;
import com.megacrit.cardcrawl.powers.SharpHidePower;
import com.megacrit.cardcrawl.powers.SporeCloudPower;

import java.util.TreeSet;

public class DragonMightPatch {
	public static final String DESCRIPTION_SUFFIX = CardCrawlGame.languagePack.getPowerStrings(DTModMain.makeID("DragonMightPower")).DESCRIPTIONS[0];
	public static TreeSet<AbstractPower> triggered = new TreeSet<>();

	@SpirePatch(clz = SporeCloudPower.class, method = "updateDescription")
	@SpirePatch(clz = SharpHidePower.class, method = "updateDescription")
	@SpirePatch(clz = BeatOfDeathPower.class, method = "updateDescription")
	public static class UpdateDescriptionPatch {
		@SpirePostfixPatch
		public static void Postfix(AbstractPower __instance) {
			if (AbstractDungeon.player instanceof DragonTamer || AbstractDungeon.player instanceof Dragon) {
				__instance.description += DESCRIPTION_SUFFIX;
			}
		}
	}

	@SpirePatch(clz = BeatOfDeathPower.class, method = SpirePatch.CONSTRUCTOR)
	public static class DescriptionBadConstructorPatch {
		@SpirePostfixPatch
		public static void Postfix(BeatOfDeathPower __instance, AbstractCreature owner, int amount) {
			if (AbstractDungeon.player instanceof DragonTamer || AbstractDungeon.player instanceof Dragon) {
				__instance.description += DESCRIPTION_SUFFIX;
			}
		}
	}

	@SpirePatch(clz = SporeCloudPower.class, method = "onDeath")
	public static class SporeCloudPowerImmunePatch {
		@SpirePostfixPatch
		public static void Postfix(SporeCloudPower __instance) {
			showImmune(__instance);
		}
	}

	@SpirePatch(clz = SharpHidePower.class, method = "onUseCard")
	public static class SharpHidePowerImmunePatch {
		@SpirePostfixPatch
		public static void Postfix(SharpHidePower __instance, AbstractCard card, UseCardAction action) {
			showImmune(__instance);
		}
	}

	@SpirePatch(clz = BeatOfDeathPower.class, method = "onAfterUseCard")
	public static class BeatOfDeathPowerImmunePatch {
		@SpirePostfixPatch
		public static void Postfix(BeatOfDeathPower __instance, AbstractCard card, UseCardAction action) {
			showImmune(__instance);
		}
	}

	public static void showImmune(AbstractPower p) {
		if (!triggered.contains(p)) {
			triggered.add(p);
			Dragon d = DragonTamer.getLivingDragon();
			if (AbstractDungeon.player instanceof Dragon) {
				d = (Dragon) AbstractDungeon.player;
			}
			if (d != null) {
				AbstractDungeon.actionManager.addToTop(new TextAboveCreatureAction(d, ApplyPowerAction.TEXT[1]));
			}
		}
	}
}
