package TheDT.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.PotionSlot;

public class ConvertPotionsAction extends AbstractGameAction {
	private AbstractPotion potion;

	public ConvertPotionsAction(AbstractPotion potion) {
		actionType = ActionType.SPECIAL;
		duration = Settings.ACTION_DUR_XFAST;
		this.potion = potion;
	}

	public void update() {
		if (duration == Settings.ACTION_DUR_XFAST) {
			for (int i = 0; i < AbstractDungeon.player.potionSlots; i++) {
				AbstractPotion p = AbstractDungeon.player.potions.get(i);
				if (!(p instanceof PotionSlot || p.getClass() == potion.getClass())) {
					AbstractPotion potionToObtain = potion.makeCopy();
					AbstractDungeon.player.obtainPotion(i, potionToObtain);
					potionToObtain.flash();
				}
			}
		}

		tickDuration();
	}
}
