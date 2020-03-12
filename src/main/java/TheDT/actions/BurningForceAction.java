package TheDT.actions;

import TheDT.DTModMain;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;

public class BurningForceAction extends AbstractGameAction {
	private DamageInfo info;

	public BurningForceAction(AbstractCreature target, DamageInfo info, AttackEffect effect) {
		this.setValues(target, info);
		this.info = info;
		this.actionType = ActionType.WAIT;
		this.attackEffect = effect;
		this.duration = Settings.ACTION_DUR_FAST;
	}

	public void update() {
		isDone = true;
		for (int i = 0; i < DTModMain.burnGen; i++) {
			addToTop(new DamageAction(target, info, attackEffect));
		}
	}
}
