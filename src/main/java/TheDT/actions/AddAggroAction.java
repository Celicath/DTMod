package TheDT.actions;

import TheDT.DTModMain;
import TheDT.powers.AggroFixPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class AddAggroAction extends AbstractGameAction {
	boolean isDragon;

	public AddAggroAction(boolean isDragon, int delta) {
		actionType = AbstractGameAction.ActionType.WAIT;
		duration = Settings.ACTION_DUR_FAST;
		this.isDragon = isDragon;
		amount = delta;
	}

	public void update() {
		isDone = true;
		if (FreezeAggroAction.frozen) {
			return;
		}
		AbstractPower afp = AbstractDungeon.player.getPower(AggroFixPower.POWER_ID);
		if (afp != null) {
			afp.flash();
			return;
		}

		if (isDragon) {
			DTModMain.dragonAggro += amount;
		} else {
			DTModMain.yourAggro += amount;
		}
	}
}
