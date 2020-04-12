package TheDT.actions;

import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.HealEffect;

public class FastAddTemporaryHPAction extends AbstractGameAction {
	public FastAddTemporaryHPAction(AbstractCreature target, AbstractCreature source, int amount) {
		setValues(target, source, amount);
		actionType = ActionType.HEAL;
		duration = Settings.ACTION_DUR_XFAST;
	}

	public void update() {
		if (duration == Settings.ACTION_DUR_XFAST) {
			TempHPField.tempHp.set(target, TempHPField.tempHp.get(target) + amount);
			if (amount > 0) {
				AbstractDungeon.effectsQueue.add(new HealEffect(target.hb.cX - target.animX, target.hb.cY, amount));
				target.healthBarUpdatedEvent();
			}
		}

		tickDuration();
	}
}
