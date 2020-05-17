package TheDT.patches;

import TheDT.DTModMain;
import TheDT.characters.DragonTamer;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.BeatOfDeathPower;
import com.megacrit.cardcrawl.powers.SharpHidePower;
import com.megacrit.cardcrawl.powers.SporeCloudPower;

public class DragonMightPatch {
	public static final String DESCRIPTION_SUFFIX = CardCrawlGame.languagePack.getPowerStrings(DTModMain.makeID("DragonMightPower")).DESCRIPTIONS[0];

	@SpirePatch(clz = SporeCloudPower.class, method = "updateDescription")
	@SpirePatch(clz = SharpHidePower.class, method = "updateDescription")
	@SpirePatch(clz = BeatOfDeathPower.class, method = "updateDescription")
	public static class UpdateDescriptionPatch {
		@SpirePostfixPatch
		public static void Postfix(AbstractPower __instance) {
			if (AbstractDungeon.player instanceof DragonTamer) {
				__instance.description += DESCRIPTION_SUFFIX;
			}
		}
	}

	@SpirePatch(clz = BeatOfDeathPower.class, method = SpirePatch.CONSTRUCTOR)
	public static class BadConstructorPatch {
		@SpirePostfixPatch
		public static void Postfix(BeatOfDeathPower __instance, AbstractCreature owner, int amount) {
			if (AbstractDungeon.player instanceof DragonTamer) {
				__instance.description += DESCRIPTION_SUFFIX;
			}
		}
	}
}
