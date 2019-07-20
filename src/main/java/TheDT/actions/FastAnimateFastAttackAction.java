package TheDT.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;

public class FastAnimateFastAttackAction extends AbstractGameAction {

	public FastAnimateFastAttackAction(AbstractCreature owner) {
		this.setValues(null, owner, 0);
		this.duration = Settings.ACTION_DUR_FAST;
		this.actionType = ActionType.WAIT;
	}

	public void update() {
		source.useFastAttackAnimation();
		isDone = true;
	}
}
