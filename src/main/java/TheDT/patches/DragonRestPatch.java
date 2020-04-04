package TheDT.patches;

import TheDT.DTModMain;
import TheDT.characters.Dragon;
import TheDT.characters.DragonTamer;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.ui.campfire.RestOption;
import com.megacrit.cardcrawl.vfx.campfire.CampfireSleepEffect;
import javassist.CtBehavior;

public class DragonRestPatch {
	public static final String[] TEXT = CardCrawlGame.languagePack.getUIString(DTModMain.makeID("RestOption")).TEXT;

	public static int getHealAmount(Dragon dragon) {
		return (int) (dragon.maxHealth * 0.3F);
	}

	@SpirePatch(clz = RestOption.class, method = "updateUsability")
	public static class RestOptionDescriptionPatch {
		@SpirePrefixPatch
		public static void Prefix(RestOption __instance, boolean canUse, @ByRef String[] ___description) {
			if (AbstractDungeon.player instanceof DragonTamer) {
				Dragon dragon = ((DragonTamer) AbstractDungeon.player).dragon;
				___description[0] = ___description[0].replace('\n', ' ');
				___description[0] += "\n" + TEXT[0] + getHealAmount(dragon) + ")" + LocalizedStrings.PERIOD;
			}
		}
	}

	@SpirePatch(clz = CampfireSleepEffect.class, method = "update")
	public static class CampfireDragonHealPatch {
		@SpireInsertPatch(locator = PlayerHealLocator.class)
		public static void Insert(CampfireSleepEffect __instance) {
			if (AbstractDungeon.player instanceof DragonTamer) {
				Dragon dragon = ((DragonTamer) AbstractDungeon.player).dragon;
				dragon.heal(getHealAmount(dragon), false);
			}
		}
	}

	private static class PlayerHealLocator extends SpireInsertLocator {
		@Override
		public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
			Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "heal");
			return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
		}
	}
}
