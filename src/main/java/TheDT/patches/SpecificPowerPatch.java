package TheDT.patches;

import TheDT.characters.Dragon;
import TheDT.characters.DragonTamer;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.colorless.SadisticNature;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.SadisticPower;

public class SpecificPowerPatch {
	@SpirePatch(clz = SadisticNature.class, method = "use")
	public static class SadisticNaturePatch {
		@SpirePostfixPatch
		public static void Postfix(SadisticNature __instance, AbstractPlayer p, AbstractMonster m, int ___magicNumber) {
			Dragon d = DragonTamer.getLivingDragon();
			if (d != null) {
				AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(d, d, new SadisticPower(d, ___magicNumber), ___magicNumber));
			}
		}
	}
}
