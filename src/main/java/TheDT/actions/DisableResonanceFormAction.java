package TheDT.actions;

import TheDT.powers.ResonanceFormPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;

public class DisableResonanceFormAction extends AbstractGameAction {
	public boolean disable;

	public DisableResonanceFormAction(boolean disable) {
		actionType = AbstractGameAction.ActionType.WAIT;
		duration = Settings.ACTION_DUR_FAST;
		this.disable = disable;
	}

	public void update() {
		this.isDone = true;
		ResonanceFormPower.disabledViaCard = disable;
	}
}
