package TheDT.actions;

import TheDT.DTModMain;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;

public class AddAggroAction extends AbstractGameAction {
	boolean isDragon;

	public AddAggroAction(boolean isDragon, int delta) {
		actionType = AbstractGameAction.ActionType.WAIT;
		duration = Settings.ACTION_DUR_FAST;
		this.isDragon = isDragon;
		amount = delta;
	}

	public void update() {
		this.isDone = true;
		if (FreezeAggroAction.frozen) {
			return;
		}
		if (isDragon) {
			DTModMain.dragonAggro += amount;
		} else {
			DTModMain.yourAggro += amount;
		}
	}
}
