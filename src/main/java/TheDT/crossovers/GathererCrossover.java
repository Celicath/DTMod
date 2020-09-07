package TheDT.crossovers;

import TheDT.potions.LesserBlazePotion;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.PotionSlot;
import the_gatherer.GathererMod;
import the_gatherer.potions.SackPotion;
import the_gatherer.powers.BomberFormPower;

public class GathererCrossover {
	public static AbstractGameAction newFierySynthesisAction() {
		return new AbstractGameAction() {
			@Override
			public void update() {
				convertSackPotions(new LesserBlazePotion());
				isDone = true;
			}
		};
	}

	public static void convertSackPotions(SackPotion potion) {
		for (int i = 0; i < GathererMod.potionSack.potions.size(); i++) {
			if (!(AbstractDungeon.player != null && AbstractDungeon.player.hasPower(BomberFormPower.POWER_ID)) &&
					!(GathererMod.potionSack.potions.get(i) instanceof PotionSlot)) {
				SackPotion sp = (SackPotion) potion.makeCopy();
				GathererMod.potionSack.setPotion(i, sp);
				sp.flash();
			}
		}
	}
}
