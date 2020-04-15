package TheDT.actions;

import TheDT.characters.DragonTamer;
import com.megacrit.cardcrawl.actions.AbstractGameAction;

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
	}
}
