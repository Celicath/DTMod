package TheDT.actions;

import TheDT.characters.DragonTamer;
import TheDT.powers.BondingPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;

public class DragonFaintAction extends AbstractGameAction {
	DragonTamer master;

	public DragonFaintAction(DragonTamer master) {
		actionType = ActionType.SPECIAL;
		this.master = master;
	}

	public void update() {
		isDone = true;
		master.setAggro(0);
		master.setFront(master);
		addToTop(new RemoveSpecificPowerAction(master, master, BondingPower.POWER_ID));
	}
}
