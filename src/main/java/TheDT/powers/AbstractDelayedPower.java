package TheDT.powers;

import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;

public abstract class AbstractDelayedPower extends TwoAmountPower implements NonStackablePower {
	public AbstractDelayedPower() {

	}

	@Override
	public void atStartOfTurn() {
		this.addToBot(new ReducePowerAction(owner, owner, this, 1));
		if (amount == 1) {
			onActivate();
		}
	}

	public abstract void onActivate();
}
